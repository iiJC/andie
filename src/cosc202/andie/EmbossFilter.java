package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply an Emboss filter.
 * </p>
 * 
 * <p>
 * Implements an Emboss filter, with 8 direction choices and with the choice of an offset 
 * </p>
 * 
 * @author Hadrien Tang
 */

public class EmbossFilter implements ImageOperation, java.io.Serializable {
    //The direction of the filter
    private String direction;
    private boolean offset;

    //the kernels for the different directions
    private float[] north = { 0, 1, 0, 0, 0, 0, 0, -1, 0 };
    private float[] north_east = { 0, 0, 1, 0, 0, 0, -1, 0, 0 };
    private float[] east = { 0, 0, 0, -1, 0, 1, 0, 0, 0 };
    private float[] south_east = { -1, 0, 0, 0, 0, 0, 0, 0, 1 };
    private float[] south = { 0, -1, 0, 0, 0, 0, 0, 1, 0 };
    private float[] south_west = { 0, 0, -1, 0, 0, 0, 1, 0, 0 };
    private float[] west = { 0, 0, 0, 1, 0, -1, 0, 0, 0 };
    private float[] north_west = { 1, 0, 0, 0, 0, 0, 0, 0, -1 };

    EmbossFilter(String direction, boolean offset) {
        this.direction = direction;
        this.offset = offset;
    }

    public BufferedImage apply(BufferedImage input) {
        try {
            float[] dir = new float[9];
            //based on the direction input, set the kernel to the equivalent direction
            switch (direction) {
                case "NORTH":
                    dir = north;
                    break;
                case "NORTH EAST":
                    dir = north_east;
                    break;
                case "EAST":
                    dir = east;
                    break;
                case "SOUTH EAST":
                    dir = south_east;
                    break;
                case "SOUTH":
                    dir = south;
                    break;
                case "SOUTH WEST":
                    dir = south_west;
                    break;
                case "WEST":
                    dir = west;
                    break;
                case "NORTH WEST":
                    dir = north_west;
                    break;
            }

            Kernel kernel = new Kernel(3, 3, dir);
            Convo2 convoOp = new Convo2(kernel, offset);
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