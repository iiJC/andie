package cosc202.andie;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.*;
import java.awt.image.*;


public class ConvolutionTest {

    // Test case for filtering an image with a simple kernel
    @Test
    public void testFilterWithSimpleKernel() {
        // Define a simple kernel for testing
        float[] simpleKernel = {1, 1, 1, 1, 1, 1, 1, 1, 1};
        Kernel kernel = new Kernel(3, 3, simpleKernel);
        
        Convo2 convolution = new Convo2(kernel);
        
        // Create a sample input BufferedImage
        BufferedImage inputImage = createUniformImage(100, 100, 0xFFFFFFFF);
        
        // Call the filter method to apply convolution
        BufferedImage outputImage = convolution.filter(inputImage, null);
        
        //the expected output
        // the input image is a grayscale image with uniform pixel values and the kernel is meant to perform a simple averaging operation
        int expectedPixelValue = 255; // Assuming input image pixel value is 255 (white)
        int expectedOutputWidth = inputImage.getWidth(); 
        int expectedOutputHeight = inputImage.getHeight(); 
        
        // Assert that the output image is not null
        assertNotNull(outputImage);
        
        // Assert that the dimensions of the output image match the expected dimensions
        assertEquals(expectedOutputWidth, outputImage.getWidth());
        assertEquals(expectedOutputHeight, outputImage.getHeight());
        
        // Assert that each pixel value in the output image matches the expected value
        for (int y = 0; y < expectedOutputHeight; y++) {
            for (int x = 0; x < expectedOutputWidth; x++) {
                int actualPixelValue = new Color(outputImage.getRGB(x, y)).getRed(); // Assuming grayscale
                assertEquals(expectedPixelValue, actualPixelValue);
            }
        }
    }
    
    //this test applies a NWemboss filter and basically checks the red channel to make sure that the 
    //convoliution operation worked properly. I added public lists in the convolution code to make the test code work
    //and i ran the convolution on excel to make sure the values are correct. 
    @Test
    void testConvolutionOn5x5ImageEmbossNW() {
        // Define kernel
        float[] kernelData = { 1, 0, 0, 0, 0, 0, 0, 0, -1}; // 3x3 kernel with all ones
        Kernel kernel = new Kernel(3, 3, kernelData);

        // Define convolution operation with nearest pixel value padding
        Convo2 convolution = new Convo2(kernel, true);

        // Define input image (5x5)
        int[][] inputPixels = {
            { 100, 120, 140, 160},
            { 110, 130, 150, 170},
            { 120, 140, 160, 180},
            { 130, 150, 170, 190},
        };
        BufferedImage inputImage = createBufferedImage(inputPixels);

        // Assert input image creation
        assertNotNull(inputImage);

        // Apply convolution on input image
        BufferedImage outputImage = convolution.filter(inputImage, null);

        // Assert output image creation
        assertNotNull(outputImage);

        // Expected output pixel values after convolution
        int[] expectedRedValuesAfter = { 107, 94, 97, 111, 100, 88, 91, 105 , 101, 90, 93, 106, 108, 97, 100, 112 };

        // Assert output image pixel values
         assertArrayEquals(expectedRedValuesAfter, convolution.redValuesAfter.stream().mapToInt(i -> i).toArray());
    }

    private void assertImagePixelValues(BufferedImage image, int[][] expectedPixels) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int actualPixel = image.getRGB(x, y) & 0xFF; // Extracting the pixel value (grayscale)
                int expectedPixel = expectedPixels[y][x];
                assertEquals(expectedPixel, actualPixel, "Mismatch at position (" + x + ", " + y + ")");
            }
        }
    }

    public static BufferedImage createUniformImage(int width, int height, int argb) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Set all pixels to the specified ARGB value
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, argb);
            }
        }

        return image;
    }

    public static BufferedImage createBufferedImage(int[][] pixels) {
        int width = pixels[0].length;
        int height = pixels.length;
    
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = bufferedImage.getRaster();
    
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelValue = pixels[y][x];
                raster.setSample(x, y, 0, pixelValue);
            }
        }
    
        return bufferedImage;
    }
}
