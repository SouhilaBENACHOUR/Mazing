package fr.ubordeaux.ao.project.controller;

import fr.ubordeaux.ao.project.view.GameView;

/**
 * Fichier "Bouchon" (Stub) pour la Personne 3.
 * Contrôleur principal qui assemble les autres composants.
 * * Rôle : Fait le lien entre la Vue et le Contrôleur Clavier.
 */
public class GameController {

    /**
     * Constructeur appelé par Main.java.
     * @param gameView La fenêtre de jeu (pour y attacher le clavier).
     * @param keyboardController Le gestionnaire de clavier.
     */
    public GameController(GameView gameView, KeyboardController keyboardController) {

        // Cette ligne est cruciale :
        // Elle dit à la fenêtre (GameView) d'écouter
        // les événements provenant du clavier (KeyboardController).
        gameView.addKeyListener(keyboardController);
    }
}