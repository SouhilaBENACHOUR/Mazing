package fr.ubordeaux.ao.project.controller;

import fr.ubordeaux.ao.project.model.Game;
import fr.ubordeaux.ao.project.view.GameView;

/**
 * Fichier "Bouchon" (Stub) pour la Personne 3.
 * Gère la boucle de jeu principale.
 * * Rôle : Implémente Runnable pour tourner sur un Thread séparé.
 * * Appelle game.updateGame() à un rythme constant (ex: 60 FPS).
 */
public class GameLoop implements Runnable {

    private Game game;
    private GameView gameView; // La vue (pour l'instant non utilisée ici)
    private boolean isRunning;

    // Vitesse du jeu: 60 "ticks" par seconde
    private static final int TARGET_FPS = 60;
    private static final long OPTIMAL_TIME = 1000 / TARGET_FPS; // (en millisecondes)

    public GameLoop(Game game, GameView gameView) {
        this.game = game;
        this.gameView = gameView;
        this.isRunning = true;
    }

    /**
     * C'est la méthode qui est exécutée par le Thread.
     */
    @Override
    public void run() {

        while (isRunning) {

            // --- 1. Mettre à jour la logique du jeu ---
            // (Ceci va appeler checkCollisions, updateEnemies, etc.
            // et notifier la GameView grâce au pattern Observer)
            game.updateGame();


            // --- 2. Pause ---
            // Contrôle la vitesse du jeu pour qu'il ne tourne pas
            // des milliers de fois par seconde.
            try {
                Thread.sleep(OPTIMAL_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
                isRunning = false;
            }
        }
    }
}