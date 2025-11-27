package fr.ubordeaux.ao.project.test;

import fr.ubordeaux.ao.project.model.Maze;

public class TestMaze {

    public static void main(String[] args) {
        System.out.println("=== Test chargement JSON ===\n");

        Maze maze = new Maze("level1.json");

        System.out.println("Largeur: " + maze.getWidth());
        System.out.println("Hauteur: " + maze.getHeight());
        System.out.println("Nom: " + maze.getName());
        System.out.println("Spawn joueur: " + maze.getPlayerSpawn());
        System.out.println("Nb ennemis: " + maze.getEnemySpawns().size());
        System.out.println("Clé: " + maze.getKeyPosition());
        System.out.println("Porte: " + maze.getDoorPosition());
        System.out.println("Sortie: " + maze.getExitPosition());

        System.out.println("\nGrille:");
        maze.print();

        System.out.println("\nTests:");
        System.out.println("(1,1) walkable? " + maze.isWalkable(1, 1));
        System.out.println("(0,0) walkable? " + maze.isWalkable(0, 0));

        System.out.println("\nOK !");
    }
}