package fr.ubordeaux.ao.project.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private List<Ennemie> ennemies = new ArrayList<>();


    public Game() {
        ennemies = new ArrayList<>();
    }

    public void addEnemy(Ennemie e) {
        ennemies.add(e);
    }

    public List<Ennemie> getEnemies() {
        return ennemies;
    }

    public void update() {
        for (Ennemie e : ennemies) {
            e.update();
        }
    }
}
