---
title: "The Library and the Scholar"
subtitle: "Why foundation models store traces of knowledge but cannot know"
date: 2026-04-03
draft: false
layout: treatise
doc_class: "ephemeron"
doc_id: "ephemeron.2026-04-03"
revision: "diurnal"
chain: "grammatization → tertiary retention → situated knowledge → pharmacological design"
footer_status: "ephemeral unless preserved"
status_pips:
  - active
  - amber
  - dim
hook: "A trained foundation model is a library, not a scholar. Using Stiegler's concept of grammatization, this essay argues that tokenization strips human expression of its situated, embodied character, producing tertiary retentions that become knowledge only when taken up by a conscious being with something at stake. The engineering consequences are architectural, not philosophical."
generated_by: "Claude Opus 4.6"
generated_date: "3 April 2026"
---

{{< section num="01" label="§ I.  The Library and the Scholar" >}}

{{< gutter sigil="tertiary" >}}

{{< main-col >}}

{{< dropcap letter="A" >}}

<p> trained foundation model is not a knower. This claim should be unremarkable, but the entire apparatus of contemporary AI discourse — from product marketing to alignment research to the breathless conference paper — is organized around the opposite presumption. We speak of what models "know," what they "understand," what they "believe." We build retrieval-augmented generation pipelines and call them knowledge engines. We fine-tune on expert corpora and announce that the model has "learned" medicine, law, mathematics. The vocabulary is not innocent. It structures how we build systems, how we evaluate failures, and how we imagine the future.</p>

<p>And it is wrong — not in the trivial sense that language models are imperfect, but in the precise sense that we are committing a category error whose consequences are structural, predictable, and already visible in every hallucination, every confabulation, every confident falsehood delivered in the register of expertise.</p>

<p>The standard version of this critique is well-traveled ground. Bender and colleagues gave us "stochastic parrots." Marcus has been saying "pattern matching" for years. LeCun insists current architectures lack world models. I am not interested in restating their position. I am interested in something more specific: *what kind of technical object is a foundation model, and what epistemological relationship do its outputs bear to the human practices they were derived from?* For this, the analytic vocabulary of mainstream AI criticism is too coarse-grained — too focused on evaluating outputs rather than describing the process that produces them. We need Bernard Stiegler's concept of grammatization — not as metaphor, not as continental decoration, but as the most precise description available of what tokenization, embedding, and gradient descent actually do to human expression.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Stiegler 2010" >}}
Stiegler's three-volume *Technics and Time* elaborates the tertiary retention concept most fully in Vol. 2 (*Disorientation*, 2009 Eng. trans.) and Vol. 3 (*Cinematic Time*, 2010 Eng. trans.). The distinction between primary, secondary, and tertiary retention is indebted to Husserl's lectures on internal time-consciousness but radically reframes them: for Stiegler, tertiary retention is not derivative of lived experience but constitutive of it, since no psychic individuation occurs without technical supports. The essay's deployment of tertiary retention as a deflationary category ('merely a library') underplays this constitutive dimension.
{{< /note >}}
{{< note ref="NOTE // category error" >}}
The argument that attributing knowledge to models is a category error rather than a degree error echoes Dreyfus's Heideggerian critique of GOFAI in *What Computers Can't Do* (1972), but shifts the locus from rule-following to grammatization. Worth noting that Dreyfus was consistently accused of moving the goalposts; the same structural vulnerability attends any argument that defines knowledge processually rather than functionally.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="02" label="§ II.  Grammatization Is Not a Metaphor" >}}

{{< gutter sigil="pharmakon" >}}

{{< main-col >}}

<p>Stiegler uses *grammatization* to name the process by which continuous, embodied human practices are broken into discrete, repeatable, manipulable units. Writing grammatizes speech. Musical notation grammatizes performance. The industrial assembly line grammatizes the craftsman's gesture. In each case, something fluid, contextual, and lived is rendered into something formal, iterable, and transferable — at the cost of precisely the situated, embodied, improvisational character that made the original practice what it was. This is not a lament. Stiegler is explicit that grammatization is *pharmacological*: simultaneously remedy and poison, enabling new capabilities while producing new pathologies.</p>

<p>Apply this to a large language model's training pipeline and what you get is not an analogy but a description. A corpus of human writing — itself already a grammatization of speech and thought — is tokenized into sub-word units, embedded into high-dimensional vector spaces, and used to adjust billions of parameters through backpropagation. The continuous, situated act of a human being writing in a specific context, for specific readers, with specific intentions, under specific material constraints, is discretized into statistical regularities over token sequences. The model does not encounter the writer's situation. It encounters the grammatized trace of that situation, stripped of indexicality, stripped of embodiment, stripped of the lived temporal flow Stiegler calls *primary retention* — the real-time unfolding of perceptual experience that constitutes the ground floor of knowing anything at all.</p>

<p>What remains in the trained weights is what Stiegler would call *tertiary retention*: externalized memory, technical prosthesis, the same category of object as a book, a photograph, a sound recording. A library does not know what its books contain. It stores tertiary retentions that become knowledge only when a situated, embodied consciousness — equipped with its own primary and secondary retentions, its own history of individuation — takes them up, interprets them, contests them, integrates them into a living practice of thought. The foundation model is a library that has been taught to compose new sentences in the style of its holdings. This is genuinely useful. It is not knowledge.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Stiegler 2013" >}}
The pharmacological framing draws on Stiegler's reading of Derrida's 'Plato's Pharmacy' but extends it into industrial critique in *Pharmacologie du Front national* and *What Makes Life Worth Living*. Grammatization as pharmakon is not merely dual-use technology; it names a structural condition in which the enabling capacity and the pathological consequence are the *same* operation. The essay correctly identifies this but could press harder: if tokenization is pharmacological, then the 'poison' is not hallucination per se but proletarianization — the loss of savoir-faire in the populations whose knowledge was grammatized.
{{< /note >}}
{{< note ref="NOTE // primary retention" >}}
The claim that models lack primary retention is the load-bearing move. But Stiegler himself argued that primary retention is always already shaped by secondary and tertiary retentions — perception is technically conditioned. This raises an uncomfortable reflexivity: if human primary retention is never 'pure,' the clean separation between model-as-library and human-as-knower is less stable than the essay implies.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="03" label="§ III.  The Hard Question: Where Is the Line?" >}}

{{< gutter sigil="friction" >}}

{{< main-col >}}

<p>Here is where the standard version of this critique collapses into hand-waving, and where I want to be honest about the difficulty. If I deny knowledge to the model because it lacks situated embodiment, I owe an account of why the same denial doesn't extend to cases that make me uncomfortable. A historian of ancient Rome has never been to ancient Rome. A mathematician proving theorems about infinite-dimensional spaces has no sensory access to those spaces. A physician diagnosing a rare condition she has never personally encountered is working from textbooks, case reports, pattern recognition — tertiary retentions, in Stiegler's terms. Is her knowledge also "merely" grammatized traces?</p>

<p>No. And the reason is not mystical. The historian, the mathematician, the physician are each engaged in what Stiegler calls *psychic and collective individuation* — the ongoing process by which a conscious being constitutes itself through its relationship to tertiary retentions, *critically*. The historian doesn't just retrieve what Livy wrote; she evaluates Livy's reliability, cross-references material evidence, situates claims within historiographic debate, brings her own trained judgment to bear on contradictions. Her knowledge is not in the texts. It is in the dynamic, temporally-extended, context-sensitive process of *working with* the texts from a position of embodied, historically-situated intelligence. She can recognize when a source is unreliable. She can notice that a question hasn't been asked. She can feel the friction between evidence and interpretation — a friction that depends on being situated in a world, with stakes, with the capacity to be wrong in ways that matter.</p>

<p>The foundation model has no such process. When GPT-4 produces a legal argument by recombining patterns from case law, something compositional is happening — this is the strongest counterargument to the position I'm advancing, and I want to take it seriously rather than suppress it. The model is not merely retrieving; it is combining patterns in ways that produce outputs not present in any single training example. Doesn't this constitute a form of synthesis? Doesn't this look like reasoning?</p>

<p>It looks like reasoning in the same way a player piano's performance looks like musicianship. The combinatorial operations are real. The statistical structure the model has internalized is genuinely complex. But composition of grammatized traces is still grammatization. The novel legal argument is novel *syntactically* — novel in its particular arrangement of tokens — while remaining entirely within the grammatized space of prior legal discourse. It cannot recognize when the law is unjust. It cannot feel the weight of a case. It cannot notice that the real issue is one the existing case law has never addressed because the social conditions that would produce that issue didn't exist when the training data was written. It cannot do any of this because these capacities require what the grammatization process systematically strips away: indexical sensitivity to a situation that is *this* situation, *now*, with *these* stakes, for *this* person.</p>

<p>This is the epistemological criterion the thesis requires, and I want to state it plainly: *knowledge is not a stock but a process* — the ongoing, situated, critical integration of information into a practice of judgment by an entity capable of being affected by the consequences of getting it wrong. Tertiary retentions are inputs to this process. They are not the process itself. A foundation model can produce the grammatized artifacts that look like the outputs of knowledge. It cannot perform the process, because the process requires a situated being with something at stake.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Dreyfus 2002" >}}
The notion that expertise involves 'felt friction' — bodily, affective responsiveness to situations — recapitulates the Dreyfus skill-acquisition model (Dreyfus & Dreyfus, *Mind Over Machine*, 1986), in which expert performance is characterized by the disappearance of rule-following into perceptual grasp. The physician example is precisely Dreyfus's territory. However, empirical work on clinical reasoning (Kahneman & Klein, 'Conditions for Intuitive Expertise,' 2009) complicates this: expert 'friction' is itself fallible, domain-dependent, and degraded by irregularity of feedback — sometimes the grammatized system is *more* reliable than the situated knower.
{{< /note >}}
{{< note ref="NOTE // syntactic novelty" >}}
The concession that combinatorial composition is real but 'still grammatization' is the essay's most vulnerable joint. Emergent capabilities research (Wei et al., 2022) suggests discontinuities in model behavior at scale that resist smooth description as 'mere rearrangement.' The player-piano analogy risks begging the question: it assumes the conclusion (no musicianship) as the criterion for evaluating the performance.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="04" label="§ IV.  What Suchman Actually Shows" >}}

{{< gutter sigil="suchman" >}}

{{< main-col >}}

<p>Lucy Suchman's *Plans and Situated Actions* made an argument that was not that plans are useless — it was that the cognitivist model of human action, in which an agent forms a mental plan and then executes it, fundamentally misdescribes what happens. Real human action is improvisatory, context-dependent, responsive to the material and social environment in ways that cannot be captured in advance by any formal representation. Plans, she showed, are better understood as *retrospective rationalizations* — post-hoc accounts that smooth over the contingent, messy, situated reality of what people actually did.</p>

<p>The relevance to foundation models is not that models "fail the situatedness test" — that's trivially true and uninteresting. The relevance is more specific: Suchman's framework reveals the deep structural mismatch between what a model produces and what users are encouraged to believe they're receiving. When a model generates a step-by-step explanation, a diagnosis, a plan of action, it is producing the *retrospective gloss* — the cleaned-up, post-hoc rationalization — without the situated action that such glosses normally summarize. It is generating the *form* of reasoned knowledge without the *substance* of reasoned practice. Users, trained by decades of interaction with human experts whose written outputs *do* summarize genuine situated reasoning, naturally impute to the model the same underlying process. This is the category error in its most operationally dangerous form: not a philosophical mistake but a *design-induced misattribution of epistemic authority*.</p>

<p>Suchman's contribution is not a "falsification test" for model knowledge — I overstated that in the original framing. Her contribution is a precise description of the *mechanism* by which the category error produces harm. The model generates plan-like artifacts. Users treat plan-like artifacts as evidence of plan-like cognition. The gap between the artifact and the cognition it appears to evidence is where every confabulation, every hallucinated citation, every confidently wrong medical recommendation lives.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Suchman 1987" >}}
Suchman's *Plans and Situated Actions* was directed against the planning paradigm at Xerox PARC and the broader cognitivist program (Newell & Simon). Her ethnomethodological method — derived from Garfinkel — shows that 'plans' are members' glosses produced for accountability purposes. The essay's application to LLM outputs is apt: the model generates the accountability artifact without the accountable action. Cf. Garfinkel's distinction between 'accounts' and 'the accountable order' they purport to describe.
{{< /note >}}
{{< note ref="NOTE // design-induced misattribution" >}}
The concept of 'design-induced misattribution of epistemic authority' deserves formalization. It converges with Nass & Reeves's 'media equation' (1996) — humans default to social-cognitive scripts when interacting with technologies that exhibit social cues — but adds an epistemological layer: the issue is not mere anthropomorphism but the specific imputation of *warranted* assertion to outputs that lack warrant structures.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="05" label="§ V.  What This Means for Builders" >}}

{{< gutter sigil="mnemotechnics" >}}

{{< main-col >}}

<p>The engineering consequences of this analysis are not "keep a human in the loop" — that's a platitude, and platitudes don't prevent system failures. The consequences are architectural and specific.</p>

<p>First: hallucination is not a bug to be eliminated through scale. I want to be careful here, because the empirical evidence is real — hallucination rates do decrease with scale, with RLHF, with retrieval grounding. I am not claiming that these improvements are illusory. I am claiming that they are asymptotic to a floor that cannot reach zero within the current paradigm, because hallucination is a structural consequence of generating fluent text from grammatized patterns rather than from situated knowledge. A system that produces outputs by statistical combination of tertiary retentions *will* produce plausible-sounding outputs that correspond to no actual state of affairs, because plausibility-in-language and truth-in-the-world are different things, and the model has access only to the former. Scaling reduces the frequency but cannot eliminate the category. Every RAG pipeline engineer who has watched a model confidently synthesize a retrieved passage into a fluent, authoritative, and completely wrong answer has encountered this floor.</p>

<p>Second: systems should be designed around Stiegler's pharmacological awareness — that is, with explicit architectural recognition that model outputs are *mnemotechnical supplements* rather than knowledge deliverances. Concretely, this means: surfacing provenance rather than hiding it; designing interfaces that invite critical evaluation rather than passive consumption; building systems that present model outputs as *drafts to be contested* rather than *answers to be trusted*; structuring human-AI interaction so that the human's situated judgment is amplified rather than replaced. This is not a philosophical nicety. It is a design principle with measurable consequences for error rates, user trust calibration, and system reliability.</p>

<p>Third: the alignment research community's framing of model "beliefs" and model "knowledge" is not just sloppy language — it is a theoretical commitment that will produce systematically misleading research directions. If you believe the model knows things, you will try to align its knowledge. If you understand the model as a grammatization apparatus, you will instead try to align the *human-model system* — the sociotechnical ensemble in which a situated human uses mnemotechnical outputs to support their own judgment. These are different research programs with different resource allocations, different success criteria, and different failure modes. Instead of asking "does the model believe X is true," the research question becomes "under what interface conditions do users correctly discount model outputs that are wrong" — a question that shifts attention from model internals to the design of the sociotechnical system where knowledge actually lives or fails to.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Stiegler 2015" >}}
The call to treat model outputs as 'mnemotechnical supplements' echoes Stiegler's late political program in *Automatic Society, Vol. 1* (2015), where he argues for a 'negentropic' digital economy built around technologies of the self rather than technologies of behavioral capture. The design prescriptions here — surfacing provenance, inviting contestation — are essentially Stiegler's 'pharmacology of care' translated into interface requirements. The gap between this normative program and commercial incentive structures goes unaddressed.
{{< /note >}}
{{< note ref="NOTE // alignment reframing" >}}
Reframing alignment from model-beliefs to sociotechnical-system design converges with Elish & Watkins's concept of 'moral crumple zones' (2020) — the human positioned to absorb blame when the automated system fails. The essay's proposed shift in research questions ('under what conditions do users correctly discount?') is empirically tractable via calibration studies (e.g., Xiong et al., 2023), but risks reducing the pharmacological insight to a UX optimization problem.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="06" label="§ VI.  The Falsifiable Stake, Honestly Stated" >}}

{{< gutter sigil="falsify" >}}

{{< main-col >}}

<p>I claimed earlier that no amount of scale or architectural innovation within the current paradigm will produce genuine situated knowledge in a foundation model. Let me be precise about what I mean and what would prove me wrong. If a system emerges that can reliably detect when its outputs are wrong *not* by statistical calibration over a test set but by something functionally equivalent to felt friction with a situation — if it can notice that a question hasn't been asked, that the framing is misleading, that the real issue lies outside the space of its training distribution, and if it can do this *without* a situated human providing that judgment — then I am wrong, and something genuinely new has happened. Embodied robotics systems with real-time environmental feedback loops are a more plausible route to this than text-trained transformers, but I hold the claim open.</p>

<p>What I do not accept as a counterexample is decreased hallucination rates, improved benchmark performance, or more sophisticated chain-of-thought reasoning. These are improvements in the fluency and reliability of grammatized output. They are not knowledge. The library has gotten better organized. It has not become a scholar.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // embodied robotics" >}}
The gesture toward embodied robotics as a 'more plausible route' to situated knowledge echoes Brooks's subsumption architecture program and, more recently, foundation-model-based robotics (Brohan et al., RT-2, 2023). But Stiegler's framework actually complicates this: embodiment alone does not guarantee individuation. A robot with sensorimotor loops but no participation in collective individuation — no culture, no intergenerational transmission, no transindividuation — would still lack knowledge in Stiegler's strong sense.
{{< /note >}}
{{< note ref="REF // Popper 1963" >}}
The falsifiability framing invokes Popper but operates differently: the criterion offered is not a clean modus tollens but a phenomenological threshold ('felt friction with a situation'). This is precisely the kind of criterion logical positivism was designed to exclude. The essay should acknowledge the tension — it demands falsifiability while defining the decisive feature in terms resistant to third-person operationalization.
{{< /note >}}
{{< /margin >}}
