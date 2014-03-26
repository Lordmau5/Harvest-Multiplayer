package com.github.lordmau5.harvest.objects.player;

import com.github.lordmau5.harvest.objects.AbstractObject;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 14:00
 */
public class Player extends AbstractObject {

    public Player() {
        super("player", true);
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }
}