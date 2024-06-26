package cosc202.andie;

import java.awt.image.*;
import java.awt.*;

/**
 * <p>
 * This class implements the ImageOperation interface and can be used to apply
 * a saturation filter to a BufferedImage.
 * </p>
 *
 * @author Angus Tucker
 * @version 1.0
 */
public class SaturationFilter implements ImageOperation, java.io.Serializable {

    private float saturationLevel;

    /**
     * Constructs a SaturationFilter object with a default saturation level of 1.0
     * (no change).
     */
    public SaturationFilter() {
        this(1.0f);
    }

    /**
     * Constructs a SaturationFilter object with a specified saturation level.
     * 
     * @param saturationLevel The level of saturation to be applied (0.0 to 2.0).
     */
    public SaturationFilter(float saturationLevel) {
        this.saturationLevel = saturationLevel;
    }

    /**
     * Applies a saturation filter to the input image.
     * 
     * @param input The input BufferedImage to which the saturation filter will be
     *              applied.
     * @return The resulting BufferedImage after applying the saturation filter.
     */
    public BufferedImage apply(BufferedImage input) {
        BufferedImage result = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

        // Convert to HSB
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int rgb = input.getRGB(x, y);
                float[] hsb = Color.RGBtoHSB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, null);
                hsb[1] = Math.min(1.0f, Math.max(0.0f, hsb[1] * saturationLevel)); // Adjusts the saturation
                int newRgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                result.setRGB(x, y, newRgb);
            }
        }
        return result;
    }
}
