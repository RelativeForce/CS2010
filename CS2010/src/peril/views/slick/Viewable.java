package peril.views.slick;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Encapsulates the behaviour of an {@link Image} in the game.
 * 
 * @author Joshua_Eddy, Joseph_Rolli
 * 
 *
 */
public class Viewable {

	/**
	 * The image that represents this {@link Viewable} on the screen.
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
		this.image = image;
		this.position = position;
	}

	/**
	 * Sets the {@link Image} of the {@link Viewable} and {@link Image#destroy()}s
	 * the previous one.
	 */
	public void replaceImage(Image image) {

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

	public void scale(int width, int height) {
		image = image.getScaledCopy(width, height);
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

	@Override
	protected void finalize() throws Throwable {
		if (hasImage())
			image.destroy();
	}
}
