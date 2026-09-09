package com.example.recordz.ui;

import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "recherche", layout = MainLayout.class)
@PageTitle("Avant-Garde - Résultats")
public class ResultatsRechercheView extends VerticalLayout implements BeforeEnterObserver {

    // { id, nom, fabricant, prix }
    private static final List<String[]> RESULTATS = List.of(
            new String[]{"10", "Sac chanel", "Chanel", "1.2"},
            new String[]{"11", "Petite robe Chanel taille 36", "Chanel", "1"},
            new String[]{"12", "Chaussure chanel taille 36", "Chanel", "9.999"},
            new String[]{"13", "T-shirt Chanel taille S", "Chanel", "9.999"}
    );

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        setPadding(true);
        setSpacing(false);

        add(paginationRow());

        Div table = new Div();
        table.getStyle()
                .set("border", "2px solid #6600CC").set("border-radius", "4px")
                .set("padding", "8px").set("max-width", "660px");

        for (String[] art : RESULTATS) {
            HorizontalLayout row = new HorizontalLayout();
            row.setAlignItems(Alignment.CENTER);
            row.getStyle().set("padding", "6px 4px").set("border-bottom", "1px solid #EEE")
                    .set("cursor", "pointer");

            Div img = new Div();
            img.getStyle().set("width", "70px").set("height", "70px")
                    .set("background-color", "#DDD").set("flex-shrink", "0");

            Anchor nom = new Anchor("/detail/" + art[0], art[1]);
            nom.getStyle().set("color", "#0000CC").set("width", "250px").set("font-size", "0.9rem");

            Span fab = new Span(art[2]);
            fab.getStyle().set("color", "#0000CC").set("width", "100px").set("font-size", "0.85rem");

            Span prix = new Span(art[3]);
            prix.getStyle().set("width", "80px").set("font-size", "0.85rem");

            row.add(img, nom, fab, prix);
            table.add(row);
        }

        add(table);
        add(paginationRow());
    }

    private HorizontalLayout paginationRow() {
        HorizontalLayout pag = new HorizontalLayout();
        pag.getStyle().set("padding", "8px 0").set("gap", "12px");
        for (String t : new String[]{"<-First page->", "<-Next->"}) {
            Anchor a = new Anchor("#", t);
            a.getStyle().set("color", "#0000CC").set("font-size", "0.85rem");
            pag.add(a);
        }
        return pag;
    }
}
