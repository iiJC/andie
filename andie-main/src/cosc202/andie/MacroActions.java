package cosc202.andie;

import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Abstract class for handling macro actions.
 * Provides methods to apply, record, and stop macros on images.
 * 
 * @author Jonathan Chan
 * @version 1.0
 */
public abstract class MacroActions {
  /**
  * Applies a macro to the current image.
  * 
  * @param e The ActionEvent that triggers this method.
  */
  public static void macroApplyAction(ActionEvent e) {
    EditableImage editableImage = ImageAction.getTarget().getImage();

    // Check if there is no image available
    if (!editableImage.hasImage()) {
      JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_NO_FILE"),
          Settings.getLanguageProperty("WARNING"),
          JOptionPane.WARNING_MESSAGE);
      return; // Exit method early if no image available
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileNameExtensionFilter("OPS files (*.ops)", "ops"));

    // Set the initial directory to src/resources/saved-macros
    File initialDir = new File("./src/saved-macros");
    if (initialDir.exists()) {
      fileChooser.setCurrentDirectory(initialDir);
    } else {
      System.out.println(Settings.getLanguageProperty("NO_DIRECTORY") + initialDir.getPath());
    }

    int result = fileChooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      String filePath = selectedFile.getAbsolutePath();

      // Call applyOpsFile method of the EditableImage instance
      editableImage.applyOpsFile(filePath);
      ImageAction.getTarget().repaint();
    }
  }

  /**
  * Starts or stops recording a macro.
  * If a macro is already being recorded, prompts the user to stop and discard it.
  * 
  * @param e The ActionEvent that triggers this method.
  */
  public static void recordMacroAction(ActionEvent e) {
    EditableImage image = ImageAction.getTarget().getImage();

    if (!image.hasImage()) {
      JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("MACRO_NO_IMG"));
      return;
    }

    if (!image.isRecording()) {
      image.setRecording(true);
      JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("MACRO_START"));
    } else {
      // Confirmation dialog
      int response = JOptionPane.showConfirmDialog(null, Settings.getLanguageProperty("MACRO_STOP_DIALOG"),
          "Stop Recording", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
      if (response == JOptionPane.YES_OPTION) {
        image.setRecording(false);
        JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("MACRO_FINISH"));
      }
    }
  }

  /**
  * Stops recording a macro and saves it to a file.
  * Prompts the user to specify the file location and name.
  * 
  * @param e The ActionEvent that triggers this method.
  */
  public static void stopMacroAction(ActionEvent e) {
    EditableImage editableImage = ImageAction.getTarget().getImage();

    // Check if there is no image or the image is not currently recording
    if (!editableImage.hasImage() || !editableImage.isRecording()) {
      JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("MACRO_NO_RECORDING"),
          Settings.getLanguageProperty("WARNING"), JOptionPane.WARNING_MESSAGE);
      return; // Exit early if conditions are not met
    }

    // Check if macro operations stack is empty
    if (editableImage.getMacroOps().isEmpty()) {
      editableImage.setRecording(false); // Stop recording if it was on
      JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("MACRO_NO_INPUT"),
          Settings.getLanguageProperty("WARNING"),
          JOptionPane.WARNING_MESSAGE);
      return; // Exit early as there are no operations to save
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileNameExtensionFilter("OPS files (*.ops)", "ops"));

    // Set the initial directory to src/resources/saved-macros
    File initialDir = new File("./src/saved-macros");
    if (initialDir.exists()) {
      fileChooser.setCurrentDirectory(initialDir);
    } else {
      System.out.println(Settings.getLanguageProperty("NO_DIRECTORY") + initialDir.getPath());
    }

    int result = fileChooser.showSaveDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      String filePath = selectedFile.getAbsolutePath();
      if (!filePath.toLowerCase().endsWith(".ops")) {
        filePath += ".ops"; // Append .ops extension if not present
      }

      if (editableImage.saveToOpsFile(filePath)) {
        JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("MACRO_RECORD_SAVE") + filePath,
            "Save Successful", JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("MACRO_RECORD_FAIL"),
            Settings.getLanguageProperty("ERROR"), JOptionPane.ERROR_MESSAGE);
      }
    }
    editableImage.setRecording(false); // Stop recording upon successful save
  }

}
