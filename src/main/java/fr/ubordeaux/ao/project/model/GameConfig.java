package fr.ubordeaux.ao.project.model;

public class GameConfig {
    /**
     * Configuration globale du jeu.
     *
     * Cette classe contient toutes les constantes utilisées dans le projet.
     * Aucune valeur ne doit être codée en dur dans le code.
     */

    // ========================================
    // CONSTANTES DE FENÊTRE
    // ========================================

    //Largeur de la fenêtre de jeu en pixels.

    public static final int WINDOW_WIDTH = 1024;

    //Hauteur de la fenêtre de jeu en pixels.

    public static final int WINDOW_HEIGHT = 768;

    // Nombre d'images par seconde.
    //Le jeu tourne à 30 FPS.

    public static final int FPS = 30;

    /**
     * Durée d'une frame en millisecondes.
     * Calculé à partir du FPS : 1000ms / 30 = 33ms par frame.
     */
    public static final int FRAME_DURATION_MS = 1000 / FPS;


    // ========================================
    // CONSTANTES DE LABYRINTHE
    // ========================================

    //Taille d'une tuile du labyrinthe en pixels.

    public static final int TILE_SIZE = 100;

    /**
     * Largeur par défaut du labyrinthe en nombre de cases.
     * Pour level1.json, on utilise 10 cases de large pour l'instant.
     */
    public static final int DEFAULT_MAZE_WIDTH = 10;

    /**
     * Hauteur par défaut du labyrinthe en nombre de cases.
     * Pour level1.json, on utilise 10 cases de haut pour l'instant.
     */
    public static final int DEFAULT_MAZE_HEIGHT = 10;


    // ========================================
    // CODES MAZING
    // ========================================

    //Code Mazing pour le sol (walkable).

    public static final int FLOOR_CODE = 0;

    //Code Mazing pour les murs (non walkable).

    public static final int WALL_CODE = 131;

    /**
     * Code Mazing pour la sortie (exit).
     * Valeur temporaire.
     */
    public static final int EXIT_CODE = 200;


    // ========================================
    // CONSTANTES DE GAMEPLAY
    // ========================================

    //Le joueur se déplace de 1 case par appui de touche.

    public static final float PLAYER_SPEED = 1.0f;

    //Les ennemis sont légèrement plus lents que le joueur.

    public static final float ENEMY_SPEED = 0.8f;

    //Nombre de vies du joueur au démarrage du jeu.

    public static final int PLAYER_LIVES = 3;

    /**
     * Rayon de détection des ennemis (en cases).
     * Si le joueur est à moins de cette distance, l'ennemi le détecte.
     */
    public static final float ENEMY_DETECTION_RADIUS = 5.0f;

    /**
     * Distance minimale pour considérer une collision (en cases).
     * Si deux entités sont à moins de cette distance, il y a collision.
     */
    public static final float COLLISION_DISTANCE = 0.8f;


    // ========================================
    // CARACTÈRES DU FICHIER DE NIVEAU
    // ========================================

    //Caractère représentant le sol dans le fichier de niveau.

    public static final char LEVEL_FLOOR = '0';

    //Caractère représentant un mur dans le fichier de niveau.

    public static final char LEVEL_WALL = '1';

    //Caractère représentant le spawn du joueur dans le fichier de niveau.

    public static final char LEVEL_PLAYER_SPAWN = 'P';

    //Caractère représentant le spawn d'un ennemi dans le fichier de niveau.

    public static final char LEVEL_ENEMY_SPAWN = 'E';

    //Caractère représentant une clé dans le fichier de niveau.

    public static final char LEVEL_KEY = 'K';

    //Caractère représentant une porte dans le fichier de niveau.

    public static final char LEVEL_DOOR = 'D';

    //Caractère représentant la sortie dans le fichier de niveau.

    public static final char LEVEL_EXIT = 'X';


    // ========================================
    // CHEMINS DES RESSOURCES
    // ========================================

    /**
     * Chemin vers le répertoire contenant les niveaux.
     */
    public static final String LEVELS_PATH = "levels/";

    /**
     * Nom du fichier du niveau 1.
     */
    public static final String LEVEL1_FILE = "level1.json";


    // ========================================
    // CONSTRUCTEUR PRIVÉ
    // ========================================

    /**
     * Constructeur privé pour empêcher l'instanciation.
     * Cette classe ne contient que des constantes statiques.
     */
    private GameConfig() {
        throw new AssertionError("GameConfig ne doit pas être instanciée");
    }
}