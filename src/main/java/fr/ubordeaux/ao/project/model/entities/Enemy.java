package fr.ubordeaux.ao.project.model.entities;

import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.entities.Player;
import fr.ubordeaux.ao.project.model.graph.MazeGraph;

/**
 * Fichier "Bouchon" (Stub) pour la Personne 2.
 * Représente un ennemi.
 */
public class Enemy extends Entity {

    public Enemy(Position pos) {
        super(pos);
    }

    // --- Méthodes appelées par Game.java ---

    public boolean isAlive() {
        return true; // L'ennemi est toujours vivant (pour l'instant)
    }

    public void update(Player player, MazeGraph mazeGraph) {
        // L'IA (P2) n'est pas encore implémentée
    }
}