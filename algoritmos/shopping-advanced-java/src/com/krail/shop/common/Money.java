package com.krail.shop.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Representa una cantidad monetaria con operaciones seguras.
 * Invariante: escala 2 decimales, redondeo HALF_UP.
 */
public final class Money {
    private final BigDecimal value;

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(BigDecimal value) {
        if (value == null) throw new IllegalArgumentException("value null");
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money of(double v) {
        return new Money(BigDecimal.valueOf(v));
    }

    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        return new Money(this.value.subtract(other.value));
    }

    public Money multiply(int qty) {
        return new Money(this.value.multiply(BigDecimal.valueOf(qty)));
    }

    public Money multiply(BigDecimal factor) {
        return new Money(this.value.multiply(factor));
    }

    public int compareTo(Money other) {
        return this.value.compareTo(other.value);
    }

    public BigDecimal asBig() {
        return value;
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}
