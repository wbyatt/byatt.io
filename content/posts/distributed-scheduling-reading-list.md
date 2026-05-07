---
title: "Reading list, Q2 2026: distributed scheduling"
subtitle: "twenty-one papers, sorted by structural family"
date: 2026-05-07T12:00:00-04:00
draft: false
---

Twenty-one papers on distributed scheduling for Q2 2026 — about half I've already worked through, the other half I'm catching up on this quarter. The list is sorted by *structural family*: how each paper approaches the scheduling problem, not when it appeared. This list will take you through the most important developments in the theory and practice of designing large-scale distributed schedulers.

The real upshot here is, as always, that engineering has tradeoffs. Both scheduler system design and implementation details invoke tradeoffs among throughput, tail latency, determinancy, scalability, and interpretability.

---

## Foundational allocation theory

### [Dominant resource fairness: Fair allocation of multiple resource types](https://www2.eecs.berkeley.edu/Pubs/TechRpts/2011/EECS-2011-18.pdf)
#### Ghodsi, Zaharia, Hindman, Konwinski, Shenker, Stoica. *NSDI 2011.*

The canonical answer to "how do you do fair share when jobs need different mixes of CPU/RAM/GPU/etc.?" Generalizes max-min fairness to vector-valued resources by allocating proportionally to each user's *dominant* resource share. Used by Mesos, YARN's Capacity Scheduler, and Kubernetes via descendants. Re-read it for the strategy-proofness proof — that's the part that turns a heuristic into a defensible policy.

### [Choosy: Max-Min Fair Sharing for Datacenter Jobs with Constraints](https://people.eecs.berkeley.edu/~matei/papers/2013/eurosys_choosy.pdf)
#### Ghodsi, Zaharia, Shenker, Stoica. *EuroSys 2013.*
DRF extended to constrained queues. Less famous, but if you've internalized DRF this is the natural follow-up.

### [Altruistic Scheduling in Multi-Resource Clusters](https://www.usenix.org/system/files/conference/osdi16/osdi16-grandl-altruistic.pdf)
#### Grandl, Chowdhury, Akella, Ananthanarayanan. *OSDI 2016.*
Altruistic scheduling — jobs voluntarily give up resources they don't currently need. Improves average completion time *and* fairness simultaneously, which DRF cannot do alone.

### [2DFQ: Two-Dimensional Fair Queuing for Multi-Tenant Cloud Services](https://www.microsoft.com/en-us/research/wp-content/uploads/2016/08/mace162dfq.pdf?msockid=169f6418a33165ee0bc670ffa2356421)
#### Blagojević, Lloyd, Anderson. *USENIX ATC 2018.*
Two-dimensional fair queueing for scheduling latency-sensitive vs. background work on the same machine. The right thing to read if you care about co-location at the queue level.

---

## Two-level: separate allocation from placement

### [Mesos: A Platform for Fine-Grained Resource Sharing in the Data Center](https://people.eecs.berkeley.edu/~alig/papers/mesos.pdf)
#### Hindman, Konwinski, Zaharia, Ghodsi, Joseph, Katz, Shenker, Stoica. *NSDI 2011.*
"Resource offers" — the master proposes packs of resources to frameworks (Spark, Hadoop, Marathon, etc.), which accept or reject. Decouples *what to run where* from *how to find a slot*. Reads as a clean architectural separation; influenced everything that followed.

### [Large-scale cluster management at Google with Borg](https://static.googleusercontent.com/media/research.google.com/en//pubs/archive/43438.pdf)
#### Verma, Pedrosa, Korupolu, Oppenheimer, Tune, Wilkes. *EuroSys 2015.*
Google's monolithic cluster manager. The lessons-learned section is the real value: priority bands, admission, preemption, the cgroup-based isolation regime, the failure modes nobody tells you about until you've operated one. A surprisingly readable industrial paper.

### [Omega: flexible, scalable schedulers for large compute clusters](https://cs.brown.edu/people/malte/pub/papers/2013-eurosys-omega.pdf)
#### Schwarzkopf, Konwinski, Abd-El-Malek, Wilkes. *EuroSys 2013.* 
Borg's successor architecture. Shared-state scheduling: schedulers operate concurrently on a copy of cluster state and resolve conflicts optimistically. Departure from Borg's monolithic decision loop. The lineage paper for Kubernetes's design.

### [Apache Hadoop YARN: Yet Another Resource Negotiato](https://www1.ece.neu.edu/~ningfang/SimPaper/YARN-SOCC2013.pdf)
#### Vavilapalli et al. *SoCC 2013.*
Hadoop 2's resource manager — two-level, multi-tenant. Practical, not theoretically novel, but operationally significant because of Hadoop's reach and the number of papers that build on its mechanisms.

### [TetriSched: global rescheduling with adaptive plan-ahead in dynamic heterogeneous clusters](https://www.pdl.cmu.edu/PDL-FTP/CloudComputing/tetrisched-tumanov-eurosys16.pdf)
#### Tumanov, Zhu, Park, Kozuch, Harchol-Balter, Ganger. *EuroSys 2016.*
Reservation-aware: jobs declare deadlines, scheduler plans ahead. Argues that pure online algorithms leave a lot of efficiency on the table when workloads have predictable structure.

---

## Decentralized / sampling-based

### [Sparrow: Distributed, Low Latency Scheduling](https://people.eecs.berkeley.edu/~matei/papers/2013/sosp_sparrow.pdf)
#### Ousterhout, Wendell, Zaharia, Stoica. *SOSP 2013.* 
Per-task-decision-in-milliseconds scheduling for short jobs. Workers handle their own queues; schedulers do batch-sampling of *d* = 2 random workers and place tasks at the lighter-loaded one. Exploits power-of-*d*-choices theory directly. Genuinely sub-millisecond decision time at high concurrency. Architecturally: stateless schedulers, all coordination implicit.

### [Apollo: Scalable and Coordinated Scheduling for Cloud-Scale Computing](https://www.usenix.org/system/files/conference/osdi14/osdi14-paper-boutin_0.pdf)
#### Boutin, Ekanayake, Lin, Shi, Zhou, Qian, Wu, Zhou. *OSDI 2014.* 
Microsoft's answer to Sparrow at Cosmos scale. Each scheduler maintains an estimated wait-time matrix per machine, updated via aggressive UDP status messages from workers. Decisions are local but well-informed; conflicts resolved via re-evaluation rather than locking. Has the most pragmatic discussion of "why TCP is too expensive for status updates at this rate" you'll find in scheduling literature.

### [Mercury: Hybrid Centralized and Distributed Scheduling in Large Shared Clusters](https://web.archive.org/web/20170814054522/https://www.usenix.org/system/files/conference/atc15/atc15-paper-karanasos.pdf)
#### Karanasos, Rao, Curino, Douglas, Chaliparambil, Fumarola, Heddaya, Ramakrishnan, Sakalanaga. *USENIX ATC 2015.*
Hybrid: best-effort containers run wherever, guaranteed-resource containers go through the central path. The architectural argument is that you can let opportunistic work scavenge under a centralized policy without changing the policy itself.

### [Hawk: Hybrid Datacenter Scheduling](https://www.usenix.org/system/files/conference/atc15/atc15-paper-delgado.pdf)
#### Delgado, Didona, Dinu, Zwaenepoel. *USENIX ATC 2015.*
Hybrid centralized/distributed. Long jobs go through the central scheduler, short jobs through a Sparrow-style distributed path. Same intuition as Mercury, different decomposition.

### [Yaq: Efficient Queue Management for Cluster Scheduling](https://www.microsoft.com/en-us/research/wp-content/uploads/2016/04/yaq-eurosys2016.pdf?msockid=169f6418a33165ee0bc670ffa2356421) 
#### Rasley, Karanasos, Kandula, Fonseca, Vojnović, Rao. *EuroSys 2016.*
Queue management at the worker. Solves a real problem distributed schedulers all hit: head-of-line blocking when a heavy task lands ahead of light tasks at a worker. Per-worker prioritization with limited reordering.

---

## Theoretical load balancing — the math underneath the schedulers

### [The Power of Two Choices in Randomized Load Balancing](https://www.eecs.harvard.edu/~michaelm/postscripts/tpds2001.pdf)
#### Mitzenmacher. *Ph.D. thesis 1996; later JACM.*
The result that motivates Sparrow: instead of choosing a server uniformly at random, pick *d* = 2 and route to the less loaded; the maximum load drops from log *n* / log log *n* to log log *n* / log *d*. Foundational. Cite this when defending *d* = 2 as a design choice.

### [Join-Idle-Queue: A Novel Load Balancing Algorithm for Dynamically Scalable Web Services](https://www.microsoft.com/en-us/research/wp-content/uploads/2011/10/idleq.pdf?msockid=169f6418a33165ee0bc670ffa2356421)
#### Lu, Xie, Kliot, Geller, Larus, Greenberg. *IFIP Performance 2011.* Microsoft.
Inverts the heartbeat direction: workers notify a small dispatcher pool when they go idle. Dispatchers route incoming work to known-idle workers with no probing. Achieves *d* = 2 performance with *O*(1) dispatcher-to-worker messages per arrival. Beautiful paper, frequently missed.

---

## Microsecond / kernel-bypass scheduling

### [ZygOS: Achieving Low Tail Latency for Microsecond-scale Networked Tasks](https://abelay.github.io/6828seminar/papers/prekas:zygos.pdf)
#### Prekas, Kogias, Bugnion. *SOSP 2017.*
Per-core run queues with work-stealing for tail latency on µs-scale RPCs. Argues that single-queue-per-system is the wrong default at this timescale.

### [Shinjuku: Preemptive Scheduling for μsecond-scale Tail Latency](https://www.usenix.org/system/files/nsdi19-kaffes.pdf)
#### Kaffes, Chong, Humphries, Belay, Mazières, Kozyrakis. *NSDI 2019.*
Centralized scheduling at microsecond granularity using preemption (centralized dispatcher + hardware preemption support). Makes the case for centralized again, at the right timescale.

### [Shenango: Achieving High CPU Efficiency for Latency-sensitive Datacenter Workloads](https://www.usenix.org/system/files/nsdi19-ousterhout.pdf)
#### Ousterhout, Fried, Behrens, Belay, Balakrishnan. *NSDI 2019.*
Microsecond-scale core allocation between latency-critical and batch workloads, with kernel-bypass networking. Reallocation latency in the single µs.

### [Caladan: Mitigating Interference at Microsecond Timescales](https://www.usenix.org/system/files/osdi20-fried.pdf)
#### Fried, Belay, Marty. *OSDI 2020.*
Successor to Shenango — interference-aware, handles tail-latency under aggressive co-location. The most sophisticated work in this lineage.

### [When Idling is Ideal: Optimizing Tail-Latency for Heavy-Tailed Datacenter Workloads with Perséphone](https://www.irenezhang.net/papers/persephone-sosp21.pdf)
Didona, Costa, Pilman, Reidys, Lee, Zwaenepoel. *SOSP 2021.* 
Application-level scheduling for tail latency under skewed workloads. Argues that even with a perfect kernel scheduler, application-level work distribution still matters when service times are heavy-tailed.
<!--
## Earlier / often-overlooked

**Quincy.** Isard, Prabhakaran, Currey, Wieder, Talwar, Goldberg. *SOSP 2009.* Microsoft. Scheduling as min-cost flow over a graph that encodes locality preferences and fairness. Beautiful formulation, computationally intensive in the original implementation; later inspired Firmament.

**Firmament.** Gog, Schwarzkopf, Gleave, Watson, Hand. *OSDI 2016.* Quincy's idea executed at production scale. Sub-second min-cost-flow scheduling decisions for thousands of machines. The fastest "centralized via optimization" scheduler I am aware of.

**Mantri.** Ananthanarayanan, Kandula, Greenberg, Stoica, Lu, Saha, Harris. *OSDI 2010.* Cause-aware straggler mitigation for MapReduce. The diagnostic framework — *why* is this task slow — generalizes well beyond the system it's set in.

**Dolly.** Ananthanarayanan, Ghodsi, Shenker, Stoica. *NSDI 2013.* Speculative execution for short jobs. Argues that duplicate-and-race should be more aggressive when jobs are short, since the speculation cost is bounded.

## Adjacent — not strictly "scheduling" but required reading for budget reasoning

**Snap.** Marty et al. *SOSP 2019.* Google's user-space network microkernel. Scheduling and dispatch are deeply intertwined here.

**eRPC.** Kalia, Kaminsky, Andersen. *NSDI 2019.* Sub-µs RPC over commodity hardware. Required reading for latency-budget reasoning.

**R2P2.** Kogias, Prekas, Ghosn, Fietz, Bugnion. *USENIX ATC 2019.* Decouples request and response routing for replica-aware load balancing. -->
<!-- 
## Suggested reading paths

**For the systems-engineering interview audience:** Mesos → Borg → Omega → Sparrow → Apollo. Five papers, all canonical, you can hold the architectural conversation in any room.

**For the HFT-architect audience:** skim Borg + Omega for the centralized lineage, Sparrow + Apollo for decentralized, then go deep on the µs family — Shinjuku, Shenango, Caladan. The philosophical thread is *centralized vs. distributed is a frequency-domain question — different timescales need different architectures*. Pair with the Shannon-Nyquist study for the signals-theoretic backbone.

**For the distributed-systems-essay audience:** lead with DRF (theory), Mesos (architecture), Borg/Omega (production), Sparrow/Apollo (decentralized). The thread is *scheduling as resource allocation under uncertainty about future demand* — and the architectural answers are about how that uncertainty is propagated and resolved. -->

---

*Working notes. Adjustments, additions, and corrections welcome — particularly anything from the post-2021 literature on ML-workload schedulers, which this list under-covers.*
