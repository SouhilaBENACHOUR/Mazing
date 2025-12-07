package fr.ubordeaux.ao.project.model.entities;

import fr.ubordeaux.ao.project.model.graph.Position;

public class Door extends Entity {
    private boolean locked = true;
    private boolean open = false;

    public Door(Position pos) {
        super(pos);
    }

    public boolean isLocked() { return locked; }

    public void tryOpen(Player player) {
        if (locked && player.hasKey()) {
            locked = false;
            player.useKey();
        }
    }

    @Override
    public boolean isCrossable(Player player) {
        if (locked) {
            tryOpen(player);
        }
        return !locked;
    }

    public void open() {
        open = true;
    }
}