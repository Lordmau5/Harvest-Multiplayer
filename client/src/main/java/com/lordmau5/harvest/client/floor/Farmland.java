package com.lordmau5.harvest.client.floor;

import com.lordmau5.harvest.client.Tile;

/**
 * @author: Lordmau5
 * @time: 16.06.2014 - 21:10
 */
public class Farmland {

    boolean tilled = false;
    boolean watered = false;

    public Tile tile;

    public Farmland(int tX, int tY) {
        this.tile = new Tile(tX, tY);
    }

    public void setTilled(boolean tilled) {
        this.tilled = tilled;
        if(!tilled)
            setWatered(false);
    }

    public void setWatered(boolean watered) {
        this.watered = watered;
    }

    public boolean isTilled() {
        return tilled;
    }

    public boolean isWatered() {
        return watered;
    }
}
