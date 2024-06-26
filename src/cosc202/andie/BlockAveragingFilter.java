package cosc202.andie;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

/**
 * <p>
 * Implements a block averaging filter that averages out the colors within
 * a rectangular or square block of pixels to reduce noise and detail, including alpha channel handling.
 * </p>
 * 
 * <p>
 * The block averaging filter processes an image by dividing it into smaller blocks and 
 * replacing each block with the average color of the pixels within that block. This operation 
 * can be used to reduce image noise and detail, resulting in a smoother appearance.
 * </p>
 * 
 * Example usage:
 * <pre>
 * {@code
 * BufferedImage inputImage = input image
 * int blockWidth = 5; // width of the averaging block
 * int blockHeight = 5; // height of the averaging block
 * BlockAveragingFilter filter = new BlockAveragingFilter(blockWidth, blockHeight);
 * BufferedImage outputImage = filter.apply(inputImage);
 * }
 * </pre>
 * 
 * <b>Note:</b> This class implements the {@link ImageOperation} interface, 
 * allowing it to be used in a sequence of image operations.
 * 
 * @see java.awt.image.BufferedImage
 * @see java.awt.Graphics
 * @see cosc202.andie.ImageOperation
 * @see java.io.Serializable
 * 
 * @version 2.0
 * @since 08/05/2024
 * @author Marcus Millan
 */
public class BlockAveragingFilter implements ImageOperation, java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int blockWidth;
    private int blockHeight;

    /**
     * Constructs a BlockAveragingFilter with specified block dimensions.
     * 
     * @param blockWidth  The width of the block for averaging.
     * @param blockHeight The height of the block for averaging.
     */
    public BlockAveragingFilter(int blockWidth, int blockHeight) {
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
    }

    /**
     * Applies the block averaging filter to an input BufferedImage.
     * 
     * <p>
     * This method processes the input image by dividing it into blocks of specified dimensions
     * and replacing each block with the average color of the pixels within that block. The resulting
     * image is returned as a new BufferedImage.
     * </p>
     * 
     * @param input The input BufferedImage to which the block averaging filter is applied.
     * @return The resulting BufferedImage after the filter has been applied.
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
        // Ensure the input image is of a type that supports direct color manipulation including alpha
        if (input.getType() != BufferedImage.TYPE_INT_ARGB) {
            BufferedImage convertedImage = new BufferedImage(input.getWidth(), input.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = convertedImage.getGraphics();
            g.drawImage(input, 0, 0, null);
            g.dispose();
            input = convertedImage;
        }

        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage output = new BufferedImage(width, height, input.getType());

        for (int y = 0; y < height; y += blockHeight) {
            for (int x = 0; x < width; x += blockWidth) {
                int[] rgb = averageBlock(input, x, y, blockWidth, blockHeight);
                fillBlock(output, x, y, blockWidth, blockHeight, rgb);
            }
        }

        return output;
    }

    /**
     * Calculates the average color of a rectangular block starting at (startX, startY), including the alpha channel.
     * 
     * @param image The input BufferedImage.
     * @param startX The starting X coordinate of the block.
     * @param startY The starting Y coordinate of the block.
     * @param blockWidth The width of the block.
     * @param blockHeight The height of the block.
     * @return An array containing the average ARGB values [alpha, red, green, blue] of the block.
     */
    private int[] averageBlock(BufferedImage image, int startX, int startY, int blockWidth, int blockHeight) {
        long sumA = 0, sumR = 0, sumG = 0, sumB = 0;
        int count = 0;

        int endX = Math.min(startX + blockWidth, image.getWidth());
        int endY = Math.min(startY + blockHeight, image.getHeight());

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                int argb = image.getRGB(x, y);
                sumA += (argb >> 24) & 0xFF;
                sumR += (argb >> 16) & 0xFF;
                sumG += (argb >> 8) & 0xFF;
                sumB += argb & 0xFF;
                count++;
            }
        }

        if (count == 0) {
            return new int[] { 0, 0, 0, 0 }; // Return transparent color if no pixels are averaged
        }
        return new int[] { (int) (sumA / count), (int) (sumR / count), (int) (sumG / count), (int) (sumB / count) };
    }

    /**
     * Fills a rectangular block with the averaged color, including alpha.
     * 
     * @param image The output BufferedImage to be filled.
     * @param startX The starting X coordinate of the block.
     * @param startY The starting Y coordinate of the block.
     * @param blockWidth The width of the block.
     * @param blockHeight The height of the block.
     * @param argb An array containing the average ARGB values [alpha, red, green, blue] to fill the block.
     */
    private void fillBlock(BufferedImage image, int startX, int startY, int blockWidth, int blockHeight, int[] argb) {
        int avgColor = (argb[0] << 24) | (argb[1] << 16) | (argb[2] << 8) | argb[3];
        for (int y = 0; y < blockHeight && startY + y < image.getHeight(); y++) {
            for (int x = 0; x < blockWidth && startX + x < image.getWidth(); x++) {
                image.setRGB(startX + x, startY + y, avgColor);
            }
        }
    }
}
