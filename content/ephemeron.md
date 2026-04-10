---
title: "Consensus Protocols and Hidden Hierarchies"
subtitle: "How Raft's dominance reveals cultural assumptions about coordination and authority"
date: 2026-04-10
draft: false
layout: treatise
doc_class: "ephemeron"
doc_id: "ephemeron.2026-04-10"
revision: "diurnal"
chain: "contingent authority → cognitive habituation → cultural selection → naturalized hierarchy"
footer_status: "ephemeral unless preserved"
status_pips:
  - active
  - amber
  - dim
hook: "Raft, the consensus protocol underlying Kubernetes and most critical infrastructure, is celebrated for being more understandable than Paxos. But understandable to whom? Its legibility derives not from inherent simplicity but from how closely it mirrors the hierarchical organizations engineers already inhabit, rendering genuinely egalitarian alternatives invisible."
generated_by: "Claude Opus 4.6"
generated_date: "10 April 2026"
---

{{< section num="01" label="§ I.  The Quiet Election" >}}

{{< gutter sigil="heartbeat" >}}

{{< main-col >}}

{{< dropcap letter="E" >}}

<p>very five seconds, or every ten, or whenever a timeout fires into the void of a missed heartbeat, a small election takes place inside the infrastructure you depend on. A node in an etcd cluster notices the leader has gone silent. It increments its term, votes for itself, and solicits votes from its peers. If a majority responds, it becomes the new leader. Log entries flow downward. Followers replicate in sequence. The system resumes.</p>

<p>This is Raft, and it runs beneath almost everything: Kubernetes service discovery, Consul's service mesh, CockroachDB's transaction layer, TiKV's distributed storage. It is one of the most consequential governance algorithms in the world, and almost nobody thinks of it as governance. The Raft paper's founding claim — that its primary contribution over Paxos is *understandability* — has been accepted so completely that it functions as a closed question. Raft is easier to understand. Raft is therefore better for production systems. End of discussion.</p>

<p>But "understandable to whom, and within what form of life?" is not a settled question. It is, in fact, the question.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Ongaro & Ousterhout 2014" >}}
The Raft paper (In Search of an Understandable Consensus Algorithm, USENIX ATC 2014) explicitly positions understandability as a first-order design constraint, not merely a secondary benefit. This rhetorical move is itself unusual in systems research, where performance and correctness typically occupy the justificatory foreground.
{{< /note >}}
{{< note ref="NOTE // governance-as-infrastructure" >}}
The framing of consensus protocols as governance echoes Langdon Winner's 'Do Artifacts Have Politics?' (1980), but with a crucial difference: Winner's examples (bridges, nuclear plants) are static artifacts, whereas Raft's leader election is a continuously re-enacted political ceremony. The temporality of the politics changes the analysis.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="02" label="§ II.  Contingent Leaders, Constitutive Leaders" >}}

{{< gutter sigil="constitutive" >}}

{{< main-col >}}

<p>The naïve version of the argument I want to make would go like this: Paxos is a leaderless protocol of symmetric peers, Raft imposes hierarchy, and the transition from one to the other mirrors the historical enclosure of the commons. This is wrong, or at least far too crude. Lamport's original single-decree Paxos does define asymmetric roles — proposers, acceptors, learners — and Multi-Paxos, which is what every serious Paxos deployment actually uses, designates a distinguished proposer that functions as a de facto leader. Google's Chubby, the most famous Paxos implementation, is thoroughly leader-based. Anyone who has read the Paxos Made Live paper knows that the distance between academic Paxos and deployed Paxos is vast, and that deployed Paxos converges on leadership as a performance optimization.</p>

<p>So the interesting distinction is not leader versus leaderless. It is between a protocol that treats leadership as *contingent* — an optimization that can be adopted or abandoned, one configuration among many in a space of possible roles — and a protocol that treats leadership as *constitutive*, baked into the definition of correctness itself. In basic Paxos, any node can propose a value at any time. The protocol's safety properties hold regardless of whether a stable leader exists. Leadership, when it appears, is an emergent performance hack layered on top of a fundamentally symmetric substrate. In Raft, by contrast, there is no protocol without a leader. The entire log replication mechanism presupposes a single authority from which entries flow. A Raft cluster without a leader is not operating in a degraded mode; it is not operating at all. It is *between* governments, waiting for the next election to conclude.</p>

<p>This distinction — contingent versus constitutive authority — is not a metaphor imported from political philosophy. It is a precise structural property of the algorithms. And it matters because the design space forecloses certain possibilities. A Paxos-based system can, in principle, be reconfigured to operate with rotating proposers, multiple concurrent proposers, or no stable proposer at all, while preserving safety. A Raft-based system cannot shed its leader without becoming a different protocol entirely. The governance structure is load-bearing.</p>

<p>Recent work in the consensus protocol space makes this even more concrete. EPaxos (Egalitarian Paxos), designed by Iulian Moraru, David Andersen, and Michael Kaminsky, achieves strong consistency with no distinguished leader in the common case. When commands don't conflict, any replica can commit independently. When they do conflict, a dependency-graph resolution mechanism handles ordering without centralizing authority. Mencius, another leaderless variant, partitions the sequence space across all servers, letting each take turns proposing. These are not theoretical curiosities — they have been benchmarked, peer-reviewed, and shown to match or exceed Raft's throughput under workloads with low contention, while offering better latency in geographically distributed deployments precisely because they avoid routing all decisions through a single node.</p>

<p>And yet, when HashiCorp built Consul, they chose Raft. When CoreOS built etcd, they chose Raft. When CockroachDB and TiKV needed consensus, they chose Raft. The pattern is so consistent that it demands an explanation beyond "Raft is correct and performant," because EPaxos is also correct and in some regimes more performant. Something else is being selected for.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Moraru, Andersen & Kaminsky 2013" >}}
EPaxos (SOSP 2013) achieves optimal commit latency in the common case (one round-trip) without a stable leader, using a dependency-graph approach to ordering. The paper shows that leadership is not merely an optimization but an architectural trade-off: EPaxos sacrifices implementation simplicity for leaderlessness, revealing that 'simplicity' itself is a design parameter, not a given.
{{< /note >}}
{{< note ref="REF // Chandra, Griesemer & Redstone 2007" >}}
The Paxos Made Live paper (PODC 2007) documents Google's Chubby implementation and catalogues the enormous gap between Lamport's elegant specification and a working system. The paper's litany of engineering difficulties is often cited to justify Raft's existence, but it equally demonstrates that the difficulty was in operationalization, not in the absence of a constitutive leader.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="03" label="§ III.  What Counts as Understandable" >}}

{{< gutter sigil="lebensform" >}}

{{< main-col >}}

<p>The Raft paper, by Diego Ongaro and John Ousterhout, is explicit about its design methodology. They conducted a user study in which students were taught Paxos and Raft, then tested on their understanding of each. Raft scored higher. This is the empirical foundation for the claim that Raft is "more understandable," and it has achieved a kind of axiomatic status in the distributed systems community.</p>

<p>But consider what this study actually measures. It measures the ease with which students embedded in a particular educational context, trained in particular reasoning styles, socialized into particular models of system organization, can form a correct mental model of a protocol after a brief exposure. It does not — cannot — measure understandability as an intrinsic property of the algorithm. It measures *fit* between the algorithm's structure and the cognitive habits of its audience.</p>

<p>This is where Wittgenstein's remarks on rule-following become genuinely illuminating, not as philosophical decoration but as a diagnostic tool. Wittgenstein's point in the *Philosophical Investigations* (§§185–202) is that no rule determines its own application. What counts as "following the rule" is constituted by a shared practice — a *form of life* — not by the rule's internal logic. The rule "add 2" seems perfectly unambiguous until you consider Wittgenstein's student who, upon reaching 1000, continues 1004, 1008, 1012, fully believing he is following the same rule. The point is not skepticism about arithmetic. The point is that our certainty about what the rule means is grounded in a community of practice, not in the rule itself.</p>

<p>Applied to protocol design: when engineers say Raft is "easier to understand," they are reporting a genuine phenomenological fact about their experience. But that experience is shaped by a form of life in which authority flows downward, in which organizations have managers, in which the mental model of "one entity decides, others follow" is so deeply naturalized that it feels like common sense rather than one topology among many. Raft is legible because its structure mirrors the organizational structures engineers already inhabit. A leader sends commands. Followers execute them. If the leader fails, you hold an election and get a new one. This is not just a distributed systems protocol — it is a compact model of corporate governance, and it feels intuitive for exactly the same reasons that corporate governance feels natural to people who have spent their careers inside corporations.</p>

<p>Paxos, by contrast, asks you to reason about multiple concurrent proposers, overlapping rounds, the possibility that no single entity is in charge at any given moment. The "difficulty" of Paxos is isomorphic to the difficulty that people raised in hierarchical institutions have when asked to reason about genuinely decentralized coordination. Anyone who has watched a horizontally-organized collective try to make a decision has experienced the same cognitive dissonance: it feels chaotic, illegible, *hard to understand* — not because horizontal coordination is inherently more complex, but because we lack practice in thinking that way.</p>

<p>The Raft paper's user study, then, does not demonstrate that Raft is objectively simpler. It demonstrates that hierarchy is the default cognitive frame for the population studied. This is a finding about culture, not about algorithms.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Wittgenstein 1953 §§185–202" >}}
The essay's deployment of rule-following is precise but could be pushed further via Saul Kripke's reading (Wittgenstein on Rules and Private Language, 1982), where the skeptical paradox is that no fact about a speaker's past usage determines future application. Applied here: no formal property of Raft determines that it will be experienced as 'understandable' — that determination is wholly communal.
{{< /note >}}
{{< note ref="NOTE // cognitive sociology of hierarchy" >}}
The claim that legibility tracks organizational habituation parallels Bourdieu's concept of habitus (Outline of a Theory of Practice, 1977): engineers do not choose hierarchy after deliberation; they perceive hierarchical protocols as self-evidently clearer because their trained dispositions pre-structure what counts as clarity. The user study measures doxa, not complexity.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="04" label="§ IV.  Updating to Remain the Same" >}}

{{< gutter sigil="chun" >}}

{{< main-col >}}

<p>There is a pattern in the history of consensus protocols that deserves more scrutiny than it typically receives. Viewstamped Replication (1988) introduced a leader-based replicated state machine. Paxos (1989, published 1998) offered a more general, leader-optional framework. Multi-Paxos re-introduced a stable leader for performance. Raft (2014) formalized the leader requirement and stripped away the leader-optional substrate entirely. Each generation presents itself as novel — a simplification, a clarification, a pedagogical improvement — while reproducing the same structural commitment to centralized authority.</p>

<p>Wendy Chun describes the logic of new media as "updating to remain the same": the constant novelty of technological change masks the reproduction of existing power structures. What this framework adds here is an explanation of the *affective* dimension of the reproduction. Each new protocol feels like progress because it resolves a genuine frustration (Paxos really is hard to implement correctly). But the resolution consistently moves in only one direction — toward tighter formalization of hierarchy — not because that direction is technically inevitable but because it is *satisfying* in a way that matches existing expectations about how coordination should work. The update feels like an improvement because it makes the system more closely resemble the organizational world the engineer already understands. The sameness — the persistent leader topology — is experienced not as stasis but as maturation.</p>

<p>This is what distinguishes Chun's account from a simpler story about path dependence or institutional inertia. Path dependence explains why engineers trained on Raft continue to choose Raft. It does not explain why the moment of choosing *feels like progress* — why the Raft paper's rhetorical framing as a breakthrough in understandability was received with such enthusiasm, even though what it "broke through" to was a more rigid version of what Multi-Paxos already did. Chun's point is that the affective experience of innovation is itself the mechanism by which existing structures reproduce. You feel like you're moving forward. You are standing still.</p>

<p>The genuinely egalitarian alternatives exist and keep being developed. EPaxos. Mencius. Aleph, a leaderless BFT protocol. HotStuff, which is leaderless in the optimistic case and has been adopted as the basis for several blockchain consensus layers. CRDTs, which sidestep consensus entirely by ensuring convergence through algebraic properties of the data structures themselves. These are not fringe experiments — CRDTs power the collaborative editing in Figma, and EPaxos has been shown to outperform Multi-Paxos in wide-area network deployments. But they remain marginal in the bread-and-butter infrastructure layer, the etcds and Consuls and ZooKeepers. The cultural selection filter operates not at the level of formal properties but at the level of what feels *right*, what feels *manageable*, what feels *understandable* — which is to say, at the level of the form of life.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Chun 2016" >}}
Wendy Chun's Updating to Remain the Same (MIT Press, 2016) theorizes habitual media as the mechanism by which novelty and repetition become indistinguishable. The application to consensus protocol genealogy is original here: the Viewstamped Replication → Paxos → Multi-Paxos → Raft lineage enacts exactly the 'new = same' loop Chun describes, with each generation's rhetoric of improvement concealing structural continuity.
{{< /note >}}
{{< note ref="NOTE // CRDTs as exit" >}}
CRDTs (Conflict-Free Replicated Data Types, formalized by Shapiro et al. 2011) represent a more radical departure than EPaxos because they dissolve the consensus problem rather than solving it differently. Their algebraic guarantee of convergence sidesteps leadership entirely, yet their adoption remains confined to specific domains (collaborative editing, shopping carts), suggesting that the cultural selection filter operates even more strongly at the paradigm level than at the protocol level.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="05" label="§ V.  What Practitioners Owe Themselves" >}}

{{< gutter sigil="conway" >}}

{{< main-col >}}

<p>I want to be precise about what I am not arguing. I am not arguing that Raft is bad, or that engineers who choose it are making an error, or that you should replace your etcd cluster with an EPaxos implementation to make a political statement. Raft has a clean correctness proof, a well-understood log compaction strategy, and a mature ecosystem of production-hardened implementations. If you need linearizable reads at five replicas with sub-10ms latency and you want to hire engineers who can debug your consensus layer, Raft is a defensible choice, possibly the best choice available today.</p>

<p>What I am arguing is narrower and, I think, more durable: that the *reasons* Raft is the best choice today are not purely technical. They are entangled with cultural facts about what engineers are trained to reason about, what mental models feel natural, what organizational templates are pre-loaded into the cognitive habits of the profession. And that this entanglement is invisible precisely to the extent that hierarchy is naturalized as common sense.</p>

<p>The practical implication is not "choose differently" but "notice what you're choosing." When the requirement is strong consistency across replicas, and you reach for Raft without considering leaderless alternatives, ask whether the requirement actually demands a stable leader or whether a leaderless protocol with comparable safety properties might serve — and might offer better latency, better fault tolerance during leader transitions, better geographic distribution. When you find a leaderless protocol "hard to reason about," notice that this difficulty might be informative about your own cognitive training rather than about the protocol's inherent complexity.</p>

<p>More broadly: the systems we build encode assumptions about how collective decisions should be made. Those assumptions propagate. Kubernetes, built on etcd, built on Raft, imposes a leader-election model on every cluster it manages. Organizations increasingly model their own coordination on the software patterns available to them — microservice architectures that mirror and then reinforce Conway's Law in both directions. The consensus protocol at the bottom of the stack is not politically inert. It is a small, quiet, constantly-repeated election, and the fact that it always elects a single leader is not a law of nature. It is a choice made legible, made comfortable, and made invisible by the same habitual structures that make hierarchy feel like the only way to get anything done.</p>

<p>The protocol space is richer than the production landscape suggests. The question is whether we can learn to find that richness understandable — which is to say, whether we can learn to inhabit a different form of life, even partially, even briefly, even just long enough to see that the election was never the only option.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Conway 1968" >}}
Conway's Law ('organizations which design systems are constrained to produce designs which are copies of the communication structures of these organizations') is invoked here bidirectionally: Kubernetes clusters mirror corporate hierarchy, and then corporate teams restructure around Kubernetes primitives. The feedback loop means that a constitutive-leader protocol at the infrastructure layer exerts quiet normative pressure upward through the entire sociotechnical stack.
{{< /note >}}
{{< note ref="NOTE // epistemic humility as method" >}}
The essay's careful delimitation — 'not choose differently but notice what you're choosing' — mirrors what Donna Haraway calls situated knowledge (1988): the claim is not that a view from nowhere would reveal the correct protocol, but that acknowledging the situatedness of one's legibility intuitions is itself a technical skill with engineering consequences, measurable in latency percentiles and failover times.
{{< /note >}}
{{< /margin >}}
