package fr.ubordeaux.ao.project.model.entities;

import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.entities.Player;
import fr.ubordeaux.ao.project.model.graph.MazeGraph;
import fr.ubordeaux.ao.project.model.graph.Direction;
import java.util.List;
import java.util.Random;

public class Enemy extends Entity {

    public enum Size { SMALL, MEDIUM, LARGE }

    private Size size;
    private float speed;
    private Random random = new Random();

    public Enemy(Position pos, Size size) {
        super(pos);
        this.size = size;

        switch (size) {
            case SMALL  -> speed = 0.05f;
            case MEDIUM -> speed = 0.10f;
            case LARGE  -> speed = 0.15f;
        }
    }

    public boolean isAlive() {
        return true;
    }

    /**
     * Mise à jour de l'ennemi.
     * Déplacement aléatoire dans toutes les directions praticables.
     */
    public void update(Player player, MazeGraph mazeGraph) {
        Position pos = getPosition();

        // 1. Obtenir toutes les directions praticables depuis la position actuelle
        List<Direction> walkableDirs = mazeGraph.getWalkableDirections(pos);

        if (!walkableDirs.isEmpty()) {
            // 2. Choisir une direction aléatoire
            Direction dir = walkableDirs.get(random.nextInt(walkableDirs.size()));

            // 3. Calculer la position suivante
            Position nextPos = pos.getNeighbor(dir);

            // 4. Déplacement selon la vitesse
            float newX = pos.getX() + (nextPos.getX() - pos.getX()) * speed;
            float newY = pos.getY() + (nextPos.getY() - pos.getY()) * speed;

            pos.setX(newX);
            pos.setY(newY);
        }
    }

    // Getters
    public Size getSize() { return size; }
    public float getSpeed() { return speed; }
}
