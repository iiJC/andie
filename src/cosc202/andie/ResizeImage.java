package cosc202.andie;

import javax.swing.JOptionPane;

import java.awt.*;
import java.awt.image.*;

/**
 * Implements an operation to resize an image by a specified percentage
 * increase.
 * This operation resizes the image while preserving its aspect ratio.
 * If the percentage increase is greater than 100, the image is scaled smoothly;
 * otherwise, it is scaled using area averaging.
 * If an error occurs during resizing, a JOptionPane with a warning message is
 * displayed.
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 *
 * @author Marcus Millan
 * @version 1.0
 */
public class ResizeImage implements ImageOperation, java.io.Serializable {

    /**
    * The percentage by which the image size should be increased.
    */
    private int sizePercentageIncrease;

    /**
     * Constructs a ResizeImage object with the specified size percentage increase.
     * 
     * @param sizePercentageIncrease The percentage increase by which to resize the
     *                               image.
     */
    ResizeImage(int sizePercentageIncrease) {
        this.sizePercentageIncrease = sizePercentageIncrease;
    }

    /**
     * Applies the resize operation to the input image.
     * If the sizePercentageIncrease is greater than 100, the image is scaled
     * smoothly;
     * otherwise, it is scaled using area averaging.
     * creates a new buffered image when it is scaled up or down. 
     * and implements some exception handling if cancel is pressed.
     * 
     * @param input The BufferedImage to be resized.
     * @return The resized BufferedImage.
     */
    public BufferedImage apply(BufferedImage input) {
        BufferedImage resizedBufferedImage = null;

        try {
            Image resizedImage = null;
            if (this.sizePercentageIncrease > 100) { //checks if user input is greater then 100
                resizedImage = input.getScaledInstance(
                        (int) (input.getWidth() * ((double) this.sizePercentageIncrease / 100)),
                        (int) (input.getHeight() * ((double) this.sizePercentageIncrease / 100)), Image.SCALE_SMOOTH);
                resizedBufferedImage = new BufferedImage(resizedImage.getWidth(null), resizedImage.getHeight(null), 2);
            } else {
                resizedImage = input.getScaledInstance(
                        (int) (input.getWidth() * ((double) this.sizePercentageIncrease / 100)),
                        (int) (input.getHeight() * ((double) this.sizePercentageIncrease / 100)),
                        Image.SCALE_AREA_AVERAGING);
                resizedBufferedImage = new BufferedImage(resizedImage.getWidth(null), resizedImage.getHeight(null), 2);
            }

            Graphics2D graphics = resizedBufferedImage.createGraphics();
            graphics.drawImage(resizedImage, 0, 0, null);
            graphics.dispose();
        } catch (Exception ex) {

            // Show JOptionPane with warning icon and message
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_RESIZE_CANCEL"),
                    Settings.getLanguageProperty("WARN_RESIZE_HEADER"),
                    JOptionPane.ERROR_MESSAGE);
        }

        return resizedBufferedImage;
    }

}
