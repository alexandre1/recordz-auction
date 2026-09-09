package com.example.recordz.model.domain;

import java.util.Set;

public enum ArticleSubCategory {

    // TV
    TV_ECRAN_PLAT(72, "Écran plat"),  // ← vérifie ton vrai ID en DB

    // Informatique — IDs issus de tes insertions (77-94)
    PC(77,               "Pc"),
    ORDINATEUR_PORTABLE(78, "Ordinateurs portable"),
    MAC(79,              "Mac"),
    TABLETTE(80,         "Tablettes"),
    SOURIS(81,           "Souris"),
    TELECOMMANDE(82,     "Télécommandes"),
    LOGICIEL(83,         "Logiciels"),
    NATEL(84,            "Natels"),
    WEBCAM(85,           "Webcams"),
    CABLAGE(86,          "Câblages"),
    CARTE_MERE(87,       "Cartes mères"),
    SCANNER(88,          "Scanners"),
    PHOTOCOPIEUR(89,     "Photocopieurs"),
    ROUTEUR(90,          "Routeurs"),
    ANTENNE_SAT(91,      "Antennes satellite"),
    LECTEUR_BIO(92,      "Lecteurs biométrique"),
    LECTEUR_CARTE(93,    "Lecteurs de cartes à puce"),
    CAISSE_ENREG(94,     "Caisses enregistreuse"),

    // Vêtements
    VETEMENT_HAUTS(1,       "Hauts"),
    VETEMENT_BAS(2,         "Bas"),
    VETEMENT_ROBES(3,       "Robes"),
    VETEMENT_MANTEAUX(4,    "Manteaux"),
    VETEMENT_CHAUSSURES(6,  "Chaussures"),
    VETEMENT_ACCESSOIRES(28,"Accessoires"),
    VETEMENT_SPORT(29,      "Sport"),
    VETEMENT_ENFANT(30,     "Enfant"),
    VETEMENT_LINGERIE(32,   "Lingerie"),
    VETEMENT_SNOWBOARD(311, "Snowboard"),

    UNKNOWN(-1, "Autre");

    private final int id;
    private final String label;

    // ✅ PC + Mac + Ordinateur portable + Tablette
    public static final Set<Integer> INFO_PC_SUBCATS =
            Set.of(77, 78, 79, 80);

    // ✅ TV écran plat
    public static final int TV_ECRAN_PLAT_ID = 72; // ← adapte selon ta DB

    // ✅ Vêtements
    public static final Set<Integer> WEAR_SIZE_SUBCATS =
            Set.of(1, 2, 3, 4, 6, 28, 29, 30, 32, 311);

    ArticleSubCategory(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId()       { return id; }
    public String getLabel() { return label; }

    public static ArticleSubCategory fromId(int id) {
        for (ArticleSubCategory sub : values()) {
            if (sub.id == id) return sub;
        }
        return UNKNOWN;
    }
}