package com.example.recordz.ui;

import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "a-propos", layout = MainLayout.class)
@PermitAll

@PageTitle("Avant-Garde - À propos")
public class AProposView extends VerticalLayout {
    public AProposView() {
        getStyle()
                .set("border-radius", "20px")
                .set("border", "1px solid #6100C1")
                .set("width", "800px")
                .set("overflow", "hidden")
                .set("padding-left", "8px");
        setPadding(true);
        H2 title = new H2("À propos d'Avant-Garde");
        title.getStyle().set("color", "#0000CC");
        add(title, new Paragraph(
                "Avant-Garde est la plateforme d'enchères suisse dédiée aux articles " +
                        "de luxe, fashion, immobilier et bien plus encore. " +
                        "Achetez et vendez en toute confiance en francs suisses (CHF)."
        ));
    }
}
