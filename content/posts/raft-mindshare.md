---
title: "The Raft Consensus"
subtitle: "on the power of simplicity in distributed systems"
date: 2024-06-17T12:45:18-04:00
draft: false
layout: treatise
doc_class: "doc/systems"
doc_id: "raft-consensus.essay"
revision: "rev 1"
chain: "Paxos → Raft → etcd → K8s"
footer_status: "§I–IV complete"
status_pips:
  - active
  - active
  - dim
---

{{< section num="01" label="§ I.  The Rise of Raft" >}}

{{< gutter sigil="consensus" >}}

{{< main-col >}}

{{< dropcap letter="T" >}}

<p>he distributed systems world has witnessed a fascinating phenomenon: the rise of the Raft consensus algorithm. More than just gaining a foothold, Raft has become the <span class="rubric">de facto standard</span> for new distributed applications that require consensus across stateful actions. From etcd, the backbone of Kubernetes, to Redpanda Data taking on the established Kafka, this simple algorithm is powering segment-defining systems.</p>

<p>But what makes Raft so special? Here's the surprising truth: Raft wasn't designed to be the fastest or most computationally efficient algorithm. It wasn't built to outperform existing solutions in some theoretical domain. It was designed with a far more human-centric goal: <span class="rubric">understandability</span>.</p>

<p>This might seem counterintuitive. In a world obsessed with performance benchmarks and cutting-edge algorithms, wouldn't a more complex and optimized solution reign supreme? The success of Raft tells a different story. By prioritizing human understanding, Raft has democratized the development of distributed systems, leading to a surge in innovation.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="REF // Ongaro &amp; Ousterhout 2014" >}}
"In Search of an Understandable Consensus Algorithm" — the Raft paper explicitly states understandability as its primary design goal. This is itself a radical methodological claim.
{{< /note >}}
{{< note ref="NOTE // Paxos" >}}
Lamport's Paxos (1998) is provably correct and theoretically elegant, but notoriously difficult to implement. Most production Paxos implementations diverge significantly from the paper — the gap between theory and practice is the problem Raft was designed to close.
{{< /note >}}
{{< /margin >}}

<!-- §II -->

{{< section-rule >}}

{{< section num="02" label="§ II.  Simplicity Breeds Innovation" >}}

{{< gutter sigil="simplicity" >}}

{{< main-col >}}

<p>Raft's core strength lies in its clear, concise design. The algorithm is easy to grasp, making it accessible to a broader range of developers. This ease of understanding empowers more people to build, debug, and contribute to distributed systems. Imagine a system where implementing consensus requires deciphering complex mathematical formulas. Only a select few with specialized knowledge could participate. Raft breaks down these barriers, allowing <span class="rubric">more minds to enter the distributed computing arena</span>.</p>

<p>This wider participation fosters a crucial element in any domain: the exchange of ideas. With more people involved, the potential for innovation explodes. New perspectives fuel discussions, leading to creative solutions and novel approaches to distributed system problems. This collective intelligence is the driving force behind the impressive applications built on Raft.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // etcd" >}}
etcd — the distributed key-value store at the heart of Kubernetes — is a Raft implementation. By extension, every Kubernetes cluster in production is a testament to Raft's practical viability.
{{< /note >}}
{{< /margin >}}

<!-- §III -->

{{< section-rule >}}

{{< section num="03" label="§ III.  Democratizing Computation" >}}

{{< gutter sigil="democracy" >}}

{{< main-col >}}

<p>Raft's success points towards a larger truth: <span class="rubric">democratizing computation</span> and the fundamental concepts that underpin it lies in the critical path of high-speed innovation. If we want to unlock new capabilities and possibilities in the distributed computing landscape, we need to focus on people as the originators of these ideas.</p>

<p>By making core concepts accessible, we empower a wider range of minds to contribute. This not only expands the talent pool but also fosters a culture of collaboration. When developers can readily understand and build upon existing solutions, progress accelerates.</p>

<p>This approach extends beyond consensus algorithms. Consider programming languages. Languages like Python and Go, with their focus on readability and simplicity, have seen widespread adoption and fueled a wave of innovation in various domains.</p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // Go" >}}
Go's design philosophy mirrors Raft's: simplicity over cleverness, readability over expressiveness. Both prioritize the collective productivity of a team over the virtuosity of an individual.
{{< /note >}}
{{< /margin >}}

<!-- §IV -->

{{< section-rule >}}

{{< section num="04" label="§ IV.  The Human Factor" >}}

{{< gutter sigil="human" >}}

{{< main-col >}}

<p>The rise of Raft highlights a critical shift in mindset. While raw computational power and theoretical efficiency remain important, prioritizing human understanding can be equally, if not more, valuable. By designing systems that are clear, concise, and approachable, we unlock the <span class="rubric">true potential for innovation</span>.</p>

<p>This doesn't mean sacrificing performance entirely. Efficient algorithms still have a place. However, the focus should be on creating a balance between complexity and comprehension. Algorithms that are both powerful and understandable can truly unleash the creative potential of the human mind.</p>

<p>So next time you're tackling a distributed system challenge, remember the Raft story. Consider the human factor and prioritize clarity alongside performance. By making your solution understandable, you might just open the door to a new wave of innovation.</p>

<p>Want to dive deeper into Raft? Check out the original paper (much like the algorithm itself, it's known for its readability): <a href="https://raft.github.io/raft.pdf">raft.github.io/raft.pdf</a></p>

{{< /main-col >}}

{{< margin >}}
{{< note ref="NOTE // balance" >}}
The tension between theoretical optimality and practical understandability recurs across computing: RISC vs CISC, SQL vs NoSQL, REST vs RPC. In each case, the "simpler" option often wins in adoption, even when the "better" option wins on benchmarks.
{{< /note >}}
{{< /margin >}}
