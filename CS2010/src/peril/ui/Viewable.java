package peril.ui;

import java.awt.Color;

import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;

import peril.board.Clickable;
import peril.board.Region;

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
 * @see java.util.LinkedList
 * @see Java.util.List
 *
 */
public class Viewable extends Clickable {

	/**
	 * 
	 */
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
	 * Holds whether the current {@link Viewable} is an overlay or not/.
	 */
	private boolean isOverlay;

	/**
	 * Constructs a new {@link Viewable}.
	 */
	public Viewable() {
		this.image = null;
		this.isOverlay = false;
		this.setX(0);
		this.setY(0);
	}

	/**
	 * Sets the image of the {@link Viewable}, and it's coordinates
	 * relative to the origin.
	 */
	public void setImage(int x, int y, Image image) {
		this.image = image;
		this.isOverlay = false;
		this.setX(x);
		this.setY(y);
	}

	public Image getImage() {

		Region region = super.getRegion();

		if (image == null && region != null) {

			image = convertRegionToImage(region);

		}

		return image;
	}

	private Image convertRegionToImage(Region region) {

		// Holds the width and height of the region.
		int width = region.getWidth();
		int height = region.getHeight();

		// Set the coordinates of the visual rep to that of the region.
		setX(region.getX());
		setY(region.getY());

		// Holds the image of the region.
		ImageBuffer imagebuffer = new ImageBuffer(width, height);

		// Holds the boolean[][] object of the region.
		boolean[][] object = region.getObject();

		// Set the colour of the visual and get its rgb value.
		Color color = Color.YELLOW;
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();

		// Iterate through every pixel in the object[][] and if it is true set the
		// colour of the visual to the specified value.
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (object[x][y]) {
					imagebuffer.setRGBA(x, y, r, g, b, 180);
				}
			}
		}
		return imagebuffer.getImage();

	}

	public boolean hasImage() {
		return image != null;
	}

	/**
	 * Sets the {@link Viewable} to be an overlay.
	 */
	public void setAsOverlay() {
		isOverlay = true;
	}

	/**
	 * Returns whether or not this {@link Viewable} is an overlay.
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
