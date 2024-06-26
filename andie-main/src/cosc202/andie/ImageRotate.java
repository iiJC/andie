package cosc202.andie;

import java.awt.image.*;

/**
 * Provides functionality to rotate an image by 0, 90, 180, or 270 degrees.
 * The rotation operation alters the dimensions of the output image depending on
 * the angle of rotation.
 * 
 * @author Ned Redmond
 */

public class ImageRotate implements ImageOperation, java.io.Serializable {
    private int degrees;

    /**
     * Constructor to create an ImageRotate operation with a specified angle.
     * 
     * @param degrees The angle in degrees by which the image should be rotated.
     *                Valid inputs are 0, 90, 180, and 270.
     */
    public ImageRotate(int degrees) {
        this.degrees = degrees;
    }

    /**
     * Applies the rotation operation to a BufferedImage.
     * The method calculates the new dimensions of the image based on the rotation
     * angle and rotates the image accordingly. For angles 90 and 270, the image's
     * width and height are swapped. For 180 degrees, the dimensions remain the same
     * but the image is flipped along both axes.
     * 
     * @param input The BufferedImage to be rotated.
     * @return A new BufferedImage object representing the rotated image.
     * @throws NullPointerException If the input image is null.
     */
    public BufferedImage apply(BufferedImage input) {
        try {

            int width = input.getWidth();
            int height = input.getHeight();

            int rotatedWidth, rotatedHeight;
            if (degrees == 180) {
                rotatedWidth = width;
                rotatedHeight = height;
            } else {
                rotatedWidth = height;
                rotatedHeight = width;
            }

            BufferedImage rotatedImage = new BufferedImage(rotatedWidth, rotatedHeight, BufferedImage.TYPE_INT_ARGB);

            if (degrees == 0) {
                return input;
            } else if (degrees == 90) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        rotatedImage.setRGB(height - y - 1, x, input.getRGB(x, y));
                    }
                }
            } else if (degrees == 180) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        rotatedImage.setRGB(width - x - 1, height - y - 1, input.getRGB(x, y));
                    }
                }
            } else if (degrees == 270) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        rotatedImage.setRGB(y, width - x - 1, input.getRGB(x, y));
                    }
                }
            }

            return rotatedImage;
        } catch (NullPointerException e) {
            System.err.println(Settings.getLanguageProperty("WARN_NULL_INPUT"));
            return input;
        }
    }
}
