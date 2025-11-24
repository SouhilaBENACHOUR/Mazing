package fr.ubordeaux.ao.project.controller;

import fr.ubordeaux.ao.project.model.Ennemie;
import fr.ubordeaux.ao.project.model.Game;
import fr.ubordeaux.ao.project.view.EnnemieView;
import fr.ubordeaux.ao.project.view.GameView;

public class GameController {

    private final Game model;
    private final GameView view;

    public GameController(Game model, GameView view) {
        this.model = model;
        this.view = view;
    }

    public void spawnEnemy(Ennemie.Size size, float x, float y) {
        Ennemie e = new Ennemie(size, x, y);
        EnnemieView v = new EnnemieView(e);

        model.addEnemy(e);
        view.add(v);
    }

    public void updateEnemies() {
        model.update();

        for (Object c : view.getComponents()) {
            if (c instanceof EnnemieView) {
                EnnemieView ev = (EnnemieView) c;
                ev.update();
            }
        }
    }


}
