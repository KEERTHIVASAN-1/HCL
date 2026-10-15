package com.hclhostel.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class Day9Generics {
    private Day9Generics() {
    }

    public static void main(String[] args) {
        demonstrateToolkit();
        demonstrateEmployeeRepository();
        loadProducts();
        demonstrateResourceClosing();
    }

    private static void demonstrateToolkit() {
        System.out.println("=== Generic Toolkit ===");
        Pair<String, Integer> pair = new Pair<>("Room", 12);
        System.out.println("Pair: " + pair);

        Box<Integer> box = new Box<>(6);
        System.out.println("Box value: " + box.getValue()
                + "; squared: " + box.square());
        System.out.println("Maximum: " + max(18, 42));

        List<Integer> scores = List.of(10, 20, 30);
        System.out.println("Sum from extends-number list: " + sumNumbers(scores));

        List<Number> numbers = new ArrayList<>();
        addIntegers(numbers, 4, 5, 6);
        System.out.println("Integers added to super-integer list: " + numbers);
        // PECS: Producer Extends for reading; Consumer Super for adding.
    }

    private static void demonstrateEmployeeRepository() {
        System.out.println("\n=== Employee Repository ===");
        CrudRepository<Employee, Integer> employees =
                new InMemoryRepository<>(Employee::getId);
        employees.save(new Employee(1, "Maya"));
        employees.save(new Employee(2, "Arun"));
        System.out.println("findById(1): " + employees.findById(1));
        System.out.println("findAll(): " + employees.findAll());
        employees.deleteById(1);
        System.out.println("After delete, findById(1): " + employees.findById(1));
    }

    private static void loadProducts() {
        System.out.println("\n=== Products from products.csv ===");
        CrudRepository<Product, String> products =
                new InMemoryRepository<>(Product::getId);
        Path csvPath = Path.of("products.csv");

        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            String row = reader.readLine();
            if (row == null || !row.equals("id,name,price")) {
                System.out.println("Could not load products: expected CSV header id,name,price.");
                return;
            }

            int lineNumber = 1;
            while ((row = reader.readLine()) != null) {
                lineNumber++;
                String[] fields = row.split(",", -1);
                if (fields.length != 3 || fields[0].isBlank() || fields[1].isBlank()) {
                    System.out.println("Skipping malformed CSV row " + lineNumber + ": " + row);
                    continue;
                }

                try {
                    products.save(new Product(fields[0].trim(), fields[1].trim(),
                            Double.parseDouble(fields[2].trim())));
                } catch (NumberFormatException exception) {
                    System.out.println("Skipping malformed CSV row " + lineNumber
                            + ": invalid price.");
                }
            }
        } catch (IOException exception) {
            System.out.println("Could not read " + csvPath
                    + ": " + exception.getMessage());
            return;
        }

        products.findAll().forEach(System.out::println);
        System.out.println("findById(P101): " + products.findById("P101"));
        products.deleteById("P103");
        System.out.println("After delete, findById(P103): "
                + products.findById("P103"));
    }

    private static void demonstrateResourceClosing() {
        System.out.println("\n=== AutoCloseable resources ===");
        try (TraceResource first = new TraceResource("first");
             TraceResource second = new TraceResource("second")) {
            System.out.println("Using resources " + first.name + " and " + second.name + ".");
        }
    }

    // Generic type parameters are primarily checked at compile time; type
    // erasure removes them from ordinary runtime type information.
    private static final class Pair<K, V> {
        private final K key;
        private final V value;

        private Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + " = " + value;
        }
    }

    private static final class Box<T extends Number> {
        private final T value;

        private Box(T value) {
            this.value = value;
        }

        private T getValue() {
            return value;
        }

        private double square() {
            return value.doubleValue() * value.doubleValue();
        }
    }

    private static <T extends Comparable<T>> T max(T first, T second) {
        return first.compareTo(second) >= 0 ? first : second;
    }

    private static double sumNumbers(List<? extends Number> values) {
        double sum = 0;
        for (Number value : values) {
            sum += value.doubleValue();
        }
        return sum;
    }

    private static void addIntegers(List<? super Integer> destination,
                                    Integer... values) {
        for (Integer value : values) {
            destination.add(value);
        }
    }

    private interface CrudRepository<T, ID> {
        void save(T entity);

        Optional<T> findById(ID id);

        List<T> findAll();

        void deleteById(ID id);
    }

    // In-memory only: entries disappear when the program exits.
    private static final class InMemoryRepository<T, ID>
            implements CrudRepository<T, ID> {
        private final Map<ID, T> entries = new LinkedHashMap<>();
        private final Function<T, ID> idExtractor;

        private InMemoryRepository(Function<T, ID> idExtractor) {
            this.idExtractor = Objects.requireNonNull(idExtractor);
        }

        @Override
        public void save(T entity) {
            T checkedEntity = Objects.requireNonNull(entity);
            ID id = Objects.requireNonNull(idExtractor.apply(checkedEntity));
            entries.put(id, checkedEntity);
        }

        @Override
        public Optional<T> findById(ID id) {
            return Optional.ofNullable(entries.get(id));
        }

        @Override
        public List<T> findAll() {
            return new ArrayList<>(entries.values());
        }

        @Override
        public void deleteById(ID id) {
            entries.remove(id);
        }
    }

    private static final class Employee {
        private final int id;
        private final String name;

        private Employee(int id, String name) {
            this.id = id;
            this.name = name;
        }

        private Integer getId() {
            return id;
        }

        @Override
        public String toString() {
            return id + " | " + name;
        }
    }

    private static final class Product {
        private final String id;
        private final String name;
        private final double price;

        private Product(String id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        private String getId() {
            return id;
        }

        @Override
        public String toString() {
            return id + " | " + name + " | $" + String.format("%.2f", price);
        }
    }

    private static final class TraceResource implements AutoCloseable {
        private final String name;

        private TraceResource(String name) {
            this.name = name;
        }

        @Override
        public void close() {
            System.out.println("Closed " + name);
        }
    }
}
