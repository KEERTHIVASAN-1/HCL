# Day 2 — Language Fundamentals + Git Fundamentals

## What I Learned

### Language Fundamentals
- **Data Types**: `byte`, `short`, `int`, `long` (integers), `float`, `double` (floating), `char`, `boolean` (primitives). Also `String` (reference type).
- **Arrays**: Fixed-size collection of same type. Syntax: `int[] arr = {1,2,3};`
- **Variables**: Named memory locations (local, instance, static).
- **Constants**: `final` keyword — value cannot change after init.
- **Operators**: Arithmetic (+, -, *, /, %), Comparison (==, !=, <, >), Logical (&&, ||, !), Assignment (=, +=, etc).
- **Type Casting**: Converting one type to another. Implicit (widening: `int` → `long` → `double`) and Explicit (narrowing: `double` → `int`).
- **Overflow**: When value exceeds the type's range (e.g., `Integer.MAX_VALUE + 1` goes negative).
- **Floating-Point Precision**: `double` is more precise than `float`, but neither is exact for decimal values.

### Git Fundamentals
- `git init` — Create a new repo locally.
- `git add .` — Stage all changed files.
- `git commit -m "message"` — Save staged changes with a message.
- `git push` — Upload commits to remote (GitHub).
- `git pull` — Download latest changes from remote.
- `.gitignore` — List files/folders Git should NOT track (e.g., `.class` files).
- **SSH**: Secure way to connect to GitHub without typing password every time.

## Practical Work

### Files
- `Constants.java` — `final` constants for hostel (rates, room count, etc).
- `SampleData.java` — Arrays of student names, room numbers, bed counts, etc.
- `MonthlyUsageAnalyser.java` — Uses arrays, constants, arithmetic, casting, ternary, `long`.

### How to Run
```bash
javac Constants.java SampleData.java MonthlyUsageAnalyser.java
java SampleData
java MonthlyUsageAnalyser
```

### Concepts Used in MonthlyUsageAnalyser
| Concept | Where |
|---|---|
| `final` constant | `TOTAL_DAYS` |
| `int[]` arrays | electricity, water data |
| `long` array | messAttendance (large totals) |
| Arithmetic operators | `+=`, `/` in loops |
| Type casting (double) | `(double) totalElectricity / TOTAL_DAYS` |
| Type casting (long) | `(long) (electricityBill + waterBill)` |
| Ternary operator | `messStatus = (avg >= 85) ? "Good" : "Average";` |
| `Integer.MAX_VALUE` | Initializer for `lowestWater` |
