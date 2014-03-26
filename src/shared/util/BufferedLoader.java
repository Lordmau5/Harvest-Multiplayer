package shared.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 16:52
 */
public class BufferedLoader {

    public BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(getClass().getResource(path));
    }

}