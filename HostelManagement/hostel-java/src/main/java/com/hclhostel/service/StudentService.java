package com.hclhostel.service;

import com.hclhostel.entity.Student;
import com.hclhostel.entity.User;
import com.hclhostel.repository.StudentRepository;
import com.hclhostel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class StudentService {
    private final StudentRepository repo;
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final String defaultStudentPassword;

    public StudentService(StudentRepository repo,
                          UserRepository users,
                          PasswordEncoder passwordEncoder,
                          @Value("${hostel.student.default-password:student123}") String defaultStudentPassword) {
        this.repo = repo;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.defaultStudentPassword = defaultStudentPassword;
    }

    public List<Student> all() {
        return repo.findAll();
    }

    public Student save(Student s) {
        if (s.getName() == null || s.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (s.getEmail() == null || s.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        s.setName(s.getName().strip());
        s.setEmail(s.getEmail().strip().toLowerCase(Locale.ROOT));
        if (!s.getEmail().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            throw new IllegalArgumentException("Enter a valid email address");
        }
        if (s.getStudentId() == null || s.getStudentId().isBlank()) {
            s.setStudentId(nextStudentId());
        } else {
            s.setStudentId(s.getStudentId().strip());
        }
        if (repo.existsByStudentId(s.getStudentId())) {
            throw new IllegalArgumentException("Student ID already exists: " + s.getStudentId());
        }
        if (repo.existsByEmail(s.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + s.getEmail());
        }
        s.setUserId(s.getEmail());

        if (users.existsByUsername(s.getEmail())) {
            throw new IllegalArgumentException("A login account already exists for: " + s.getEmail());
        }

        try {
            users.save(new User(s.getName(), s.getEmail(), passwordEncoder.encode(defaultStudentPassword), "STUDENT"));
            return repo.save(s);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("Duplicate student ID or email", e);
        }
    }

    public Student findByUserId(String userId) {
        return repo.findByUserId(userId).orElse(null);
    }

    private String nextStudentId() {
        long next = repo.count() + 1;
        String studentId = "STU" + String.format("%03d", next);
        while (repo.existsByStudentId(studentId)) {
            next++;
            studentId = "STU" + String.format("%03d", next);
        }
        return studentId;
    }
}
