# Day 11 — AI Integration (Core Java)

## Project summary

This project demonstrates a student registration example built with core Java.
The task referred to FR7–FR8 but did not include their definitions in the
repository; this exercise uses the following interpretation:

- **FR7:** Register students with non-blank IDs and names, and reject duplicate
  IDs.
- **FR8:** Find students by ID and list them in registration order.

## What was implemented

- `Day11Main.java` is the runnable entry point. It demonstrates student
  registration, lookup, roster listing, duplicate-ID rejection, and required
  field validation.
- `Student.java` defines an immutable student entity. It trims and validates
  its ID and name and implements ID-based `equals` and `hashCode`.
- `StudentService.java` validates registration and lookup requests, rejects
  duplicate IDs, and delegates storage to the repository.
- `StudentRepository.java` uses a `LinkedHashMap` for ID lookup and
  registration-order listing. Roster results are immutable snapshots, not
  references to internal storage.

Invalid inputs produce explicit exceptions; the demo catches only the expected
validation errors to show their messages. Data is stored in memory and is lost
when the program exits.

## Compile and run

From the `Day-11` directory, with a JDK installed:

```powershell
javac -Xlint:all -d out Day11Main.java Student.java StudentService.java StudentRepository.java
java -cp out Day11Main
```
