package cosc202.andie;

import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Actions provided by the File menu.
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * 
 * @author Steven Mills
 * @author Jonathan Chan (Additions for Language, Export, and Exit)
 * @version 1.0
 */

public class FileActions {

    protected ArrayList<Action> actions;

    /**
     * Initializes FileActions by creating a list of file-related actions.
     *
     * This constructor initializes FileActions by creating an empty list of actions
     * and then populating it
     * by calling the createActions() method.
     */
    public FileActions() {
        actions = new ArrayList<>();
        createActions();
    }

    /**
     * Creates file-related actions.
     *
     * This method creates instances of various file-related actions, such as open,
     * save, save as, export, and exit.
     * It retrieves language-specific labels for each action from the settings.
     * Each action is associated with a specific keyboard shortcut for quick access.
     * The created actions are added to the actions list for later use.
     */

    private void createActions() {
        String open = Settings.getLanguageProperty("Open");
        String save = Settings.getLanguageProperty("Save");
        String saveAs = Settings.getLanguageProperty("SaveAs");
        String export = Settings.getLanguageProperty("Export");
        String exit = Settings.getLanguageProperty("Exit");

        actions.add(new FileOpenAction(open, null, Settings.getLanguageProperty("Opendesc"), KeyEvent.VK_O));
        actions.add(new FileSaveAction(save, null, Settings.getLanguageProperty("Savedesc"), KeyEvent.VK_S));
        actions.add(new FileSaveAsAction(saveAs, null, Settings.getLanguageProperty("Saveasdesc"), KeyEvent.VK_A));
        actions.add(new FileExportAction(export, null, Settings.getLanguageProperty("Exportdesc"), KeyEvent.VK_E));
        actions.add(new FileExitAction(exit, null, Settings.getLanguageProperty("Exitdesc"), KeyEvent.VK_F4));
    }

    /**
     * Creates a menu containing file-related actions.
     *
     * This method creates a menu containing file-related actions, such as open,
     * save, save as, export, and exit.
     * It retrieves the language-specific label for the file menu from the settings.
     * Each action is added as a JMenuItem to the file menu.
     * Additionally, a separator is added between the file actions and the language
     * change action.
     * The resulting menu is then returned.
     * 
     * @return The JMenu containing the file actions.
     */

    public JMenu createMenu() {
        JMenu fileMenu = new JMenu(Settings.getLanguageProperty("File"));

        for (Action action : actions) {
            if (action instanceof FileExitAction) {
                fileMenu.add(new JMenuItem(action)).setAccelerator(
                        KeyStroke.getKeyStroke((Integer) action.getValue("MnemonicKey"), InputEvent.ALT_DOWN_MASK));
            } else {
                fileMenu.add(new JMenuItem(action)).setAccelerator(
                        KeyStroke.getKeyStroke((Integer) action.getValue("MnemonicKey"), InputEvent.CTRL_DOWN_MASK));
            }
        }

        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(new ChangeLanguageAction(Settings.getLanguageProperty("Language"), null,
                Settings.getLanguageProperty("Languagedesc"), KeyEvent.VK_L)));

        return fileMenu;
    }

    /**
     * Action to open an image from file.
     */
    public class FileOpenAction extends ImageAction {
        FileOpenAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            // Check if there is an image and if there are unsaved changes
            if (target.getImage().hasImage() && !target.getImage().isSaved()) {
                int response = JOptionPane.showConfirmDialog(null,
                        Settings.getLanguageProperty("IMAGE_OVERRIDE"),
                        Settings.getLanguageProperty("CONFIRM"),
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) {
                    return;
                } else if (response == JOptionPane.YES_OPTION) {
                    // Save the current image before opening a new one
                    try {
                        target.getImage().save();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("ERROR"),
                                Settings.getLanguageProperty("ERROR_HEADER"),
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().open(imageFilepath);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_FILE_OPEN_GENERIC"),
                            Settings.getLanguageProperty("ERROR_HEADER"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * Action to save an image to its current file location.
     */
    public class FileSaveAction extends ImageAction {
        FileSaveAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            // Check if there is an image loaded in the target component
            EditableImage editableImage = ImageAction.getTarget().getImage();
            if (!editableImage.hasImage()) {
                JOptionPane.showMessageDialog(target, Settings.getLanguageProperty("WARN_NO_FILE_FOUND"),
                        Settings.getLanguageProperty("ERROR"),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                target.getImage().save();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_SAVE"),
                        Settings.getLanguageProperty("ERROR_HEADER"), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    /**
     * Action to save an image to a new file location.
     */
    public class FileSaveAsAction extends ImageAction {
        FileSaveAsAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            // Check if there is an image loaded in the target component
            EditableImage editableImage = ImageAction.getTarget().getImage();
            if (!editableImage.hasImage()) {
                JOptionPane.showMessageDialog(target, Settings.getLanguageProperty("WARN_NO_FILE"),
                        Settings.getLanguageProperty("ERROR"),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().saveAs(imageFilepath);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_SAVE"),
                            Settings.getLanguageProperty("ERROR_HEADER"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
    }

    /**
     * Exports the current operations as a new image.
     */
    public class FileExportAction extends ImageAction {
        FileExportAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Check if there is an image loaded in the target component
            EditableImage editableImage = ImageAction.getTarget().getImage();
            if (!editableImage.hasImage()) {
                JOptionPane.showMessageDialog(target, Settings.getLanguageProperty("WARN_NO_FILE"),
                        Settings.getLanguageProperty("ERROR"),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(Settings.getLanguageProperty("EXPORT_HEADER"));

            // Set up the file filter for all image files
            FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
                    Settings.getLanguageProperty("EXPORT_FILTER"), "jpg", "jpeg", "png", "bmp");
            fileChooser.addChoosableFileFilter(imageFilter);
            fileChooser.setFileFilter(imageFilter);

            // Set default file name
            fileChooser.setSelectedFile(new File("image.png"));

            int userSelection = fileChooser.showSaveDialog(target);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    // Ensure the file has an extension; if not, add the default one (.png)
                    if (!fileToSave.getName().contains(".")) {
                        fileToSave = new File(fileToSave.toString() + ".png");
                    }

                    // Check if the file already exists and append a number if it does
                    fileToSave = getUniqueFile(fileToSave);

                    target.getImage().export(fileToSave.getAbsolutePath());

                    JOptionPane.showMessageDialog(target, Settings.getLanguageProperty("EXPORT_SUCCESS"),
                            Settings.getLanguageProperty("EXPORT"),
                            JOptionPane.INFORMATION_MESSAGE);

                    target.getImage().export(fileToSave.getAbsolutePath());

                    JOptionPane.showMessageDialog(target, Settings.getLanguageProperty("EXPORT_SUCCESS"),
                            Settings.getLanguageProperty("EXPORT"),
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(target,
                            Settings.getLanguageProperty("WAARN_EXPORT_GENERIC") + ex.getMessage(), "WARN_EXPORT",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
    * Ensures a unique file name by appending a number to the base name if the file already exists.
    * 
    * This method checks if the given file already exists. If it does, it generates a new file name by 
    * appending a number in parentheses to the base name before the file extension. The number increments 
    * until a unique file name is found.
    * 
    * @param file The initial file to check for uniqueness.
    * @return A unique file object with a name that does not conflict with existing files.
    */
    private File getUniqueFile(File file) {
        String filePath = file.getAbsolutePath();
        String name = filePath.substring(0, filePath.lastIndexOf('.'));
        String extension = filePath.substring(filePath.lastIndexOf('.'));

        int counter = 1;
        while (file.exists()) {
            file = new File(name + "(" + counter + ")" + extension);
            counter++;
        }
        return file;
    }

    /**
     * Action to change the language settings of the application.
     *
     * This class defines an action for changing the language settings of the
     * application.
     * It extends AbstractAction to provide the necessary functionality.
     * When triggered, it displays a dialog allowing the user to select from a list
     * of supported languages.
     * Upon selection, it updates the language setting in the application
     * configuration.
     * A message prompt is displayed to the user to restart the application and is
     * necessary for the language changes to take effect.
     * In case of any errors during the language change process, an appropriate
     * error message is shown.
     */

    public class ChangeLanguageAction extends AbstractAction {
        ChangeLanguageAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            String[] languages = { Settings.getLanguageProperty("LANGUAGE_ENGLISH"),
                    Settings.getLanguageProperty("LANGUAGE_FRENCH"), Settings.getLanguageProperty("LANGUAGE_SPANISH") };
            String selectedLanguage = (String) JOptionPane.showInputDialog(null,
                    Settings.getLanguageProperty("LANGUAGE_DIALOG"),
                    Settings.getLanguageProperty("LANGUAGE_HEADER"), JOptionPane.PLAIN_MESSAGE, null, languages,
                    languages[0]);

            if (selectedLanguage != null) {
                try {
                    Settings.setConfigProperty("language", selectedLanguage); // Save language setting
                    JOptionPane.showMessageDialog(null,
                            Settings.getLanguageProperty("LANGUAGE_CHANGED_DIALOGUE") + selectedLanguage,
                            Settings.getLanguageProperty("DIALOG_SUCCESS_HEADER"), JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            Settings.getLanguageProperty("WARN_LANGUAGE_FAIL") + ex.getMessage(),
                            Settings.getLanguageProperty("ERROR_HEADER"),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Action to quit the application.
     */
    public class FileExitAction extends AbstractAction {
        FileExitAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            exitAction();
        }
    }

    public static void exitAction() {
        // Get the current EditableImage instance
        EditableImage currentImage = ImageAction.getTarget().getImage();

        // Exit immediately if there is no image
        if (currentImage == null || !currentImage.hasImage()) {
            System.exit(0);
        }

        // Exit immediately if the image is saved
        if (currentImage.isSaved()) {
            System.exit(0);
        } else {
            // Prompt the user with a dialog box if the image is not saved
            int option = JOptionPane.showConfirmDialog(null,
                    Settings.getLanguageProperty("SAVE_AND_OPEN_DIALOG"),
                    Settings.getLanguageProperty("EXIT"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            switch (option) {
                case JOptionPane.YES_OPTION:
                    // Save and exit
                    try {
                        currentImage.save();
                        System.exit(0);
                    } catch (Exception e1) {
                        // Handle the save error exception
                        JOptionPane.showMessageDialog(null,
                                Settings.getLanguageProperty("SAVE_FAIL"),
                                Settings.getLanguageProperty("ERROR"),
                                JOptionPane.ERROR_MESSAGE);
                        e1.printStackTrace();
                    }
                    break;
                case JOptionPane.NO_OPTION:
                    // Exit without saving
                    System.exit(0);
                    break;
                case JOptionPane.CANCEL_OPTION:
                    // Do nothing (cancel)
                    break;
                default:
                    // Do nothing (default case for any unexpected option)
                    break;
            }
        }
    }
}
