package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the Edit menu.
 * </p>
 * 
 * <p>
 * The Edit menu is very common across a wide range of applications.
 * There are a lot of operations that a user might expect to see here.
 * In the sample code there are Undo and Redo actions, but more may need to be
 * added.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @author Jonthan Chan (Additions for Copy and Paste, and language support)
 * @version 1.0
 */
public class EditActions {

    /** A list of actions for the Edit menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Edit menu actions.
     * </p>
     */
    public EditActions() {
        actions = new ArrayList<Action>();
        createActions();

    }

    /**
     * <p>
     * Create actions for undo and redo functionalities.
     * </p>
     * 
     * <p>
     * This method initializes actions for undo and redo functionalities.
     * It retrieves language-specific labels for 'Undo' and 'Redo' from the
     * settings.
     * Then, it creates instances of UndoAction and RedoAction, associating them
     * with corresponding
     * keyboard shortcuts (CTRL+Z for undo and CTRL+Y for redo by default).
     * These actions are added to the actions list to be loaded in the menu.
     * </p>
     */

    private void createActions() {
        String Undo = Settings.getLanguageProperty("Undo");
        String Redo = Settings.getLanguageProperty("Redo");
        String Copy = Settings.getLanguageProperty("Copy");
        String Paste = Settings.getLanguageProperty("Paste");

        actions.add(
                new UndoAction(Undo, null, Settings.getLanguageProperty("Undodesc"), Integer.valueOf(KeyEvent.VK_Z)));
        actions.add(
                new RedoAction(Redo, null, Settings.getLanguageProperty("Redodesc"), Integer.valueOf(KeyEvent.VK_Y)));
        actions.add(
                new CopyAction(Copy, null, Settings.getLanguageProperty("Copydesc"), Integer.valueOf(KeyEvent.VK_C)));
        actions.add(
                new PasteAction(Paste, null, Settings.getLanguageProperty("Pastedesc"),
                        Integer.valueOf(KeyEvent.VK_V)));
    }

    /**
     * <p>
     * Create a menu containing the list of Edit actions.
     * </p>
     * 
     * @return The edit menu UI element.
     */
    public JMenu createMenu() {
        JMenu editMenu = new JMenu(Settings.getLanguageProperty("Edit"));

        for (Action action : actions) {
            editMenu.add(new JMenuItem(action)).setAccelerator(
                    KeyStroke.getKeyStroke((Integer) action.getValue("MnemonicKey"), InputEvent.CTRL_DOWN_MASK));
        }

        return editMenu;
    }

    /**
     * <p>
     * Action to undo an {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#undo()
     */
    public class UndoAction extends ImageAction {

        /**
         * <p>
         * Create a new undo action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        UndoAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the undo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the UndoAction is triggered.
         * It undoes the most recently applied operation.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().undo();
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to redo an {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#redo()
     */
    public class RedoAction extends ImageAction {

        /**
         * <p>
         * Create a new redo action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        RedoAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the redo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the RedoAction is triggered.
         * It redoes the most recently undone operation.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().redo();
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to copy the current image to the clipboard.
     * </p>
     */
    public class CopyAction extends ImageAction {

        CopyAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            target.getImage().copyToClipboard();
        }
    }

    /**
     * <p>
     * Action to paste an image from the clipboard.
     * </p>
     */
    public class PasteAction extends ImageAction {

        PasteAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            target.getImage().openFromClipboard();
            target.repaint();
            target.getParent().revalidate();
        }
    }

}
