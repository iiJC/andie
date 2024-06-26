package cosc202.andie;

import java.awt.image.*;

/**
 * Provides functionality for cropping images to a specified region.
 * Implements the ImageOperation interface to apply cropping operations on BufferedImage objects.
 * 
 * @author Ned Redmond
 */

public class ImageCrop implements ImageOperation, java.io.Serializable {

    private int x1, y1, width, height;

    // Constructor for ImageCrop class
    ImageCrop(int x1, int y1, int width, int height) {
        this.x1 = x1;
        this.y1 = y1;
        this.width = width;
        this.height = height;

    }

    /**
     * Applies the cropping operation to a BufferedImage.
     * Crops the image to the specified region defined by coordinates and dimensions.
     * 
     * @param input The BufferedImage to be cropped.
     * @return A new BufferedImage object representing the cropped region.
     */
    public BufferedImage apply(BufferedImage input) {
        try {

            if (x1 >= 0 && y1 >= 0 && x1 + width <= input.getWidth() && y1 + height <= input.getHeight()) {
                BufferedImage croppedImage = input.getSubimage(x1, y1, width, height);
                return croppedImage;
            } else {
                // Display a warning if the crop region is outside the image bounds
                System.err.println(Settings.getLanguageProperty("WARN_CROP_OUTSIDE"));
                return input; // Return the original input
            }

        } catch (NullPointerException e) {
            System.err.println(Settings.getLanguageProperty("WARN_NULL_INPUT"));
            return input; // Return the original input if there is a null pointer exception.
        }
    }

}
