package MatrixObjects;

import java.awt.*;

public class Tile {
    public int ownerId;  // the ID of the player who owns the tile (0 means neutral)
    public Color color;    // the color of the tile
    public boolean isTrail;

    public int trailId;// whether the tile is part of a player's trail

    public int positionX;

    public int positionY;



    public Tile(int x, int y) {
        this.ownerId = 0;
        this.color = Color.GRAY;
        this.isTrail = false;
        trailId = 0;
        positionX = x;
        positionY = y;
    }

    public Tile(Tile other) {
        this.positionX = other.positionX;
        this.positionY = other.positionY;
        this.ownerId = other.ownerId;
    }



    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isTrail() {
        return isTrail;
    }

    public void setTrail(boolean trail) {
        isTrail = trail;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}