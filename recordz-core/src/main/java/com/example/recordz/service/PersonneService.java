package com.example.recordz.service;

import com.example.recordz.model.domain.Personne;
import com.example.recordz.repository.PersonneRepository;
import jdk.jfr.TransitionTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonneService {

    private final PersonneRepository personneRepository;

    public PersonneService(PersonneRepository personneRepository) {

        this.personneRepository = personneRepository;
    }


    @Transactional(readOnly = true)
    public Optional<Personne> findByEmail(String email) {
        return personneRepository.findByEmail(email);
    }

    /**
     * Appelé par CustomOAuth2UserService lors de la connexion Google.
     * Crée le compte si nécessaire (upsert).
     */
    @Transactional
    public Personne syncOAuth2User(String email, String displayName) {
        return personneRepository.upsertFromOAuth(email, displayName);
    }
    /**
     * Met à jour le profil d'une personne existante.
     * Appelé depuis ProfilView.
     */
    @Transactional
    public void updateProfil(Personne personne) {
        if (personne.getIdPersonne() == null) {
            throw new IllegalArgumentException("Impossible de mettre à jour : id_personne manquant.");
        }
        personneRepository.update(personne);
    }

    @Transactional(readOnly = true)
    public Personne findByNomUtilisateur(String username) {
        return personneRepository.findByNomUtilisateur(username)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Personne findById(Long id) {
        return personneRepository.findById(id).orElse(null);
    }

}
