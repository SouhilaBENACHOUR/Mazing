package fr.ubordeaux.ao.project.controller;

import fr.ubordeaux.ao.project.view.GameView;
import fr.ubordeaux.ao.project.model.Game;
import fr.ubordeaux.ao.project.model.entities.Enemy;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private final GameView gameView;
    private final KeyboardController keyboardController;

    private final List<EnemyController> enemyControllers = new ArrayList<>();

    public GameController(GameView gameView, KeyboardController keyboardController) {
        this.gameView = gameView;
        this.keyboardController = keyboardController;

        // Permet d'écouter les touches du clavier
        this.gameView.addKeyListener(this.keyboardController);
    }

    /**
     * Appelé après game.populateEntitiesFromMaze()
     * Crée les EnemyController et lie chaque Enemy à sa vue.
     */
    public void attachEnemyControllers(Game game) {
        enemyControllers.clear();

        for (Enemy enemy : game.getEnemies()) {

            EnemyController ctrl = new EnemyController(
                    enemy,
                    null,
                    game.getPlayer(),
                    game.getMazeGraph(),
                    game.getMaze()
            );

            enemyControllers.add(ctrl);
        }
    }

    /**
     * Mise à jour des ennemis (appelée à chaque frame)
     */
    public void update() {
        for (EnemyController ctrl : enemyControllers) {
            ctrl.update();
        }
    }
}
