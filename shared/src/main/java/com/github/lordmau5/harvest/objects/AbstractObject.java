package com.github.lordmau5.harvest.objects;

//import com.github.lordmau5.harvest.client.Client;

import java.awt.image.BufferedImage;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 14:01
 */
public abstract class AbstractObject {

    private BufferedImage image;
    private String textureName;
    private int textureHeight;
    private int textureWidth;

    private int x;
    private int y;

    public AbstractObject() {}

    public AbstractObject(String textureName, boolean animated) {
        //TODO: this should not be on shared
        //FIXME: re-enable this
        /*String fEnding = animated ? ".gif" : ".png";
        this.textureName = "/textures/objects/" + textureName + fEnding;
        try {
            image = Client.loader.loadImage(this.textureName);

            textureWidth = image.getWidth();
            textureHeight = image.getHeight();

            System.out.println(textureWidth + " : " + textureHeight);
        }
        catch (IOException e) {}*/
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[] getPosition() {
        return new int[]{this.x, this.y};
    }

    public boolean canPassThrough() { return false; }
}