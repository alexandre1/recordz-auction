package com.example.recordz.scheduler;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.jooq.impl.DSL.*;

@Component
public class EnchereScheduler {

    private final DSLContext dsl;
    private final JavaMailSender mailSender;

    public EnchereScheduler(DSLContext dsl, JavaMailSender mailSender) {
        this.dsl = dsl;
        this.mailSender = mailSender;
    }

    @Scheduled(fixedDelay = 60_000)
    public void cloturerEncheresExpirees() {
        String now = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        // ── 1. Récupérer les enchères dont la date de fin correspond à maintenant ──
        var encheres = dsl.select(
                        field("met_en_vente.ref_article"),
                        field("met_en_vente.ref_vendeur"),
                        field("article.prix"),
                        field("article.nom"),
                        field("article.ref_condition_payement"),
                        field("article.ref_mode_de_livraison"),
                        field("article.enchere_date_fin")          // ✅ récupéré ici, pas besoin d'une 2e requête
                )
                .from(table("met_en_vente"))
                .join(table("article"))
                .on(field("met_en_vente.ref_article").eq(field("article.id_article")))
                .where(field("article.enchere_date_fin").like("%" + now + "%"))
                .and(field("article.enchere").eq(1))
                .and(field("article.vendu").eq(0))
                .fetch();

        for (Record enchere : encheres) {
            Integer refArticle             = enchere.get(field("met_en_vente.ref_article"),        Integer.class);
            Integer refVendeur             = enchere.get(field("met_en_vente.ref_vendeur"),         Integer.class);
            String  nomArticle             = enchere.get(field("article.nom"),                      String.class);
            Integer refConditionPayement   = enchere.get(field("article.ref_condition_payement"),   Integer.class);
            Integer refModeDeLivraison     = enchere.get(field("article.ref_mode_de_livraison"),    Integer.class);
            LocalDateTime enchéreDateFin   = enchere.get(field("article.enchere_date_fin"),         LocalDateTime.class);

            // ── 2. Récupérer le gagnant (enchère max) ──
            Record gagnant = dsl.select(
                            field("ref_enchereur"),
                            max(field("prix", Double.class)).as("max_prix")
                    )
                    .from(table("enchere"))
                    .where(field("ref_article").eq(refArticle))
                    .fetchOne();

            if (gagnant == null || gagnant.get(field("ref_enchereur")) == null) {
                // Aucune enchère placée — on marque l'article comme invendu
                dsl.update(table("article"))
                        .set(field("ref_statut"), 8)
                        .where(field("id_article").eq(refArticle))
                        .execute();
                continue;
            }

            Integer refAcheteur = gagnant.get(field("ref_enchereur"), Integer.class);

            Double prix = gagnant.get(field("max_prix"), Double.class);

            // ── 3. Récupérer les infos acheteur ──
            Record acheteur = dsl.select(
                            field("nom_utilisateur"),
                            field("email")
                    )
                    .from(table("personne"))
                    .where(field("id_personne").eq(refAcheteur))
                    .fetchOne();

            // ── 4. Récupérer les infos vendeur ──
            Record vendeur = dsl.select(
                            field("nom_utilisateur"),
                            field("email")
                    )
                    .from(table("personne"))
                    .where(field("id_personne").eq(refVendeur))
                    .fetchOne();

            if (acheteur == null || vendeur == null) continue;

            String emailAcheteur = acheteur.get(field("email"),            String.class);
            String emailVendeur  = vendeur.get(field("email"),             String.class);
            String nomAcheteur   = acheteur.get(field("nom_utilisateur"),  String.class);
            String nomVendeur    = vendeur.get(field("nom_utilisateur"),   String.class);

            // ── 5. Mettre à jour le statut de l'article ──
            dsl.update(table("article"))
                    .set(field("ref_statut"), 1) // vendu aux enchères
                    .set(field("vendu"),       1)
                    .where(field("id_article").eq(refArticle))
                    .execute();

            // ── 6. Insérer dans a_paye ──
            // ✅ On réutilise les données déjà récupérées à l'étape 1 — pas de requête supplémentaire
            System.out.println("ref_condition_payement : " + refConditionPayement);
            System.out.println("ref_mode_de_livraison  : " + refModeDeLivraison);

            dsl.insertInto(table("a_paye"))
                    .set(field("ref_article"),              refArticle)
                    .set(field("ref_vendeur"),              refVendeur)
                    .set(field("ref_acheteur"),             refAcheteur)
                    .set(field("ref_statut"),               8) // en attente de paiement
                    .set(field("date_fermeture_enchere"),   enchéreDateFin)
                    .set(field("ref_condition_payement"),   refConditionPayement)
                    .set(field("ref_mode_de_livraison"),    refModeDeLivraison)
                    .set(field("montant"),    prix)
                    .execute();

            // ── 7. Envoyer les emails ──
            envoyerEmailAcheteur(emailAcheteur, nomAcheteur, nomArticle);
            envoyerEmailVendeur(emailVendeur,   nomVendeur,  nomArticle, nomAcheteur);

            System.out.println("✅ Enchère clôturée — article: " + nomArticle
                    + " | gagnant: " + nomAcheteur
                    + " | vendeur: " + nomVendeur);
        }
    }

    // ── Email acheteur ────────────────────────────────────────────────────────
    private void envoyerEmailAcheteur(String email, String nom, String nomArticle) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setFrom("robot@avant-garde.ch");
        msg.setSubject("Vous avez gagné une enchère !");
        msg.setText("""
                Bonjour %s,
                
                Félicitations ! Vous avez remporté l'enchère pour l'article : %s
                
                Veuillez vous connecter à votre compte pour procéder au paiement.
                
                L'équipe Avant-Garde
                """.formatted(nom, nomArticle));
        try {
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("❌ Erreur envoi email acheteur : " + e.getMessage());
        }
    }

    // ── Email vendeur ─────────────────────────────────────────────────────────
    private void envoyerEmailVendeur(String email, String nomVendeur,
                                     String nomArticle, String nomAcheteur) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setFrom("robot@avant-garde.ch");
        msg.setSubject("Votre enchère a été remportée !");
        msg.setText("""
                Bonjour %s,
                
                Votre article "%s" a été remporté par %s.
                
                Vous serez notifié dès que le paiement sera effectué.
                
                L'équipe Avant-Garde
                """.formatted(nomVendeur, nomArticle, nomAcheteur));
        try {
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("❌ Erreur envoi email vendeur : " + e.getMessage());
        }
    }


}
