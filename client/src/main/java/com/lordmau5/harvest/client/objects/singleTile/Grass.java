package com.lordmau5.harvest.client.objects.singleTile;

import com.lordmau5.harvest.client.objects.Entity;

/**
 * @author: Lordmau5
 * @time: 16.06.2014 - 09:45
 */
public class Grass extends Entity {
    public Grass() {
        super("grass", 16, -1, -1);
    }
    public Grass(int xTile, int yTile) {
        super("grass", 16, xTile, yTile);
    }
}
