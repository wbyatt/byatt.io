---
title: "The Control Plane Is a Control System"
subtitle: "a formal claim"
date: 2026-06-10T00:00:00-04:00
draft: false
---
The modern control plane is a control system. One might think that goes without saying, but what's critical here is that this claim has theoretical content. When I say "the control plane is a control system," this is not a metaphor (at least, no more than any other sentence is a metaphor). I'm claiming that the language, theory, and results of a century of control systems engineering make formal assertions about the systems we run, and, more to the point, that we can *use* those assertions to first explore and then constrain the design space when we build control planes.

## The vocabulary of control

First, let's establish what I mean by a control system. The vocabulary is blessedly short and finite. A control system has the following components:

- The **plant**: the system being governed.
- The **reference**: the description of what we want the plant to look like. This is the end goal, the desired state.
- One or more **sensors**: the instrumentation we have to know what reality currently looks like.
- The **error**: the measured difference between the reference and the sensor readings.
- The **controller**: the component that consumes the error and develops a plan for correction.
- The **actuator**: the component that applies the controller's corrections to the plant. This is the point at which governance concretely occurs.

This maps onto the control plane with surprisingly little effort. Every component exists, with clean correspondence:

| Control theory | Distributed systems |
|----------------|---------------------|
| plant          | data plane |
| reference      | desired state, manifests |
| sensor         | status reporting, health checks, watches, telemetry |
| error          | drift |
| controller     | reconciler, controller, operator |
| actuator       | mutating operations |

![](/images/control-plane-as-control-system/control-loop.svg)

This correspondence is structural, not incidental. A Kubernetes reconciler very literally computes an error term — desired state minus observed state — and selects an actuation intended to drive that error to zero. It is a negative feedback loop with exactly the same structure the block diagram above describes, and that block diagram is the object about which control theory makes, supports, and proves claims.

One difference worth noting, because rigor demands it: our control planes are sampled, discrete-time, nonlinear systems, and the controller usually has at best a crude model of the plant: we don't model the precise execution state and out-of-order CPU pipeline of every downstream dataplane. The elegant closed-form results of classical control broadly assume linearity and continuity we don't have. Some of the algebra and, more pointedly, the differential calculus may not always transfer. What does transfer, however, is the *taxonomy*: the enumeration of the ways feedback loops behave and the ways they fail, and the library of mechanisms whose stabilizing properties are formally understood. For software architects, those mechanisms and their properties are what we need to understand to build better systems and deliver them to our organizations.

## Modeling Problems

Any engineer or architect who has designed, built, or operated a control plane has encountered unstable control. This is the control failure where the system never reaches steady state. In the language of our control systems vocabulary, the plant never agrees with the reference, actual state never achieves desired state. Predicting instabilities can, at first, appear intractable, as though our only path is to aggressively exercise and monitor the control plane, wait for pathologies, and engineer them out.

However, if we appropriately model the control plane as a control system and apply controls-theoretic framing, we can get a robust enumeration of this problem space. This lets us rely on prior art to *get ahead* of failures: instead of designing against the happy path plus whatever failure modes we happen to imagine (a famously weak form of engineering imagination), we can take the known failure modes of feedback systems, ask what each one means in a control plane, and design against that.

This theory body gives us four primary destabilization mechanisms. That's four reasonably well-understood ways a control loop stops converging on its reference and starts generating incorrect, oscillating, or diverging corrections.

1. **Lag.** There is always non-zero latency between the control plane's observation of the data plane and the application of a change. Watch propagation, cache staleness, work queues, and the actuation itself all take real time. In control terms this is *dead time* in the loop, and dead time is poison: the controller is always correcting an error that existed some time ago. The canonical incident shape: an autoscaler observes high load and adds capacity; the capacity takes ninety seconds to become visible in the metrics; the autoscaler observes the *same* high load at the next tick and adds capacity again. The controller has amplified its own in-flight correction. Nothing malfunctioned. Unit tests passed. But the system is unstable.

2. **Overshoot.** This is what lag looks like carried to its conclusion and systematized: the controller builds a correction sized for the error it measured, not the error that will exist when the correction lands. By the time the actuation completes, the plant has partially recovered on its own, or earlier corrections have arrived, and the "correction" pushes the system past the reference in the other direction. Now there's an error of opposite sign, which produces an opposite correction, which also lands late. The system oscillates around its desired state — sometimes decaying toward convergence, sometimes sustaining, sometimes growing. When you watch a fleet scale up and down every few minutes under perfectly steady load, you are watching this figure:

![](/images/control-plane-as-control-system/lag-overshoot.svg)

3. **Noise.** In any sufficiently large system there is chaos in the sensors: network partitions, swapping latency, stop-the-world GC, blocking IO. Any of these can make a sensor report that a healthy component is failed, when the truthful reading is thirty seconds away. A controller that acts on every reading converts sensor noise directly into plant churn, and that churn is itself load, which produces more noisy readings. (Sensor noise in distributed systems is also more structured than we tend to assume: a sampled telemetry pipeline can manufacture clean-looking signals that don't exist in the plant at all. [I've written about exactly this](/posts/you-cant-stop-the-signal/) — aliasing is a specific structural manifestation of sensor noise.)

4. **Self-disturbance.** The control plane is not outside the system it governs; it is part of the same distributed system, sharing networks, disks, and API servers with the plant. So we have to ask: in what ways can the control plane perturb the very readings it depends on, or the plant's ground truth itself? Can it poll so aggressively that it degrades the API server serving its watches — a denial of service against its own sensors? Can a single reconciliation emit a change so large it saturates the disk or the network it shares with the workload? Classical control mostly gets to assume the controller is implemented on hardware separate from the plant. We don't, and the loops where the controller disturbs its own sensors are the ones that produce the weirdest incidents.

What's valuable here is that this enumeration is *prior* to any particular design. By modeling the control plane this way, we can identify the primary instability patterns of a system before we've built it — and just as importantly, recognize them in production for what they are: not novel emergencies, but known failure modes with names, signatures, and standard treatments.

## Formalizing Solutions

The taxonomy would be worth the price of admission on its own. Quite famously, the earlier in design we can catch errors, the cheaper they are to correct. But control theory also hands us at least some framework for the solution space: mechanisms whose stabilizing properties are formally understood and mechanically implementable. At least two of them are omnipresent in distributed systems and do an enormous amount of work, because we deploy both constantly without acknowledging that they are the same two ideas every time.

### Damping

A damped system is one that dissipates the energy of its own oscillation. In mechanical terms, it's the shock absorber: the spring still pushes back toward the reference, but the damper bleeds off velocity so the system settles instead of ringing. The design question in classical control is choosing the damping ratio — too little and you overshoot and oscillate; too much and you converge sluggishly.

We have been building dampers for decades under other names. Exponential backoff is damping in the time domain: each successive correction attempt is permitted less frequently, which bleeds energy out of a retrying loop exactly the way a damper bleeds velocity. Jitter is the multi-loop counterpart: when thousands of clients observe the same disturbance — a restarted dependency, an expired cache, a config push — they risk becoming thousands of control loops oscillating *in phase*, and their corrections constructively interfere. The thundering herd is resonance. Jitter decorrelates the phases so the energy spreads instead of spiking.

```go
// Damping for a reconcile loop: each consecutive failed
// correction earns a longer delay before the next attempt,
// and jitter decorrelates this loop from every other loop
// that observed the same disturbance.
func nextRequeue(attempt int) time.Duration {
	const base = 100 * time.Millisecond
	const ceiling = 30 * time.Second

	d := min(base<<attempt, ceiling) // exponential damping
	return d/2 + rand.N(d/2)         // jitter: uniform over [d/2, d)
}
```

Notice that the controls framing doesn't just give us decontextualized solutions: it also tells us *which problems each mechanism is for*. Backoff-with-jitter is the standard treatment wherever many loops share one disturbance: thundering herds, reconnect storms after a partition heals, synchronized cache expiry. These look like different incidents in a postmortem, but they are abstractable to a single class of phenomenon: underdamped loops in resonance.

### Hysteresis

Hysteresis makes a system's behavior depend on its history. The canonical implementation is the dead band, and the canonical example is your thermostat: it turns the furnace on below 65°F and off above 70°F (19°C and 21°C, for those of you who use temperature systems calibrated against the phase change of water and not the comfort of human beings), and in the band between, it does whatever it was already doing. The two thresholds create *memory* — there is a region of sensor space where the system holds state rather than reacting — and that memory prevents a noisy reading near a single threshold from toggling the furnace forty times an hour.

In control planes, hysteresis is the prescription for noise. A single threshold turns every boundary-adjacent sensor reading into a state transition; a dead band requires the signal to commit before the controller does. The same idea generalizes from thresholds to time: a *staleness gate* refuses to act on observations that the controller has reason to believe predate its own last actuation. Kubernetes carries this as `observedGeneration`, and checking it is hysteresis against the lag failure mode:

```go
func (c *Controller) reconcile(obj Object) error {
	// Staleness gate: the status we're reading was computed
	// against an older generation of the spec, so our sensor
	// is reporting on a world our last actuation hasn't
	// reached yet. Acting now means correcting an error we
	// may have already corrected — lag-driven overshoot.
	if obj.Status.ObservedGeneration < obj.Generation {
		c.requeueAfter(obj, c.settleTime)
		return nil
	}

	return c.actuate(obj, c.plan(obj))
}
```

Hysteresis is engineering, and engineering is not free. The controls framing is very clear about the tradeoff: a dead band swaps responsiveness for stability. Widen it and you flap less but react later; narrow it and you track the reference tightly but chatter near the boundary. That width is a design parameter of your control plane, and both knowing and exposing that during design can focus engineering and tighten incident response times.

## Damping a Scheduling Herd

Consider a fleet of ten thousand workers that each poll a scheduler for work assignments on a fixed thirty-second timer. A deploy restarts the fleet within the same few seconds — and now ten thousand timers are phase-aligned. Every thirty seconds, the scheduler absorbs the entire fleet's polling in a two-second window, queues saturate, some requests time out, the workers retry, and the retries are *also* aligned. Steady state, but a pathological one: the loop has found a resonant mode and is feeding it.

The first fix is the jitter we already wrote: randomize each worker's poll interval over, say, ±50%, and the phase alignment decays within a few cycles instead of persisting indefinitely.

The subtler problem is spatial rather than temporal. Suppose each worker, when it polls, is routed to the least-loaded of the scheduler's replicas — least-loaded *according to load data that lags reality*. Then every router makes the same "optimal" choice from the same stale snapshot, and the herd coherently piles onto one replica, which becomes the most loaded, which (one stale interval later) redirects the herd to the next victim. Deterministic greedy choice under lag doesn't balance load; it oscillates it. The treatment is randomization again, in space this time — the power-of-two-choices technique:

```go
// Power of two choices: sample two replicas at random, take
// the less loaded. Deterministic pick-the-least-loaded turns
// every router into part of a coherent herd aimed at the same
// stale "winner"; sampling two breaks the coherence while
// still preferring less-loaded replicas.
func pick(replicas []Replica) Replica {
	a := replicas[rand.N(len(replicas))]
	b := replicas[rand.N(len(replicas))]
	if a.Load() < b.Load() {
		return a
	}
	return b
}
```

Both fixes are the same move in the controls vocabulary: inject decorrelation to damp a resonant feedback loop. One operates in the time domain, one in the space of routing decisions. Knowing that they're the same move is what lets you reach for them *before* the incident, anywhere your design has many actors correcting against one shared, lagged signal.

## Hysteresis in Health Checking

Now consider a load balancer ejecting backends on failed health checks. A backend drifts to the edge of its latency budget — under brief load spikes its probe occasionally exceeds the timeout (after all, it serves user requests preferentially over health checks), then recovers. With a single threshold and no memory, each excursion ejects the backend and each recovery readmits it. That's flapping, and it would merely be noisy except for what the ejection *does*: the flapping backend's traffic redistributes onto its peers, pushing the busiest of them over the same threshold. Now two backends are flapping in alternation, trading the same excess load back and forth. The health-checking loop and the load-balancing loop have coupled, and the coupled system oscillates. This is noise amplified into churn, exactly as the taxonomy predicts.

The treatment is a dead band in evidence-space: require sustained proof before either transition.

![](/images/control-plane-as-control-system/hysteresis.svg)

Kubernetes ships this as the `failureThreshold` and `successThreshold` on its probes; Envoy's outlier detection has the same shape. Stripped to its mechanism, it's a few lines:

```go
type HealthGate struct {
	healthy   bool
	successes int
	failures  int

	// The dead band: consecutive evidence required to change
	// state. Wider band = more stability, more reaction lag.
	FailureThreshold int
	SuccessThreshold int
}

func (g *HealthGate) Observe(ok bool) bool {
	if ok {
		g.successes, g.failures = g.successes+1, 0
		if !g.healthy && g.successes >= g.SuccessThreshold {
			g.healthy = true
		}
	} else {
		g.failures, g.successes = g.failures+1, 0
		if g.healthy && g.failures >= g.FailureThreshold {
			g.healthy = false
		}
	}
	return g.healthy
}
```

The gate's output is a function of the input's *history* — that's the formal definition of hysteresis, and it's the entire point. The health state now carries memory, and memory is what stands between a noisy sensor and a churning plant. When you tune those thresholds, you are choosing the width of a dead band, and everything control theory knows about that trade-off — stability bought with responsiveness — applies to your YAML.

## Continuation Threads

I've covered two mechanisms and four failure modes, but this is just a start. The same framing keeps paying out, and as a community we can keep pulling this thread. A preview of where it goes:

**Level-triggering versus edge-triggering** is a controls distinction before it is a systems one: do you act on the *state* of the error or on *changes* to it? Kubernetes' insistence on level-triggered reconciliation is a claim about which of the two survives missed observations — and it connects directly to the sampling-theory argument I made in [the signal post](/posts/you-cant-stop-the-signal/), because an edge you didn't sample is an edge that never happened.

**Control loop interactions** deserve their own treatment. We compose control planes out of many single-purpose loops — horizontal autoscaling, vertical autoscaling, descheduling, cluster autoscaling — each individually stable, and then we're surprised when the composition isn't. Control theory has spent decades on coupled multivariable loops, and its first lesson is blunt: stability does not compose by default.

**Fail-open versus fail-closed** is the actuator question: what does the loop do when the loop itself breaks? When the controller can't reach its sensors, does the actuator hold the plant's last state or drive it to a safe one? Aviation calls this static stability, and the distributed-systems versions of the trade-off are just as consequential.

The throughline of all of it is the claim I opened with. We are already running control systems — thousands of them, coupled, sampled, and lagged — whether or not we call them that. The only choice we actually get is whether to design them with a century of theory in hand, or to rediscover each of its failure modes one incident at a time. We should make that choice deliberately. The theory is sitting right there.
