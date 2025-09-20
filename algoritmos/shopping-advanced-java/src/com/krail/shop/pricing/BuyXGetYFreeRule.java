package com.krail.shop.pricing;

import com.krail.shop.common.Money;
import com.krail.shop.model.CartItem;

/** Regla Buy X Get Y Free por SKU. */
public class BuyXGetYFreeRule implements PricingRule {
    private final String sku;
    private final int x;
    private final int y;

    public BuyXGetYFreeRule(String sku, int x, int y) {
        this.sku = sku; this.x = x; this.y = y;
    }

    @Override
    public Money discount(PricingContext ctx, CartItem item) {
        if (item == null) return Money.ZERO;
        if (!item.getProduct().getSku().equalsIgnoreCase(sku)) return Money.ZERO;
        int q = item.getQuantity();
        int bundle = x + y;
        int freeUnits = (q / bundle) * y; // por cada bundle, Y gratis
        return item.getProduct().getPrice().multiply(freeUnits);
    }
}
