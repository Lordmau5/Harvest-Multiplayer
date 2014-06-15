package com.lordmau5.harvest.client;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * @author: Lordmau5
 * @time: 15.06.2014 - 21:37
 */
public class Entity {

    public final String texture;
    private final Shape boundingBox;
    public Tile tile;

    public Entity(String texture, int spriteSize, int xTile, int yTile) {
        this.texture = texture;
        this.boundingBox = new Rectangle(xTile * 16 + 1, yTile * 16, spriteSize, spriteSize);
        this.tile = new Tile(xTile, yTile);
    }

    public Shape getBoundingBox() {
        return this.boundingBox;
    }

}
