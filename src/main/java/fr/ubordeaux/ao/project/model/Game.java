package fr.ubordeaux.ao.project.model;

import fr.ubordeaux.ao.project.model.entities.Enemy;
import fr.ubordeaux.ao.project.model.entities.Entity;
import fr.ubordeaux.ao.project.model.entities.Player;
import fr.ubordeaux.ao.project.model.graph.MazeGraph;
import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.graph.Direction;
import fr.ubordeaux.ao.project.model.patterns.factory.EntityFactory;
import fr.ubordeaux.ao.project.model.patterns.factory.EntityType;
import fr.ubordeaux.ao.project.model.patterns.observer.GameObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Classe centrale du modèle (MVC).
 * Gère l'état du jeu et notifie la Vue via Observer.
 */
public class Game {

    private Maze maze;
    private MazeGraph mazeGraph;
    private Player player;
    private List<Enemy> enemies;
    private List<Entity> items;

    private int score;
    private int lives;
    private boolean isGameOver;
    private boolean isLevelComplete;
    private boolean isFinalVictory;
    private boolean isLoading;

    private List<GameObserver> observers;

    public Game() {
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.mazeGraph = new MazeGraph();
        this.maze = null;
        this.score = 0;
        this.lives = GameConfig.PLAYER_LIVES;
        this.isGameOver = false;
        this.isLevelComplete = false;
        this.isFinalVictory = false;
        this.isLoading = false;

    }

    public void loadLevel(String levelFileName) {
        setLoading(true);

        // Nettoyer pour le nouveau niveau (mais garder vies et score)
        this.player = null;
        this.enemies.clear();
        this.items.clear();
        this.isLevelComplete = false;
        this.isGameOver = false;

        try {
            this.maze = new Maze(levelFileName);
            mazeGraph.buildGraph(this.maze);
            populateEntitiesFromMaze();
            System.out.println("Niveau '" + maze.getName() + "' chargé.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERREUR lors du chargement du niveau : " + levelFileName);
            this.isGameOver = true;
        }

        setLoading(false);
        notifyObservers();
    }

    public void updateGame() {
        if (isGameOver || isLevelComplete) return;

        if (isGameOver || isLevelComplete || isLoading) {
            return;
        }
        if (player == null || maze == null) {
            return;
        }

        if (player != null) {
            player.update();
        }
        if (player != null) {
            player.update();
        }

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (enemy.isAlive()) {
                enemy.update(player, mazeGraph);
            } else {
                score += 100;
                enemyIterator.remove();
            }
        }

        checkCollisions();
        checkGameStatus();
        notifyObservers();
    }

    public void handlePlayerMove(Direction direction) {
        if (isGameOver || isLevelComplete || player == null) return;

        player.setDirection(direction);
        player.setIsMoving(true);

        Position currentPos = player.getPosition();
        Position targetPos = currentPos.getNeighbor(direction);

        if (maze.isWalkable((int) targetPos.getX(), (int) targetPos.getY())) {
            Entity blockingEntity = getEntityAt(targetPos, items);
            if (blockingEntity != null && !blockingEntity.isCrossable(player)) {
                notifyObservers();
                return;
            }
            player.setPosition(targetPos);
        }

        notifyObservers();
    }

    public void handlePlayerStop() {
        if (player != null) {
            player.setIsMoving(false);
            notifyObservers();
        }
    }

    private void populateEntitiesFromMaze() {
        // 1. Créer le joueur
        if (maze.getPlayerSpawn() != null) {
            Entity p = EntityFactory.createEntity(EntityType.PLAYER, maze.getPlayerSpawn());
            if (p != null) {
                this.player = (Player) p;
            }
        }

        // 2. Créer les ennemis
        for (Position spawn : maze.getEnemySpawns()) {
            Entity e = EntityFactory.createEntity(EntityType.ENEMY, spawn);
            if (e != null) {
                this.enemies.add((Enemy) e);
            }
        }

        // 3. Créer la clé
        if (maze.getKeyPosition() != null) {
            Entity k = EntityFactory.createEntity(EntityType.KEY, maze.getKeyPosition());
            if (k != null) {
                this.items.add(k);
            }
        }

        // 4. Créer TOUTES les portes
        if (maze.getDoorPositions() != null) {
            for (Position doorPos : maze.getDoorPositions()) {
                Entity d = EntityFactory.createEntity(EntityType.DOOR, doorPos);
                if (d != null) {
                    this.items.add(d);
                }
            }
        }

        // 5. Créer la sortie
        if (maze.getExitPosition() != null) {
            Entity x = EntityFactory.createEntity(EntityType.EXIT, maze.getExitPosition());
            if (x != null) {
                this.items.add(x);
            }
        }
    }

    private void checkCollisions() {
        if (player == null) return;

        for (Enemy enemy : enemies) {
            if (player.getPosition().distanceTo(enemy.getPosition()) < GameConfig.COLLISION_DISTANCE) {
                player.takeDamage();
                if (!player.isAlive()) {
                    lives--;
                    if (lives <= 0) {
                        isGameOver = true;
                    } else {
                        player.respawn(maze.getPlayerSpawn());
                    }
                }
            }
        }

        Iterator<Entity> itemIterator = items.iterator();
        while (itemIterator.hasNext()) {
            Entity item = itemIterator.next();
            if (item == null) continue;

            if (player.getPosition().distanceTo(item.getPosition()) < GameConfig.COLLISION_DISTANCE) {
                item.onContact(player);
                if (item.isConsumed()) {
                    itemIterator.remove();
                }
            }
        }
    }

    private void checkGameStatus() {
        if (lives <= 0 && !isGameOver) {
            isGameOver = true;
            System.out.println("GAME OVER");
        }

        if (player.getPosition().equals(maze.getExitPosition())) {
            float distance = (float) player.getPosition().distanceTo(maze.getExitPosition());
            if (distance < 0.9f) {
                isLevelComplete = true;
                score += 1000;
                System.out.println("NIVEAU TERMINE !");
            }
        }
    }

    private void resetGame() {
        this.player = null;
        this.enemies.clear();
        this.items.clear();
        this.isGameOver = false;
        this.isLevelComplete = false;
    }

    public void addObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            this.observers.add(observer);
        }
    }

    public void removeObserver(GameObserver observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers() {
        for (GameObserver observer : new ArrayList<>(observers)) {
            observer.onGameUpdate(this);
        }
    }

    public Maze getMaze() { return maze; }
    public Player getPlayer() { return player; }
    public List<Enemy> getEnemies() { return enemies; }
    public List<Entity> getItems() { return items; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public boolean isGameOver() { return isGameOver; }
    public boolean isLevelComplete() { return isLevelComplete; }

    private Entity getEntityAt(Position pos, List<Entity> entityList) {
        for (Entity entity : entityList) {
            if (entity == null) continue;
            if (entity.getPosition().equals(pos)) {
                return entity;
            }
        }
        return null;
    }

    public boolean isFinalVictory() {
        return isFinalVictory;
    }

    public void setFinalVictory(boolean finalVictory) {  // ← AJOUTER setter
        this.isFinalVictory = finalVictory;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
    }
}