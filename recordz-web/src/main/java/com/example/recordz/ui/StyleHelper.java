package com.example.recordz.ui;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;

public class StyleHelper {

    // ── Select stylisé ────────────────────────────────────
    public static <T> Select<T> styledSelect(String label) {
        Select<T> select = new Select<>();
        if (label != null && !label.isBlank()) select.setLabel(label);
        applySelectStyle(select);
        return select;
    }

    // ── ComboBox stylisée ─────────────────────────────────
    public static <T> ComboBox<T> styledComboBox(String label) {
        ComboBox<T> combo = new ComboBox<>();
        if (label != null && !label.isBlank()) combo.setLabel(label);
        applyComboStyle(combo);
        return combo;
    }
    // ── TextField stylisé ─────────────────────────────────
    public static TextField styledTextField(String label) {
        TextField tf = new TextField();
        if (label != null && !label.isBlank()) tf.setLabel(label);
        applyTextFieldStyle(tf);
        return tf;
    }

    // ── TextArea stylisé ──────────────────────────────────
    public static com.vaadin.flow.component.textfield.TextArea styledTextArea(String label) {
        com.vaadin.flow.component.textfield.TextArea ta =
                new com.vaadin.flow.component.textfield.TextArea();
        if (label != null && !label.isBlank()) ta.setLabel(label);
        applyTextAreaStyle(ta);
        return ta;
    }

    // ── Appliquer le style sur un TextField existant ──────
    public static void applyTextFieldStyle(TextField tf) {
        tf.getElement().getStyle()
                .set("--vaadin-input-field-border-color", "#6100C1")
                .set("--vaadin-input-field-focused-border-color", "#6100C1")
                .set("--vaadin-input-field-hover-border-color", "#9B40FF")
                .set("--vaadin-input-field-border-radius", "20px");
        tf.getStyle()
                .set("border-radius", "20px")
                .set("font-size", "0.85rem")
                .set("color", "#333");
    }

    // ── Appliquer le style sur un TextArea existant ───────
    public static void applyTextAreaStyle(
            com.vaadin.flow.component.textfield.TextArea ta) {
        ta.getElement().getStyle()
                .set("--vaadin-input-field-border-color", "#6100C1")
                .set("--vaadin-input-field-focused-border-color", "#6100C1")
                .set("--vaadin-input-field-hover-border-color", "#9B40FF")
                .set("--vaadin-input-field-border-radius", "12px");
        ta.getStyle()
                .set("border-radius", "12px")
                .set("font-size", "0.85rem")
                .set("color", "#333");
    }

    // ── TextField en lecture seule (pour fieldRow) ────────
    public static TextField styledReadOnlyField(String value) {
        TextField tf = styledTextField(null);
        tf.setValue(value != null ? value : "");
        tf.setReadOnly(true);
        tf.getElement().getStyle()
                .set("--vaadin-input-field-border-color", "#DDD")
                .set("--vaadin-input-field-background", "#FAFAFA");
        return tf;
    }
    // ── Appliquer le style sur un Select existant ─────────
    public static <T> void applySelectStyle(Select<T> select) {
        select.getStyle()
                .set("border", "1px solid #6100C1")
                .set("border-radius", "20px")
                .set("color", "#6100C1")
                .set("font-size", "0.85rem");

        // Style via la partie native du composant
        select.getElement().getStyle()
                .set("--vaadin-select-border-radius", "20px")
                .set("--vaadin-input-field-border-color", "#6100C1")
                .set("--vaadin-input-field-focused-border-color", "#6100C1")
                .set("--vaadin-input-field-hover-border-color", "#9B40FF");
    }

    // ── Appliquer le style sur un ComboBox existant ───────
    public static <T> void applyComboStyle(ComboBox<T> combo) {
        combo.getElement().getStyle()
                .set("--vaadin-input-field-border-color", "#6100C1")
                .set("--vaadin-input-field-focused-border-color", "#6100C1")
                .set("--vaadin-input-field-hover-border-color", "#9B40FF")
                .set("--vaadin-combo-box-overlay-width", "auto");
        combo.getStyle()
                .set("border-radius", "20px")
                .set("font-size", "0.85rem");
    }

    // ── Bouton stylisé contour violet ─────────────────────
    public static com.vaadin.flow.component.button.Button styledButton(String label) {
        var btn = new com.vaadin.flow.component.button.Button(label);
        btn.getStyle()
                .set("background", "none")
                .set("border", "1px solid #6100C1")
                .set("color", "#6100C1")
                .set("border-radius", "20px")
                .set("font-size", "0.8rem")
                .set("cursor", "pointer")
                .set("padding", "4px 12px")
                .set("margin-top", "4px");
        return btn;
    }

    // ── Bouton stylisé plein violet (action principale) ───
    public static com.vaadin.flow.component.button.Button styledButtonPrimary(String label) {
        var btn = styledButton(label);
        btn.getStyle()
                .set("background-color", "#6100C1")
                .set("color", "white");
        return btn;
    }

    // ── Anchor stylisé ────────────────────────────────────
    public static Anchor styledLink(String label, String href) {
        Anchor link = new Anchor(href, label);
        link.getStyle()
                .set("color", "#0000CC")
                .set("font-weight", "bold")
                .set("font-size", "0.85rem")
                .set("display", "block")
                .set("margin-bottom", "4px")
                .set("margin-top", "10px")
                .set("text-decoration", "none");
        return link;
    }

    // ── Anchor stylisé plein ──────────────────────────────
    public static Anchor styledLinkPrimary(String label, String href) {
        Anchor link = styledLink(label, href);
        link.getStyle()
                .set("color", "#0000CC")
                .set("font-size", "0.85rem")
                .set("text-decoration", "none");
        return link;
    }
}