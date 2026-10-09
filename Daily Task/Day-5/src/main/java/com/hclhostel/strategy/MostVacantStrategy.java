package com.hclhostel.strategy;

import com.hclhostel.model.Room;
import java.util.Comparator;
import java.util.List;

public class MostVacantStrategy implements AllocationStrategy {
    @Override
    public Room chooseRoom(List<Room> rooms) {
        return rooms.stream()
                .filter(r -> r.getFreeBeds() > 0)
                .max(Comparator.comparingInt(Room::getFreeBeds))
                .orElse(null);
    }
}
