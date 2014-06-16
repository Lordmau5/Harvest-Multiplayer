package com.lordmau5.harvest.client;

import com.lordmau5.harvest.client.objects.Entity;
import com.lordmau5.harvest.client.objects.PlayerBoundaries;
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

    public Entity holding;

    private final Shape[] boundingBox;

    public Player() {
        boundingBox = new Shape[4];
        boundingBox[PlayerBoundaries.TOP_LEFT.ordinal()] = new Rectangle(pX + 11, pY + 16, 6, 6);
        boundingBox[PlayerBoundaries.TOP_RIGHT.ordinal()] = new Rectangle(pX + 17, pY + 16, 6, 6);

        boundingBox[PlayerBoundaries.BOTTOM_LEFT.ordinal()] = new Rectangle(pX + 11, pY + 24, 6, 6);
        boundingBox[PlayerBoundaries.BOTTOM_RIGHT.ordinal()] = new Rectangle(pX + 17, pY + 24, 6, 6);
    }

    public void updatePos(float x, float y) {
        this.pX = x;
        this.pY = y;

        updateFacingTile();

        boundingBox[PlayerBoundaries.TOP_LEFT.ordinal()].setLocation(pX + 11, pY + 16);
        boundingBox[PlayerBoundaries.TOP_RIGHT.ordinal()].setLocation(pX + 17, pY + 16);

        boundingBox[PlayerBoundaries.BOTTOM_LEFT.ordinal()].setLocation(pX + 11, pY + 24);
        boundingBox[PlayerBoundaries.BOTTOM_RIGHT.ordinal()].setLocation(pX + 17, pY + 24);
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

    public boolean intersects(Entity ent) {
        for(Shape bBox : this.boundingBox)
            if(bBox.intersects(ent.getBoundingBox()))
                return true;

        return false;
    }

    public int[] calculateIntersection(Entity ent) {
        int[] ret = {0, 0};

        boolean[] inters = new boolean[4];

        inters[0] = boundingBox[PlayerBoundaries.TOP_LEFT.ordinal()].intersects(ent.getBoundingBox());
        inters[1] = boundingBox[PlayerBoundaries.TOP_RIGHT.ordinal()].intersects(ent.getBoundingBox());
        inters[2] = boundingBox[PlayerBoundaries.BOTTOM_LEFT.ordinal()].intersects(ent.getBoundingBox());
        inters[3] = boundingBox[PlayerBoundaries.BOTTOM_RIGHT.ordinal()].intersects(ent.getBoundingBox());

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
