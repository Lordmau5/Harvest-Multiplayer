package com.lordmau5.harvest.client;

import com.lordmau5.harvest.client.util.texture.Sprite;
import com.lordmau5.harvest.client.util.texture.Texture;
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

import java.awt.*;

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

    void loadImages() {
        objectTexture = new Texture("objectSheet", false);
        //texture = TextureLoader.getTexture("PNG", org.newdawn.slick.util.ResourceLoader.getResourceAsStream("textures/objects/stone.png"));
        testFont = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, 24), false);
    }


    void test() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        initGL(); // init OpenGL
        loadImages();
        currentSprite = objectTexture.getSprite("grass");

        getDelta(); // call once before loop to initialise lastFrame
        lastFPS = getTime(); // call before loop to initialise fps timer

        while (!Display.isCloseRequested()) {
            int delta = getDelta();

            update(delta);
            renderGL();

            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }

    float scale = 1f;
    boolean iterated = false;

    public void update(int delta) {
        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            if(shift && !iterated) {
                currentSprite = objectTexture.nextSprite(currentSprite);
                System.out.println(currentSprite.getName());
                iterated = true;
            }
            else {
                x -= 0.35f * delta;
            }
        }
        else {
            iterated = false;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            if(shift) {
                currentSprite = objectTexture.getSprite("stone");
            }
            else {
                x += 0.35f * delta;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            scale += 0.05f;
            if(scale > 4f)
                scale = 4f;
            y -= 0.35f * delta;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            scale -= 0.05f;
            if(scale < 1f)
                scale = 1f;
            y += 0.35f * delta;
        }

        if (x < 16) x = 16;
        if (x > 800) x = 800;
        if (y < 16) y = 16;
        if (y > 600) y = 600;

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
        //GL11.glViewport(0, 0, 800, 600);

        //GL11.glEnable(GL11.GL_CULL_FACE);
        //GL11.glCullFace(GL11.GL_BACK);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, 1, -1);
        //GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    public void renderGL() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);

        //GL11.glColor3f(0.5f, 0.5f, 1.0f);

        GL11.glBindTexture(ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB, objectTexture.getTexture());
        int sX = currentSprite.getX();
        int sY = currentSprite.getY();
        int sX2 = currentSprite.getX() + currentSprite.getWidth();
        int sY2 = currentSprite.getY() + currentSprite.getHeight();

        GL11.glRotatef(180, 0, 0, 0);
        GL11.glScalef(scale, scale, 0);

        //GL11.glPushMatrix();
        //GL11.glRotatef(rotation, 0f, 0f, 1f);
        //GL11.glTranslatef(-x - currentSprite.getWidth(), -y - currentSprite.getHeight(), 0);

        GL11.glBegin(GL11.GL_QUADS);
            /*GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(x - currentSprite.getWidth(), y - currentSprite.getHeight());
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(x + currentSprite.getWidth(), y - currentSprite.getHeight());
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(x + currentSprite.getWidth(), y + currentSprite.getHeight());
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(x - currentSprite.getWidth(), y + currentSprite.getHeight());*/

            GL11.glTexCoord2f(sX, sY2);
            GL11.glVertex2f(sX, sY);
            GL11.glTexCoord2f(sX2, sY2);
            GL11.glVertex2f(sX2, sY);
            GL11.glTexCoord2f(sX2, sY);
            GL11.glVertex2f(sX2, sY2);
            GL11.glTexCoord2f(sX, sY);
            GL11.glVertex2f(sX, sY2);
        GL11.glEnd();
        //GL11.glTranslatef(x + currentSprite.getWidth(), y + currentSprite.getHeight(), 0);
        GL11.glBindTexture(ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB, 0);
        GL11.glPopMatrix();
        //GL11.glPopMatrix();
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