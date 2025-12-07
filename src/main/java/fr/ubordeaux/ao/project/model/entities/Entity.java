package fr.ubordeaux.ao.project.model.entities;
import fr.ubordeaux.ao.project.model.graph.Position;
import fr.ubordeaux.ao.project.model.patterns.factory.EntityType;

public class Entity {

    protected Position position;
    private boolean consumed = false;


    public Entity(Position pos) {
        this.position = pos;
    }

    public Position getPosition() {
        return this.position;
    }


    public boolean isCrossable(Player player) {
        return true;
    }


    public void onContact(Player player) {

    }


    public boolean isConsumed() {
        return false;
    }


    public EntityType getType() {
        return null;
    }

    public void consume() {
         consumed = true;
    }
}