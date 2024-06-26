package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * This class implements the ImageOperation interface and can be used to apply
 * a sharpening filter to a BufferedImage.
 * </p>
 *
 * @author Angus Tucker
 * @version 1.0
 */
public class SharpenFilter implements ImageOperation, java.io.Serializable {

    /**
     * Constructs a SharpenFilter object.
     */
    public SharpenFilter() {
    }

    /**
     * Applies a sharpening filter to the input image.
     * 
     * @param input The input BufferedImage of which the sharpening filter will be
     *              applied.
     * @return The resulting BufferedImage after applying the sharpening filter.
     */
    public BufferedImage apply(BufferedImage input) {

        // Defining the 3x3 Kernel array for the sharpening filter
        float[] array = { 0, (float) -1 / 2, 0,
                (float) -1 / 2, 3, (float) -1 / 2,
                0, (float) -1 / 2, 0 };

        // Create a Kernel object implementing the array
        Kernel kernel = new Kernel(3, 3, array);

        // Creating a ConvolveOp object implementing the kernel
        ConvolveOp convOp = new ConvolveOp(kernel);
        BufferedImage result = new BufferedImage(input.getColorModel(),
                input.copyData(null),
                input.isAlphaPremultiplied(), null);

        // Applying the convolution operation to the input image and storing the result
        // in the new image.
        convOp.filter(input, result);

        return result;
    }
}
