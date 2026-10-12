package com.hclhostel.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "allocations")
@CompoundIndex(name = "room_bed_unique", def = "{'roomNo': 1, 'bedNo': 1}", unique = true)
public class Allocation {
    @Id
    private String id;
    @Indexed(unique = true)
    private String studentId;
    private String roomNo;
    private int bedNo;
    private String status = "ACTIVE";

    public Allocation() {}
    public Allocation(String studentId, String roomNo, int bedNo) {
        this.studentId = studentId;
        this.roomNo = roomNo;
        this.bedNo = bedNo;
    }

    public String getId() { return id; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public int getBedNo() { return bedNo; }
    public void setBedNo(int bedNo) { this.bedNo = bedNo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
