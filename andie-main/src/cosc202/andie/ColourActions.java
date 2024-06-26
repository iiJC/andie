package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.GridLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>
 * Actions provided by the Colour menu.
 * </p>
 * 
 * <p>
 * The Colour menu contains actions that affect the colour of each pixel
 * directly
 * without reference to the rest of the image.
 * This includes conversion to greyscale in the sample code, but more operations
 * will need to be added.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @author Angus Tucker (Additions for BrightnessContrast, ColourChannelCycling)
 * @version 1.0
 */
public class ColourActions {

    /** A list of actions for the Colour menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Colour menu actions.
     * </p>
     */
    public ColourActions() {
        actions = new ArrayList<Action>();
        createActions();
    }

    /**
     * Initializes and populates the list of actions for the Colour menu.
     *
     * <p>
     * This method creates instances of actions related to color manipulation within
     * the image,
     * leveraging settings for language-specific labels.
     * It includes actions for converting an image to greyscale, inverting the
     * image's colors, and cycling through color channels.
     * Each action is associated with a specific key event for quick access.
     * These actions are then added to the actions list, to be included in the
     * application's UI.
     * </p>
     */
    private void createActions() {
        String Greyscale = Settings.getLanguageProperty("Greyscale");
        String ImageInversion = Settings.getLanguageProperty("ImageInversion");
        String ColourChannelCycling = Settings.getLanguageProperty("ColourChannelCycling");
        String BrightnessContrast = Settings.getLanguageProperty("BrightnessContrastAdjustment");

        actions.add(new ConvertToGreyAction(Greyscale, null, Settings.getLanguageProperty("Greyscaledesc"),
                Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new ImageInversionAction(ImageInversion, null, Settings.getLanguageProperty("ImageInversiondesc"),
                Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new ColourChannelCyclingAction(ColourChannelCycling, null,
                Settings.getLanguageProperty("ColourChannelCyclingdesc"), Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new BrightnessContrastAction(BrightnessContrast, null,
                Settings.getLanguageProperty("BrightnessContrastdesc"), Integer.valueOf(KeyEvent.VK_G)));
    }

    /**
     * <p>
     * Create a menu containing the list of Colour actions.
     * </p>
     * 
     * @return The colour menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu(Settings.getLanguageProperty("Colour"));

        for (Action action : actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    /**
     * <p>
     * Action to convert an image to greyscale.
     * </p>
     * 
     * @see ConvertToGrey
     */
    public class ConvertToGreyAction extends ImageAction {

        /**
         * <p>
         * Create a new convert-to-grey action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        ConvertToGreyAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ConvertToGreyAction is triggered.
         * It changes the image to greyscale.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().apply(new ConvertToGrey());
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * Action to invert the colors of the image.
     *
     * This method is triggered when the ImageInversionAction is activated. It
     * applies
     * the ImageInversion operation to the current image, effectively inverting its
     * colors,
     * and then triggers a repaint and revalidation of the target component.
     */
    public class ImageInversionAction extends ImageAction {
        ImageInversionAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
        * Callback for when the ImageInversionAction is triggered.
        * @param e The event triggering this callback.
        */
        public void actionPerformed(ActionEvent e) {
            target.getImage().apply(new ImageInversion());
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * An action for adjusting brightness and contrast of an image using sliders.
     * <p>
     * This action displays a dialog with sliders for adjusting the brightness and contrast of an image. 
     * When the user clicks "OK", the brightness and contrast adjustments are applied to the image.
     * </p>
     * <p>
     * This class extends the {@link ImageAction} class.
     * </p>
     * 
     * @see ImageAction
     * @see BrightnessAndContrast
     */
    public class BrightnessContrastAction extends ImageAction {

        BrightnessContrastAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the BrightnessContrastAction is triggered.
         * </p>
         */
        public void actionPerformed(ActionEvent e) {
            // Create Sliders for Brightness and Contrast
            JSlider brightnessSlider = createSlider(-100, 100, 25, 5);
            JSlider contrastSlider = createSlider(-100, 100, 20, 5);

            // Create Panel to hold Sliders
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(2, 2));
            panel.add(new JLabel(Settings.getLanguageProperty("brightness")));
            panel.add(brightnessSlider);
            panel.add(new JLabel(Settings.getLanguageProperty("contrast")));
            panel.add(contrastSlider);

            // Create Options for JOptionPane
            Object[] options = { Settings.getLanguageProperty("ok"), Settings.getLanguageProperty("cancel") };

            // Shows Option
            int option = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    Settings.getLanguageProperty("b_c_question"),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    (Icon) this.getValue("WindowIcon"),
                    options,
                    options[0]);

            // Apply Brightness/Contrast Filter if OK is clicked
            if (option == 0) {
                applyBrightnessContrast(brightnessSlider.getValue(), contrastSlider.getValue());
            }
        }

        // Helper method to create sliders
        private JSlider createSlider(int min, int max, int majorTickSpacing, int minorTickSpacing) {
            JSlider slider = new JSlider(min, max);
            slider.setMajorTickSpacing(majorTickSpacing);
            slider.setMinorTickSpacing(minorTickSpacing);
            slider.setPaintTicks(true);

            return slider;
        }

        // Applies the Brightness/Contrast Filter
        private void applyBrightnessContrast(int brightness, int contrast) {
            target.getImage().apply(new BrightnessAndContrast(brightness, contrast));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to apply specific Colour Channels to an image.
     * </p>
     * 
     * @see ColourChannelCycling
     */
    public class ColourChannelCyclingAction extends ImageAction {

        private int previousOptionIndex = 0;

        /**
         * <p>
         * Create a new ColourChannelCycling action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        ColourChannelCyclingAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the ColourChannelCycling action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ColourChannelCyclingAction is triggered.
         * It prompts the user with options towards re-ordering the image's RGB values,
         * then
         * applies the appropriate {@link ColourChannelCycling}
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            // Creates the user options of RGB colour channels
            Object[] options = { "Original RGB", "RBG", "GBR", "BRG", "GRB", "BGR" };

            // Creates a combo box, implementing the user's options
            JComboBox<Object> comboBox = new JComboBox<>(options);

            comboBox.setSelectedIndex(previousOptionIndex);

            // Displays the dialog box with the combo box
            int option = JOptionPane.showOptionDialog(null, comboBox,
                    Settings.getLanguageProperty("COLOUR_CYCLE_DIALOG"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            }

            // Retrievess the index of the selected option, then creates a
            // ColourChannelCycling object with the selected preference
            int selectedOptionIndex = comboBox.getSelectedIndex();
            ColourChannelCycling colourCyclingOperation = new ColourChannelCycling(selectedOptionIndex);

            // Applies the ColourChannelCycling operation to the given image
            target.getImage().apply(colourCyclingOperation);
            target.repaint();
            target.getParent().revalidate();

            previousOptionIndex = selectedOptionIndex;
        }

    }
}
