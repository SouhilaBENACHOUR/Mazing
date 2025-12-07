package fr.ubordeaux.ao.project;

import fr.ubordeaux.ao.project.model.Game;
import fr.ubordeaux.ao.project.view.GameView;
import fr.ubordeaux.ao.project.controller.GameController;
import fr.ubordeaux.ao.project.controller.KeyboardController;
import fr.ubordeaux.ao.project.controller.GameLoop;
import fr.ubordeaux.ao.mazing.api.IWindowGame;
import fr.ubordeaux.ao.mazing.api.WindowGame;

import javax.swing.*;
import java.awt.Color;

public class Main {


    private static int currentLevel = 1;
    private static Game gameModel;
    private static GameView gameView;
    private static IWindowGame windowGame;

    public static void main(String[] args) {
        windowGame = new WindowGame();
        windowGame.setTileSize(55);
        ((JFrame) windowGame).setSize(1400, 1200);

        gameModel = new Game();
        gameView = new GameView(windowGame);
        KeyboardController keyboardController = new KeyboardController(gameModel);
        GameLoop gameLoop = new GameLoop(gameModel, gameView);
        GameController gameController = new GameController(gameView, keyboardController);

        gameModel.addObserver(gameView);

        startNewGame();

        windowGame.setVisible(true);

        Thread gameThread = new Thread(gameLoop);
        gameThread.start();

        startLevelProgressionChecker();

        System.out.println("Jeu démarré. Bonne chance !");
    }

    private static void loadLevel(int level) {
        String levelFile = "level" + level + ".json";

        try {

            windowGame.clear();
            gameView.clearEnemies();
            gameModel.setCurrentLevel(level);
            gameModel.loadLevel(levelFile);

            while (gameModel.isLoading()) {
                Thread.sleep(50);
            }

            switch (level) {
                case 1 -> windowGame.setTileSize(55);
                case 2 -> windowGame.setTileSize(50);
                case 3 -> windowGame.setTileSize(40);
            }

            if (gameModel.getMaze() != null) {

                gameView.drawMaze(gameModel.getMaze());
                gameView.drawItems(gameModel.getMaze());
            }

            System.out.println("Chargement du niveau " + level + " terminé");

        } catch (Exception e) {
            System.err.println("Erreur : Impossible de charger " + levelFile);
            e.printStackTrace();
        }
    }
    private static void startNewGame() {
        System.out.println("Démarrage d’une nouvelle partie...");
        currentLevel = 1;
        gameModel.resetLevel();
        loadLevel(currentLevel);
        System.out.println("Nouvelle partie prête !");
    }

    private static void startLevelProgressionChecker() {
        Thread progressionThread = new Thread(() -> {
            System.out.println("Thread de progression démarré");

            while (true) {
                try {
                    Thread.sleep(300);

                    boolean levelComplete = gameModel.isLevelComplete();
                    boolean loading = gameModel.isLoading();

                    if (levelComplete && !loading) {
                        System.out.println("\nNIVEAU COMPLET DÉTECTÉ !");
                        gameModel.setLoading(true);

                        currentLevel++;
                        if (currentLevel <= 3) {
                            SwingUtilities.invokeLater(() ->
                                    gameView.getHudRenderer().showTemporaryMessage("NEXT LEVEL", Color.CYAN));
                            Thread.sleep(2000);
                            SwingUtilities.invokeLater(() ->
                                    gameView.getHudRenderer().hideMessage());

                            System.out.println("Chargement du niveau " + currentLevel + "...\n");
                            loadLevel(currentLevel);

                            gameModel.setLoading(false);
                            System.out.println("Niveau " + currentLevel + " prêt\n");
                        } else {
                            showFinalVictoryScreen();
                            Thread.sleep(2000);
                            startNewGame();
                        }
                    }

                    if (gameModel.isGameOver()) {
                        System.out.println("\nGAME OVER\n");
                        SwingUtilities.invokeLater(() ->
                                gameView.getHudRenderer().showTemporaryMessage("GAME OVER", Color.RED));
                        Thread.sleep(2000);
                        startNewGame();
                    }

                } catch (InterruptedException e) {
                    System.err.println("Thread interrompu");
                    break;
                } catch (Exception e) {
                    System.err.println("ERREUR dans levelProgressionChecker: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        });

        progressionThread.setDaemon(true);
        progressionThread.start();
    }

    private static void showFinalVictoryScreen() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    null,
                    "FÉLICITATIONS !\n\nVous avez terminé les 3 niveaux !\n\nScore final : " + gameModel.getScore(),
                    "VICTOIRE !",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

}
