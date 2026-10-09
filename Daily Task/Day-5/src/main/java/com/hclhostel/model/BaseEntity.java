package com.hclhostel.model;

import java.time.LocalDateTime;

public abstract class BaseEntity {
    protected int id;
    protected LocalDateTime createdAt;

    public BaseEntity(int id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
