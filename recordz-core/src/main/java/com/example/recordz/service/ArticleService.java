package com.example.recordz.service;

import com.example.recordz.model.domain.Article;
import com.example.recordz.model.domain.FiltreArticle;
import com.example.recordz.model.domain.Personne;
import com.example.recordz.repository.ArticleRepository;
import org.jooq.impl.QOM;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Logique métier pour la gestion des articles (annonces marketplace).
 */
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final PersonneService personneService;

    public ArticleService(ArticleRepository articleRepository,PersonneService personneService) {
        this.articleRepository = articleRepository;
        this.personneService = personneService;
    }
    @Transactional(readOnly = true)
    public Personne findPersonneById(Long id) {
        return personneService.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }
    @Transactional(readOnly = true)
    public Article findByIdArticle(Long id) {
        return articleRepository.findByIdArticle(id);
    }
    @Transactional(readOnly = true)
    public int countByCategorie(int refCategorie) {
        return articleRepository.countByCategorie(refCategorie);
    }

    @Transactional
    public void insertAPaye(
            Integer refArticle,
            Integer refVendeur,
            Long refAcheteur,
            BigDecimal montant,
            LocalDateTime dateFermetureEnchere,
            Integer refModeDeLivraison,
            Integer refConditionPayement,
            Integer quantite,
            Integer refCanton
    ) {
        this.articleRepository.insertAPaye(
                refArticle,
                refVendeur,
                refAcheteur,
                montant,
                dateFermetureEnchere,
                refModeDeLivraison,
                refConditionPayement,
                quantite,
                refCanton
        ); // ← parenthèse ) et non accolade }
    }  // ← plus de point-virgule parasite ici


    @Transactional
    public void updateStatutEstLivre(Integer idArticle, Article article, Personne  personne) {
        this.articleRepository.updateStatutEstLivre(idArticle, article, personne);
    }

    @Transactional
    public void updateStatutEstLivreParPoste(Integer idArticle, Article article, Personne  personne) {
        this.articleRepository.updateStatutEstLivre(idArticle, article, personne);
    }


    @Transactional
    public void updateFaitDeLapublicite(Integer idArticle) {
        this.articleRepository.updateFaitDeLapublicite(idArticle);
    }


    @Transactional
    public void updateStatutPaye(Integer idArticle, Article article) {
        this.articleRepository.updateStatutPaye(idArticle, article);
    }
    @Transactional(readOnly = true)
    public Integer findRefUtilisateurByEmail(String email) {
        return this.articleRepository.findRefUtilisateurByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Article> findByFiltres(String nom, String marque, Integer refCategorie,
                                       Integer refSousCategorie, Double prixMin, Double prixMax,
                                       boolean encheresOnly, int page, int pageSize) {
        return articleRepository.findByFiltres(
                nom, marque, refCategorie, refSousCategorie,
                prixMin, prixMax, encheresOnly,
                pageSize, (page - 1) * pageSize
        );
    }



    @Transactional(readOnly = true)
    public int countByFiltres(String nom, String marque, Integer refCategorie,
                              Integer refSousCategorie, Double prixMin, Double prixMax,
                              boolean encheresOnly) {
        return articleRepository.countByFiltres(
                nom, marque, refCategorie, refSousCategorie,
                prixMin, prixMax, encheresOnly
        );
    }

    @Transactional(readOnly = true)
    public List<Article> findAll(int page, int pageSize) {
        return articleRepository.findAll(pageSize, (page - 1) * pageSize);
    }

    @Transactional(readOnly = true)
    public List<Article> findAllTries(int page, int pageSize) {
        return articleRepository.findAllTries(pageSize, (page - 1) * pageSize);
    }

    @Transactional(readOnly = true)
    public int countAll() {
        return articleRepository.countAll();
    }

    @Transactional(readOnly = true)
    public List<Article> findActiveArticlesByVendeur(String email, int page, int pageSize) {
        return articleRepository.findActiveArticlesByVendeur(
                email, pageSize, (page - 1) * pageSize
        );
    }

    @Transactional(readOnly = true)
    public List<Article> findActiveArticlesByVendeurSansPub(String email, int page, int pageSize) {
        return articleRepository.findActiveArticlesByVendeurSansPub(
                email, pageSize, (page - 1) * pageSize
        );
    }

    @Transactional(readOnly = true)
    public int countAPayerByUsername(String  email) {
        return articleRepository.countAPayerByUsername(email);
    }

    @Transactional(readOnly = true)
    public int countMesArticlesVendus(String email) {
        return articleRepository.countArticlesVendus(email);
    }
    /**
     * FIX : signature harmonisée avec les autres méthodes (page, pageSize).
     * Le calcul de l'offset est fait ici, pas dans la vue.
     */
    @Transactional(readOnly = true)
    public List<Article> findArticleAttenteDePayement(String username, int page, int pageSize) {
        return articleRepository.findArticleAttenteDePayement(
                username, pageSize, (page - 1) * pageSize
        );
    }

    @Transactional(readOnly = true)
    public List<Article> findArticlesAchetesByVendeur(String email, int limit, int offset) {
        return articleRepository.findArticlesAchetesByVendeur(email, limit, offset);
    }

    @Transactional(readOnly = true)
    public List<Article> findArticlesByVendeurAndStatut(String email, int limit, int offset, int statut) {
        return articleRepository.findArticlesByVendeurAndStatut(email, limit, offset, statut);
    }

    @Transactional(readOnly = true)
    public List<Article> findArticlesAchetes(String email, int limit, int offset) {

        return articleRepository.findArticlesAchetes(email, limit, offset);
    }
    @Transactional(readOnly = true)
    public List<Article> findMesVendus (String email, int limit, int offset) {
        return articleRepository.findArticlesVendus(email, limit, offset);
    }
    /**
     * FIX : suppression du doublon countEnAttenteDePayementCounter.
     * Une seule méthode publique avec un nom clair.
     */
    @Transactional(readOnly = true)
    public int countAttentePaymentByUsername(String username) {
        return articleRepository.countEnAttenteDePayementCounter(username);
    }

    /**
     * FIX : countALivrerByUsername exposé dans le service (manquait).
     */
    @Transactional(readOnly = true)
    public int countALivrerByUsername(String username) {
        return articleRepository.countALivrerByUsername(username);
    }

    @Transactional(readOnly = true)
    public int countAllerChercherByUsername(String username) {
        return articleRepository.countAllerChercherByUsername(username);
    }

    /**
     * FIX : readOnly = true ajouté (méthode de lecture).
     *
     return articleRepository.findArticlesALivrer(email, page, pageSize);
     * Signature harmonisée : (email, page, pageSize) comme les autres.
     */
    @Transactional(readOnly = true)
    public List<Article> findArticlesALivrerEnAttente(String email, int page, int pageSize) {
        return articleRepository.findArticlesALivrerEnAttente(email, page, pageSize);
    }

    @Transactional(readOnly = true)
    public List<Article> findArticlesALivrerADomicile(String email, int page, int pageSize) {
        return articleRepository.findArticlesALivrerADomicile(email, page, pageSize);
    }

    @Transactional(readOnly = true)
    public List<Article>  findArticlesALivrerADomicilePourMoi(String email, int page, int pageSize){
        return articleRepository.findArticlesALivrerADomicilePourMoi(email, page, pageSize);
    }

    @Transactional(readOnly = true)
    public List<Article>  findArticlesALivrerPourMoiParPoste(String email, int page, int pageSize){
        return articleRepository.findArticlesALivrerPourMoiParPoste(email, page, pageSize);
    }

    @Transactional(readOnly = true)
    public List<Article>  findArticlesALivrerParPoste(String email, int page, int pageSize){
        return articleRepository.findArticlesALivrerParPoste(email, page, pageSize);
    }

    @Transactional(readOnly = true)
    public List<Article> findArticlesAllerChercher(String email, int limit, int offset) {
        return articleRepository.findArticlesAllerChercher(email, limit, offset);
    }

    // ✅ Service passe page et pageSize directement
    @Transactional(readOnly = true)
    public List<Article> findArticlesAPaye(String email, int page, int pageSize) {
        return articleRepository.findArticlesAPaye(email, page, pageSize);
    }
    @Transactional(readOnly = true)
    public int countActiveArticlesByVendeur(String email) {
        return articleRepository.countActiveArticlesByVendeur(email);
    }

    @Transactional(readOnly = true)
    public int countMesAchatsByVendeur(String email) {
        return articleRepository.countMesAchatsByVendeur(email);
    }
    /**
     * Charge un article et incrémente son compteur de visites.
     */
    @Transactional
    public Optional<Article> findByIdAndTrackVisit(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        article.ifPresent(a -> articleRepository.incrementVisites(id));
        return article;
    }

    @Transactional(readOnly = true)
    public int countByFiltresDynamiques(FiltreArticle filtre) {
        return articleRepository.countByFiltresDynamiques(filtre);
    }

    @Transactional(readOnly = true)
    public List<Article> findByCategorie(int refCategorie, int page, int pageSize) {
        return articleRepository.findByCategorie(refCategorie, pageSize, (page - 1) * pageSize);
    }

    @Transactional(readOnly = true)
    public List<Article> findBySousCategorie(int refCategorie, int refSousCategorie, int page, int pageSize) {
        return articleRepository.findBySousCategorie(refCategorie, refSousCategorie, pageSize, (page - 1) * pageSize);
    }

    @Transactional(readOnly = true)
    public List<Article> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return List.of();
        return articleRepository.search(keyword.trim(), 50);
    }

    @Transactional(readOnly = true)
    public List<Article> findByFiltresDynamiques(FiltreArticle filtre) {
        return articleRepository.findByFiltresDynamiques(filtre);
    }

    @Transactional(readOnly = true)
    public List<Article> findActiveAuctions() {
        return articleRepository.findActiveAuctions();
    }

    @Transactional(readOnly = true)
    public List<Article> findByVendeur(Long refVendeur) {
        return articleRepository.findByVendeur(refVendeur);
    }

    /**
     * Publie une nouvelle annonce après validation des champs obligatoires.
     */
    @Transactional
    public Long publish(Article article) {
        validate(article);
        return articleRepository.insert(article);
    }

    /**
     * Marque un article comme vendu.
     */
    @Transactional
    public void markAsSold(Long id) {
        articleRepository.markAsSold(id);
    }

    @Transactional
    public void confirmerReception (Integer  idArticle, Integer refAchteur) {
        articleRepository.confirmerReception(idArticle, refAchteur);
    }



    private void validate(Article a) {
        if (a.getNom() == null || a.getNom().isBlank())
            throw new IllegalArgumentException("Le nom de l'article est obligatoire");
        if (a.getPrix() == null || a.getPrix() < 0)
            throw new IllegalArgumentException("Le prix doit être positif");
        if (a.getRefCategorie() == null)
            throw new IllegalArgumentException("La catégorie est obligatoire");
        if (a.getMarque() == null || a.getMarque().isBlank())
            throw new IllegalArgumentException("La marque est obligatoire");
        if (a.getRefConditionPayement() == null)
            throw new IllegalArgumentException("La condition de paiement est obligatoire");
        if (a.getRefModeDeLivraison() == null)
            throw new IllegalArgumentException("Le mode de livraison est obligatoire");
    }
}
