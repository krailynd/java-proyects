package com.krail.shop.pricing;

import com.krail.shop.common.Money;
import com.krail.shop.model.CartItem;

/**
 * Regla de pricing que devuelve descuento a aplicar sobre un ítem o carrito.
 */
public interface PricingRule {
    /**
     * @param ctx contexto con datos del carrito
     * @param item item a evaluar (puede ser null si la regla es global)
     * @return descuento (>=0)
     */
    Money discount(PricingContext ctx, CartItem item);
}
