package com.krail.shop.repo;

import com.krail.shop.common.Money;
import com.krail.shop.model.Coupon;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Repositorio en memoria de cupones. */
public class CouponRepository {
    private final Map<String, Coupon> coupons = new HashMap<>();

    public CouponRepository() {
        coupons.put("WELCOME10", new Coupon("WELCOME10", Coupon.Type.PERCENTAGE, new BigDecimal("0.10"), Money.ZERO, false));
        coupons.put("FREESHIP", new Coupon("FREESHIP", Coupon.Type.FIXED, null, Money.of(10), true));
    }

    public Optional<Coupon> find(String code) {
        if (code == null) return Optional.empty();
        return Optional.ofNullable(coupons.get(code.toUpperCase()));
    }
}
