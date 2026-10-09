package com.hclhostel.payment;

public class CardPayment extends Payment implements Refundable {
    private final String last4;

    public CardPayment(String txnId, double amount, String last4) {
        super(txnId, amount);
        this.last4 = last4;
    }

    @Override
    public boolean pay(double amt) {
        if (amt <= 0) return false;
        this.amount = amt;
        return true;
    }

    public boolean pay(double amt, String cvv) {
        if (cvv == null || cvv.length() != 3) return false;
        return pay(amt);
    }

    @Override
    public boolean refund(double amount) {
        return amount > 0;
    }

    @Override
    public String summary() {
        return super.summary() + " card=****" + last4;
    }
}
