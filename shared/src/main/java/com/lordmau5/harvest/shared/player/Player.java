package com.lordmau5.harvest.shared.player;

import com.lordmau5.harvest.shared.Tile;
import com.lordmau5.harvest.shared.World;
import com.lordmau5.harvest.shared.farmable.crops.CropRegistry;
import com.lordmau5.harvest.shared.farmable.crops.CropState;
import com.lordmau5.harvest.shared.farmable.crops.ICrop;
import com.lordmau5.harvest.shared.farmable.seeds.ISeeds;
import com.lordmau5.harvest.shared.floor.Farmland;
import com.lordmau5.harvest.shared.items.IUseable;
import com.lordmau5.harvest.shared.objects.Entity;
import com.lordmau5.harvest.shared.objects.IPickupable;
import com.lordmau5.harvest.shared.objects.PlayerBoundaries;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Lordmau5
 * @time: 15.06.2014 - 21:35
 */
public class Player {

    public static Map<String, Animation> playerAnims = new HashMap<>();

    public float pX = 90, pY = 90;
    public Tile playerTile = new Tile((int) Math.ceil(pX / 16), (int) Math.ceil(pY / 16));
    public Tile playerFacingTile = new Tile(0, 0);
    public PlayerFacing pFacing = PlayerFacing.DOWN;

    private World world;

    private String username;
    public Animation playerAnim;

    public Entity holding;
    public IUseable useable;

    private final Shape[] boundingBox;

    public Player(String username) {
        this.username = username;

        playerAnim = new Animation(new SpriteSheet(playerAnims.get("stand").getImage(pFacing.ordinal()), 32, 32), 1000);

        boundingBox = new Shape[4];
        boundingBox[PlayerBoundaries.TOP_LEFT.ordinal()] = new Rectangle(pX + 11, pY + 16, 6, 6);
        boundingBox[PlayerBoundaries.TOP_RIGHT.ordinal()] = new Rectangle(pX + 17, pY + 16, 6, 6);

        boundingBox[PlayerBoundaries.BOTTOM_LEFT.ordinal()] = new Rectangle(pX + 11, pY + 24, 6, 6);
        boundingBox[PlayerBoundaries.BOTTOM_RIGHT.ordinal()] = new Rectangle(pX + 17, pY + 24, 6, 6);
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public String getUsername() {
        return username;
    }

    public boolean walk(PlayerFacing facing, int delta, boolean isRunning) {
        boolean walkBlocked = false;
        float modifier = isRunning ? 0.2f : 0.1f;
        if(facing == PlayerFacing.UP) {
            playerAnim = isCarrying() ? (isRunning ? playerAnims.get("carryUpRun") : playerAnims.get("carryUp")) : (isRunning ? playerAnims.get("runUp") : playerAnims.get("walkUp"));
            playerTile.updatePos((int) Math.floor((pX + 15) / 16), (int) Math.ceil((pY - 20 - delta * 0.1f) / 16));

            if(world.isSurroundingOk(this) && pY > 0)
                pY -= delta * modifier;
            else
                walkBlocked = true;

            pFacing = PlayerFacing.UP;
        }
        else if(facing == PlayerFacing.DOWN) {
            playerAnim = isCarrying() ? (isRunning ? playerAnims.get("carryDownRun") : playerAnims.get("carryDown")) : (isRunning ? playerAnims.get("runDown") : playerAnims.get("walkDown"));
            playerTile.updatePos((int) Math.floor((pX + 15) / 16), (int) Math.ceil((pY - 20 + delta * 0.1f) / 16));

            if(world.isSurroundingOk(this) && pY + playerAnim.getHeight() < world.farmHeight * 16)
                pY += delta * modifier;
            else
                walkBlocked = true;

            pFacing = PlayerFacing.DOWN;
        }
        else if(facing == PlayerFacing.LEFT) {
            playerAnim = isCarrying() ? (isRunning ? playerAnims.get("carryLeftRun") : playerAnims.get("carryLeft")) : (isRunning ? playerAnims.get("runLeft") : playerAnims.get("walkLeft"));
            playerTile.updatePos((int) Math.ceil((pX - delta * 0.1f) / 16), (int) Math.floor((pY + 8) / 16));

            if(world.isSurroundingOk(this) && pX > 0)
                pX -= delta * modifier;
            else
                walkBlocked = true;

            pFacing = PlayerFacing.LEFT;
        }
        else if(facing == PlayerFacing.RIGHT) {
            playerAnim = isCarrying() ? (isRunning ? playerAnims.get("carryRightRun") : playerAnims.get("carryRight")) : (isRunning ? playerAnims.get("runRight") : playerAnims.get("walkRight"));
            playerTile.updatePos((int) Math.ceil((pX + delta * 0.1f) / 16), (int) Math.floor((pY + 8) / 16));

            if(world.isSurroundingOk(this) && pX + playerAnim.getWidth() < world.farmWidth * 16)
                pX += delta * modifier;
            else
                walkBlocked = true;

            pFacing = PlayerFacing.RIGHT;
        }
        setPlayerTileBasedOnPosition();
        return walkBlocked;
    }

    public void doAction(int keycode) {
        switch(keycode) {
            case Keyboard.KEY_C: { // Pickup / place down items (currently only Grass)
                if(holding != null) {
                    if(world.isEntityInRectangle(new Rectangle(playerFacingTile.getX() * 16 + 4, playerFacingTile.getY() * 16 + 4, 7, 7)))
                        return;

                    world.addGrass(playerFacingTile.getX(), playerFacingTile.getY());
                    holding = null;
                    return;
                }

                Entity ent = world.getObjectAtPosition(playerFacingTile.getX(), playerFacingTile.getY());
                if(ent == null || !(ent instanceof IPickupable))
                    return;

                world.removeObject(playerFacingTile);
                holding = ent;
            }
            case Keyboard.KEY_B: {
                Entity ent = getWorld().getObjectAtPosition(playerFacingTile.getX(), playerFacingTile.getY());
                if(ent != null)
                    return;

                Farmland land = getWorld().getFarmland().get(playerFacingTile);

                land.setTilled(!land.isTilled());
            }
            case Keyboard.KEY_N: {
                Entity ent = getWorld().getObjectAtPosition(playerFacingTile.getX(), playerFacingTile.getY());
                if(ent != null)
                    return;

                Farmland land = getWorld().getFarmland().get(playerFacingTile);
                if(!land.isTilled())
                    return;

                land.setWatered(!land.isWatered());
            }
            case Keyboard.KEY_P: {
                Entity ent = getWorld().getObjectAtPosition(playerFacingTile.getX(), playerFacingTile.getY());
                if(ent != null)
                    return;

                Farmland land = getWorld().getFarmland().get(playerFacingTile);
                if(land == null || !land.isTilled() || land.hasCrop())
                    return;

                if(!(useable instanceof ISeeds))
                    return;

                Tile cropTile = new Tile(playerFacingTile.getX(), playerFacingTile.getY());
                getWorld().addCrop(cropTile, CropRegistry.buildCrop(CropRegistry.getCrop((ISeeds) useable)));
            }
            case Keyboard.KEY_I: {
                Entity ent = getWorld().getObjectAtPosition(playerFacingTile.getX(), playerFacingTile.getY());
                if(ent != null)
                    return;

                Farmland land = getWorld().getFarmland().get(playerFacingTile);
                if(land == null || !land.isTilled() || !land.hasCrop())
                    return;

                ICrop crop = land.getCrop();
                System.out.println(crop != null ? (crop.getSeeds().getSeedName() + " - " + crop.getCropState().toString()) : "None");
            }
            case Keyboard.KEY_L: {
                if(getWorld().getFarmland().isEmpty())
                    return;

                List<Tile> remove = new ArrayList<>();
                for(Map.Entry<Tile, Farmland> entry : getWorld().getFarmland().entrySet()) {
                    entry.getValue().operate();

                    if(entry.getValue().hasCrop())
                        if(entry.getValue().getCrop().getCropState() == CropState.GRASS_MORPH)
                            remove.add(entry.getKey());
                }
                if(!remove.isEmpty())
                    for(Tile cropTile : remove) {
                        getWorld().removeCrops(cropTile);
                        getWorld().addGrass(cropTile.getX(), cropTile.getY());
                    }
            }
        }
    }

    public void setPlayerTileBasedOnPosition() {
        int x = (int) Math.ceil(pX / 16);
        int y = (int) Math.ceil((pY + 10) / 16);
        playerTile.updatePos(x, y);
    }

    private boolean isCarrying() {
        return holding != null;
    }

    public void updatePos(float x, float y) {
        this.pX = x;
        this.pY = y;

        setPlayerTileBasedOnPosition();

        updateFacingTile();

        boundingBox[PlayerBoundaries.TOP_LEFT.ordinal()].setLocation(pX + 11, pY + 16);
        boundingBox[PlayerBoundaries.TOP_RIGHT.ordinal()].setLocation(pX + 17, pY + 16);

        boundingBox[PlayerBoundaries.BOTTOM_LEFT.ordinal()].setLocation(pX + 11, pY + 24);
        boundingBox[PlayerBoundaries.BOTTOM_RIGHT.ordinal()].setLocation(pX + 17, pY + 24);
    }

    void updateFacingTile() {
        if(pFacing == PlayerFacing.UP) {
            playerFacingTile.updatePos(playerTile.getX(), playerTile.getY() - 1);
        }
        else if(pFacing == PlayerFacing.DOWN) {
            playerFacingTile.updatePos(playerTile.getX(), playerTile.getY() + 1);
        }
        else if(pFacing == PlayerFacing.LEFT) {
            playerFacingTile.updatePos(playerTile.getX() - 1, playerTile.getY());
        }
        else if(pFacing == PlayerFacing.RIGHT) {
            playerFacingTile.updatePos(playerTile.getX() + 1, playerTile.getY());
        }
    }

    public boolean intersects(Shape shape) {
        for(Shape bBox : this.boundingBox)
            if(bBox.intersects(shape))
                return true;

        return false;
    }

    public boolean intersects(Entity ent) {
        for(Shape bBox : this.boundingBox)
            if(bBox.intersects(ent.getBoundingBox()))
                return true;

        return false;
    }

    public int[] calculateIntersection(Shape bBox) {
        int[] ret = {0, 0};

        boolean[] inters = new boolean[4];

        inters[0] = boundingBox[PlayerBoundaries.TOP_LEFT.ordinal()].intersects(bBox);
        inters[1] = boundingBox[PlayerBoundaries.TOP_RIGHT.ordinal()].intersects(bBox);
        inters[2] = boundingBox[PlayerBoundaries.BOTTOM_LEFT.ordinal()].intersects(bBox);
        inters[3] = boundingBox[PlayerBoundaries.BOTTOM_RIGHT.ordinal()].intersects(bBox);

        if (inters[0]) {
            if(pFacing == PlayerFacing.LEFT && !inters[1])
                ret[1] += 1;
            else if(pFacing == PlayerFacing.UP && !inters[1])
                ret[0] += 1;
        }
        if (inters[1]) {
            ret[1] += 1;
            if(pFacing == PlayerFacing.RIGHT && !inters[0])
                ret[1] += 1;
            else if(pFacing == PlayerFacing.UP && !inters[0])
                ret[0] -= 1;
        }
        if (inters[2]) {
            if(pFacing == PlayerFacing.LEFT)
                ret[1] -= 1;
            else if(pFacing == PlayerFacing.DOWN && !inters[3])
                ret[0] += 1;
        }
        if(inters[3]) {
            if(pFacing == PlayerFacing.RIGHT)
                ret[1] -= 1;
            else if(pFacing == PlayerFacing.DOWN && !inters[2])
                ret[0] -= 1;
        }

        if(inters[2] && inters[3])
            ret[1] = -1;
        if(inters[0] && inters[1])
            ret[0] = 0;
        if(inters[0] && inters[2])
            return new int[] {1, 0};
        if(inters[1] && inters[3])
            return new int[] {-1, 0};

        return ret;
    }

}
