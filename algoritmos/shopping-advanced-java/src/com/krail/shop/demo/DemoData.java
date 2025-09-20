package com.krail.shop.demo;

import com.krail.shop.pricing.*;
import com.krail.shop.repo.InventoryRepository;
import com.krail.shop.service.CartService;

import java.math.BigDecimal;

/** Carga reglas de ejemplo al carrito. */
public class DemoData {
    public static void loadRules(CartService cart) {
        cart.addRule(new BuyXGetYFreeRule("SKU-MOUSE", 2, 1));                 // 2x1 en mouse (3x2)
        cart.addRule(new BulkPriceRule("SKU-TECLADO", 5, new BigDecimal("110.00"))); // teclado a 110 si llevas 5+
        cart.addRule(new PercentageDiscountRule(new BigDecimal("0.05")));      // -5% global
    }
}
