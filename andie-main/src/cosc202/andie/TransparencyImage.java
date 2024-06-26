package cosc202.andie;

import java.awt.*;
import java.awt.image.*;

import javax.swing.JOptionPane;

/**
 * Provides functionality for adjusting the transparency of an image.
 * Implements the ImageOperation interface to apply transparency operations on
 * BufferedImage objects.
 * This class allows setting a transparency level to adjust the alpha value of the image.
 * 
 * @version 2.0
 * @since 18/05/2024
 * @author Marcus Millan
 * 
 */
public class TransparencyImage implements ImageOperation, java.io.Serializable {

    private int transparencyLevel;

    /**
     * Constructor for creating a TransparencyImage operation with a specific transparency level.
     * 
     * @param transparencyLevel The level of transparency to apply (0-100). 
     *                          0 means fully transparent and 100 means fully opaque.
     */
    public TransparencyImage(int transparencyLevel) {
        this.transparencyLevel = Math.min(Math.max(transparencyLevel, 0), 100); // Ensure level is within 0-100
    }

    /**
     * Gets the current transparency level.
     * 
     * @return The current transparency level (0-100).
     */
    public int getTransparencyLevel() {
        return transparencyLevel;
    }
    
    /**
     * Applies the transparency operation to a BufferedImage.
     * The operation adjusts the alpha value of each pixel in the image based on the transparency level.
     * 
     * @param input The BufferedImage to be adjusted.
     * @return A new BufferedImage object that is the result of applying the transparency operation to the input image,
     *         or null if the input image is null.
     */
    public BufferedImage apply(BufferedImage input) {
        if (input == null) {
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_NON_IMG_FILE"),
            Settings.getLanguageProperty("WARN_NON_IMG_FILE"),
            JOptionPane.ERROR_MESSAGE);
            return null; // Return null if the input image is null
        }

        try {
            int width = input.getWidth();
            int height = input.getHeight();
            BufferedImage transparentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            // Calculate the alpha value from the transparency level (0-100)
            int alpha = (int) (transparencyLevel * 2.55); // transparencyLevel 0-100 maps to alpha 0-255

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgba = input.getRGB(x, y);
                    Color color = new Color(rgba, true);
                    // If the pixel is not fully transparent, adjust its alpha value
                    if (color.getAlpha() != 0) {
                        Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
                        transparentImage.setRGB(x, y, newColor.getRGB());
                    } else {
                        // Preserve fully transparent pixels (background)
                        transparentImage.setRGB(x, y, rgba);
                    }
                }
            }
            return transparentImage;
        } catch (Exception e) {
            System.err.println(Settings.getLanguageProperty("WARN_EXCEPTION"));
            return input; // Return the original input in case of an exception
        }
    }
}
