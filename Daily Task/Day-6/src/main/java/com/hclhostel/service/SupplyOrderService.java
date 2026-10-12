package com.hclhostel.service;

import com.hclhostel.exception.InsufficientStockException;
import com.hclhostel.exception.InvalidQuantityException;

import java.util.Map;

public class SupplyOrderService {
    private final Map<String, Integer> stock = Map.of(
            "Bedsheet", 10,
            "Soap", 5,
            "Bucket", 2
    );

    public int processOrder(String item, int qty) throws InsufficientStockException {
        if (qty <= 0) {
            throw new InvalidQuantityException("Quantity must be > 0, got " + qty);
        }
        int available = stock.getOrDefault(item, 0);
        if (qty > available) {
            throw new InsufficientStockException("Only " + available + " " + item + " in stock, requested " + qty);
        }
        return available - qty;
    }
}
