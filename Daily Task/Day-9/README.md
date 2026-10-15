# Day 9 — Java Generics and Resources

## What this day covers

- `Pair<K, V>` stores and displays a typed key-value pair.
- `Box<T extends Number>` demonstrates a bounded numeric type and squaring its value.
- `<T extends Comparable<T>> max(...)` finds the larger of two comparable values.
- Wildcard helpers read from `List<? extends Number>` and add to `List<? super Integer>` (PECS: Producer Extends, Consumer Super).
- A short source comment explains generic type erasure.
- `CrudRepository<T, ID>` declares save, lookup, list, and delete operations. `InMemoryRepository<T, ID>` implements them using a `Map` and a typed `Function<T, ID>` identifier extractor. Employee and Product use the same implementation; it stores data only in memory and is not a persistent database.
- `products.csv` is read relative to this project directory with try-with-resources; malformed rows are skipped and a missing file is reported.
- Two `AutoCloseable` resources demonstrate reverse-order closing.

All examples and small entity types are kept in one Java source file, without external dependencies.

## Compile and run

Run these commands from the `Day-9` directory so the relative `products.csv` path resolves:

```text
javac --release 21 -Xlint:all -d out src/main/java/com/hclhostel/app/Day9Generics.java
java -cp out com.hclhostel.app.Day9Generics
```
