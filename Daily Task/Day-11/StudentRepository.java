import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class StudentRepository {
    private final Map<String, Student> studentsById = new LinkedHashMap<>();

    public void save(Student student) {
        Student checkedStudent = Objects.requireNonNull(student, "Student must not be null.");
        studentsById.put(checkedStudent.getId(), checkedStudent);
    }

    public Optional<Student> findById(String studentId) {
        String checkedId = Objects.requireNonNull(studentId, "Student ID must not be null.");
        return Optional.ofNullable(studentsById.get(checkedId));
    }

    public List<Student> findAll() {
        return List.copyOf(studentsById.values());
    }
}
