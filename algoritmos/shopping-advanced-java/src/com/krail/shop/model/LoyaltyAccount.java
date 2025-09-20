package com.krail.shop.model;

/** Cuenta de fidelidad simple basada en puntos. */
public class LoyaltyAccount {
    private final String customerId;
    private int points;

    public LoyaltyAccount(String customerId) {
        this.customerId = customerId; this.points = 0;
    }

    public String getCustomerId() { return customerId; }
    public int getPoints() { return points; }

    public void addPoints(int p) { if (p>0) points += p; }

    public boolean redeem(int p) {
        if (p <= 0) return false;
        if (p > points) return false;
        points -= p; return true;
    }
}
