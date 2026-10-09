package com.hclhostel.strategy;

import com.hclhostel.model.Room;
import java.util.List;

public interface AllocationStrategy {
    Room chooseRoom(List<Room> rooms);
}
