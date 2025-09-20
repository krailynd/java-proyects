package com.krail.shop.service;

import com.krail.shop.common.Money;

/** Pago en efectivo (simulado). */
public class CashPayment implements PaymentStrategy {
    @Override public boolean pay(Money amount) { return true; }
    @Override public String name() { return "Efectivo"; }
}
