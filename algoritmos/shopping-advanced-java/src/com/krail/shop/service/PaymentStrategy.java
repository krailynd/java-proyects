package com.krail.shop.service;

import com.krail.shop.common.Money;

/** Estrategia de pago. */
public interface PaymentStrategy {
    /** Procesa el pago (simulado). */
    boolean pay(Money amount);
    String name();
}
