package cosc202.andie;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

/**
 * <p>
 * A toolbar to quickly access common actions so the user can efficiently
 * perform operations.
 * </p>
 * 
 * @author Jonathan Chan
 * @version 1.0
 */
public class Toolbar extends JPanel {

  /**
   * <p>
   * Create a toolbar that contains essential/common actions.
   * </p>
   */
  public Toolbar() {
    JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(false);

    setLayout(new BorderLayout());
    add(toolbar, BorderLayout.WEST);

    String directory = "/resources/icons/";

    try {
      // Load in icons
      ImageIcon openIcon = new ImageIcon(Toolbar.class.getResource(directory + "open_icon.png"));
      ImageIcon saveIcon = new ImageIcon(Toolbar.class.getResource(directory + "save_icon.png"));
      ImageIcon saveAsIcon = new ImageIcon(Toolbar.class.getResource(directory + "saveas_icon.png"));
      ImageIcon exportIcon = new ImageIcon(Toolbar.class.getResource(directory + "export_icon.png"));
      ImageIcon undoIcon = new ImageIcon(Toolbar.class.getResource(directory + "undo_icon.png"));
      ImageIcon redoIcon = new ImageIcon(Toolbar.class.getResource(directory + "redo_icon.png"));
      ImageIcon zoomInIcon = new ImageIcon(Toolbar.class.getResource(directory + "zoom_in_icon.png"));
      ImageIcon zoomOutIcon = new ImageIcon(Toolbar.class.getResource(directory + "zoom_out_icon.png"));
      ImageIcon macroApplyIcon = new ImageIcon(Toolbar.class.getResource(directory + "macro_apply_icon.png"));
      ImageIcon macroRecordIcon = new ImageIcon(Toolbar.class.getResource(directory + "macro_record_icon.png"));
      ImageIcon macroStopIcon = new ImageIcon(Toolbar.class.getResource(directory + "macro_stop_icon.png"));

      // Creating buttons and attaching their corresponding actions
      toolbar.add(new JButton(new OpenAction(openIcon)));
      toolbar.add(new JButton(new SaveAction(saveIcon)));
      toolbar.add(new JButton(new SaveAsAction(saveAsIcon)));
      toolbar.add(new JButton(new ExportAction(exportIcon)));
      toolbar.add(new JButton(new UndoAction(undoIcon)));
      toolbar.add(new JButton(new RedoAction(redoIcon)));
      toolbar.add(new JButton(new ZoomInAction(zoomInIcon)));
      toolbar.add(new JButton(new ZoomOutAction(zoomOutIcon)));
      toolbar.add(new JButton(new MacroApplyAction(macroApplyIcon)));
      toolbar.add(new JButton(new MacroRecordAction(macroRecordIcon)));
      toolbar.add(new JButton(new MacroStopAction(macroStopIcon)));

    } catch (Exception e) {
      System.err.println("Error loading toolbar icons: " + e.getMessage());
      // In the case of an error, don't build the toolbar
    }
  }

  private class OpenAction extends AbstractAction {
    public OpenAction(ImageIcon openIcon) {
      super("", openIcon);
      putValue(Action.SHORT_DESCRIPTION, Settings.getLanguageProperty("Open"));
      Image img = openIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
      ImageIcon resizedIcon = new ImageIcon(img);
      putValue(Action.SMALL_ICON, resizedIcon);
    }

    public void actionPerformed(ActionEvent e) {
      FileActions.FileOpenAction openAction = new FileActions().new FileOpenAction(Settings.getLanguageProperty("Open"),
          null,
          Settings.getLanguageProperty("Open_desc"), Integer.valueOf(KeyEvent.VK_O));
      openAction.actionPerformed(e);
    }
  }

  /**
   * Action that saves the current image.
   * 
   * @see EditableImage#save()
   */
  private class SaveAction extends AbstractAction {

    public SaveAction(ImageIcon icon) {
      super("", icon);
      putValue(Action.SHORT_DESCRIPTION, Settings.getLanguageProperty("Save"));
      // Resize the icon to a more appropriate size
      Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
      ImageIcon resizedIcon = new ImageIcon(img);
      putValue(Action.SMALL_ICON, resizedIcon);
    }

    public void actionPerformed(ActionEvent e) {
      FileActions.FileSaveAction saveAction = new FileActions().new FileSaveAction(Settings.getLanguageProperty("Save"),
          null,
          Settings.getLanguageProperty("Save_desc"), Integer.valueOf(KeyEvent.VK_S));
      saveAction.actionPerformed(e);
    }

  }

  private class SaveAsAction extends AbstractAction {

    public SaveAsAction(ImageIcon icon) {
      super("", icon);
      putValue(Action.SHORT_DESCRIPTION, Settings.getLanguageProperty("SaveAs"));
      Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
      ImageIcon resizedIcon = new ImageIcon(img);
      putValue(Action.SMALL_ICON, resizedIcon);
    }

    public void actionPerformed(ActionEvent e) {
      FileActions.FileSaveAsAction saveAsAction = new FileActions().new FileSaveAsAction(
          Settings.getLanguageProperty("SaveAs"),
          null,
          Settings.getLanguageProperty("Saveasdesc"), Integer.valueOf(KeyEvent.VK_A));
      saveAsAction.actionPerformed(e);
    }
  }

  private class ExportAction extends AbstractAction {
    public ExportAction(ImageIcon icon) {
      super("", icon);
      putValue(Action.SHORT_DESCRIPTION, Settings.getLanguageProperty("Export"));
      Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
      ImageIcon resizedIcon = new ImageIcon(img);
      putValue(Action.SMALL_ICON, resizedIcon);
    }

    public void actionPerformed(ActionEvent e) {
      FileActions.FileExportAction exportAction = new FileActions().new FileExportAction(
          Settings.getLanguageProperty("Export"),
          null,
          Settings.getLanguageProperty("Export_desc"), Integer.valueOf(KeyEvent.VK_E));
      exportAction.actionPerformed(e);
    }
  }

  private class UndoAction extends AbstractAction {
    public UndoAction(ImageIcon icon) {
      super("", icon);
      putValue(Action.SHORT_DESCRIPTION, Settings.getLanguageProperty("Undo"));
      Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
      ImageIcon resizedIcon = new ImageIcon(img);
      putValue(Action.SMALL_ICON, resizedIcon);
    }

    public void actionPerformed(ActionEvent e) {
      EditActions.UndoAction undoAction = new EditActions().new UndoAction(
          Settings.getLanguageProperty("Undo"),
          null,
          Settings.getLanguageProperty("Undodesc"), Integer.valueOf(KeyEvent.VK_Z));
      undoAction.actionPerformed(e);
    }
  }

  private class RedoAction extends AbstractAction {
    public RedoAction(ImageIcon icon) {
      super("", icon);
      putValue(Action.SHORT_DESCRIPTION, Settings.getLanguageProperty("Redo"));
      Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
      ImageIcon resizedIcon = new ImageIcon(img);
      putValue(Action.SMALL_ICON, resizedIcon);
    }

    public void actionPerformed(ActionEvent e) {
      EditActions.RedoAction redoAction = new EditActions().new RedoAction(
          Settings.getLanguageProperty("Redo"),
          null,
          Settings.getLanguageProperty("Redodesc"), Integer.valueOf(KeyEvent.VK_Y));
      redoAction.actionPerformed(e);
    }
  }

  private class ZoomInAction extends AbstractAction {
    public ZoomInAction(ImageIcon icon) {
      super("", icon);
      putValue(Action.SHORT_DESCRIPTION, Settings.getLanguageProperty("ZoomIn"));
      Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
      ImageIcon resizedIcon = new ImageIcon(img);
      putValue(Action.SMALL_ICON, resizedIcon);
    }

    public void actionPerformed(ActionEvent e) {
      ViewActions.ZoomInAction zoomInAction = new ViewActions().new ZoomInAction(
          Settings.getLanguageProperty("ZoomIn"),
          null,
          Settings.getLanguageProperty("ZoomIndesc"), Integer.valueOf(KeyEvent.VK_I));
      zoomInAction.actionPerformed(e);

    }
  }

  private class ZoomOutAction extends AbstractAction {
    public ZoomOutAction(ImageIcon icon) {
      super("", icon);
      putValue(Action.SHORT_DESCRIPTION, Settings.getLanguageProperty("ZoomOut"));
      Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
      ImageIcon resizedIcon = new ImageIcon(img);
      putValue(Action.SMALL_ICON, resizedIcon);
    }

    public void actionPerformed(ActionEvent e) {
      ViewActions.ZoomOutAction zoomOutAction = new ViewActions().new ZoomOutAction(
          Settings.getLanguageProperty("ZoomOut"),
          null,
          Settings.getLanguageProperty("ZoomOutdesc"), Integer.valueOf(KeyEvent.VK_O));
      zoomOutAction.actionPerformed(e);
    }
  }

  private class MacroApplyAction extends AbstractAction {
    public MacroApplyAction(ImageIcon icon) {
      super("", icon);
      putValue(Action.SHORT_DESCRIPTION,
          Settings.getLanguageProperty("Macro_Apply"));
      Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
      ImageIcon resizedIcon = new ImageIcon(img);
      putValue(Action.SMALL_ICON, resizedIcon);
    }

    public void actionPerformed(ActionEvent e) {
      MacroActions.macroApplyAction(e);
    }
  }

  private class MacroRecordAction extends AbstractAction {
    public MacroRecordAction(ImageIcon icon) {
      super("", icon);
      putValue(Action.SHORT_DESCRIPTION,
          Settings.getLanguageProperty("Macro_Record"));
      Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
      ImageIcon resizedIcon = new ImageIcon(img);
      putValue(Action.SMALL_ICON, resizedIcon);
    }

    public void actionPerformed(ActionEvent e) {
      MacroActions.recordMacroAction(e);
    }
  }

  private class MacroStopAction extends AbstractAction {
    public MacroStopAction(ImageIcon icon) {
      super("", icon);
      putValue(Action.SHORT_DESCRIPTION,
          Settings.getLanguageProperty("Macro_Stop"));
      Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
      ImageIcon resizedIcon = new ImageIcon(img);
      putValue(Action.SMALL_ICON, resizedIcon);
    }

    public void actionPerformed(ActionEvent e) {
      MacroActions.stopMacroAction(e);
    }
  }

}
