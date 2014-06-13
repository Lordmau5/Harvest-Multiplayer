package com.lordmau5.harvest.environment;

/**
 * @author: Lordmau5
 * @time: 11.06.2014 - 17:03
 */
public class FloatPoint {

    private float x, y;

    public FloatPoint() {
        this(0, 0);
    }
    public FloatPoint(float x, float y) {
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
