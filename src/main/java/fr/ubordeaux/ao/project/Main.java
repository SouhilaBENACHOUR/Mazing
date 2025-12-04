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

/**
 * Classe principale pour lancer le jeu.
 * Point d'entrée de l'application.
 *
 * @author Personne 3 (P3)
 * @version 1.0
 */
public class Main {

    private static int currentLevel = 1;
    private static Game gameModel;
    private static GameView gameView;
    private static IWindowGame windowGame;

    /**
     * Point d'entrée principal du jeu.
     * Initialise tous les composants MVC et démarre le jeu.
     *
     * @param args Arguments de ligne de commande (non utilisés)
     */
    public static void main(String[] args) {

        // 1. Créer la fenêtre Mazing
        windowGame = new WindowGame();
        windowGame.setTileSize(55);
        ((JFrame) windowGame).setSize(1400, 1200);

        // 2. Créer les composants MVC
        gameModel = new Game();
        gameView = new GameView(windowGame);
        KeyboardController keyboardController = new KeyboardController(gameModel);
        GameLoop gameLoop = new GameLoop(gameModel, gameView);
        GameController gameController = new GameController(gameView, keyboardController);

        // 3. Connecter le pattern Observer
        gameModel.addObserver(gameView);

        // 4. Charger le premier niveau
        loadLevel(1);

        // 5. Rendre la fenêtre visible
        windowGame.setVisible(true);

        // 6. Dessiner le labyrinthe initial
        if (gameModel.getMaze() != null) {
            gameView.drawMaze(gameModel.getMaze());
            gameView.drawItems(gameModel.getMaze());
        }

        // 7. Démarrer la boucle de jeu
        Thread gameThread = new Thread(gameLoop);
        gameThread.start();

        // 8. Démarrer la surveillance de progression
        startLevelProgressionChecker();

        System.out.println("Jeu démarré. Bonne chance !");
    }

    /**
     * Charge un niveau spécifique depuis un fichier JSON.
     * Ajuste la taille des tuiles selon le niveau.
     *
     * @param level Le numéro du niveau (1, 2 ou 3)
     */
    private static void loadLevel(int level) {
        String levelFile = "level" + level + ".json";

        try {
            gameModel.loadLevel(levelFile);

            // Attendre la fin du chargement
            while (gameModel.isLoading()) {
                Thread.sleep(50);
            }

            // Ajuster la taille des tuiles selon le niveau
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

    /**
     * Thread qui surveille la progression des niveaux.
     * Gère les transitions entre niveaux et la victoire finale.
     */
    private static void startLevelProgressionChecker() {
        Thread progressionThread = new Thread(() -> {

            while (true) {
                try {
                    Thread.sleep(300);

                    boolean levelComplete = gameModel.isLevelComplete();
                    boolean loading = gameModel.isLoading();

                    if (levelComplete && !loading) {

                        gameModel.setLoading(true);

                        int completedLevel = currentLevel;
                        currentLevel++;

                        if (currentLevel <= 3) {
                            // Passage au niveau suivant
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
                            // Victoire finale
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
        });

        progressionThread.setDaemon(true);
        progressionThread.start();
    }

    /**
     * Affiche la fenêtre de victoire finale avec le score.
     */
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