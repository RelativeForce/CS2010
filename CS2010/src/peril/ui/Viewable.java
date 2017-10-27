package peril.ui;

import org.newdawn.slick.Image;

import peril.Point;
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
 * @author Joshua_Eddy, Joseph_Rolli
 * 
 *
 */
public abstract class Viewable extends Clickable {

	/**
	 * The image that represents this {@link Viewable} on the {@link UserInterface}.
	 */
	private Image image;

	/**
	 * The {@link Point} position of the {@link Viewable}.
	 */
	private Point position;

	/**
	 * Constructs a new {@link Viewable}.
	 */
	public Viewable() {
		this.image = null;
		this.position = new Point(0, 0);
	}

	/**
	 * Sets the image of the {@link Viewable}, and it's coordinates relative to the
	 * origin.
	 */
	public void setImage(Point position, Image image) {

		// Check position
		if (position == null) {
			throw new NullPointerException("Point cannot be null.");
		} else if (image == null) {
			throw new NullPointerException("Image cannot be null.");
		}

		this.image = image;
		this.position = position;
	}

	/**
	 * Retrieves the {@link Image} of this {@link Viewable}.
	 * 
	 * @return {@link Image}
	 */
	public Image getImage() {

		// If this viewable has a region but does not have a image.
		if (!hasImage() && hasRegion()) {
			image = getRegion().convert();
		}

		return image;
	}

	/**
	 * Retrieves whether this {@link Viewable} has an {@link Image} or not.
	 * 
	 * @return <code>boolean</code>
	 */
	public boolean hasImage() {
		return image != null;
	}

	/**
	 * Retrieves the {@link Point} position of the {@link Viewable}.
	 * 
	 * @return
	 */
	public Point getPosition() {
		return position;
	}

}
