package fr.ubordeaux.ao.project.model.patterns.factory;
import fr.ubordeaux.ao.project.model.entities.*;
import fr.ubordeaux.ao.project.model.graph.Position;

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

        switch (type) {
            case PLAYER:
                return new Player(pos);
            case ENEMY_SMALL:
                return new Enemy(pos, Enemy.Size.SMALL);
            case ENEMY_MEDIUM:
                return new Enemy(pos, Enemy.Size.MEDIUM);
            case ENEMY_LARGE:
                return new Enemy(pos, Enemy.Size.LARGE);

            case WALL:
                return new Wall(pos);
             case KEY:
               return new Key(pos);
            case DOOR:
               return new Door(pos);
             case EXIT:
               return new Exit(pos);

            default:
                return null;
        }
    }
}