package com.lordmau5.harvest.objects;

import com.lordmau5.harvest.objects.world.Grass;
import com.lordmau5.harvest.objects.world.Stone;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Lordmau5
 * @time: 11.06.2014 - 11:38
 */
public class ObjectRegister {

    private static List<AbstractObject> objectList = new ArrayList<>();

    public static void addObject(AbstractObject object) {
        if(!objectList.contains(object) && !object.getTextureName().isEmpty())
            objectList.add(object);
    }

    public static List<AbstractObject> getObjectList() {
        return objectList;
    }

    public static void init() {
        new Grass();
        new Stone();
    }

}
