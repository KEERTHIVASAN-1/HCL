# Day 1 — Java Platform Basics + Agile/Scrum Basics

## What I Learned

### Java Platform
- **JVM** (Java Virtual Machine): Runs the bytecode. Platform-dependent (different for Windows, Mac, Linux).
- **JRE** (Java Runtime Environment): JVM + libraries needed to *run* Java programs.
- **JDK** (Java Development Kit): JRE + tools (javac, java, javap) needed to *develop* Java programs.
- **JVM Architecture**: Class Loader → Bytecode Verifier → Execution Engine (Interpreter + JIT Compiler).
- **Flow**: `.java` file → `javac` compiler → `.class` bytecode → JVM → machine code.

### Agile / Scrum
- **Sprint**: A fixed time-box (usually 2-4 weeks) where a usable product increment is delivered.
- **Standup**: Daily 15-min meeting — What I did, What I'll do, Blockers.
- **Retrospective**: End-of-sprint meeting to improve process (What went well? What didn't? Action items).
- **User Stories**: Short descriptions of a feature from the user's perspective: "As a [role], I want [feature], so that [benefit]."
- **Story Points**: Relative estimate of effort (1, 2, 3, 5, 8, 13... Fibonacci).
- **Definition of Done (DoD)**: Checklist that must pass before a story is considered complete.

## Practical Work

### Files
- `PlatformInfo.java` — Prints Java version, OS name, processors, max/free heap.

### How to Run
```bash
javac PlatformInfo.java
java PlatformInfo
javap -c PlatformInfo
java -verbose:class PlatformInfo
```

### Output Expected
```
=== Java Platform Information ===
Java Version  : 17.0.x
OS Name       : Windows 11
Processors    : 8
Max Heap (MB) : 2048
Free Heap (MB): 245
```

### Key Commands Explained
- `javac` — Compiles `.java` to `.class` bytecode.
- `java` — Runs the bytecode on JVM.
- `javap -c` — Shows disassembled bytecode (see what the compiler generated).
- `java -verbose:class` — Shows every class being loaded by the JVM at runtime.
