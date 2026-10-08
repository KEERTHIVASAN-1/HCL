package com.hclhostel.desk;

public class Student {
    private final String name;
    private final boolean present;

    public Student(String name, boolean present) {
        this.name = name;
        this.present = present;
    }

    public String getName() { return name; }
    public boolean isPresent() { return present; }
}
