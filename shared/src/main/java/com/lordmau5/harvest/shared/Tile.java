package com.lordmau5.harvest.shared;

/**
 * @author: Lordmau5
 * @time: 15.06.2014 - 18:22
 */
public class Tile {

    private int x, y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void updatePos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object otherTile) {
        return ((Tile)otherTile).x == x && ((Tile)otherTile).y == y;
    }

    @Override
    public String toString() {
        return "Tile_" + x + "_" + y;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 37 * hash + x;
        hash = 37 * hash + y;
        return hash;
    }

}
