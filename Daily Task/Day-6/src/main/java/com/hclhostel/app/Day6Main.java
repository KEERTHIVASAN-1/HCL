package com.hclhostel.app;

import com.hclhostel.exception.InsufficientStockException;
import com.hclhostel.exception.InvalidQuantityException;
import com.hclhostel.service.SupplyOrderService;

import java.time.LocalTime;
import java.util.Scanner;

public class Day6Main {
    public static void main(String[] args) {
        SupplyOrderService service = new SupplyOrderService();
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print("\nItem (or 'quit'): ");
            String item = sc.nextLine().trim();
            if (item.equalsIgnoreCase("quit")) {
                running = false;
                break;
            }

            int qty;
            try {
                qty = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number: " + e.getMessage());
                continue;
            }

            try {
                int remaining = service.processOrder(item, qty);
                System.out.println("Order OK. Remaining " + item + ": " + remaining);
            } catch (InvalidQuantityException | NumberFormatException e) {
                System.out.println("Bad input: " + e.getMessage());
            } catch (InsufficientStockException e) {
                throw new RuntimeException("Order failed for " + item, e);
            } finally {
                System.out.println("[AUDIT " + LocalTime.now().withNano(0) + "] attempted item=" + item + " qty=" + qty);
            }
        }
        sc.close();
        System.out.println("Bye.");
    }
}
