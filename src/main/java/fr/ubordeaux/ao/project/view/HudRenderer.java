package fr.ubordeaux.ao.project.view;

import fr.ubordeaux.ao.mazing.api.IWindowGame;
import fr.ubordeaux.ao.project.model.Game;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;

/**
 * Gère l'affichage du HUD (vies, score, messages).
 *
 * @author Personne 3 (P3)
 * @version 1.0
 */
public class HudRenderer {

    private JLabel livesLabel;
    private JLabel gameOverLabel;

    public HudRenderer(IWindowGame windowGame) {
        JFrame frame = (JFrame) windowGame;

        livesLabel = new JLabel("Vies: 3");
        livesLabel.setFont(new Font("Arial", Font.BOLD, 24));
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setBounds(10, 10, 200, 30);

        gameOverLabel = new JLabel("GAME OVER");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 72));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setVisible(false);
        gameOverLabel.setHorizontalAlignment(JLabel.CENTER);

        frame.add(livesLabel);
        frame.add(gameOverLabel, BorderLayout.CENTER);
    }

    /**
     * Met à jour le HUD avec les données du modèle.
     * NE gère QUE les vies, PAS les messages.
     */
    public void draw(Game game) {
        livesLabel.setText("Vies: " + game.getLives());

        // NE PAS toucher au gameOverLabel ici
        // Les messages sont gérés par showTemporaryMessage() dans Main.java
    }

    /**
     * Affiche un message temporaire.
     */
    public void showTemporaryMessage(String message, Color color) {
        gameOverLabel.setText(message);
        gameOverLabel.setForeground(color);
        gameOverLabel.setVisible(true);
    }

    /**
     * Cache le message.
     */
    public void hideMessage() {
        gameOverLabel.setVisible(false);
    }
}