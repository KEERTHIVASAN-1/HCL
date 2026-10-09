package com.hclhostel.payment;

public class UPIPayment extends Payment implements Refundable {
    private final String upiId;

    public UPIPayment(String txnId, double amount, String upiId) {
        super(txnId, amount);
        this.upiId = upiId;
    }

    @Override
    public boolean pay(double amt) {
        if (amt <= 0 || upiId == null) return false;
        this.amount = amt;
        return true;
    }

    @Override
    public boolean refund(double amount) {
        return amount > 0;
    }

    @Override
    public String summary() {
        return super.summary() + " upi=" + upiId;
    }
}
