package com.lordmau5.harvest.objects.world;

import com.lordmau5.harvest.objects.AbstractObject;

/**
 * @author: Lordmau5
 * @time: 11.06.2014 - 13:37
 */
public class Stone extends AbstractObject {

    public Stone() { super("stone", false); }
    public Stone(int x, int y) {
        super("stone", false, x, y);
    }
}
