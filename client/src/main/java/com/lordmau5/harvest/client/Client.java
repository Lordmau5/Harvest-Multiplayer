package com.lordmau5.harvest.client;

import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.List;

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

    float pX = 90, pY = 90;
    Tile playerTile = new Tile((int) Math.ceil(pX / 16), (int) Math.ceil(pY / 16));
    enum pFacing_E {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    pFacing_E pFacing = pFacing_E.DOWN;

    Animation playerAnim;
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
    List<int[]> grassPos = new ArrayList<>();

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

        facing = new SpriteSheet("textures/plstand.png", 32, 32);

        grass = new Image("textures/grassTest.png");

        addGrass(8, 8);

        playerAnim = new Animation(new SpriteSheet(facing.getSubImage(pFacing.ordinal() * 32, 0, 32, 32), 32, 32), 1000);
    }

    void movement(GameContainer container, int delta) {
        boolean walkingSomewhere = false;
        boolean walkBlocked = false;

        int oldX = playerTile.getX();
        int oldY = playerTile.getY();

        Input input = container.getInput();
        if (input.isKeyDown(Input.KEY_UP)) { // Walk Up
            playerAnim = a_up;
            playerTile.updatePos(oldX, (int) Math.ceil((pY - delta * 0.1f) / 16));
            if(!isBlocked()) {
                pY -= delta * 0.1f;
            }
            else
                walkBlocked = true;

            pFacing = pFacing_E.UP;

            walkingSomewhere = true;
        }
        else if (input.isKeyDown(Input.KEY_DOWN)) {
            playerAnim = a_down;
            playerTile.updatePos(oldX, (int) Math.ceil((pY + delta * 0.1f) / 16));
            if(!isBlocked()) {
                pY += delta * 0.1f;
            }
            else
                walkBlocked = true;

            pFacing = pFacing_E.DOWN;

            walkingSomewhere = true;
        }
        else if (input.isKeyDown(Input.KEY_LEFT)) {
            playerAnim = a_left;
            playerTile.updatePos((int) Math.ceil((pX - delta * 0.1f) / 16), oldY);
            if(!isBlocked()) {
                pX -= delta * 0.1f;
            }
            else
                walkBlocked = true;

            pFacing = pFacing_E.LEFT;

            walkingSomewhere = true;
        }
        else if (input.isKeyDown(Input.KEY_RIGHT)) {
            playerAnim = a_right;
            playerTile.updatePos((int) Math.ceil((pX + delta * 0.1f) / 16), oldY);
            if(!isBlocked()) {
                pX += delta * 0.1f;
            }
            else
                walkBlocked = true;

            pFacing = pFacing_E.RIGHT;
            walkingSomewhere = true;
        }

        if(!walkingSomewhere) {
            playerAnim = new Animation(new SpriteSheet(facing.getSubImage(pFacing.ordinal() * 32, 0, 32, 32), 32, 32), 1000);
        }

        if(walkBlocked)
            playerTile.updatePos(oldX, oldY);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        movement(container, delta);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        farmLandTest.render(0, 0);

        for(int[] pos : grassPos) {
            grass.draw(pos[0] * 16, pos[1] * 16);
        }

        playerAnim.draw(pX, pY);
    }

    void addGrass(int tX, int tY) {
        blocked[tX][tY] = true;
        grassPos.add(new int[] {8, 8});
    }

    float[] recalcPos(float x, float y) {
        if(pFacing == pFacing_E.UP)
            y += 6;
        else if(pFacing == pFacing_E.DOWN)
            y -= 6;
        else if(pFacing == pFacing_E.LEFT)
            x += 6;
        else if(pFacing == pFacing_E.RIGHT)
            x -= 6;
        return new float[] {x, y};
    }

    boolean isBlocked() {
        int x = playerTile.getX();
        int y = playerTile.getY();
        if(x < 0 || y < 0)
            return false;

        System.out.println(x + " - " + y);
        return blocked[x][y];
    }
}