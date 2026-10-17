package com.example.recordz.ui;

import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "a-propos", layout = MainLayout.class)
@AnonymousAllowed
public class AProposView extends VerticalLayout implements HasDynamicTitle {

    public AProposView() {
        getStyle()
                .set("border-radius", "20px")
                .set("border", "1px solid #6100C1")
                .set("max-width", "800px")
                .set("overflow", "hidden")
                .set("padding-left", "8px");
        setWidthFull();
        setPadding(true);

        H2 title = new H2(getTranslation("about.title"));
        title.getStyle().set("color", "#0000CC");

        add(title, new Paragraph(getTranslation("about.propos")));
    }

    @Override
    public String getPageTitle() {
        return getTranslation("about.page.title");
    }
}