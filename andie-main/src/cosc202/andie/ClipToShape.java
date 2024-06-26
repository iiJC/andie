package cosc202.andie;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Provides functionality for cropping images to a specified shape.
 * Implements the ImageOperation interface to apply cropping operations on BufferedImage objects.
 * 
 * @author Ned Redmond
 * @version 1.0
 */

public class ClipToShape implements ImageOperation, java.io.Serializable {

    private Shape shape;

    // Constructor for CropToShape class
    ClipToShape(Shape shape) {
        this.shape = shape;
    }

    /**
     * Applies the cropping operation to a BufferedImage.
     * Crops the image to the specified shape.
     *
     * @param input The BufferedImage to be cropped.
     * @return A new BufferedImage object representing the cropped region.
     */
    public BufferedImage apply(BufferedImage input) {
        try {
            // Create a BufferedImage with ARGB type
            BufferedImage clippedImage = new BufferedImage(input.getWidth(), input.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);

            // Create a Graphics2D object from the cropped image
            Graphics2D g2d = clippedImage.createGraphics();

            // Set the custom shape as the clipping path
            g2d.setClip(shape);

            // Draw the original image onto the clipped image with the clipping path applied
            g2d.drawImage(input, 0, 0, null);

            // Dispose the graphics object
            g2d.dispose();

            // Now clippedImage contains the original image clipped to the specified shape

            return clippedImage;
        } catch (Exception e) {
            System.err.println(Settings.getLanguageProperty("WARN_NULL_INPUT"));
            return input; // Return the original input if there is an exception.
        }
    }
}
