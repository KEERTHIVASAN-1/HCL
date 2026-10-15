# Day 10 — Java Memory Management Lab

## Results from this environment

Observed with Java 25.0.1 on Windows. The stack and heap experiments each ran in
their own JVM. String timings are one observed run with three trials and no
warm-up; they are examples, not universal benchmarks.

| Experiment | Command or JVM option | Observed result | Explanation in simple words |
|---|---|---|---|
| Stack overflow | `java -Xss256k -cp out MemoryLab stack`; `java -Xss512k -cp out MemoryLab stack` | Caught `StackOverflowError` after 2,020 calls with 256k and 6,506 calls with 512k. | Each recursive call uses a stack frame. A larger configured thread stack allowed more calls in these runs. Catching the error is only for this controlled demo, not normal application recovery. |
| Heap limit | `java -Xmx64m -cp out MemoryLab heap` | Caught `OutOfMemoryError` after retaining 29 MiB of one-MiB arrays. | `-Xmx64m` limits the Java heap, not the whole JVM process. JVM metadata, thread stacks, native memory, and other process memory are outside that heap limit. |
| String concatenation | `java -cp out MemoryLab strings` | Both methods produced 5,000 characters in each trial. `String +=`: 21.482, 8.817, 11.394 ms. `StringBuilder`: 1.263, 0.513, 0.213 ms. | Strings are immutable, so repeated concatenation may create intermediate strings. These measurements depend on JDK, machine, JIT optimization, and workload; they do not prove `StringBuilder` always wins. |
| Garbage collection log | `java -Xmx64m -Xlog:gc -cp out MemoryLab gc` | G1 was selected. The 256 MiB total allocation workload produced 49 logged young-pause events; for example, `GC(0) Pause Young ... 28M->17M(64M)`. | A GC message is a JVM log entry for a collection event; look for `[info][gc]` and `GC(n)`. The heap occupancy in this event decreased, but the log does not identify any particular object. Allocations do not each immediately trigger collection. |
| Integer cache | `java -cp out MemoryLab integer` | For 100: `== true`, `equals() true`. For 1000: `== false`, `equals() true`. | `Integer.valueOf()` may reuse cached wrapper objects. For wrappers, `==` compares references while `equals()` compares values. Cache behavior beyond the standard range may depend on JVM configuration; use value comparison in application logic. |

## Reproduce in Windows PowerShell

Open PowerShell and run:

```powershell
Set-Location 'D:\hcl\Daily Task\Day-10'
javac -Xlint:all -d out MemoryLab.java
java -Xss256k -cp out MemoryLab stack
java -Xss512k -cp out MemoryLab stack
java -Xmx64m -cp out MemoryLab heap
java -cp out MemoryLab strings
java -Xmx64m -Xlog:gc -cp out MemoryLab gc
java -cp out MemoryLab integer
```

The `stack` and `heap` commands intentionally catch their respective errors
inside isolated demonstrations. Do not combine these experiments in a single
JVM process.

## Stack and heap illustration from Day 7

This is based on the existing `addToWaitlist` method in
`Day-7/Day7Main.java`. It receives a set, a queue, and a request; when the ID is
new, the set retains the ID and the queue retains the request.

```text
Thread stack                         Heap
------------------------------       ---------------------------------
addToWaitlist frame                  LinkedHashSet (ids)
  ids ----------------------------->   "S101" reference
  waitlist -----------------------> ArrayDeque (waitlist)
  request ------------------------>   Request object
                                       studentId --> "S101"
                                       book      --> "Java Basics"
                                       faculty   --> false
                                       overdue   --> false

After pollFirst() removes the request from the queue, the Request object can
become eligible for garbage collection once no local or other references to it
remain. The ID string is still reachable from the set until that entry is
removed or the set itself becomes unreachable.
```

Stack and heap are useful conceptual runtime areas. JVM/JIT optimization means
this diagram does not guarantee that every local variable or value occupies a
specific physical location.

## Review of Day 7–Day 9 for requested memory pitfalls

No confirmed memory issue was found that needs a code change:

- No unnecessarily growing static collections.
- Day 9's CSV reader is closed with try-with-resources.
- No repeated `String +=` operation inside a loop.
- Collections in the examples are local and small; Day 9's repository intentionally retains its sample entries while in use.
- No assumptions about `Integer` caching were found.

Day 8's `BadEmployee` deliberately uses a static counter to demonstrate an
incorrect `hashCode`; it does not retain a growing collection or cause a
memory-retention issue in the sample program.
