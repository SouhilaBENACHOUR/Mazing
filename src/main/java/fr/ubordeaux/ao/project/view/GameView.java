package fr.ubordeaux.ao.project.view;

import fr.ubordeaux.ao.project.model.Game;
import fr.ubordeaux.ao.project.model.GameConfig;
import fr.ubordeaux.ao.project.model.Maze;
import fr.ubordeaux.ao.project.model.entities.Player;
import fr.ubordeaux.ao.project.model.patterns.observer.GameObserver;
import fr.ubordeaux.ao.project.controller.KeyboardController;

import fr.ubordeaux.ao.mazing.api.Crusader;
import fr.ubordeaux.ao.mazing.api.IWindowGame;

import javax.swing.JFrame;

/**
 * Vue du jeu avec Mazing.
 * (Responsabilité : Personne 3)
 */
public class GameView implements GameObserver {

    private IWindowGame windowGame;
    private HudRenderer hudRenderer;
    private Crusader crusaderSprite;

    // Pas besoin du drapeau 'isMazeDrawn'

    public GameView(IWindowGame windowGame) {
        this.windowGame = windowGame;

        this.crusaderSprite = new Crusader();
        this.windowGame.add(crusaderSprite);

        this.hudRenderer = new HudRenderer(windowGame);
    }

    /**
     * C'est la méthode de l'interface GameObserver (PLAYER-9).
     * Appelée à chaque "tick" par la GameLoop.
     */
    @Override
    public void onGameUpdate(Game game) {

        // --- MODIFICATION ---
        // onGameUpdate ne met à jour QUE les éléments dynamiques.
        // Le labyrinthe statique n'est plus dessiné ici.
        // --- FIN MODIFICATION ---

        drawPlayer(game.getPlayer());
        hudRenderer.draw(game);

        // (P3 devra ajouter le dessin des ennemis/items ici)
        // drawEnemies(game.getEnemies());
        // drawItems(game.getItems());
    }

    /**
     * Méthode pour attacher le KeyListener au JFrame.
     */
    public void addKeyListener(KeyboardController controller) {
        ((JFrame) this.windowGame).addKeyListener(controller);
    }

    /**
     * Met à jour la position du joueur (PLAYER-3).
     */
    private void drawPlayer(Player player) {
        if (player == null || crusaderSprite == null) return;

        // --- MODIFICATION ---
        // Le joueur est à Z=0, au même niveau que le sol.
        float x = (float) player.getPosition().getX();
        float y = (float) player.getPosition().getY();
        float z = 0.0f; // Au niveau du sol
        // --- FIN MODIFICATION ---

        crusaderSprite.setPosition(x, y, z);

        String apiDirection = player.getDirection().toString();
        crusaderSprite.setDirection(apiDirection);

        if (!player.isAlive()) {
            crusaderSprite.setMode(Crusader.Mode.DEATH);
        } else if (player.isMoving()) {
            crusaderSprite.setMode(Crusader.Mode.WALK);
        } else {
            crusaderSprite.setMode(Crusader.Mode.IDLE);
        }
    }

    /**
     * Dessine le labyrinthe statique (murs et sol).
     * (Logique fournie par P3)
     * --- MODIFICATION : Cette méthode est maintenant 'public' ---
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

                // Ajoute le sprite de décor à la scène
                windowGame.add(code, x, y, 0); // z=0 (le sol)
            }
        }
    }

    /**
     * Calcule le code de sprite de mur correct pour l'auto-tiling.
     * (Logique fournie par P3)
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
        int codeVertical = 131; // GameConfig.WALL_CODE

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
                return codeHorizontal; // Par défaut
            }
        }
    }
}