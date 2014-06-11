package com.lordmau5.harvest.environment;

/**
 * @author: Lordmau5
 * @time: 11.06.2014 - 17:03
 */
public class Point {

    private float x, y;

    public Point() {
        this(0, 0);
    }
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() { return this.x; }

    public float getY() { return this.y; }

}
