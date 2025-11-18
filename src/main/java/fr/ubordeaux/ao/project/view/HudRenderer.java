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
 * (Responsabilité : Personne 1)
 * * Rôle : Affiche les vies et le message "GAME OVER".
 * * [cite_start]Utilise les capacités de Swing (JFrame) de l'API Mazing[cite: 16, 21].
 */
public class HudRenderer {

    private JLabel livesLabel;
    private JLabel gameOverLabel;

    public HudRenderer(IWindowGame windowGame) {
        JFrame frame = (JFrame) windowGame;// Caster la fenêtre en JFrame [cite: 26]

        // 1. Label pour les vies
        livesLabel = new JLabel("Vies: 3");
        livesLabel.setFont(new Font("Arial", Font.BOLD, 24));
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setBounds(10, 10, 200, 30); // Position en pixels (coin sup gauche)
        
        // 2. Label pour le Game Over
        gameOverLabel = new JLabel("GAME OVER");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 72));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setVisible(false); // Caché par défaut
        // Centrer le label (besoin d'un panel)
        gameOverLabel.setHorizontalAlignment(JLabel.CENTER);

        // Ajoute les labels au JFrame
        // Utilise un BorderLayout pour le Game Over, et un positionnement absolu pour les vies
        frame.add(livesLabel);
        frame.add(gameOverLabel, BorderLayout.CENTER);
    }

    /**
     * Met à jour le HUD avec les données du modèle.
     * Appelé par GameView.onGameUpdate().
     */
    public void draw(Game game) {
        // Mettre à jour le texte des vies
        livesLabel.setText("Vies: " + game.getLives());

        // Afficher "GAME OVER" si le jeu est terminé
        if (game.isGameOver()) {
            gameOverLabel.setText("GAME OVER");
            gameOverLabel.setVisible(true);
        } else if (game.isLevelComplete()) {
            gameOverLabel.setText("YOU WIN!");
            gameOverLabel.setForeground(Color.GREEN);
            gameOverLabel.setVisible(true);
        } else {
            gameOverLabel.setVisible(false);
        }
    }
}