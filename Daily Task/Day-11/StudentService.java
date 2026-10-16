import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = Objects.requireNonNull(repository, "Repository must not be null.");
    }

    public void register(Student student) {
        Student checkedStudent = Objects.requireNonNull(student, "Student must not be null.");
        if (repository.findById(checkedStudent.getId()).isPresent()) {
            throw new IllegalArgumentException(
                    "Student ID already exists: " + checkedStudent.getId());
        }
        repository.save(checkedStudent);
    }

    public Optional<Student> findById(String studentId) {
        return repository.findById(requireStudentId(studentId));
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    private static String requireStudentId(String studentId) {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("Student ID must not be blank.");
        }
        return studentId.trim();
    }
}
