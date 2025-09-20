package com.krail.shop.service;

import com.krail.shop.common.Money;
import com.krail.shop.model.CartItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Generador de recibos en texto. */
public class ReceiptGenerator {
    private final String currency;

    public ReceiptGenerator(String currencySymbol) {
        this.currency = currencySymbol;
    }

    public String generate(List<CartItem> items, CheckoutService.Summary s, String region, String paymentMethod, int earnedPoints, int remainingPoints) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RECIBO ===\n");
        sb.append("Fecha: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n\n");
        sb.append("Items:\n");
        for (CartItem ci : items) {
            sb.append(String.format("- %s x%d  %s%s\n", ci.getProduct().getName(), ci.getQuantity(), currency, ci.lineSubtotal()));
        }
        sb.append("\n");
        line(sb, "Subtotal", s.subtotal);
        line(sb, "Descuentos reglas", s.discounts);
        line(sb, "Descuento cupón", s.couponDiscount);
        line(sb, "Base imponible ("+region+")", s.taxableBase);
        line(sb, "Impuesto", s.tax);
        line(sb, "TOTAL", s.total);
        sb.append("\nPago: ").append(paymentMethod).append("\n");
        sb.append("Puntos ganados: ").append(earnedPoints).append(" | Puntos disponibles: ").append(remainingPoints).append("\n");
        sb.append("Gracias por su compra.\n");
        return sb.toString();
    }

    private void line(StringBuilder sb, String label, Money v) {
        sb.append(String.format("%-22s %s%s\n", label + ":", currency, v));
    }
}
