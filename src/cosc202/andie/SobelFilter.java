package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply Sobel filter.
 * </p>
 * 
 * <p>
 * Implements a Sobel filter, horizontal or vertical with the choice of an offset 
 * </p>
 * 
 * @author Hadrien Tang
 */

public class SobelFilter implements ImageOperation, java.io.Serializable {
    /**
    * The kernel for horizontal Sobel filtering.
    */
    private float[] h = { -0.5f, 0, 0.5f, 1, 0, -1, -0.5f, 0, 0.5f };
    /**
    * The kernel for vertical Sobel filtering.
    */
    private float[] v = { -0.5f, -1, 0.5f, 0, 0, 0, 0.5f, 1, 0.5f };
    /**
     * The direction of the Sobel filter.
     */
    private String direction;
    /**
    * Whether to apply an offset to the filter.
    */
    private boolean offset;

    /**
    * Constructs a SobelFilter with the specified direction and offset.
    * 
    * @param direction The direction of the Sobel filter ("Horizontal" or "Vertical").
    * @param offset    Whether to apply an offset to the filter.
    */
    //the filter takes a direction input(horizontal or vertical) and an offset input of true or false
    SobelFilter(String direction, boolean offset) {
        this.direction = direction;
        this.offset = offset;
    }

    /**
    * Applies the Sobel filter to the input image.
    * 
    * @param input The input BufferedImage to which the filter is applied.
    * @return The output BufferedImage after applying the Sobel filter.
    */
    public BufferedImage apply(BufferedImage input) {
        try {
            float[] dir = new float[9];
            if (direction == "Horizontal") {
                dir = h;
            }
            if (direction == "Vertical")
                dir = v;

            Kernel kernel = new Kernel(3, 3, dir);
            Convo2 convoOp = new Convo2(kernel, offset);
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