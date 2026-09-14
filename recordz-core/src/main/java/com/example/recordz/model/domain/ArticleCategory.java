package com.example.recordz.model.domain;

/**
 * Catégories d'articles avec leurs IDs MySQL correspondants
 * Source: logique extraite du code Perl LoadProperties.java / loadAddArticle
 */
public enum ArticleCategory {

    ART(22222, "Art & Design"),
    FASHION_FEMME(1, "Mode Femme"),
    FASHION_HOMME(2, "Mode Homme"),
    PARFUM(13, "Parfum"),
    COSMETIQUE(16, "Cosmétique"),
    LINGERIE(21, "Lingerie"),
    BEBE(23, "Bébé"),
    IMMOBILIER(25, "Immobilier"),
    VINS(27, "Vins"),
    JEUX(29, "Jeux"),
    COLLECTION(18, "Collection"),
    LIVRES(9, "Livres"),
    INFORMATIQUE(10, "Informatique"),
    TV_VIDEO(7, "TV & Vidéo"),
    DVD(38, "DVD & K7"),
    CD_VINYL_MP3(39, "CD / Vinyl / Mp3"),
    VOITURE(5, "Voiture"),
    MOTO(6, "Moto"),
    MONTRE(34, "Montres"),
    BIJOUX(35, "Bijoux"),
    BATEAU(94, "Bateau / Offshore"),
    SPORT(96, "Sport"),
    ANIMAUX(33, "Animaux"),
    JARDIN(36, "Habitat & Jardin"),
    INSTRUMENTS(92, "Instruments de musique"),
    CIGARES(20, "Cigares"),
    UNKNOWN(-1, "Autre");

    private final int id;
    private final String label;

    ArticleCategory(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static ArticleCategory fromId(int id) {
        for (ArticleCategory cat : values()) {
            if (cat.id == id) return cat;
        }
        return UNKNOWN;
    }
}
