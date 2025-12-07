package fr.ubordeaux.ao.project.model;

public class GameConfig {
    /**
     * Configuration globale du jeu.
     *
     * Cette classe contient toutes les constantes utilisées dans le projet.
     * Aucune valeur ne doit être codée en dur dans le code.
     */

    public static final int WINDOW_WIDTH = 1000;


    public static final int WINDOW_HEIGHT = 1200;

    public static final int FPS = 30;

    /**
     * Durée d'une frame en millisecondes.
     * Calculé à partir du FPS : 1000ms / 30 = 33ms par frame.
     */
    public static final int FRAME_DURATION_MS = 1000 / FPS;

    public static final int TILE_SIZE = 64;

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



    public static final int FLOOR_CODE = 0;


    public static final int WALL_CODE = 131;

    /**
     * Code Mazing pour la sortie (exit).
     * Valeur temporaire.
     */
    public static final int EXIT_CODE = 300;


    /**
     * Code Mazing pour le coffre (représente la clé).
     */
    public static final int KEY_CODE = 551;




    /**
     * Codes Mazing pour les portes fermées.
     */
    public static final int DOOR_CLOSED_HORIZONTAL = 161;
    public static final int DOOR_CLOSED_VERTICAL = 163;

    /**
     * Codes Mazing pour les portes ouvertes.
     */
    public static final int DOOR_OPEN_HORIZONTAL = 165;
    public static final int DOOR_OPEN_VERTICAL = 166;


    public static final float PLAYER_SPEED = 1.0f;

    public static final float ENEMY_SPEED = 0.8f;

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


    public static final char LEVEL_FLOOR = '0';

    public static final char LEVEL_WALL = '1';


    public static final char LEVEL_PLAYER_SPAWN = 'P';

    public static final char LEVEL_ENEMY_SPAWN = 'E';


    public static final char LEVEL_KEY = 'K';


    public static final char LEVEL_DOOR = 'D';

    public static final char LEVEL_EXIT = 'X';


    /**
     * Chemin vers le répertoire contenant les niveaux.
     */
    public static final String LEVELS_PATH = "levels/";

    /**
     * Nom du fichier du niveau 1.
     */
    public static final String LEVEL1_FILE = "level1.json";


    /**
     * Constructeur privé pour empêcher l'instanciation.
     * Cette classe ne contient que des constantes statiques.
     */
    private GameConfig() {
        throw new AssertionError("GameConfig ne doit pas être instanciée");
    }
}