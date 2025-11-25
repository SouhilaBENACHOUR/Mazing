package fr.ubordeaux.ao.project.view;

import fr.ubordeaux.ao.mazing.api.Direction;
import fr.ubordeaux.ao.mazing.api.Spider;
import fr.ubordeaux.ao.project.model.entities.Enemy;

public class EnemyView extends Spider {

    private final Enemy model;

    public EnemyView(Enemy model) {
        this.model = model;

        switch (model.getSize()) {
            case SMALL  -> { setScale(1.5f); setFrameRate(Mode.WALK, 0.7f); }
            case MEDIUM -> { setScale(2.5f); setFrameRate(Mode.WALK, 1.0f); }
            case LARGE  -> { setScale(3.5f); setFrameRate(Mode.WALK, 1.7f); }
        }

        setMode(Mode.WALK);
        setDirection(Direction.WEST);

        float x = model.getPosition().getX();
        float y = model.getPosition().getY();

        setPosition(x, y, 0);
    }

    public Enemy getModel() {
        return model;
    }
}
