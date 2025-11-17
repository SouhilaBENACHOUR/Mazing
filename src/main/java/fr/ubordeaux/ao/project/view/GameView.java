package fr.ubordeaux.ao.project.view;

import fr.ubordeaux.ao.project.model.GameConfig;
import fr.ubordeaux.ao.project.model.Maze;
import fr.ubordeaux.ao.mazing.api.WindowGame;

/**
 * Vue du jeu avec Mazing.
 */
public class GameView {

    private WindowGame window;
    private Maze maze;

    public GameView(Maze maze) {
        this.maze = maze;
        this.window = new WindowGame();
        drawMaze();
    }

    private void drawMaze() {
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                char tile = maze.getTile(x, y);

                int code;
                if (tile == '1') {
                    code = getWallCode(x, y);
                } else {
                    code = GameConfig.FLOOR_CODE;
                }

                window.add(code, x, y, 0);
            }
        }
    }

    private int getWallCode(int x, int y) {
        boolean wallLeft = (x > 0) && maze.getTile(x - 1, y) == '1';
        boolean wallRight = (x < maze.getWidth() - 1) && maze.getTile(x + 1, y) == '1';
        boolean wallUp = (y > 0) && maze.getTile(x, y - 1) == '1';
        boolean wallDown = (y < maze.getHeight() - 1) && maze.getTile(x, y + 1) == '1';

        // Compter les connexions horizontales et verticales
        int horizontal = 0;
        int vertical = 0;

        if (wallLeft) horizontal++;
        if (wallRight) horizontal++;
        if (wallUp) vertical++;
        if (wallDown) vertical++;

        // Priorité à l'orientation dominante
        if (horizontal > vertical) {
            return 129;  // Plus de connexions horizontales
        } else if (vertical > horizontal) {
            return 131;  // Plus de connexions verticales
        } else {
            // Égalité - utiliser la position pour décider
            // Les murs du bord haut/bas sont horizontaux
            if (y == 0 || y == maze.getHeight() - 1) {
                return 129;
            }
            // Les murs du bord gauche/droite sont verticaux
            else if (x == 0 || x == maze.getWidth() - 1) {
                return 131;
            }
            // Sinon, par défaut horizontal
            else {
                return 129;
            }
        }
    }

    public WindowGame getWindow() {
        return window;
    }
}