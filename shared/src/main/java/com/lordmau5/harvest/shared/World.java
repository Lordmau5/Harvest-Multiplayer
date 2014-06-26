package com.lordmau5.harvest.shared;

import com.lordmau5.harvest.shared.farmable.crops.CropState;
import com.lordmau5.harvest.shared.farmable.crops.ICrop;
import com.lordmau5.harvest.shared.floor.Farmland;
import com.lordmau5.harvest.shared.objects.Entity;
import com.lordmau5.harvest.shared.objects.doubleTile.BigStone;
import com.lordmau5.harvest.shared.objects.singleTile.Grass;
import com.lordmau5.harvest.shared.player.Player;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Lordmau5
 * @time: 17.06.2014 - 21:53
 */
public class World {

    public static Map<String, Entity> entities = new HashMap<>();

    public int farmWidth = 32;
    public int farmHeight = 32;
    private Map<Tile, Entity> objects = new HashMap<>();
    private Map<Tile, Farmland> farmLand = new HashMap<>();

    public World() {

    }

    public void setFarmLand(Map<Tile, Farmland> farmLand) {
        this.farmLand = farmLand;
    }

    public Map<Tile, Farmland> getFarmland() {
        return farmLand;
    }

    public void setObjects(Map<Tile, Entity> objects) {
        this.objects = objects;
    }

    public Map<Tile, Entity> getObjects() {
        return objects;
    }

    public boolean isSurroundingOk(Player player) {
        boolean ret = true;
        int[] inter = intersectsWithAny(player);
        int[] interLand = intersectsWithFarmlandCrops(player);
        if(inter[0] != 0 || inter[1] != 0 || interLand[0] != 0 || interLand[1] != 0)
            ret = false;

        if(!ret) {
            player.updatePos(player.pX + inter[0] + interLand[0], player.pY + inter[1] + interLand[1]);
            return false;
        }
        return true;
    }

    private int[] intersectsWithAny(Player player) {
        for(Map.Entry<Tile, Entity> entry : objects.entrySet()) {
            Entity ent = entry.getValue();
            if(player.intersects(ent))
                return player.calculateIntersection(ent.getBoundingBox());
        }
        return new int[]{0, 0};
    }

    private int[] intersectsWithFarmlandCrops(Player player) {
        for(Map.Entry<Tile, Farmland> entry : farmLand.entrySet()) {
            Farmland land = entry.getValue();
            if(!land.hasCrop() || land.getCrop().getCropState() == CropState.SEEDS)
                continue;

            if(player.intersects(land.boundingBox))
                return player.calculateIntersection(land.boundingBox);
        }
        return new int[]{0, 0};
    }

    public boolean isEntityInRectangle(Shape shape) {
        for(Map.Entry<Tile, Entity> entry : objects.entrySet())
            if(entry.getValue().getBoundingBox().intersects(shape))
                return true;
        return false;
    }

    public Entity getObjectAtPosition(int tX, int tY) {
        Tile tile = new Tile(tX, tY);
        if(!objects.containsKey(tile))
            return null;
        return objects.get(tile);
    }

    public Entity getObjectAtPosition(Tile tile) {
        return objects.containsKey(tile) ? objects.get(tile) : null;
    }

    public void addEntity(String name, int tX, int tY) {
        if(!entities.containsKey(name))
            return;

        Entity ent = entities.get(name).newInstance();
        ent.updatePos(tX, tY);
        objects.put(new Tile(tX, tY), ent);
        fixFarmlands(new Rectangle(tX * 16 + 4, tY * 16 + 4, ent.spriteSize - 8, ent.spriteSize - 8));
    }

    public boolean removeObject(Tile facingTile) {
        Rectangle faceRect = new Rectangle(facingTile.getX() * 16 + 4, facingTile.getY() * 16 + 4, 7, 7);
        for (Map.Entry<Tile, Entity> entry : objects.entrySet()) {
            Entity ent = entry.getValue();
            if (faceRect.intersects(ent.getBoundingBox())) {
                objects.remove(entry.getKey());
                return true;
            }
        }
        return false;
    }

    public void fixFarmlands(Shape shape) {
        Rectangle landRect = new Rectangle(0, 0, 8, 8);
        for(Map.Entry<Tile, Farmland> entry : farmLand.entrySet()) {
            Farmland land = entry.getValue();
            Tile tile = entry.getKey();
            landRect.setLocation(tile.getX() * 16 + 4, tile.getY() * 16 + 4);
            if(landRect.intersects(shape))
                land.setTilled(false);
        }
    }

    public void addCrop(Tile tile, ICrop crop) {
        if(farmLand.containsKey(tile)) {
            Farmland land = farmLand.get(tile);
            if(!land.hasCrop())
                land.setCrop(crop);
        }
    }

    public void removeCrops(Tile tile) {
        if(farmLand.containsKey(tile)) {
            Farmland land = farmLand.get(tile);
            land.setCrop(null);
        }
    }

    static {
        entities.put("grass", new Grass());
        entities.put("bigStone", new BigStone());
    }

}
