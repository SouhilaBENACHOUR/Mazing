package fr.ubordeaux.ao.project.view;

import fr.ubordeaux.ao.mazing.api.Direction;
import fr.ubordeaux.ao.mazing.api.Spider;
import fr.ubordeaux.ao.project.model.Ennemie;

public class EnnemieView extends Spider {

    private final Ennemie model;

    public EnnemieView(Ennemie model) {
        this.model = model;

        switch (model.getSize()) {
            case SMALL  -> { setScale(1.5f); setFrameRate(Mode.WALK, 0.7f); }
            case MEDIUM -> { setScale(2.5f); setFrameRate(Mode.WALK, 1.0f); }
            case LARGE  -> { setScale(3.5f); setFrameRate(Mode.WALK, 1.7f); }
        }

        setMode(Mode.WALK);
        setDirection(Direction.WEST);
        setPosition(model.getX(), model.getY(), 0);
    }

    public Ennemie getModel() {
        return model;
    }

    public void update() {
        setPosition(model.getX(), model.getY(), 0);
    }
}
