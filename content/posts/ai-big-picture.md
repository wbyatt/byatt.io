---
title: "The Big Picture of AI"
subtitle: "why you don't need to know every detail"
date: 2024-06-15T12:45:18-04:00
draft: true
layout: treatise
doc_class: "doc/strategy"
doc_id: "ai-big-picture.essay"
revision: "rev 1"
chain: "paradigm → puzzle → pattern"
footer_status: "§I–III complete"
status_pips:
  - active
  - amber
  - dim
---

{{< section num="01" label="§ I.  The Paradigm" >}}

{{< gutter sigil="kuhn.1962" >}}

{{< main-col >}}

{{< dropcap letter="I" >}}

<p>n the whirlwind of current AI advancements, trying to keep up with every new change can be dizzying. But unless you're an active researcher in the field, you really don't need to. You just need to keep abreast of the aggregate, because that's where the <span class="rubric">real story</span> is for the rest of us.</p>

<p>Large Language Models (LLMs) have created a paradigm shift in computing. One of the nice things about paradigm shifts, though, is that we know how they work. In 1962, philosopher of science Thomas Kuhn described the anatomy of a paradigm shift (and also coined the phrase itself) in <em>The Structure of Scientific Revolutions</em>.</p>

<p>In the early phases of a new paradigm, there is an avalanche of "normal science," where the new paradigm begets huge amounts of discovery based on the new premises and techniques. However, this normal science is incremental, each new advancement pushes the field only marginally. Kuhn called it <span class="rubric">"puzzle-solving,"</span> it's applying the new paradigm in specific contexts to fit specific problems. It is only in aggregate, from the middle distance, that we can see the net effect of these incremental changes, or the abstractions that cut across solutions for multiple puzzles. It is only from a distance that the larger patterns of the new paradigm begin to emerge.</p>

<p>For business and engineering leaders focused on improving customers' or clients' lives using AI, there is no utility in following every single advancement, in knowing in detail all the changes impacting the technical landscape. You don't need to dive into questions of LoRA vs QLoRA fine-tuning, or whether Kolmogorov-Arnold networks provide a marginal improvement over multilayer perceptrons. It may be valuable to know that we're making strides in both pre-training and fine-tuning costs, that it's getting easier and cheaper to run inference against multiple models at the same time, and that context windows are expanding. But even that might be more fine-grained than your requirements need.</p>

<p>So don't worry about every detail. Understand the general architecture of the kinds of problems you hope to solve with AI, where the existing bottlenecks are, and keep an eye there. And spend your extra time reading some helpful philosophy of science.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Kuhn 1962" >}}
<em>The Structure of Scientific Revolutions</em> introduced the concept of paradigm shifts and the cyclical nature of scientific progress. Kuhn argued that science does not advance by steady accumulation but through revolutionary breaks.
{{< /note >}}
{{< note ref="NOTE // normal science" >}}
Kuhn's "puzzle-solving" is not pejorative. Normal science is productive and necessary — it fills out the new paradigm's implications. The mistake is confusing any single puzzle solution for the paradigm itself.
{{< /note >}}
{{< /margin >}}

<!-- §II -->

{{< section-rule >}}

{{< section num="02" label="§ II.  The Importance of the Big Picture" >}}

{{< gutter sigil="strategy" >}}

{{< main-col >}}

<p>While it's tempting to get caught up in the day-to-day details of AI research, focusing on the big picture can provide several benefits:</p>

<ul>
<li><strong>Strategic Decision-Making</strong>: Understanding the overall direction of AI development can help you make informed decisions about where to invest your resources and efforts.</li>
<li><strong>Avoiding FOMO</strong>: By focusing on the key trends, you can avoid feeling overwhelmed by the constant stream of new advancements and ensure you're not missing out on anything truly significant.</li>
<li><strong>Identifying Opportunities</strong>: Understanding the broader context of AI can help you identify new opportunities and applications for this technology in your business or industry.</li>
<li><strong>Building a Strong Foundation</strong>: A solid understanding of the fundamental principles of AI will serve as a strong foundation for future learning and growth.</li>
</ul>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // aggregate" >}}
The aggregate view is not laziness — it is the correct level of abstraction for decision-makers. Individual advances are noise; the trend line is signal.
{{< /note >}}
{{< /margin >}}

<!-- §III -->

{{< section-rule >}}

{{< section num="03" label="§ III.  Key Trends to Watch" >}}

{{< gutter sigil="trends" >}}

{{< main-col >}}

<p>Here are some of the most important trends in AI that you should be aware of:</p>

<ul>
<li><strong>Scaling</strong>: The ability to train larger and larger models on more data is driving significant advancements in AI performance.</li>
<li><strong>Cost Reduction</strong>: The cost of training and running AI models is decreasing, making AI more accessible to businesses of all sizes.</li>
<li><strong>Contextual Understanding</strong>: AI models are becoming increasingly capable of understanding and generating text and other forms of content in context.</li>
<li><strong>Multimodality</strong>: AI is starting to integrate multiple modalities, such as text, images, and audio, to create more powerful and versatile applications.</li>
<li><strong>Explainability</strong>: There is a growing focus on developing AI models that are more explainable and transparent, making it easier to understand how they arrive at their decisions.</li>
</ul>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // scaling laws" >}}
The relationship between model size, data, and compute follows predictable power laws — the so-called "scaling laws" first characterized by Kaplan et al. at OpenAI. These laws are themselves a pattern visible only from the aggregate.
{{< /note >}}
{{< note ref="NOTE // access" >}}
Cost reduction and multimodality together represent a democratization of capability: what required a research lab in 2020 is available via API in 2024.
{{< /note >}}
{{< /margin >}}
