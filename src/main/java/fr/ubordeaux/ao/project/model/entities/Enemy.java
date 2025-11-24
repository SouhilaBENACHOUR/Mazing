package fr.ubordeaux.ao.project.model.entities;

import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.entities.Player;
import fr.ubordeaux.ao.project.model.graph.MazeGraph;

public class Enemy extends Entity {

    public enum Size { SMALL, MEDIUM, LARGE }

    private Size size;
    private float speed;

    public Enemy(Position pos, Size size) {
        super(pos);
        this.size = size;

        switch (size) {
            case SMALL  -> speed = 0.15f;
            case MEDIUM -> speed = 0.10f;
            case LARGE  -> speed = 0.05f;
        }
    }
    public boolean isAlive() {
        return true;
    }

    /**
     * Mise à jour de l'ennemi.
     * Pour l'instant : déplacement horizontal simple comme dans Ennemie.
     */

    public void update(Player player, MazeGraph mazeGraph) {
        Position pos = getPosition();

        // Déplacement horizontal comme dans votre ancienne classe Ennemie
        float x = pos.getX() - speed;
        float y = pos.getY();

        // Mettre à jour les coordonnées directement
        pos.setX(x);
        pos.setY(y);

        // Vous pourrez ensuite utiliser mazeGraph + player pour une IA avancée.
    }

    // Getters
    public Size getSize() { return size; }
    public float getSpeed() { return speed; }
}
