---
title: "The Art of Technical Debt"
date: 2024-09-16T12:27:00-04:00
draft: false
cover:
  image: /images/technical-debt-banner.png
  alt: A banner image abstractly representing the intersectin of tech and finance
  relative: false
---

##### The Art of Technical Debt: When to Embrace It, When to Refactor

A specter is haunting software, the specter of technical debt. Read any discussion space where software engineers are present--Stack Overflow, the-website-formerly-known-as-Twitter, Mastodon--and you'll see "technical debt" discussed in terms ranging from frustration to contempt. The more level-headed commentators will acknowledge that technical debt is an inevitability, but generally with a sense that it's a manifestation of living in an imperfect world. But far from being uniformly negative, technical debt can be a powerful tool when wielded strategically--and not just for product-oriented spreadsheet jockeys, but for the day-to-day developer experience as well. This post aims to provide a framework for understanding technical debt and offer guidance on when to embrace it and when to prioritize refactoring.

#### Understanding Technical Debt

Let's begin with understanding technical debt and what differentiates it from, for example, iterative development or just plain bad software design. Technical debt refers to the implied cost of additional rework caused by choosing an easy (limited) solution now instead of using a more robust approach that would take longer to implement in the near term.

It's important that we distinguish this from other engineering choices that may appear, at first glance, to be similar. Releasing a limited feature set, using the minimum viable product (MVP) model, does not incur debt because we are not expecting costs related to reworking, but rather we are expecting *new* costs associated with continued feature development. Nor is it technical debt when we simply made bad design or implementation decisions that we are now paying for: technical debt is a strategic, intentional choice, not an engineering mistake.

> **Technical debt** refers to the implied cost of additional rework caused by choosing an easy (limited) solution now instead of using a more robust approach that would take longer to implement in the near term

#### The Financial Debt Metaphor

Just as financial debt can be a tool for growth when managed wisely, technical debt can accelerate product development. The original loan principal is the additional cost it would have taken to develop the more robust solution, while the interest is the increased difficulty of changing the system over time. The "payoff amount" is the time and cost that would be required to eliminate the debt at some point after it is originally assumed. Like financial debt, technical debt isn't inherently bad—it's all about how you manage it.

#### Strategic Framework for Technical Debt Management

Much like assuming financial debt, the basic framework for evaluating ROI of technical debt is actually relatively straightforward. There are unknowns that can never be predicted with certainty, but planners can make their best informed estimates and make tactical choices accordingly.

##### How to Evaluate a Debt Opportunity

There are three primary criteria I use to evaluate whether or not a particular problem is a good candidate for debt. This framework helps me to determine what the real benefits and cost structure of assuming some particular technical debt would be.

1. **Product Upside Evaluation**: Consider the potential market advantage of releasing sooner. If being first-to-market or quickly iterating on customer feedback could significantly boost your product's success, taking on some technical debt might be worthwhile. It's worthwhile to note that a lot of the choices we have the opportunity to make in software have real cost and timeline impacts on the order of developer-hours. I would generally evaluate these as insufficient upside. We should primarily be looking for debt scenarious with upsides on the order of days or longer.

2. **Debt Servicing Cost Estimation**: Evaluate both the short-term productivity impact and the long-term maintenance burden. If the debt can be easily isolated and doesn't significantly impede ongoing development, it might be acceptable. This is one that can often be harder to estimate. Developers are notoriously bad at cost estimates in the first place, asking for hypothetical long-term cost estimates only compounds this problem. This is why the technical assessment becomes critical: does this debt occur somewhere that can be isolated? Can we incur this debt while still applying principles of good software design? If so, we can reasonably expect lower long-term servicing costs.

3. **Organizational Support Assessment**: This is often underestimated or outright ignored in this process. We must gauge the understanding and buy-in from leadership. Ensure there's a commitment to allocate resources for eventual debt repayment, either with long-term servicing or an eventual lump-sum payoff. Without this support, even small debts can snowball into major issues, and this is also where developer experience takes the most significant hit: developers who are constantly working against debt, instead of building new things, live in a state of soul-crushing hamster-wheeling. It's also important to consider expectations management here: if your stakeholders begin to expect delivery time miracles, your overall debt position can spiral. It's important that stakeholders understand that a liability exists on the technical books with this choice, and that it must be paid down.

##### Managing Technical Debt Over the Long Run

Once we have assumed some technical debt in our project, we need to begin to manage it with a unified technical debt portfolio strategy. This needs to be holistically developed and managed, and not an ad hoc addendum to the rest of our development programme.

1. **Regular Debt Audits**: Periodically review and prioritize your technical debt. Not all debt is created equal, and some may even become irrelevant as your system evolves. It's important to understand which debts are becoming burdens on your books and which are not.

2. **Incremental Repayment**: Instead of attempting to clear all debt at once, integrate debt repayment into your development cycle. How exactly this is done is dependent on the particular debt. Is it possible to incrementally refactor the section as you work on components that interact with it? Can you incrementally develop a replacement system? These questions and structuring your SDLC around their answers helps maintain a balance between new feature development and system improvement.

3. **Balancing Act**: Strive for a balance between taking on new debt and repaying existing debt. This equilibrium is crucial for maintaining development velocity while keeping the codebase manageable. Accepting significant new technical debts without a concomitant increase in overall technical organization size or paying down some existing debt is a recipe for unhappy developers and grinding productivity to a halt.

#### Best Practices for Mitigating Technical Debt Risks

Once debt is on the books and being appropriately strategically managed, there are assets we can introduce that help us tactically manage those debts and prevent unexpected downside realizations. One of the failures of the "debt" metaphor is that financial debt usually has a known liability structure: we know exactly how and when we will incur servicing costs related to the financial debt. The same is not true for technical debt. It's very much possible for some new piece of code to contribute to technical debt's long-term costs without that being intentional or planned. As such, it's important we be aware of this risk and plan against it.

1. **Robust Testing Strategies**: Implement comprehensive unit and integration testing, especially around areas prone to technical debt. This practice helps catch debt-related issues early and prevents them from spreading.

2. **Enhanced Monitoring and Observability**: Use performance monitoring to identify debt-related bottlenecks and error tracking to prioritize debt repayment. This data-driven approach ensures you're addressing the most impactful debt first.

3. **Documentation and Knowledge Sharing**: Maintain clear documentation of known technical debt and share insights across teams. This transparency helps in making informed decisions about when to address specific debts.

#### Developer Joy

Thus far, I've written at length about how to evaluate and manage technical debt, and what some of the possibilities are that can contribute to destructive developer experiences. But I have not yet made the argument that well-managed debt can lead to *improved* developer experience, which is arguably the most controversial assertion I made in the introduction.

It is, however, an assertion I stand by. The basic architecture of developer joy lies in allowing developers to work on interesting projects, at high pace, and to succeed and ship frequently. Developers want to avoid working on tedious, grindy projects, they want to avoid spinning wheels on code that doesn't meaningfully push the project forward, and very, very few developers honestly enjoy solving bugs in legacy code (they exist, and you want them on your team, but they are not the bread and butter of your shop).

As such, what we can achieve with well-managed debt is a situation where developers can spend more time working on projects or features which move the overall product forward. As I stated earlier, a good candidate for debt is one where it can be well-encapsulated and isolated from other areas of code, and where there's a significant gap between the costs of the faster solution and the more robust solution. In this case, allowing developers to rapidly deploy, and then to isolate the debt section, will allow them to focus on more interesting projects.

Well-structured debt also gives your more ambitious engineers a knockdown target to aspire towards. By structuring the debt section with, for example, a clear and stable API, they can at some point work to clear the debt in a way that's both well-defined and challenging. For your best engineers, there is often no more enjoyable experience and no better use of their time.

#### Conclusion

Technical debt, when approached strategically, can be a valuable tool in a software engineering team's arsenal. The key lies in making informed decisions about when to take on debt, how to manage it, and when to prioritize its repayment. By maintaining a balanced approach and leveraging best practices, engineering leaders can use technical debt to drive innovation and speed without sacrificing long-term system health.

Remember, the goal isn't to eliminate technical debt entirely, but to manage it effectively. By doing so, you can harness its power to accelerate development while mitigating its risks, ultimately leading to more successful and sustainable software projects. However, at the beginning of this post 