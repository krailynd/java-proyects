package com.krail.shop;

import com.krail.shop.common.Money;
import com.krail.shop.common.Validation;
import com.krail.shop.model.CartItem;
import com.krail.shop.model.Coupon;
import com.krail.shop.repo.CouponRepository;
import com.krail.shop.repo.InventoryRepository;
import com.krail.shop.service.*;
import com.krail.shop.demo.DemoData;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GUI mínima con JOptionPane (sin Scanner) que orquesta el flujo:
 * - Ver catálogo
 * - Agregar al carrito
 * - Aplicar cupón
 * - Elegir región/impuesto
 * - Redimir puntos
 * - Pagar (efectivo/tarjeta)
 * - Mostrar recibo
 *
 * Lógica separada en servicios/repositorios.
 */
public class App {
    public static void main(String[] args) {
        // Dependencias
        InventoryRepository inventory = new InventoryRepository();
        CouponRepository coupons = new CouponRepository();
        CartService cart = new CartService();
        DemoData.loadRules(cart);
        TaxCalculator tax = new TaxCalculator();
        CheckoutService checkout = new CheckoutService(inventory, tax);
        LoyaltyService loyalty = new LoyaltyService("cliente-001");
        ReceiptGenerator receiptGen = new ReceiptGenerator("S/ ");

        JOptionPane.showMessageDialog(null,
                "Bienvenido al Sistema de Compras Avanzado\n" +
                "- Ver catálogo y agregue productos.\n" +
                "- Use cupones (WELCOME10, FREESHIP).\n" +
                "- Impuestos por región (PE/US/EU).\n" +
                "- Puntos de fidelidad y pago simulado.", "Tienda", JOptionPane.INFORMATION_MESSAGE);

        Coupon selectedCoupon = null;

        mainLoop:
        while (true) {
            String[] options = {"Catálogo", "Agregar al carrito", "Ver carrito", "Cupón", "Checkout", "Salir"};
            int op = JOptionPane.showOptionDialog(null, "¿Qué desea hacer?", "Menú",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (op == -1 || op == 5) break; // Salir/cerrar

            switch (op) {
                case 0: // catálogo
                    String catalog = inventory.findAll().stream()
                            .map(p -> String.format("%s | %s | S/%s | stock:%d", p.getSku(), p.getName(), p.getPrice(), p.getStock()))
                            .collect(Collectors.joining("\n"));
                    JOptionPane.showMessageDialog(null, catalog.isEmpty() ? "Sin productos" : catalog, "Catálogo", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case 1: { // agregar
                    String sku = Validation.askNonEmpty("Ingrese SKU a agregar:", "Agregar");
                    if (sku == null) break;
                    var opt = inventory.findBySku(sku);
                    if (opt.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No existe SKU.", "Agregar", JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                    Integer qty = Validation.askInt("Cantidad:", "Agregar");
                    if (qty == null) break;
                    if (qty == 0) { JOptionPane.showMessageDialog(null, "Cantidad debe ser > 0", "Agregar", JOptionPane.WARNING_MESSAGE); break; }
                    if (qty > opt.get().getStock()) {
                        JOptionPane.showMessageDialog(null, "Stock insuficiente.", "Agregar", JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                    cart.add(opt.get(), qty);
                    JOptionPane.showMessageDialog(null, "Agregado.", "Agregar", JOptionPane.INFORMATION_MESSAGE);
                } break;

                case 2: { // ver carrito
                    List<CartItem> items = cart.getItems();
                    if (items.isEmpty()) { JOptionPane.showMessageDialog(null, "Carrito vacío"); break; }
                    String lines = items.stream()
                            .map(ci -> String.format("%s x%d  S/%s",
                                    ci.getProduct().getName(), ci.getQuantity(), ci.lineSubtotal()))
                            .collect(Collectors.joining("\n"));
                    JOptionPane.showMessageDialog(null,
                            "Items:\n" + lines + "\n\nSubtotal: S/" + cart.subtotal() + "\nDescuentos reglas: S/" + cart.totalDiscount(),
                            "Carrito", JOptionPane.INFORMATION_MESSAGE);
                } break;

                case 3: { // cupón
                    String code = Validation.askNonEmpty("Ingrese código de cupón:", "Cupón");
                    if (code == null) break;
                    var c = coupons.find(code);
                    if (c.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Cupón no válido");
                    } else if (c.get().isSingleUse() && c.get().isUsed()) {
                        JOptionPane.showMessageDialog(null, "Cupón ya utilizado");
                    } else {
                        selectedCoupon = c.get();
                        JOptionPane.showMessageDialog(null, "Cupón aplicado: " + selectedCoupon.getCode());
                    }
                } break;

                case 4: { // checkout
                    if (cart.getItems().isEmpty()) { JOptionPane.showMessageDialog(null, "Carrito vacío"); break; }

                    String regionStr = (String) JOptionPane.showInputDialog(null, "Región de impuestos:", "Checkout",
                            JOptionPane.QUESTION_MESSAGE, null, new String[]{"PE","US","EU"}, "PE");
                    if (regionStr == null) break;
                    TaxCalculator.Region region = TaxCalculator.Region.valueOf(regionStr);

                    int availablePts = loyalty.getAccount().getPoints();
                    String redeem = JOptionPane.showInputDialog(null, "Puntos disponibles: " + availablePts + "\n" +
                            "¿Cuántos desea usar? (100 pts = S/10)", "Fidelidad", JOptionPane.QUESTION_MESSAGE);
                    int redeemPts = 0;
                    if (redeem != null && !redeem.trim().isEmpty()) {
                        try {
                            redeemPts = Integer.parseInt(redeem.trim());
                            if (redeemPts < 0 || redeemPts > availablePts) redeemPts = 0;
                        } catch (NumberFormatException ignored) {}
                    }

                    var summary = checkout.compute(cart.getItems(), cart.totalDiscount(), selectedCoupon, region, redeemPts);

                    String payMethod = (String) JOptionPane.showInputDialog(null, "Método de pago:", "Pago",
                            JOptionPane.QUESTION_MESSAGE, null, new String[]{"Efectivo","Tarjeta"}, "Efectivo");
                    if (payMethod == null) break;

                    PaymentStrategy ps = "Tarjeta".equals(payMethod) ? new CardPayment() : new CashPayment();
                    // Simulación de pago
                    boolean paid = ps.pay(summary.total);
                    if (!paid) { JOptionPane.showMessageDialog(null, "Pago cancelado/denegado."); break; }

                    // Redimir puntos
                    if (redeemPts > 0) loyalty.getAccount().redeem(redeemPts);
                    // Ganar puntos
                    int earned = loyalty.computeEarnedPoints(Double.parseDouble(summary.total.toString()));
                    loyalty.getAccount().addPoints(earned);

                    // Disminuir stock
                    checkout.commitStock(cart.getItems());

                    // Recibo
                    String receipt = new ReceiptGenerator("S/ ").generate(cart.getItems(), summary, regionStr, ps.name(), earned, loyalty.getAccount().getPoints());
                    JTextArea area = new JTextArea(receipt, 20, 50);
                    area.setEditable(false);
                    JOptionPane.showMessageDialog(null, new JScrollPane(area), "Recibo", JOptionPane.INFORMATION_MESSAGE);

                    // Reset de carrito y cupón
                    cart.getItems().clear();
                    selectedCoupon = null;
                } break;
            }
        }

        JOptionPane.showMessageDialog(null, "¡Hasta luego!");
    }
}
