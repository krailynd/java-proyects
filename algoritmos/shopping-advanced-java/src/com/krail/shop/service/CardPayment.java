package com.krail.shop.service;

import com.krail.shop.common.Money;
import com.krail.shop.common.Validation;

import javax.swing.*;

/** Pago con tarjeta (simulado con validación Luhn). */
public class CardPayment implements PaymentStrategy {
    private String lastPanMasked = "";

    @Override
    public boolean pay(Money amount) {
        String pan = JOptionPane.showInputDialog(null, "Ingrese número de tarjeta (13–19 dígitos):", "Pago con Tarjeta", JOptionPane.QUESTION_MESSAGE);
        if (pan == null) return false;
        pan = pan.replaceAll("\\s+","");

        if (!Validation.isValidCardPAN(pan)) {
            JOptionPane.showMessageDialog(null, "Tarjeta inválida (Luhn).", "Pago", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        lastPanMasked = "**** **** **** " + pan.substring(pan.length()-4);
        JOptionPane.showMessageDialog(null, "Pago aprobado por S/ " + amount + "\nTarjeta: " + lastPanMasked, "Pago", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    @Override
    public String name() { return "Tarjeta"; }

    public String getLastPanMasked() { return lastPanMasked; }
}
