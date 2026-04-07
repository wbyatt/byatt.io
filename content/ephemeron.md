---
title: "The Invisible Labor of Agreement"
subtitle: "How distributed systems hide the maintenance work that keeps them alive"
date: 2026-04-07
draft: false
layout: treatise
doc_class: "ephemeron"
doc_id: "ephemeron.2026-04-07"
revision: "diurnal"
chain: "consensus → reproductive labor → care epistemology → formalization boundaries → maintenance quorums"
footer_status: "ephemeral unless preserved"
status_pips:
  - active
  - amber
  - dim
hook: "Consensus theory celebrates the protocols that produce agreement while rendering invisible the maintenance labor that makes agreement possible. Drawing on Federici and Puig de la Bellacasa, this essay argues that care-based coordination is structurally inexpressible within consensus formalism—and sketches a novel primitive that could change that."
generated_by: "Claude Opus 4.6"
generated_date: "7 April 2026"
---

{{< section num="01" label="§ I.  The Invisible Labor of Agreement" >}}

{{< gutter sigil="reproductive" >}}

{{< main-col >}}

{{< dropcap letter="E" >}}

<p>very distributed system runs on two economies. The first is visible, formalized, and celebrated: the consensus protocol that ensures all nodes agree on the same value in the same order. This is the economy of Raft's leader election, Paxos's ballot numbers, PBFT's three-phase commit. It has a rich theoretical literature, elegant impossibility proofs, and the quiet prestige of solving what physicists would call a fundamental problem. The second economy is invisible, ad hoc, and considered beneath formal attention: garbage collection clearing the debris of abandoned proposals, heartbeat mechanisms sustaining the illusion of liveness, replica repair quietly stitching torn state back together after partitions heal, connection keepalives whispering *I'm still here, I'm still here* into a void that has no formal obligation to answer. This second economy has no theory. It has operational runbooks.</p>

<p>Silvia Federici would recognize this arrangement immediately. Her argument in *Caliban and the Witch* is not merely that capitalism depends on unpaid reproductive labor—cooking, cleaning, childbearing, emotional care—but that capitalism *structurally cannot see* this labor because the conceptual apparatus it uses to define "production" was built precisely to exclude it. The wage relation doesn't accidentally overlook domestic work; the wage relation is *constituted by* that exclusion. Work legible to capital is work that produces a commodity. Work that produces the *worker herself*—that maintains, repairs, and reproduces the conditions under which commodity production is possible—is, by definitional fiat, not work at all.</p>

<p>The mapping onto distributed systems is not metaphorical. Consensus theory defines its problem space through two properties: safety (all nodes agree on one value) and liveness (the system eventually makes progress). Everything required to make consensus *possible*—stable network connections, functioning replicas, recovered state, managed resources—is definitionally outside the consensus abstraction. These are preconditions, not properties. The theory takes them as given, or handles their absence through failure detectors and timeout heuristics that live in a different formal register than the protocol itself. Replica repair is to Raft what domestic labor is to the wage economy: absolutely necessary, structurally illegible, someone else's problem.</p>

<p>This is where Federici's framework earns its keep—not as a loose analogy about "enclosure" (linearizing writes does not dispossess peasants), but as a precise diagnosis of how formal systems render their own dependencies invisible. Consensus theory didn't *forget* to formalize maintenance. Consensus theory is what you get when you draw the boundaries of formalization in a way that maintenance cannot enter.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Federici 2004" >}}
Federici's argument in *Caliban and the Witch* extends Marx's primitive accumulation to gendered labor, but itself draws on Dalla Costa and James's *The Power of Women and the Subversion of the Community* (1972), which first theorized domestic labor as productive of labor-power rather than merely ancillary to it. The move from 'overlooked' to 'structurally illegible' is the key escalation.
{{< /note >}}
{{< note ref="NOTE // operational debt" >}}
The partition between consensus theory and operational runbooks echoes what Saltzer, Reed, and Clark (1984) codified as the end-to-end argument: push complexity to the edges. But here the claim is stronger—that the 'edges' absorb not residual complexity but constitutive labor, and that the architectural elegance of the core is purchased by this displacement.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="02" label="§ II.  What Care Knows That Consensus Doesn't" >}}

{{< gutter sigil="epistemic care" >}}

{{< main-col >}}

<p>María Puig de la Bellacasa's *Matters of Care* makes a philosophical move that initially seems too soft for systems engineering. She distinguishes between three stances toward knowledge: matters of *fact* (the positivist's settled truth), matters of *concern* (Latour's contested assemblages of human and nonhuman actors), and matters of *care* (relations that require ongoing, non-sovereign, asymmetric attention to persist). Her argument is that care is not a sentiment or an ethic bolted onto knowledge practices from outside. Care is an *epistemic mode*—a way of knowing that emerges specifically from the labor of maintaining relations that would otherwise fall apart.</p>

<p>The critical question is what this framework reveals that a pragmatic engineering sensibility doesn't already know. After all, SRE teams already practice something like care-based coordination. Incident response tolerates partial knowledge as a matter of course. Circuit breakers treat degraded operation as a first-class design concern rather than a failure mode. Gradual rollouts embody a relationship to uncertainty that looks nothing like consensus finality. A reasonable engineer might say: we already know maintenance matters, we already build for it, we don't need continental philosophy to tell us to write better health checks.</p>

<p>But Puig de la Bellacasa's framework does something that pragmatic engineering sensibility cannot: it explains *why these practices resist formalization*. Care, in her account, is not a property or a protocol. It is a mode of relating that is constitutively ongoing, partial, and non-sovereign—meaning it cannot be captured by a system that requires termination, totality, and authority to function. Health checks are not an underdeveloped consensus protocol waiting for better theory. They are a fundamentally different kind of coordination, one that maintains relatedness across difference rather than resolving difference into agreement. The SRE team practicing incident response is not doing informal consensus. They are doing something for which consensus theory has no vocabulary, and Puig de la Bellacasa gives us the vocabulary to say *what* that something is and *why* it resists the dominant formalization.</p>

<p>Here is the specific prediction her framework generates, which pragmatic engineering alone does not: any attempt to formalize care-based coordination *as a consensus protocol*—to give it safety properties, termination guarantees, leader election—will destroy exactly the features that make it effective. It will take the ongoing and make it terminal. It will take the partial and make it total. It will take the non-sovereign and install a leader. This is not a claim that formalization is always bad. It is a claim that the *particular* formalization embodied by consensus theory is structurally incompatible with care-based coordination, and that this incompatibility is not a temporary limitation but a consequence of the founding abstractions.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Puig de la Bellacasa 2017" >}}
Puig de la Bellacasa's tripartite distinction (fact/concern/care) is a deliberate intervention against Latour's *Reassembling the Social* (2005), which she reads as still sovereign in its analytic posture—'concern' maps controversies but does not commit to sustaining the things it studies. Care as epistemic mode imports feminist standpoint epistemology (Haraway, Harding) into STS without reducing it to identity politics.
{{< /note >}}
{{< note ref="NOTE // anti-formalization" >}}
The prediction that formalizing care-as-consensus destroys it parallels James C. Scott's *Seeing Like a State* (1998): legibility projects that succeed on their own terms systematically annihilate the mētis (practical knowledge) they depend on. The essay's specific claim—that consensus theory's founding abstractions (termination, totality, authority) are the mechanism of destruction—is tighter than Scott's, which locates the failure in scale rather than in the logic of the formalism itself.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="03" label="§ III.  Probabilistic Consensus and the Question of Partial Agreement" >}}

{{< gutter sigil="falsify" >}}

{{< main-col >}}

<p>The sharpest objection to this thesis comes not from traditional consensus research but from its heterodox margins. Avalanche-family protocols, for instance, achieve consensus through repeated random subsampling—each node queries a small random subset of peers, shifts its preference toward the local majority, and repeats until the network converges. There is no leader. There is no single authoritative proposal. Agreement emerges probabilistically, through iterated local interactions, with a tunable confidence threshold. Variable-quorum systems like those in Byzantine quorum protocols allow different operations to require different levels of agreement, making partial consensus a design parameter rather than a failure mode. These are not Raft. They are not Paxos. Do they falsify the claim?</p>

<p>They pressure it productively. Avalanche genuinely relaxes sovereignty—there is no leader election, no single point of authority. Variable quorums genuinely accommodate partial agreement. But notice what neither system relaxes: *finality*. Both still aim to terminate. Both still resolve ambiguity into a single agreed-upon state. Both still treat the purpose of coordination as producing a *value* that all participants can treat as settled. The ongoing, non-terminal, repair-oriented mode that Puig de la Bellacasa identifies as care is not what these systems formalize. They formalize a more democratic, less authoritarian path *to the same destination*: enclosure of ambiguity into agreement.</p>

<p>This distinction matters because it clarifies what the thesis is actually claiming. The claim is not that all consensus protocols require strong leadership, or that all require unanimous agreement, or that all require deterministic guarantees. The claim is that consensus theory as a field is organized around the *production of finality*—the conversion of contested, ambiguous, multiply-held state into singular, settled, extractable truth—and that this orientation toward finality is what renders care-based coordination formally inexpressible within it. Probabilistic consensus is a more humane enclosure. It is enclosure nonetheless.</p>

<p>A genuine falsification would require a formally specified protocol whose *safety property* is not eventual agreement on a single value but rather the *maintenance of productive disagreement*—a protocol that treats the ongoing coexistence of multiple partial truths as the thing to be preserved rather than the problem to be solved, and that treats repair of relations between divergent nodes as a core guarantee rather than a liveness heuristic. I am not aware of such a protocol. CRDTs approach something like it—they allow divergent local states that merge deterministically—but they achieve this precisely by *abandoning global consensus*, which does not reform the paradigm so much as exit it. The exit is telling.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Rocket-Snowflake 2020" >}}
The Avalanche protocol (Team Rocket, 2020) is indeed leaderless and probabilistic, but its metastability-based convergence still presupposes a single decided bit per transaction. The essay's identification of finality-as-invariant across heterodox consensus variants echoes Aguilera's taxonomy (2004), which classifies protocols by communication pattern and failure model but never relaxes the assumption that the output is a single decided value.
{{< /note >}}
{{< note ref="NOTE // CRDTs as exit" >}}
Shapiro et al. (2011) designed CRDTs precisely to avoid coordination, not to reform it. That CRDTs resolve divergence through algebraic merge functions (join-semilattices) rather than through agreement protocols is taken here as evidence that the consensus paradigm cannot be internally reformed to accommodate persistent multiplicity—one must leave its grammar entirely. The exit is indeed telling: it suggests the boundary of consensus is a logical, not merely historical, artifact.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="04" label="§ IV.  A Novel Primitive: Maintenance Quorums" >}}

{{< gutter sigil="tending" >}}

{{< main-col >}}

<p>If this analysis is to be more than critique—if it is to produce something an engineer could build—it needs to generate at least one design idea that does not already exist in practice.</p>

<p>Here is a sketch. Consider a coordination primitive I'll call a *maintenance quorum*: a protocol whose safety property is not "all participants agree on value V" but rather "no participant's state has diverged from any other participant's state by more than threshold T for longer than duration D without a repair attempt being initiated." The thing being guaranteed is not agreement but *bounded neglect*. The protocol does not elect a leader; it detects *abandonment*—nodes that have drifted too far from the relational web without anyone noticing or acting. Its failure mode is not split-brain (two leaders, two truths) but *orphaning* (a participant left unattended beyond the acceptable threshold). Its progress condition is not "a value is eventually chosen" but "all relations are eventually tended."</p>

<p>This is not a consensus protocol. That is the point. It is a *care protocol*—a coordination primitive organized around Puig de la Bellacasa's insight that what matters is not the production of shared truth but the maintenance of shared relation. It could operate alongside consensus protocols rather than replacing them, providing formal guarantees about the very maintenance labor that consensus theory currently externalizes. An implementation might look like an anti-entropy protocol with formally specified repair obligations rather than best-effort gossip synchronization—something between Dynamo's read repair and a protocol with actual safety guarantees about relational integrity.</p>

<p>Whether this is buildable in a way that is both formally tractable and operationally useful is genuinely uncertain. But its *conceivability* does something important: it demonstrates that the absence of care-based coordination primitives in the consensus literature is a consequence of specific definitional choices, not of logical necessity. We could formalize maintenance. We have chosen not to. And as distributed systems increasingly underpin social infrastructure—identity systems, financial networks, governance platforms—that choice becomes material. Every Raft cluster is a small political commitment to the principle that the purpose of coordination is the production of authoritative, singular truth. Every health check running outside that cluster's formal guarantees is a reminder that what actually keeps the system alive has no theory, no proof, and no name.</p>

<p>The question is not whether to abolish consensus. It is whether to keep pretending that consensus is all there is—that the formal, visible, celebrated economy of agreement can continue to free-ride on the informal, invisible, necessary economy of care, without ever acknowledging the debt.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // bounded neglect" >}}
The 'maintenance quorum' inverts the classical safety/liveness pair: its safety property (bounded divergence with guaranteed repair initiation) is closer to what Alpern and Schneider (1985) would classify as a liveness property, since it concerns eventual action rather than invariant preservation. This categorical slippage may be the point—care-based primitives may require rethinking the safety/liveness distinction itself, not merely adding new protocols within it.
{{< /note >}}
{{< note ref="REF // DeCandia et al. 2007" >}}
Dynamo's read-repair and anti-entropy gossip (DeCandia et al., 2007) are the closest extant ancestors of what is proposed here, but they carry no formal repair *obligation*—repair is best-effort, governed by heuristic timers, and invisible to Dynamo's consistency model (eventual convergence via vector clocks). The leap from 'protocol with repair heuristics' to 'protocol whose safety guarantee is relational maintenance' is nontrivial and would require, at minimum, a formal model of 'abandonment detection' analogous to Chandra and Toueg's (1996) failure detector hierarchy.
{{< /note >}}
{{< /margin >}}
