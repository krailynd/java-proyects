package com.krail.shop.service;

import com.krail.shop.common.Money;
import com.krail.shop.model.CartItem;
import com.krail.shop.model.Coupon;
import com.krail.shop.model.Product;
import com.krail.shop.pricing.PricingContext;
import com.krail.shop.repo.InventoryRepository;

import java.util.List;

/** Orquestación de checkout: valida stock, aplica cupones, impuestos, puntos, y ejecuta pago. */
public class CheckoutService {

    public static class Summary {
        public Money subtotal;
        public Money discounts;
        public Money couponDiscount;
        public Money taxableBase;
        public Money tax;
        public Money total;
    }

    private final InventoryRepository inventory;
    private final TaxCalculator taxCalculator;

    public CheckoutService(InventoryRepository inventory, TaxCalculator taxCalculator) {
        this.inventory = inventory; this.taxCalculator = taxCalculator;
    }

    public Summary compute(List<CartItem> items, Money discounts, Coupon coupon, TaxCalculator.Region region, int loyaltyRedeemPoints) {
        Summary s = new Summary();
        // Subtotal
        Money subtotal = Money.ZERO;
        for (CartItem ci : items) subtotal = subtotal.add(ci.lineSubtotal());

        // Descuentos de reglas
        s.discounts = discounts;

        // Cupón
        s.couponDiscount = Money.ZERO;
        if (coupon != null) {
            s.couponDiscount = coupon.apply(subtotal.subtract(discounts));
        }

        // Redención de puntos (100 pts = S/10)
        Money loyaltyDiscount = Money.of((loyaltyRedeemPoints/100)*10.0);

        // Base imponible
        s.subtotal = subtotal;
        s.taxableBase = subtotal.subtract(discounts).subtract(s.couponDiscount).subtract(loyaltyDiscount);
        if (s.taxableBase.compareTo(Money.ZERO) < 0) s.taxableBase = Money.ZERO;

        // Impuesto
        s.tax = taxCalculator.taxFor(region, s.taxableBase);
        // Total
        s.total = s.taxableBase.add(s.tax);

        return s;
    }

    /** Disminuye el stock tras pago exitoso. */
    public void commitStock(List<CartItem> items) {
        for (CartItem ci : items) {
            Product p = inventory.findBySku(ci.getProduct().getSku()).orElseThrow();
            p.decreaseStock(ci.getQuantity());
        }
    }
}
