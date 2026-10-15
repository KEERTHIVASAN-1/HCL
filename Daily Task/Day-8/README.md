# Day 8 — Java Maps, Sorting, and Object Equality

## What this day covers

The assignment is under `src/main/java/com/hclhostel/`:

- `model/Employee.java` defines an employee, natural ordering by ID, and consistent `equals`/`hashCode` by ID.
- `model/BadEmployee.java` intentionally demonstrates a broken `hashCode` contract: objects equal by ID can have different hash codes.
- `app/EmployeeDirectory.java` demonstrates:
  - `HashMap` lookup by employee ID and department counts `Map.merge`.
  - `TreeMap` for sorted department reports and `LinkedHashMap` for insertion order.
  - Natural ordering and custom comparators for employee reports.
  - A `HashMap` lookup failure caused by the intentionally incorrect `BadEmployee` implementation, contrasted with the correct `Employee`.

## Compile and run

From the `Day-8` directory, with a JDK installed:

```text
javac -d out src/main/java/com/hclhostel/model/Employee.java src/main/java/com/hclhostel/model/BadEmployee.java src/main/java/com/hclhostel/app/EmployeeDirectory.java
java -cp out com.hclhostel.app.EmployeeDirectory
```
