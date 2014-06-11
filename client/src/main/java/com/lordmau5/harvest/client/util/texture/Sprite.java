package com.lordmau5.harvest.client.util.texture;

/**
 * @author: Lordmau5
 * @time: 11.06.2014 - 10:31
 */
public class Sprite {

    private final String name;
    private final int x;
    private final int y;
    private final int w;
    private final int h;

    public Sprite(String name, int x, int y, int w, int h) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Sprite sprite = (Sprite) o;

        if (h != sprite.h) {
            return false;
        }
        if (w != sprite.w) {
            return false;
        }
        if (x != sprite.x) {
            return false;
        }
        if (y != sprite.y) {
            return false;
        }
        if (name != null ? !name.equals(sprite.name) : sprite.name != null) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Sprite{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

}
