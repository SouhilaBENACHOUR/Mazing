package fr.ubordeaux.ao.project.model.entities;

import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.entities.Player;
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
    private float speed = 0.45f;
    private boolean canMove; // vrai uniquement si le joueur a commencé à bouger
    private long lastMoveTime = 0;      // Dernier moment où l'ennemi a bougé
    private long moveCooldown;          // Durée minimale entre deux déplacements


    public Enemy(Position pos, Size size) {
        super(pos);
        this.size = size;

        switch (size) {
            case SMALL  -> { speed = 0.40f; moveCooldown = 200; }
            case MEDIUM -> { speed = 0.45f; moveCooldown = 250; }
            case LARGE  -> { speed = 0.50f; moveCooldown = 300; }
        }

        this.canMove = false;
    }


    public boolean isAlive() {
        return true;
        return true; // L'ennemi est toujours vivant (pour l'instant)
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

        if (!parent.containsKey(goal)) return List.of();

        List<Position> path = new LinkedList<>();
        Position cur = goal;
        while (cur != null) {
            path.add(0, cur);
            cur = parent.get(cur);
        }

        return path;
    }


    public void update(Player player, MazeGraph mazeGraph) {
        if (!canMove && player.isMoving()) {
            canMove = true;
        }

        if (!canMove) return;

        long now = System.currentTimeMillis();

        //  Si le cooldown n'est pas écoulé l'araigné ne bouge pas
        if (now - lastMoveTime < moveCooldown) return;

        // On bouge
        lastMoveTime = now;

        Position pos = getPosition();
        Position playerPos = player.getPosition();

        List<Position> path = findPath(pos, playerPos, mazeGraph);
        if (path.size() < 2) return;

        Position next = path.get(1);

        // Déplacement progressif selon speed
        pos.setX(next.getX());
        pos.setY(next.getY());
        // L'IA (P2) n'est pas encore implémentée
    }

    // --- Getters ---
    public Size getSize() { return size; }
    public float getSpeed() { return speed; }
}
