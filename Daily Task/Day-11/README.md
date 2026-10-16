# Day 11 — AI Integration (Core Java)

## Functional requirements for this exercise

The task refers to FR7–FR8 but does not include their original definitions in
this repository. For this standalone example, they are interpreted as:

- **FR7:** Register a student with a non-blank ID and name; reject duplicate IDs.
- **FR8:** Find a student by ID and return the roster in registration order.

## Files

- `Day11Main.java` runs the prompt/review example and demonstrates FR7–FR8.
- `Student.java` is an immutable student entity with ID-based `equals` and
  `hashCode`.
- `StudentService.java` applies registration validation and delegates storage.
- `StudentRepository.java` stores students in a `LinkedHashMap` and returns
  immutable roster snapshots.
- `AI-CODE-REVIEW.md` compares vague and specific prompts and records six
  issues and their fixes.
- `AI-USAGE.md` records three prompt/review entries.

## Compile and run

From the `Day-11` directory, with a JDK installed:

```powershell
javac -Xlint:all -d out Day11Main.java Student.java StudentService.java StudentRepository.java
java -cp out Day11Main
```

## Design notes

- This is an in-memory exercise; data is lost when the process exits.
- `LinkedHashMap` provides direct ID lookup and deterministic registration
  order for the roster.
- The entity is immutable, and `findAll()` returns an immutable snapshot rather
  than the repository's internal collection.
- Exceptions for invalid input are visible to callers; the main method catches
  only the expected validation errors to demonstrate the rejection messages.
