import java.util.List;

public class Day11Main {
    private Day11Main() {
    }

    public static void main(String[] args) {
        StudentRepository repository = new StudentRepository();
        StudentService students = new StudentService(repository);

        students.register(new Student("S101", "Maya Patel"));
        students.register(new Student("S102", "Arun Kumar"));

        System.out.println("=== Student lookup (FR8) ===");
        System.out.println("S101: " + students.findById("S101").orElseThrow());

        System.out.println("\n=== Students in registration order (FR8) ===");
        List<Student> roster = students.findAll();
        roster.forEach(System.out::println);

        System.out.println("\n=== Duplicate ID validation (FR7) ===");
        try {
            students.register(new Student("S101", "Another Student"));
        } catch (IllegalArgumentException exception) {
            System.out.println("Registration rejected: " + exception.getMessage());
        }

        System.out.println("\n=== Required-field validation (FR7) ===");
        try {
            students.register(new Student(" ", "No ID"));
        } catch (IllegalArgumentException exception) {
            System.out.println("Registration rejected: " + exception.getMessage());
        }
    }
}
