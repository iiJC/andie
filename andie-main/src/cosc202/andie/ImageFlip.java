package cosc202.andie;

import java.awt.image.*;

/**
 * Provides functionality for flipping images either horizontally or vertically.
 * Implements the ImageOperation interface to apply flipping operations on
 * BufferedImage objects.
 * This class supports two types of flips: horizontal and vertical, determined
 * at instantiation.
 * 
 * @author Ned Redmond
 */
public class ImageFlip implements ImageOperation, java.io.Serializable {

    /**
     * Nested class to define flip types as constants for readability and ease of
     * use.
     * Allows specifying the flip operation type using descriptive identifiers.
     */
    public class FlipType {
        public static final int horizontal = 0;
        public static final int vertical = 1;
    }

    // Field to store the type of flip to be applied.
    private int flipType;

    /**
     * Constructor for creating an ImageFlip operation with a specific flip type.
     * 
     * @param flipType The type of flip operation to apply. Use FlipType.horizontal
     *                 or FlipType.vertical.
     */
    public ImageFlip(int flipType) {
        this.flipType = flipType;
    }

    /**
     * Applies the flipping operation to a BufferedImage.
     * The operation can flip the image either horizontally or vertically, depending
     * on the flipType set during instantiation.
     * 
     * @param input The BufferedImage to be flipped.
     * @return A new BufferedImage object that is the result of applying the flip
     *         operation to the input image.
     */
    public BufferedImage apply(BufferedImage input) {
        try {

            int width = input.getWidth();
            int height = input.getHeight();
            BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (flipType == FlipType.horizontal) {
                        flippedImage.setRGB(width - x - 1, y, input.getRGB(x, y));
                    } else {
                        flippedImage.setRGB(x, height - y - 1, input.getRGB(x, y));
                    }
                }
            }
            return flippedImage;
        } catch (NullPointerException e) {
            System.err.println(Settings.getLanguageProperty("WARN_NULL_INPUT"));
            return input; // Return the original input if there is a null pointer exception.
        }
    }

}
