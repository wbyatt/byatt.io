# Writing & studies — ideas

Pitched to the same audience as the rest of the site: DSE / VPE / CTO and senior systems-architect roles at HFT firms. Concrete, first-person, no "thought-leader" varnish.

This file lives outside `content/` so Hugo never publishes it. Move an idea into `content/posts/` (or `content/studies/`, when that exists) when ready to draft.

---

## From the design brief

### Writing examples cited

**Distributed systems**
- On blast radius and the cost of a single shared database — coupling as a property of the system, not the code; why the fix is rarely "split the database."
- On consistency models, plainly — linearizable, sequential, causal, eventual, in English, with what each one costs.
- The two-database problem — when a system grew a second source of truth and nobody noticed.

**Leadership**
- What I actually do as a systems architect — a week of meetings, a week of design docs, a week of code, repeat.
- Hiring senior engineers without a take-home — three rounds, two hours total, no homework.

**Notes**
- Reading list, Q1 2026 — five papers, two books, one long blog post.

**Talks (CV mock)**
- Blast radius as a design constraint — QCon, 2025.
- Hiring senior engineers without a take-home — StaffPlus, 2024.

### Studies cited

- **04** — A control-plane diagram, drawn six ways. The same system as it reads to a junior engineer, an SRE, a security reviewer, and an exec.
- **03** — Building Raft from scratch in a weekend. Two days, one notebook; what I got right and wrong.
- **02** — A reading log of every CRDT paper I could find. Annotated, sorted by usefulness not novelty.
- **01** — Re-reading *A Philosophy of Software Design*. Five years on, what holds up.

---

## Proposed additions

### Distributed systems / control planes
Signals depth, currency, this-is-actually-what-I-do.

- Why control planes are mostly a schema problem — the data model dwarfs the network code.
- Reconfiguration is harder than failover — adding a region without a maintenance window.
- Cross-region consensus, ranked by what it costs you — Raft / Paxos / SWIM / EPaxos, in operational dollars.
- Idempotency keys are a contract, not an implementation detail.
- Why we stopped using `etcd` for X (and kept it for Y) — pick a real fork in the road.
- What "eventually consistent" actually buys at p99.9.
- Modeling the fleet as a continuous system — sync-free scheduling at sub-millisecond latency. If you sample collaborators above their Nyquist rate and characterize their bandwidth, you can predict their state without asking — coordination becomes signal reconstruction, not RPC. The argument: synchronization is a fallback for cases where you couldn't characterize the signal; in regimes where you *can* (HFT, control planes with predictable workloads, anything with bounded rates of change), Nyquist-correct sampling beats locks/queues/consensus on latency by orders of magnitude. Lands the leadership read: "lock-free is the wrong goal — bandwidth-characterized is."
- Signals-theoretic observability — your monitoring pipeline is a cascaded sampler chain (kernel → cAdvisor → Prometheus → Grafana → your eyes), each stage with its own bandwidth, each stage capable of aliasing. "Correct" is sampler-relative; the same physical workload renders four different ways depending on integration window. Pairs with the existing study at /studies/shannon-nyquist/ as the empirical artifact.

### Latency & HFT-adjacent
Signals HFT-fluent without LARPing as a quant.

- Tail latency is a system property — JIT, allocator, scheduler, NIC, all of it.
- What a 5µs budget actually feels like — what you give up at each tier.
- Coordinated omission, in plain English, with examples.
- Lock-free is not a goal; predictable is — when wait-free isn't worth it.
- When kernel bypass earns its keep — and when it's a tax.

### Architecture & decision-making
Signals judgment, not credentialing.

- The cheap, the principled, and the right answer — pick two.
- Reversibility is the most underrated design dimension.
- The four kinds of "we should rewrite this."
- How to read a system you didn't build, in a week.
- Failure-domain analysis on a napkin.
- The complexity class you didn't measure — most architects know the Big-O of code they wrote; senior architects know the Big-O of the primitives they depend on under contention (lock manager wait queues, scheduler runqueues, NIC rings, GC pause vs. live-set). Mature-system failures are rarely algorithmic; they're a primitive whose cost function nobody modeled, hit by a workload shape nobody predicted.

### Leadership & operating model
Signals actually ran orgs, isn't bluffing.

- Org charts as failure-domain diagrams.
- What I delegate and what I won't — and why the second list is short.
- On-call as a leading indicator of architectural debt.
- The design doc is the artifact; the meeting is the rehearsal.
- Hiring for systems thinkers without asking "design Twitter."

### Studies (smaller, demo-shaped)
Signals still building, not just narrating.

- A toy CRDT in 200 lines, with the failure modes labeled.
- A two-region key-value store with clock skew injected.
- A diff of three real-world Raft implementations, line-by-line on the bits that matter.
- A flame graph of a misconfigured Postgres connection pool — annotated for the exec read.
- What changed in `linux/sched/` over 10 years, and why an HFT shop should care.
