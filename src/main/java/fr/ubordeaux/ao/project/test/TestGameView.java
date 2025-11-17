package fr.ubordeaux.ao.project.test;

import fr.ubordeaux.ao.project.model.Maze;
import fr.ubordeaux.ao.project.view.GameView;

/**
 * Test de l'affichage du labyrinthe avec Mazing.
 */
public class TestGameView {

    public static void main(String[] args) {
        System.out.println("=== Test d'affichage avec Mazing ===\n");

        try {
            // Charger le niveau
            Maze maze = new Maze("level1.json");
            System.out.println("Labyrinthe chargé : " + maze.getWidth() + "x" + maze.getHeight());

            // Créer la vue
            GameView view = new GameView(maze);
            System.out.println("Fenêtre créée et labyrinthe affiché !");

            System.out.println("\nFenêtre ouverte. Regardez l'écran !");
            System.out.println("Fermez la fenêtre pour terminer le programme.");

            // Garder le programme en vie
            Thread.sleep(Long.MAX_VALUE);

        } catch (Exception e) {
            System.err.println("Erreur:");
            e.printStackTrace();
        }
    }
}