package fr.ubordeaux.ao.project.model.entities;

import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.graph.Direction;

/**
 * Représente le joueur dans le modèle.
 * (Responsabilité : Personne 1)
 * * Rôle : Gérer l'état du joueur (position, vies, direction, mouvement).
 * Hérite de Entity (P2).
 */
public class Player extends Entity { // <-- MODIFICATION IMPORTANTE

    private int lives;
    private boolean isAlive;
    private Direction direction;
    private boolean isMoving;

    public Player(Position startPosition) {
        super(startPosition); // Appelle le constructeur de Entity
        this.lives = 3;
        this.isAlive = true;
        this.direction = Direction.SOUTH;
        this.isMoving = false;
    }

    // --- Méthodes appelées par Game.java ---

    public void update() {
        // (P2 peut utiliser ça pour le State Pattern)
    }

    public void takeDamage() {
        if (isAlive) {
            this.loseLife();
        }
    }

    public void respawn(Position startPosition) {
        this.position = startPosition; // "position" vient de Entity
        this.isAlive = true;
        this.direction = Direction.SOUTH;
        this.isMoving = false;
    }

    // --- Getters & Setters ---

    // getPosition() est hérité de Entity, mais setPosition est spécifique
    public void setPosition(Position newPosition) {
        this.position = newPosition;
    }

    public Direction getDirection() { return this.direction; }
    public void setDirection(Direction d) { this.direction = d; }
    public boolean isMoving() { return this.isMoving; }
    public void setIsMoving(boolean moving) { this.isMoving = moving; }

    // --- Méthodes du Ticket PLAYER-2 ---

    public void loseLife() {
        if (this.lives > 0) {
            this.lives--;
        }
        if (this.lives <= 0) {
            this.isAlive = false;
        }
    }

    public boolean isAlive() {
        return this.lives > 0;
    }

    public int getLives() {
        return this.lives;
    }
}