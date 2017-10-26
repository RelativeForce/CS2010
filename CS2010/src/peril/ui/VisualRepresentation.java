package peril.ui;
import peril.Player;
import peril.board.Army;
import peril.board.Clickable;
import peril.board.Country;

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

public abstract class VisualRepresentation extends Clickable{
	
	/**
	 * 2D array holding RGB values, to be displayed.
	 */
	private Integer[][] image;
	
	/**
	 * Int value representing horizontal position of the image relative to (0,0) the top left of the {@link board} image.
	 */
	private int x;
	
	/**
	 * Int value representing vertical position of the image relative to (0,0) the top left of the {@link board} image.
	 */
	private int y;
	
	/**
	 * Holds whether the current {@link VisualRepresentation} is an overlay or not/.
	 */
	private boolean isOverlay;
	
	/**
	 * Sets the image of the {@link VisualRepresentation}, and it's coordinates relative to the origin.
	 */
	public void setImage(int x, int y, Integer[] [] image) {
		this.image = image;
		this.x = x;
		this.y = y;
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
}

