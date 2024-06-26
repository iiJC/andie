package cosc202.andie;

import java.awt.image.*;
import java.util.*;

/**
 * <p>
 * ImageOperation to apply a Median filter.
 * </p>
 * 
 * <p>
 * Implements a median filter operation to reduce noise in an image.
 * The median filter replaces each pixel value with the median value
 * of its neighboring pixels within a local window.
 * </p>
 * 
 * @author Hadrien Tang
 */
public class MedianFilter implements ImageOperation, java.io.Serializable {

    private int radius;

    /**
     * Constructs a MedianFilter object with a specified radius.
     * The radius determines the size of the local neighborhood window.
     * 
     * @param radius The radius of the neighborhood window.
     */
    MedianFilter(int radius) {
        this.radius = radius;
    }

    /**
     * Constructs a MedianFilter object with a default radius of 1.
     */
    MedianFilter() {
        this(1);
    }

    /**
     * Applies the median filter operation to the input image.
     * For each pixel in the input image, this method computes the median
     * value of the pixel values in its local neighborhood defined by the
     * radius. The median value is then used as the new pixel value.
     * 
     * @param input The BufferedImage to which the filter is applied.
     * @return A new BufferedImage object with the median filter applied.
     * @throws NullPointerException If the input image is null.
     */
    public BufferedImage apply(BufferedImage input) {
        try {
            // make the output image
            BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null),
                    input.isAlphaPremultiplied(), null);
            // array size of the local neighborhood
            int size = (2 * radius + 1) * (2 * radius + 1);
            int median = size / 2;

            // go throught the entire image with this forloop
            for (int y = 0; y < input.getHeight(); y++) {
                for (int x = 0; x < input.getWidth(); x++) {
                    // get the neighborhood for each pixel that we're currently at
                    int[][] neighbors = getNeighborhood(input, x, y);

                    int[] a = new int[size];
                    int[] r = new int[size];
                    int[] g = new int[size];
                    int[] b = new int[size];

                    // go through the neigborhood and get the pixel values for each pixel and put
                    // them in the corresponding rgb channels
                    int index = 0;
                    for (int i = 0; i < neighbors.length; i++) {
                        for (int j = 0; j < neighbors.length; j++) {
                            int argb = neighbors[i][j];
                            a[index] = (argb & 0xFF000000) >> 24;
                            r[index] = (argb & 0x00FF0000) >> 16;
                            g[index] = (argb & 0x0000FF00) >> 8;
                            b[index] = (argb & 0x000000FF);
                            index++;
                        }
                    }

                    // sort the arrays
                    Arrays.sort(a);
                    Arrays.sort(r);
                    Arrays.sort(g);
                    Arrays.sort(b);

                    // get the medians
                    int amed = a[median];
                    int rmed = r[median];
                    int gmed = g[median];
                    int bmed = b[median];

                    // chuck them back together
                    int argb = (amed << 24) | (rmed << 16) | (gmed << 8) | bmed;

                    output.setRGB(x, y, argb);

                }
            }
            return output;
        } catch (NullPointerException e) {
            System.out.println(Settings.getLanguageProperty("WARN_NULL_INPUT"));
            return input;
        }
    }

    /**
     * Retrieves the local neighborhood of a pixel in the image.
     * The neighborhood consists of pixels surrounding the specified
     * pixel within the radius defined by the filter.
     * 
     * @param input The BufferedImage containing the pixel.
     * @param x     The x-coordinate of the pixel.
     * @param y     The y-coordinate of the pixel.
     * @return A 2D array representing the local neighborhood of the pixel.
     */
    int[][] getNeighborhood(BufferedImage input, int x, int y) {
        int[][] neighborhood = new int[radius * 2 + 1][radius * 2 + 1];
        int X1 = Math.max(0, x - radius);
        int Y1 = Math.max(0, y - radius);
        int Xend = Math.min(input.getWidth() - 1, x + radius);
        int Yend = Math.min(input.getHeight() - 1, y + radius);

        for (int i = X1; i <= Xend; i++) {
            for (int j = Y1; j <= Yend; j++) {
                neighborhood[i - X1][j - Y1] = input.getRGB(i, j);
            }
        }

        return neighborhood;
    }

}
