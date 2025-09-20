package com.krail.shop.service;

import com.krail.shop.model.LoyaltyAccount;

/** Servicio simple de fidelidad. */
public class LoyaltyService {
    private final LoyaltyAccount account;

    public LoyaltyService(String customerId) {
        this.account = new LoyaltyAccount(customerId);
    }

    public LoyaltyAccount getAccount() { return account; }

    /** 1 punto por cada 10 unidades monetarias gastadas (parte entera). */
    public int computeEarnedPoints(double paidAmount) {
        return (int) Math.floor(paidAmount / 10.0);
    }
}
