package com.hclhostel.model;

public class BadEmployee {
    private static int nextHashCode = 1;
    private final String id;
    private final int incorrectHashCode = nextHashCode++;

    public BadEmployee(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof BadEmployee employee)) return false;
        return id.equals(employee.id);
    }

    @Override
    public int hashCode() {
        return incorrectHashCode;
    }
}
