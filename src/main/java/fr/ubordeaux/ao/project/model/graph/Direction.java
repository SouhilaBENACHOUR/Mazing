package fr.ubordeaux.ao.project.model.graph;

/**
 * Énumération représentant les 4 directions cardinales.
 * (Responsabilité : Personne 1)
 * * Rôle : Fournit une représentation claire des mouvements et
 * contient le décalage (dx, dy) pour chaque direction
 * afin de faciliter les calculs de position.
 */
public enum Direction {
    // Chaque direction stocke son changement en x et y
    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}