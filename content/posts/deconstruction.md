---
title: "Deconstructing the Instruction-Data Dichotomy"
date: 2024-06-17T12:45:18-04:00
cover:
  image: /images/deconstruction-banner.png
  alt
draft: false
---

### Deconstructing the Instruction-Data Dichotomy

In the software engineering discourse, a pervasive myth has long shaped our understanding of code and its relationship to data. This myth posits a clear, binary separation: code consists of instructions that operate on extrinsic data. It's a comforting notion, one that neatly compartmentalizes the complexities of programming into active agents (code) and passive recipients (data). This dichotomy has become so ingrained in our collective consciousness that it often goes unquestioned, forming the bedrock of how we conceptualize, teach, and practice software development.

However, this widely accepted paradigm is not just an oversimplification—it's fundamentally flawed. The supposed boundary between instruction and data is far more porous and dynamic than we typically acknowledge. This false dichotomy limits our understanding of computation's true nature and, consequently, constrains our ability to innovate and solve complex problems effectively.

In this exploration, we will challenge this entrenched belief, demonstrating that the relationship between code and data is far more intricate and intertwined than the traditional view suggests. By deconstructing this myth, we aim to reveal a more nuanced and powerful perspective on the nature of software—one that recognizes the fluid interplay between instruction and data at every level of computation.

This reconceptualization is not merely an academic exercise. Understanding the true nature of the instruction-data relationship has profound implications for how we approach software design, team dynamics, and the very role of computation in solving real-world problems. By shedding light on this often-overlooked aspect of programming, we can open new avenues for creativity, collaboration, and innovation in software engineering.

#### All Code is Data

The most basic issue here is that your code cannot operate, your code has no meaning, without the possibility of interpreting it as data. Your ability to work on your code requires that your hard drive be able to store the code as data. It requires that your IDE be able to open, parse, and syntax highlight the code as data. Even more fundamentally, your interpreter, compiler, or assembler is treating your code as data to produce machine code: to the compiler, even your code is an extrinsic data source upon which its instructions operate.

Nor is this merely an artifact of specific software subjectivities, we are not free to consider our code as entirely instructions from our perspective and live-and-let-live as the IDE, hard drive, and compiler interpret it as data from theirs. In our own interactions with code, we have always latent the possibility of dataness in our code. Consider the possibility of self-modifying code, or metaprogramming. I'm not just here discussing polymorphic malware, but consider the following everyday, commonplace snippet of Ruby-on-Rails code:

```ruby
class User < ApplicationRecord
  has_many :posts
end
```

The `has_many` method has the technical function of creating an instance method called `posts` on the `User` class. That is to say, it *modifies the class at runtime to create and attach instructions we did not write*. This is only possible because the `has_many` method is capable of treating our class not merely as a lexical context, but as a parameter it receives. At runtime, our class abstraction is treated directly as data.

#### Data can be instruction

One of the first techniques many of us learn is the table-driven methods, canonically articulated in the masterful Code Complete. In a table-driven method, instead of using switch statements or if/then logic to determine which branch of instruction to take, we use a data structure and a lookup in that data structure to drive our behavior. In JavaScript, we might do this:

```javascript
const operations = {
  add: (x, y) => x + y,
  subtract: (x, y) => x - y,
  multiply: (x, y) => x * y,
  divide: (x, y) => {
    if (y === 0) throw new Error('Division by zero');
    return x / y;
  }
};
```

This is straightforwardly a data structure. It is stored on the heap like data, it is subject to garbage collection like data, and it is referenceable and can be passed like data, because it *is data*. But it also can be executed like instruction, it semantically contains functions like instruction, and it can throw errors like instruction. Because it also *is instruction*.

While this is a toy example, table-driven methods on the whole are quite production-worthy, and there are other forms of data-driven instruction available to us. An engineer building a scheduling service might allow arbitrary event repetition rules to be saved in a database and executed as instance generators. A robotics engineer can set configuration parameters that dramatically impact the behavior of a robot. A machine learning scientist tunes hyperparameters to instruct his system's learning process. There is an entire universe where information that's initially conceived of as data is behaviorally indistinguishable from instruction.

### The Apotheosis: Homoiconicity

Our boundary collapse between instruction and data reaches its fullest expression in languages that demonstrate homoiconicty. Homoiconicity is a property of a programming language where its code and data structures have semantically indistinguishable representations, and thus the code can operate on itself in place.

The most famous homoiconic language is the Lisp family. Lisp's primary data primitive is the [S-expression](https://en.wikipedia.org/wiki/S-expression), but a Lisp program itself is nothing more nor less than a single massive S-expression. That is to say, in Lisp, programs are constructed directly from the very data structures they manipulate.

```lisp
(defun mirror-of-computation (code)
  (if (listp code)
      (cons (mirror-of-computation (car code))
            (mirror-of-computation (cdr code)))
  code))
```

This recursive code takes code as its input and outputs that code, reversed. But is that reversal an operation on data, or is it a runtime operation on code? Can it run on itself? How shall we view this, and frankly what, exactly, is genuinely extrinsic to this function?

Nor is this feature unique to Lisp. In fact, at the machine code level, **all Von Neumann architectures demonstrate homiconicity**: there is no intrinsic way to determine whether some byte flowing through a machine is data or instruction, the function of a particular byte is determined entirely by its spatio-temporal execution context--that is, where and when it enters the machine. Consider the following partial table of x86_64 opcodes:

|Opcode    |  Instruction | Description
|----------|----------------|-----------------------
|0x50-0x57 |  PUSH r64  |Push 64-bit register onto stack
|0x58-0x5F |  POP r64     |Pop 64-bit register from stack
|0x89    | MOV r/m64, r64 |Move 64-bit register to register/memory
|0x8B    | MOV r64, r/m64 |Move 64-bit register/memory to register
|0xB8-0xBF |  MOV r64, imm64| Move 64-bit immediate to register
|0x01    |ADD r/m64, r64  |Add 64-bit register to register/memory
|0x29    |SUB r/m64, r64  |Subtract 64-bit register from register/memory
|0x31    |XOR r/m64, r64  |XOR 64-bit register with register/memory
|0x39    |CMP r/m64, r64  |Compare 64-bit register with register/memory

The opcodes in the left column represent the sequence of bytes that are used to instruct the CPU to perform a command. We cannot help but note that these are simply that: sequences of bytes, identical in every way to any other sequence of bytes. Thus, a total collapse of the boundaries between instruction and data sits at the very bottom of our stack, and we cannot meaningfully conceive of code as an active agent operating on inert, extrinsic data.

#### Why it matters

Our basic cognitive primitives about what software is and how it's constructed inform our positions on what problems software solves, how it solves them, and who it solves them for. As one example, the tendency to view software as primarily an automation tool is rooted in this mistaken belief that software is active instruction that operates on inert data: in the most immediate reading of this automation-oriented perspective, it relies on the idea that there is some information which can be ingested and business processes can be applied to that data. While automation is a considerable power of computing, it is by no means the only or even the most common form of desirable behavior from a computer.

But more broadly, it encourages a limited and strictly hierarchical view of the possibilities of computation. It prevents us from asking how we might more creatively understand our work, but it also prevents us from asking how we might more collaboratively understand our work modalities themselves. If it is only ours to write code against inert data, then we the developers must also behave as active participants against inert data. We must receive data from outside sources. And this is reflected in the most common forms of software organization structure: we *receive* UX designs, or requirements specifications, or access to data lakes, and then we *act* upon those inert artifacts. This prevents us from being active participants in the work process and alienates us from our organizations.

Unwinding this limited and narrow view of software is, I believe, critical to creating more effective software and more effective software teams. And it begins with this analysis of the real meaning of code.