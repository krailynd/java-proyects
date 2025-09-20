package com.krail.shop.service;

import com.krail.shop.common.Money;
import com.krail.shop.model.CartItem;
import com.krail.shop.model.Product;
import com.krail.shop.pricing.PricingContext;
import com.krail.shop.pricing.PricingRule;

import java.util.ArrayList;
import java.util.List;

/** Gestión del carrito (agregar, quitar, totalizar con reglas). */
public class CartService {
    private final List<CartItem> items = new ArrayList<>();
    private final List<PricingRule> rules = new ArrayList<>();

    public void addRule(PricingRule rule){ rules.add(rule); }
    public List<CartItem> getItems() { return items; }

    public void add(Product p, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty<=0");
        // combinar líneas del mismo SKU
        for (CartItem ci : items) {
            if (ci.getProduct().getSku().equalsIgnoreCase(p.getSku())) {
                ci.setQuantity(ci.getQuantity() + qty);
                return;
            }
        }
        items.add(new CartItem(p, qty));
    }

    public Money subtotal() {
        Money s = Money.ZERO;
        for (CartItem ci : items) s = s.add(ci.lineSubtotal());
        return s;
    }

    public Money totalDiscount() {
        PricingContext ctx = new PricingContext(items);
        Money d = Money.ZERO;
        // Descuentos por ítem
        for (CartItem ci : items) {
            for (PricingRule r : rules) d = d.add(r.discount(ctx, ci));
        }
        // Descuentos globales (item=null)
        for (PricingRule r : rules) d = d.add(r.discount(ctx, null));
        // Limitar a subtotal
        if (d.compareTo(subtotal()) > 0) return subtotal();
        return d;
    }
}
