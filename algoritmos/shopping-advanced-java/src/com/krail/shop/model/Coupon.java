package com.krail.shop.model;

import com.krail.shop.common.Money;
import java.math.BigDecimal;

/** Modelo de cupón (porcentaje o monto fijo). */
public class Coupon {
    public enum Type { PERCENTAGE, FIXED }
    private final String code;
    private final Type type;
    private final BigDecimal percent; // 0..1 para PERCENTAGE
    private final Money fixed;        // para FIXED
    private final boolean singleUse;

    private boolean used = false;

    public Coupon(String code, Type type, BigDecimal percent, Money fixed, boolean singleUse) {
        this.code = code; this.type = type; this.percent = percent; this.fixed = fixed; this.singleUse = singleUse;
    }

    public String getCode() { return code; }
    public Type getType() { return type; }
    public boolean isUsed() { return used; }
    public boolean isSingleUse() { return singleUse; }

    public Money apply(Money base) {
        if (used && singleUse) return Money.ZERO;
        Money disc = Money.ZERO;
        if (type == Type.PERCENTAGE) {
            disc = base.multiply(percent);
        } else if (type == Type.FIXED) {
            disc = fixed;
        }
        // No permitir descuento negativo final
        if (disc.compareTo(base) > 0) disc = base;
        if (singleUse) used = true;
        return disc;
    }
}
