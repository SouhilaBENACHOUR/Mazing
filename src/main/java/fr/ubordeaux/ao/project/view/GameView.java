package fr.ubordeaux.ao.project.view;

import fr.ubordeaux.ao.project.model.Game;
import fr.ubordeaux.ao.project.model.GameConfig;
import fr.ubordeaux.ao.project.model.Maze;
import fr.ubordeaux.ao.project.model.entities.Enemy;
import fr.ubordeaux.ao.project.model.entities.Player;
import fr.ubordeaux.ao.project.model.patterns.observer.GameObserver;
import fr.ubordeaux.ao.project.controller.KeyboardController;

import fr.ubordeaux.ao.mazing.api.Crusader;
import fr.ubordeaux.ao.mazing.api.IWindowGame;

import javax.swing.JFrame;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vue du jeu avec Mazing.
 * (Responsabilité : Personne 3)
 */
public class GameView implements GameObserver {

    private IWindowGame windowGame;
    private HudRenderer hudRenderer;
    private Crusader crusaderSprite;
    private Map<Enemy, EnemyView> enemyViews = new HashMap<>();

    public GameView(IWindowGame windowGame) {
        this.windowGame = windowGame;

        this.crusaderSprite = new Crusader();
        this.windowGame.add(crusaderSprite);

        this.hudRenderer = new HudRenderer(windowGame);
    }

    /**
     * Méthode de l'interface GameObserver.
     * Appelée à chaque "tick" par la GameLoop.
     */
    @Override
    public void onGameUpdate(Game game) {
        drawPlayer(game.getPlayer());
        drawEnemies(game.getEnemies());
        hudRenderer.draw(game);
    }

    /**
     * Attache le KeyListener au JFrame.
     */
    public void addKeyListener(KeyboardController controller) {
        ((JFrame) this.windowGame).addKeyListener(controller);
    }

    /**
     * Met à jour la position et l'animation du joueur.
     */
    private void drawPlayer(Player player) {
        if (player == null || crusaderSprite == null) return;

        float x = (float) player.getPosition().getX();
        float y = (float) player.getPosition().getY();
        float z = 0.1f + (enemyViews.size() * 0.001f);

        crusaderSprite.setPosition(x, y, z);
        crusaderSprite.setDirection(player.getDirection().toString());

        if (!player.isAlive()) {
            crusaderSprite.setMode(Crusader.Mode.DEATH);
        } else if (player.isMoving()) {
            crusaderSprite.setMode(Crusader.Mode.WALK);
        } else {
            crusaderSprite.setMode(Crusader.Mode.IDLE);
        }
    }

    /**
     * Met à jour les EnemyView pour tous les ennemis.
     */
    private void drawEnemies(List<Enemy> enemies) {
        if (enemies == null) return;

        // Crée les EnemyView manquants
        for (Enemy enemy : enemies) {
            if (!enemyViews.containsKey(enemy)) {
                EnemyView view = new EnemyView(enemy);
                enemyViews.put(enemy, view);
                windowGame.add(view);
            }
        }

        // Met à jour la position de tous les EnemyView existants
        int index = 0;
        for (Enemy enemy : enemies) {
            EnemyView view = enemyViews.get(enemy);
            if (view != null) {
                float x = enemy.getPosition().getX();
                float y = enemy.getPosition().getY();
                float z = 0.1f + index * 0.001f; // couche différente

                view.setPosition(x, y, z);
            }
            index++;
        }

        // Nettoie les vues inutiles
        enemyViews.keySet().removeIf(e -> !enemies.contains(e));
    }

    /**
     * Dessine le labyrinthe statique (murs et sol).
     */
    public void drawMaze(Maze maze) {
        if (maze == null) {
            System.err.println("Erreur : Tentative de dessiner un labyrinthe null.");
            return;
        }

        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                char tile = maze.getTile(x, y);

                int code;
                if (tile == GameConfig.LEVEL_WALL) {
                    code = getWallCode(x, y, maze);
                } else {
                    code = GameConfig.FLOOR_CODE;
                }

                windowGame.add(code, x, y, 0); // z=0 (le sol)
            }
        }
    }

    /**
     * Calcule le code de sprite de mur pour l'auto-tiling.
     */
    private int getWallCode(int x, int y, Maze maze) {
        boolean wallLeft = (x > 0) && maze.getTile(x - 1, y) == GameConfig.LEVEL_WALL;
        boolean wallRight = (x < maze.getWidth() - 1) && maze.getTile(x + 1, y) == GameConfig.LEVEL_WALL;
        boolean wallUp = (y > 0) && maze.getTile(x, y - 1) == GameConfig.LEVEL_WALL;
        boolean wallDown = (y < maze.getHeight() - 1) && maze.getTile(x, y + 1) == GameConfig.LEVEL_WALL;

        int horizontal = 0;
        int vertical = 0;

        if (wallLeft) horizontal++;
        if (wallRight) horizontal++;
        if (wallUp) vertical++;
        if (wallDown) vertical++;

        int codeHorizontal = 129;
        int codeVertical = 131;

        if (horizontal > vertical) {
            return codeHorizontal;
        } else if (vertical > horizontal) {
            return codeVertical;
        } else {
            if (y == 0 || y == maze.getHeight() - 1) {
                return codeHorizontal;
            } else if (x == 0 || x == maze.getWidth() - 1) {
                return codeVertical;
            } else {
                return codeHorizontal;
            }
        }
    }
}
