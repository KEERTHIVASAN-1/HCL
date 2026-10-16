import java.util.Objects;

public final class Student {
    private final String id;
    private final String name;

    public Student(String id, String name) {
        this.id = requireText(id, "Student ID");
        this.name = requireText(name, "Student name");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank.");
        }
        return value.trim();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Student student)) {
            return false;
        }
        return id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student{id='" + id + "', name='" + name + "'}";
    }
}
