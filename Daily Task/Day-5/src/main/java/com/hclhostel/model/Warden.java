package com.hclhostel.model;

public class Warden extends User {
    public Warden(String username) {
        super(username);
    }

    @Override
    public String role() { return "WARDEN"; }
}
