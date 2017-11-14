package peril.ui.components;

import org.newdawn.slick.Image;

import peril.Point;

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
public abstract class Viewable {

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
		this.image = image;
		this.position = position;
	}

	/**
	 * Retrieves the {@link Image} of this {@link Viewable}.
	 * 
	 * @return {@link Image}
	 */
	public Image getImage() {
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

	/**
	 * Sets the {@link Point} position of this {@link Viewable}.
	 * 
	 * @param position
	 *            {@link Point}
	 */
	public void setPosition(Point position) {
		if (position == null) {
			throw new NullPointerException("Position cannot be null.");
		}
		this.position = position;
	}

	/**
	 * Retrieves the Width of the {@link Image} in this {@link Viewable}.
	 * 
	 * @return <code>int</code>
	 */
	public int getWidth() {

		// If there is a image in this Viewable then use that Width.
		if (hasImage()) {
			return image.getWidth();
		}
		throw new NullPointerException("The image is null.");
	}

	/**
	 * Retrieves the height of the {@link Image} in this {@link Viewable}.
	 * 
	 * @return <code>int</code>
	 */
	public int getHeight() {

		// If there is a image in this Viewable then use that height.
		if (hasImage()) {
			return image.getHeight();
		}
		throw new NullPointerException("The image is null.");
	}

}