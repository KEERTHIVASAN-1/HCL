package com.hclhostel.service;

import com.hclhostel.entity.Allocation;
import com.hclhostel.repository.AllocationRepository;
import com.hclhostel.repository.RoomRepository;
import com.hclhostel.repository.StudentRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AllocationService {
    private final AllocationRepository allocations;
    private final RoomRepository rooms;
    private final StudentRepository students;

    public AllocationService(AllocationRepository allocations, RoomRepository rooms, StudentRepository students) {
        this.allocations = allocations;
        this.rooms = rooms;
        this.students = students;
    }

    public List<Allocation> all() {
        return allocations.findAll();
    }

    public List<Integer> availableBeds(String roomNo) {
        if (roomNo == null || roomNo.isBlank()) {
            return List.of();
        }

        var room = rooms.findByRoomNo(roomNo).orElse(null);
        if (room == null) {
            return List.of();
        }

        List<Integer> availableBeds = new ArrayList<>();
        for (int bedNo = 1; bedNo <= room.getCapacity(); bedNo++) {
            if (allocations.findByRoomNoAndBedNo(roomNo, bedNo).isEmpty()) {
                availableBeds.add(bedNo);
            }
        }
        return availableBeds;
    }

    public Allocation allocate(String studentId, String roomNo, int bedNo) {
        if (studentId == null || studentId.isBlank() || roomNo == null || roomNo.isBlank()) {
            throw new IllegalArgumentException("Student and room are required");
        }
        String normalizedStudentId = studentId.strip();
        String normalizedRoomNo = roomNo.strip();
        var student = students.findByStudentId(normalizedStudentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + normalizedStudentId));
        var room = rooms.findByRoomNo(normalizedRoomNo)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + normalizedRoomNo));
        if (bedNo < 1 || bedNo > room.getCapacity()) {
            throw new IllegalArgumentException("Bed number must be between 1 and " + room.getCapacity());
        }
        if (allocations.findByStudentId(normalizedStudentId).isPresent()) {
            throw new IllegalStateException("Student " + normalizedStudentId + " already has an active allocation");
        }
        try {
            return allocations.save(new Allocation(normalizedStudentId, normalizedRoomNo, bedNo));
        } catch (DuplicateKeyException e) {
            if (allocations.findByRoomNoAndBedNo(normalizedRoomNo, bedNo).isPresent()) {
                throw new IllegalStateException("Bed " + bedNo + " in room " + normalizedRoomNo + " is already allocated", e);
            }
            if (allocations.findByStudentId(normalizedStudentId).isPresent()) {
                throw new IllegalStateException("Student " + normalizedStudentId + " already has an active allocation", e);
            }
            throw new IllegalStateException("This student or bed already has an allocation", e);
        }
    }
}
