package com.krail.shop.pricing;

import com.krail.shop.common.Money;
import com.krail.shop.model.CartItem;

import java.math.BigDecimal;

/** Precio unitario reducido al superar un umbral de cantidad. */
public class BulkPriceRule implements PricingRule {
    private final String sku;
    private final int minQty;
    private final BigDecimal unitPrice; // nuevo precio unitario

    public BulkPriceRule(String sku, int minQty, BigDecimal unitPrice) {
        this.sku = sku; this.minQty = minQty; this.unitPrice = unitPrice;
    }

    @Override
    public Money discount(PricingContext ctx, CartItem item) {
        if (item == null) return Money.ZERO;
        if (!item.getProduct().getSku().equalsIgnoreCase(sku)) return Money.ZERO;
        if (item.getQuantity() < minQty) return Money.ZERO;
        // descuento = (precioNormal - precioBulk) * cantidad
        BigDecimal normal = item.getProduct().getPrice().asBig();
        BigDecimal bulk = unitPrice;
        if (bulk.compareTo(normal) >= 0) return Money.ZERO;
        BigDecimal diff = normal.subtract(bulk);
        return new Money(diff.multiply(BigDecimal.valueOf(item.getQuantity())));
    }
}
