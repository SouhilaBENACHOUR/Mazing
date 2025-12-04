package fr.ubordeaux.ao.project.controller;

import fr.ubordeaux.ao.project.model.graph.Direction;
import fr.ubordeaux.ao.project.model.Game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Gère les entrées clavier pour le joueur.
 * (Responsabilité : Personne 3, mais utilise la logique de P1)
 * * Rôle : Implémente KeyListener pour intercepter les touches
 * et appeler les méthodes de mouvement sur le modèle (Game).
 */
public class KeyboardController extends KeyAdapter {

    private Game game;

    public KeyboardController(Game game) {
        this.game = game;
    }

    /**
     * Méthode appelée lorsque l'utilisateur appuie sur une touche.
     * Appelle handlePlayerMove pour que le joueur se tourne et
     * démarre l'animation de marche.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (game.isGameOver() || game.isLevelComplete()) {
            return; // Ne rien faire si le jeu est terminé
        }

        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_Z: // Pour ZQSD
                game.handlePlayerMove(Direction.NORTH);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S: // Pour ZQSD
                game.handlePlayerMove(Direction.SOUTH);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_Q: // Pour ZQSD
                game.handlePlayerMove(Direction.WEST);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D: // Pour ZQSD
                game.handlePlayerMove(Direction.EAST);
                break;
        }
    }

    /**
     * Méthode appelée lorsque l'utilisateur relâche une touche.
     * Appelle handlePlayerStop pour que l'animation de marche s'arrête
     * et que le joueur passe en mode IDLE.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Vérifie si c'était une touche de mouvement
        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_Z ||
                keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S ||
                keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_Q ||
                keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {

            // Dit au modèle que le joueur s'est arrêté de bouger
            game.handlePlayerStop();
        }
    }
}