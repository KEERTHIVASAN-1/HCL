package com.hclhostel.model;

public class Bed extends BaseEntity {
    private final String bedId;

    public Bed(int id, String bedId) {
        super(id);
        this.bedId = bedId;
    }

    public String getBedId() { return bedId; }
}
