package com.hclhostel.model;

public class Room extends BaseEntity {
    private final String roomNo;
    private final int floor;
    private int freeBeds;

    public Room(int id, String roomNo, int floor, int freeBeds) {
        super(id);
        this.roomNo = roomNo;
        this.floor = floor;
        this.freeBeds = freeBeds;
    }

    public String getRoomNo() { return roomNo; }
    public int getFloor() { return floor; }
    public int getFreeBeds() { return freeBeds; }
    public void allocateOne() { if (freeBeds > 0) freeBeds--; }
}
