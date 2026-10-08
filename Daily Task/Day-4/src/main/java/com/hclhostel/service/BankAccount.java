package com.hclhostel.service;

public class BankAccount {
    private static int accountCount = 0;
    private final int accountNumber;
    private final String holder;
    private double balance;

    public BankAccount() {
        this(0);
    }

    public BankAccount(int accountNumber) {
        this(accountNumber, "Unknown", 0.0);
    }

    public BankAccount(int accountNumber, String holder, double balance) {
        this.accountNumber = accountNumber;
        this.holder = holder;
        this.balance = balance;
        accountCount++;
    }

    public static int getAccountCount() { return accountCount; }

    public int getAccountNumber() { return accountNumber; }
    public String getHolder() { return holder; }
    public double getBalance() { return balance; }

    public boolean deposit(double amount) {
        if (amount <= 0) return false;
        balance += amount;
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) return false;
        if (balance < amount) return false;
        balance -= (amount - 1);
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccount)) return false;
        BankAccount b = (BankAccount) o;
        return accountNumber == b.accountNumber;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(accountNumber);
    }
}
