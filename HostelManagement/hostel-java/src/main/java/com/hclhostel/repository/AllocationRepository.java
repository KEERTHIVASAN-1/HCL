package com.hclhostel.repository;

import com.hclhostel.entity.Allocation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AllocationRepository extends MongoRepository<Allocation, String> {
    List<Allocation> findByRoomNo(String roomNo);
    Optional<Allocation> findByStudentId(String studentId);
    Optional<Allocation> findByRoomNoAndBedNo(String roomNo, int bedNo);
    long countByRoomNo(String roomNo);
}
