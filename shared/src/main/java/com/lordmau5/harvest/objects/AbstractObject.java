package com.lordmau5.harvest.objects;

import com.lordmau5.harvest.environment.Tile;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 14:01
 */
public abstract class AbstractObject {
    private final String textureName;
    private final boolean isAnimated;
    private Tile position;

    public AbstractObject() {
        this("", false);
    }
    public AbstractObject(String textureName, boolean isAnimated) {
        this.textureName = textureName;
        this.isAnimated = isAnimated;

        this.position = new Tile(0, 0);
        ObjectRegister.addObject(this);
    }

    public AbstractObject(String textureName, boolean isAnimated, int x, int y) {
        this(textureName, isAnimated);
        this.position = new Tile(x, y);
    }

    public String getTextureName() { return textureName; }
    public boolean isAnimated() { return isAnimated; }

    public Tile getPosition() {
        return position;
    }

    public boolean canPassThrough() {
        return false;
    }
}