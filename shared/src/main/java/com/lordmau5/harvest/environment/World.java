package com.lordmau5.harvest.environment;

import com.lordmau5.harvest.objects.AbstractObject;
import com.lordmau5.harvest.objects.floor.Farmland;
import com.lordmau5.harvest.objects.world.Grass;
import com.lordmau5.harvest.objects.world.Stone;

import java.util.*;

/**
 * Author: Lordmau5
 * Date: 26.03.14
 * Time: 20:05
 */
public class World {
    private Map<Tile, AbstractObject> objects = new HashMap<>();
    private Map<Tile, AbstractObject> land = new HashMap<>();
    //private List<AbstractObject> objects = new ArrayList<>();
    private AbstractObject[] validObjects = {new Grass(), new Stone()};

    public World() {
        for(int y=0; y<8; y++)
            for(int x=0; x<8; x++)
                addFarmland(new Tile(x, y), new Farmland(x, y));
        randomGenerateObjects();
    }

    public void randomGenerateObjects() {
        objects.clear();

        Random random = new Random();

        int maxGen = 20;
        while(maxGen > 0) {
            AbstractObject obj = validObjects[random.nextInt(validObjects.length)];

            int x = random.nextInt(7);
            int y = random.nextInt(7);
            if(isObjectAtPosition(x, y))
                continue;

            addObject(new Tile(x, y), obj);
            maxGen--;
        }

        checkDupes();
    }

    void checkDupes() {
        for(Map.Entry<Tile, ?> entry : objects.entrySet()) {
            System.out.println(entry.getKey().toString());
        }
    }

    public void addObject(Tile position, AbstractObject object) {
        if(!objects.containsKey(position)) {
            objects.put(position, object);
        }
    }

    public void addFarmland(Tile position, AbstractObject object) {
        if(!land.containsKey(position))
            land.put(position, object);
    }

    public Map<Tile, AbstractObject> getObjects() {
        return objects;
    }

    public Map<Tile, AbstractObject> getFarmland() {
        return land;
    }

    public boolean isObjectAtPosition(int x, int y) {
        return objects.containsKey(new Tile(x, y));
    }
}