package com.krail.shop.service;

import com.krail.shop.common.Money;

import java.math.BigDecimal;

/** Impuestos por región (simples). */
public class TaxCalculator {
    public enum Region { PE, US, EU }

    public Money taxFor(Region r, Money base) {
        BigDecimal rate;
        switch (r) {
            case PE: rate = new BigDecimal("0.18"); break;
            case US: rate = new BigDecimal("0.07"); break;
            case EU: rate = new BigDecimal("0.20"); break;
            default: rate = BigDecimal.ZERO;
        }
        return new Money(base.asBig().multiply(rate));
    }
}
