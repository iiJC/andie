package cosc202.andie;

import java.awt.image.*;


/**
 * <p>
 * This class implements the ImageOperation interface and can be used to apply
 * a gaussian filter to a BufferedImage.
 * </p>
 *
 * @author Hadrien Tang
 */


public class GaussianBlurFilter implements ImageOperation, java.io.Serializable {

    /**
     * The size of filter to apply. 
     */
    private int radius;

    GaussianBlurFilter(int radius) {
        this.radius = radius;
    }

    /**
     * Constructs a GaussianBlurFilter object with the specified radius.
     *
     * @param radius The radius of the Gaussian blur filter.
     */
    GaussianBlurFilter() {
        this(1);
    }

    /**
     * Applies the Gaussian blur filter to the input BufferedImage.
     *
     * @param input The input BufferedImage to apply the blur filter to.
     * @return The resulting BufferedImage after applying the blur filter.
     */
    public BufferedImage apply(BufferedImage input) {
        try {

            // System.out.println(" this gaussian ran"); [Debug Statement]
            float sigma = radius / 3.0f;

            int size = (2 * radius + 1) * (2 * radius + 1);
            float[] array = new float[size];
            float sum = 0;
            int index = 0;

            // make the kernel values
            for (int y = -radius; y <= radius; y++) {
                for (int x = -radius; x <= radius; x++) {
                    float value = (float) (Math.exp(-(x * x + y * y) / (2 * sigma * sigma))
                            / (2 * Math.PI * sigma * sigma));
                    array[index] = value;
                    sum += value;
                    index++;
                }
            }

            // normalize the array
            for (int i = 0; i < size; i++) {
                array[i] /= sum;
                // test to see that the gaussian kernel is the same as the one provided
                //System.out.println("array element  " + array[i]);
            }

            Kernel kernel = new Kernel(2 * radius + 1, 2 * radius + 1, array);
            Convo2 convoOp = new Convo2(kernel);
            //ConvolveOp convOp = new ConvolveOp(kernel);
            BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null),
                    input.isAlphaPremultiplied(), null);
            convoOp.filter(input, output);

            return output;
        } catch (NullPointerException e) {
            System.err.println(Settings.getLanguageProperty("WARN_NULL_INPUT"));
            return input;
        }
    }
}
