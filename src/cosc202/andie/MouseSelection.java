package cosc202.andie;

import java.awt.event.*;

/**
 * Manages the selection of a region on an ImagePanel using mouse events.
 * 
 * @author Ned Redmond
 */

public class MouseSelection implements MouseListener {
    private int pressx;
    private int pressy;
    private int releasex;
    private int releasey;
    private int X;
    private int Y;
    private int width;
    private int height;
    private ImagePanel target;
    private boolean selectionMade = false;

    // Constructor for MouseSelection class
    public MouseSelection(ImagePanel target) {
        this.target = target;
    }

    // Mouse event handlers
    /**
     * Handles the mouse released event.
     * Determines the selected region based on mouse press and release positions.
     * 
     * @param e The MouseEvent representing the mouse release event.
     */
    public void mouseReleased(MouseEvent e) {

        if (target.getImage() == null) {//no image selected
            width = 0;
            height = 0;
            X = 0;
            Y = 0;
            selectionMade = false;
            return;
        } else {

            releasex = e.getX();
            releasey = e.getY();

            X = Math.min(pressx, releasex);
            Y = Math.min(pressy, releasey);
            width = Math.abs(releasex - pressx);
            height = Math.abs(releasey - pressy);

            if (X < 0 || Y < 0 || X + width > target.getWidth() || Y + height > target.getHeight()) {
                selectionMade = false;
                width = 0;
                height = 0;
                X = 0;
                Y = 0;

            } else {
                selectionMade = true;

            }

        }
        target.repaint();

    }

    /**
     * Handles the mouse pressed event.
     * Records the initial mouse press position.
     * 
     * @param e The MouseEvent representing the mouse press event.
     */
    public void mousePressed(MouseEvent e) {
        pressx = e.getX();
        pressy = e.getY();

    }

    /**
     * Gets the X-coordinate of the selected region.
     * 
     * @return The X-coordinate of the selected region.
     */
    public int getX() {
        return X;
    }

    /**
     * Gets the Y-coordinate of the selected region.
     * 
     * @return The Y-coordinate of the selected region.
     */
    public int getY() {
        return Y;
    }

    /**
     * Gets the width of the selected region.
     * 
     * @return The width of the selected region.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the selected region.
     * 
     * @return The height of the selected region.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Checks if a selection has been made.
     * 
     * @return True if a selection has been made, false otherwise.
     */
    public boolean isSelectionMade() {
        return selectionMade;
    }

    /**
     * Handles the mouse clicked event.
     * Clears the current selection.
     * 
     * @param e The MouseEvent representing the mouse click event.
     */
    public void mouseClicked(MouseEvent e) {
        width = 0;
        height = 0;
        X = 0;
        Y = 0;

        selectionMade = false;
        target.repaint();

    }

    /**
     * Handles the mouse entered event.
     * 
     * @param e The MouseEvent representing the mouse enter event.
     */
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Handles the mouse exited event.
     * 
     * @param e The MouseEvent representing the mouse exit event.
     */
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Clears the current selection.
     */
    public void clearSelection() {
        width = 0;
        height = 0;
        X = 0;
        Y = 0;
        selectionMade = false;
        target.repaint();
    }

    /**
     * Gets the X-coordinate where the mouse was pressed.
     * 
     * @return The X-coordinate of the mouse press position.
     */
    public int getPressX() {
        return pressx;
    }

    /**
     * Gets the X-coordinate where the mouse was released.
     * 
     * @return The X-coordinate of the mouse release position.
     */
    public int getReleaseX() {
        return releasex;
    }

    /**
     * Gets the Y-coordinate where the mouse was pressed.
     * 
     * @return The Y-coordinate of the mouse press position.
     */
    public int getPressY() {
        return pressy;
    }

    /**
     * Gets the Y-coordinate where the mouse was released.
     * 
     * @return The Y-coordinate of the mouse release position.
     */
    public int getReleaseY() {
        return releasey;
    }
}
