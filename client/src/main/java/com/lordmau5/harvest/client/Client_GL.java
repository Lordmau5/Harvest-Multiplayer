package com.lordmau5.harvest.client;

import com.lordmau5.harvest.client.entities.ClientPlayer;
import com.lordmau5.harvest.client.util.texture.Sprite;
import com.lordmau5.harvest.client.util.texture.Texture;
import com.lordmau5.harvest.environment.Point;
import com.lordmau5.harvest.environment.World;
import com.lordmau5.harvest.objects.AbstractObject;
import com.lordmau5.harvest.objects.ObjectRegister;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ARBTextureRectangle;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

import java.awt.Font;

/**
 * Author: Lordmau5
 * Date: 29.03.14
 * Time: 13:53
 */
public class Client_GL {

    float x = 400, y = 300;
    long lastFrame;
    int fps;
    long lastFPS;

    //Texture texture;
    TrueTypeFont testFont;

    static Texture objectTexture;
    static Sprite currentSprite;

    World world;
    ClientPlayer player;

    void loadImages() {
        objectTexture = new Texture("objectSheet", false);
        //texture = TextureLoader.getTexture("PNG", org.newdawn.slick.util.ResourceLoader.getResourceAsStream("textures/objects/stone.png"));
        testFont = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, 24), false);
    }


    int frameCount = 0;
    void test() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        initGL(); // init OpenGL
        loadImages();
        world = new World();
        player = new ClientPlayer();

        System.out.println(objectTexture.getTexture() + " - " + player.texture.getTexture());

        getDelta(); // call once before loop to initialise lastFrame
        lastFPS = getTime(); // call before loop to initialise fps timer

        while (!Display.isCloseRequested()) {
            int delta = getDelta();

            update(delta);
            renderGL();

            if(frameCount++ == 8) {
                frameCount = 0;
                player.render();
            }

            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }

    public void update(int delta) {
        Point pPos = player.getPosition();
        boolean isWalkingSomewhere = false;
        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)) { // Walk Up
            player.faceTowards(ClientPlayer.Facing.UP);
            player.updateWalking(true);

            float modifier = 0.1f;
            if(shift) {
                modifier = 0.25f;
                player.updateRunning(true);
            }
            else {
                player.updateRunning(false);
            }

            if(!world.isObjectAtPosition(pPos.getX() + modifier * delta, pPos.getY()))
                pPos.setXY(pPos.getX(), pPos.getY() - modifier * delta);
            isWalkingSomewhere = true;
        }
        else {
            player.updateWalking(false);
            player.updateRunning(false);
        }
        //-------------------------------------------------
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !isWalkingSomewhere) { // Walk Down
            player.faceTowards(ClientPlayer.Facing.DOWN);
            player.updateWalking(true);

            float modifier = 0.1f;
            if(shift) {
                modifier = 0.25f;
                player.updateRunning(true);
            }
            else {
                player.updateRunning(false);
            }

            if(!world.isObjectAtPosition(pPos.getX() + modifier * delta, pPos.getY()))
                pPos.setXY(pPos.getX(), pPos.getY() + modifier * delta);
            isWalkingSomewhere = true;
        }
        else if(!isWalkingSomewhere) {
            player.updateWalking(false);
            player.updateRunning(false);
        }
        //-------------------------------------------------
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) && !isWalkingSomewhere) { // Walk Left
            player.faceTowards(ClientPlayer.Facing.LEFT);
            player.updateWalking(true);

            float modifier = 0.1f;
            if(shift) {
                modifier = 0.25f;
                player.updateRunning(true);
            }
            else {
                player.updateRunning(false);
            }

            if(!world.isObjectAtPosition(pPos.getX() + modifier * delta, pPos.getY()))
                pPos.setXY(pPos.getX() - modifier * delta, pPos.getY());
            isWalkingSomewhere = true;
        }
        else if(!isWalkingSomewhere) {
            player.updateWalking(false);
            player.updateRunning(false);
        }
        //-------------------------------------------------
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && !isWalkingSomewhere) { // Walk Right
            player.faceTowards(ClientPlayer.Facing.RIGHT);
            player.updateWalking(true);

            float modifier = 0.1f;
            if(shift) {
                modifier = 0.25f;
                player.updateRunning(true);
            }
            else {
                player.updateRunning(false);
            }

            if(!world.isObjectAtPosition(pPos.getX() + modifier * delta, pPos.getY()))
                pPos.setXY(pPos.getX() + modifier * delta, pPos.getY());
        }
        else if(!isWalkingSomewhere) {
            player.updateWalking(false);
            player.updateRunning(false);
        }
        //-------------------------------------------------

        if (pPos.getX() < 0) pPos.setXY(0, pPos.getY());
        if (pPos.getX() > 800 - player.getProperSprite().getWidth()) pPos.setXY(800 - player.getProperSprite().getWidth(), pPos.getY());
        if (pPos.getY() < 0) pPos.setXY(pPos.getX(), 0);
        if (pPos.getY() > 600 - player.getProperSprite().getHeight()) pPos.setXY(pPos.getX(), 600 - player.getProperSprite().getHeight());

        updateFPS(); // update FPS Counter
    }

    public int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps + " | Scale: " + scale);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    public void initGL() {
        GL11.glEnable(ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB);
        GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //GL11.glViewport(0, 0, 800, 600);

        //GL11.glEnable(GL11.GL_CULL_FACE);
        //GL11.glCullFace(GL11.GL_BACK);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, 1, -1);
        //GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    float scale = 1f;
    public void renderGL() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GL11.glPushMatrix();

        GL11.glBindTexture(ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB, objectTexture.getTexture());
        for(AbstractObject object : world.getObjects()) {
            Point point = object.getPosition();
            Sprite sprite = objectTexture.getSprite(object.getTextureName());
            int sX = sprite.getX();
            int sY = sprite.getY();
            int sX2 = sprite.getX() + sprite.getWidth();
            int sY2 = sprite.getY() + sprite.getHeight();

            GL11.glTranslatef(point.getX() + sX2 * scale, point.getY() + sY2 * scale, 0);
            GL11.glRotatef(180, 0, 0, 0);
            GL11.glScalef(scale, scale, 0);

            GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(sX, sY2);
                GL11.glVertex2f(sX, sY);
                GL11.glTexCoord2f(sX2, sY2);
                GL11.glVertex2f(sX2, sY);
                GL11.glTexCoord2f(sX2, sY);
                GL11.glVertex2f(sX2, sY2);
                GL11.glTexCoord2f(sX, sY);
                GL11.glVertex2f(sX, sY2);
            GL11.glEnd();
        }
        GL11.glBindTexture(ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB, 0);

        GL11.glPopMatrix();



        GL11.glPushMatrix();

        GL11.glBindTexture(ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB, player.texture.getTexture());
        Point point = player.getPosition();
        Sprite sprite = player.getProperSprite();
        int sX = sprite.getX();
        int sY = sprite.getY();
        int sX2 = sprite.getX() + sprite.getWidth();
        int sY2 = sprite.getY() + sprite.getHeight();

        GL11.glTranslatef(point.getX() + sX2 * scale, point.getY() + sY2 * scale, 0);
        GL11.glRotatef(180, 0, 0, 0);
        GL11.glScalef(scale, scale, 0);

        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(sX, sY2);
            GL11.glVertex2f(sX, sY);
            GL11.glTexCoord2f(sX2, sY2);
            GL11.glVertex2f(sX2, sY);
            GL11.glTexCoord2f(sX2, sY);
            GL11.glVertex2f(sX2, sY2);
            GL11.glTexCoord2f(sX, sY);
            GL11.glVertex2f(sX, sY2);
        GL11.glEnd();

        GL11.glBindTexture(ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB, 0);

        GL11.glPopMatrix();
    }

    static void setupTextures() {
        System.out.println("Objects, please?");
        for(AbstractObject object : ObjectRegister.getObjectList()) {
            System.out.println(object.getTextureName());
        }
    }

    public static void main(String args[]) {
        Client_GL gl = new Client_GL();

        ObjectRegister.init();
        setupTextures();

        gl.test();
    }

}