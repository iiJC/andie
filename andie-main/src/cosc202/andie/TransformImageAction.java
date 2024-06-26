package cosc202.andie;

import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;

/**
 * <p>
 * Actions provided by the Transform menu.
 * </p>
 * 
 * <p>
 * The Transform menu contains actions that affect the transformation of the
 * image,
 * such as resizing, rotating, etc.
 * </p>
 * 
 * @author Marcus Millan
 * @version 1.0
 */
public class TransformImageAction {

    /**
     * A list of actions for the Transform menu.
     * where actions for transform menu can be stored
     * 
     */
    protected ArrayList<Action> actions;

    /**
     * This is primarily where transform image actions will be stored such as rotate
     * and resize.
     */
    public TransformImageAction() {
        actions = new ArrayList<Action>();
        createActions();
    }

    /**
     * Initializes the actions for transformations such as resize, rotate, flip,
     * crop, draw, and transparency, and adds them to the actions list. Each action is configured with its name,
     * description, and a mnemonic for accessibility. The names of the actions are
     * retrieved from the application's settings, allowing for internationalization.
     */
    public void createActions() {
        String Resize = Settings.getLanguageProperty("Resize");
        String Rotate = Settings.getLanguageProperty("Rotate");
        String HorizontalFlip = Settings.getLanguageProperty("HorizontalFlip");
        String VerticalFlip = Settings.getLanguageProperty("VerticalFlip");
        String Crop = Settings.getLanguageProperty("Crop");
        String Draw = Settings.getLanguageProperty("Draw");
        String ClipShape = Settings.getLanguageProperty("ClipShape");
        String Transparency = Settings.getLanguageProperty("Transparency");

        actions.add(new ResizeAction(Resize, null, Settings.getLanguageProperty("Resizedesc"),
                Integer.valueOf(KeyEvent.VK_R)));
        actions.add(new RotateAction(Rotate, null, Settings.getLanguageProperty("Rotatedesc"),
                Integer.valueOf(KeyEvent.VK_O)));
        actions.add(new FlipAction(HorizontalFlip, null, Settings.getLanguageProperty("HorizontalFlipdesc"),
                KeyEvent.VK_H, 0));
        actions.add(
                new FlipAction(VerticalFlip, null, Settings.getLanguageProperty("VerticalFlipdesc"), KeyEvent.VK_V, 1));
        actions.add(new CropAction(Crop, null, Settings.getLanguageProperty("Cropdesc"), null));
        actions.add(new DrawAction(Draw, null, Settings.getLanguageProperty("Drawdesc"), null));
        actions.add(new ClipToShapeAction(ClipShape, null, Settings.getLanguageProperty("ClipShapedesc"), null));
        actions.add(new TransparencyAction(Transparency, null, Settings.getLanguageProperty("Transparencydesc"),
                Integer.valueOf(KeyEvent.VK_T)));
    }

    /**
     * Create a menu containing the list of Transform actions.
     * 
     * @return The transform menu UI element.
     */
    public JMenu createMenu() {
        JMenu transformMenu = new JMenu(Settings.getLanguageProperty("Transform"));

        for (Action action : actions) {
            transformMenu.add(new JMenuItem(action));
        }
        // goes throught the Action array and assigns it to the menu.

        return transformMenu;
    }

    /**
     * Defines the action for rotating an image. When triggered, this action
     * presents a dialog asking the user to select a rotation angle (90, 180, or 270 degrees) and applies the selected rotation to the current image. The image is then updated and displayed in the application window.
     */
    public class RotateAction extends ImageAction {
        /**
         * Constructs a new RotateAction with the specified parameters.
         * 
         * @param name      The name of the action.
         * @param icon      The icon representing the action.
         * @param desc      The description of the action.
         * @param mnemonic  The mnemonic key associated with the action.
         */
        RotateAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Performs the rotation action. This method is called whenever the RotateAction
         * is triggered.
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            Integer[] rotationAngles = { 90, 180, 270 };

            Integer rotationAngle = (Integer) JOptionPane.showInputDialog(
                    null, Settings.getLanguageProperty("ROTATE_DIALOG"), Settings.getLanguageProperty("ROTATE_HEADER"),
                    JOptionPane.QUESTION_MESSAGE, null,
                    rotationAngles, rotationAngles[0]);

            if (rotationAngle != null) {

                ImageRotate rotateImage = new ImageRotate(rotationAngle);

                target.getImage().apply(rotateImage);

                target.repaint();

            }
        }
    }

    /**
     * Defines the action for flipping an image. When triggered, this action applies
     * a flip transformation (horizontal or vertical) to the current image. The type
     * of flip (horizontal or vertical) is determined by the value of flipType parameter passed during initialization.
     */
    public class FlipAction extends ImageAction {

        private int flipType;

        /**
         * Constructs a new FlipAction with the specified parameters.
         * 
         * @param name      The name of the action.
         * @param icon      The icon representing the action.
         * @param desc      The description of the action.
         * @param mnemonic  The mnemonic key associated with the action.
         * @param flipType  The type of flip (0 for horizontal, 1 for vertical).
         */
        FlipAction(String name, ImageIcon icon, String desc, Integer mnemonic, int flipType) {
            super(name, icon, desc, mnemonic);
            this.flipType = flipType;
        }

        /**
         * Performs the flip action. This method is called whenever the FlipAction
         * is triggered.
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            ImageFlip imageFlip = new ImageFlip(flipType);

            target.getImage().apply(imageFlip);

            target.repaint();
        }
    }

    /**
     * Defines the action for resizing an image. When triggered, this action provides a prompt
     * for which the user can resize an image to their specifications. The scaling of the image is determined by the user input.
     */
    public class ResizeAction extends ImageAction {

        /**
         * Creates a new resize action.
         * 
         * @param name      The name of the action.
         * @param icon      An icon to use to represent the action.
         * @param desc      A brief description of the action.
         * @param mnemonic  A mnemonic key to use as a shortcut.
         */
        ResizeAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Callback for when the resize action is triggered. This method is called
         * whenever the ResizeAction is triggered. It resizes the image by calling the resize image class and passing in the user input.
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            // Prompt user for size percentage increase
            int sizePercentageIncrease = promptForSizePercentageIncrease();

            // Create a new ResizeImage operation
            ResizeImage resizeImage = new ResizeImage(sizePercentageIncrease);

            // Apply the resizing operation on the target image
            target.getImage().apply(resizeImage);

            // Repaint the target component
            target.repaint();

            target.getParent().revalidate();
        }

        /**
         * Method to prompt the user for the size percentage increase. Uses exception handling to ensure the user input is a valid positive integer.
         * 
         * @return The size percentage increase entered by the user.
         */
        private int promptForSizePercentageIncrease() {
            // Display an input dialog to prompt the user for the size percentage increase
            String inputValue = JOptionPane.showInputDialog(null,
                    Settings.getLanguageProperty("RESIZE_DIALOG"), Settings.getLanguageProperty("RESIZE_HEADER"),
                    JOptionPane.QUESTION_MESSAGE);

            // Check if the input dialog is canceled
            if (inputValue == null) {
                // Return a special value to indicate cancellation
                return -1;
            }

            // Check if the input value is empty
            if (inputValue.trim().isEmpty()) {
                // Show an error message if the input value is empty
                JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_VALID_INTEGER"),
                        Settings.getLanguageProperty("WARN_INVALID_INPUT"),
                        JOptionPane.ERROR_MESSAGE);
                // Recursive call to prompt again
                return promptForSizePercentageIncrease();
            }

            // Parses the input value as an integer
            try {
                int sizePercentageIncrease = Integer.parseInt(inputValue);
                // Check if the entered value is valid (positive integer)
                if (sizePercentageIncrease > 0) {
                    return sizePercentageIncrease;
                } else {
                    // Show an error message if the entered value is not valid
                    JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_POSITIVE_INTEGER"),
                            Settings.getLanguageProperty("WARN_INVALID_INPUT"),
                            JOptionPane.ERROR_MESSAGE);
                    // Recursive call to prompt again
                    return promptForSizePercentageIncrease();
                }
            } catch (NumberFormatException e) {
                // Show an error message if the entered value is not a valid integer
                JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_VALID_INTEGER"),
                        Settings.getLanguageProperty("WARN_INVALID_INPUT"),
                        JOptionPane.ERROR_MESSAGE);
                // Recursive call to prompt again
                return promptForSizePercentageIncrease();
            }
        }
    }

    /**
     * Defines the action for cropping an image. When triggered, this action applies
     * a crop transformation to the current image based on the user's selection.
     * The selected region is determined by the current selection made by the user on the image.
     */
    public class CropAction extends ImageAction {
        int x1, y1, width, height;

        /**
         * Initializes the action for cropping an image. When triggered, this action
         * applies a crop transformation to the current image based on the user's selection.
         * The selected region is determined by the current selection made by the user on the image.
         * 
         * @param name The name of the action.
         * @param icon The icon representing the action.
         * @param desc The description of the action.
         * @param mnemonic The mnemonic key associated with the action.
         */
        CropAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Performs the cropping action on the current image.
         * If a selection has been made by the user, the action crops the image
         * to the selected region. Otherwise, it does nothing.
         * 
         * @param e The ActionEvent triggering this action.
         */
        public void actionPerformed(ActionEvent e) {
            x1 = target.selection.getX();
            y1 = target.selection.getY();
            width = target.selection.getWidth();
            height = target.selection.getHeight();

            if (target.selection.isSelectionMade()) {
                ImageCrop cropImage = new ImageCrop(x1, y1, width + 1, height + 1);

                // Apply the resizing operation on the target image
                target.getImage().apply(cropImage);

                // Repaint the target component
                target.repaint();

                target.getParent().revalidate();

                target.selection.clearSelection();
            }
        }
    }

    public class DrawAction extends ImageAction {
        int x, y, width, height;
        Color color;
        String shape;
        boolean shouldDraw;

        /**
         * Initializes the action for drawing shapes on the image. When triggered, this action
         * prompts the user to select a shape and a color for drawing on the image.
         * 
         * @param name The name of the action.
         * @param icon The icon representing the action.
         * @param desc The description of the action.
         * @param mnemonic The mnemonic key associated with the action.
         */
        DrawAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Performs the drawing action on the current image.
         * If a selection has been made by the user, the action draws the selected shape
         * with the selected color on the image at the selected region. Otherwise, it does nothing.
         * 
         * @param e The ActionEvent triggering this action.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage()) {
                JOptionPane.showMessageDialog(null, "No image loaded.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            x = target.selection.getX();
            y = target.selection.getY();
            width = target.selection.getWidth();
            height = target.selection.getHeight();

            JComboBox<String> shapeSelector = new JComboBox<>(new String[] {
                    "Rectangle", "Ellipse", "Round Rectangle", "Triangle", "Pentagon", "Hexagon", "Star", "Line"
            });
            JComboBox<String> colorSelector = new JComboBox<>(new String[] { "Red", "Green", "Blue", "Black" });

            Object[] options = { "Shape:", shapeSelector, "Color:", colorSelector };
            int option = JOptionPane.showOptionDialog(null, options, "Select Shape",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

            if (option == JOptionPane.OK_OPTION) {
                shape = (String) shapeSelector.getSelectedItem();
                String colorStr = (String) colorSelector.getSelectedItem();

                switch (colorStr) {
                    case "Red":
                        color = Color.RED;
                        break;
                    case "Green":
                        color = Color.GREEN;
                        break;
                    case "Blue":
                        color = Color.BLUE;
                        break;
                    case "Black":
                        color = Color.BLACK;
                        break;
                    default:
                        color = Color.RED; // Default to red if color selection is invalid
                }
                shouldDraw = true;

                if (target.selection.isSelectionMade()) { // only if an area is selected
                    Shape drawShape = createShape(shape, x, y, width, height);

                    if (drawShape != null) {
                        DrawShapes draw = new DrawShapes(drawShape, color);

                        // Apply the drawing operation on the target image
                        target.getImage().apply(draw);

                        // Repaint the target component
                        target.repaint();

                        target.getParent().revalidate();

                        target.selection.clearSelection();
                    }
                }
            }
        }

        /**
         * Creates a shape based on the specified type and dimensions.
         * 
         * @param shapeType The type of shape to create.
         * @param x The x-coordinate of the shape.
         * @param y The y-coordinate of the shape.
         * @param width The width of the shape.
         * @param height The height of the shape.
         * @return The created shape.
         */
        private Shape createShape(String shapeType, int x, int y, int width, int height) {
            switch (shapeType) {
                case "Rectangle":
                    return new Rectangle2D.Double(x, y, width, height);
                case "Ellipse":
                    return new Ellipse2D.Double(x, y, width, height);
                case "Round Rectangle":
                    return new RoundRectangle2D.Double(x, y, width, height, width / 5.0, height / 5.0);
                case "Triangle":
                    return createPolygon(new int[] { x + width / 2, x, x + width },
                            new int[] { y, y + height, y + height }, 3);
                case "Pentagon":
                    return createRegularPolygon(5, x + width / 2, y + height / 2, Math.min(width, height) / 2);
                case "Hexagon":
                    return createRegularPolygon(6, x + width / 2, y + height / 2, Math.min(width, height) / 2);
                case "Star":
                    return createStar(5, x + width / 2, y + height / 2, Math.min(width, height) / 4,
                            Math.min(width, height) / 2);
                case "Line":
                    return new Line2D.Double(target.selection.getPressX(), target.selection.getPressY(),
                            target.selection.getReleaseX(), target.selection.getReleaseY());
                default:
                    return null;
            }
        }

        /**
         * Creates a polygon shape with the given points.
         * 
         * @param xPoints The x-coordinates of the points.
         * @param yPoints The y-coordinates of the points.
         * @param nPoints The number of points.
         * @return A Polygon shape.
         */
        private Shape createPolygon(int[] xPoints, int[] yPoints, int nPoints) {
            return new Polygon(xPoints, yPoints, nPoints);
        }

        /**
         * Creates a regular polygon (e.g., pentagon, hexagon) with the specified number of sides.
         * 
         * @param numSides The number of sides of the polygon.
         * @param centerX  The x-coordinate of the center of the polygon.
         * @param centerY  The y-coordinate of the center of the polygon.
         * @param radius   The radius of the polygon.
         * @return A regular Polygon shape.
         */
        private Shape createRegularPolygon(int numSides, int centerX, int centerY, int radius) {
            int[] xPoints = new int[numSides];
            int[] yPoints = new int[numSides];
            for (int i = 0; i < numSides; i++) {
                xPoints[i] = centerX + (int) (radius * Math.cos(2 * Math.PI * i / numSides));
                yPoints[i] = centerY + (int) (radius * Math.sin(2 * Math.PI * i / numSides));
            }
            return new Polygon(xPoints, yPoints, numSides);
        }

        /**
         * Creates a star shape with the specified number of points.
         * 
         * @param numPoints  The number of points of the star.
         * @param centerX    The x-coordinate of the center of the star.
         * @param centerY    The y-coordinate of the center of the star.
         * @param innerRadius The radius of the inner points of the star.
         * @param outerRadius The radius of the outer points of the star.
         * @return A star-shaped Polygon.
         */
        private Shape createStar(int numPoints, int centerX, int centerY, int innerRadius, int outerRadius) {
            int[] xPoints = new int[2 * numPoints];
            int[] yPoints = new int[2 * numPoints];
            for (int i = 0; i < 2 * numPoints; i++) {
                int radius = (i % 2 == 0) ? outerRadius : innerRadius;
                xPoints[i] = centerX + (int) (radius * Math.cos(Math.PI * i / numPoints));
                yPoints[i] = centerY + (int) (radius * Math.sin(Math.PI * i / numPoints));
            }
            return new Polygon(xPoints, yPoints, 2 * numPoints);
        }
    }

    /**
     * Defines the action for clipping the image to a shape on the image. When triggered, this action
     * prompts the user to select a shape and a color for drawing on the image.
     */
    public class ClipToShapeAction extends ImageAction {

        ClipToShapeAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Prompt the user to select a shape
            String[] shapeOptions = { "Rectangle", "Ellipse", "Round Rectangle", "Triangle", "Pentagon", "Hexagon",
                    "Star" };
            String selectedShape = (String) JOptionPane.showInputDialog(null, "Select Shape", "Crop to Shape",
                    JOptionPane.PLAIN_MESSAGE, null, shapeOptions, shapeOptions[0]);

            if (selectedShape != null) {
                // Get the current image dimensions
                int width = target.getImage().getCurrentImage().getWidth();
                int height = target.getImage().getCurrentImage().getHeight();

                // Define the shape based on the selected shape type
                Shape shape = null;
                switch (selectedShape) {
                    case "Rectangle":
                        shape = new Rectangle2D.Double(0, 0, width, height);
                        break;
                    case "Ellipse":
                        shape = new Ellipse2D.Double(0, 0, width, height);
                        break;
                    case "Round Rectangle":
                        shape = new RoundRectangle2D.Double(0, 0, width, height, width / 5.0, height / 5.0);
                        break;
                    case "Triangle":
                        shape = createPolygon(new int[] { width / 2, 0, width }, new int[] { 0, height, height }, 3);
                        break;
                    case "Pentagon":
                        shape = createRegularPolygon(5, width / 2, height / 2, Math.min(width, height) / 2);
                        break;
                    case "Hexagon":
                        shape = createRegularPolygon(6, width / 2, height / 2, Math.min(width, height) / 2);
                        break;
                    case "Star":
                        shape = createStar(5, width / 2, height / 2, Math.min(width, height) / 4,
                                Math.min(width, height) / 2);
                        break;
                }

                if (shape != null) {
                    ClipToShape cropShape = new ClipToShape(shape);

                    // Crop the image using the selected shape
                    target.getImage().apply(cropShape);

                    // Repaint the target component
                    target.repaint();
                }
            }
        }

        /**
         * Creates a polygon shape with the given points.
         * 
         * @param xPoints The x-coordinates of the points.
         * @param yPoints The y-coordinates of the points.
         * @param nPoints The number of points.
         * @return A Polygon shape.
         */
        private Shape createPolygon(int[] xPoints, int[] yPoints, int nPoints) {
            return new Polygon(xPoints, yPoints, nPoints);
        }

        /**
         * Creates a regular polygon (e.g., pentagon, hexagon) with the specified number of sides.
         * 
         * @param numSides The number of sides of the polygon.
         * @param centerX  The x-coordinate of the center of the polygon.
         * @param centerY  The y-coordinate of the center of the polygon.
         * @param radius   The radius of the polygon.
         * @return A regular Polygon shape.
         */
        private Shape createRegularPolygon(int numSides, int centerX, int centerY, int radius) {
            int[] xPoints = new int[numSides];
            int[] yPoints = new int[numSides];
            for (int i = 0; i < numSides; i++) {
                xPoints[i] = centerX + (int) (radius * Math.cos(2 * Math.PI * i / numSides));
                yPoints[i] = centerY + (int) (radius * Math.sin(2 * Math.PI * i / numSides));
            }
            return new Polygon(xPoints, yPoints, numSides);
        }

        /**
         * Creates a star shape with the specified number of points.
         * 
         * @param numPoints  The number of points of the star.
         * @param centerX    The x-coordinate of the center of the star.
         * @param centerY    The y-coordinate of the center of the star.
         * @param innerRadius The radius of the inner points of the star.
         * @param outerRadius The radius of the outer points of the star.
         * @return A star-shaped Polygon.
         */
        private Shape createStar(int numPoints, int centerX, int centerY, int innerRadius, int outerRadius) {
            int[] xPoints = new int[2 * numPoints];
            int[] yPoints = new int[2 * numPoints];
            for (int i = 0; i < 2 * numPoints; i++) {
                int radius = (i % 2 == 0) ? outerRadius : innerRadius;
                xPoints[i] = centerX + (int) (radius * Math.cos(Math.PI * i / numPoints));
                yPoints[i] = centerY + (int) (radius * Math.sin(Math.PI * i / numPoints));
            }
            return new Polygon(xPoints, yPoints, 2 * numPoints);
        }
    }

    /**
     * for the transparency action class
     * @author Marcus Millan
     */
    public class TransparencyAction extends ImageAction {

        /**
         * Initializes the action for adjusting the transparency of an image. When triggered,
         * this action prompts the user to select a transparency level and applies it to the image.
         * 
         * @param name The name of the action.
         * @param icon The icon representing the action.
         * @param desc The description of the action.
         * @param mnemonic The mnemonic key associated with the action.
         */
        TransparencyAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * Performs the transparency adjustment action on the current image.
         * Prompts the user for a transparency level using a slider and applies it to the image.
         * 
         * @param e The ActionEvent triggering this action.
         */
        public void actionPerformed(ActionEvent e) {
            // Prompt user for transparency level using a slider
            int transparencyLevel = promptForTransparencyLevel();

            // Check if the user canceled the dialog
            if (transparencyLevel == -1) {
                return;
            }

            // Create a new TransparencyImage operation
            TransparencyImage transparencyImage = new TransparencyImage(transparencyLevel);

            // Apply the transparency operation on the target image
            target.getImage().applyTransparency(transparencyImage);

            // Repaint the target component
            target.repaint();

            target.getParent().revalidate();
        }

        /**
         * Prompts the user for the transparency level using a JSlider.
         * 
         * @return The transparency level entered by the user.
         */
        private int promptForTransparencyLevel() {
            // Create a slider with values from 0 to 100
            int currentTransparencyLevel = target.getImage().getTransparencyLevel();

            JSlider slider = new JSlider(0, 100, currentTransparencyLevel);
            slider.setMajorTickSpacing(10);
            slider.setMinorTickSpacing(1);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);

            // Create a panel to hold the slider
            JPanel panel = new JPanel();
            panel.add(new JLabel(Settings.getLanguageProperty("TRANSPARENCY_LABEL")));
            panel.add(slider);

            // Show the dialog with the slider
            int option = JOptionPane.showConfirmDialog(null, panel, Settings.getLanguageProperty("TRANSPARENCY_HEADER"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // Check if the user clicked OK
            if (option == JOptionPane.OK_OPTION) {
                return slider.getValue();
            } else {
                // Return a special value to indicate cancellation
                return -1;
            }
        }
    }
}
