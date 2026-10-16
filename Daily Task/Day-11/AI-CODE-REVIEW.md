# Day 11 — Prompt and Review Drill

## Prompt comparison

| Prompt style | Prompt | Draft shape and review |
|---|---|---|
| Vague | "Make a Student class and service for a school." | A plausible first draft may use public mutable fields, accept blank values, expose its backing roster, omit consistent identity methods, linearly scan a list for IDs, and catch exceptions without reporting them. The prompt does not state ownership, validation, lookup, or error-handling requirements. |
| Specific | "Using core Java only, create an immutable Student with a non-blank trimmed ID and name, ID-based equals/hashCode, a service that rejects duplicate IDs, and an in-memory repository that supports ID lookup and registration-order listing without exposing its collection. Do not swallow errors. Include a runnable main demonstrating success and invalid input." | This is reflected in `Student.java`, `StudentService.java`, `StudentRepository.java`, and `Day11Main.java`. The requirements make boundaries, collection behavior, and failure behavior reviewable. |

## Review table: issues found and fixed

| # | Issue in the initial draft | Why it is a problem | Fix in the reviewed implementation |
|---|---|---|---|
| 1 | Student fields were public and mutable. | Callers can change an ID after insertion, making repository keys and entity state disagree. | `Student` is final, its fields are private and final, and it exposes getters only. |
| 2 | The constructor accepted null, empty, and whitespace-only fields. | Invalid records can enter the service and make lookup behavior inconsistent. | `Student` rejects null or blank ID/name values and trims accepted input. |
| 3 | `equals` compared IDs but `hashCode` used the object identity. | Equal students could have different hash codes, breaking hash-based collection behavior. | Both `equals` and `hashCode` use the student ID. |
| 4 | `findAll()` returned the repository's backing list. | A caller could remove or add entries without going through registration rules. | The repository returns an immutable snapshot using `List.copyOf`. |
| 5 | The repository used a list and scanned every entry for an ID. | ID lookups take linear time and duplicate checks are unnecessarily expensive. | `LinkedHashMap` supplies keyed lookup and preserves registration order. |
| 6 | A broad catch ignored registration and lookup failures. | Callers see apparent success even when the operation failed, making defects hard to diagnose. | Service and repository methods propagate meaningful validation errors; the demo catches only expected invalid-input exceptions and prints their messages. |

The final code was checked against each finding. The initial-draft descriptions
record the review drill; the runnable files contain the corrected version.
