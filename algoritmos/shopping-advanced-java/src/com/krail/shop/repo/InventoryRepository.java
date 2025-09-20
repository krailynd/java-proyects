package com.krail.shop.repo;

import com.krail.shop.common.Money;
import com.krail.shop.model.Product;

import java.util.*;

/** Repositorio en memoria de inventario. */
public class InventoryRepository {
    private final Map<String, Product> bySku = new HashMap<>();

    public InventoryRepository() {
        // Datos de ejemplo
        save(new Product("SKU-TECLADO", "Teclado Mecánico", Money.of(129.90), 25));
        save(new Product("SKU-MOUSE", "Mouse Gamer", Money.of(89.50), 50));
        save(new Product("SKU-AURIS", "Auriculares", Money.of(149.00), 30));
        save(new Product("SKU-MONITOR", "Monitor 24\"", Money.of(699.00), 15));
    }

    public void save(Product p) { bySku.put(p.getSku(), p); }

    public Optional<Product> findBySku(String sku) {
        return Optional.ofNullable(bySku.get(sku));
    }

    public Collection<Product> findAll() {
        return Collections.unmodifiableCollection(bySku.values());
    }
}
