package com.lordmau5.harvest.objects.floor;

import com.lordmau5.harvest.objects.AbstractObject;

/**
 * @author: Lordmau5
 * @time: 13.06.2014 - 12:42
 */
public class Farmland extends AbstractObject {

    public Farmland() {
        super("farmland", false);
    }
    public Farmland(int x, int y) {
        super("farmland", false, x, y);
    }

}
