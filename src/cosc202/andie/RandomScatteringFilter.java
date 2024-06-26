package cosc202.andie;

import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * <p>
 * Implements a random scattering effect that replaces each pixel in the output image
 * with a random pixel from within a certain radius of the original location.
 * </p>
 * 
 * <p>
 * The random scattering effect creates a randomized, scattered appearance by 
 * choosing a pixel from a surrounding area within a specified radius and using 
 * it to replace the original pixel. This filter can be useful for artistic effects 
 * or to simulate noise and other visual distortions.
 * </p>
 * 
 * Example usage:
 * <pre>
 * {@code
 * BufferedImage inputImage = ... // input image
 * int radius = 5; // radius for scattering effec
 * RandomScatteringFilter filter = new RandomScatteringFilter(radius);
 * BufferedImage outputImage = filter.apply(inputImage);
 * }
 * </pre>
 * 
 * <b>Note:</b> This class implements the {@link ImageOperation} interface, 
 * allowing it to be used in a sequence of image operations.
 * 
 * @see java.awt.image.BufferedImage
 * @see java.util.Random
 * @see cosc202.andie.ImageOperation
 * @see java.io.Serializable
 * 
 * @version 2.0
 * @since 05/05/2024
 * @author Marcus Millan
 */
public class RandomScatteringFilter implements ImageOperation, java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int radius;

    /**
     * Constructs a RandomScatteringFilter with a specified radius.
     * 
     * @param radius The radius within which to scatter pixels.
     */
    public RandomScatteringFilter(int radius) {
        this.radius = radius;
    }

    /**
     * Applies the random scattering effect to an input BufferedImage.
     * 
     * <p>
     * This method iterates over each pixel in the input image and replaces it with
     * a pixel chosen at random from within a specified radius. The radius determines
     * the maximum distance from the original pixel's position that the replacement
     * pixel can be taken from.
     * </p>
     * 
     * <p>
     * If the calculated random position is outside the image boundaries, it is clamped
     * to the nearest edge to ensure the coordinates remain valid.
     * </p>
     * 
     * @param input The input BufferedImage to which the random scattering effect is applied.
     * @return The resulting BufferedImage after the effect has been applied.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {
        if (input == null) {
            JOptionPane.showMessageDialog(
                    null,
                    Settings.getLanguageProperty("WARN_NON_IMG_FILE"),
                    Settings.getLanguageProperty("WARN_NON_IMG_FILE"),
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage output = new BufferedImage(width, height, input.getType());
        Random rand = new Random();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Calculate random offsets within the radius
                int randomX = x + rand.nextInt(2 * radius + 1) - radius;
                int randomY = y + rand.nextInt(2 * radius + 1) - radius;

                // Ensure the new coordinates are within image boundaries
                randomX = Math.max(0, Math.min(randomX, width - 1));
                randomY = Math.max(0, Math.min(randomY, height - 1));

                // Set the pixel at (x, y) in the output image to the pixel at (randomX, randomY) in the input image
                output.setRGB(x, y, input.getRGB(randomX, randomY));
            }
        }

        return output;
    }
}
