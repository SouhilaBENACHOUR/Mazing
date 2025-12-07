package fr.ubordeaux.ao.project.model.entities;

import fr.ubordeaux.ao.project.model.graph.Position;

public class Key extends Entity {

    public Key(Position pos) {
        super(pos);
    }

    @Override
    public void onContact(Player player) {
        player.collectKey(this);
        this.consume();
    }
}