package cosc202.andie;

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Convo2 applies convolution filters to images using a specified kernel. It does padding on the side by getting the closest pixel value that's in the image
 * </p>
 * 
 * <p>
 * This class allows filtering of BufferedImage objects by applying convolution operations.
 * </p>
 * 
 * @author Hadrien Tang
 * 
 * */

public class Convo2 {

    private final Kernel kernel;
    private boolean offset;

    //these are for testing purposes. that way i can test only one channel to make sure the convolution works 
    public List<Integer> redValuesBefore = new ArrayList<>();
    public List<Integer> redValuesAfter = new ArrayList<>();

    //one constructor that is just the kernel, and one with the offset choice
    public Convo2(Kernel kernel) {
        this.kernel = kernel;
    }

    /**
    * Constructor for Convo2 with a kernel with the offset choice.
    *
    * @param kernel The convolution kernel to be used.
    * @param offset Whether to apply an offset.
    */
    public Convo2(Kernel kernel, boolean offset) {
        this.kernel = kernel;
        this.offset = offset;
    }

    /**
    * Applies the convolution filter to the input image.
    *
    * @param input  The input BufferedImage.
    * @param output The output BufferedImage.
    * @return The filtered BufferedImage.
    */
    public BufferedImage filter(BufferedImage input, BufferedImage output) {
        //if destination is null then create one that is exactly the same
        int middlevalue = 128;

        if (output == null) {
            output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
        } else if (input == output) {
            throw new IllegalArgumentException("Source and destination images must be different");
        }

        // Retrieve the kernel data
        float[] kernelData = kernel.getKernelData(null);

        // Extract kernel dimensions and padding
        int kernelWidth = kernel.getWidth();
        int r = kernelWidth / 2;

        //check if the input has an alpha channel
        ColorModel colorModel = input.getColorModel();
        boolean Alphachannel = colorModel.hasAlpha();

        //get the height and width of the image
        int height = input.getHeight();
        int width = input.getWidth();

        // Iterate over each pixel in the source image
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                // Initialize sums for each color channel
                float sumRed = 0;
                float sumGreen = 0;
                float sumBlue = 0;
                float sumAlpha = 0;

                //initialize the color channels
                int alpha = 0, red = 0, green = 0, blue = 0;

                // Iterate over each pixel in the kernel
                for (int ky = -r; ky <= r; ky++) {
                    for (int kx = -r; kx <= r; kx++) {

                        //the padding stuff, make sure that the kernel doesn't go over the edge basically
                        int pixelX = Math.min(Math.max(x + kx, 0), input.getWidth() - 1);
                        int pixelY = Math.min(Math.max(y + ky, 0), input.getHeight() - 1);

                        //get the color int from the pixel in the image
                        int argb = input.getRGB(pixelX, pixelY);

                        //seperate into each color channel

                        if (Alphachannel) {
                            alpha = (argb >> 24) & 0xFF;
                        }
                        red = (argb >> 16) & 0xFF;
                        green = (argb >> 8) & 0xFF;
                        blue = argb & 0xFF;

                        //add the red values to the list for testing purposes
                        redValuesBefore.add(red);

                        //this code does not work for some reason
                        /* 
                        alpha = (argb & 0xFF000000) >>> 24;
                        red = (argb & 0x00FF0000) >>> 16;
                        green = (argb & 0x0000FF00) >>> 8;
                        blue = (argb & 0x000000FF);
                        */

                        // Retrieve the kernel value at the current position
                        float kernelValue = kernelData[(ky + r) * kernelWidth + (kx + r)];

                        // Accumulate the weighted color values
                        if (Alphachannel) {
                            sumAlpha += kernelValue * alpha;
                        }
                        sumRed += kernelValue * red;
                        sumGreen += kernelValue * green;
                        sumBlue += kernelValue * blue;
                    }
                }

                // bound the numbers to the 0,255 range and apply offset if specified
                if (offset) {
                    sumRed += middlevalue;
                    sumGreen += middlevalue;
                    sumBlue += middlevalue;

                }

                //clamp the values to the 0,255 range
                sumRed = Math.min(Math.max(sumRed, 0), 255);
                sumGreen = Math.min(Math.max(sumGreen, 0), 255);
                sumBlue = Math.min(Math.max(sumBlue, 0), 255);

                //turn the floats into ints so that they can be put back together
                int endr = (int) (sumRed);
                int endg = (int) (sumGreen);
                int endb = (int) (sumBlue);

                int resultARGB;

                if (Alphachannel) {

                    if (offset)
                        sumAlpha += middlevalue;

                    sumAlpha = Math.min(Math.max(sumAlpha, 0), 255);
                    int enda = (int) (sumAlpha);
                    resultARGB = (enda << 24) | (endr << 16) | (endg << 8) | endb;
                } else {
                    //put the colors back into one
                    resultARGB = (255 << 24) | (endr << 16) | (endg << 8) | endb;
                }

                //add the convoluted and offseted red values to the list
                redValuesAfter.add(endr);

                // Set the color of the corresponding pixel in the destination image
                output.setRGB(x, y, resultARGB);
            }
        }

        // Return the resulting image
        return output;
    }

}
