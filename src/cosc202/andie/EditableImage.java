package cosc202.andie;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.JOptionPane;
import java.awt.datatransfer.*;

/**
 * <p>
 * An image with a set of operations applied to it.
 * </p>
 * 
 * <p>
 * The EditableImage represents an image with a series of operations applied to
 * it.
 * It is fairly core to the ANDIE program, being the central data structure.
 * The operations are applied to a copy of the original image so that they can
 * be undone.
 * THis is what is meant by "A Non-Destructive Image Editor" - you can always
 * undo back to the original image.
 * </p>
 * 
 * <p>
 * Internally the EditableImage has two {@link BufferedImage}s - the original
 * image
 * and the result of applying the current set of operations to it.
 * The operations themselves are stored on a {@link Stack}, with a second
 * {@link Stack}
 * being used to allow undone operations to be redone.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @author Jonathan Chan (Additions for Export, Saving, Clipboard, Macros)
 * @version 1.0
 */
class EditableImage {

    /** The original image. This should never be altered by ANDIE. */
    private BufferedImage original;
    /**
     * The current image, the result of applying {@link ops} to {@link original}.
     */
    private BufferedImage current;
    /** The sequence of operations currently applied to the image. */
    private Stack<ImageOperation> ops;
    /** A memory of 'undone' operations to support 'redo'. */
    private Stack<ImageOperation> redoOps;
    /** The file where the original image is stored/ */
    private String imageFilename;
    /** The file where the operation sequence is stored. */
    private String opsFilename;

    private Stack<ImageOperation> macroOps;
    private boolean recording = false;
    private boolean isSaved = false;

    /**
     * <p>
     * Create a new EditableImage.
     * </p>
     * 
     * <p>
     * A new EditableImage has no image (it is a null reference), and an empty stack
     * of operations.
     * </p>
     */
    public EditableImage() {
        original = null;
        current = null;
        ops = new Stack<ImageOperation>();
        redoOps = new Stack<ImageOperation>();
        imageFilename = null;
        opsFilename = null;
    }

    /**
     * <p>
     * Check if there is an image loaded.
     * </p>
     * 
     * @return True if there is an image, false otherwise.
     */
    public boolean hasImage() {
        return current != null;
    }

    public boolean isSaved() {
        return isSaved;
    }

    /**
     * <p>
     * Make a 'deep' copy of a BufferedImage.
     * </p>
     * 
     * <p>
     * Object instances in Java are accessed via references, which means that
     * assignment does
     * not copy an object, it merely makes another reference to the original.
     * In order to make an independent copy, the {@code clone()} method is generally
     * used.
     * {@link BufferedImage} does not implement {@link Cloneable} interface, and so
     * the
     * {@code clone()} method is not accessible.
     * </p>
     * 
     * <p>
     * This method makes a cloned copy of a BufferedImage.
     * This requires knowledge of some details about the internals of the
     * BufferedImage,
     * but essentially comes down to making a new BufferedImage made up of copies of
     * the internal parts of the input.
     * </p>
     * 
     * <p>
     * This code is taken from StackOverflow:
     * <a href=
     * "https://stackoverflow.com/a/3514297">https://stackoverflow.com/a/3514297</a>
     * in response to
     * <a href=
     * "https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage">https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage</a>.
     * Code by Klark used under the CC BY-SA 2.5 license.
     * </p>
     * 
     * <p>
     * This method (only) is released under
     * <a href="https://creativecommons.org/licenses/by-sa/2.5/">CC BY-SA 2.5</a>
     * </p>
     * 
     * @param bi The BufferedImage to copy.
     * @return A deep copy of the input.
     */
    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * <p>
     * Open an image from a file.
     * </p>
     * 
     * <p>
     * Opens an image from the specified file.
     * Also tries to open a set of operations from the file with <code>.ops</code>
     * added.
     * So if you open <code>some/path/to/image.png</code>, this method will also try
     * to
     * read the operations from <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param filePath The file to open the image from.
     * @throws Exception If something goes wrong.
     */
    public void open(String filePath) throws Exception {
        imageFilename = filePath;
        opsFilename = imageFilename + ".ops";
        File imageFile = new File(imageFilename);
        original = ImageIO.read(imageFile);
        current = deepCopy(original);

        try {
            FileInputStream fileIn = new FileInputStream(this.opsFilename);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Silence the Java compiler warning about type casting.
            // Understanding the cause of the warning is way beyond
            // the scope of COSC202, but if you're interested, it has
            // to do with "type erasure" in Java: the compiler cannot
            // produce code that fails at this point in all cases in
            // which there is actually a type mismatch for one of the
            // elements within the Stack, i.e., a non-ImageOperation.
            @SuppressWarnings("unchecked")
            Stack<ImageOperation> opsFromFile = (Stack<ImageOperation>) objIn.readObject();
            ops = opsFromFile;
            redoOps.clear();
            objIn.close();
            fileIn.close();
        } catch (Exception ex) {
            // Could be no file or something else. Carry on for now.
            ops.clear();
            redoOps.clear();
        }
        this.refresh();
    }

    /**
     * <p>
     * Save an image to file.
     * </p>
     * 
     * <p>
     * Saves an image to the file it was opened from, or the most recent file saved
     * as.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also
     * save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @throws Exception If something goes wrong.
     */
    public void save() throws Exception {
        if (this.opsFilename == null) {
            this.opsFilename = this.imageFilename + ".ops";
        }
        // Write image file based on file extension
        String extension = imageFilename.substring(1 + imageFilename.lastIndexOf(".")).toLowerCase();
        ImageIO.write(original, extension, new File(imageFilename));
        // Write operations file
        FileOutputStream fileOut = new FileOutputStream(this.opsFilename);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(this.ops);
        objOut.close();
        fileOut.close();
        isSaved = true;
    }

    /**
     * <p>
     * Save an image to a specified file.
     * </p>
     * 
     * <p>
     * Saves an image to the file provided as a parameter.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also
     * save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param imageFilename The file location to save the image to.
     * @throws Exception If something goes wrong.
     */
    public void saveAs(String imageFilename) throws Exception {
        this.imageFilename = imageFilename;
        this.opsFilename = imageFilename + ".ops";
        save();
    }

    /**
     * <p>
     * Apply an {@link ImageOperation} to this image.
     * </p>
     * 
     * @param op The operation to apply.
     */
    public void apply(ImageOperation op) {
        current = op.apply(current);
        ops.add(op); // Always add to the main operations stack
        if (isRecording()) {
            macroOps.push(op); // Also add to macroOps if recording
        }
        isSaved = false;
    }

    /**
     * <p>
     * Undo the last {@link ImageOperation} applied to the image.
     * </p>
     */
    public void undo() {
        if (!hasImage()) {
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_NO_FILE"),
                    Settings.getLanguageProperty("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (!ops.isEmpty()) {
                redoOps.push(ops.pop()); // Move the last operation to redo stack
                if (isRecording() && !macroOps.isEmpty()) {
                    macroOps.pop(); // If recording macros, remove the last macro operation
                }
                refresh(); // Refresh the image after undoing the operation
            } else {
                JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("NO_UNDO"),
                        Settings.getLanguageProperty("WARNING"),
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (EmptyStackException ex) {
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("NO_UNDO"),
                    Settings.getLanguageProperty("WARNING"), JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * <p>
     * Reapply the most recently {@link undo}ne {@link ImageOperation} to the image.
     * </p>
     */
    public void redo() {
        if (!hasImage()) {
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_NO_FILE"),
                    Settings.getLanguageProperty("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            apply(redoOps.pop()); // Apply the operation from the redo stack
            isSaved = false;
        } catch (EmptyStackException ex) {
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("NO_REDO"),
                    Settings.getLanguageProperty("WARNING"),
                    JOptionPane.WARNING_MESSAGE);
        }

    }

    /**
     * <p>
     * Get the current image after the operations have been applied.
     * </p>
     * 
     * @return The result of applying all of the current operations to the
     *         {@link original} image.
     */
    public BufferedImage getCurrentImage() {
        return current;
    }

    /**
     * <p>
     * Reapply the current list of operations to the original.
     * </p>
     * 
     * <p>
     * While the latest version of the image is stored in {@link current}, this
     * method makes a fresh copy of the original and applies the operations to it in
     * sequence.
     * This is useful when undoing changes to the image, or in any other case where
     * {@link current}
     * cannot be easily incrementally updated.
     * </p>
     */
    private void refresh() {
        current = deepCopy(original);
        for (ImageOperation op : ops) {
            current = op.apply(current);
        }
    }

    /**
     * <p>
     * Export the current image to a specified file path.
     * </p>
     * 
     * <p>
     * This method exports the current image to the specified file path.
     * It first checks if there is an image loaded; if not, it throws an
     * IllegalStateException.
     * Then, it determines the file extension from the provided file path and
     * ensures it is a supported type;
     * otherwise, it throws an IllegalArgumentException.
     * Next, it retrieves the current image (after applying all operations) using
     * {@link getCurrentImage}.
     * Finally, it writes the image to the specified file using ImageIO, and prints
     * a success message.
     * </p>
     * 
     * @param filePath The file path to export the image to.
     * @throws Exception If there is no image loaded, or the file type is
     *                   unsupported, or an error occurs during export.
     */
    public void export(String filePath) throws Exception {
        if (!hasImage()) {
            throw new IllegalStateException(Settings.getLanguageProperty("WARN_EXPORT_NO_IMG"));
        }

        // Determine the file extension from the provided file path
        String extension = getFileExtension(filePath).toLowerCase();

        // Check if the file extension is supported
        if (!isSupportedFileType(extension)) {
            throw new IllegalArgumentException(Settings.getLanguageProperty("WARN_EXPORT_INVALID_FILE") + extension);
        }

        // Get the current image (after applying all operations)
        BufferedImage imageToExport = getCurrentImage();

        // Write the image to file
        ImageIO.write(imageToExport, extension, new File(filePath));
        System.out.println(Settings.getLanguageProperty("EXPORT_SUCCESS_PATH") + filePath);
    }

    /**
     * Utility method to extract the file extension from a file path.
     * 
     * @param filePath The file path to extract the extension from.
     * @return The file extension.
     */
    private String getFileExtension(String filePath) {
        int lastIndex = filePath.lastIndexOf('.');
        if (lastIndex == -1) {
            return ""; // No file extension found
        }
        return filePath.substring(lastIndex + 1);
    }

    /**
     * <p>
     * Check if the given file extension is supported for export.
     * </p>
     * 
     * <p>
     * This method checks if the given file extension is supported for export.
     * Supported file types include PNG, JPG, JPEG, and BMP.
     * </p>
     * 
     * @param extension The file extension to check.
     * @return True if the extension is supported, false otherwise.
     */
    private boolean isSupportedFileType(String extension) {
        return extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg")
                || extension.equals("bmp");
    }

    public void applyOpsFile(String opsFilePath) {
        Stack<ImageOperation> opsFromFile;

        // Use try-with-resources for automatic resource management
        try (FileInputStream fileIn = new FileInputStream(opsFilePath);
                ObjectInputStream objIn = new ObjectInputStream(fileIn)) {

            // Attempt to read the object from the file
            opsFromFile = (Stack<ImageOperation>) objIn.readObject();

            // If no operations were loaded, notify the user and return
            if (opsFromFile.isEmpty()) {
                JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("NO_MACROS"),
                        Settings.getLanguageProperty("WARNING"),
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Apply all operations in the stack
            for (ImageOperation op : opsFromFile) {
                apply(op);
            }

            // Refresh UI or re-render the image
            this.refresh();

        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_NO_FILE_FOUND"),
                    Settings.getLanguageProperty("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Failed to read the file. Check the file format and permissions.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null,
                    Settings.getLanguageProperty("WARN_INVALID_OPS_FILE"), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_GENERIC"),
                    Settings.getLanguageProperty("ERROR"), JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Save the current {@code macroOps} to a file.
     * 
     * @param macroOpsFileName The file location to save the ops file to.
     * @return Whether the operation was successful
     */
    public boolean saveToOpsFile(String macroOpsFileName) {
        if (macroOps == null || macroOps.isEmpty()) {
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("NO_SAVE"),
                    Settings.getLanguageProperty("WARNING"),
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try (FileOutputStream fileOut = new FileOutputStream(macroOpsFileName);
                ObjectOutputStream objOut = new ObjectOutputStream(fileOut)) {
            objOut.writeObject(macroOps);
            macroOps.clear(); // Clearing after successful write
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_NO_FILE_FOUND"),
                    Settings.getLanguageProperty("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("IO_ERROR"),
                    Settings.getLanguageProperty("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("UNEXPECTED_MACRO"),
                    Settings.getLanguageProperty("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("SAVE_MACRO_SUCCESS"),
                Settings.getLanguageProperty("INFORMATION"),
                JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
        if (recording) {
            macroOps = new Stack<ImageOperation>(); // Initialize macroOps stack when recording starts
        } else {
            macroOps = null; // Clear macroOps stack when recording stops
        }
    }

    public boolean isRecording() {
        return recording;
    }

    public Stack<ImageOperation> getMacroOps() {
        return macroOps;
    }

    /**
     * <p>
     * Copy the current image to the system clipboard.
     * </p>
     */
    public void copyToClipboard() {
        if (!hasImage()) {
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("WARN_NO_FILE"),
                    Settings.getLanguageProperty("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            TransferableImage trans = new TransferableImage(current);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(trans, null);

            // Show success message
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("CLIPBOARD_SUCCESS"),
                    Settings.getLanguageProperty("DIALOG_SUCCESS_HEADER"),
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("CLIPBOARD_FAIL"),
                    Settings.getLanguageProperty("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * <p>
     * A private inner class that allows data to be transferred between
     * applications. In this case, it is being used to transfer data to the clipboard.
     * </p>
     * 
     * <p>Code adapted from here:
     * <p>https://stackoverflow.com/questions/4552045/copy-bufferedimage-to-clipboard</p>
     */
    private static class TransferableImage implements Transferable {
        private Image image;

        public TransferableImage(Image image) {
            this.image = image;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(DataFlavor.imageFlavor) && image != null) {
                return image;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.imageFlavor };
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(DataFlavor.imageFlavor);
        }

    }

    /**
     * <p>
     * Open a new image from the clipboard.
     * </p>
     */
    public void openFromClipboard() {
        if (current != null) {
            int response = JOptionPane.showConfirmDialog(null,
                    Settings.getLanguageProperty("IMAGE_OVERRIDE"),
                    Settings.getLanguageProperty("CONFIRM"),
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (response != JOptionPane.YES_OPTION) {
                return;
            }
        }

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
            try {
                Image img = (Image) clipboard.getData(DataFlavor.imageFlavor);
                if (img instanceof BufferedImage) {
                    BufferedImage bufferedImage = (BufferedImage) img;
                    setDataFields(bufferedImage, "clipboard", new Stack<>());
                } else {
                    BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null),
                            BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = bufferedImage.createGraphics();
                    g2d.drawImage(img, 0, 0, null);
                    g2d.dispose();
                    setDataFields(bufferedImage, "clipboard", new Stack<>());
                    isSaved = false;
                }
                refresh();
            } catch (UnsupportedFlavorException | IOException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, Settings.getLanguageProperty("CLIPBOARD_NO_IMG"),
                    Settings.getLanguageProperty("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setDataFields(BufferedImage img, String source, Stack<ImageOperation> operations) {
        this.original = img;
        this.current = deepCopy(img);
        this.imageFilename = source;
        this.opsFilename = source + ".ops";
        this.ops = operations;
        this.redoOps.clear();
    }

    private int transparencyLevel = 100; // Default transparency level (100% opaque)

    public int getTransparencyLevel() { //gets transparencylevel from image
        return transparencyLevel;
    }

    /**
     * sets the transparency from image 
     * 
     * @param transparencyLevel
     */

    public void setTransparencyLevel(int transparencyLevel) {
        this.transparencyLevel = transparencyLevel;

    }

    /**
     * Applies a transparency operation to the current image.
     * Updates the current image by applying the provided ImageOperation, 
     * and stores the operation in the operations history.
     * If the operation is an instance of TransparencyImage, updates the transparency level.
     *
     * @param op The ImageOperation to apply to the current image.
     */
    public void applyTransparency(ImageOperation op) {
        current = op.apply(current);
        ops.add(op);
        if (op instanceof TransparencyImage) {
            transparencyLevel = ((TransparencyImage) op).getTransparencyLevel();
        }
    }

}
