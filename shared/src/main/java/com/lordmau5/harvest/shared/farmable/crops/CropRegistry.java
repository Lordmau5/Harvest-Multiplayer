package com.lordmau5.harvest.shared.farmable.crops;

import com.lordmau5.harvest.shared.farmable.seeds.ISeeds;
import com.lordmau5.harvest.shared.farmable.seeds.TomatoSeeds;
import com.lordmau5.harvest.shared.farmable.seeds.TurnipSeeds;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Lordmau5
 * @time: 17.06.2014 - 10:07
 */
public class CropRegistry {

    private static Map<String, ICrop> cropMap = new HashMap<>();

    public static void addCrop(ISeeds seeds, ICrop crop) {
        cropMap.put(seeds.getSeedName(), crop);
    }

    public static ICrop buildCrop(ICrop crop) {
        try {
            ICrop cr = crop.getClass().newInstance();
            return cr;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ICrop getCrop(String seeds) {
        if(!cropMap.containsKey(seeds))
            return null;

        return cropMap.get(seeds);
    }

    public static Map<String, ICrop> getCropMap() {
        return cropMap;
    }

    static {
        addCrop(new TomatoSeeds(), new TomatoCrop());
        addCrop(new TurnipSeeds(), new TurnipCrop());
    }

}
