package fr.ubordeaux.ao.project.model;

import fr.ubordeaux.ao.project.model.entities.*;
import fr.ubordeaux.ao.project.model.graph.MazeGraph;
import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.graph.Direction;
import fr.ubordeaux.ao.project.model.patterns.factory.EntityFactory;
import fr.ubordeaux.ao.project.model.patterns.factory.EntityType;
import fr.ubordeaux.ao.project.model.patterns.observer.GameObserver;


import java.util.*;

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
    private boolean isLoading;
    private int currentLevel = 1;
    private final Random random = new Random();


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
        this.isLoading = false;
    }

    public void loadLevel(String levelFileName) {
        setLoading(true);
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
        if (isGameOver || isLevelComplete || isLoading) return;
        if (player == null || maze == null) return;

        player.update();

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (enemy.isAlive()) {
                enemy.update(player, mazeGraph, maze);
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

        Position currentPos = player.getPosition();
        Position targetPos = currentPos.getNeighbor(direction);

        if (maze.isWalkable((int) targetPos.getX(), (int) targetPos.getY())) {
            Entity blockingEntity = getEntityAt(targetPos, items);
            if (blockingEntity != null && !blockingEntity.isCrossable(player)) {
                notifyObservers();
                return;
            }
            // Déplacement du joueur
            player.setPreviousPosition(currentPos);
            player.setPosition(targetPos);
            player.setDirection(direction);
            player.setIsMoving(true);
        } else {
            player.setIsMoving(false);
        }

        notifyObservers();
    }

    public void handlePlayerStop() {
        if (player != null) {
            player.setIsMoving(false);
            notifyObservers();
        }
    }


    private EntityType getRandomEnemyType() {
        EntityType[] types = {EntityType.ENEMY_SMALL, EntityType.ENEMY_MEDIUM, EntityType.ENEMY_LARGE};
        int idx = new Random().nextInt(types.length);
        return types[idx];
    }


    private void populateEntitiesFromMaze() {
        // Vider les entités précédentes
        if (enemies == null) enemies = new ArrayList<>();
        else enemies.clear();

        // Player
        if (maze.getPlayerSpawn() != null) {
            Entity p = EntityFactory.createEntity(EntityType.PLAYER, maze.getPlayerSpawn());
            if (p != null) {
                this.player = (Player) p;
            }
        }

        if (player == null) return;

        // Positions occupées pour éviter collisions
        Set<Position> occupiedPositions = new HashSet<>();
        occupiedPositions.add(player.getPosition());

        List<Position> spawns = maze.getEnemySpawns();
        if (spawns == null || spawns.isEmpty()) return;

        int enemiesToSpawn = switch (currentLevel) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            default -> 0;
        };

        for (int i = 0; i < enemiesToSpawn; i++) {
            // Choisir un spawn aléatoire parmi les points disponibles
            Position spawnPos = spawns.get(random.nextInt(spawns.size()));

            // Trouver une position libre proche du spawn
            Position pos = findFreePositionNear(spawnPos, occupiedPositions);

            // Si aucune position libre trouvée, on tente un autre spawn (limité), sinon on skip
            if (pos == null) {
                boolean found = false;
                for (int attempt = 0; attempt < spawns.size(); attempt++) {
                    Position trySpawn = spawns.get((i + attempt) % spawns.size());
                    pos = findFreePositionNear(trySpawn, occupiedPositions);
                    if (pos != null) { found = true; break; }
                }
                if (!found) {
                    // impossible de spawn davantage d'ennemis sans collision
                    break;
                }
            }

            Enemy e = (Enemy) EntityFactory.createEntity(getRandomEnemyType(), pos);
            if (e != null) {
                e.setPosition(pos);
                enemies.add(e);
                occupiedPositions.add(pos);
            }
        }
        for (Position keyPos : maze.getKeyPosition()) {
            Entity key = EntityFactory.createEntity(EntityType.KEY, keyPos);
            if (key != null) items.add(key);
        }

        for (Position doorPos : maze.getDoorPositions()) {
            Entity door = EntityFactory.createEntity(EntityType.DOOR, doorPos);
            if (door != null) items.add(door);
        }

        if (maze.getExitPosition() != null) {
            Entity exit = EntityFactory.createEntity(EntityType.EXIT, maze.getExitPosition());
            if (exit != null) items.add(exit);
        }

    }

    private Position findFreePositionNear(Position pos, Set<Position> occupied) {

        if (maze.isWalkable((int) pos.getX(), (int) pos.getY())
                && !occupied.contains(pos)) {
            return pos;
        }


        int[][] offsets = {
                {1,0}, {-1,0}, {0,1}, {0,-1},
                {1,1}, {-1,-1}, {1,-1}, {-1,1}
        };

        for (int[] o : offsets) {
            Position newPos = new Position(
                    (int) pos.getX() + o[0],
                    (int) pos.getY() + o[1]
            );

            if (maze.isWalkable((int) newPos.getX(), (int) newPos.getY())
                    && !occupied.contains(newPos)) {
                return newPos;
            }
        }

        return null;
    }


    private void checkCollisions() {
        if (player == null) return;

        // Collision avec ennemis
        for (Enemy enemy : enemies) {
            if (player.getPosition().distanceTo(enemy.getPosition()) < GameConfig.COLLISION_DISTANCE) {
                player.takeDamage();
                if (!player.isAlive()) {
                    lives--;
                    if (lives <= 0) isGameOver = true;
                    else player.respawn(maze.getPlayerSpawn());
                }
            }
        }

        // Collision avec items (clés, portes, exit, autres)
        Iterator<Entity> itemIterator = items.iterator();
        while (itemIterator.hasNext()) {
            Entity item = itemIterator.next();
            if (item == null) continue;

            if (player.getPosition().distanceTo(item.getPosition()) < GameConfig.COLLISION_DISTANCE) {
                if (item instanceof Key key) {
                    player.collectKey(key);
                    key.consume();
                    itemIterator.remove();
                } else if (item instanceof Door door) {
                    if (player.hasKey()) {
                        player.useKey();
                        door.open();
                    } else {
                        // Revenir à la position précédente si la porte est fermée
                        player.setPosition(player.getPreviousPosition());
                    }
                } else if (item instanceof Exit) {
                    isLevelComplete = true;
                    score += 1000;
                    System.out.println("NIVEAU TERMINE !");
                } else {
                    // Pour tout autre item traversable
                    item.onContact(player);
                    if (item.isConsumed()) itemIterator.remove();
                }
            }
        }
    }

    private void checkGameStatus() {
        if (player == null || maze == null || maze.getExitPosition() == null) return;

        if (lives <= 0 && !isGameOver) {
            isGameOver = true;
            System.out.println("GAME OVER");
        }

        if (player.getPosition().equals(maze.getExitPosition())) {
            isLevelComplete = true;
            score += 1000;
            System.out.println("NIVEAU TERMINE !");

        }
    }

    public void resetLevel() {
        this.player = null;
        this.enemies.clear();
        this.items.clear();
        this.isGameOver = false;
        this.isLevelComplete = false;
        this.lives = GameConfig.PLAYER_LIVES;
        notifyObservers();
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    public void addObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (GameObserver observer : new ArrayList<>(observers)) {
            observer.onGameUpdate(this);
        }
    }

    private Entity getEntityAt(Position pos, List<Entity> entityList) {
        for (Entity entity : entityList) {
            if (entity == null) continue;
            if (entity.getPosition().equals(pos)) return entity;
        }
        return null;
    }

    public Maze getMaze() { return maze; }
    public Player getPlayer() { return player; }
    public List<Enemy> getEnemies() { return enemies; }
    public List<Entity> getItems() { return items; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public boolean isGameOver() { return isGameOver; }
    public boolean isLevelComplete() { return isLevelComplete; }
    public boolean isLoading() { return isLoading; }
    public void setLoading(boolean loading) { this.isLoading = loading; }


    public MazeGraph getMazeGraph() { return mazeGraph; }
}
