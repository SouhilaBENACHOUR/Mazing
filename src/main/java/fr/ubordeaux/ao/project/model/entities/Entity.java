package fr.ubordeaux.ao.project.model.entities;
import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.entities.Player;
import fr.ubordeaux.ao.project.model.patterns.factory.EntityType;

public class Entity {

    protected Position position;

    public Entity(Position pos) {
        this.position = pos;
    }

    // --- Méthodes appelées par Game.java ---

    public Position getPosition() {
        return this.position;
    }

    // Permet-on au joueur de traverser ?
    public boolean isCrossable(Player player) {
        return true; // Par défaut, on peut tout traverser
    }

    // Qu'arrive-t-il si le joueur entre en contact ?
    public void onContact(Player player) {
        // Ne fait rien par défaut
    }

    // L'objet doit-il disparaître après contact ? (ex: une clé)
    public boolean isConsumed() {
        return false; // Non par défaut
    }

    // Quel est le type de cette entité ?
    public EntityType getType() {
        return null; // P2 doit implémenter ça
    }
}