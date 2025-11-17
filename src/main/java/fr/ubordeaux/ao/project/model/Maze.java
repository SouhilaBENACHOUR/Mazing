package fr.ubordeaux.ao.project.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Labyrinthe du jeu.
 * Charge depuis un fichier JSON.
 */
public class Maze {

    private int width;
    private int height;
    private char[][] tiles;
    private String name;

    private Position playerSpawn;
    private List<Position> enemySpawns;
    private Position keyPosition;
    private Position doorPosition;
    private Position exitPosition;

    public Maze(String fileName) {
        this.enemySpawns = new ArrayList<>();
        loadJson(fileName);
    }

    private void loadJson(String fileName) {
        String path = GameConfig.LEVELS_PATH + fileName;
        var stream = getClass().getClassLoader().getResourceAsStream(path);

        if (stream == null) {
            System.err.println("Fichier non trouvé : " + path);
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            // Parser le JSON avec Gson
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            // Récupérer les infos de base
            this.name = json.get("name").getAsString();
            this.width = json.get("width").getAsInt();
            this.height = json.get("height").getAsInt();

            // Créer la grille
            tiles = new char[height][width];

            // Lire les lignes de tuiles
            JsonArray tilesArray = json.getAsJsonArray("tiles");

            for (int y = 0; y < height; y++) {
                String line = tilesArray.get(y).getAsString();
                String[] parts = line.split(" ");

                for (int x = 0; x < width; x++) {
                    char c = parts[x].charAt(0);

                    // Gérer les cases spéciales
                    if (c == 'P') {
                        playerSpawn = new Position(x, y);
                        tiles[y][x] = '0';
                    } else if (c == 'E') {
                        enemySpawns.add(new Position(x, y));
                        tiles[y][x] = '0';
                    } else if (c == 'K') {
                        keyPosition = new Position(x, y);
                        tiles[y][x] = '0';
                    } else if (c == 'D') {
                        doorPosition = new Position(x, y);
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
        }
    }

    public boolean isWalkable(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        return tiles[y][x] != '1';
    }

    public char getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return '1';
        }
        return tiles[y][x];
    }

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
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getName() { return name; }
    public Position getPlayerSpawn() { return playerSpawn; }
    public List<Position> getEnemySpawns() { return enemySpawns; }
    public Position getKeyPosition() { return keyPosition; }
    public Position getDoorPosition() { return doorPosition; }
    public Position getExitPosition() { return exitPosition; }
}