package com.hclhostel.desk;

public class Bed {
    private final String block;
    private final int roomNo;
    private final int bedNo;
    private boolean occupied;

    public Bed(String block, int roomNo, int bedNo, boolean occupied) {
        this.block = block;
        this.roomNo = roomNo;
        this.bedNo = bedNo;
        this.occupied = occupied;
    }

    public String getBlock() { return block; }
    public int getRoomNo() { return roomNo; }
    public int getBedNo() { return bedNo; }
    public boolean isOccupied() { return occupied; }

    public String label() {
        return block + "-" + roomNo + "-Bed" + bedNo;
    }
}
