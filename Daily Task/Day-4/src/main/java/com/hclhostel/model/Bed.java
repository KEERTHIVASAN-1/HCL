package com.hclhostel.model;

public class Bed {
    private String bedId;
    private boolean occupied;

    public Bed() {
        this("", false);
    }

    public Bed(String bedId, boolean occupied) {
        this.bedId = bedId;
        this.occupied = occupied;
    }

    public String getBedId() { return bedId; }
    public boolean isOccupied() { return occupied; }
}
