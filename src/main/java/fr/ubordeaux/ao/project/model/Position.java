package fr.ubordeaux.ao.project.model;

import fr.ubordeaux.ao.mazing.api.Direction;

// Classe Position simple pour stocker X,Y
public class Position {
    private int x;
    private int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){ return x; }
    public int getY(){ return y; }

    public void setX(int x){ this.x = x; }
    public void setY(int y){ this.y = y; }

    // renvoie une nouvelle position selon la direction
    public Position next(Direction d){
        int nx = x, ny = y;
        switch(d){
            case NORTH -> ny -= 1;
            case SOUTH -> ny += 1;
            case EAST  -> nx += 1;
            case WEST  -> nx -= 1;
            case NORTHEAST -> { nx += 1; ny -= 1; }
            case NORTHWEST -> { nx -= 1; ny -= 1; }
            case SOUTHEAST -> { nx += 1; ny += 1; }
            case SOUTHWEST -> { nx -= 1; ny += 1; }
        }
        return new Position(nx, ny);
    }
}
