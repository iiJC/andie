package cosc202.andie;

import java.awt.Point;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import cosc202.andie.ImageOperation;

/**
 * <p>
 * ImageOperation used to adjust an image's Brightness and Contrast.
 * </p>
 * 
 * <p>
 * This class implements the ImageOperation interface and provides methods to
 * apply brightness and contrast adjustments to a BufferedImage.
 * </p>
 * 
 * <p>
 * Instances of this class are serializable.
 * </p>
 * 
 * @author Angus Tucker
 * @version 1.0
 */

public class BrightnessAndContrast implements ImageOperation, java.io.Serializable {

    private double brightness;
    private double contrast;

    /**
     * <p>
     * Creates a new BrightnessAndContrast operation based on specific brightness
     * and contrast parameters.
     * </p>
      * @param brightness the brightness adjustment value (-100 to 100)
     * @param contrast   the contrast adjustment value (-100 to 100)
     */
    public BrightnessAndContrast(int brightness, int contrast) {
        this.brightness = brightness;
        this.contrast = contrast;
    }

    /**
     * Applies the brightness and contrast adjustments to the input BufferedImage
     * and returns the resulting image.
     * 
     * @param input the BufferedImage to be adjusted
     * @return the adjusted BufferedImage
     */
    public BufferedImage apply(BufferedImage input) {
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null),
                input.isAlphaPremultiplied(), null);
        for (int y = 0; y < input.getHeight(); ++y) {
            for (int x = 0; x < input.getWidth(); ++x) {
                output.setRGB(x, y, getARGB(x, y, input));
            }
        }
        return output;
    }

    // Calculates the ARGB value for the pixel at position (x, y) in the input image
    private int getARGB(int x, int y, BufferedImage input) {
        int argb = input.getRGB(x, y);

        int a = (argb & 0xFF000000) >> 24;
        int r = (argb & 0x00FF0000) >> 16;
        int g = (argb & 0x0000FF00) >> 8;
        int b = (argb & 0x000000FF);

        int red = calculateColour(r);
        int green = calculateColour(g);
        int blue = calculateColour(b);

        red = cast(red);
        blue = cast(blue);
        green = cast(green);

        // Assign the new values onto the pixel
        argb = (a << 24) | (red << 16) | (green << 8) | blue;
        return argb;
    }

    // calculates the colour based on previous colour value and the new contrast and
    // brightness
    private int calculateColour(int x) {
        return (int) Math.round(((1 + (contrast / 100)) * (x - 127.5)) + (127.5 * (1 + (brightness / 100))));
    }

    // used to cast the colour value to rgb range
    private int cast(int x) {
        if (x > 255)
            return 255;
        if (x < 0)
            return 0;
        return x;
    }

}