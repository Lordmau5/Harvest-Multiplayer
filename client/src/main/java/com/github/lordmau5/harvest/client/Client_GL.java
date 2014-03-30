package com.github.lordmau5.harvest.client;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.IOException;

/**
 * Author: Lordmau5
 * Date: 29.03.14
 * Time: 13:53
 */
public class Client_GL {

    float x = 400, y = 300;
    float rotation = 0;
    long lastFrame;
    int fps;
    long lastFPS;

    Texture texture;

    void loadImages() {
        try {
            texture = TextureLoader.getTexture("PNG", org.newdawn.slick.util.ResourceLoader.getResourceAsStream("textures/objects/stone.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    float force = 1f;

    public void update(int delta) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                rotation += 0.15f * delta * force;
                force += 0.025f;
                if(force > 15f)
                    force = 15f;
            }
            else {
                x -= 0.35f * delta;
                force = 1f;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                rotation -= 0.15f * delta * force;
                force += 0.025f;
                if(force > 15f)
                    force = 15f;
            }
            else {
                x += 0.35f * delta;
                force = 1f;
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) y -= 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) y += 0.35f * delta;

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
            Display.setTitle("FPS: " + fps + " | Force: " + force);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    public void initGL() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glViewport(0,0,800,600);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    public void renderGL() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        //GL11.glColor3f(0.5f, 0.5f, 1.0f);

        texture.bind();
        for(int tX = 0; tX < 2; tX++)
            for(int tY = 0; tY < 2; tY++) {

                GL11.glPushMatrix();
                GL11.glTranslatef(x + (tX * texture.getImageWidth()), y + (tY * texture.getImageHeight()), 0);
                //GL11.glRotatef(rotation, 0f, 0f, 1f);
                GL11.glTranslatef(-x - (tX * texture.getImageWidth()), -y - (tY * texture.getImageHeight()), 0);

                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(0, 0);
                GL11.glVertex2f(x - texture.getImageWidth(), y - texture.getImageHeight());
                GL11.glTexCoord2f(1, 0);
                GL11.glVertex2f(x + texture.getImageWidth(), y - texture.getImageHeight());
                GL11.glTexCoord2f(1, 1);
                GL11.glVertex2f(x + texture.getImageWidth(), y + texture.getImageHeight());
                GL11.glTexCoord2f(0, 1);
                GL11.glVertex2f(x - texture.getImageWidth(), y + texture.getImageHeight());
                GL11.glEnd();
                GL11.glPopMatrix();
            }
    }

    public static void main(String args[]) {
        Client_GL gl = new Client_GL();
        gl.test();
    }

}