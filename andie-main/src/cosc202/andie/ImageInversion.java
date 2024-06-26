package cosc202.andie;

import java.awt.image.*;

/**
 * Implements an operation to invert the colors of an image.
 * Inversion involves transforming each pixel's color values to their
 * complementary values.
 * 
 * <p>
 * The images produced by this operation are still technically color images,
 * in that they have red, green, and blue values, but each pixel has inverted
 * values for red, green, and blue, resulting in an inverted color effect.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Marcus Millan
 * @version 1.0
 */
public class ImageInversion implements ImageOperation, java.io.Serializable {

    ImageInversion() {

    }

    /**
     * Applies the inversion operation to a given BufferedImage.
     * This method iterates through each pixel of the input image,
     * calculates the complementary color values, and updates the
     * image with the inverted colors.
     * 
     * @param input The BufferedImage to which the inversion will be applied.
     * @return A new BufferedImage with inverted colors.
     */
    public BufferedImage apply(BufferedImage input) {

        for (int y = 0; y < input.getHeight(); ++y) {
            for (int x = 0; x < input.getWidth(); ++x) {
                int argb = input.getRGB(x, y);
                int a = (argb & 0xFF000000) >> 24;
                int r = (argb & 0x00FF0000) >> 16;
                int g = (argb & 0x0000FF00) >> 8;
                int b = (argb & 0x000000FF);

                r = 255 - r;
                g = 255 - g;
                b = 255 - b;

                // int grey = (int) Math.round(0.3*r + 0.6*g + 0.1*b);

                argb = (a << 24) | (r << 16) | (g << 8) | b;
                input.setRGB(x, y, argb);
            }
        }

        return input;
    }

}
