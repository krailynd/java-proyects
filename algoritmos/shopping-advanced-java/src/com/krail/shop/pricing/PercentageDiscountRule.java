package com.krail.shop.pricing;

import com.krail.shop.common.Money;
import com.krail.shop.model.CartItem;

import java.math.BigDecimal;

/** Descuento porcentual global sobre el total del carrito. */
public class PercentageDiscountRule implements com.krail.shop.pricing.PricingRule {
    private final BigDecimal percent; // 0..1

    public PercentageDiscountRule(BigDecimal percent) {
        this.percent = percent;
    }

    @Override
    public Money discount(PricingContext ctx, CartItem item) {
        // Solo una vez sobre el total del carrito (item==null)
        if (item != null) return Money.ZERO;
        // Calcular total actual
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : ctx.getItems()) {
            total = total.add(ci.lineSubtotal().asBig());
        }
        return new Money(total.multiply(percent));
    }
}
