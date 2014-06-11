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
        for(int y=0; y<4; y++)
            for(int x=0; x<4; x++)
                addObject(new Stone(100 + x * 16, 100 + y * 16));
    }

    public void addObject(AbstractObject object) {
        if(!objects.contains(object)) {
            objects.add(object);
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