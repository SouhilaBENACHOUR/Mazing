package fr.ubordeaux.ao.project.controller;

import fr.ubordeaux.ao.mazing.api.ICharacter;
import fr.ubordeaux.ao.project.model.Maze;
import fr.ubordeaux.ao.project.model.entities.Enemy;
import fr.ubordeaux.ao.project.model.entities.Player;
import fr.ubordeaux.ao.project.model.graph.MazeGraph;
import fr.ubordeaux.ao.project.view.EnemyView;

import java.util.function.Predicate;

public class EnemyController implements Predicate<ICharacter<?>> {

    private final Enemy model;
    private final EnemyView view;
    private final Player player;
    private final MazeGraph mazeGraph;
    private final Maze maze;

    public EnemyController(EnemyView view, Enemy model, Player player, MazeGraph mazeGraph, Maze maze) {
        this.view = view;
        this.model = model;
        this.player = player;
        this.mazeGraph = mazeGraph;
        this.maze = maze;
    }

    @Override
    public boolean test(ICharacter<?> character) {

        // Mise à jour du modèle
        model.update(player, mazeGraph, maze);

        // Mise à jour de la vue
        float x = model.getPosition().getX();
        float y = model.getPosition().getY();

        view.setPosition(x, y, 0);

        return true;
    }
}
