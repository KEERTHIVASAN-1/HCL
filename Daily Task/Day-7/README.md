# Day 7 — Java Collections

## What this day covers

`Day7Main.java` demonstrates Java collection interfaces and common implementations:

- `List` with `ArrayList` for ordered catalogue entries and indexed access.
- `Set` with `LinkedHashSet` to reject duplicate student IDs while preserving insertion order.
- `Queue` with `ArrayDeque` for FIFO waitlist processing.
- `PriorityQueue` with a comparator to process faculty requests first.
- `Iterator.remove()` for safe removal during iteration, and `ListIterator` for forward and reverse traversal.
- `ArrayList` versus `LinkedList` indexed reads and front insertions using a small timing experiment. Timings vary by machine and JVM run.

The program is a standalone Java file with no external dependencies.

## Compile and run

From this directory, with a JDK installed:

```text
javac Day7Main.java
java Day7Main
```
