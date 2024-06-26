package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.GridLayout;
import javax.swing.JSpinner.DefaultEditor;

/**
 * <p>
 * Actions provided by the Filter menu.
 * </p>
 * 
 * <p>
 * The Filter menu contains actions that update each pixel in an image based on
 * some small local neighbourhood.
 * This includes a mean filter (a simple blur) in the sample code, but more
 * operations will need to be added.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @author Angus Tucker, Marcus Millan, Hadrian Tang, Jonathan Chan (Additions for various filters)
 * @version 1.0
 */
public class FilterActions {

    /** A list of actions for the Filter menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Filter menu actions.
     * </p>
     */
    public FilterActions() {
        actions = new ArrayList<Action>();
        createActions();
    }

    /**
     * Initializes and adds filter-related actions to the actions list.
     *
     * This method retrieves the language-specific labels for various filter
     * operations from the application settings.
     * It then creates actions for applying mean filter, median filter, gaussian
     * blur, and sharpen filter to images.
     * Each action is configured with a description and a mnemonic for accessibility
     * and ease of use.
     * These actions are added to a list, making them available for attaching to UI
     * components like buttons or menu items.
     */

    private void createActions() {
        String MeanFilter = Settings.getLanguageProperty("MeanFilter");
        String MedianFilter = Settings.getLanguageProperty("MedianFilter");
        String GaussianBlurFilter = Settings.getLanguageProperty("GaussianBlurFilter");
        String SharpenFilter = Settings.getLanguageProperty("SharpenFilter");
        String EmbossFilter = Settings.getLanguageProperty("EmbossFilter");
        String SobelFilter = Settings.getLanguageProperty("SobelFilter");
        String LaplacianFilter = Settings.getLanguageProperty("LaplacianFilter");
        String BlockAveragingFilter = Settings.getLanguageProperty("BlockAveragingFilter");
        String RandomScatteringFilter = Settings.getLanguageProperty("RandomScatteringFilter");
        String SaturationFilter = Settings.getLanguageProperty("SaturationFilter");

        // Existing actions
        actions.add(
                new MeanFilterAction(MeanFilter, null, Settings.getLanguageProperty("MeanFilterdesc"), KeyEvent.VK_M));
        actions.add(new MedianFilterAction(MedianFilter, null, Settings.getLanguageProperty("MedianFilterdesc"),
                KeyEvent.VK_D));
        actions.add(new GaussianBlurFilterAction(GaussianBlurFilter, null,
                Settings.getLanguageProperty("GaussianBlurFilterdesc"), KeyEvent.VK_G));
        actions.add(new SharpenFilterAction(SharpenFilter, null, Settings.getLanguageProperty("SharpenFilterdesc"),
                KeyEvent.VK_S));
        actions.add(new EmbossFilterAction(EmbossFilter, null, Settings.getLanguageProperty("EmbossFilterdesc"),
                Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new SobelFilterAction(SobelFilter, null, Settings.getLanguageProperty("SobelFilterdesc"),
                Integer.valueOf(KeyEvent.VK_M)));
        actions.add(new LaplacianFilterAction(LaplacianFilter, null,
                Settings.getLanguageProperty("LaplacianFilterdesc"), KeyEvent.VK_L));
        actions.add(new BlockAveragingFilterAction(BlockAveragingFilter, null,
                Settings.getLanguageProperty("BlockAveragingFilterdesc"), KeyEvent.VK_B));
        actions.add(new RandomScatteringFilterAction(RandomScatteringFilter, null,
                Settings.getLanguageProperty("RandomScatteringFilterdesc"), KeyEvent.VK_R));
        actions.add(new SaturationFilterAction(SaturationFilter, null,
                Settings.getLanguageProperty("SaturationFilterdesc"), KeyEvent.VK_T));
    }

    /**
     * <p>
     * Create a menu containing the list of Filter actions.
     * </p>
     * 
     * @return The filter menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu(Settings.getLanguageProperty("Filter"));

        for (Action action : actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    /**
     * <p>
     * Action to blur an image with a mean filter.
     * </p>
     * 
     * @see MeanFilter
     */
    public class MeanFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new mean-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        MeanFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the MeanFilterAction is triggered.
         * It prompts the user for a filter radius, then applies an appropriately sized
         * {@link MeanFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {

            // Determine the radius - ask the user.
            int radius = 1;

            // Pop-up dialog box to ask for the radius value.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            int option = JOptionPane.showOptionDialog(null, radiusSpinner,
                    Settings.getLanguageProperty("FILTER_RADIUS_DIALOG"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                radius = radiusModel.getNumber().intValue();
            }

            // Create and apply the filter
            target.getImage().apply(new MeanFilter(radius));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    public class MedianFilterAction extends ImageAction {

        MedianFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the MedianAction is triggered.
         * It prompts the user for a filter radius, then applies an appropriately sized
         * {@link MeanFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {

            // Determine the radius - ask the user.
            int radius = 1;

            // Pop-up dialog box to ask for the radius value.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            int option = JOptionPane.showOptionDialog(null, radiusSpinner,
                    Settings.getLanguageProperty("FILTER_RADIUS_DIALOG"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                radius = radiusModel.getNumber().intValue();
            }

            // Create and apply the filter
            target.getImage().apply(new MedianFilter(radius));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    // Gaussian blur filter
    public class GaussianBlurFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new gaussian-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        GaussianBlurFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {

            // Determine the radius - ask the user.
            int radius = 1;

            // Pop-up dialog box to ask for the radius value.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            int option = JOptionPane.showOptionDialog(null, radiusSpinner,
                    Settings.getLanguageProperty("FILTER_RADIUS_DIALOG"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                radius = radiusModel.getNumber().intValue();
            }

            // Create and apply the filter
            target.getImage().apply(new GaussianBlurFilter(radius));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Action to sharpen an image with a sharpen filter.
     * </p>
     * 
     * @see SharpenFilter
     */
    public class SharpenFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new mean-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        SharpenFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the SharpenFilter action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the SharpenFilterAction is triggered.
         * It applies the appropriate SharpenFilter action to the image
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {

            target.getImage().apply(new SharpenFilter());
            target.repaint();
            target.getParent().revalidate();
        }
    }

    // Emboss filter
    public class EmbossFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new Emboss-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        EmbossFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {

            // Define the filter directions
            String[] filterDirections = { "NORTH", "NORTH EAST", "EAST", "SOUTH EAST", "SOUTH", "SOUTH WEST", "WEST",
                    "NORTH WEST" };

            // Create a panel to hold the components
            JPanel panel = new JPanel(new GridLayout(0, 1));

            // Add a combo box for filter direction
            JComboBox<String> filterComboBox = new JComboBox<>(filterDirections);
            panel.add(new JLabel(Settings.getLanguageProperty("Filter_Direction")));
            panel.add(filterComboBox);

            // Add a combo box for offset
            String[] offsetOptions = { "Apply Offset", "No Offset" };
            JComboBox<String> offsetComboBox = new JComboBox<>(offsetOptions);
            panel.add(new JLabel("Offset:"));
            panel.add(offsetComboBox);

            // Show the dialog box
            int option = JOptionPane.showConfirmDialog(null, panel, Settings.getLanguageProperty("Filter_Options"),
                    JOptionPane.OK_CANCEL_OPTION);

            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            }

            // Extract the selected filter direction
            String direction = (String) filterComboBox.getSelectedItem();

            // Extract the selected offset option
            boolean apply_offset = offsetComboBox.getSelectedIndex() == 0;

            // Create and apply the filter
            target.getImage().apply(new EmbossFilter(direction, apply_offset));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Action to detect edges with Sobel filter.
     * </p>
     * 
     * @see SobelFilter
     */
    public class SobelFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new Sobel-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        SobelFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * This method is called whenever the SobelFilterAction is triggered.
         * It prompts the user for a direction, then applies the direction filter
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {

            // Define the filter directions
            String[] filterDirections = { "Horizontal", "Vertical" };

            // Create a panel to hold the components
            JPanel panel = new JPanel(new GridLayout(0, 1));

            // Add a combo box for filter direction
            JComboBox<String> filterComboBox = new JComboBox<>(filterDirections);
            panel.add(new JLabel(Settings.getLanguageProperty("Filter_Direction")));
            panel.add(filterComboBox);

            // Add a combo box for offset
            String[] offsetOptions = { "Apply Offset", "No Offset" };
            JComboBox<String> offsetComboBox = new JComboBox<>(offsetOptions);
            panel.add(new JLabel(Settings.getLanguageProperty("Offset")));
            panel.add(offsetComboBox);

            // Show the dialog box
            int option = JOptionPane.showConfirmDialog(null, panel, Settings.getLanguageProperty("Filter_Options"),
                    JOptionPane.OK_CANCEL_OPTION);

            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            }

            // Extract the selected filter direction
            String direction = (String) filterComboBox.getSelectedItem();

            // Extract the selected offset option
            boolean apply_offset = offsetComboBox.getSelectedIndex() == 0;

            // Create and apply the filter
            target.getImage().apply(new SobelFilter(direction, apply_offset));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Action to detect edges with Laplacian filter.
     * </p>
     * 
     * @see LaplacianFilter
     */
    public class LaplacianFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new Laplacian-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        LaplacianFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * This method is called whenever the LaplacianlFilterAction is triggered.
         * It prompts the user for an offset choice
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {

            // Create a panel to hold the components
            JPanel panel = new JPanel(new GridLayout(0, 1));

            // Add a combo box for offset
            String[] offsetOptions = { "Apply Offset", "No Offset" };
            JComboBox<String> offsetComboBox = new JComboBox<>(offsetOptions);
            panel.add(new JLabel("Offset:"));
            panel.add(offsetComboBox);

            // Show the dialog box
            int option = JOptionPane.showConfirmDialog(null, panel, "Filter Options", JOptionPane.OK_CANCEL_OPTION);

            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            }

            // Extract the selected offset option
            boolean apply_offset = offsetComboBox.getSelectedIndex() == 0;

            // Create and apply the filter
            target.getImage().apply(new LaplacianFilter(apply_offset));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    public class BlockAveragingFilterAction extends ImageAction {
        BlockAveragingFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            int blockWidth = 1; // Default block width
            int blockHeight = 1; // Default block height

            // Pop-up dialog box to ask for the block width value.
            SpinnerNumberModel widthModel = new SpinnerNumberModel(1, 1, null, 1);
            JSpinner widthSpinner = new JSpinner(widthModel);

            JComponent widthEditor = widthSpinner.getEditor();
            if (widthEditor instanceof DefaultEditor) {
                ((DefaultEditor) widthEditor).getTextField().setEditable(false);
            }

            int widthOption = JOptionPane.showOptionDialog(null, widthSpinner,
                    Settings.getLanguageProperty("BLOCK_WIDTH_DIALOG"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            if (widthOption == JOptionPane.OK_OPTION) {
                blockWidth = widthModel.getNumber().intValue();

                // Pop-up dialog box to ask for the block height value.
                SpinnerNumberModel heightModel = new SpinnerNumberModel(1, 1, null, 1);
                JSpinner heightSpinner = new JSpinner(heightModel);

                JComponent heightEditor = heightSpinner.getEditor();
                if (heightEditor instanceof DefaultEditor) {
                    ((DefaultEditor) heightEditor).getTextField().setEditable(false);
                }

                int heightOption = JOptionPane.showOptionDialog(null, heightSpinner,
                        Settings.getLanguageProperty("BLOCK_HEIGHT_DIALOG"),
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                if (heightOption == JOptionPane.OK_OPTION) {
                    blockHeight = heightModel.getNumber().intValue();

                    // Apply the filter
                    target.getImage().apply(new BlockAveragingFilter(blockWidth, blockHeight));
                    target.repaint();
                    target.getParent().revalidate();
                }
            }
        }
    }

    public class RandomScatteringFilterAction extends ImageAction {

        RandomScatteringFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Determine the radius - ask the user.
            int radius = 1;

            // Pop-up dialog box to ask for the radius value.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(5, 1, null, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);

            JComponent editor = radiusSpinner.getEditor();
            if (editor instanceof DefaultEditor) {
                ((DefaultEditor) editor).getTextField().setEditable(false);
            } // prevents user from manipulating the text box, resulting in error prevention

            int option = JOptionPane.showOptionDialog(null, radiusSpinner,
                    Settings.getLanguageProperty("SCATTER_RADIUS_DIALOG"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            // Check the return value from the dialog box.
            if (option == JOptionPane.OK_OPTION) {
                radius = radiusModel.getNumber().intValue();

                // Create and apply the filter
                target.getImage().apply(new RandomScatteringFilter(radius));
                target.repaint();
                target.getParent().revalidate();
            }
        }
    }

    /**
     * <p>
     * Action to adjust saturation of an image with a saturation filter.
     * </p>
     * 
     * @see SaturationFilter
     */
    public class SaturationFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new saturation-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        SaturationFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the SaturationFilter action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the SaturationFilterAction is triggered.
         * It applies the appropriate SaturationFilter action to the image
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            JSlider slider = new JSlider(-100, 100, 0); // Range -100 to 100, default 0
            slider.setMajorTickSpacing(50);
            slider.setMinorTickSpacing(10);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);

            int option = JOptionPane.showConfirmDialog(null, slider, Settings.getLanguageProperty("SATURATION_DIALOG"),
                    JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                float saturationLevel = 1.0f + (slider.getValue() / 100.0f);
                target.getImage().apply(new SaturationFilter(saturationLevel));
                target.repaint();
                target.getParent().revalidate();
            }
        }
    }
}