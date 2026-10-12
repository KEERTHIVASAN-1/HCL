package com.hclhostel.repository;

import com.hclhostel.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByStudentId(String studentId);
    Optional<Student> findByEmail(String email);
    Optional<Student> findByUserId(String userId);
    boolean existsByStudentId(String studentId);
    boolean existsByEmail(String email);
}
