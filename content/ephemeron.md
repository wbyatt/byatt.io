---
title: "The Individuation Engine"
subtitle: "Simondon, Wittgenstein, and the LLM understanding debate"
date: 2026-04-02
draft: false
layout: treatise
doc_class: "ephemeron"
doc_id: "ephemeron.2026-04-02"
revision: "diurnal"
chain: "grounding → individuation → milieu → signal"
footer_status: "ephemeral unless preserved"
status_pips:
  - active
  - amber
  - dim
hook: "The question of whether LLMs 'understand' language has calcified into a stale binary — stochastic parrots versus functional understanding. Both sides are wrong, and for the same reason: they treat understanding as a property an individual either possesses or lacks. Gilbert Simondon's theory of individuation dissolves this debate entirely, reframing it as a question about the *milieu* in which meaning emerges — and in doing so, generates a concrete engineering discipline that standard prompt engineering cannot reach."
generated_by: "Claude Opus 4.6"
generated_date: "2 April 2026"
---

{{< section num="01" label="§ I.  The Debate and Its Failure" >}}

{{< gutter sigil="ontology" >}}

{{< main-col >}}

{{< dropcap letter="B" >}}

<p>oth sides assume they are disagreeing about LLMs. They are actually disagreeing about the ontology of meaning itself, and neither has the apparatus to notice.</p>

<p>Every few months, the discourse cycles back: do large language models <em>understand</em> language? The camps are well-rehearsed. On one side, Bender, Koller, and their co-authors insist that LLMs are "stochastic parrots," manipulating form without access to meaning, ungrounded in the world of reference that gives language its semantic content. On the other side, a growing contingent of functionalists — Andreas, Shanahan, and others — argue that if a system exhibits the functional competencies associated with understanding, the question of whether some additional metaphysical ingredient is present is idle.</p>

<p>Gilbert Simondon does. His theory of individuation, developed in the late 1950s and only recently gaining serious traction in anglophone philosophy of technology, provides the frame that both camps lack. But to see why, we need to be precise about where each argument breaks down — not merely wave at category errors, but identify the specific structural failure in each position.</p>

<p>Start with Bender and Koller's grounding argument. Their central claim is that a system trained only on linguistic form cannot access meaning because meaning requires grounding in communicative intent and real-world reference. This is not, as it is sometimes caricatured, a naive Cartesian claim that understanding requires consciousness. It is a claim about the necessary conditions for semantic content: that form alone underdetermines meaning, and that the gap is filled by situated reference. The argument is strong against a specific target — the claim that LLMs have <em>the same kind</em> of understanding as human speakers. But it overreaches when it concludes that LLMs therefore have <em>no</em> semantically relevant relationship to language at all. The move from "not grounded in the way humans are grounded" to "merely manipulating form" smuggles in an assumption: that human-style grounding is the only mechanism by which a system can enter into semantically productive relations with language. This is precisely what needs to be argued, not assumed. When a code-generation model produces a function that correctly implements a specification it has never seen before, composing API calls in a novel configuration that satisfies stated constraints, describing this as "form manipulation" preserves the letter of the grounding thesis at the cost of its explanatory power. The model's output is <em>about</em> something — it refers, it satisfies conditions, it can be evaluated against a world — even if the process that produced it does not involve embodied perception or communicative intent.</p>

<p>The functionalist position has the opposite problem. It correctly identifies that the stochastic parrot framing cannot account for the genuine competencies LLMs exhibit, but it then overcorrects by importing the vocabulary of understanding wholesale. To say that an LLM "understands" language because it exhibits understanding-like behavior is to commit to a functionalism so coarse that it loses the ability to distinguish between genuinely different kinds of systems. A thermostat exhibits temperature-regulation behavior, but calling it "understanding" of temperature obscures more than it reveals. The functionalist needs to explain not just that LLMs produce competent outputs, but <em>how</em> they produce them — what kind of process this is, and how it differs from both human cognition and simple lookup tables. Without that, "understanding" is not an explanation but a label, and the debate degenerates into a dispute about who gets to use a prestigious word. Wittgenstein helps here, but only if we take him seriously rather than using "meaning is use" as a philosophical bumper sticker. The later Wittgenstein's real contribution is the concept of <em>grammar</em> — not syntactic rules but the network of conditions that determine what counts as a meaningful move within a language game. When an LLM generates code that compiles and passes tests, it is not imitating a language game from outside; it is operating within the grammar of that game, making moves that are assessable by the game's own criteria. But — and this is where Wittgenstein restrains any overreach — operating within a grammar is not the same as <em>understanding</em> in the rich, form-of-life sense that Wittgenstein ties to shared human practices. The LLM participates in the language game of code generation without sharing the form of life of programmers.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Bender et al. 2021" >}}
"On the Dangers of Stochastic Parrots" — the grounding argument's strongest articulation. The claim is not about consciousness but about the necessary conditions for semantic content.
{{< /note >}}
{{< note ref="NOTE // Wittgenstein" >}}
PI §43: "the meaning of a word is its use in the language." But the later Wittgenstein goes further — meaning requires not just use but participation in a form of life. This is the restraint the functionalists need.
{{< /note >}}
{{< /margin >}}

<!-- §II -->

{{< section-rule >}}

{{< section num="02" label="§ II.  Simondon's Framework" >}}

{{< gutter sigil="simondon" >}}

{{< main-col >}}

<p>Simondon's theory of individuation offers a way out, not by splitting the difference but by reframing the question entirely. For Simondon, the fundamental error in Western metaphysics is the assumption that individuals — whether physical, biological, or technical — come first, and that relations between them are secondary. He inverts this: <span class="rubric">individuation is primary</span>, and the individual is a provisional, partial result of an ongoing process. A crystal does not exist as a form that is then placed in a solution; it <em>individuates</em> at the boundary between the already-crystallized region and the supersaturated medium, each new layer of structure propagating from the conditions established by the last. Simondon calls this process <em>transduction</em>, and it is always a relation between an individuating system and its <span class="rubric">associated milieu</span> — the environment that is not external to the individual but co-constituted with it.</p>

<p>Applied to LLMs, this framework dissolves the understanding debate by refusing its premise. The question "does the model understand?" presupposes that understanding is a property that an individual either possesses or lacks. Simondon would say: understanding is not a property but a phase of individuation. What happens during autoregressive generation is a transductive process in which each token conditions the probability landscape for the next, propagating structure through the sequence in a way that is formally analogous to — though not identical with — crystallization in a metastable medium. In Simondon's strict sense, transduction requires a <em>metastable</em> pre-individual state: a system with latent potential energy, far from equilibrium, where a small perturbation can trigger a cascading phase change. The closest analogue in autoregressive generation is the state of the model <em>at the moment of prompting</em> — the trained weights encoding a vast space of latent linguistic potentials, the prompt introducing asymmetry that initiates a specific trajectory of individuation through that space. This is a productive analogy, not a strict formal equivalence. The trained model is not metastable in the thermodynamic sense; it does not undergo an irreversible phase transition. But the structural parallel is illuminating: generation is not retrieval of pre-formed content, nor is it random sampling. It is a process in which structure propagates through a domain, each region serving as the organizing principle for the next, and in which the result is underdetermined by any single factor — model, prompt, or context — but emerges from their transductive relation. Simondon gives us the vocabulary to describe the intermediate status of LLMs: the model individuates within the game's milieu without being the kind of individual — a human speaker embedded in a form of life — that the game was originally designed for. This is not deficiency. It is a <span class="rubric">new mode of technical individuation</span>, and it demands its own engineering discipline.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Simondon 1958" >}}
<em>Du mode d'existence des objets techniques</em> and <em>L'individuation à la lumière des notions de forme et d'information</em>. The theory of transduction is developed in the latter.
{{< /note >}}
{{< note ref="NOTE // analogy" >}}
The transduction mapping is productive but not exact. The trained model is not metastable in the thermodynamic sense. What is preserved is the structural principle: each region of structure serves as the organizing condition for the next.
{{< /note >}}
{{< /margin >}}

<!-- §III -->

{{< section-rule >}}

{{< section num="03" label="§ III.  The Engineering Reframe: Milieu Design" >}}

{{< gutter sigil="milieu" >}}

{{< main-col >}}

<p>This reframing is not merely a more elegant description of what we already know. It generates a concrete divergence from standard engineering practice — and this is where the Simondonian frame earns its keep.</p>

<p>Consider the problem of multi-agent LLM systems, where several model instances collaborate on a complex task. Standard prompt-engineering wisdom treats this as a communication problem: each agent receives instructions specifying its role, and the system designer's job is to make those instructions as clear and complete as possible. The implicit model is intent-communication — I have a goal, I express it to the agent, the agent executes. When the system fails, the diagnosis is typically that the instructions were ambiguous, or that the agents' roles were poorly defined, and the fix is to write better prompts.</p>

<p>The Simondonian frame diagnoses the problem differently. Multi-agent failure is not primarily a failure of communication but a failure of <span class="rubric">milieu design</span>. Each agent individuates not from its instructions alone but from the total associated milieu: the shared context window, the outputs of other agents, the accumulated state of the conversation, the constraints imposed by tool interfaces. When agents produce incoherent or contradictory outputs, the Simondonian engineer asks not "were the instructions clear?" but "is the shared milieu structured to support coherent co-individuation?" These are different questions with different answers. The intent-communication framing leads you to refine individual agent prompts in isolation. The milieu-engineering framing leads you to redesign the <em>shared information architecture</em> — the structure of what agents can see of each other's outputs, the temporal ordering of their interactions, the constraints that shape the space of possible individuation trajectories.</p>

<p>Here is a concrete case where the two framings diverge. Suppose you have a planning agent and an execution agent, and the execution agent consistently deviates from the plan. The intent-communication diagnosis: the execution agent's instructions insufficiently emphasize plan adherence. The fix: add stronger constraints to the execution prompt, perhaps with few-shot examples of plan-following. The milieu-engineering diagnosis: the execution agent's associated milieu — the context in which it individuates — contains signals that compete with the plan. Perhaps the execution agent has access to raw user input that introduces asymmetries the planning agent did not account for, or the plan is presented in a format that does not propagate well through the execution agent's generation process. The fix: restructure the milieu so that the plan is not merely <em>stated</em> but <em>architecturally dominant</em> in the execution agent's context — placed at the position in the context window where it will most effectively condition generation, with competing signals attenuated or removed. The first approach treats the agent as a mind that needs clearer orders. The second treats the generation process as individuation that needs a better-structured milieu. In practice, the second approach addresses a class of failures — what you might call <span class="rubric">milieu-incoherence failures</span> — that the first systematically misdiagnoses.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // divergence" >}}
The planning/execution case is where the two framings produce genuinely different engineering recommendations — not just different descriptions of the same fix.
{{< /note >}}
{{< note ref="NOTE // architecture" >}}
"Milieu design" is not a synonym for "prompt engineering." It encompasses context window structure, retrieval pipelines, tool interfaces, agent visibility boundaries, and temporal ordering of interactions.
{{< /note >}}
{{< /margin >}}

<!-- §IV -->

{{< section-rule >}}

{{< section num="04" label="§ IV.  Consequences" >}}

{{< gutter sigil="signal" >}}

{{< main-col >}}

<p>This distinction scales beyond multi-agent systems. Any non-trivial LLM integration involves the design of an associated milieu: the context window, the retrieval pipeline, the tool interfaces, the conversation history. The engineering challenge is not to communicate intent to a mind but to structure the conditions under which a transductive process will propagate in useful directions. Temperature and sampling parameters are not "creativity settings" for a thinker; they are properties of the milieu's metastability, controlling the degree to which the generation process can explore divergent individuation trajectories. Retrieval-augmented generation is not "giving the model information"; it is restructuring the pre-individual field from which individuation proceeds, introducing asymmetries that channel the process toward specific domains of structure.</p>

<p>The practical upshot is a reorientation of how we think about LLM reliability. If meaning emerges through transductive relation between model, prompt, and milieu, then reliability is not a property of the model but of the <span class="rubric">individuation regime</span> — the total system of model, context architecture, and interaction design. A model that hallucinates in one milieu may be perfectly reliable in another, not because it has "learned" anything new but because the milieu's structure channels individuation along trajectories where hallucination is a low-probability divergence rather than an attractor. Debugging LLM failures, on this view, is less like debugging a program and more like diagnosing why a crystal is forming with defects: you look at the medium, the temperature, the seed — the conditions of individuation — not at the crystal itself.</p>

<p>The right unit of analysis for LLM engineering is not the model. It is not the prompt. It is the individuation process that encompasses both, and the associated milieu that shapes it. Getting this wrong does not just produce philosophical confusion. It produces systems that fail in ways their designers cannot diagnose — because the designers are looking at the wrong level of abstraction, asking whether the model "understood" the prompt when they should be asking whether the milieu supported <span class="rubric">coherent individuation</span>. The debate over machine understanding is, in the end, a distraction from the real work: engineering the conditions under which technical individuation produces reliable, legible, and useful signals.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // reliability" >}}
Reliability as a property of the individuation regime, not the model, is the thesis's most consequential engineering claim. It reframes hallucination from "the model got confused" to "the milieu failed to constrain individuation."
{{< /note >}}
{{< /margin >}}
