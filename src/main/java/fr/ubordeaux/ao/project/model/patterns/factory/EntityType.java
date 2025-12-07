package fr.ubordeaux.ao.project.model.patterns.factory;

/**
 * Énumération de tous les types d'entités pouvant être créés.
 * (Responsabilité : Personne 2, mais ce stub est nécessaire pour P1)
 * * Rôle : Associe un caractère (lu depuis le fichier .txt) à un type d'entité.
 */
public enum EntityType {
    PLAYER('P'),
    ENEMY_SMALL('S'),
    ENEMY_MEDIUM('M'),
    ENEMY_LARGE('L'),
    KEY('K'),
    WALL('W'),
    DOOR('D'),
    EXIT('X'),
    FLOOR(' ');
    private final char charRepresentation;


    /**
     * Constructeur privé pour l'énumération.
     * @param c Le caractère représentant l'entité dans les fichiers de niveau.
     */
    EntityType(char c) {
        this.charRepresentation = c;
    }

    /**
     * C'est la méthode que vous avez demandée (getChar).
     * Elle est nécessaire pour Maze.java (ex: maze.isWalkable).
     * @return Le caractère associé à ce type d'entité.
     */
    public char getChar() {
        return this.charRepresentation;
    }

    /**
     * Méthode statique pour trouver un type d'entité à partir d'un caractère.
     * (Nécessaire pour Game.java, dans populateEntitiesFromMaze)
     * @param c Le caractère lu depuis le fichier .txt.
     * @return L'EntityType correspondant, ou null si aucun n'est trouvé.
     */
    public static EntityType fromChar(char c) {
        for (EntityType type : EntityType.values()) {
            if (type.getChar() == c) {
                return type;
            }
        }
        return null;
    }
}