package fr.ubordeaux.ao.project.model.entities;

import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.patterns.factory.EntityType;

/**
 * Représente la sortie d'un niveau.
 * Le joueur doit atteindre cette position pour terminer le niveau.
 */
public class Exit extends Entity {

    public Exit(Position position) {
        super(position);
    }

    @Override
    public boolean isCrossable(Player player) {
        return true;
    }

    @Override
    public void onContact(Player player) {
    }

    @Override
    public EntityType getType() {
        return EntityType.EXIT;
    }
}
