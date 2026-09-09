package com.example.recordz.service;

import com.example.recordz.model.domain.Article;
import com.example.recordz.model.domain.Enchere;
import com.example.recordz.repository.ArticleRepository;
import com.example.recordz.repository.EnchereRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class EnchereService {

    private final EnchereRepository enchereRepository;
    private final ArticleRepository  articleRepository;

    public EnchereService(EnchereRepository enchereRepository,
                          ArticleRepository articleRepository) {
        this.enchereRepository = enchereRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional(readOnly = true)
    public List<Enchere> getEncheresForArticle(Long refArticle) {
        return enchereRepository.findByArticle(refArticle);
    }

    /**
     * Retourne la meilleure (plus haute) enchère pour un article.
     * Utilisé dans EncheresView pour afficher l'offre courante.
     */
    @Transactional(readOnly = true)
    public Optional<Enchere> getMeilleureEnchere(Long refArticle) {
        if (refArticle == null) return Optional.empty();
        return enchereRepository.findHighestBid(refArticle);
    }

    @Transactional(readOnly = true)
    public List<Enchere> findAllEnchere(int page, int pageSize, String email) {
        return enchereRepository.findAllByVendeur(pageSize, (page - 1) * pageSize, email);
    }

    public int countAllByVendeur(String email) {
        return enchereRepository.countAllByVendeur(email);
    }

    @Transactional(readOnly = true)
    public List<Enchere> getEncheresForVendeur(String email) {
        return enchereRepository.findAllByEnchereur(email);
    }
    /**
     * Place une enchère après validation :
     *  - l'article doit être en mode enchère
     *  - le montant doit dépasser la meilleure offre actuelle
     *  - l'article ne doit pas être déjà vendu
     */
    @Transactional
    public void placerEnchere(Long refArticle, Long refEnchereur, BigDecimal montant) {
        Article article = articleRepository.findById(refArticle)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Article introuvable : " + refArticle));

        if (article.getEnchere() == null || article.getEnchere() != 1) {
            throw new IllegalStateException("Cet article n'est pas en mode enchère.");
        }

        if (article.getVendu() != null && article.getVendu() == 1) {
            throw new IllegalStateException("Cet article est déjà vendu.");
        }

        Optional<Enchere> meilleure = enchereRepository.findHighestBid(refArticle);
        if (meilleure.isPresent()
                && montant.compareTo(meilleure.get().getPrix()) <= 0) {
            throw new IllegalArgumentException(
                    "Le montant doit être supérieur à l'enchère actuelle de "
                            + meilleure.get().getPrix() + " CHF");
        }

        enchereRepository.save(refArticle, refEnchereur, montant);
    }

    @Transactional(readOnly = true)
    public int getNombreEncheresForArticle(Long refArticle) {
        return enchereRepository.countByArticle(refArticle);
    }
}
