# Day 10 — Java Memory Management Lab

## What this day covers

- `MemoryLab.java` demonstrates stack overflow, heap exhaustion, string
  concatenation, garbage-collection logging, and the `Integer` cache.
- `monitoring/MemorySnapshot.java` captures a point-in-time view of the JVM
  heap's used, committed, and maximum memory.
- `monitoring/HeapUsageDemo.java` compares heap readings before and after
  retaining a few small byte arrays. JVM memory readings vary by runtime and
  should be treated as observations, not exact allocation measurements.
- `MemoryLabResults.md` records sample results and commands from one
  environment.

## Compile and run

Run these commands from the `Day-10` directory with a JDK installed:

```powershell
javac -Xlint:all -d out MemoryLab.java monitoring\MemorySnapshot.java monitoring\HeapUsageDemo.java
java -cp out MemoryLab strings
java -cp out monitoring.HeapUsageDemo
```

The stack and heap experiments intentionally catch their respective errors
inside isolated demonstrations. Run them in separate JVM processes; see
`MemoryLabResults.md` for the full set of experiments and options.
