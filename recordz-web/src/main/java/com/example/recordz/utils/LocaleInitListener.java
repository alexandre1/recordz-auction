package com.example.recordz.utils;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;

@Component
public class LocaleInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            Locale saved = (Locale) VaadinSession.getCurrent()
                    .getAttribute(MainLayout.SESSION_LOCALE);
            if (saved != null) {
                uiEvent.getUI().setLocale(saved);
            } else {
                // Locale du navigateur ramenée à l'une des 3 langues gérées
                String lang = uiEvent.getUI().getLocale().getLanguage();
                uiEvent.getUI().setLocale(switch (lang) {
                    case "de" -> Locale.GERMAN;
                    case "en" -> Locale.ENGLISH;
                    default   -> Locale.FRENCH;
                });
            }
        });
    }
}