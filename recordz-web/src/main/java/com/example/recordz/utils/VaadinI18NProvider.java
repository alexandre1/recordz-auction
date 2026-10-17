package com.example.recordz.utils;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static java.util.Locale.GERMAN;
import static java.util.ResourceBundle.getBundle;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vaadin.flow.i18n.I18NProvider;

@Component
public class VaadinI18NProvider implements I18NProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(VaadinI18NProvider.class);

    public static final String RESOURCE_BUNDLE_NAME = "vaadinapp";

    private static final Locale FR_CH = Locale.of("fr", "CH");

    private static final ResourceBundle RESOURCE_BUNDLE_EN = getBundle(RESOURCE_BUNDLE_NAME, ENGLISH);
    private static final ResourceBundle RESOURCE_BUNDLE_DE = getBundle(RESOURCE_BUNDLE_NAME, GERMAN);
    private static final ResourceBundle RESOURCE_BUNDLE_FR = getBundle(RESOURCE_BUNDLE_NAME, FRENCH);
    private static final ResourceBundle RESOURCE_BUNDLE_FR_CH = getBundle(RESOURCE_BUNDLE_NAME, FR_CH);

    private static final List<Locale> PROVIDED_LOCALES = List.of(ENGLISH, GERMAN, FRENCH, FR_CH);

    public VaadinI18NProvider() {
        LOGGER.info("{} was found..", VaadinI18NProvider.class.getSimpleName());
    }

    @Override
    public List<Locale> getProvidedLocales() {
        return PROVIDED_LOCALES;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            return "";
        }
        ResourceBundle bundle = bundleFor(locale);

        if (!bundle.containsKey(key)) {
            LOGGER.warn("Missing i18n resource key '{}' for locale {}", key, locale);
            return key;
        }

        String value = bundle.getString(key);
        if (params != null && params.length > 0) {
            return java.text.MessageFormat.format(value, params);
        }
        return value;
    }

    private static ResourceBundle bundleFor(Locale locale) {
        if (locale == null) {
            return RESOURCE_BUNDLE_EN;
        }
        if (FR_CH.equals(locale)) {
            return RESOURCE_BUNDLE_FR_CH;
        }
        return switch (locale.getLanguage()) {
            case "de" -> RESOURCE_BUNDLE_DE;
            case "fr" -> RESOURCE_BUNDLE_FR;
            default   -> RESOURCE_BUNDLE_EN;
        };
    }
}