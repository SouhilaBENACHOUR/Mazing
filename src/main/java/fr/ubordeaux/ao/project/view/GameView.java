package fr.ubordeaux.ao.project.view;

import fr.ubordeaux.ao.mazing.api.Spider;
import fr.ubordeaux.ao.project.model.Game;
import fr.ubordeaux.ao.project.model.GameConfig;
import fr.ubordeaux.ao.project.model.Maze;
import fr.ubordeaux.ao.project.model.entities.Door;
import fr.ubordeaux.ao.project.model.entities.Enemy;
import fr.ubordeaux.ao.project.model.entities.Entity;
import fr.ubordeaux.ao.project.model.entities.Player;
import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.patterns.observer.GameObserver;
import fr.ubordeaux.ao.project.controller.KeyboardController;

import fr.ubordeaux.ao.mazing.api.Crusader;
import fr.ubordeaux.ao.mazing.api.IWindowGame;

import javax.swing.JFrame;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vue du jeu avec Mazing (partie View du MVC).
 * Responsable de l'affichage du labyrinthe, du joueur et du HUD.
 *
 * @author Personne 3 (P3)
 * @version 1.0
 */
public class GameView implements GameObserver {

    private IWindowGame windowGame;
    private HudRenderer hudRenderer;
    private Crusader crusaderSprite;
    private Map<Enemy, EnemyView> enemyViews = new HashMap<>();

    /**
     * Constructeur de la vue.
     *
     * @param windowGame La fenêtre Mazing pour l'affichage
     */
    public GameView(IWindowGame windowGame) {
        this.windowGame = windowGame;

        this.crusaderSprite = new Crusader();
        this.windowGame.add(crusaderSprite);

        this.hudRenderer = new HudRenderer(windowGame);
    }

    /**
     * Méthode du pattern Observer.
     * Appelée à chaque mise à jour du jeu pour redessiner les éléments dynamiques.
     *
     * @param game L'état actuel du jeu
     */
    @Override
    public void onGameUpdate(Game game) {
        // Si le niveau est terminé ou en chargement, cacher les ennemis
        if (game.isLevelComplete() || game.isLoading()) {
            hideAllEnemies();
        } else {
            drawPlayer(game.getPlayer());
            drawEnemies(game.getEnemies());
        }

        updateDoors(game);
        hudRenderer.draw(game);
    }

    /**
     * Cache tous les sprites d'ennemis en les plaçant hors écran
     */
    private void hideAllEnemies() {
        for (EnemyView view : enemyViews.values()) {
            view.setPosition(-10000, -10000, 0);
        }
    }
    /**
     * Attache le contrôleur clavier à la fenêtre.
     *
     * @param controller Le contrôleur clavier
     */
    public void addKeyListener(KeyboardController controller) {
        ((JFrame) this.windowGame).addKeyListener(controller);
    }

    /**
     * Met à jour la position et l'animation du joueur.
     *
     * @param player Le joueur à dessiner
     */
    private void drawPlayer(Player player) {
        if (player == null || crusaderSprite == null) return;

        float x = (float) player.getPosition().getX();
        float y = (float) player.getPosition().getY();
        float z = 0.0f;

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
     * Met à jour la position et l'animation du spider.
     *
     * @param enemies Le spider à dessiner
     */

    public void drawEnemies(List<Enemy> enemies) {
        if (enemies == null) return;

        Map<Enemy, EnemyView> updatedViews = new HashMap<>();

        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) continue;

            EnemyView view = enemyViews.get(enemy);
            if (view == null) {
                view = new EnemyView(enemy);
                windowGame.add(view);
                System.out.println("Nouveau sprite créé pour ennemi");
            }

            float x = enemy.getPosition().getX();
            float y = enemy.getPosition().getY();
            view.setPosition(x, y, 0);
            view.setMode(Spider.Mode.WALK);

            updatedViews.put(enemy, view);
        }


        enemyViews = updatedViews;
    }
    /**
     * Dessine le labyrinthe statique (murs et sol).
     * Appelé une seule fois au chargement du niveau.
     *
     * @param maze Le labyrinthe à dessiner
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

                windowGame.add(code, x, y, 0);
            }
        }
    }

    /**
     * Dessine les objets du jeu (clés, portes, sortie).
     * Appelé une seule fois au chargement du niveau.
     *
     * @param maze Le labyrinthe contenant les objets
     */
    public void drawItems(Maze maze) {
        if (maze == null) return;


        if (maze.getKeyPosition() != null) {
            List<Position> keyPositions = maze.getKeyPosition();
            for (Position keyPos : keyPositions) {
                int x = (int) Math.round(keyPos.getX());
                int y = (int) Math.round(keyPos.getY());
                windowGame.add(GameConfig.KEY_CODE, x, y, 0);
            }
        }

        if (maze.getDoorPositions() != null) {
            for (Position doorPos : maze.getDoorPositions()) {
                int doorCode = getDoorCode((int) doorPos.getX(),
                        (int) doorPos.getY(),
                        maze,
                        false);
                windowGame.add(doorCode,
                        (int) doorPos.getX(),
                        (int) doorPos.getY(),
                        0);
            }
        }

        if (maze.getExitPosition() != null) {
            Position exitPos = maze.getExitPosition();
            windowGame.add(GameConfig.EXIT_CODE,
                    (int) exitPos.getX(),
                    (int) exitPos.getY(),
                    0);
        }
    }

    /**
     * Détermine le code de porte selon son orientation.
     *
     * @param x Position X de la porte
     * @param y Position Y de la porte
     * @param maze Le labyrinthe
     * @param isOpen true si la porte est ouverte
     * @return Le code Mazing correspondant
     */
    private int getDoorCode(int x, int y, Maze maze, boolean isOpen) {
        boolean wallLeft = (x > 0) && maze.getTile(x - 1, y) == GameConfig.LEVEL_WALL;
        boolean wallRight = (x < maze.getWidth() - 1) && maze.getTile(x + 1, y) == GameConfig.LEVEL_WALL;
        boolean wallUp = (y > 0) && maze.getTile(x, y - 1) == GameConfig.LEVEL_WALL;
        boolean wallDown = (y < maze.getHeight() - 1) && maze.getTile(x, y + 1) == GameConfig.LEVEL_WALL;

        int horizontalWalls = (wallLeft ? 1 : 0) + (wallRight ? 1 : 0);
        int verticalWalls = (wallUp ? 1 : 0) + (wallDown ? 1 : 0);

        if (isOpen) {
            if (horizontalWalls >= verticalWalls) {
                return 165;
            } else {
                return 166;
            }
        } else {
            if (horizontalWalls >= verticalWalls) {
                return 161;
            } else {
                return 163;
            }
        }
    }

    private void updateDoors(Game game) {
        Maze maze = game.getMaze();

        for (Entity item : game.getItems()) {
            if (item instanceof Door door) {
                int x = (int) door.getPosition().getX();
                int y = (int) door.getPosition().getY();
                int code = getDoorCode(x, y, maze, door.isOpen());
                windowGame.add(code, x, y, 0);
            }
        }
    }


    /**
     * Calcule le code de mur selon l'orientation pour l'auto-tiling.
     *
     * @param x Position X du mur
     * @param y Position Y du mur
     * @param maze Le labyrinthe
     * @return Le code Mazing correspondant
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

    /**
     * Retourne la fenêtre Mazing.
     *
     * @return La fenêtre de jeu
     */
    public IWindowGame getWindow() {
        return windowGame;
    }

    /**
     * Retourne le HudRenderer.
     *
     * @return Le renderer du HUD
     */
    public HudRenderer getHudRenderer() {
        return hudRenderer;
    }
    /**
     * Nettoie tous les sprites d'ennemis.
     * À appeler avant de charger un nouveau niveau.
     */
    public void clearEnemies() {
        // Cacher tous les sprites avant de vider la map
        for (EnemyView view : enemyViews.values()) {
            view.setPosition(-10000, -10000, 0);
        }
        enemyViews.clear();
        System.out.println("Map des sprites d'ennemis vidée");
    }
}