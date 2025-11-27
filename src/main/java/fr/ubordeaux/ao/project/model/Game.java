package fr.ubordeaux.ao.project.model;

// Imports de vos classes (P1, P2, P3)
import fr.ubordeaux.ao.project.model.entities.Enemy;
import fr.ubordeaux.ao.project.model.entities.Entity;
import fr.ubordeaux.ao.project.model.entities.Player;
import fr.ubordeaux.ao.project.model.graph.MazeGraph;
import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.graph.Direction; // Import corrigé
// Import corrigé
import fr.ubordeaux.ao.project.model.patterns.factory.EntityFactory;
import fr.ubordeaux.ao.project.model.patterns.factory.EntityType;
import fr.ubordeaux.ao.project.model.patterns.observer.GameObserver;

// Imports Java
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Classe centrale du modèle (Modèle-Vue-Contrôleur).
 * (Responsabilité : Personne 1)
 * * Rôle : Gère l'état du jeu (logique, collisions, victoire)
 * et notifie la Vue via le pattern Observer.
 */
public class Game {

    // --- Attributs d'état du jeu ---
    private Maze maze;
    private MazeGraph mazeGraph;
    private Player player;
    private List<Enemy> enemies;
    private List<Entity> items; // Clés, Portes, Sortie

    private int score;
    private int lives;
    private boolean isGameOver;
    private boolean isLevelComplete;

    // --- Attributs pour Pattern Observer ---
    private List<GameObserver> observers;

    /**
     * Constructeur de Game.
     */
    public Game() {
        // Listes des entités
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.observers = new ArrayList<>();

        // Objets de logique (P1)
        this.mazeGraph = new MazeGraph();
        this.maze = null; // Sera initialisé dans loadLevel

        // États du jeu (via GameConfig)
        this.score = 0;
        this.lives = GameConfig.PLAYER_LIVES;
        this.isGameOver = false;
        this.isLevelComplete = false;
    }

    // --- 1. MÉTHODES DE LOGIQUE PRINCIPALE ---

    /**
     * Charge un niveau à partir d'un nom de fichier JSON.
     * (Responsabilité P1)
     */
    public void loadLevel(String levelFileName) {
        resetGame();

        try {
            // 1. Charger le labyrinthe (qui charge le JSON)
            this.maze = new Maze(levelFileName);

            // 2. Construire le graphe basé sur le labyrinthe chargé (P1)
            mazeGraph.buildGraph(this.maze);

            // 3. Placer les entités en utilisant les infos du labyrinthe (P1)
            populateEntitiesFromMaze();

            System.out.println("Niveau '" + maze.getName() + "' chargé.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERREUR lors du chargement du niveau : " + levelFileName);
            this.isGameOver = true;
        }

        notifyObservers();
    }

    /**
     * Méthode principale appelée par la GameLoop (P3) à chaque "tick".
     * (Responsabilité P1)
     */
    public void updateGame() {
        if (isGameOver || isLevelComplete) return; // Jeu en pause

        // 1. Mettre à jour le joueur (délègue à P2)
        if (player != null) {
            player.update();
        }

        // 2. Mettre à jour les ennemis (délègue à P2)
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (enemy.isAlive()) {
                // L'IA (P2) utilise le graphe (P1)
                enemy.update(player, mazeGraph);
            } else {
                score += 100; // Logique de mort (P1)
                enemyIterator.remove();
            }
        }

        // 3. Vérifier la logique du jeu (P1)
        checkCollisions();
        checkGameStatus();

        // 4. Notifier la Vue (P1)
        notifyObservers();
    }

    /**
     * Gère la demande de mouvement du joueur (appelée par KeyboardController - P3).
     * (Logique P1)
     */
    public void handlePlayerMove(Direction direction) {
        if (isGameOver || isLevelComplete || player == null) return;

        player.setDirection(direction);
        player.setIsMoving(true);

        Position currentPos = player.getPosition();
        Position targetPos = currentPos.getNeighbor(direction);

        // 1. Vérifie la praticabilité (MAINTENANT CORRIGÉE GRÂCE À MAZE.JAVA)
        if (maze.isWalkable((int) targetPos.getX(), (int) targetPos.getY())) {

            // 2. Vérifie les entités (ex: une porte OUVERTE)
            Entity blockingEntity = getEntityAt(targetPos, items);
            if (blockingEntity != null && !blockingEntity.isCrossable(player)) {
                notifyObservers();
                return; // Bloqué par une porte fermée
            }

            // 3. Mouvement AUTORISÉ
            player.setPosition(targetPos);

        }

        notifyObservers();
    }

    /**
     * Gère l'arrêt du joueur (appelé par KeyboardController - P3).
     * (Logique P1)
     */
    public void handlePlayerStop() {
        if (player != null) {
            player.setIsMoving(false);
            notifyObservers();
        }
    }


    // --- 2. LOGIQUE INTERNE (HELPERS P1) ---

    /**
     * Utilise les données chargées par Maze.java pour créer les entités
     * via la Factory (P2).
     * (Responsabilité P1)
     */
    /**
     * Utilise les données chargées par Maze.java pour créer les entités
     * via la Factory (P2).
     * (Responsabilité P1)
     */
    private void populateEntitiesFromMaze() {

        // 1. Créer le joueur
        if (maze.getPlayerSpawn() != null) {
            Entity p = EntityFactory.createEntity(EntityType.PLAYER, maze.getPlayerSpawn());
            if (p != null) { // <-- SÉCURITÉ
                this.player = (Player) p;
            }
        }

        // 2. Créer les ennemis
        for (Position spawn : maze.getEnemySpawns()) {
            Entity e = EntityFactory.createEntity(EntityType.ENEMY, spawn);
            if (e != null) { // <-- SÉCURITÉ
                this.enemies.add((Enemy) e);
            }
        }

        // 3. Créer la clé (s'il y en a une)
        if (maze.getKeyPosition() != null) {
            Entity k = EntityFactory.createEntity(EntityType.KEY, maze.getKeyPosition());
            if (k != null) { // <-- SÉCURITÉ (CORRECTION PRINCIPALE)
                this.items.add(k);
            }
        }

        // 4. Créer la porte (s'il y en a une)
        if (maze.getDoorPosition() != null) {
            Entity d = EntityFactory.createEntity(EntityType.DOOR, maze.getDoorPosition());
            if (d != null) { // <-- SÉCURITÉ (CORRECTION PRINCIPALE)
                this.items.add(d);
            }
        }

        // 5. Créer la sortie (s'il y en a une)
        if (maze.getExitPosition() != null) {
            Entity x = EntityFactory.createEntity(EntityType.EXIT, maze.getExitPosition());
            if (x != null) { // <-- SÉCURITÉ (CORRECTION PRINCIPALE)
                this.items.add(x);
            }
        }
    }

    /**
     * Vérifie les collisions (logique P1, utilise P2).
     */
    private void checkCollisions() {
        if (player == null) return;

        // 1. Collision Joueur <-> Ennemis
        for (Enemy enemy : enemies) {
            // Utilise une vérification de distance (depuis GameConfig)
            if (player.getPosition().distanceTo(enemy.getPosition()) < GameConfig.COLLISION_DISTANCE) {
                player.takeDamage(); // Délègue à P2
                if (!player.isAlive()) { // (P2)
                    lives--; // Logique P1
                    if (lives <= 0) {
                        isGameOver = true;
                    } else {
                        // Respawn à la position de départ du niveau (P1)
                        player.respawn(maze.getPlayerSpawn());
                    }
                }
            }
        }

        // 2. Collision Joueur <-> Items
        Iterator<Entity> itemIterator = items.iterator();
        while (itemIterator.hasNext()) {
            Entity item = itemIterator.next();

            if (item == null) continue; // <-- SÉCURITÉ (CORRIGE LIGNE 241)

            if (player.getPosition().distanceTo(item.getPosition()) < GameConfig.COLLISION_DISTANCE) {
                item.onContact(player);
                if (item.isConsumed()) {
                    itemIterator.remove(); // Logique P1
                }
            }
        }
    }

    /**
     * Gère la Victoire/Défaite (Logique P1).
     */
    private void checkGameStatus() {
        // 1. Défaite
        if (lives <= 0 && !isGameOver) {
            isGameOver = true;
            System.out.println("GAME OVER");
        }

        // 2. Victoire
        // Vérifie si le joueur est sur la case Sortie
        if (player.getPosition().equals(maze.getExitPosition())) {
            // (P2 doit gérer la logique de "la sortie est-elle ouverte ?")
            // Pour l'instant, on gagne si on touche la sortie
            isLevelComplete = true;
            score += 1000;
            System.out.println("NIVEAU TERMINE !");
        }
    }

    /**
     * Réinitialise l'état du jeu (Logique P1).
     */
    private void resetGame() {
        this.player = null;
        this.enemies.clear();
        this.items.clear();
        this.isGameOver = false;
        this.isLevelComplete = false;
        // On ne réinitialise ni le score ni les vies
    }


    // --- 3. PATTERN OBSERVER (P1) ---

    public void addObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            this.observers.add(observer);
        }
    }

    public void removeObserver(GameObserver observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers() {
        // Crée une copie pour éviter ConcurrentModificationException
        for (GameObserver observer : new ArrayList<>(observers)) {
            observer.onGameUpdate(this);
        }
    }


    // --- 4. GETTERS (pour P3) ---

    public Maze getMaze() { return maze; }
    public Player getPlayer() { return player; }
    public List<Enemy> getEnemies() { return enemies; }
    public List<Entity> getItems() { return items; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public boolean isGameOver() { return isGameOver; }
    public boolean isLevelComplete() { return isLevelComplete; }

    // --- 5. HELPERS (P1) ---

    /**
     * Trouve une entité dans la liste d'items à une position donnée.
     * (Utilisé pour vérifier les portes bloquantes)
     */
    private Entity getEntityAt(Position pos, List<Entity> entityList) {
        for (Entity entity : entityList) {

            if (entity == null) continue; // <-- SÉCURITÉ (CORRIGE LIGNE 323)

            if (entity.getPosition().equals(pos)) {
                return entity;
            }
        }
        return null;
    }
}