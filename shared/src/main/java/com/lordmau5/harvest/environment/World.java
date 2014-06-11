package com.lordmau5.harvest.environment;

import com.lordmau5.harvest.objects.AbstractObject;
import com.lordmau5.harvest.objects.world.Stone;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lordmau5
 * Date: 26.03.14
 * Time: 20:05
 */
public class World {
    private List<AbstractObject> objects = new ArrayList<>();

    public World() {
        addObject(new Stone(100, 100));
        addObject(new Stone(100, 116));
        addObject(new Stone(116, 100));
        addObject(new Stone(116, 116));
    }

    public void addObject(AbstractObject object) {
        if(!objects.contains(object)) {
            objects.add(object);
            System.out.println(object.getPosition().getX() + " - " + object.getPosition().getY());
        }
    }

    public List<AbstractObject> getObjects() {
        return objects;
    }

    public boolean isObjectAtPosition(float x, float y) {
        if(objects.isEmpty())
            return false;

        for(AbstractObject obj : objects) {
            Point pos = obj.getPosition();
            if(x == pos.getX() && y == pos.getY())
                return true;
        }
        return false;
    }
}