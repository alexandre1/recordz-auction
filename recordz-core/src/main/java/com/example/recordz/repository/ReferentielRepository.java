package com.example.recordz.repository;

import com.example.recordz.model.domain.Referentiel.*;
import com.example.recordz.service.ReferenceService;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

/**
 * Data access for all reference / lookup tables.
 * Results are cached — these tables change very rarely.
 */
@Repository
public class    ReferentielRepository {

    private final DSLContext dsl;

    public ReferentielRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    private static final int LANGUE_FR = 1; // ← constante par défaut

    @Cacheable("libelles")
    public Optional<String> findLibelle(Long idLibelle) {
        return dsl.select(field("libelle", String.class))
                .from(table("libelle"))
                .where(field("id_libelle").eq(idLibelle))
                .fetchOptional(r -> r.get(field("libelle", String.class)));
    }

    public List<ReferenceService.ReferenceItem> findAllFuelTypes() {
        return dsl.fetch("""
        SELECT tell.ref_type_essence, l.libelle
        FROM type_essence_libelle_langue tell
        JOIN libelle l ON l.id_libelle = tell.ref_libelle
        WHERE tell.ref_langue = ?
        """, LANGUE_FR)
                .map(r -> new ReferenceService.ReferenceItem(
                        r.get("ref_type_essence", Integer.class),
                        r.get("libelle", String.class)
                ));
    }

    public List<ReferenceService.ReferenceItem> findAllGearboxTypes() {
        return dsl.fetch("""
        SELECT bdvll.ref_boite_de_vitesse, l.libelle
        FROM boite_de_vitesse_libelle_langue bdvll
        JOIN libelle l ON l.id_libelle = bdvll.ref_libelle
        WHERE bdvll.ref_langue = ?
        """, LANGUE_FR)
                .map(r -> new ReferenceService.ReferenceItem(
                        r.get("ref_boite_de_vitesse", Integer.class),
                        r.get("libelle", String.class)
                ));
    }

    public List<ReferenceService.ReferenceItem> findAllWineTypes() {
        return dsl.fetch("""
        SELECT tdvll.ref_type_de_vin, l.libelle
        FROM type_de_vin_libelle_langue tdvll
        JOIN libelle l ON l.id_libelle = tdvll.ref_libelle
        WHERE tdvll.ref_langue = ?
        """, LANGUE_FR)
                .map(r -> new ReferenceService.ReferenceItem(
                        r.get("ref_type_de_vin", Integer.class),
                        r.get("libelle", String.class)
                ));
    }
    public List<ReferenceService.ReferenceItem> findAllGameTypes() {
        return dsl.fetch("""
        SELECT tdjll.ref_type_de_jeux, l.libelle
        FROM type_de_jeux_libelle_langue tdjll
        JOIN libelle l ON l.id_libelle = tdjll.ref_libelle
        WHERE tdjll.ref_langue = ?
        """, LANGUE_FR)
                .map(r -> new ReferenceService.ReferenceItem(
                        r.get("ref_type_de_jeux", Integer.class),
                        r.get("libelle", String.class)
                ));
    }
    public List<ReferenceService.ReferenceItem> findAllScreenTypes() {
        return dsl.fetch("""
        SELECT tell.ref_type_ecran, l.libelle
        FROM type_ecran_libelle_langue tell
        JOIN libelle l ON l.id_libelle = tell.ref_libelle
        WHERE tell.ref_langue = ?
        """, LANGUE_FR)
                .map(r -> new ReferenceService.ReferenceItem(
                        r.get("ref_type_ecran", Integer.class),
                        r.get("libelle", String.class)
                ));
    }// ── Catégories ───────────────────────────────────────────────────────────

    @Cacheable("categories")
    public List<CategorieLabel> findCategoriesByLangue(int refLangue) {
        return dsl.select()
                .from(table("categorie_libelle_langue"))
                .where(field("ref_langue").eq(refLangue))
                .fetch(r -> new CategorieLabel(
                        r.get(field("ref_categorie", Integer.class)),
                        r.get(field("ref_libelle", Long.class)),
                        r.get(field("ref_langue", Integer.class))  // ✅ Integer.class pas 1
                ));
    }

    // ✅ Surcharge avec valeur par défaut FR
    public List<CategorieLabel> findCategoriesByLangue() {
        return findCategoriesByLangue(LANGUE_FR);
    }

    @Cacheable("main-categories")
    public List<MainCategorieLabel> findMainCategoriesByLangue(int refLangue) {
        return dsl.select()
                .from(table("main_categorie_libelle_langue"))
                .where(field("ref_langue").eq(refLangue))
                .fetch(r -> new MainCategorieLabel(
                        r.get(field("ref_main_categorie", Integer.class)),
                        r.get(field("ref_libelle", Long.class)),
                        r.get(field("ref_langue", Integer.class))  // ✅ parenthèse fermée correctement
                ));
    }

    @Cacheable("cantons")
    public List<Canton> findAllCantons() {
        return dsl.select()
                .from(table("canton_fr"))
                .orderBy(field("nom").asc())
                .fetch(r -> new Canton(
                        r.get(field("id_canton", Integer.class)),
                        r.get(field("nom", String.class))
                ));
    }

    @Cacheable("departements")
    public List<Departement> findAllDepartements() {
        return dsl.select()
                .from(table("departement"))
                .orderBy(field("code").asc())
                .fetch(r -> new Departement(
                        r.get(field("id_departement", Integer.class)),
                        r.get(field("nom", String.class)),
                        r.get(field("code", String.class))
                ));
    }

    @Cacheable("pays-present")
    public List<PaysPresent> findPaysPresentActifs() {
        return dsl.select()
                .from(table("pays_present"))
                .orderBy(field("nom").asc())
                .fetch(r -> new PaysPresent(
                        r.get(field("id_pays_present", Integer.class)),
                        r.get(field("nom", String.class))
                ));
    }

    // ── Vin ──────────────────────────────────────────────────────────────────

    @Cacheable("regions-vin")
    public List<PaysRegionVin> findPaysRegionVins() {
        return dsl.select()
                .from(table("pays_region_vin"))
                .fetch(r -> new PaysRegionVin(
                        r.get(field("id_pays_region_vin", Integer.class)),
                        r.get(field("ref_pays", Integer.class)),
                        r.get(field("parent_id", Integer.class)),
                        r.get(field("nom", String.class))
                ));
    }

    @Cacheable("cepages")
    public List<Cepage> findCepagesByRegion(int refPaysRegionVin) {
        return dsl.select()
                .from(table("cepage"))
                .where(field("ref_pays_region_vin").eq(refPaysRegionVin))
                .orderBy(field("nom").asc())
                .fetch(r -> new Cepage(
                        r.get(field("id_cepage", Integer.class)),
                        r.get(field("ref_pays_region_vin", Integer.class)),
                        r.get(field("ref_type_de_vin", Integer.class)),
                        r.get(field("nom", String.class))
                ));
    }

    public List<MainCategorieLabel> findMainCategoriesByLangue() {
        return findMainCategoriesByLangue(LANGUE_FR);
    }

    @Cacheable("subcategories")
    public List<SubcategorieLabel> findSubcategoriesByCategorie(int refCategorie, int refLangue) {
        return dsl.select()
                .from(table("subcategorie_libelle_langue"))
                .where(field("ref_categorie").eq(refCategorie))
                .and(field("ref_langue").eq(refLangue))
                .fetch(r -> new SubcategorieLabel(
                        r.get(field("ref_subcategorie", Integer.class)),
                        r.get(field("ref_categorie", Integer.class)),
                        r.get(field("ref_libelle", Long.class)),
                        r.get(field("ref_langue", Integer.class))  // ✅
                ));
    }

    public List<SubcategorieLabel> findSubcategoriesByCategorie(int refCategorie) {
        return findSubcategoriesByCategorie(refCategorie, LANGUE_FR);
    }

// ── Types essence ─────────────────────────────────────────────────────────

    @Cacheable("types-essence")
    public List<TypeEssence> findTypesEssence(int refLangue) {
        return dsl.select()
                .from(table("type_essence_libelle_langue"))
                .where(field("ref_langue").eq(refLangue))
                .fetch(r -> new TypeEssence(
                        r.get(field("ref_type_essence", Integer.class)),
                        r.get(field("ref_libelle", Long.class)),
                        r.get(field("ref_langue", Integer.class))  // ✅
                ));
    }

    public List<TypeEssence> findTypesEssence() {
        return findTypesEssence(LANGUE_FR);
    }

// ── Conditions livraison et payement — supprimer le refLangue = 1 hardcodé ──

    @Cacheable("conditions-livraison")
    public List<ConditionLivraison> findConditionsLivraison(int refLangue) {
        return dsl.select()   // ✅ supprimé refLangue = 1 (mauvaise pratique)
                .from(table("condition_livraison_libelle_langue"))
                .where(field("ref_langue").eq(refLangue))
                .fetch(r -> new ConditionLivraison(
                        r.get(field("ref_mode_de_livraison", Integer.class)),
                        r.get(field("ref_libelle", Long.class)),
                        r.get(field("ref_langue", Integer.class)),
                        r.get(field("frais", java.math.BigDecimal.class))
                ));
    }

    public List<ConditionLivraison> findConditionsLivraison() {
        return findConditionsLivraison(LANGUE_FR);
    }

    @Cacheable("conditions-payement")
    public List<ConditionPayement> findConditionsPayement(int refLangue) {
        return dsl.select()   // ✅ supprimé refLangue = 1 hardcodé
                .from(table("condition_payement_libelle_langue"))
                .where(field("ref_langue").eq(refLangue))
                .fetch(r -> new ConditionPayement(
                        r.get(field("ref_condition_payement", Integer.class)),
                        r.get(field("ref_libelle", Long.class)),
                        r.get(field("ref_langue", Integer.class))
                ));
    }

    public List<ConditionPayement> findConditionsPayement() {
        return findConditionsPayement(LANGUE_FR);
    }

    @Cacheable("boites-vitesse")
    public List<BoiteDeVitesse> findBoitesDeVitesse(int refLangue) {
        return dsl.select()   // ✅ supprimé refLangue = 1 hardcodé
                .from(table("boite_de_vitesse_libelle_langue"))
                .where(field("ref_langue").eq(refLangue))
                .fetch(r -> new BoiteDeVitesse(
                        r.get(field("ref_boite_de_vitesse", Integer.class)),
                        r.get(field("ref_libelle", Long.class)),
                        r.get(field("ref_langue", Integer.class))
                ));
    }

    public List<BoiteDeVitesse> findBoitesDeVitesse() {
        return findBoitesDeVitesse(LANGUE_FR);
    }

    @Cacheable("genres")
    public List<Genre> findAllGenres() {
        return dsl.select()
                .from(table("genre"))
                .orderBy(field("genre").asc())
                .fetch(r -> new Genre(
                        r.get(field("id_genre", Integer.class)),
                        r.get(field("genre",    String.class))
                ));
    }
    public void enregistrerDemandeVisite(
            int refArticle,
            Integer refVendeur,
            String email,
            String telephone,
            String nom,
            String prenom,
            String adresse,
            String commentaire,
            LocalDateTime dateVisite
                ) {
        dsl.execute("""
        INSERT INTO demande_visite (
            ref_article,
            ref_vendeur,
            email,
            tel_mobile,
            nom,
            prenom,
            adresse,
            commentaire,
            date_visite,    
            date_visite_fin
            
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """,
                refArticle,
                refVendeur,
                email,
                telephone,
                nom,
                prenom,
                adresse,
                commentaire,
                dateVisite,
                dateVisite.plusHours(1)
        );
    }
}