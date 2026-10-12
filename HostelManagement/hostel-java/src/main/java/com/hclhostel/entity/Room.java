package com.hclhostel.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rooms")
public class Room {
    @Id
    private String id;
    @Indexed(unique = true)
    private String roomNo;
    private String block;
    private int capacity;

    public Room() {}
    public Room(String roomNo, String block, int capacity) {
        this.roomNo = roomNo;
        this.block = block;
        this.capacity = capacity;
    }

    public String getId() { return id; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public String getBlock() { return block; }
    public void setBlock(String block) { this.block = block; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}
