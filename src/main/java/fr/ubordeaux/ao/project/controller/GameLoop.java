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
    private GameView gameView;
    private boolean isRunning;
    private static final int TARGET_FPS = 60;
    private static final long OPTIMAL_TIME = 1000 / TARGET_FPS;

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
            game.updateGame();

            try {
                Thread.sleep(OPTIMAL_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
                isRunning = false;
            }
        }
    }
}