package com.krail.shop.common;

/**
 * Resultado genérico (éxito / error) para evitar excepciones de control de flujo.
 * @param <T> tipo de dato en caso de éxito
 */
public class Result<T> {
    public final boolean ok;
    public final String error;
    public final T value;

    private Result(boolean ok, String error, T value) {
        this.ok = ok; this.error = error; this.value = value;
    }

    public static <T> Result<T> ok(T v) {
        return new Result<>(true, null, v);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(false, msg, null);
    }
}
