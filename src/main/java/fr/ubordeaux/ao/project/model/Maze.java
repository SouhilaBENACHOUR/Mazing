package fr.ubordeaux.ao.project.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.ubordeaux.ao.project.model.graph.Position;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente le labyrinthe du jeu.
 * Charge et gère la structure du niveau depuis un fichier JSON.
 *
 * @author Personne 3 (P3)
 * @version 1.0
 */
public class Maze {

    private int width;
    private int height;
    private char[][] tiles;
    private String name;

    private Position playerSpawn;
    private List<Position> enemySpawns;
    private List<Position> keyPositions;
    private List<Position> doorPositions;
    private Position exitPosition;


    /**
     * Constructeur du labyrinthe.
     *
     * @param fileName Le nom du fichier JSON à charger (ex: "level1.json")
     */
    public Maze(String fileName) {
        this.enemySpawns = new ArrayList<>();
        this.doorPositions = new ArrayList<>();
        this.keyPositions = new ArrayList<>();
        loadJson(fileName);
    }

    /**
     * Charge le labyrinthe depuis un fichier JSON.
     * Parse les tuiles et identifie les positions spéciales (joueur, ennemis, objets).
     *
     * @param fileName Le nom du fichier JSON
     */
    private void loadJson(String fileName) {
        String path = GameConfig.LEVELS_PATH + fileName;
        var stream = getClass().getClassLoader().getResourceAsStream(path);

        if (stream == null) {
            System.err.println("Fichier non trouvé : " + path);
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            Gson gson = new Gson();
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            this.name = json.get("name").getAsString();
            this.width = json.get("width").getAsInt();
            this.height = json.get("height").getAsInt();

            tiles = new char[height][width];

            JsonArray tilesArray = json.getAsJsonArray("tiles");

            for (int y = 0; y < height; y++) {
                String line = tilesArray.get(y).getAsString();
                String[] parts = line.split(" ");

                for (int x = 0; x < width; x++) {
                    char c = parts[x].charAt(0);

                    if (c == 'P') {
                        playerSpawn = new Position(x, y);
                        tiles[y][x] = '0';
                    } else if (c == 'E') {
                        enemySpawns.add(new Position(x, y));
                        tiles[y][x] = '0';
                    } else if (c == 'K') {
                        keyPositions.add(new Position(x, y));
                        tiles[y][x] = '0';
                    } else if (c == 'D') {
                        doorPositions.add(new Position(x, y));
                        tiles[y][x] = 'D';
                    } else if (c == 'X') {
                        exitPosition = new Position(x, y);
                        tiles[y][x] = '0';
                    } else {
                        tiles[y][x] = c;
                    }
                }
            }

            reader.close();
            System.out.println("Niveau chargé : " + name);

        } catch (Exception e) {
            System.err.println("Erreur chargement JSON : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Vérifie si une case est praticable (le joueur peut marcher dessus).
     *
     * @param x Position X
     * @param y Position Y
     * @return true si la case est praticable, false sinon
     */
    public boolean isWalkable(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        return tiles[y][x] == GameConfig.LEVEL_FLOOR;
    }

    /**
     * Retourne le caractère de la tuile à une position donnée.
     *
     * @param x Position X
     * @param y Position Y
     * @return Le caractère de la tuile ('0', '1', 'D', etc.)
     */
    public char getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return '1';
        }
        return tiles[y][x];
    }

    /**
     * Affiche le labyrinthe dans la console (debug).
     */
    public void print() {
        System.out.println("=== " + name + " (" + width + "x" + height + ") ===");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(tiles[y][x] + " ");
            }
            System.out.println();
        }
    }

    // Getters

    /**
     * @return La largeur du labyrinthe
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return La hauteur du labyrinthe
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return Le nom du niveau
     */
    public String getName() {
        return name;
    }

    /**
     * @return La position de spawn du joueur
     */
    public Position getPlayerSpawn() {
        return playerSpawn;
    }

    /**
     * @return La liste des positions de spawn des ennemis
     */
    public List<Position> getEnemySpawns() {
        return enemySpawns;
    }

    /**
     * @return La position de la clé
     */
    public List<Position> getKeyPosition() {
        return keyPositions;
    }

    /**
     * @return La liste des positions des portes
     */
    public List<Position> getDoorPositions() {
        return doorPositions;
    }

    /**
     * @return La position de la sortie
     */
    public Position getExitPosition() {
        return exitPosition;
    }
}