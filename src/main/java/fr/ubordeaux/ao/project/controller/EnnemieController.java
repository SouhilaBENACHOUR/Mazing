package fr.ubordeaux.ao.project.controller;

import fr.ubordeaux.ao.mazing.api.ICharacter;
import fr.ubordeaux.ao.project.model.Ennemie;
import fr.ubordeaux.ao.project.view.EnnemieView;

import java.util.function.Predicate;

public class EnnemieController  implements Predicate<ICharacter<?>> {

    private Ennemie model;
    private EnnemieView view;

    public EnnemieController(EnnemieView view, Ennemie model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public boolean test(ICharacter<?> character) {

        model.update();

        view.setPosition(model.getX(), model.getY(), 0);

        return true;
    }
}
