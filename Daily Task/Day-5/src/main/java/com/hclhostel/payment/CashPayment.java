package com.hclhostel.payment;

public class CashPayment extends Payment {
    public CashPayment(String txnId, double amount) {
        super(txnId, amount);
    }

    @Override
    public boolean pay(double amt) {
        if (amt <= 0) return false;
        this.amount = amt;
        return true;
    }

    @Override
    public String summary() {
        return super.summary() + " cash";
    }
}
