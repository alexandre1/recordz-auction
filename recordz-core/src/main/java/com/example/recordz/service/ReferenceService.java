package com.example.recordz.service;

import static org.jooq.impl.DSL.*;
import com.example.recordz.model.domain.*;

import com.example.recordz.repository.ArticleRepository;
import com.example.recordz.repository.ReferentielRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class ReferenceService {

    private final DSLContext dsl;
    private static final int LANGUE_FR = 1;
    private final ReferentielRepository referentielRepository; // ← ajouter
    private final ArticleRepository articleRepository;

    /** Fourni par le module web (langue de l'UI). Absent dans un contexte sans interface : français par défaut. */
    @Autowired(required = false)
    private CurrentLocaleProvider localeProvider;


    public ReferenceService(DSLContext dsl, ReferentielRepository referentielRepository, ArticleRepository articleRepository) {
        this.dsl = dsl;
        this.referentielRepository = referentielRepository;
        this.articleRepository = articleRepository;
    }

    public List<ReferenceItem> findAllTypesEnchere() {
        return findLocalized("condition_enchere_libelle_langue", "id");
    }



    public String findPersonneEmail(Integer idPersonne) {
        return dsl.fetch("""
            SELECT email FROM personne WHERE id_personne = ?
            """, idPersonne)
                .map(r -> r.get("email", String.class))
                .stream()
                .findFirst()
                .orElse(null);
    }

    public record ProfilStats(
            String nomUtilisateur,
            String nom,
            String prenom,
            int enchOuv,
            int enchFerm,
            int ventesOuv,
            int ventesFerm,
            int evalVentes,
            int evalAchats,
            int nbArticlesAchetes, int nbCommentaires,
            int nbArticlesMisEnVente,
            int nbVisiteurs   // ← nouveau
    ) {}

    public int findNbArticlesMisEnVente(int idVendeur) {
        return dsl.fetch("""
        SELECT COUNT(*) as c FROM met_en_vente
        WHERE ref_vendeur = ?
        """, idVendeur)
                .get(0).get("c", Integer.class);
    }
    public Integer findPersonneIdByEmail(String email) {
        var result = dsl.fetch("""
        SELECT id_personne as c
        FROM personne
        WHERE email = ?
        """, email);

        return result.isEmpty() ? null : result.get(0).get("c", Integer.class);
    }

    public Integer findPersonneId(Integer idPArticle) {
        var result = dsl.fetch("""
        SELECT id_personne as c
        FROM personne, a_paye
        WHERE ref_article =  ? and  a_paye.ref_acheteur = personne.id_personne 
        """, idPArticle);

        return result.isEmpty() ? null : result.get(0).get("c", Integer.class);
    }

    public Integer findIdVendeur(int articleId) {
        var result = dsl.fetch("""
        SELECT p.id_personne as c
        FROM personne p
        JOIN met_en_vente mv ON p.id_personne = mv.ref_vendeur
        WHERE mv.ref_article = ?
        """, articleId);

        return result.isEmpty() ? null : result.get(0).get("c", Integer.class);
    }

    public Integer findIdAcheteur(int articleId) {
        var result = dsl.fetch("""
        SELECT p.id_personne as c
        FROM personne p
        JOIN a_paye mv ON p.id_personne = mv.ref_acheteur
        WHERE mv.ref_article = ?
        """, articleId);

        return result.isEmpty() ? null : result.get(0).get("c", Integer.class);
    }

    public Integer findIdVendeur(Integer idArticle) {
        var result = dsl.fetch("""
            SELECT p.id_personne as c FROM met_en_vente mv , article as a , personne p WHERE 
            mv.ref_article = a.id_article AND a.id_article = ? AND p.id_personne = mv.ref_vendeur 
            """, idArticle);

        return result.isEmpty() ? null : result.get(0).get("c", Integer.class);
    }
    public int findUserID(String email) {
        return dsl.fetch("""
        SELECT p.id_personne as c 
        FROM personne  mv
        JOIN personne p ON p.id_personne
        WHERE p.email = ?
        """, email)                          // ✅ parenthèse fermée ici
                .get(0).get("c", Integer.class);
    }
    // ✅ Nouveau code
    public List<ReferenceItem> findAllFuelTypes() {
        return findLocalized("type_essence_libelle_langue", "ref_type_essence");
    }

    public List<ReferenceItem> findAllGearboxTypes() {
        return findLocalized("boite_de_vitesse_libelle_langue", "ref_boite_de_vitesse");
    }
    public List<ReferenceItem> findAllWineTypes() {
        return findLocalized("type_de_vin_libelle_langue", "ref_type_de_vin");
    }
    // Retourne : [nom, prenom, date, question, texte]
    public List<String[]> findCommentaires(int idArticle) {
        return dsl.fetch("""
        SELECT p.nom, p.prenom, c.date, c.question, c.texte
        FROM commentaire c
        JOIN personne p ON p.id_personne = c.ref_emetteur
        WHERE c.ref_article = ?
        ORDER BY c.date ASC
        """, idArticle)
                .map(r -> new String[]{
                        r.get("nom",      String.class),
                        r.get("prenom",   String.class),
                        r.get("date",     String.class),
                        r.get("question", String.class),
                        r.get("texte",    String.class)
                });
    }
    public List<Number> findStatsByBrandAndYear(String marque, int annee, Integer catId) {
        List<Number> result = new ArrayList<>(Collections.nCopies(12, 0));

        String sql = """
        SELECT MONTH(ap.date_fermeture_enchere) AS mois,
               SUM(ap.quantite)                 AS total
        FROM a_paye ap
        JOIN article a ON a.id_article = ap.ref_article
        WHERE a.marque = ?
          AND YEAR(ap.date_fermeture_enchere) = ?
          AND ap.date_fermeture_enchere IS NOT NULL
        """
                + (catId != null ? " AND a.ref_categorie = ?\n" : "")
                + """
        GROUP BY MONTH(ap.date_fermeture_enchere)
        ORDER BY mois
        """;
        var query = catId != null
                ? dsl.fetch(sql, marque, annee, catId)
                : dsl.fetch(sql, marque, annee);

        query.forEach(r -> {
            int mois     = r.get("mois",  Integer.class);
            Number total = r.get("total", Long.class);
            result.set(mois - 1, total != null ? total : 0);
        });

        return result;
    }
    public List<String> findAllBrands() {
        return dsl.fetch("""
            SELECT DISTINCT marque
            FROM article
            ORDER BY marque DESC
            """)
                .map(r -> r.get("marque", String.class)); // ✅ retourne directement un String
    }

    public List<String[]> findCommentairesByEMmetteur(String email) {
        return dsl.fetch("""
        SELECT p.nom, p.prenom, c.date, c.question, c.texte
        FROM commentaire c
        JOIN personne p ON p.id_personne = c.ref_emetteur
        WHERE c.ref_article = c.ref_emetteur AND  p.email = ?
        ORDER BY c.date DESC
        """, email)
                .map(r -> new String[]{
                        r.get("nom",      String.class),
                        r.get("prenom",   String.class),
                        r.get("date",     String.class),
                        r.get("question", String.class),
                        r.get("texte",    String.class)
                });
    }

    public void enregistrerCommentaire(int idArticle, int idAuteur,
                                       String question, String texte) {
        dsl.execute("""
        INSERT INTO commentaire (ref_article, ref_emetteur, question, texte, date)
        VALUES (?, ?, ?, ?, NOW())
        """, idArticle, idAuteur, question, texte);
    }


    public void enregistrerNote(int idArticle, int idAcheteur, int note) {
        dsl.execute("""
        INSERT INTO evaluation_article (ref_article, ref_acheteur, note,date,ref_vendeur)
        VALUES (?, ? ,?,  NOW(), 0)
        """, idArticle, idAcheteur, note);
    }

    public List<ReferenceItem> findAllGameTypes() {
        return findLocalized("type_de_jeux_libelle_langue", "ref_type_de_jeux");
    }

    public List<ReferenceItem> findAllScreenTypes() {
        return findLocalized("type_ecran_libelle_langue", "ref_type_ecran");
    }
    public void enregistrerAchat(int idArticle, int idAcheteur, Double prix, Integer quantite, Integer refConditionPayement, Integer refModeLivraiso) {

        // ── 1. Récupérer le vendeur (owned) depuis l'article ──────────────────
        int idVendeur = findVendeurByArticle(idArticle);

        // ── 2. INSERT transaction ──────────────────────────────────────────────
        dsl.execute("""
            INSERT INTO a_paye (ref_acheteur, ref_vendeur, date_payement, montant, ref_article, ref_condition_payement, ref_mode_de_livraison, ref_statut, quantite, ref_canton, date_fermeture_enchere)
            VALUES (?, ?, NULL, ?, ?, ?, ?, 8, 1, 7, NULL)
    """, idAcheteur, idVendeur, prix != null ? prix : 0.0, idArticle, refConditionPayement, refModeLivraiso);
//   1          2          3                  4          5                    6
        // ── 3. Marquer l'article comme vendu ──────────────────────────────────
        int qu = countArticleQuantite(idArticle) -1;
        if (qu == 0) {
            System.out.println("UPPPPPPPPPPPPPPPPPPPPPPPPPDATEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
            dsl.execute("""
            UPDATE article SET vendu = 1  , quantite = ? WHERE id_article = ? 
            """, 0,  idArticle);
        }else {
            System.out.println("NOT UPDATED");
            dsl.execute("""
            UPDATE article SET vendu = 0  , quantite = ? WHERE id_article = ? 
            """, qu,  idArticle);
        }
    }

    private int countArticleQuantite (int idArticle) {
        return dsl.fetch("""
        SELECT quantite FROM article WHERE id_article = ?
        """, idArticle)
                .stream()
                .findFirst()
                .map(r -> r.get("quantite", Integer.class))
                .orElse(0);
    }
    private int findVendeurByArticle(int idArticle) {
        return dsl.fetch("""
        SELECT owned FROM article WHERE id_article = ?
        """, idArticle)
                .stream()
                .findFirst()
                .map(r -> r.get("owned", Integer.class))
                .orElse(0);
    }
    public ProfilStats findProfilStats(String email, String username) {
        // Récupérer la personne
        var row = dsl.fetch("""
        SELECT id_personne, nom, prenom, email, nom_utilisateur
        FROM personne WHERE email = ?
        """, email)
                .stream().findFirst().orElse(null);

// ✅ Nouveau — 12 paramètres
        if (row == null) return new ProfilStats(username, "", "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        int id     = row.get("id_personne", Integer.class);
        String nom    = row.get("nom", String.class);
        String prenom = row.get("prenom", String.class);
        String nomUtil = row.get("nom_utilisateur", String.class);



        // Enchères ouvertes (vendeur, enchere > 0, vendu = 0)
        int enchOuv = dsl.fetch("""
        SELECT COUNT(*) as c FROM article a
        JOIN met_en_vente m ON m.ref_article = a.id_article
        WHERE m.ref_vendeur = ? AND a.enchere > 0 AND a.vendu = 0
        """, id).get(0).get("c", Integer.class);

        // Enchères fermées
        int enchFerm = dsl.fetch("""
        SELECT COUNT(*) as c FROM article a
        JOIN met_en_vente m ON m.ref_article = a.id_article
        WHERE m.ref_vendeur = ? AND a.enchere > 0 AND a.vendu = 1
        """, id).get(0).get("c", Integer.class);

        // Ventes directes ouvertes (enchere = 0)
        int ventesOuv = dsl.fetch("""
        SELECT COUNT(*) as c FROM article a
        JOIN met_en_vente m ON m.ref_article = a.id_article
        WHERE m.ref_vendeur = ? 
        AND (a.enchere = 0 OR a.enchere IS NULL) 
        AND a.vendu = 0
    """, id).get(0).get("c", Integer.class);
        // Ventes directes fermées
        int ventesFerm = dsl.fetch("""
            SELECT COUNT(*) as c FROM article a
            JOIN met_en_vente m ON m.ref_article = a.id_article
            WHERE m.ref_vendeur = ? 
            AND a.vendu = 1
        """, id).get(0).get("c", Integer.class);
        // Évaluations des ventes (en tant que vendeur)
        int evalVentes = dsl.fetch("""
        SELECT COUNT(*) as c FROM evaluation_vente WHERE ref_vendeur = ?
        """, id).get(0).get("c", Integer.class);

        // Évaluations des achats (en tant qu'acheteur)
        int evalAchats = dsl.fetch("""
        SELECT COUNT(*) as c FROM evaluation_article WHERE ref_acheteur = ?
        """, id).get(0).get("c", Integer.class);

        // Articles achetés
        int nbAchetes = dsl.fetch("""
            SELECT COUNT(*) as c FROM a_paye ap
            JOIN article a ON a.id_article = ap.ref_article
            JOIN personne p ON p.id_personne = ap.ref_vendeur
            WHERE ap.ref_acheteur = ?
            """
                , id).get(0).get("c", Integer.class);

        // Commentaires
        int nbCommentaires = dsl.fetch("""
        SELECT COUNT(*) as c FROM commentaire WHERE ref_emetteur = ?
        """, id).get(0).get("c", Integer.class);
        int nbArticlesMisEnVente = dsl.fetch("""
        SELECT COUNT(*) as c FROM met_en_vente
        WHERE ref_vendeur = ?
        """, id).get(0).get("c", Integer.class);


        int nbVisiteurs = dsl.fetch("""
        SELECT COUNT(*) as c FROM demande_visite
        WHERE ref_vendeur = ?
    """, id).get(0).get("c", Integer.class);

        return new ProfilStats(nomUtil, nom, prenom,
                enchOuv, enchFerm, ventesOuv, ventesFerm,
                evalVentes, evalAchats, nbAchetes, nbCommentaires,
                nbArticlesMisEnVente, nbVisiteurs);
    }

    public void updatePochette(int idArticle, String nomFichier) {
        dsl.execute("""
        UPDATE article SET pochette = ? WHERE id_article = ?
        """, nomFichier, idArticle);
    }

    // Ventes directes (enchere = 0) ouvertes ou fermées
    public List<String[]> findVentesDirectes(int idVendeur, boolean vendues) {
        return dsl.fetch("""
        SELECT a.id_article, a.nom, a.prix, a.pochette
        FROM article a
        JOIN met_en_vente m ON m.ref_article = a.id_article
        WHERE m.ref_vendeur = ?
        AND (a.enchere = 0 OR a.enchere IS NULL)
        AND a.vendu = ?
        """, idVendeur, vendues ? 1 : 0)
                .map(r -> new String[]{
                        String.valueOf(r.get("id_article", Integer.class)),
                        r.get("nom", String.class),
                        r.get("prix", String.class),
                        r.get("pochette", String.class)
                });
    }

    public List<String[]> findVentesEncheres(int idVendeur) {
        return dsl.fetch("""
        SELECT a.id_article, a.nom, a.prix, a.pochette
        FROM article a
        JOIN met_en_vente m ON m.ref_article = a.id_article
        WHERE m.ref_vendeur = ?
        AND (a.enchere = '1')
        AND a.vendu = '0'
        """, idVendeur)
                .map(r -> new String[]{
                        String.valueOf(r.get("id_article", Integer.class)),
                        r.get("nom", String.class),
                        r.get("prix", String.class),
                        r.get("pochette", String.class)
                });
    }

    // Évaluations des achats
    public List<String[]> findEvalAchats(int idAcheteur) {
        return dsl.fetch("""
        SELECT a.nom, ea.note
        FROM evaluation_article ea
        JOIN article a ON a.id_article = ea.ref_article
        WHERE ea.ref_acheteur = ?
        """, idAcheteur)
                .map(r -> new String[]{
                        r.get("nom", String.class),
                        String.valueOf(r.get("note", Integer.class)),

                });
    }
    public List<String[]> findVEvalAAchatForProfil(Integer idVendeur) {
        return dsl.fetch("""
    SELECT a.id_article, a.nom, a.prix, a.pochette, m.note
    FROM article a
    JOIN evaluation_article m ON m.ref_article = a.id_article
    WHERE m.ref_acheteur = ?
     """, idVendeur)
                .map(r -> new String[]{
                        String.valueOf(r.get("id_article", Integer.class)),
                        r.get("nom", String.class),
                        r.get("prix", String.class),
                        r.get("pochette", String.class),
                        String.valueOf(r.get("note", Integer.class))  // ← converti en String
                });
    }

    // Commentaires par id personne
    public List<String[]> findCommentairesByPersonneId(int idPersonne) {
        return dsl.fetch("""
        SELECT c.date, c.question, c.texte,c.ref_article
        FROM commentaire c
        WHERE c.ref_emetteur = ?
        ORDER BY c.date DESC
        """, idPersonne)
                .map(r -> new String[]{
                        r.get("date", String.class),
                        r.get("question", String.class),
                        r.get("texte", String.class),
                        String.valueOf(r.get("ref_article", Long.class))
                });
    }
    public int ajouterArticle(
            int idCategorie,
            Integer idSousCategorie,
            int idConditionPayement,
            int idModeLivraison,
            int idEtat,
            int typeEnchere,        // ← maintenant un int direct
            String fabricant,
            String titre,
            String description,
            double prix,
            int idVendeur           // ← ref vendeur depuis la DB
    ) {
        // 1. Insérer l'article
        dsl.execute("""
                INSERT INTO article (
                    marque, nom, label, prix, etat,
                    ref_categorie, ref_subcategorie,
                    owned, ref_statut, ref_depot,
                    enchere, ref_condition_payement,
                    ref_mode_de_livraison, vendu, lang
                ) VALUES (?, ?, ?, ?, ?,
                          ?, ?,
                          ?, 1, 9,
                          ?, ?,
                          ?, 0, 'fr')
                """,
                fabricant, titre, description, prix, String.valueOf(idEtat),
                idCategorie, idSousCategorie,
                idVendeur,   // ← ici au lieu de 0
                typeEnchere, idConditionPayement,
                idModeLivraison
        );
        // 2. Récupérer l'id de l'article inséré
        int idArticle = dsl.fetchOne("SELECT LAST_INSERT_ID() as id")
                .get("id", Integer.class);
        System.out.println("idArticle=" + idArticle + " | idVendeur=" + idVendeur);

        try {
            int rows = dsl.execute("""
        INSERT INTO met_en_vente (ref_vendeur, ref_article, date_stock)
        VALUES (?, ?, CURDATE())
        """, idVendeur, idArticle);
            System.out.println("met_en_vente rows=" + rows);
        } catch (Exception e) {
            System.err.println("ERREUR met_en_vente : " + e.getMessage());
        }
        return idArticle; // ✅ retourner l'id
    }

    public Integer findPersonneIdByUsername(String username) {
        return dsl.fetch("""
            SELECT id_personne FROM personne WHERE email = ?
            """, username)
                .stream()
                .findFirst()
                .map(r -> r.get("id_personne", Integer.class))
                .orElse(null);
    }

    /**
     * Met à jour tous les champs spécifiques d'un article après insertion initiale.
     * À ajouter dans ReferenceService.java
     */
    public void updateChampsSpecifiques(int idArticle, com.example.recordz.model.domain.dto.ArticleFormData d) {
        dsl.execute("""
            UPDATE article SET
                quantite              = ?,
                enchere_date_debut    = ?,
                enchere_date_fin      = ?,
                nb_cheveaux           = ?,
                nb_cylindre           = ?,
                nb_km                 = ?,
                annee                 = ?,
                premiere_immatriculation = ?,
                essence_ou_diesel     = ?,
                ref_boite_de_vitesse  = ?,
                clima                 = ?,
                ref_location_ou_achat = ?,
                ref_pays              = ?,
                ref_canton            = ?,
                lieu                  = ?,
                adresse               = ?,
                npa                   = ?,
                nb_piece              = ?,
                surface_habitable     = ?,
                superficie_terrain    = ?,
                annee_construction    = ?,
                ref_pays_region_vin   = ?,
                ref_cepage            = ?,
                ref_type_de_vin       = ?,
                tx                    = ?,
                acteurs               = ?,
                realisateur           = ?,
                auteur                = ?,
                ref_type_de_jeux      = ?,
                ref_type_ecran        = ?,
                dimension             = ?,
                processeur            = ?,
                ram                   = ?,
                disque_dur            = ?,
                taille                = ?
            WHERE id_article = ?
            """,
                d.quantite,
                d.enchereDateDebut,
                d.enchereDateFin,
                d.nbCheveaux,
                d.nbCylindre,
                d.nbKm,
                d.annee != null ? Integer.parseInt(d.annee) : null,
                d.premiereImmatriculation,
                d.essenceOuDiesel,
                d.refBoiteDeVitesse,
                d.clima != null ? (d.clima ? 1 : 0) : 0,
                d.refLocationOuAchat,
                d.refPays,
                d.refCanton,
                d.lieu,
                d.adresse,
                d.npa,
                d.nbPiece,
                d.surfaceHabitable,
                d.superficieTerrain,
                d.anneeConstruction,
                d.refPaysRegionVin,
                d.refCepage,
                d.refTypeDeVin,
                d.millesime,
                d.acteurs,
                d.realisateur,
                d.auteur,
                d.refTypeDeJeux,
                d.refTypeEcran,
                d.dimension,
                d.processeur,
                d.ram,
                d.disqueDur,
                d.taille,
                idArticle
        );
    }
    // ── Location ou achat (Location / Achat) ─────────────────────────────
    public List<ReferenceItem> findAllLocationOuAchat() {
        return findLocalized("location_ou_achat_libelle_langue", "ref_location_ou_achat");
    }

    public List<ReferenceItem> findAllPays() {
        return dsl.fetch("SELECT id_pays_present, nom FROM pays_present ORDER BY id_pays_present")
                .map(r -> new ReferenceItem(r.get(0, Integer.class), r.get(1, String.class)));
    }

    public List<ReferenceItem> findAllCantons() {
        return dsl.fetch("SELECT id_canton, nom FROM canton_fr ORDER BY id_canton")
                .map(r -> new ReferenceItem(r.get(0, Integer.class), r.get(1, String.class)));
    }

    public List<ReferenceItem> findAllCepages() {
        return dsl.fetch("SELECT id_cepage, nom FROM cepage ORDER BY nom")
                .map(r -> new ReferenceItem(r.get(0, Integer.class), r.get(1, String.class)));
    }

    public List<ReferenceItem> findAllPaysRegionVin() {
        return dsl.fetch("SELECT id_pays_region_vin, nom FROM pays_region_vin ORDER BY nom")
                .map(r -> new ReferenceItem(r.get(0, Integer.class), r.get(1, String.class)));
    }
    public List<String[]> findVisiteursVendeur(String email) {
        return dsl.fetch("""
            SELECT personne.nom, personne.prenom
            FROM demande_visite , personne
            WHERE ref_vendeur =  personne.id_personne AND personne.email = ?
            ORDER BY id_demande DESC
            LIMIT 50
            """, email)
                .map(r -> new String[]{
                        r.get("nom", String.class),
                        r.get("prenom", String.class)
                });
    }
    public Integer findNoteForArticle(Integer id) {
        var result = dsl.fetch("""
        SELECT ea.note
        FROM evaluation_article ea
        JOIN article a ON a.id_article = ea.ref_article
        JOIN personne p ON p.id_personne = ea.ref_acheteur
        WHERE a.id_article = ?
        """, id);

        if (result.isEmpty()) return null;
        return result.get(0).get("note", Integer.class);
    }

    public List<String[]> findArticlesAchetes(int idAcheteur) {
        return dsl.fetch("""
            SELECT a.id_article , a.nom, a.marque, a.pochette,
                   ap.montant,  p.nom_utilisateur as vendeur
            FROM a_paye ap
            JOIN article a ON a.id_article = ap.ref_article
            JOIN personne p ON p.id_personne = ap.ref_vendeur
            WHERE ap.ref_acheteur = ? 
            ORDER BY ap.date_fermeture_enchere DESC
            LIMIT 50
            """, idAcheteur)
                .map(r -> new String[]{
                        String.valueOf(r.get("id_article", Integer.class)),
                        r.get("nom", String.class),
                        r.get("montant", String.class),
                        r.get("pochette", String.class),
                        r.get("vendeur", String.class)
                });
    }

    public List<String> searchNoms(String filtre, int limit) {
        if (filtre == null || filtre.isBlank()) {
            return List.of();
        }
        return articleRepository.findNomsByFiltre(filtre, limit);
    }
    public void enregistrerVisite(int idArticle, String nom, String prenom, String email) {
        try {
            Integer refPersonne = findPersonneIdByEmail(email);
            if (refPersonne == null) refPersonne = 0;

            dsl.execute("""
            INSERT INTO article_visite
                (ref_article, ref_personne, nom, prenom, email, date_visite)
            VALUES (?, ?, ?, ?, ?, now())
            """, idArticle, refPersonne, nom, prenom, email);

        } catch (Exception ex) {
            System.err.println("Erreur enregistrerVisite : " + ex.getMessage());
        }
    }

    public void enregistrerOffre(int idArticle, int idAcheteur, double montant) {
        // 1. Insérer l'offre
        dsl.execute("""
        INSERT INTO enchere (ref_article, ref_enchereur, prix, date_enchere)
        VALUES (?, ?, ?, NOW())
        """, idArticle, idAcheteur, montant);

        // 2. Mettre à jour le compteur d'enchères sur l'article
        dsl.execute("""
        UPDATE article SET nbr_enchere = COALESCE(nbr_enchere, 0) + 1
        WHERE id_article = ?
        """, idArticle);
    }

    public List<String[]> findVisiteurs(int idArticle) {
        return dsl.fetch("""
        SELECT av.nom, av.prenom, av.email, av.date_visite
        FROM article_visite av
        WHERE av.ref_article = ?
        ORDER BY av.date_visite DESC
        LIMIT 50
        """, idArticle)
                .map(r -> new String[]{
                        r.get("nom", String.class),
                        r.get("prenom", String.class),
                        r.get("email", String.class),
                        r.get("date_visite", java.time.LocalDateTime.class) != null
                                ? r.get("date_visite", java.time.LocalDateTime.class).toString()
                                : ""
                });
    }
    public Double findDerniereOffre(int idArticle) {
        return dsl.fetch("""
            SELECT prix FROM enchere
            WHERE ref_article = ?
            ORDER BY date_enchere DESC
            LIMIT 1
            """, idArticle)
                .stream()
                .findFirst()
                .map(r -> r.get("prix", Double.class))
                .orElse(null);
    }


    public List<ReferenceItem> findAllPayements() {
        return findLocalized("condition_payement_libelle_langue", "ref_condition_payement");
    }

    public List<ReferenceItem> findAllLivraisons() {
        return findLocalized("condition_livraison_libelle_langue", "ref_mode_de_livraison");
    }

    public List<ReferenceItem> findAllLivraisonsParVendeur(int refVendeur) {
        return findLocalized("condition_livraison_libelle_langue", "ref_mode_de_livraison");
    }

    public List<ReferenceItem> findAllEtats() {
        return findLocalized("etat_libelle_langue", "ref_etat");
    }
    public List<ReferenceItem> findAllMois() {
        return findLocalized("mois_libelle_langue", "ref_mois");
    }


    // ── Records ───────────────────────────────────────────────

    public record CategorieItem(int id, String nom) {
        @Override public String toString() { return nom; }
    }

    public record SousCategorieItem(int id, String nom) {
        @Override public String toString() { return nom; }
    }

    public record ReferenceItem(int id, String nom) {
        @Override public String toString() { return nom; }
    }

    public void enregistrerDemandeVisite(int refArticle,
                                         int refVendeur,
                                         String email,
                                         String telephone,
                                         String nom,
                                         String prenom,
                                         String adresse,
                                         String commentaire,
                                         LocalDateTime dateVisite) {
        referentielRepository.enregistrerDemandeVisite(
                refArticle, refVendeur, email, telephone, nom, prenom, adresse, commentaire, dateVisite
        );
    }


    // Nouvelle version avec offset
    public List<String[]> findDerniersEncherisseurs(int idArticle, int limit, int offset) {
        var nom            = field(name("personne", "nom"), String.class);
        var prenom         = field(name("personne", "prenom"), String.class);
        var nomUtilisateur = field(name("personne", "nom_utilisateur"), String.class);
        var prix           = field(name("enchere", "prix"), java.math.BigDecimal.class);
        var date           = field(name("enchere", "date_enchere"), java.time.LocalDateTime.class);
        var idEnch         = field(name("enchere", "id_enchere"), Long.class);

        return dsl
                .select(nom, prenom, prix, date, nomUtilisateur)
                .from(table(name("enchere")))
                .join(table(name("personne")))
                .on(field(name("personne", "id_personne"), Long.class)
                        .eq(field(name("enchere", "ref_enchereur"), Long.class)))
                .where(field(name("enchere", "ref_article"), Long.class).eq((long) idArticle))
                .orderBy(date.desc(), idEnch.desc())
                .limit(limit)
                .offset(offset)                                   // ← nouveau
                .fetch(r -> new String[]{
                        r.get(nom),
                        r.get(prenom),
                        String.format(Locale.ROOT, "%.2f", r.get(prix)),
                        r.get(date) != null ? r.get(date).toString() : null,
                        r.get(nomUtilisateur)
                });
    }

    // L'ancienne signature continue de fonctionner pour les autres appelants
    public List<String[]> findDerniersEncherisseurs(int idArticle, int limit) {
        return findDerniersEncherisseurs(idArticle, limit, 0);
    }

    // Nombre total d'offres, pour calculer le nombre de pages
    public int countEncherisseurs(int idArticle) {
        return dsl.selectCount()
                .from(table(name("enchere")))
                .where(field(name("enchere", "ref_article"), Long.class).eq((long) idArticle))
                .fetchOne(0, int.class);
    }

    /** Locale courante fournie par le module web (français par défaut). */
    /**
     * Liste de référence "xxx_libelle_langue" dans la langue courante de l'interface.
     * Base = lignes françaises ; libellé remplacé par la traduction EN/DE si elle existe.
     * (table et refCol sont des constantes du code, jamais des saisies utilisateur.)
     */
    private List<ReferenceItem> findLocalized(String table, String refCol) {
        return findLocalized(table, refCol, currentLocale());
    }

    private List<ReferenceItem> findLocalized(String table, String refCol, Locale locale) {
        String sql = """
            SELECT fr.%1$s AS ref_id, COALESCE(lt.libelle, lf.libelle) AS libelle
            FROM %2$s fr
            JOIN libelle lf ON lf.id_libelle = fr.ref_libelle
            LEFT JOIN %2$s tr ON tr.%1$s = fr.%1$s AND tr.ref_langue = ?
            LEFT JOIN libelle lt ON lt.id_libelle = tr.ref_libelle
            WHERE fr.ref_langue = ?
            ORDER BY fr.%1$s
            """.formatted(refCol, table);
        return dsl.fetch(sql, langueId(locale), LANGUE_FR)
                .map(r -> new ReferenceItem(
                        r.get("ref_id", Integer.class),
                        r.get("libelle", String.class)));
    }

    private Locale currentLocale() {
        return localeProvider != null ? localeProvider.get() : Locale.FRENCH;
    }

    /** Table langue : FR=1, DE=2, EN=3. */
    private static int langueId(Locale locale) {
        String lang = locale == null ? "fr" : locale.getLanguage();
        return switch (lang) {
            case "de" -> 2;
            case "en" -> 3;
            default   -> LANGUE_FR;
        };
    }

    // ── Catégories ────────────────────────────────────────────────────────
    public List<CategorieItem> findAllCategories() {
        return findAllCategories(currentLocale());
    }

    public List<CategorieItem> findAllCategories(Locale locale) {
        // Base = libellé français ; remplacé par la traduction si elle existe
        return dsl.fetch("""
            SELECT fr.ref_categorie,
                   COALESCE(lt.libelle, lf.libelle) AS libelle
            FROM categorie_libelle_langue fr
            JOIN libelle lf ON lf.id_libelle = fr.ref_libelle
            LEFT JOIN categorie_libelle_langue tr
                   ON tr.ref_categorie = fr.ref_categorie AND tr.ref_langue = ?
            LEFT JOIN libelle lt ON lt.id_libelle = tr.ref_libelle
            WHERE fr.ref_langue = ?
            ORDER BY COALESCE(lt.libelle, lf.libelle)
            """, langueId(locale), LANGUE_FR)
                .map(r -> new CategorieItem(
                        r.get("ref_categorie", Integer.class),
                        r.get("libelle", String.class)));
    }

    // ── Sous-catégories (toujours jointes sur la catégorie) ───────────────
    public List<SousCategorieItem> findSousCategoriesByCategorie(int idCategorie) {
        return findSousCategoriesByCategorie(idCategorie, currentLocale());
    }

    public List<SousCategorieItem> findSousCategoriesByCategorie(int idCategorie, Locale locale) {
        return dsl.fetch("""
            SELECT fr.ref_subcategorie,
                   COALESCE(lt.libelle, lf.libelle) AS libelle
            FROM subcategorie_libelle_langue fr
            JOIN libelle lf ON lf.id_libelle = fr.ref_libelle
            LEFT JOIN subcategorie_libelle_langue tr
                   ON tr.ref_categorie    = fr.ref_categorie
                  AND tr.ref_subcategorie = fr.ref_subcategorie
                  AND tr.ref_langue       = ?
            LEFT JOIN libelle lt ON lt.id_libelle = tr.ref_libelle
            WHERE fr.ref_langue = ? AND fr.ref_categorie = ?
            ORDER BY COALESCE(lt.libelle, lf.libelle)
            """, langueId(locale), LANGUE_FR, idCategorie)
                .map(r -> new SousCategorieItem(
                        r.get("ref_subcategorie", Integer.class),
                        r.get("libelle", String.class)));
    }

    // ── Libellés d'une catégorie / sous-catégorie précise ─────────────────
    public String findCategorieLibelle(int idCategorie) {
        return findCategorieLibelle(idCategorie, currentLocale());
    }

    public String findCategorieLibelle(int idCategorie, Locale locale) {
        return findAllCategories(locale).stream()
                .filter(c -> c.id() == idCategorie)
                .map(CategorieItem::nom)
                .findFirst().orElse(null);
    }

    public String findSubCategorieLibelle(int idCategorie, int idSubcat) {
        return findSubCategorieLibelle(idCategorie, idSubcat, currentLocale());
    }

    public String findSubCategorieLibelle(int idCategorie, int idSubcat, Locale locale) {
        return findSousCategoriesByCategorie(idCategorie, locale).stream()
                .filter(s -> s.id() == idSubcat)
                .map(SousCategorieItem::nom)
                .findFirst().orElse(null);
    }
}
