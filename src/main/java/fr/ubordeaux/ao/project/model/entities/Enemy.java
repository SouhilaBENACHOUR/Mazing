package fr.ubordeaux.ao.project.model.entities;

import fr.ubordeaux.ao.project.model.Maze;
import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.entities.Player;
import fr.ubordeaux.ao.project.model.graph.MazeGraph;
import fr.ubordeaux.ao.project.model.graph.Direction;

import java.util.*;

public class Enemy extends Entity {


    public void setPosition(Position pos) {
        this.position = pos;
    }

    public enum Size { SMALL, MEDIUM, LARGE }

    private Size size;
    private float speed = 0.45f;
    private boolean canMove;
    private long lastMoveTime = 0;
    private long moveCooldown;

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
    }

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

    public void update(Player player, MazeGraph mazeGraph, Maze maze) {

        // Activer l'ennemi uniquement quand le joueur bouge une première fois
        if (!canMove && player.isMoving()) {
            canMove = true;
        }
        if (!canMove) return;

        long now = System.currentTimeMillis();
        if (now - lastMoveTime < moveCooldown) return;
        lastMoveTime = now;

        Position pos = getPosition();
        Position playerPos = player.getPosition();

        // PATHFINDING BFS
        List<Position> path = findPath(pos, playerPos, mazeGraph);

        // Pas de chemin → on ne bouge pas
        if (path.size() < 2) return;

        // Case suivante
        Position next = path.get(1);

        // SECURITÉ ABSOLUE : l’Ennemi ne bouge QUE si walkable
        int nx = (int) next.getX();
        int ny = (int) next.getY();

        if (!maze.isWalkable(nx, ny))
            return;

        // Déplacement accepté
        setPosition(new Position(nx, ny));
    }

    public Size getSize() { return size; }
    public float getSpeed() { return speed; }
}
