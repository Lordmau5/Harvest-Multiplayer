package com.lordmau5.harvest.client;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 11:43
 */
public class Client extends BasicGame {

    public Client() {
        super("Harvest Multiplayer");
    }

    //--------------------------------------------------------

    TiledMap farmLandTest;
    boolean[][] blocked = new boolean[1024][1024];

    SpriteSheet facing;

    Animation a_up, a_down, a_left, a_right;
    SpriteSheet s_up, s_down, s_left;

    public static void main(String args[]) {
        AppGameContainer game;
        try {
            game = new AppGameContainer(new Client());
            game.setDisplayMode(800, 600, false);
            game.setShowFPS(false);
            game.setUpdateOnlyWhenVisible(false);
            game.start();

        } catch (SlickException e) {
            e.printStackTrace();
        }

    }

    Image grass;
    Map<Tile, Entity> grassPos = new HashMap<>();

    Player player;

    @Override
    public void init(GameContainer container) throws SlickException {
        farmLandTest = new TiledMap("textures/farmland_map.tmx");

        //-------------------------------------------------------

        s_up = new SpriteSheet("textures/plwalkup.png", 32, 32);
        a_up = new Animation(s_up, 250);

        s_down = new SpriteSheet("textures/plwalkdown.png", 32, 32);
        a_down = new Animation(s_down, 250);

        s_left = new SpriteSheet("textures/plwalkleft.png", 32, 32);
        a_left = new Animation(s_left, 250);
        a_right = new Animation(new SpriteSheet(s_left.getFlippedCopy(true, false), 32, 32), 250);

        //------------------------------------------------------

        player = new Player();

        facing = new SpriteSheet("textures/plstand.png", 32, 32);

        grass = new Image("textures/grassTest.png");

        addGrass(8, 8);
        addGrass(8, 9);
        addGrass(16, 8);
        addGrass(18, 8);
        addGrass(14, 6);

        player.playerAnim = new Animation(new SpriteSheet(facing.getSubImage(player.pFacing.ordinal() * 32, 0, 32, 32), 32, 32), 1000);
    }

    void movement(GameContainer container, int delta) {
        boolean walkingSomewhere = false;
        boolean walkBlocked = false;

        float oldXF = player.pX;
        float oldYF = player.pY;

        int oldX = player.playerTile.getX();
        int oldY = player.playerTile.getY();

        Input input = container.getInput();
        if (input.isKeyDown(Input.KEY_UP)) { // Walk Up
            player.playerAnim = a_up;
            player.playerTile.updatePos((int) Math.floor((player.pX + 15) / 16), (int) Math.ceil((player.pY - 20 - delta * 0.1f) / 16));
            //if(getObjectAtPosition(player.playerTile.x, player.playerTile.y) == null && !player.intersects(getObjectAtPosition(player.playerTile.x, player.playerTile.y))) {
                if(isSurroundingOk(oldXF, oldYF, player.playerTile.x, player.playerTile.y + 1))
                    player.pY -= delta * 0.1f;
                else
                    walkBlocked = true;
            //}
            //else
            //    walkBlocked = true;

            player.pFacing = PlayerFacing.UP;

            walkingSomewhere = true;
        }
        else if (input.isKeyDown(Input.KEY_DOWN)) {
            player.playerAnim = a_down;
            player.playerTile.updatePos((int) Math.floor((player.pX + 15) / 16), (int) Math.ceil((player.pY - 20 + delta * 0.1f) / 16));
            //if(getObjectAtPosition(player.playerTile.x, player.playerTile.y) == null && !player.intersects(getObjectAtPosition(player.playerTile.x, player.playerTile.y))) {
                if(isSurroundingOk(oldXF, oldYF, player.playerTile.x, player.playerTile.y + 1))
                    player.pY += delta * 0.1f;
                else
                    walkBlocked = true;
            //}
            //else
            //    walkBlocked = true;

            player.pFacing = PlayerFacing.DOWN;

            walkingSomewhere = true;
        }
        else if (input.isKeyDown(Input.KEY_LEFT)) {
            player.playerAnim = a_left;
            player.playerTile.updatePos((int) Math.ceil((player.pX - delta * 0.1f) / 16), (int) Math.floor((player.pY + 8) / 16));
            //if(getObjectAtPosition(player.playerTile.x, player.playerTile.y) == null && !player.intersects(getObjectAtPosition(player.playerTile.x, player.playerTile.y))) {
                if(isSurroundingOk(oldXF, oldYF, player.playerTile.x - 1, player.playerTile.y))
                    player.pX -= delta * 0.1f;
                else
                    walkBlocked = true;
            //}
            //else
            //    walkBlocked = true;

            player.pFacing = PlayerFacing.LEFT;

            walkingSomewhere = true;
        }
        else if (input.isKeyDown(Input.KEY_RIGHT)) {
            player.playerAnim = a_right;
            player.playerTile.updatePos((int) Math.ceil((player.pX + delta * 0.1f) / 16), (int) Math.floor((player.pY + 8) / 16));
            //if(getObjectAtPosition(player.playerTile.x, player.playerTile.y) == null || !player.intersects(getObjectAtPosition(player.playerTile.x, player.playerTile.y))) {
                if(isSurroundingOk(oldXF, oldYF, player.playerTile.x + 1, player.playerTile.y))
                    player.pX += delta * 0.1f;
                else
                    walkBlocked = true;
            //}
            //else
            //    walkBlocked = true;

            player.pFacing = PlayerFacing.RIGHT;
            walkingSomewhere = true;
        }

        if(!walkingSomewhere) {
            player.playerAnim = new Animation(new SpriteSheet(facing.getSubImage(player.pFacing.ordinal() * 32, 0, 32, 32), 32, 32), 1000);
        }

        if(walkBlocked)
            player.playerTile.updatePos(oldX, oldY);
        setPlayerTileBasedOnPosition();
    }

    void otherActions(GameContainer container, int delta) {
        Input input = container.getInput();
        if(input.isKeyPressed(Keyboard.KEY_G)) {
            Tile facingTile = player.playerFacingTile;
            if(getObjectAtPosition(facingTile.x, facingTile.y) == null)
                addGrass(facingTile.x, facingTile.y);
            else
                removeGrass(facingTile.x, facingTile.y);
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        movement(container, delta);
        otherActions(container, delta);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        farmLandTest.render(0, 0);

        for(Map.Entry<Tile, Entity> pos : grassPos.entrySet()) {
            //g.setColor(new Color(1f, 1f, 1f, 1f));
            Tile tile = pos.getKey();
            grass.draw(tile.x * 16, tile.y * 16);
            //g.setColor(new Color(0f, 0f, 0f, 0.75f));
            //g.draw(pos.getValue().getBoundingBox());
        }
        g.setColor(new Color(1f, 1f, 1f, 1f));

        player.playerAnim.draw(player.pX, player.pY);


        g.setColor(new Color(0f, 0f, 0f, 0.75f));

        //g.draw(player.getBoundingBox());
        //g.drawRect(player.playerTile.x * 16, player.playerTile.y * 16, 16, 16);

        //g.drawRect(player.playerFacingTile.x * 16, player.playerFacingTile.y * 16, 16, 16);
    }

    boolean isSurroundingOk(float oldX, float oldY, int tileX, int tileY) {
        if (player.intersects(getObjectAtPosition(tileX, tileY))) {
            player.updatePos(oldX, oldY);

            return false;
        }
        if (player.intersects(getObjectAtPosition(tileX, tileY - 1))) { // Top
            player.updatePos(player.pX, player.pY + 0.1f);

            return false;
        }
        if (player.intersects(getObjectAtPosition(tileX, tileY + 1))) { // Bottom
            player.updatePos(player.pX, player.pY - 0.1f);

            return false;
        }
        if (player.intersects(getObjectAtPosition(tileX - 1, tileY))) { // Left
            player.updatePos(player.pX + 0.1f, player.pY);

            return false;
        }
        if (player.intersects(getObjectAtPosition(tileX + 1, tileY))) { // Right
            player.updatePos(player.pX - 0.1f, player.pY);

            return false;
        }
        return true;
    }

    void addGrass(int tX, int tY) {
        grassPos.put(new Tile(tX, tY), new Entity("grass", 16, tX, tY));
    }
    void removeGrass(int tX, int tY) {
        grassPos.remove(new Tile(tX, tY));
    }

    void setPlayerTileBasedOnPosition() {
        int x = player.playerTile.getX();
        int y = (int) Math.ceil((player.pY + 10) / 16);
        player.playerTile.updatePos(x, y);
        player.updatePos(player.pX, player.pY);
    }

    Entity getObjectAtPosition(int tX, int tY) {
        for(Map.Entry<Tile, Entity> entry : grassPos.entrySet()) {
            Tile tile = entry.getKey();
            if(tX == tile.x && tY == tile.y)
                return entry.getValue();
        }
        return null;
    }
}