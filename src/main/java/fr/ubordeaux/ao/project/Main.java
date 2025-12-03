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

        loadLevel(1);

        windowGame.setVisible(true);

        if (gameModel.getMaze() != null) {
            gameView.drawMaze(gameModel.getMaze());
            gameView.drawItems(gameModel.getMaze());
        }

        Thread gameThread = new Thread(gameLoop);
        gameThread.start();

        startLevelProgressionChecker();

        System.out.println("Jeu démarré. Bonne chance !");
    }

    private static void loadLevel(int level) {
        String levelFile = "level" + level + ".json";

        try {
            gameModel.loadLevel(levelFile);

            while (gameModel.isLoading()) {
                Thread.sleep(50);
            }

            switch (level) {
                case 1:
                    windowGame.setTileSize(55);
                    break;
                case 2:
                    windowGame.setTileSize(50);
                    break;
                case 3:
                    windowGame.setTileSize(40);
                    break;
            }

            System.out.println("Chargement du niveau " + level + " terminé");

        } catch (Exception e) {
            System.err.println("Erreur : Impossible de charger " + levelFile);
            e.printStackTrace();
        }
    }

    private static void startLevelProgressionChecker() {
        Thread progressionThread = new Thread(() -> {

            while (true) {
                try {
                    Thread.sleep(300);

                    // DEBUG DÉTAILLÉ
                    boolean levelComplete = gameModel.isLevelComplete();
                    boolean loading = gameModel.isLoading();

                    // Afficher l'état toutes les 3 secondes environ
                    if (System.currentTimeMillis() % 3000 < 300) {
                        System.out.println("DEBUG CHECK: currentLevel=" + currentLevel +
                                ", isLevelComplete=" + levelComplete +
                                ", isLoading=" + loading);
                    }

                    if (levelComplete && !loading) {

                        System.out.println("!!! NIVEAU COMPLET DÉTECTÉ !!!");
                        System.out.println("DEBUG: currentLevel AVANT incrémentation = " + currentLevel);

                        gameModel.setLoading(true);

                        int completedLevel = currentLevel;
                        currentLevel++;

                        System.out.println("DEBUG: currentLevel APRÈS incrémentation = " + currentLevel);
                        System.out.println("DEBUG: Test condition: currentLevel <= 3 ? " + (currentLevel <= 3));

                        if (currentLevel <= 3) {
                            System.out.println(">>> BRANCHE: Passage au niveau suivant <<<");
                            System.out.println("\n🎉 Niveau " + completedLevel + " terminé !");

                            SwingUtilities.invokeLater(() -> {
                                gameView.getHudRenderer().showTemporaryMessage("NEXT LEVEL ➡", Color.CYAN);
                            });

                            Thread.sleep(2000);

                            SwingUtilities.invokeLater(() -> {
                                gameView.getHudRenderer().hideMessage();
                            });

                            System.out.println("Chargement du niveau " + currentLevel + "...\n");

                            loadLevel(currentLevel);

                            if (gameModel.getMaze() != null) {
                                gameView.drawMaze(gameModel.getMaze());
                                gameView.drawItems(gameModel.getMaze());
                            }

                            gameModel.setLoading(false);

                        } else {
                            System.out.println(">>> BRANCHE: VICTOIRE FINALE <<<");
                            System.out.println("\n🏆 FÉLICITATIONS ! Vous avez terminé tous les niveaux ! 🏆\n");

                            SwingUtilities.invokeLater(() -> {
                                gameView.getHudRenderer().showTemporaryMessage("🏆 YOU WIN! 🏆", Color.YELLOW);
                            });

                            Thread.sleep(2000);

                            showFinalVictoryScreen();

                            break;
                        }
                    }

                    if (gameModel.isGameOver()) {
                        System.out.println("\n💀 GAME OVER 💀\n");
                        SwingUtilities.invokeLater(() -> {
                            gameView.getHudRenderer().showTemporaryMessage("GAME OVER", Color.RED);
                        });
                        break;
                    }

                } catch (InterruptedException e) {
                    break;
                } catch (Exception e) {
                    System.err.println("ERREUR dans levelProgressionChecker: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("Thread levelProgressionChecker terminé.");
        });

        progressionThread.setDaemon(true);
        progressionThread.start();
    }

    private static void showFinalVictoryScreen() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    null,
                    "🏆 FÉLICITATIONS !\n\nVous avez terminé les 3 niveaux !\n\nScore final : " + gameModel.getScore(),
                    "VICTOIRE !",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }
}