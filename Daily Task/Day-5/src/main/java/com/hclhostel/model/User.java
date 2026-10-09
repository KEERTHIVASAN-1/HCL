package com.hclhostel.model;

public class User {
    protected final String username;

    public User(String username) {
        this.username = username;
    }

    public String role() { return "USER"; }

    @Override
    public String toString() {
        return "[" + role() + "] " + username;
    }
}
