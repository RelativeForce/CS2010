package peril.ui;

import org.newdawn.slick.Image;

import peril.board.Clickable;

/**
 * Encapsulates the behaviour of an objects visual representation in the game.
 * VisualRepresentations:
 * <ul>
 * <li>Have and manage a 2D array of RGB values representing pixels.</li>
 * <li>Have a position.</li>
 * <li>Can be an overlay.</li>
 * <li>Can be displayed on-screen</li>
 * </ul>
 * 
 * @author Joshua_Eddy
 * 
 * @see java.util.LinkedList
 * @see Java.util.List
 *
 */

public class VisualRepresentation extends Clickable {

	private Image image;

	/**
	 * Int value representing horizontal position of the image relative to (0,0) the
	 * top left of the {@link board} image.
	 */
	private int x;

	/**
	 * Int value representing vertical position of the image relative to (0,0) the
	 * top left of the {@link board} image.
	 */
	private int y;

	/**
	 * Holds whether the current {@link VisualRepresentation} is an overlay or not/.
	 */
	private boolean isOverlay;

	/**
	 * Sets the image of the {@link VisualRepresentation}, and it's coordinates
	 * relative to the origin.
	 */
	public void setImage(int x, int y, Image image) {
		this.image = image;

		this.setX(x);
		this.setY(y);
	}

	public Image getImage() {
		return image;
	}

	/**
	 * Sets the {@link VisualRepresentation} to be an overlay.
	 */
	public void setAsOverlay() {
		isOverlay = true;
	}

	/**
	 * Returns whether or not this {@link VisualRepresentation} is an overlay.
	 */
	public boolean isOverlay() {
		return isOverlay;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
