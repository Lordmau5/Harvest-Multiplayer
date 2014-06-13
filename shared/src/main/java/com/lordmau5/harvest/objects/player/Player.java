package com.lordmau5.harvest.objects.player;

import com.lordmau5.harvest.environment.FloatPoint;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 14:00
 */
public class Player {

    private final String textureName;
    private final boolean isAnimated;
    private FloatPoint position;

    public Player() {
        this("player", true, 0, 0);
    }

    public Player(String textureName, boolean isAnimated, float x, float y) {
        this.textureName = textureName;
        this.isAnimated = isAnimated;

        this.position = new FloatPoint(x, y);
        System.out.println(position.getX() + " - " + position.getY());
    }

    public FloatPoint getPosition() { return this.position; }
}