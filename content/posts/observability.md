---
title: "Observability from the Beginning"
subtitle: "an argument for the entire development team"
date: 2024-09-13T12:00:00-04:00
draft: false
layout: treatise
doc_class: "doc/ops"
doc_id: "observability.essay"
revision: "rev 1"
chain: "logs → metrics → traces → insight"
footer_status: "§I–V complete"
status_pips:
  - active
  - active
  - amber
---

{{< section num="01" label="§ I.  The Problem" >}}

{{< gutter sigil="o11y" >}}

{{< main-col >}}

{{< dropcap letter="E" >}}

<p>ventually, every piece of software we build and deploy in production generates some user experience that requires us to examine the state of our application <em>as it's running in production</em>. Maybe when we first launch some system, we can wrap it up in a VM or a container and fling it into the world, but eventually we find that we need more detailed information about our application's actual running conditions. This is the point at which we know observability is required, where we know we need to monitor and observe our system and take actions based on that.</p>

<p>All too often, we treat observability as a secondary system, as something that we tack on to our systems after the fact, on an ad hoc basis. We try to instrument our systems as part of the devops or operations processes, at the orchestration and deployment level. Where we look for integration at the application level, we try to drop in one-liner DataDog APM integrations, instead of thinking carefully about how we can build our applications in the first place to support production metrics and logging.</p>

<p>This is a <span class="rubric">mistake</span>. It's a mistake that's often introduced very early in the product lifecycle, before developers even start their work. And like all mistakes that early, its downstream consequences are expensive and often nearly impossible to ever fully mitigate. Developers have no particular immediate stake in observability: we run these systems locally, with zero concurrency, under no load, and with access to development environment logging and debuggers. When product owners focus entirely on features, without considering infrastructure, developers can meet those needs without ever introducing mature observability. This, then, is an argument aimed at the entire development team, including our colleagues in product and project management.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // incentives" >}}
The observability gap is a classic principal-agent problem: the people who build the system (developers) are not the people who operate it (SREs, ops). Without shared ownership, the gap persists.
{{< /note >}}
{{< /margin >}}

<!-- §II -->

{{< section-rule >}}

{{< section num="02" label="§ II.  The Three Signals" >}}

{{< gutter sigil="signals" >}}

{{< main-col >}}

<p>Observability is a bedrock principle in software development, granting developers and operators the power to gain profound insights into their systems. It's about measuring the internal state of a system by examining its outputs. This, in turn, allows us to monitor the health and performance of the applications, pinpoint and resolve issues, and make data-driven choices on how to enhance them.</p>

<p>There are three key signals we seek to observe: <span class="rubric">logs, metrics, and traces</span>. Logs are records of specific events within a system. A query is made to a database, a user clicks a link, an error occurs. Metrics are quantitative measurements of system state, including metrics around both use and performance. Traces follow the flow of a request through a system, from an ingress through to various services and datastores — which is particularly useful for debugging and performance optimization in service-oriented architectures.</p>

<p>By adequately observing and analyzing these signals, teams can gain valuable insights into the behavior of their systems, how users are experiencing and using the system, and enabling them to make data-driven decisions across the SDLC, from prioritizing future features to identifying technical performance bottlenecks or error conditions.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // three pillars" >}}
Logs, metrics, and traces are often called the "three pillars of observability." Each provides a different lens: logs give discrete events, metrics give aggregate trends, traces give causal chains.
{{< /note >}}
{{< note ref="NOTE // OpenTelemetry" >}}
The OpenTelemetry project aims to unify these three signals under a single instrumentation framework — a recognition that they are perspectives on the same underlying system state.
{{< /note >}}
{{< /margin >}}

<!-- §III -->

{{< section-rule >}}

{{< section num="03" label="§ III.  Observability for Operations" >}}

{{< gutter sigil="ops" >}}

{{< main-col >}}

<p>Observability is the key practice in making data-driven decisions about operating a software system. Whether we're looking to optimize for cost, reliability, or user interactivity, we need accurate information to make good decisions about how we're running our production deployments.</p>

<p>Perhaps most obviously, observability allows us to monitor performance and resource consumption. We can identify where a system is experiencing bottlenecks, and determine whether those are CPU or IO bound bottlenecks, scaling as needed. We can identify if we're overprovisioning on some axis, needlessly spending money. We can make predictions about load under certain conditions and prepare our operations for those predictions, allowing us to meet SLO's and expense targets proactively.</p>

<p>But beyond that, observability is our <span class="rubric">primary analytical tool for problem analysis</span>. When things do go wrong — SLO failures, error spikes, data disasters, any of the things that can go wrong in a software system — signal data and observability tooling's analysis functions are both the clues and the tools we use to understand precisely what happened, and how we can determine the most effective interventions to prevent future errors of that class. This not only speeds up resolution times but also makes us to make our software more reliable, leading to happier customers and better products.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // SLO" >}}
Service Level Objectives define the reliability targets a system must meet. Without observability, SLOs are aspirational; with observability, they become measurable contracts.
{{< /note >}}
{{< note ref="NOTE // MTTR" >}}
Mean Time to Recovery is the metric most directly improved by mature observability. The faster you can identify and understand a failure, the faster you can fix it.
{{< /note >}}
{{< /margin >}}

<!-- §IV -->

{{< section-rule >}}

{{< section num="04" label="§ IV.  The Product Pipeline" >}}

{{< gutter sigil="product" >}}

{{< main-col >}}

<p>Much of the literature on observability focuses on the preceding section, on the value of healthy observability to developers and operators. However, I believe this misses the <span class="rubric">real point</span>, and where both ultimate responsibility for and long-term value from observability lie. Whether your business requirements necessitate agile or traditional project management approaches, you must have observability in your initial product plans.</p>

<p>Agile project management solves the problem of shifting product requirements. We build an MVP, and then we deploy to production and enter into a dialogue with our stakeholders to rapidly iterate on the product and allow us to pivot with market conditions and stakeholder priorities. Every single sprint we run without observability is a sprint in which we're missing core data that we could be using to identify and refine those requirements. With observability, engineering and operations stakeholders can better articulate costs associated with development; product and sales stakeholders have better ideas of which features are being used and the experience using them; financial stakeholders can better understand ROI on individual features or subsystems. Without observability as early as the MVP, stakeholders are shooting in the dark, and there are entire classes of concerns that could impact product requirements that are simply unknown.</p>

<p>If you're blessed with a problem domain with static requirements — either a slow-moving market or sufficient control of the operating parameters to be able to say with certainty that the landscape won't change on you — your team may reasonably opt for more traditional project management techniques, the so-called "waterfall" approach. Under this project management philosophy, as well, it's critical to incorporate observability as early as possible. The key benefit to observability under waterfall is in long-term operations and maintenance: good observability makes operators' lives easier and bugfixes more efficient. But articulating observability as a requirement from the outset allows for more efficient and accurate management of the project. It informs PM's that they need to bring in ops as stakeholders for determining observability needs, it allows developers to better estimate development time and plan systems architecture, and it avoids last-minute scrambles to tack systems on ad-hoc when ops inevitably tells you it's a requirement.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // agile" >}}
The agile manifesto's emphasis on "responding to change" presupposes that you can detect when change is needed. Without observability, the feedback loop that agile depends on is incomplete.
{{< /note >}}
{{< note ref="NOTE // waterfall" >}}
Even in waterfall, the operations phase is the longest and most expensive. Front-loading observability amortizes its cost across the entire operational lifetime of the system.
{{< /note >}}
{{< /margin >}}

<!-- §V -->

{{< section-rule >}}

{{< section num="05" label="§ V.  The Launch Requirement" >}}

{{< gutter sigil="launch" >}}

{{< main-col >}}

<p>What all of this speaks to is that observability should be understood as <span class="rubric">requirement from any project's outset</span>. Our initial signals don't have to include everything and anything a system could possibly produce, but we should have an instrumentation framework in place that generates the most immediately actionable signals and allows developers and operators to easily extend it if new signals become germane.</p>

<p>By introducing observability as soon as possible in the process, we can make our engineering and operations teams more efficient and effective, and we can more tightly couple our product operations to our organization's business goals.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // minimum" >}}
The minimum viable observability: structured logging, request latency histograms, and error rate counters. These three alone unlock the majority of operational insight.
{{< /note >}}
{{< /margin >}}
