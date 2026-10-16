# AI Usage Log — Day 11

These entries document the prompt/review exercise. AI-generated suggestions
were treated as drafts; the final code was checked against the requirements
and reviewed for correctness before use.

| # | Prompt or task | AI contribution | Human review and outcome |
|---|---|---|---|
| 1 | Vague prompt: "Make a Student class and service for a school." | Produced a basic entity/service outline to compare against a more constrained request. | Reviewed for encapsulation, null/blank validation, ID identity, collection exposure, lookup choice, and exception handling. Used the shortcomings as the issue checklist; see `AI-CODE-REVIEW.md`. |
| 2 | Specific prompt requesting an immutable Student, ID-based equality, duplicate rejection, and safe roster access. | Suggested a POJO/service/repository shape and a runnable demonstration. | Kept the clear responsibility split, checked equality/hash consistency, validated inputs, and confirmed the returned roster is an immutable snapshot. |
| 3 | "Add one more entity and repository; use core Java, preserve registration order, support ID lookup, and do not hide errors." | Suggested an additional entity/repository design and CRUD-style lookup/list operations. | Chose `Student` as the entity and `LinkedHashMap` for repository storage; verified deterministic listing, duplicate rejection in the service, and visible validation errors. |

No external libraries or generated code tools are required to build this
exercise. The examples are educational and the repository stores data only in
memory.
