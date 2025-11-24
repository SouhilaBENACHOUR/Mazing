package fr.ubordeaux.ao.project;

import fr.ubordeaux.ao.project.controller.GameController;
import fr.ubordeaux.ao.project.model.Ennemie;
import fr.ubordeaux.ao.project.model.Game;
import fr.ubordeaux.ao.project.view.GameView;

public class Main {

    public static void main(String[] args) {
        Game model = new Game();
        GameView view = new GameView();
        GameController controller = new GameController(model, view);


        controller.spawnEnemy(Ennemie.Size.SMALL, 6, 3);
        controller.spawnEnemy(Ennemie.Size.MEDIUM, 4, 5);
        controller.spawnEnemy(Ennemie.Size.LARGE, 2, 2);


        new Thread(() -> {
            while (true) {
                controller.updateEnemies();
                try { Thread.sleep(15); } catch (InterruptedException ignored) {}
            }
        }).start();
    }
}
