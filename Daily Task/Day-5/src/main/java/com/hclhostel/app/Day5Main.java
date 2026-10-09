package com.hclhostel.app;

import com.hclhostel.model.Admin;
import com.hclhostel.model.Bed;
import com.hclhostel.model.Room;
import com.hclhostel.model.Student;
import com.hclhostel.model.User;
import com.hclhostel.model.Warden;
import com.hclhostel.payment.CardPayment;
import com.hclhostel.payment.CashPayment;
import com.hclhostel.payment.Payment;
import com.hclhostel.payment.Refundable;
import com.hclhostel.payment.UPIPayment;
import com.hclhostel.strategy.AllocationStrategy;
import com.hclhostel.strategy.LowestFloorStrategy;
import com.hclhostel.strategy.MostVacantStrategy;

import java.util.Arrays;
import java.util.List;

public class Day5Main {
    public static void main(String[] args) {
        Payment[] payments = {
                new CardPayment("T1", 500, "1234"),
                new UPIPayment("T2", 300, "ravi@upi"),
                new CashPayment("T3", 200)
        };
        System.out.println("-- Runtime polymorphism (pay + summary overrides) --");
        for (Payment p : payments) {
            p.pay();
            System.out.println(p.summary());
        }

        System.out.println("-- Overload pay(double) + pay(double, String cvv) --");
        CardPayment cp = new CardPayment("T4", 0, "9999");
        System.out.println("pay(700)      -> " + cp.pay(700));
        System.out.println("pay(700,12)   -> " + cp.pay(700, "12"));
        System.out.println("pay(700,123)  -> " + cp.pay(700, "123"));

        System.out.println("-- Refundable interface on Card/UPI, Cash NOT Refundable --");
        for (Payment p : payments) {
            if (p instanceof Refundable r) {
                System.out.println(p.getClass().getSimpleName() + " refund(100) -> " + r.refund(100));
            } else {
                System.out.println(p.getClass().getSimpleName() + " not refundable");
            }
        }

        System.out.println("-- Role hierarchy (toString override + role override with super ctor) --");
        List<User> users = Arrays.asList(new User("alice"), new Warden("priya"), new Admin("admin1"));
        for (User u : users) System.out.println(u);

        System.out.println("-- BaseEntity inheritance (all extend BaseEntity, use super(id)) --");
        Student s = new Student(1, "Ravi");
        Room r = new Room(10, "A-101", 1, 2);
        Bed b = new Bed(100, "A-101-B1");
        System.out.println(s + " createdAt=" + s.getCreatedAt().toLocalTime().withNano(0));
        System.out.println("Room  #" + r.getId() + " " + r.getRoomNo() + " floor=" + r.getFloor());
        System.out.println("Bed   #" + b.getId() + " " + b.getBedId());

        System.out.println("-- Allocation Strategy (interface + 2 impls) runtime polymorphism --");
        List<Room> rooms = Arrays.asList(
                new Room(1, "A-101", 1, 1),
                new Room(2, "A-201", 2, 3),
                new Room(3, "B-301", 3, 4)
        );
        AllocationStrategy s1 = new LowestFloorStrategy();
        AllocationStrategy s2 = new MostVacantStrategy();
        System.out.println("LowestFloor   -> " + s1.chooseRoom(rooms).getRoomNo());
        System.out.println("MostVacant    -> " + s2.chooseRoom(rooms).getRoomNo());
    }
}
