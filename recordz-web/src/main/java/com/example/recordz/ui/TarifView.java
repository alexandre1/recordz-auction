package com.example.recordz.ui;

import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "tarif", layout = MainLayout.class)
@PermitAll
@PageTitle("Avant-Garde - Tarifs")
public class TarifView extends VerticalLayout {
    public TarifView() {
        getStyle()
                .set("border-radius", "20px")
                .set("border", "1px solid #6100C1")
                .set("overflow", "hidden")
                .set("width", "800px")
                .set("padding-left", "8px");
        setPadding(true);
        H2 title = new H2("Tarifs");
        title.getStyle().set("color", "#0000CC");
        add(title);

        Div table = new Div();

        String[][] rows = {
                {"Mise en vente standard", "Gratuit"},
                {"Commission sur vente", "0%"},
                {"Article mis en vedette", "1000 CHF"},
                {"Publicité", "Sur devis"}
        };
        for (String[] r : rows) {
            HorizontalLayout row = new HorizontalLayout();
            Span lbl = new Span(r[0]);
            lbl.getStyle().set("width", "300px").set("color", "#0000CC");
            Span val = new Span(r[1]);
            val.getStyle().set("font-weight", "bold");
            row.add(lbl, val);
            table.add(row);
        }
        add(table);
    }
}
