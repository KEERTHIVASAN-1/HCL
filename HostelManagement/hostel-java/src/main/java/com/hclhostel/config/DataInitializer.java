package com.hclhostel.config;

import com.hclhostel.entity.Student;
import com.hclhostel.entity.User;
import com.hclhostel.repository.StudentRepository;
import com.hclhostel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository users;
    private final StudentRepository students;
    private final PasswordEncoder encoder;

    @Value("${hostel.demo.admin.username:admin}") private String adminUser;
    @Value("${hostel.demo.admin.password:admin123}") private String adminPass;
    @Value("${hostel.demo.warden.username:warden}") private String wardenUser;
    @Value("${hostel.demo.warden.password:warden123}") private String wardenPass;
    @Value("${hostel.demo.student.username:student}") private String studentUser;
    @Value("${hostel.demo.student.password:student123}") private String studentPass;
    @Value("${hostel.demo.maintenance.username:maintenance}") private String maintUser;
    @Value("${hostel.demo.maintenance.password:maint123}") private String maintPass;

    public DataInitializer(UserRepository users, StudentRepository students, PasswordEncoder encoder) {
        this.users = users;
        this.students = students;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        createIfMissing("Admin User", adminUser, adminPass, "ADMIN");
        createIfMissing("Warden Singh", wardenUser, wardenPass, "WARDEN");
        createIfMissing("Student One", studentUser, studentPass, "STUDENT");
        createIfMissing("Maintenance Staff", maintUser, maintPass, "MAINTENANCE_STAFF");
        createDemoStudentProfile();
    }

    private void createIfMissing(String name, String username, String rawPassword, String role) {
        if (!users.existsByUsername(username)) {
            users.save(new User(name, username, encoder.encode(rawPassword), role));
            System.out.println("Created demo user: " + username + " (" + role + ")");
        }
    }

    private void createDemoStudentProfile() {
        if (students.findByUserId(studentUser).isPresent() || students.findByEmail(studentUser).isPresent()) {
            return;
        }

        Student demoStudent = new Student(nextStudentId(), "Student One", studentUser);
        demoStudent.setUserId(studentUser);
        students.save(demoStudent);
        System.out.println("Created demo student profile: " + demoStudent.getStudentId());
    }

    private String nextStudentId() {
        long next = students.count() + 1;
        String studentId = "STU" + String.format("%03d", next);
        while (students.existsByStudentId(studentId)) {
            next++;
            studentId = "STU" + String.format("%03d", next);
        }
        return studentId;
    }
}
