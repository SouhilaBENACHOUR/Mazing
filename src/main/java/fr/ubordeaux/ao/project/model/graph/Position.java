package fr.ubordeaux.ao.project.model.graph;

// MODIFICATION: Import needed for getNeighbor()
import fr.ubordeaux.ao.project.model.graph.Direction;
import java.util.Objects; // (Good practice to add this for equals)

/**
 * Position dans le jeu (x, y, z).
 */
public class Position {

    private float x;
    private float y;
    private float z;

    public Position(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(int x, int y) {
        this.x = (float) x;
        this.y = (float) y;
        this.z = 0.0f;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    // --- METHOD 1 ADDED (Fixes 'Cannot resolve method 'getNeighbor'') ---

    /**
     * Calcule la position voisine dans une direction donnée.
     * @param direction La direction (NORTH, SOUTH, etc.)
     * @return La new Position
     */
    public Position getNeighbor(Direction direction) {
        // Crée une nouvelle position en ajoutant le déplacement (dx, dy)
        // tout en conservant la même altitude (z)
        return new Position(
                this.x + direction.getDx(),
                this.y + direction.getDy(),
                this.z
        );
    }

    // --- METHOD 2 ADDED (Fixes 'Cannot resolve method 'distanceTo'') ---

    /**
     * Calcule la distance 2D (euclidienne) entre cette position et une autre.
     * @param other L'autre position
     * @return La distance.
     */
    public double distanceTo(Position other) {
        if (other == null) {
            return Double.POSITIVE_INFINITY;
        }
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        // (dx * dx + dy * dy) est plus rapide que Math.pow
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // --- RECOMMENDED: Add equals() and hashCode() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        // On compare sur la grille 2D, en ignorant z pour la logique de jeu
        return Math.round(x) == Math.round(position.x) &&
                Math.round(y) == Math.round(position.y);
    }

    @Override
    public int hashCode() {
        // Hash code basé sur la position arrondie de la grille
        return Objects.hash(Math.round(x), Math.round(y));
    }
}