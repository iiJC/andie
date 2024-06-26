package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 * <p>
 * This class implements the ImageOperation interface and can be used to apply
 * color channel cycling to a BufferedImage.
 * </p>
 *
 * @author Angus Tucker
 * @version 1.0
 */
public class ColourChannelCycling implements ImageOperation, java.io.Serializable {

    private int argbReOrderOption;

    /**
     * Constructs a ColourChannelCycling object with the specified ARGB reordering
     * option.
     * 
     * @param argbReOrderOption The option specifying the ARGB channel reordering.
     */
    public ColourChannelCycling(int argbReOrderOption) {
        this.argbReOrderOption = argbReOrderOption;
    }

    /**
     * Applies color channel cycling to the input image based on the specified
     * reordering option.
     * 
     * @param input The input BufferedImage to which the color channel cycling will
     *              be applied.
     * @return The resulting BufferedImage after applying the color channel cycling.
     */
    public BufferedImage apply(BufferedImage input) {

        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null),
                input.isAlphaPremultiplied(), null);

        // for loops to iterate through each pixel in the input image
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int argb = input.getRGB(x, y);
                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                // based on the specific option chosen by the user, each case performs a
                // different Colour Channel re-ordering
                switch (argbReOrderOption) {
                    case 0:

                        break;
                    case 1:
                        argb = (a << 24) | (r << 16) | (b << 8) | g;
                        break;
                    case 2:
                        argb = (a << 24) | (b << 16) | (r << 8) | g;
                        break;
                    case 3:
                        argb = (a << 24) | (g << 16) | (b << 8) | r;
                        break;
                    case 4:
                        argb = (a << 24) | (g << 16) | (r << 8) | b;
                        break;
                    case 5:
                        argb = (a << 24) | (b << 16) | (g << 8) | r;
                        break;
                }

                output.setRGB(x, y, argb);
            }
        }
        return output;

    }
}
