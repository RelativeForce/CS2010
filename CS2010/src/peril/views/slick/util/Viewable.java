package peril.views.slick.util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Encapsulates the behaviour of an {@link Image} with a {@link Point} position
 * on screen.
 * 
 * @author Joshua_Eddy, Joseph_Rolli
 * 
 * @since 2018-02-17
 * @version 1.01.01
 * 
 * @see Image
 * @see Point
 */
public class Viewable {

	/**
	 * The {@link Image} that represents this {@link Viewable} on the screen.
	 */
	private Image image;

	/**
	 * The {@link Point} position of the {@link Viewable}.
	 */
	private Point position;

	/**
	 * Constructs a new {@link Viewable} with no image.
	 * 
	 * @param position
	 *            The {@link Point} position of the {@link Viewable}.
	 */
	public Viewable(Point position) {
		this.image = null;

		if (position == null) {
			throw new NullPointerException("Position cannot be null");
		}
		this.position = position;
	}

	/**
	 * Constructs a new {@link Viewable}.
	 * 
	 * @param image
	 *            The image that represents this {@link Viewable} on the screen.
	 * @param position
	 *            The {@link Point} position of the {@link Viewable}.
	 */
	public Viewable(Image image, Point position) {
		this(position);
		this.image = image;
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
	 * Sets the {@link Image} of the {@link Viewable} and {@link Image#destroy()}s
	 * the previous one.
	 */
	public void replaceImage(Image image) {

		// If the current image is not null, destroy it.
		if (this.image != null) {

			try {
				this.image.destroy();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

		this.image = image;
	}

	/**
	 * Sets the {@link Image} of the {@link Viewable} without
	 * {@link Image#destroy()}ing the previous one.
	 */
	public void swapImage(Image image) {
		this.image = image;
	}

	/**
	 * Scales this {@link Viewable} to a new width and height.
	 * 
	 * @param width
	 *            The new width.
	 * @param height
	 *            The new height.
	 */
	public void scale(int width, int height) {
		image = image.getScaledCopy(width, height);
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

	/**
	 * Retrieves whether this {@link Viewable} has an {@link Image} or not.
	 * 
	 * @return <code>boolean</code>
	 */
	public boolean hasImage() {
		return image != null;
	}

	/**
	 * Retrieves the {@link Image} of this {@link Viewable}.
	 * 
	 * @return The {@link Image} of this {@link Viewable}.
	 * 
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Retrieves the {@link Point} position of the {@link Viewable}.
	 * 
	 * @return The {@link Point} position of the {@link Viewable}.
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * When the {@link Viewable} is finalised destroy the {@link Image}.
	 */
	@Override
	protected final void finalize() throws Throwable {
		if (hasImage()) {
			image.destroy();
		}
	}
}
