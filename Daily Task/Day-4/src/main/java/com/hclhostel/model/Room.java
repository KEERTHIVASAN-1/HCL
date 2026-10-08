package com.hclhostel.model;

public class Room {
    private String roomNo;
    private int capacity;

    public Room() {
        this("", 0);
    }

    public Room(String roomNo, int capacity) {
        this.roomNo = roomNo;
        this.capacity = capacity;
    }

    public String getRoomNo() { return roomNo; }
    public int getCapacity() { return capacity; }
}
