package fr.ubordeaux.ao.project;

import fr.ubordeaux.ao.project.model.Game;
import fr.ubordeaux.ao.project.model.GameConfig;
import fr.ubordeaux.ao.project.view.GameView;
import fr.ubordeaux.ao.project.controller.GameController;
import fr.ubordeaux.ao.project.controller.KeyboardController;
import fr.ubordeaux.ao.project.controller.GameLoop;
import fr.ubordeaux.ao.mazing.api.IWindowGame;
import fr.ubordeaux.ao.mazing.api.WindowGame;

import javax.swing.*;

/**
 * Classe principale pour lancer le jeu.
 */
public class Main {

    public static void main(String[] args) {

        // --- 1. Initialisation des Composants ---
        IWindowGame windowGame = new WindowGame();
        ((JFrame) windowGame).setSize(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

        Game gameModel = new Game();
        GameView gameView = new GameView(windowGame);
        KeyboardController keyboardController = new KeyboardController(gameModel);
        GameLoop gameLoop = new GameLoop(gameModel, gameView);
        GameController gameController = new GameController(gameView, keyboardController);

        // --- 2. Connexion des Composants (Pattern Observer) ---
        gameModel.addObserver(gameView);


        // --- 3. Démarrage du Jeu ---
        try {
            // Étape A: Charger la logique du jeu
            gameModel.loadLevel(GameConfig.LEVEL1_FILE);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERREUR : Impossible de charger le niveau initial.");
            return;
        }

        // Étape B: Rendre la fenêtre visible
        windowGame.setVisible(true);

        // --- MODIFICATION ICI ---
        // Étape C: Dessiner le décor statique (MAINTENANT que la fenêtre est visible)
        gameView.drawMaze(gameModel.getMaze());
        // --- FIN MODIFICATION ---

        // Étape D: Démarrer la boucle de jeu
        Thread gameThread = new Thread(gameLoop);
        gameThread.start();

        System.out.println("Jeu démarré. Bonne chance !");
    }
}