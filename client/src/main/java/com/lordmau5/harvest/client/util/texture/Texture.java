package com.lordmau5.harvest.client.util.texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBTextureRectangle;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.PNGDecoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Lordmau5
 * Date: 26.03.14
 * Time: 20:27
 */
public class Texture {
    String textureName;
    boolean animated;
    Map<String, Sprite> spriteMap = new HashMap<>();
    int texture;

    public Texture(String textureName, boolean animated) {
        this.textureName = textureName;
        this.animated = animated;
        loadTexture();
        setupSprites();
    }

    public int getTexture() {
        return texture;
    }

    public Sprite getSprite(String name) {
        return spriteMap.containsKey(name) ? spriteMap.get(name) : null;
    }

    public Sprite nextSprite(Sprite currentSprite) {
        boolean needsFirstSprite = true;
        Sprite firstSprite = null;
        boolean isNextSprite = false;
        for(Sprite sprite : spriteMap.values()) {
            if(needsFirstSprite) {
                firstSprite = sprite;
                needsFirstSprite = false;
            }
            if(isNextSprite) {
                return sprite;
            }
            if(sprite.equals(currentSprite)) {
                isNextSprite = true;
            }
        }
        return firstSprite;
    }

    void loadTexture() {
        texture = GL11.glGenTextures();
        GL11.glBindTexture(ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB, texture);
        InputStream is;
        try {
            is = new FileInputStream("client/src/main/resources/textures/objects/" + textureName + ".png");
            PNGDecoder decoder = new PNGDecoder(is);
            ByteBuffer buffer = BufferUtils.createByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.RGBA);
            buffer.flip();
            is.close();
            GL11.glTexParameteri(ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexImage2D(ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cleanUp(boolean asCrash) {
        GL11.glDeleteTextures(texture);
        Display.destroy();
        System.exit(asCrash ? 1 : 0);
    }

    void setupSprites() {
        try {
            File file = new File("client/src/main/resources/textures/objects/" + textureName + ".xml");
            System.out.println(file.getAbsolutePath());

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("SubTexture");
            for(int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String name = element.getAttribute("name");
                    int x = Integer.parseInt(element.getAttribute("x"));
                    int y = Integer.parseInt(element.getAttribute("y"));
                    int w = Integer.parseInt(element.getAttribute("width"));
                    int h = Integer.parseInt(element.getAttribute("height"));
                    Sprite sprite = new Sprite(name, x, y, w, h);
                    spriteMap.put(sprite.getName(), sprite);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            cleanUp(true);
        }
    }
}