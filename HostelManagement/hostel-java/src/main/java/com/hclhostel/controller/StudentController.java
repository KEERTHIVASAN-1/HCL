package com.hclhostel.controller;

import com.hclhostel.entity.Student;
import com.hclhostel.repository.AllocationRepository;
import com.hclhostel.service.RoomService;
import com.hclhostel.service.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentService students;
    private final AllocationRepository allocations;
    private final RoomService rooms;

    public StudentController(StudentService students, AllocationRepository allocations, RoomService rooms) {
        this.students = students;
        this.allocations = allocations;
        this.rooms = rooms;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Student s = students.findByUserId(auth.getName());
        model.addAttribute("accountUsername", auth.getName());
        model.addAttribute("student", s);
        if (s != null) {
            var allocation = allocations.findByStudentId(s.getStudentId()).orElse(null);
            model.addAttribute("allocation", allocation);
            if (allocation != null) {
                model.addAttribute("allocatedRoom", rooms.findByRoomNo(allocation.getRoomNo()));
            }
        }
        return "student/dashboard";
    }
}
