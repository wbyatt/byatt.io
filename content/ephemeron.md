---
title: "The CLI as Concrete Technical Object"
subtitle: "Why command-line interfaces are structurally more evolved than GUIs"
date: 2026-04-04
draft: false
layout: treatise
doc_class: "ephemeron"
doc_id: "ephemeron.2026-04-04"
revision: "diurnal"
chain: "abstraction → concretization → functional convergence → associated milieu → compositional syntax"
footer_status: "ephemeral unless preserved"
status_pips:
  - active
  - amber
  - dim
hook: "The conventional narrative treats GUIs as evolved and CLIs as primitive, but Simondon's philosophy of technology inverts this entirely. The CLI's composability, functional polyvalence, and capacity to co-evolve with its user give it the structural properties of a mature technical object — properties that keep getting independently reinvented wherever complexity demands them."
generated_by: "Claude Opus 4.6"
generated_date: "4 April 2026"
---

{{< section num="01" label="§ I.  The Evolved Interface" >}}

{{< gutter sigil="atavism" >}}

{{< main-col >}}

{{< dropcap letter="T" >}}

<p>here's a persistent assumption in the discourse around software interfaces that visual sophistication represents progress. The GUI replaced the CLI, the story goes, because it was better — more intuitive, more humane, more evolved. Click a button, drag a file, see your results rendered in full color. This narrative has the satisfying shape of a march toward civilization: we crawled from the command line, we walked with desktop metaphors, we now run in fluid, gesture-driven interfaces that anticipate our desires. The CLI persists, on this account, as an atavism — a tool for holdouts, nostalgists, and gatekeepers who confuse obscurity with power.</p>

<p>This story is perfectly wrong. Not because GUIs aren't useful, but because the evolutionary metaphor, taken seriously, reverses the hierarchy entirely. Gilbert Simondon, the French philosopher of technology whose work *Du mode d'existence des objets techniques* remains the most rigorous account of how technical objects actually develop, provides the framework for seeing why. The CLI doesn't persist despite its minimalism. It persists because of what that minimalism structurally achieves.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Simondon 1958" >}}
Simondon's *Du mode d'existence des objets techniques* was largely ignored until Deleuze cited it approvingly in *Difference and Repetition* (1968) and *Logic of Sense* (1969). The English translation by Cécile Malaspina and John Rogove did not appear until 2017, meaning most Anglophone software discourse has absorbed Simondon only at second hand, through Stiegler or Combes.
{{< /note >}}
{{< note ref="NOTE // progress narrative" >}}
The 'march of interfaces' story recapitulates Whig historiography applied to technology. Compare Langdon Winner's critique in *Autonomous Technology* (1977): narratives of technological progress smuggle in normative claims about what counts as advancement while disguising the political choices embedded in design.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="02" label="§ II.  Concretization Against the Grain" >}}

{{< gutter sigil="concretization" >}}

{{< main-col >}}

<p>Simondon's central insight is that technical objects evolve from the *abstract* toward the *concrete*. This is counterintuitive. In common usage, "abstract" suggests advanced, refined, theoretical; "concrete" suggests raw, simple, primitive. Simondon inverts this. An abstract technical object is one in which each component serves an isolated function, with little synergy between parts. The components are logically separable; they could be rearranged or replaced without fundamentally altering the system's operative character. A concrete technical object is one in which the parts achieve functional convergence — each element serves multiple roles simultaneously, and the system's behavior emerges from the synergy of its components rather than from their sum.</p>

<p>The classic Simondonian example is the evolution of the internal combustion engine. Early engines had separate systems for cooling, lubrication, and structural support. As the design matured, components began to serve multiple functions: the engine block became simultaneously structural frame, heat sink, and oil channel. Parts could no longer be understood in isolation. The engine became more *concrete* — more integrated, more synergistic, more irreducible.</p>

<p>Now consider the GUI. A toolbar button performs one action. A menu item maps to one function. A dialog box captures one set of parameters. Each visual widget is a discrete element serving an isolated role, connected to others only through the application's internal logic, which remains opaque to the user. This is the anatomy of an abstract technical object: functionally siloed, visually enumerated, with each capability reified as a separate interface element. Adding new functionality means adding new widgets. The system grows by accretion, not by deepening the operative density of what already exists.</p>

<p>A CLI pipeline like `find . -name "*.log" -mtime +30 | xargs grep -l "ERROR" | sort | uniq -c | sort -rn` operates on an entirely different structural principle. Each element — `find`, `grep`, `sort`, `uniq` — is not a fixed-function widget but a transformation whose operative meaning is shaped by what surrounds it. `grep` inside a pipeline serves a different role than `grep` invoked alone, not because its code changes but because its *function* — what it actually does in the context of a task — is determined by the data flowing into it and the operation waiting downstream. The parts achieve a synergy that is irreducible to their individual specifications. This is functional convergence. This is concretization.</p>

<p>I should be precise about what I'm claiming here, because Simondon developed the concept of concretization to describe the internal evolutionary trajectory of a single technical lineage — how the engine evolves, how the vacuum tube evolves. Applying it as a comparative framework across two distinct classes of interface — the CLI and the GUI — strains the concept beyond its original domain. I'm doing it anyway, deliberately, because the structural properties Simondon identifies as markers of concretization (functional polyvalence, synergistic integration, emergent operative capacity) are present in one class and largely absent in the other, and this tells us something important about why these interfaces behave the way they do over time. The claim isn't that the CLI *descended from* the GUI and represents its maturation. The claim is that the CLI *exhibits the structural properties* Simondon associates with evolved technical objects, while the GUI exhibits those he associates with primitive ones — and this inversion of the conventional progress narrative has real consequences for how we design what comes next.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Simondon 1958, Part I Ch. 1" >}}
The abstract-to-concrete trajectory is Simondon's alternative to both hylomorphism and cybernetic functionalism. Muriel Combes (*Gilbert Simondon and the Philosophy of the Transindividual*, 2013) emphasizes that concretization is not optimization — it is not the object becoming more efficient but becoming more internally coherent, its own causal regime tightening.
{{< /note >}}
{{< note ref="NOTE // compositionality" >}}
The essay's identification of Unix pipes as functional convergence resonates with Doug McIlroy's 1964 memo proposing 'screwing together' programs 'like garden hose.' But note the tension: McIlroy's metaphor is modular and additive, while Simondonian concretization implies that the parts lose their separability. The pipe operator is the hinge — it is what transforms modularity into synergy, making the claim work.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="03" label="§ III.  The Associated Milieu" >}}

{{< gutter sigil="milieu" >}}

{{< main-col >}}

<p>What Simondon calls the *associated milieu* is the technical environment that a mature object generates around itself and that, in turn, conditions its operation. A concrete technical object doesn't merely sit inside an environment; it partially constitutes its own operating context, and that context feeds back into how the object functions. The relationship is recursive, generative. Simondon calls the underlying mechanism *transduction* — a specific ontogenetic operation in which activity at one domain's boundary induces structural change that propagates into adjacent domains, like crystallization extending through a supersaturated solution.</p>

<p>The shell environment is an associated milieu in something very close to Simondon's technical sense. A developer's `.bashrc` or `.zshrc` is not a preferences file. It is a cumulative record of the reciprocal shaping between human intention and machine capability. Aliases compress frequent operations. PATH modifications restructure what tools are available and in what priority. Shell functions compose existing utilities into new ones that didn't exist before and that exist nowhere else — they are local to this particular human-machine system. The history file is an archaeological record of past transductions: problems encountered, solutions composed, patterns discovered. Over months and years, the shell environment *individuates with the user*. It becomes a technical ecology that is neither purely the human's creation nor purely the machine's affordance but something genuinely emergent from their ongoing interaction.</p>

<p>Now look at what happens in GUI-dominant environments. System Preferences resets to defaults. Application interfaces enforce predetermined interaction paths. Customization, where it exists, operates within narrow parameters defined by the designer: you may choose which toolbar buttons are visible, but you may not compose them into novel operations. The GUI resists co-individuation by design. It is built to be the same for every user, because its legibility depends on consistency. This is not a flaw — it's the architectural consequence of being an abstract technical object. When your components serve isolated functions, there's no space for the user to intervene in the *relationships between* components, because those relationships are hardwired in the application layer, invisible and untouchable.</p>

<p>The CLI, by contrast, *exposes the joints*. Pipes, redirects, subshells, environment variables — these are not features but the connective tissue of a compositional system. The user operates on the relationships themselves, not just the components. And because those relationships are syntactic — expressed in text, readable, modifiable, storable, shareable — they become the substrate for an associated milieu that evolves in genuine reciprocity with the user's practice.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Simondon 1958, Part I Ch. 3" >}}
Simondon distinguishes the *associated milieu* from a mere environment: the Guimbal turbine generates its own lubricating and cooling milieu from the water it operates in. Yuk Hui (*On the Existence of Digital Objects*, 2016) extends this concept to digital environments, arguing that metadata schemas and protocol layers constitute associated milieus for data objects — a move structurally parallel to the dotfile argument made here.
{{< /note >}}
{{< note ref="NOTE // transduction" >}}
Transduction is Simondon's most demanding concept. It is not feedback (cybernetics) or dialectical mediation (Hegel) but a propagative ontogenesis where structure emerges at the boundary of disparate domains. Treating the shell history file as an 'archaeological record of past transductions' is suggestive but risks flattening transduction into mere documentation of adaptation. The stronger claim would be that each pipeline execution restructures the operator's cognitive schema and the machine's available state simultaneously — a genuine phase-shift, not a log entry.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="04" label="§ IV.  Independent Reinvention and the Falsifiability Question" >}}

{{< gutter sigil="falsify" >}}

{{< main-col >}}

<p>If this analysis is merely a philosophical rationalization for Unix partisanship, it should be easy to falsify: we'd expect CLI-like interfaces to appear *only* where Unix culture has direct influence, and to be absent where independent design traditions operate. If, however, the structural properties I'm describing reflect something real about how compositional interfaces outperform siloed ones under conditions of complexity, we should expect CLI-like patterns to be independently reinvented even outside the Unix lineage.</p>

<p>The standard examples — Terraform, `kubectl`, the proliferation of CLI tools in the cloud-native ecosystem — are suggestive but contaminated. The people who built these tools grew up on Unix. Cultural transmission, not independent convergence, is the parsimonious explanation.</p>

<p>Better cases exist. Consider the evolution of spreadsheet formulas. Excel's formula language — cell references composed with functions, nestable, pipeable through chains of transformation — developed within Microsoft's GUI-first design culture and was aimed at users who would never open a terminal. Yet the power users of Excel are, functionally, CLI users. They compose textual expressions that transform data through chains of operations, they build libraries of reusable formulas, they develop personal conventions and templates that constitute something very like an associated milieu. The formula bar is a command line embedded within the heart of the GUI paradigm. It was not inherited from Unix; it was independently generated by the operative logic of needing to compose transformations on data. And notably, Excel's formula language has only grown *more* CLI-like over time — the introduction of LAMBDA functions, LET bindings, and dynamic arrays in recent years represents a convergence toward functional composition that would be immediately legible to a shell scripter.</p>

<p>Consider also the domain of visual programming — node-based interfaces like Max/MSP, Unreal Blueprints, or Scratch. These represent genuine attempts to create compositional interfaces within a visual paradigm. Their persistence is real, but so is their ceiling: at scale, every node-based visual programming system generates demand for a textual scripting layer. Unreal has Blueprints but also C++. Unity has visual scripting but privileges C#. The visual composition hits a complexity barrier that textual composition does not, because text admits a density of relational specification that spatial layout cannot match without becoming illegible. This is not a failure of design imagination. It is the structural consequence of the difference between abstract and concrete interface architectures.</p>

<p>And then there is the most contemporary case: the emergence of AI agent frameworks. The first wave of LLM interfaces was conversational — chat boxes, natural language, the GUI-like promise of "just tell it what you want." The second wave, already underway, is compositional: tool-use architectures, function-calling protocols, agent graphs with typed inputs and outputs that can be chained, branched, and composed. LangChain, CrewAI, the function-calling specifications from OpenAI and Anthropic — these are pipe-and-filter architectures. They are CLIs in new clothing. Not because their designers are Unix nostalgists, but because the problem of orchestrating complex operations across multiple capabilities generates, through its own operative logic, a demand for compositional syntax.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Kaplan 2016" >}}
The Excel-as-command-line observation echoes Simon Peyton Jones et al., 'A User-Centred Approach to Functions in Excel' (2003), which argued that spreadsheets are the world's most widely used functional programming language. Felienne Hermans (*Spreadsheet Implementation Technology*, 2014) documents how expert spreadsheet users develop idioms, naming conventions, and sheet architectures that function as tacit programming cultures — the associated milieu argument under another name.
{{< /note >}}
{{< note ref="NOTE // visual ceiling" >}}
The claim that node-based visual programming hits a complexity barrier text does not has empirical backing in Whitley's 'visual programming language' studies and in Moody's 'Physics of Notations' (2009), which demonstrates that spatial representations exhaust discriminability faster than symbolic ones. But counterpoint: Bret Victor's 'Learnable Programming' (2012) argues the failure is not inherent to visuality but to impoverished visual vocabularies. The debate remains genuinely open.
{{< /note >}}
{{< /margin >}}

{{< section-rule >}}

{{< section num="05" label="§ V.  What Persists and Why" >}}

{{< gutter sigil="individuation" >}}

{{< main-col >}}

<p>The honest version of this argument is not that the CLI is superior to the GUI in all contexts. It is that the structural properties the CLI exhibits — functional polyvalence, compositional syntax, the generation of an associated milieu — are the properties that persist under pressure, that get independently reinvented, that scale to complexity. The GUI's properties — discoverability, visual feedback, low initial learning cost — are genuine virtues, but they are virtues of *introduction*, not of *mastery*. They optimize the first hour. The CLI's properties optimize the thousandth.</p>

<p>Simondon never argued that abstract technical objects are useless. He argued that they represent an early stage of individuation — functional but not yet fully realized, with latent potential that concretization would eventually unlock. The GUI is a magnificent abstract technical object. It has made computing accessible to billions of people. But when those people need to do something *complex* — when they need to compose, automate, adapt, and evolve their interactions with machines — they reach for text. They reach for syntax. They reach for the command line, or for something that, whatever its visual trappings, operates by command-line logic.</p>

<p>This isn't nostalgia. It isn't elitism. It is the tendency of technical objects, under the pressure of actual use, to converge toward the structural integration that makes them most capable of evolving with their operators. The CLI persists because it is, in the precise Simondonian sense, the more concrete form — not by descent, but by structure. And as the complexity of what we ask machines to do continues to increase, the interfaces that endure will be the ones that achieve the most operative density in the least expressive surface. Not buttons. Not chat. Composable syntax, shaped by use, individuating with its operator.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Stiegler 1998" >}}
Bernard Stiegler's *Technics and Time, 1* extends Simondon's individuation into a theory of human-technical co-constitution via 'tertiary retention' — externalized memory that preconditions future thought. The dotfile-as-milieu and the composable-syntax-as-mastery arguments here converge precisely on Stiegler's point: the tool that individuates with its user is also the tool that constitutes the user's technical memory. The CLI is a tertiary retention system; the GUI, by resisting co-individuation, forecloses it.
{{< /note >}}
{{< note ref="NOTE // operative density" >}}
The closing formulation — 'the most operative density in the least expressive surface' — inverts the information-theoretic framing of interface design, which typically optimizes for bandwidth to the user. Here the optimization target is bandwidth *from* the user: maximal compositional power per unit of syntactic expression. This is closer to Kolmogorov complexity than to usability metrics, and it implicitly redefines 'good interface' as 'highest ratio of achievable states to required input.'
{{< /note >}}
{{< /margin >}}
