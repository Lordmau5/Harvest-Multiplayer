package com.lordmau5.harvest.shared.objects.doubleTile;

import com.lordmau5.harvest.shared.objects.Entity;

/**
 * @author: Lordmau5
 * @time: 16.06.2014 - 09:45
 */
public class BigStone extends Entity {
    public BigStone() {
        super("bigStone", 32, -1, -1);
    }
    public BigStone(int xTile, int yTile) {
        super("bigStone", 32, xTile, yTile);
    }
}
