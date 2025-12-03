package fr.ubordeaux.ao.project.model.entities;

import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.entities.Player;

/**
 * Fichier "Bouchon" (Stub) pour la Personne 2.
 * Représente un mur.
 */
public class Wall extends Entity {

    public Wall(Position pos) {
        super(pos);
    }

    /**
     * Un mur n'est jamais praticable.
     * (Ceci n'est pas utilisé par Game.java, qui se fie à Maze.isWalkable(),
     * mais c'est une bonne pratique de l'inclure).
     */
    @Override
    public boolean isCrossable(Player player) {
        return false;
    }
}