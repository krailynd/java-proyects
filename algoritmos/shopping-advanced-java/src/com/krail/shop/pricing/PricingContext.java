package com.krail.shop.pricing;

import com.krail.shop.model.CartItem;

import java.util.List;

/** Contexto de pricing: items, totales previos, etc. */
public class PricingContext {
    private final List<CartItem> items;

    public PricingContext(List<CartItem> items) {
        this.items = items;
    }

    public List<CartItem> getItems() { return items; }
}
