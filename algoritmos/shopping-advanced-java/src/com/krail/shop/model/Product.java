package com.krail.shop.model;

import com.krail.shop.common.Money;

/**
 * Producto en catálogo.
 * Pre: price >= 0, stock >= 0
 */
public class Product {
    private final String sku;
    private final String name;
    private final Money price;
    private int stock;

    public Product(String sku, String name, Money price, int stock) {
        if (sku == null || sku.isEmpty()) throw new IllegalArgumentException("SKU vacío");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Nombre vacío");
        if (price == null) throw new IllegalArgumentException("Precio null");
        if (stock < 0) throw new IllegalArgumentException("Stock negativo");
        this.sku = sku; this.name = name; this.price = price; this.stock = stock;
    }

    public String getSku() { return sku; }
    public String getName() { return name; }
    public Money getPrice() { return price; }
    public int getStock() { return stock; }

    public void decreaseStock(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty<=0");
        if (qty > stock) throw new IllegalStateException("Stock insuficiente");
        stock -= qty;
    }

    public void increaseStock(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty<=0");
        stock += qty;
    }
}
