package com.hclhostel.payment;

public abstract class Payment {
    protected String txnId;
    protected double amount;

    public Payment(String txnId, double amount) {
        this.txnId = txnId;
        this.amount = amount;
    }

    public boolean pay() {
        return pay(this.amount);
    }

    public abstract boolean pay(double amt);

    public String summary() {
        return txnId + " amount=" + amount;
    }
}
