package com.hclhostel.controller;

import com.hclhostel.service.AllocationService;
import com.hclhostel.service.RoomService;
import com.hclhostel.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/warden")
public class WardenController {
    private final StudentService students;
    private final RoomService rooms;
    private final AllocationService allocations;

    public WardenController(StudentService students, RoomService rooms, AllocationService allocations) {
        this.students = students;
        this.rooms = rooms;
        this.allocations = allocations;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        var studentList = students.all();
        var roomSummaries = rooms.occupancySummaries();
        long totalBeds = rooms.totalBeds();
        long occupiedBeds = rooms.occupiedBeds();

        model.addAttribute("students", studentList);
        model.addAttribute("studentCount", studentList.size());
        model.addAttribute("rooms", roomSummaries);
        model.addAttribute("roomCount", roomSummaries.size());
        model.addAttribute("allocations", allocations.all());
        model.addAttribute("totalBeds", totalBeds);
        model.addAttribute("occupiedBeds", occupiedBeds);
        model.addAttribute("availableBeds", rooms.availableBeds());
        model.addAttribute("utilizationRate", totalBeds == 0 ? 0 : Math.round((occupiedBeds * 100.0) / totalBeds));
        model.addAttribute("blockStats", rooms.blockOccupancy());
        return "warden/dashboard";
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
        return "warden/allocation-form";
    }

    @PostMapping("/allocations")
    public String addAllocation(@RequestParam String studentId,
                                @RequestParam String roomNo,
                                @RequestParam int bedNo,
                                Model model) {
        try {
            allocations.allocate(studentId, roomNo, bedNo);
            return "redirect:/warden/dashboard";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("students", students.all());
            model.addAttribute("rooms", rooms.all());
            model.addAttribute("selectedRoomNo", roomNo);
            model.addAttribute("availableBeds", allocations.availableBeds(roomNo));
            return "warden/allocation-form";
        }
    }
}
