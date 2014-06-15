package com.lordmau5.harvest.client;

import org.newdawn.slick.Animation;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * @author: Lordmau5
 * @time: 15.06.2014 - 21:35
 */
public class Player {

    public float pX = 90, pY = 90;
    public Tile playerTile = new Tile((int) Math.ceil(pX / 16), (int) Math.ceil(pY / 16));
    public Tile playerFacingTile = new Tile(0, 0);
    public PlayerFacing pFacing = PlayerFacing.DOWN;

    public Animation playerAnim;

    private final Shape boundingBox;

    public Player() {
        boundingBox = new Rectangle(pX + 11, pY + 16, 11, 14);
    }

    public Shape getBoundingBox() {
        return this.boundingBox;
    }

    public void updatePos(float x, float y) {
        this.pX = x;
        this.pY = y;

        updateFacingTile();

        this.boundingBox.setLocation(x + 11, y + 16);
    }

    void updateFacingTile() {
        if(pFacing == PlayerFacing.UP) {
            playerFacingTile.updatePos(playerTile.x, playerTile.y - 1);
        }
        else if(pFacing == PlayerFacing.DOWN) {
            playerFacingTile.updatePos(playerTile.x, playerTile.y + 1);
        }
        else if(pFacing == PlayerFacing.LEFT) {
            playerFacingTile.updatePos(playerTile.x - 1, playerTile.y);
        }
        else if(pFacing == PlayerFacing.RIGHT) {
            playerFacingTile.updatePos(playerTile.x + 1, playerTile.y);
        }
    }

    public boolean intersects(Entity object) {
        if (this.getBoundingBox() == null || object == null || object.getBoundingBox() == null)
            return false;

        return this.getBoundingBox().intersects(object.getBoundingBox());
    }

}
