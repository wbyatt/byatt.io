---
title: "On the Unrolling of the Real Line"
subtitle: "being a treatise on the construction of continua from counting alone"
doc_class: "doc/math"
doc_id: "peano-reals.treatise"
revision: "rev 2"
chain: "ℕ ⊂ ℤ ⊂ ℚ ⊂ ℝ"
footer_status: "§I–IV complete"
status_pips:
  - active
  - amber
  - dim
layout: treatise
draft: false
---

{{< section num="01" label="§ I.  The Axioms of Peano" >}}

{{< gutter sigil="peano.1889" >}}

{{< main-col >}}

{{< dropcap letter="N" >}}

<p>ow, the natural numbers arise not from experience but from the form of thought itself. Peano, following Dedekind's <em>Was sind und was sollen die Zahlen?</em>, distilled arithmetic to five propositions — each as spare and hard as flint — from which the entire edifice of number shall be raised stone by stone. Let <span class="math">ℕ</span> denote the set in question, <span class="math">0</span> its least element, and <span class="math">S</span> the successor function: the engine of increment, the <span class="rubric">pulse of counting</span>.</p>

<p>These axioms are not merely rules but definitions: they tell us what the naturals <em>are</em>, not merely how they behave. From them we shall peel back layer after layer of abstraction until the continuous real line stands exposed — wrested, astonishingly, from the act of adding one.</p>

{{< axiom-block label="Axiomata // Peano 1889" >}}
{{< axiom num="PA.I" >}}<span class="math">0 ∈ ℕ</span>. Zero is a natural number.{{< /axiom >}}
{{< axiom num="PA.II" >}}For every <span class="math">n ∈ ℕ</span>, its successor <span class="math">S(n) ∈ ℕ</span>. The act of adding one is always possible.{{< /axiom >}}
{{< axiom num="PA.III" >}}For all <span class="math">n ∈ ℕ</span>, <span class="math">S(n) ≠ 0</span>. Nothing succeeds to zero — it is the origin, uncaused.{{< /axiom >}}
{{< axiom num="PA.IV" >}}<span class="math">S</span> is injective: if <span class="math">S(m) = S(n)</span> then <span class="math">m = n</span>. Different numbers have different successors.{{< /axiom >}}
{{< axiom num="PA.V" >}}If a set <span class="math">K ⊆ ℕ</span> contains <span class="math">0</span> and is closed under <span class="math">S</span>, then <span class="math">K = ℕ</span>. This is induction — the <span class="rubric">inextinguishable flame</span> that propagates truth upward without end.{{< /axiom >}}
{{< /axiom-block >}}

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Dedekind 1888" >}}
The successor function encodes the entire structure of ℕ. Remarkably, PA.V — induction — is the only axiom that cannot be expressed in first-order logic without the full power of second-order quantification over sets.
{{< /note >}}
{{< note ref="NOTE // Gödel 1931" >}}
Any consistent system strong enough to contain PA.I–V will contain true statements it cannot prove. The axioms are sufficient for number theory; they are not sufficient for truth.
{{< /note >}}
{{< /margin >}}

<!-- §II -->

{{< section-rule >}}

{{< section num="02" label="§ II.  Integers &amp; Rationals" >}}

{{< gutter sigil="ℕ→ℤ→ℚ" >}}

{{< main-col >}}

<p>From these five strokes we build addition and multiplication by structural recursion, then define the <em>integers</em> as equivalence classes of pairs: <span class="math">(a, b)</span> represents the signed number <span class="math">a − b</span>, and two pairs <span class="math">(a,b) ∼ (c,d)</span> when <span class="math">a + d = b + c</span>. The negative numbers are not objects discovered in nature but <span class="rubric">certificates of subtraction</span> — formal witnesses that a computation would have succeeded had its inputs been larger.</p>

{{< codex caption="ℤ as a quotient of ℕ × ℕ // Grothendieck group construction" lang="Haskell" >}}
{{< cl >}}<span class="cm">-- ℤ as equivalence classes of pairs (a, b) ≅ a - b</span>{{< /cl >}}
{{< cl >}}<span class="kw">type</span> <span class="ty">IntPair</span> <span class="op">=</span> (<span class="ty">Nat</span>, <span class="ty">Nat</span>){{< /cl >}}
{{< cl >}}{{< /cl >}}
{{< cl >}}<span class="nm">equiv</span> <span class="op">::</span> <span class="ty">IntPair</span> <span class="op">-></span> <span class="ty">IntPair</span> <span class="op">-></span> <span class="ty">Bool</span>{{< /cl >}}
{{< cl >}}<span class="nm">equiv</span> (a, b) (c, d) <span class="op">=</span> a <span class="op">+</span> d <span class="op">==</span> b <span class="op">+</span> c{{< /cl >}}
{{< cl >}}{{< /cl >}}
{{< cl >}}<span class="nm">addZ</span> <span class="op">::</span> <span class="ty">IntPair</span> <span class="op">-></span> <span class="ty">IntPair</span> <span class="op">-></span> <span class="ty">IntPair</span>{{< /cl >}}
{{< cl >}}<span class="nm">addZ</span> (a, b) (c, d) <span class="op">=</span> (a <span class="op">+</span> c,  b <span class="op">+</span> d){{< /cl >}}
{{< cl >}}{{< /cl >}}
{{< cl >}}<span class="nm">negZ</span>  <span class="op">::</span> <span class="ty">IntPair</span> <span class="op">-></span> <span class="ty">IntPair</span>{{< /cl >}}
{{< cl >}}<span class="nm">negZ</span>  (a, b) <span class="op">=</span> (b, a)    <span class="cm">-- the mirror operator</span>{{< /cl >}}
{{< /codex >}}

<p>The rationals follow the same pattern: represent <span class="math">p/q</span> as a pair <span class="math">(p, q)</span> with <span class="math">q ≠ 0</span>, and declare <span class="math">(p,q) ∼ (r,s)</span> when <span class="math">ps = qr</span>. Division becomes, like negation before it, a syntactic device — an invented object whose entire justification is that it makes equations solvable.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // symmetry" >}}
The move from ℕ to ℤ is a symmetrization — we demand that subtraction be total. The move from ℤ to ℚ is a rationalization — we demand division be total. Both are quotient constructions: we forget details to gain closure.
{{< /note >}}
{{< note ref="REF // Grothendieck" >}}
The same construction — pairs modulo an equivalence — generates the K-theory groups that classify vector bundles. Peano's arithmetic and algebraic topology share a common engine.
{{< /note >}}
{{< /margin >}}

<!-- §III -->

{{< section-rule >}}

{{< section num="03" label="§ III.  The Leap into Continuity" >}}

{{< gutter sigil="ℚ→ℝ.cut" >}}

{{< main-col >}}

<p>Here the construction arrives at its most vertiginous step. The rationals are <em>dense</em> — between any two there lies a third — yet they are riddled with gaps. The square root of two is missing. So is <span class="math">π</span>. So is every transcendental, of which there are, in a precise sense, incomparably more than the rationals they elude.</p>

<p>Dedekind's answer is the <span class="rubric">cut</span>: a partition of <span class="math">ℚ</span> into two nonempty sets <span class="math">(A, B)</span> such that every element of <span class="math">A</span> is less than every element of <span class="math">B</span>, and <span class="math">A</span> has no greatest member. Each such cut <em>is</em> a real number — not a name for one, but the thing itself, reconceived as a <em>division of what we already know</em>. The gap in the rationals does not point to a missing object; it <span class="rubric">constitutes</span> one.</p>

{{< codex caption="a Dedekind cut in ℚ // √2 without naming it" lang="Lean 4" >}}
{{< cl >}}<span class="cm">-- A real number as a downward-closed cut in ℚ</span>{{< /cl >}}
{{< cl >}}<span class="kw">structure</span> <span class="ty">Cut</span> <span class="kw">where</span>{{< /cl >}}
{{< cl >}}  lower    <span class="op">:</span> ℚ <span class="op">→</span> <span class="ty">Prop</span>{{< /cl >}}
{{< cl >}}  nonempty <span class="op">:</span> <span class="op">∃</span> q, lower q{{< /cl >}}
{{< cl >}}  bounded  <span class="op">:</span> <span class="op">∃</span> q, <span class="op">¬</span>lower q{{< /cl >}}
{{< cl >}}  downward <span class="op">:</span> p <span class="op">≤</span> q <span class="op">→</span> lower q <span class="op">→</span> lower p{{< /cl >}}
{{< cl >}}  open_up  <span class="op">:</span> lower q <span class="op">→</span> <span class="op">∃</span> r <span class="op">></span> q, lower r{{< /cl >}}
{{< cl >}}{{< /cl >}}
{{< cl >}}<span class="cm">-- √2, defined without naming its value</span>{{< /cl >}}
{{< cl >}}<span class="kw">def</span> <span class="nm">sqrt2</span> <span class="op">:</span> <span class="ty">Cut</span> <span class="op">:=</span> {{{< /cl >}}
{{< cl >}}  lower <span class="op">:=</span> <span class="kw">fun</span> q <span class="op">=></span> q <span class="op"><</span> 0 <span class="op">∨</span> q<span class="op">*</span>q <span class="op"><</span> 2{{< /cl >}}
{{< cl >}}  <span class="cm">-- proofs of nonempty, bounded, downward, open_up</span>{{< /cl >}}
{{< cl >}}  <span class="cm">-- follow from ordered field properties of ℚ</span>{{< /cl >}}
{{< cl >}}}{{< /cl >}}
{{< /codex >}}

<p>Cantor's equivalent formulation uses Cauchy sequences: a real is an equivalence class of rational sequences <span class="math">(q₁, q₂, q₃, …)</span> whose terms grow arbitrarily close, two sequences identified when their difference converges to zero. Where Dedekind sees a real as a <em>place</em>, Cantor sees it as a <em>process of approach</em> — two portraits of the same unseen face.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // 1872" >}}
Cantor and Dedekind arrived at equivalent constructions independently in the same year — a remarkable unison at the threshold of modern analysis.
{{< /note >}}
{{< note ref="NOTE // cardinality" >}}
The reals are uncountable (Cantor's diagonal argument, 1891). There are strictly more real numbers than there are natural numbers, integers, or rationals — all three of which are equinumerous.
{{< /note >}}
{{< note ref="NOTE // gaps" >}}
ℚ is dense but not complete. ℝ is both. Completeness — every Cauchy sequence converges — is the single property that makes calculus possible.
{{< /note >}}
{{< /margin >}}

<!-- §IV -->

{{< section-rule >}}

{{< section num="04" label="§ IV.  The Table of Ascent" >}}

{{< gutter sigil="index" >}}

{{< main-col >}}

{{< construction caption="Construction chain // Peano → Continuum" >}}
<thead>
  <tr>
    <th>Structure</th>
    <th>Construction</th>
    <th>Gain</th>
    <th>Defect repaired</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>ℕ</td>
    <td>Peano axioms; successor <em>S</em></td>
    <td>Counting, addition, induction</td>
    <td><em>—</em> (origin)</td>
  </tr>
  <tr>
    <td>ℤ</td>
    <td>Pairs <em>(a,b)</em> mod <em>a+d = b+c</em></td>
    <td>Subtraction total</td>
    <td>Subtraction may fail in ℕ</td>
  </tr>
  <tr>
    <td>ℚ</td>
    <td>Pairs <em>(p,q)</em> mod <em>ps = qr</em></td>
    <td>Division by nonzero total</td>
    <td>Division may fail in ℤ</td>
  </tr>
  <tr>
    <td>ℝ</td>
    <td>Dedekind cuts or Cauchy classes</td>
    <td>Completeness: all limits exist</td>
    <td>Cauchy sequences may not converge in ℚ</td>
  </tr>
</tbody>
{{< /construction >}}

<p>At each step a defect in the previous structure is repaired by a quotient construction — an act of deliberate forgetting, in which pairs of objects are declared identical so that an operation which was previously partial becomes total. The integers forget the individual values of <span class="math">a</span> and <span class="math">b</span>, remembering only their difference. The reals forget the particulars of a Cauchy sequence, remembering only its limit.</p>

<p>What is remarkable is that the real line — the substrate of all geometry, analysis, and physics — contains nothing that was not already, in embryo, implicit in the bare act of counting. The <span class="rubric">continuum is the shadow cast by ℕ</span>, stretched through three quotient constructions into its full, unfathomable length. Peano gave us five axioms; Dedekind showed us they were enough.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // pattern" >}}
Each construction is a Grothendieck completion: given a monoid with a partial operation, freely adjoin inverses. ℤ completes (ℕ, +). ℚ completes (ℤ\{0}, ×). ℝ completes (ℚ, Cauchy).
{{< /note >}}
{{< note ref="NOTE // beyond ℝ" >}}
The construction does not end here. ℂ adjoins √-1. The p-adic numbers ℚₚ complete ℚ under a different metric. The surreals extend ℝ to include infinitesimals. Each is a different answer to the question: <em>what are we willing to forget?</em>
{{< /note >}}
{{< /margin >}}
