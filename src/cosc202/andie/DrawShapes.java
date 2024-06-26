package cosc202.andie;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Provides functionality for drawing shapes (rectangles, ellipses, lines) on a BufferedImage.
 * Implements the ImageOperation interface to apply drawing operations on BufferedImage objects.
 * 
 * @author Ned Redmond
 */
public class DrawShapes implements ImageOperation, Serializable {

    private Shape shape;
    private Color color;

    /**
     * Constructs a new DrawShapes operation.
     *
     * @param shape The shape to draw.
     * @param color The color of the shape.
     */
    public DrawShapes(Shape shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    /**
     * Applies the drawing operation to a BufferedImage.
     * Draws the specified shape on the image.
     * 
     * @param input The BufferedImage to draw the shapes on.
     * @return A new BufferedImage object with shapes drawn according to the parameters.
     */
    public BufferedImage apply(BufferedImage input) {
        try {
            // Create a Graphics2D object from the input image
            Graphics2D g2d = input.createGraphics();

            // Set the color for the shape
            g2d.setColor(color);

            // Draw the shape on the image
            g2d.draw(shape);
            g2d.fill(shape);

            // Dispose the graphics object
            g2d.dispose();

            return input;
        } catch (Exception e) {
            System.err.println(Settings.getLanguageProperty("ERROR_Shape") + e.getMessage());
            return input; // Return the original input if there is an exception.
        }
    }
}
