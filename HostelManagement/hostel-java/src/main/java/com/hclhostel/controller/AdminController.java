package com.hclhostel.controller;

import com.hclhostel.entity.Student;
import com.hclhostel.service.AllocationService;
import com.hclhostel.service.RoomService;
import com.hclhostel.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final StudentService students;
    private final RoomService rooms;
    private final AllocationService allocations;

    public AdminController(StudentService students, RoomService rooms, AllocationService allocations) {
        this.students = students;
        this.rooms = rooms;
        this.allocations = allocations;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        var studentList = students.all();
        var roomSummaries = rooms.occupancySummaries();
        var allocationList = allocations.all();
        long totalBeds = rooms.totalBeds();
        long occupiedBeds = rooms.occupiedBeds();

        model.addAttribute("studentCount", studentList.size());
        model.addAttribute("roomCount", roomSummaries.size());
        model.addAttribute("totalBeds", totalBeds);
        model.addAttribute("occupiedBeds", occupiedBeds);
        model.addAttribute("availableBeds", rooms.availableBeds());
        model.addAttribute("utilizationRate", totalBeds == 0 ? 0 : Math.round((occupiedBeds * 100.0) / totalBeds));
        model.addAttribute("students", studentList);
        model.addAttribute("rooms", roomSummaries);
        model.addAttribute("allocations", allocationList);
        model.addAttribute("blockStats", rooms.blockOccupancy());
        return "admin/dashboard";
    }

    @GetMapping("/students")
    public String students(Model model) {
        model.addAttribute("students", students.all());
        return "admin/students";
    }

    @GetMapping("/students/new")
    public String newStudent(Model model) {
        model.addAttribute("student", new Student());
        return "admin/student-form";
    }

    @PostMapping("/students")
    public String addStudent(@ModelAttribute Student student, Model model) {
        try {
            students.save(student);
            return "redirect:/admin/students";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("student", student);
            return "admin/student-form";
        }
    }

    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("rooms", rooms.occupancySummaries());
        return "admin/rooms";
    }

    @GetMapping("/rooms/new")
    public String newRoom(Model model) {
        model.addAttribute("room", new com.hclhostel.entity.Room());
        return "admin/room-form";
    }

    @PostMapping("/rooms")
    public String addRoom(@ModelAttribute com.hclhostel.entity.Room room, Model model) {
        try {
            rooms.save(room);
            return "redirect:/admin/rooms";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("room", room);
            return "admin/room-form";
        }
    }

    @GetMapping("/allocations")
    public String allocations(Model model) {
        model.addAttribute("allocations", allocations.all());
        return "admin/allocations";
    }

    @GetMapping("/allocations/new")
    public String newAllocation(@RequestParam(required = false) String roomNo, Model model) {
        var roomList = rooms.all();
        String selectedRoomNo = roomNo;
        if ((selectedRoomNo == null || selectedRoomNo.isBlank()) && !roomList.isEmpty()) {
            selectedRoomNo = roomList.getFirst().getRoomNo();
        }

        model.addAttribute("students", students.all());
        model.addAttribute("rooms", roomList);
        model.addAttribute("selectedRoomNo", selectedRoomNo);
        model.addAttribute("availableBeds", allocations.availableBeds(selectedRoomNo));
        return "admin/allocation-form";
    }

    @PostMapping("/allocations")
    public String addAllocation(@RequestParam String studentId,
                                @RequestParam String roomNo,
                                @RequestParam int bedNo,
                                Model model) {
        try {
            allocations.allocate(studentId, roomNo, bedNo);
            return "redirect:/admin/allocations";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("students", students.all());
            model.addAttribute("rooms", rooms.all());
            model.addAttribute("selectedRoomNo", roomNo);
            model.addAttribute("availableBeds", allocations.availableBeds(roomNo));
            return "admin/allocation-form";
        }
    }
}
