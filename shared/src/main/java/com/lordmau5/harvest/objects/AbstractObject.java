package com.lordmau5.harvest.objects;

import org.lwjgl.util.Point;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 14:01
 */
public abstract class AbstractObject {
    private final String textureName;
    private final boolean isAnimated;
    private Point position;

    public AbstractObject() {
        this("", false);
    }
    public AbstractObject(String textureName, boolean isAnimated) {
        this.textureName = textureName;
        this.isAnimated = isAnimated;

        ObjectRegister.addObject(this);
    }

    public String getTextureName() { return this.textureName; }
    public boolean isAnimated() { return this.isAnimated; }

    public void setPosition(int x, int y) {
        this.position.setLocation(x, y);
    }

    public Point getPosition() {
        return this.position;
    }

    public boolean canPassThrough() {
        return false;
    }
}