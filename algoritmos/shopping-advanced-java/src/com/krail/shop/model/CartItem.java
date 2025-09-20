package com.krail.shop.model;

import com.krail.shop.common.Money;

/** Ítem del carrito. */
public class CartItem {
    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        if (product == null) throw new IllegalArgumentException("Producto null");
        if (quantity <= 0) throw new IllegalArgumentException("Cantidad <=0");
        this.product = product; this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int q) {
        if (q <= 0) throw new IllegalArgumentException("Cantidad <=0");
        quantity = q;
    }
    public Money lineSubtotal() { return product.getPrice().multiply(quantity); }
}
