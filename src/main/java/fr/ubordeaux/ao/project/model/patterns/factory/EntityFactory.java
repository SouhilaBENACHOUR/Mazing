package fr.ubordeaux.ao.project.model.patterns.factory;

// Imports nécessaires pour créer les entités
import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.entities.Entity;
import fr.ubordeaux.ao.project.model.entities.Player;
import fr.ubordeaux.ao.project.model.entities.Enemy;
import fr.ubordeaux.ao.project.model.entities.Wall;
// (P2 devra ajouter les imports pour Key, Door, Exit, etc.)

/**
 * Fichier "Bouchon" (Stub) pour la Personne 2.
 * Usine (Factory) pour créer toutes les entités du jeu.
 */
public class EntityFactory {

    /**
     * Crée et retourne une entité en fonction de son type et sa position.
     * * --- MODIFICATION ---
     * Accepte maintenant (EntityType, Position) pour correspondre à Game.java
     * * @param type Le type d'entité à créer (PLAYER, ENEMY, etc.)
     * @param pos  La position où créer l'entité
     * @return L'entité créée, ou null si le type est inconnu.
     */
    public static Entity createEntity(EntityType type, Position pos) {

        // P2 devra implémenter la logique complète ici
        // Pour l'instant, nous créons les stubs minimums pour que P1 fonctionne

        switch (type) {
            case PLAYER:
                return new Player(pos); // P1 a besoin de ça
            case ENEMY:
                return new Enemy(pos); // P1 a besoin de ça
            case WALL:
                return new Wall(pos); // P1 a besoin de ça (pour isWalkable)

            // P2 ajoutera KEY, DOOR, EXIT...
            // case KEY:
            //    return new Key(pos);
            // case DOOR:
            //    return new Door(pos);
            // case EXIT:
            //    return new Exit(pos);

            default:
                // Pour FLOOR (' ') ou les types inconnus
                return null;
        }
    }
}