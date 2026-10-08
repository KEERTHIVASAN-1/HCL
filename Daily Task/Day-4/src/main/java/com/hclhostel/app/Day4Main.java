package com.hclhostel.app;

import com.hclhostel.model.Bed;
import com.hclhostel.model.Room;
import com.hclhostel.model.Student;
import com.hclhostel.service.BankAccount;

public class Day4Main {
    public static void main(String[] args) {
        BankAccount a = new BankAccount(100, "Ravi", 2000);
        BankAccount b = new BankAccount(101, "Amit", 3000);
        BankAccount c = new BankAccount(102, "Priya", 5000);

        a.deposit(1000);
        a.withdraw(500);

        b.deposit(0);
        b.deposit(500);
        b.withdraw(1000);

        c.deposit(1000);
        c.withdraw(800);

        System.out.println("Accounts created: " + BankAccount.getAccountCount());
        System.out.println("a [" + a.getAccountNumber() + "] " + a.getHolder() + " bal=" + a.getBalance());
        System.out.println("b [" + b.getAccountNumber() + "] " + b.getHolder() + " bal=" + b.getBalance());
        System.out.println("c [" + c.getAccountNumber() + "] " + c.getHolder() + " bal=" + c.getBalance());
        System.out.println("a.equals(a)? " + a.equals(a));
        System.out.println("a.equals(b)? " + a.equals(b));

        Student s = new Student(1, "Ravi");
        Room r = new Room("A-101", 3);
        Bed bd = new Bed("A-101-B1", false);
        System.out.println("Student: " + s.getId() + "/" + s.getName());
        System.out.println("Room: " + r.getRoomNo() + "/" + r.getCapacity());
        System.out.println("Bed: " + bd.getBedId() + "/occupied=" + bd.isOccupied());
    }
}
