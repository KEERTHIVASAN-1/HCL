package com.hclhostel.repository;

import com.hclhostel.entity.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoomRepository extends MongoRepository<Room, String> {
    Optional<Room> findByRoomNo(String roomNo);
    boolean existsByRoomNo(String roomNo);
}
