package fr.ubordeaux.ao.project.model;

public class Ennemie {
    public enum Size { SMALL, MEDIUM, LARGE }

    private Size size;
    private float speed;
    private float x, y;

    public Ennemie(Size size, float x, float y) {
        this.size = size;
        this.x = x;
        this.y = y;

        switch(size) {
            case SMALL  -> speed = 0.15f;
            case MEDIUM -> speed = 0.10f;
            case LARGE  -> speed = 0.05f;
        }
    }

    public void update() {
        x -= speed;
    }

    public Size getSize() { return size; }
    public float getSpeed() { return speed; }
    public float getX() { return x; }
    public float getY() { return y; }
}
