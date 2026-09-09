package com.example.recordz.ui;

import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "test", layout = MainLayout.class)
@PageTitle("Avant-Garde - Mes informations")
@PermitAll

public class TestUi extends VerticalLayout {

    public TestUi() {

        removeAll();
        setPadding(true);
        setSpacing(false);

        add(buildSecondaryNav());

    }

    private HorizontalLayout buildSecondaryNav() {
        HorizontalLayout nav = new HorizontalLayout();
        nav.getStyle()
                .set("color", "#0000CC")
                .set("font-size", "0.85rem");
        setPadding(true);
        setSpacing(false);

        nav.add(
                navLink("Informations",       "/mon-compte/informations"),
                navLink("Mes encheres",       "/encheres"),
                navLink("Ajouter un article", "/ajouter-article"),
                navLink("Faire de la publicité", "#")
        );
        return nav;
    }

    private Anchor navLink(String text, String href) {
        Anchor a = new Anchor(href, text);
        a.getStyle()
                .set("color", "#0000CC")
                .set("font-size", "0.85rem")
                .set("text-decoration", "none");
        return a;
    }
}
