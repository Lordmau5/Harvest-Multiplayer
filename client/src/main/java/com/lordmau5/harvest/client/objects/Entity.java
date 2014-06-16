package com.lordmau5.harvest.client.objects;

import com.lordmau5.harvest.client.Tile;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * @author: Lordmau5
 * @time: 15.06.2014 - 21:37
 */
public class Entity {

    public final String texture;
    private Shape boundingBox;
    public Tile tile;
    private int spriteSize;

    public Entity(String texture, int spriteSize, int xTile, int yTile) {
        this.spriteSize = spriteSize;
        this.texture = texture;
        this.boundingBox = new Rectangle(xTile * 16 + 1, yTile * 16, spriteSize, spriteSize);
        this.tile = new Tile(xTile, yTile);
    }

    public void updatePos(int xTile, int yTile) {
        this.boundingBox.setLocation(xTile * 16 + 1, yTile * 16);
        this.tile.updatePos(xTile, yTile);
    }

    public Shape getBoundingBox() {
        return this.boundingBox;
    }

    public Entity clone() {
        try {
            return this.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
