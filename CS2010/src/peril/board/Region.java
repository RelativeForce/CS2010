package peril.board;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import peril.Point;

/**
 * Denotes the outer boundaries of the region of pixels given by a
 * {@link BufferedImage} and {@link Color}.
 * 
 * @author Joshua_Eddy
 *
 */
public class Region {

	/**
	 * Holds a X by 2 table that denotes the top and bottom boundaries of the shape
	 * with the {@link Color} specified in the
	 * {@link Region#Region(BufferedImage, Color)}. Where X is the width of the
	 * {@link BufferedImage} that was specified.
	 */
	private Integer[][] verticalBoundaries;

	/**
	 * Holds a X by 2 table that denotes the left and right boundaries of the shape
	 * with the {@link Color} specified in the
	 * {@link Region#Region(BufferedImage, Color)}. Where X is the height of the
	 * {@link BufferedImage} that was specified.
	 */
	private Integer[][] horizontalBoundaries;

	/**
	 * <code>boolean[][]</code> where if a pixel from the specified
	 * {@link BufferedImage} is the specified {@link Color} the it is assigned true,
	 * otherwise it is false.
	 */
	private boolean[][] object;

	/**
	 * Constructs a new <code>HitBox</code> object.
	 * 
	 * @param object
	 *            The <code>GraphicalObject</code> from which the
	 *            <code>HitBox</code> is derived.
	 * 
	 * @see environment.graphics.objects.GraphicalObject
	 */
	public Region(BufferedImage image, Color colour) {

		object = getRegion(image, colour);
		setVerticalBoundaries(image.getWidth(), image.getHeight());
		setHorizontalBoundaries(image.getWidth(), image.getHeight());

	}

	/**
	 * Constructs a combined {@link Region}. Only used by
	 * {@link Region#combine(List)}.
	 * 
	 * @param object
	 *            <code>boolean[][]</code> where if a pixel from the specified
	 *            {@link BufferedImage} is the specified {@link Color} the it is
	 *            assigned true, otherwise it is false.
	 * 
	 */
	private Region(boolean[][] object) {
		this.object = object;
	}

	/**
	 * Overlaps a specified {@link Region} onto this {@link Region}'s
	 * {@link Region#object}.
	 * 
	 * @param region
	 *            {@link Region} that is added to this {@link Region}.
	 * @param width
	 *            The width of both {@link Region}.
	 * @param height
	 *            The height ob both {@link Region}.
	 */
	private void addRegion(Region region, int width, int height) {

		// Iterate through every element in the region.
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				// If the current element in the parameter region is true but this regions's
				// object is false the set it to true.
				if (!this.object[x][y] || region.object[x][y]) {
					this.object[x][y] = true;
				}
			}
		}

	}

	/**
	 * Combines a {@link List} of {@link Region} into ONE {@link Region}.
	 * 
	 * @param list
	 *            {@link List} of {@link Region}s to be combined.
	 * @param width
	 *            <code>int</code> width of the space the {@link Region} is in.
	 * @param height
	 *            <code>int</code> height of the space the {@link Region} is in.
	 * @return {@link Region}
	 */
	public static Region combine(List<Region> list, int width, int height) {

		if (list == null || list.isEmpty()) {
			throw new IllegalArgumentException();
		}
		// Holds the region that will be the result.
		Region base = null;

		// Iterate through all the regions in the list.
		for (Region region : list) {

			// If the base region is null use the current regions boolean[][] to construct a
			// new region. Otherwise add the region to the base.
			if (base == null) {
				base = new Region(region.object);
			} else {
				base.addRegion(region, width, height);
			}
		}

		// If there is a base region, calculate the boundaries of the base region.
		if (base != null) {

			base.setVerticalBoundaries(width, height);
			base.setHorizontalBoundaries(width, height);

		}

		return base;
	}

	/**
	 * Retrieves a <code>boolean[][]</code> where if a pixel from the specified
	 * {@link BufferedImage} is the specified {@link Color} the it is assigned true,
	 * otherwise it is false.
	 * 
	 * @param image
	 *            {@link BufferedImage}
	 * @param color
	 *            {@link Color}
	 * @return <code>boolean[][]</code>
	 */
	private boolean[][] getRegion(BufferedImage image, Color color) {

		// Holds the array of booleans initially all elements are set to false
		boolean[][] object = new boolean[image.getWidth()][image.getHeight()];

		// Iterate through all pixel in the image and if a pixel is the same colour as
		// the one specified set that pixels position to true.
		for (int y = 0; y < image.getHeight(); y++) {

			for (int x = 0; x < image.getWidth(); x++) {

				if (color.getRGB() == image.getRGB(x, y)) {
					object[x][y] = true;
				}
			}
		}

		return object;
	}

	/**
	 * Assigns the outer horizontal boundaries of the region of pixels given by a
	 * {@link BufferedImage} and {@link Color}.
	 * 
	 * @param width
	 *            {@link BufferedImage#getWidth()}
	 * @param height
	 *            {@link BufferedImage#getHeight()}
	 */
	private void setHorizontalBoundaries(int width, int height) {

		/*
		 * IMPORTATNT CONCEPT: This methods iterates from the left edge of the image
		 * (assuming that x grows as you go to the right on the screen) to find the left
		 * most edge of the shape in the image. Then repeat the process from the right
		 * edge. The result is a heightx2 table with the top row being the x coordinates
		 * of the left edge of the regions shape and the bottom row being the x
		 * coordinates for the right edge of the region shape.
		 */

		// Initialise the horizontal array to be the same length as the height of the
		// image.
		horizontalBoundaries = new Integer[height][2];

		// Iterate through each row of the image.
		for (int rowNum = 0; rowNum < height; rowNum++) {

			// Iterate through each column on the current row.
			for (int x = 0; x < width; x++) {

				/*
				 * If the current column is not at the end of the row and the column to the left
				 * is false that is the boundary of this row OR if the current column is the end
				 * of the row and is true it is this rows boundary.
				 * 
				 */
				if (x > 0) {
					if (!object[x - 1][rowNum]) {
						horizontalBoundaries[rowNum][0] = x;
						break;
					}
				} else {
					if (object[x][rowNum]) {
						horizontalBoundaries[rowNum][0] = x;
						break;
					}
				}

			}

			// Iterate through each column on the current row.
			for (int x = width - 1; x >= 0; x--) {

				/*
				 * If the current column is not at the end of the row and the column to the
				 * right is false that is the boundary of this row OR if the current column is
				 * the end of the row and is true it is this rows boundary.
				 * 
				 */
				if (x < width - 1) {
					if (!object[x + 1][rowNum]) {
						horizontalBoundaries[rowNum][1] = x;
						break;
					}
				} else {
					if (object[x][rowNum]) {
						horizontalBoundaries[rowNum][1] = x;
						break;
					}
				}
			}
		}

	}

	/**
	 * Assigns the outer vertical boundaries of the region of pixels given by a
	 * {@link BufferedImage} and {@link Color}.
	 * 
	 * 
	 * 
	 * @param width
	 *            {@link BufferedImage#getWidth()}
	 * @param height
	 *            {@link BufferedImage#getHeight()}
	 */
	private void setVerticalBoundaries(int width, int height) {

		/*
		 * IMPORTANT CONCEPT: This methods iterates from the bottom edge of the image
		 * (assuming that y grows as you go down the screen) to find the lowest edge of
		 * the shape in the image. Then repeat the process from the top edge. The result
		 * is a widthx2 table with the top row being the y coordinates of the bottom
		 * edge of the regions shape and the bottom row being the y coordinates for the
		 * top edge of the region shape.
		 */

		// Initialise the vertical array to be the same length as the width of the
		// image.
		verticalBoundaries = new Integer[width][2];

		// Iterate through each row of the image.
		for (int colNum = 0; colNum < width; colNum++) {

			// Iterate through each row on the current column.
			for (int y = 0; y < height; y++) {

				if (y > 0) {
					if (!object[colNum][y + 1]) {
						verticalBoundaries[colNum][0] = y;
						break;
					}
				} else {
					if (object[colNum][y]) {
						verticalBoundaries[colNum][0] = y;
						break;
					}
				}
			}

			// Iterate through each row on the current column.
			for (int y = height - 1; y >= 0; y--) {

				if (y < height - 1) {
					if (!object[colNum][y + 1]) {
						verticalBoundaries[colNum][1] = y;
						break;
					}
				} else {
					if (object[colNum][y]) {
						verticalBoundaries[colNum][1] = y;
						break;
					}
				}
			}
		}

	}

	/**
	 * Retrieves whether or not the specified {@link Point} is inside this region.
	 * 
	 * @param point
	 *            {@link Point}
	 * @return <code>boolean</code>
	 */
	public boolean isInside(Point point) {

		// If the x and y are out side the bounds of the region return false;
		if (point.x < 0 && point.x > horizontalBoundaries.length && point.y < 0
				&& point.y > verticalBoundaries.length) {
			return false;
		}

		return ((point.x >= horizontalBoundaries[point.y][0] && point.x <= horizontalBoundaries[point.y][1])
				&& (point.y >= verticalBoundaries[point.x][0] && point.y <= verticalBoundaries[point.x][1]));
	}
}
