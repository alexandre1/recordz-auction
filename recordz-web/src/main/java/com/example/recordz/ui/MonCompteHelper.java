package com.example.recordz.ui;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MonCompteHelper {


    private static Anchor navLink(String text, String href) {
        Anchor a = new Anchor(href, text);
        a.getStyle()
                .set("color", "#0000CC")
                .set("font-weight", "bold")
                .set("font-size", "0.85rem")
                .set("display", "block")
                .set("margin-bottom", "4px")
                .set("margin-top", "10px")
                .set("text-decoration", "none");

        return a;
    }
}