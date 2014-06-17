package com.lordmau5.harvest.shared.util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * @author: Lordmau5
 * @time: 17.06.2014 - 21:57
 */
public class ImageLoader {

    public static Image loadImage(String location) {
        try {
            return new Image(location);
        } catch (SlickException e) {
            e.printStackTrace();
        }
        return null;
    }

}
