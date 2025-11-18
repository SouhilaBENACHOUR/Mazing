package fr.ubordeaux.ao.project.model.patterns.observer; // <-- CORRECT (adds ".model")import model.Game;

import fr.ubordeaux.ao.project.model.Game;

/**
 * L'interface "Observer" (Observateur).
 * (Responsabilité : Personne 1)
 * * Rôle : Définit le contrat que la Vue (GameView) doit implémenter
 * pour pouvoir être notifiée par le Modèle (Game).
 */
public interface GameObserver {
    /**
     * Méthode appelée par le Game (le Sujet) lorsque son état change.
     * @param game Le modèle de jeu avec les nouvelles données à afficher.
     */
    void onGameUpdate(Game game);
}