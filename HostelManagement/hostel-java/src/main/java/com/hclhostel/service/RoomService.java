package com.hclhostel.service;

import com.hclhostel.entity.Room;
import com.hclhostel.repository.AllocationRepository;
import com.hclhostel.repository.RoomRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {
    private final RoomRepository rooms;
    private final AllocationRepository allocations;

    public RoomService(RoomRepository rooms, AllocationRepository allocations) {
        this.rooms = rooms;
        this.allocations = allocations;
    }

    public List<Room> all() {
        return rooms.findAll();
    }

    public Room findByRoomNo(String roomNo) {
        if (roomNo == null || roomNo.isBlank()) {
            return null;
        }
        return rooms.findByRoomNo(roomNo.strip()).orElse(null);
    }

    public Room save(Room r) {
        if (r.getRoomNo() == null || r.getRoomNo().isBlank()) {
            throw new IllegalArgumentException("Room number is required");
        }
        if (r.getBlock() == null || r.getBlock().isBlank()) {
            throw new IllegalArgumentException("Block is required");
        }
        r.setRoomNo(r.getRoomNo().strip());
        r.setBlock(r.getBlock().strip());
        if (r.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be > 0");
        }
        if (rooms.existsByRoomNo(r.getRoomNo())) {
            throw new IllegalArgumentException("Room number already exists: " + r.getRoomNo());
        }
        try {
            return rooms.save(r);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("Duplicate room number", e);
        }
    }

    public Map<String, Object> occupancy(Room r) {
        long occupied = allocations.countByRoomNo(r.getRoomNo());
        Map<String, Object> m = new HashMap<>();
        m.put("room", r);
        m.put("occupied", occupied);
        m.put("available", r.getCapacity() - occupied);
        m.put("occupancyPercent", r.getCapacity() == 0 ? 0 : Math.round((occupied * 100.0) / r.getCapacity()));
        return m;
    }

    public List<Map<String, Object>> occupancySummaries() {
        return rooms.findAll().stream().map(this::occupancy).toList();
    }

    public List<Map<String, Object>> blockOccupancy() {
        Map<String, Map<String, Object>> blocks = new LinkedHashMap<>();
        for (Room room : rooms.findAll()) {
            String blockName = (room.getBlock() == null || room.getBlock().isBlank()) ? "Unassigned" : room.getBlock().strip();
            Map<String, Object> blockSummary = blocks.computeIfAbsent(blockName, key -> {
                Map<String, Object> summary = new HashMap<>();
                summary.put("block", key);
                summary.put("roomCount", 0L);
                summary.put("totalBeds", 0L);
                summary.put("occupiedBeds", 0L);
                summary.put("availableBeds", 0L);
                summary.put("occupancyPercent", 0L);
                return summary;
            });

            long roomCount = (long) blockSummary.get("roomCount");
            long totalBeds = (long) blockSummary.get("totalBeds");
            long occupiedBeds = (long) blockSummary.get("occupiedBeds");
            long roomOccupied = allocations.countByRoomNo(room.getRoomNo());

            blockSummary.put("roomCount", roomCount + 1);
            blockSummary.put("totalBeds", totalBeds + room.getCapacity());
            blockSummary.put("occupiedBeds", occupiedBeds + roomOccupied);
        }

        List<Map<String, Object>> summaries = new ArrayList<>();
        for (Map<String, Object> blockSummary : blocks.values()) {
            long totalBeds = (long) blockSummary.get("totalBeds");
            long occupiedBeds = (long) blockSummary.get("occupiedBeds");
            blockSummary.put("availableBeds", totalBeds - occupiedBeds);
            blockSummary.put("occupancyPercent", totalBeds == 0 ? 0 : Math.round((occupiedBeds * 100.0) / totalBeds));
            summaries.add(blockSummary);
        }
        return summaries;
    }

    public long totalBeds() {
        return rooms.findAll().stream().mapToLong(Room::getCapacity).sum();
    }

    public long occupiedBeds() {
        return allocations.count();
    }

    public long availableBeds() {
        return totalBeds() - occupiedBeds();
    }
}
