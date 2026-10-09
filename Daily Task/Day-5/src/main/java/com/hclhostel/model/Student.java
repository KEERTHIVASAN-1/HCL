package com.hclhostel.model;

public class Student extends BaseEntity {
    private final String name;

    public Student(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public String toString() {
        return "Student#" + getId() + " " + name;
    }
}
