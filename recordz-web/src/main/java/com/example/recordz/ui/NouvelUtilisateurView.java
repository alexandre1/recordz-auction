package com.example.recordz.ui;

import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "nouvel-utilisateur", layout = MainLayout.class)
@PageTitle("Avant-Garde - Nouvel utilisateur")
@PermitAll
public class NouvelUtilisateurView extends VerticalLayout {
    public NouvelUtilisateurView() {
        setPadding(true);
        setAlignItems(Alignment.CENTER);
        setMaxWidth("400px");

        H2 title = new H2("Créer un compte");
        title.getStyle().set("color", "#0000CC");

        TextField userF = new TextField("Nom d'utilisateur");
        userF.setWidth("300px");
        TextField emailF = new TextField("E-mail");
        emailF.setWidth("300px");
        PasswordField pwF = new PasswordField("Mot de passe");
        pwF.setWidth("300px");
        PasswordField pw2F = new PasswordField("Confirmer le mot de passe");
        pw2F.setWidth("300px");

        Button btn = new Button("Créer le compte");
        btn.getStyle().set("background-color", "#E0E0E0");
        btn.addClickListener(e ->
                btn.getUI().ifPresent(ui -> ui.navigate("login"))
        );

        add(title, userF, emailF, pwF, pw2F, btn);
    }
}
