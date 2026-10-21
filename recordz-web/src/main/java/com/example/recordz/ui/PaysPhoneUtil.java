package com.example.recordz.ui;

import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.text.Collator;
import java.util.*;

/**
 * Outils partagés pour les pays, les drapeaux (images) et les numéros de téléphone.
 * Utilisable depuis toutes les vues (DetailArticleView, InformationsPersonnellesView…).
 */
public final class PaysPhoneUtil {

    /**
     * Source des images de drapeaux.
     * - CDN : "https://flagcdn.com/w40/%s.png"
     * - Local : "/flags/%s.svg" (SVG de flag-icons dans META-INF/resources/flags/)
     */
    private static final String FLAG_URL_PATTERN = "https://flagcdn.com/w40/%s.png";

    private static final PhoneNumberUtil PHONE_UTIL = PhoneNumberUtil.getInstance();

    /** Codes ISO des pays proposés (l'indicatif est déduit de libphonenumber). */
    private static final String[] CODES = {
            "CH", "FR", "DE", "IT", "BE", "ES", "PT", "GB", "US",
            "CA", "JP", "CN", "BR", "AU", "MA", "SN", "TN"
    };

    private PaysPhoneUtil() {
    }

    // ── Modèle ─────────────────────────────────────────────────

    public record PaysPhone(String code, String indicatif, String nom) {

        /** Nom français du pays = valeur stockée en base ("Suisse", "France", …). */
        public String nomFrancais() {
            return new Locale("", code).getDisplayCountry(Locale.FRENCH);
        }

        @Override
        public String toString() {
            return nom + " (" + indicatif + ")";
        }
    }

    /** Résultat du découpage d'un numéro stocké : pays de l'indicatif + numéro à afficher. */
    public record PhoneParts(PaysPhone pays, String numero) {
    }

    // ── Liste et recherche ─────────────────────────────────────

    /** Liste des pays, avec le nom dans la langue de l'interface. */
    public static List<PaysPhone> buildList(Locale locale) {
        Collator collator = Collator.getInstance(locale);
        List<String> favoris = Arrays.asList(CODES);   // épinglés en tête, dans cet ordre

        Comparator<PaysPhone> ordre = Comparator
                .comparingInt((PaysPhone p) -> {
                    int i = favoris.indexOf(p.code());
                    return i < 0 ? Integer.MAX_VALUE : i;
                })
                .thenComparing(PaysPhone::nom, collator);

        return Arrays.stream(Locale.getISOCountries())           // ~249 codes ISO
                .map(code -> {
                    int dial = PHONE_UTIL.getCountryCodeForRegion(code);   // 0 si inconnu
                    if (dial <= 0) return null;
                    return new PaysPhone(code, "+" + dial,
                            Locale.of("", code).getDisplayCountry(locale));
                })
                .filter(Objects::nonNull)
                .sorted(ordre)
                .toList();
    }

    public static PaysPhone findByCode(List<PaysPhone> list, String code) {
        if (code == null) return null;
        return list.stream().filter(p -> p.code().equalsIgnoreCase(code)).findFirst().orElse(null);
    }

    /** Retrouve un pays à partir de la valeur stockée en base (nom français). */
    public static PaysPhone findByFrenchName(List<PaysPhone> list, String nomFrancais) {
        if (nomFrancais == null || nomFrancais.isBlank()) return null;
        return list.stream()
                .filter(p -> p.nomFrancais().equalsIgnoreCase(nomFrancais.trim()))
                .findFirst().orElse(null);
    }

    // ── Drapeaux ───────────────────────────────────────────────

    public static String flagUrl(String countryCode) {
        return String.format(FLAG_URL_PATTERN, countryCode.toLowerCase(Locale.ROOT));
    }

    /** Image de drapeau de taille fixe (24 x 18 px). */
    public static Image createFlagImage(String countryCode) {
        Image flag = new Image(flagUrl(countryCode), countryCode);
        flag.setWidth("24px");
        flag.setHeight("18px");
        flag.getStyle()
                .set("object-fit", "cover")
                .set("border-radius", "2px")
                .set("box-shadow", "0 0 1px rgba(0,0,0,0.4)")
                .set("flex-shrink", "0");
        return flag;
    }

    /**
     * Configure une ComboBox avec les drapeaux : image dans la liste déroulante
     * et drapeau du pays sélectionné dans le champ fermé.
     * À appeler AVANT le premier setValue(...).
     *
     * @param showDial true : "CH +41" / "Suisse +41" (champ indicatif) ; false : nom du pays seul
     */
    public static void applyFlags(ComboBox<PaysPhone> box, boolean showDial) {
        // Texte du champ fermé et critère de filtrage
        box.setItemLabelGenerator(p -> showDial ? p.code() + " " + p.indicatif() : p.nom());

        // Lignes de la liste déroulante : drapeau + texte
        box.setRenderer(new ComponentRenderer<HorizontalLayout, PaysPhone>(p -> {
            HorizontalLayout line = new HorizontalLayout();
            line.setAlignItems(FlexComponent.Alignment.CENTER);
            line.setPadding(false);
            line.setSpacing(true);
            line.add(createFlagImage(p.code()),
                    new Span(showDial ? p.nom() + " " + p.indicatif() : p.nom()));
            return line;
        }));

        // Drapeau du pays sélectionné dans le champ fermé
        Image prefix = createFlagImage("CH");
        prefix.setVisible(false);
        box.setPrefixComponent(prefix);
        box.addValueChangeListener(e -> {
            PaysPhone p = e.getValue();
            prefix.setVisible(p != null);
            if (p != null) {
                prefix.setSrc(flagUrl(p.code()));
                prefix.setAlt(p.code());
            }
        });
    }

    // ── Téléphone ──────────────────────────────────────────────

    /** Formate le numéro au fil de la saisie, selon les règles du pays. */
    public static String formatAsYouType(String raw, String regionCode) {
        AsYouTypeFormatter formatter = PHONE_UTIL.getAsYouTypeFormatter(regionCode);
        String out = "";
        for (char c : raw.toCharArray()) {
            if (Character.isDigit(c)) {
                out = formatter.inputDigit(c);
            }
        }
        return out;
    }

    /**
     * Convertit une saisie en numéro international sans zéro de tête ("+41791234567").
     *
     * @return "" si la saisie est vide, null si le numéro est invalide
     */
    public static String toE164(String input, String regionCode) {
        if (input == null || input.isBlank()) return "";
        try {
            Phonenumber.PhoneNumber n = PHONE_UTIL.parse(input.trim(), regionCode);
            if (!PHONE_UTIL.isValidNumber(n)) return null;
            return PHONE_UTIL.format(n, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            return null;
        }
    }

    /**
     * Découpe un numéro stocké en base pour préremplir l'indicatif et le champ numéro.
     * Gère aussi les anciens numéros saisis sans indicatif (ex. "079 123 45 67").
     */
    public static PhoneParts splitStored(String stored, PaysPhone defaut, List<PaysPhone> liste) {
        if (stored == null || stored.isBlank()) {
            return new PhoneParts(defaut, "");
        }
        try {
            Phonenumber.PhoneNumber n = PHONE_UTIL.parse(stored.trim(), defaut.code());
            PaysPhone p = findByCode(liste, PHONE_UTIL.getRegionCodeForNumber(n));
            if (p != null) {
                // Numéro sans le "0" de tête : l'indicatif est déjà affiché dans la ComboBox
                String national = formatAsYouType(
                        PHONE_UTIL.getNationalSignificantNumber(n), p.code());
                return new PhoneParts(p, national);
            }
            // Pays absent de la liste : on garde le pays par défaut et on affiche le format international
            return new PhoneParts(defaut, PHONE_UTIL.format(n, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
        } catch (NumberParseException e) {
            return new PhoneParts(defaut, stored.trim());
        }
    }}
