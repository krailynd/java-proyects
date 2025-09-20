package com.krail.shop.common;

import javax.swing.*;

/**
 * Utilidades de validación y diálogos seguros.
 */
public final class Validation {
    private Validation(){}

    public static Integer askInt(String prompt, String title) {
        while (true) {
            String s = JOptionPane.showInputDialog(null, prompt, title, JOptionPane.QUESTION_MESSAGE);
            if (s == null) return null; // cancelar
            s = s.trim();
            if (s.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No puede estar vacío", "Validación", JOptionPane.WARNING_MESSAGE);
                continue;
            }
            try {
                int v = Integer.parseInt(s);
                if (v < 0) throw new NumberFormatException();
                return v;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ingrese un entero válido (>=0).", "Validación", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public static String askNonEmpty(String prompt, String title) {
        while (true) {
            String s = JOptionPane.showInputDialog(null, prompt, title, JOptionPane.QUESTION_MESSAGE);
            if (s == null) return null;
            s = s.trim();
            if (s.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No puede estar vacío", "Validación", JOptionPane.WARNING_MESSAGE);
            } else {
                return s;
            }
        }
    }

    /** Validación simple Luhn para tarjeta. */
    public static boolean isValidCardPAN(String pan) {
        if (pan == null || !pan.matches("\\d{13,19}")) return false;
        int sum = 0; boolean alt = false;
        for (int i = pan.length()-1; i>=0; i--) {
            int n = pan.charAt(i) - '0';
            if (alt) { n *= 2; if (n>9) n -= 9; }
            sum += n; alt = !alt;
        }
        return sum % 10 == 0;
    }
}
