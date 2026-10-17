package com.example.recordz.utils;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.example.recordz.service.CurrentLocaleProvider;
import com.vaadin.flow.component.UI;

@Component
public class VaadinCurrentLocaleProvider implements CurrentLocaleProvider {

    @Override
    public Locale get() {
        UI ui = UI.getCurrent();
        return ui != null ? ui.getLocale() : Locale.FRENCH;
    }
}