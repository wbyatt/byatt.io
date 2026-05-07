---
title: "Deconstructing the Instruction-Data Dichotomy"
subtitle: "on the false boundary between code and data"
date: 2024-09-17T12:45:18-04:00
draft: true
layout: treatise
doc_class: "doc/theory"
doc_id: "instruction-data.essay"
revision: "rev 1"
chain: "code ⇌ data ⇌ code"
footer_status: "§I–IV complete"
status_pips:
  - active
  - active
  - active
---

{{< section num="01" label="§ I.  The Myth" >}}

{{< gutter sigil="dichotomy" >}}

{{< main-col >}}

{{< dropcap letter="I" >}}

<p>n the software engineering discourse, a pervasive myth has long shaped our understanding of code and its relationship to data. This myth posits a clear, binary separation: code consists of instructions that operate on extrinsic data. It's a comforting notion, one that neatly compartmentalizes the complexities of programming into active agents (code) and passive recipients (data). This dichotomy has become so ingrained in our collective consciousness that it often goes unquestioned, forming the bedrock of how we conceptualize, teach, and practice software development.</p>

<p>However, this widely accepted paradigm is not just an oversimplification — it's <span class="rubric">fundamentally flawed</span>. The supposed boundary between instruction and data is far more porous and dynamic than we typically acknowledge. This false dichotomy limits our understanding of computation's true nature and, consequently, constrains our ability to innovate and solve complex problems effectively.</p>

<p>In this exploration, we will challenge this entrenched belief, demonstrating that the relationship between code and data is far more intricate and intertwined than the traditional view suggests. By deconstructing this myth, we aim to reveal a more nuanced and powerful perspective on the nature of software — one that recognizes the fluid interplay between instruction and data at every level of computation.</p>

<p>This reconceptualization is not merely an academic exercise. Understanding the true nature of the instruction-data relationship has profound implications for how we approach software design, team dynamics, and the very role of computation in solving real-world problems. By shedding light on this often-overlooked aspect of programming, we can open new avenues for creativity, collaboration, and innovation in software engineering.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // Derrida" >}}
The deconstructive move here mirrors Derrida's strategy: identify a binary opposition that appears natural (instruction/data), show that each term already contains its opposite, and reveal the hierarchy the opposition conceals.
{{< /note >}}
{{< /margin >}}

<!-- §II -->

{{< section-rule >}}

{{< section num="02" label="§ II.  All Code is Data" >}}

{{< gutter sigil="code→data" >}}

{{< main-col >}}

<p>The most basic issue here is that your code cannot operate, your code has no meaning, without the possibility of interpreting it as data. Your ability to work on your code requires that your hard drive be able to store the code as data. It requires that your IDE be able to open, parse, and syntax highlight the code as data. Even more fundamentally, your interpreter, compiler, or assembler is treating your code as data to produce machine code: to the compiler, even your code is an <span class="rubric">extrinsic data source</span> upon which its instructions operate.</p>

<p>Nor is this merely an artifact of specific software subjectivities, we are not free to consider our code as entirely instructions from our perspective and live-and-let-live as the IDE, hard drive, and compiler interpret it as data from theirs. In our own interactions with code, we have always latent the possibility of dataness in our code. Consider the possibility of self-modifying code, or metaprogramming. I'm not just here discussing polymorphic malware, but consider the following everyday, commonplace snippet of Ruby-on-Rails code:</p>

{{< codex caption="ActiveRecord metaprogramming // class as mutable data" lang="Ruby" >}}
{{< cl >}}<span class="kw">class</span> <span class="ty">User</span> <span class="op"><</span> <span class="ty">ApplicationRecord</span>{{< /cl >}}
{{< cl >}}  <span class="nm">has_many</span> <span class="op">:</span><span class="st">posts</span>{{< /cl >}}
{{< cl >}}<span class="kw">end</span>{{< /cl >}}
{{< /codex >}}

<p>The <code>has_many</code> method has the technical function of creating an instance method called <code>posts</code> on the <code>User</code> class. That is to say, it <em>modifies the class at runtime to create and attach instructions we did not write</em>. This is only possible because the <code>has_many</code> method is capable of treating our class not merely as a lexical context, but as a parameter it receives. At runtime, our class abstraction is <span class="rubric">treated directly as data</span>.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // compiler" >}}
The compiler's relationship to source code is the ur-example: what you consider "instructions" is, to the compiler, nothing more than a stream of characters to be parsed, transformed, and emitted. Your code is its data.
{{< /note >}}
{{< note ref="NOTE // Rails" >}}
ActiveRecord's metaprogramming is not exotic — it is the standard idiom of the framework. Millions of production applications depend on the class-as-data pattern daily.
{{< /note >}}
{{< /margin >}}

<!-- §III -->

{{< section-rule >}}

{{< section num="03" label="§ III.  Data Can Be Instruction" >}}

{{< gutter sigil="data→code" >}}

{{< main-col >}}

<p>One of the first techniques many of us learn is the table-driven methods, canonically articulated in the masterful <em>Code Complete</em>. In a table-driven method, instead of using switch statements or if/then logic to determine which branch of instruction to take, we use a data structure and a lookup in that data structure to drive our behavior. In JavaScript, we might do this:</p>

{{< codex caption="table-driven dispatch // data that executes" lang="JavaScript" >}}
{{< cl >}}<span class="kw">const</span> <span class="nm">operations</span> <span class="op">=</span> {{{< /cl >}}
{{< cl >}}  <span class="nm">add</span><span class="op">:</span>      (<span class="nm">x</span>, <span class="nm">y</span>) <span class="op">=></span> <span class="nm">x</span> <span class="op">+</span> <span class="nm">y</span>,{{< /cl >}}
{{< cl >}}  <span class="nm">subtract</span><span class="op">:</span> (<span class="nm">x</span>, <span class="nm">y</span>) <span class="op">=></span> <span class="nm">x</span> <span class="op">-</span> <span class="nm">y</span>,{{< /cl >}}
{{< cl >}}  <span class="nm">multiply</span><span class="op">:</span> (<span class="nm">x</span>, <span class="nm">y</span>) <span class="op">=></span> <span class="nm">x</span> <span class="op">*</span> <span class="nm">y</span>,{{< /cl >}}
{{< cl >}}  <span class="nm">divide</span><span class="op">:</span>   (<span class="nm">x</span>, <span class="nm">y</span>) <span class="op">=></span> {{{< /cl >}}
{{< cl >}}    <span class="kw">if</span> (<span class="nm">y</span> <span class="op">===</span> <span class="nm">0</span>) <span class="kw">throw new</span> <span class="ty">Error</span>(<span class="st">'Division by zero'</span>);{{< /cl >}}
{{< cl >}}    <span class="kw">return</span> <span class="nm">x</span> <span class="op">/</span> <span class="nm">y</span>;{{< /cl >}}
{{< cl >}}  }{{< /cl >}}
{{< cl >}}};{{< /cl >}}
{{< /codex >}}

<p>This is straightforwardly a data structure. It is stored on the heap like data, it is subject to garbage collection like data, and it is referenceable and can be passed like data, because it <em>is data</em>. But it also can be executed like instruction, it semantically contains functions like instruction, and it can throw errors like instruction. Because it also <span class="rubric">is instruction</span>.</p>

<p>While this is a toy example, table-driven methods on the whole are quite production-worthy, and there are other forms of data-driven instruction available to us. An engineer building a scheduling service might allow arbitrary event repetition rules to be saved in a database and executed as instance generators. A robotics engineer can set configuration parameters that dramatically impact the behavior of a robot. A machine learning scientist tunes hyperparameters to instruct his system's learning process. There is an entire universe where information that's initially conceived of as data is behaviorally indistinguishable from instruction.</p>

<p>Our boundary collapse between instruction and data reaches its fullest expression in languages that demonstrate <span class="rubric">homoiconicity</span>. Homoiconicity is a property of a programming language where its code and data structures have semantically indistinguishable representations, and thus the code can operate on itself in place.</p>

<p>The most famous homoiconic language is the Lisp family. Lisp's primary data primitive is the <a href="https://en.wikipedia.org/wiki/S-expression">S-expression</a>, but a Lisp program itself is nothing more nor less than a single massive S-expression. That is to say, in Lisp, programs are constructed directly from the very data structures they manipulate.</p>

{{< codex caption="mirror-of-computation // code operating on code-as-data" lang="Lisp" >}}
{{< cl >}}(<span class="kw">defun</span> <span class="nm">mirror-of-computation</span> (<span class="nm">code</span>){{< /cl >}}
{{< cl >}}  (<span class="kw">if</span> (<span class="nm">listp</span> <span class="nm">code</span>){{< /cl >}}
{{< cl >}}      (<span class="nm">cons</span> (<span class="nm">mirror-of-computation</span> (<span class="nm">car</span> <span class="nm">code</span>)){{< /cl >}}
{{< cl >}}            (<span class="nm">mirror-of-computation</span> (<span class="nm">cdr</span> <span class="nm">code</span>))){{< /cl >}}
{{< cl >}}  <span class="nm">code</span>)){{< /cl >}}
{{< /codex >}}

<p>This recursive code takes code as its input and outputs that code, reversed. But is that reversal an operation on data, or is it a runtime operation on code? Can it run on itself? How shall we view this, and frankly what, exactly, is genuinely extrinsic to this function?</p>

<p>Nor is this feature unique to Lisp. In fact, at the machine code level, <strong>all Von Neumann architectures demonstrate homoiconicity</strong>: there is no intrinsic way to determine whether some byte flowing through a machine is data or instruction, the function of a particular byte is determined entirely by its spatio-temporal execution context — that is, where and when it enters the machine. Consider the following partial table of x86_64 opcodes:</p>

{{< construction caption="x86_64 opcodes // bytes that are simultaneously data and instruction" >}}
<thead>
  <tr>
    <th>Opcode</th>
    <th>Instruction</th>
    <th>Description</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>0x50–0x57</td>
    <td>PUSH r64</td>
    <td>Push 64-bit register onto stack</td>
  </tr>
  <tr>
    <td>0x58–0x5F</td>
    <td>POP r64</td>
    <td>Pop 64-bit register from stack</td>
  </tr>
  <tr>
    <td>0x89</td>
    <td>MOV r/m64, r64</td>
    <td>Move 64-bit register to register/memory</td>
  </tr>
  <tr>
    <td>0x8B</td>
    <td>MOV r64, r/m64</td>
    <td>Move 64-bit register/memory to register</td>
  </tr>
  <tr>
    <td>0xB8–0xBF</td>
    <td>MOV r64, imm64</td>
    <td>Move 64-bit immediate to register</td>
  </tr>
  <tr>
    <td>0x01</td>
    <td>ADD r/m64, r64</td>
    <td>Add 64-bit register to register/memory</td>
  </tr>
  <tr>
    <td>0x29</td>
    <td>SUB r/m64, r64</td>
    <td>Subtract 64-bit register from register/memory</td>
  </tr>
  <tr>
    <td>0x31</td>
    <td>XOR r/m64, r64</td>
    <td>XOR 64-bit register with register/memory</td>
  </tr>
  <tr>
    <td>0x39</td>
    <td>CMP r/m64, r64</td>
    <td>Compare 64-bit register with register/memory</td>
  </tr>
</tbody>
{{< /construction >}}

<p>The opcodes in the left column represent the sequence of bytes that are used to instruct the CPU to perform a command. We cannot help but note that these are simply that: sequences of bytes, identical in every way to any other sequence of bytes. Thus, a <span class="rubric">total collapse of the boundaries</span> between instruction and data sits at the very bottom of our stack, and we cannot meaningfully conceive of code as an active agent operating on inert, extrinsic data.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // McConnell 2004" >}}
<em>Code Complete</em> devotes an entire chapter to table-driven methods, calling them one of the most underused techniques in programming. The technique itself is proof that data structures can encode behavior.
{{< /note >}}
{{< note ref="NOTE // S-expressions" >}}
McCarthy's original 1960 Lisp paper made homoiconicity not a feature but the foundation: programs are lists, lists are programs. The distinction between "running" and "manipulating" a Lisp program is purely one of context.
{{< /note >}}
{{< note ref="NOTE // Von Neumann" >}}
The Von Neumann architecture's stored-program concept — instructions and data share the same memory — is the hardware-level expression of this same insight. The dichotomy was never real; it was a pedagogical convenience.
{{< /note >}}
{{< /margin >}}

<!-- §IV -->

{{< section-rule >}}

{{< section num="04" label="§ IV.  Why It Matters" >}}

{{< gutter sigil="praxis" >}}

{{< main-col >}}

<p>Our basic cognitive primitives about what software is and how it's constructed inform our positions on what problems software solves, how it solves them, and who it solves them for. As one example, the tendency to view software as primarily an automation tool is rooted in this mistaken belief that software is active instruction that operates on inert data: in the most immediate reading of this automation-oriented perspective, it relies on the idea that there is some information which can be ingested and business processes can be applied to that data. While automation is a considerable power of computing, it is by no means the only or even the most common form of desirable behavior from a computer.</p>

<p>But more broadly, it encourages a limited and strictly hierarchical view of the possibilities of computation. It prevents us from asking how we might more creatively understand our work, but it also prevents us from asking how we might more <span class="rubric">collaboratively</span> understand our work modalities themselves. If it is only ours to write code against inert data, then we the developers must also behave as active participants against inert data. We must receive data from outside sources. And this is reflected in the most common forms of software organization structure: we <em>receive</em> UX designs, or requirements specifications, or access to data lakes, and then we <em>act</em> upon those inert artifacts. This prevents us from being active participants in the work process and alienates us from our organizations.</p>

<p>Unwinding this limited and narrow view of software is, I believe, critical to creating more effective software and more effective software teams. And it begins with this analysis of the <span class="rubric">real meaning of code</span>.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // hierarchy" >}}
The instruction/data hierarchy maps onto the organizational hierarchy: architects "instruct," developers "implement," data "flows in." Dissolving the first hierarchy opens space to question the second.
{{< /note >}}
{{< note ref="NOTE // alienation" >}}
The parallel to Marx's alienation of labor is intentional: when developers are reduced to "instruction-writers" who act upon extrinsic inputs, they are separated from the creative and collaborative dimensions of their work.
{{< /note >}}
{{< /margin >}}
