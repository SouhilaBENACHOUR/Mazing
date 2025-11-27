package fr.ubordeaux.ao.project.model.entities;

import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.graph.MazeGraph;
import fr.ubordeaux.ao.project.model.graph.Direction;

import java.util.*;

/**
 * Ennemi qui poursuit le joueur grâce à un pathfinding BFS.
 * BFS (Breadth-First Search) permet de trouver le chemin le plus court
 * dans un labyrinthe constitué de cases et de murs.
 * L'ennemi n'avance que lorsque le joueur est en mouvement.
 */
public class Enemy extends Entity {

    public enum Size { SMALL, MEDIUM, LARGE }

    private Size size;
    private float speed;
    private boolean canMove; // vrai uniquement si le joueur a commencé à bouger
    private Random random = new Random();

    public Enemy(Position pos, Size size) {
        super(pos);
        this.size = size;

        switch (size) {
            case SMALL  -> speed = 0.35f;
            case MEDIUM -> speed = 0.40f;
            case LARGE  -> speed = 0.45f;
        }

        this.canMove = false; // par défaut, l'ennemi attend que le joueur bouge
    }

    public boolean isAlive() {
        return true;
    }

    // --- BFS pour trouver le chemin le plus court ---
    private List<Position> findPath(Position start, Position goal, MazeGraph maze) {
        Map<Position, Position> parent = new HashMap<>();
        Queue<Position> queue = new LinkedList<>();

        queue.add(start);
        parent.put(start, null);

        while (!queue.isEmpty()) {
            Position p = queue.poll();
            if (p.equals(goal)) break;

            for (Direction d : maze.getWalkableDirections(p)) {
                Position next = p.getNeighbor(d);
                if (!parent.containsKey(next)) {
                    parent.put(next, p);
                    queue.add(next);
                }
            }
        }

        if (!parent.containsKey(goal)) return List.of(); // aucun chemin

        List<Position> path = new LinkedList<>();
        Position cur = goal;
        while (cur != null) {
            path.add(0, cur);
            cur = parent.get(cur);
        }

        return path;
    }

    /**
     * Mise à jour de l'ennemi.
     * L’ennemi poursuit le joueur uniquement si ce dernier est en mouvement.
     */
    public void update(Player player, MazeGraph mazeGraph) {
        // Déclenchement du mouvement uniquement si le joueur bouge
        if (!canMove && player.isMoving()) {
            canMove = true;
        }

        if (!canMove) return; // ne bouge pas tant que le joueur est immobile

        Position pos = getPosition();
        Position playerPos = player.getPosition();

        List<Position> path = findPath(pos, playerPos, mazeGraph);
        if (path.size() < 2) return; // pas de mouvement possible

        Position next = path.get(1);

        // Déplacement progressif
        float newX = pos.getX() + (next.getX() - pos.getX()) * speed;
        float newY = pos.getY() + (next.getY() - pos.getY()) * speed;

        pos.setX(newX);
        pos.setY(newY);
    }

    // --- Getters ---
    public Size getSize() { return size; }
    public float getSpeed() { return speed; }
}
