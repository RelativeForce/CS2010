package peril.board;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import peril.Point;

/**
 * Denotes the region of pixels given by a {@link BufferedImage} and a
 * {@link Color}.
 * 
 * <br>
 * Member class: {@link Reducer}
 * 
 * @author Joshua_Eddy
 *
 */
public final class Region {

	/**
	 * <code>boolean[][]</code> where if a pixel from the specified
	 * {@link BufferedImage} is the specified {@link Color} the it is assigned true,
	 * otherwise it is false.
	 */
	private boolean[][] object;

	/**
	 * The <code>int</code> width of this {@link Region}.
	 */
	private int width;

	/**
	 * The <code>int</code> height of this {@link Region}.
	 */
	private int height;

	/**
	 * The <code>int</code> x vector from the (0,0) of the image this {@link Region}
	 * is a part of to (0,0) of this {@link Region}.
	 */
	private int x;

	/**
	 * The <code>int</code> y vector from the (0,0) of the image this {@link Region}
	 * is a part of to (0,0) of this {@link Region}.
	 */
	private int y;

	/**
	 * Constructs a new <code>HitBox</code> object.
	 * 
	 * @param object
	 *            The <code>GraphicalObject</code> from which the
	 *            <code>HitBox</code> is derived.
	 * 
	 * @see environment.graphics.objects.GraphicalObject
	 */
	public Region(Image image, Color colour) {
		constructor(getRegion(image, colour), image.getWidth(), image.getHeight());
	}

	/**
	 * Constructs a combined {@link Region}. Only used by
	 * {@link Region#combine(List)}.
	 * 
	 * @param object
	 *            <code>boolean[][]</code> where if a pixel from the specified
	 *            {@link BufferedImage} is the specified {@link Color} the it is
	 *            assigned true, otherwise it is false.
	 * @param width
	 *            This width of the image this region is a part of.
	 * @param height
	 *            This height of the image this region is a part of.
	 * 
	 */
	public Region(boolean[][] object, int width, int height) {
		constructor(object, width, height);
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

		// Check params
		if (list == null) {
			throw new IllegalArgumentException("List cannot be null.");
		} else if (list.isEmpty()) {
			throw new IllegalArgumentException("List cannot be empty.");
		} else if (width < 0) {
			throw new IllegalArgumentException("Width cannot be negative.");
		} else if (height < 0) {
			throw new IllegalArgumentException("Height cannot be negative.");
		}
		// Holds the region that will be the result.
		boolean[][] base = new boolean[width][height];

		// Iterate through all the regions in the list.
		for (Region region : list) {

			// Iterate through every element in the region.
			for (int y = 0; y < region.height; y++) {
				for (int x = 0; x < region.width; x++) {

					// If the current element in the parameter region is true but this regions's
					// object is false the set it to true.
					if (!base[region.x + x][region.y + y] && region.object[x][y]) {
						base[region.x + x][region.y + y] = true;
					}
				}
			}
		}

		return new Region(base, width, height);
	}

	/**
	 * Retrieves whether or not the specified {@link Point} is inside this region.
	 * 
	 * @param point
	 *            {@link Point}
	 * @return <code>boolean</code>
	 */
	public boolean isInside(Point point) {

		// Null param check.
		if (point == null) {
			throw new IllegalArgumentException("Point cannot be null");
		}

		// If the x and y are out side the bounds of the region return false;
		if (point.x < x || point.x > x + width || point.y < y || point.y > y + height) {
			return false;
		}

		return object[point.x - x][point.y - y];
	}

	/**
	 * Retrieves the x position of the {@link Region}.
	 * 
	 * @return <code>int</code>
	 */
	public int getX() {
		return x;
	}

	/**
	 * Retrieves the y position of the {@link Region}.
	 * 
	 * @return <code>int</code>
	 */
	public int getY() {
		return y;
	}

	/**
	 * Retrieves the width of the {@link Region}.
	 * 
	 * @return <code>int</code>
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Retrieves the height of the {@link Region}.
	 * 
	 * @return <code>int</code>
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Retrieves the boolean object that represents the {@link Region}.
	 * 
	 * @return <code>boolean[][]</code>
	 */
	public boolean[][] getObject() {
		return object;
	}

	/**
	 * Includes the common code from both constructors.
	 * 
	 * @param object
	 *            The object that will be reduce and stored in this {@link Region}.
	 * @param width
	 *            This width of the image this region is a part of.
	 * @param height
	 *            This height of the image this region is a part of.
	 */
	private void constructor(boolean[][] object, int width, int height) {
		this.object = new Reducer().reduce(object, width, height);
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
	private boolean[][] getRegion(Image image, Color color) {

		// Holds the array of booleans initially all elements are set to false
		boolean[][] object = new boolean[image.getWidth()][image.getHeight()];

		// Iterate through all pixel in the image and if a pixel is the same colour as
		// the one specified set that pixels position to true.
		for (int y = 0; y < image.getHeight(); y++) {

			for (int x = 0; x < image.getWidth(); x++) {

				if (color.equals(image.getColor(x, y))) {
					object[x][y] = true;
				}
			}
		}

		return object;
	}

	/**
	 * Designed to reduce a <code>boolean[][]</code> from a specified
	 * <code>int</code> width and <code>int</code> height to the smallest possible
	 * <code>boolean[][]</code> without loosing any data. This will minimise the
	 * storage space an increase the efficiency greatly. It also helps construct the
	 * parent class {@link Region}.<br>
	 * Using {@link Reducer#reduce(boolean[][], int, int)}<br>
	 * Resembles the factory pattern.
	 * 
	 * @author Joshua_Eddy
	 *
	 */
	private final class Reducer {

		/**
		 * Holds the lower x boundary.
		 */
		private int lowerXBoundary;

		/**
		 * Holds the upper x boundary.
		 */
		private int upperXBoundary;

		/**
		 * Holds the upper y boundary.
		 */
		private int upperYBoundary;

		/**
		 * Holds the lower y boundary.
		 */
		private int lowerYBoundary;

		/**
		 * Reduces a specified <code>boolean[][]</code> from the specified width and
		 * height to the its smallest possible size without loss of data. This also
		 * assigns {@link Region#width}, {@link Region#height}, {@link Region#x} and
		 * {@link Region#y}.
		 * 
		 * @param toReduce
		 * @param width
		 * @param height
		 * @return
		 */
		public boolean[][] reduce(boolean[][] toReduce, int width, int height) {

			// Initial value is the opposite edge of the image.
			lowerXBoundary = width;
			lowerYBoundary = height;
			upperXBoundary = 0;
			upperYBoundary = 0;

			setYBoundaries(toReduce, width, height);
			setXBoundaries(toReduce, width, height);

			return reduceArray(toReduce);

		}

		/**
		 * Assigns the outer horizontal boundary of the region of pixels given by a
		 * {@link BufferedImage} and {@link Color}.
		 * 
		 * @param toReduce
		 * 
		 * @param width
		 *            {@link BufferedImage#getWidth()}
		 * @param height
		 *            {@link BufferedImage#getHeight()}
		 */
		private void setXBoundaries(boolean[][] toReduce, int width, int height) {

			/*
			 * IMPORTATNT CONCEPT: This methods iterates from the left edge of the image
			 * (assuming that x grows as you go to the right on the screen) to find the left
			 * most edge of the shape in the image. Then repeat the process from the right
			 * edge. The result is lowest and and highest x values of valid elements.
			 */

			// Iterate through each row of the image.
			for (int rowNum = 0; rowNum < height; rowNum++) {

				getLowerXBoundary(toReduce, rowNum, width);

				getUpperXBoundary(toReduce, rowNum, width);

			}

		}

		/**
		 * Assigns the outer vertical boundaries of the region of pixels given by a
		 * {@link BufferedImage} and {@link Color}.
		 * 
		 * @param toReduce
		 * 
		 * 
		 * 
		 * @param width
		 *            {@link BufferedImage#getWidth()}
		 * @param height
		 *            {@link BufferedImage#getHeight()}
		 */
		private void setYBoundaries(boolean[][] toReduce, int width, int height) {

			/*
			 * IMPORTANT CONCEPT: This methods iterates from the bottom edge of the image
			 * (assuming that y grows as you go down the screen) to find the lowest edge of
			 * the shape in the image. Then repeat the process from the top edge. The result
			 * is lowest and and highest y values of valid elements.
			 */

			// Iterate through each row of the image.
			for (int colNum = 0; colNum < width; colNum++) {

				getLowerYBoundary(toReduce, colNum, height);

				getUpperYBoundary(toReduce, colNum, height);

			}

		}

		/**
		 * Assigns {@link Region#lowerXBoundary} by iterating through all the columns in
		 * the specified row.
		 * 
		 * @param toReduce
		 *            <code>boolean[][]</code> to be reduced in size.
		 * @param rowNum
		 *            <code>int</code> index of the row currently being processed by
		 *            {@link Reducer#setXBoundaries(boolean[][], int, int)}.
		 * @param width
		 *            <code>int</code> width of the <code>boolean[][]</code> that is
		 *            being reduced.
		 */
		private void getLowerXBoundary(boolean[][] toReduce, int rowNum, int width) {

			// Iterate through each column on the current row from the right.
			for (int x = 0; x < width; x++) {

				// If the current column is further left that the current left boundary set it
				// as the new left boundary. And exit the loop.
				if (toReduce[x][rowNum]) {
					if (x < lowerXBoundary) {
						lowerXBoundary = x;
					}
					break;
				}
			}

		}

		/**
		 * Assigns {@link Region#upperXBoundary} by iterating through all the columns in
		 * the specified row.
		 * 
		 * @param toReduce
		 *            <code>boolean[][]</code> to be reduced in size.
		 * @param rowNum
		 *            <code>int</code> index of the row currently being processed by
		 *            {@link Reducer#setXBoundaries(boolean[][], int, int)}.
		 * @param width
		 *            <code>int</code> width of the <code>boolean[][]</code> that is
		 *            being reduced.
		 */
		private void getUpperXBoundary(boolean[][] toReduce, int rowNum, int width) {

			// Iterate through each column on the current row from the left.
			for (int x = width - 1; x >= 0; x--) {

				// If the current column is further right that the current right boundary set it
				// as the new right boundary. And exit the loop.
				if (toReduce[x][rowNum]) {
					if (x > upperXBoundary) {
						upperXBoundary = x;
					}
					break;
				}
			}

		}

		/**
		 * Assigns {@link Region#upperYBoundary} by iterating through all the rows in
		 * the specified column.
		 * 
		 * @param toReduce
		 *            <code>boolean[][]</code> to be reduced in size.
		 * @param colNum
		 *            <code>int</code> index of the column currently being processed by
		 *            {@link Reducer#setYBoundaries(boolean[][], int, int)}.
		 * @param height
		 *            <code>int</code> height of the <code>boolean[][]</code> that is
		 *            being reduced.
		 */
		private void getUpperYBoundary(boolean[][] toReduce, int colNum, int height) {

			// Iterate through each row on the current column from the top.
			for (int y = height - 1; y >= 0; y--) {

				if (toReduce[colNum][y]) {
					if (y > upperYBoundary) {
						upperYBoundary = y;
					}
					break;
				}

			}

		}

		/**
		 * Assigns {@link Region#lowerYBoundary} by iterating through all the rows in
		 * the specified column.
		 * 
		 * @param toReduce
		 *            <code>boolean[][]</code> to be reduced in size.
		 * @param colNum
		 *            <code>int</code> index of the column currently being processed by
		 *            {@link Reducer#setYBoundaries(boolean[][], int, int)}.
		 * @param height
		 *            <code>int</code> height of the <code>boolean[][]</code> that is
		 *            being reduced.
		 */
		private void getLowerYBoundary(boolean[][] toReduce, int colNum, int height) {
			// Iterate through each row on the current column from the bottom.
			for (int y = 0; y < height; y++) {

				if (toReduce[colNum][y]) {

					if (y < lowerYBoundary) {
						lowerYBoundary = y;
					}
					break;
				}
			}
		}

		/**
		 * Shrinks the size of a specified <code>boolean[][]</code> to its smallest
		 * possible size based on {@link Reducer#lowerXBoundary},
		 * {@link Reducer#lowerYBoundary}, {@link Reducer#upperXBoundary} and
		 * {@link Reducer#upperYBoundary}. Assigns {@link Region#x} and
		 * {@link Region#y}.
		 * 
		 * @param toReduce
		 *            <code>boolean[][]</code> to be reduced in size.
		 * @return <code>boolean[][]</code> smallest version of the specified array.
		 */
		private boolean[][] reduceArray(boolean[][] toReduce) {

			width = upperXBoundary - lowerXBoundary + 1;
			height = upperYBoundary - lowerYBoundary + 1;

			// Create the new object array to be the size of the space between the
			// boundaries.
			boolean[][] tempObject = new boolean[width][height];

			// Set the x and y values of the region to the lower boundaries.
			x = lowerXBoundary;
			y = lowerYBoundary;

			// Iterate through each row of the object[][]
			for (int y = lowerYBoundary; y <= upperYBoundary; y++) {

				// Iterate through each column of the object[][]
				for (int x = lowerXBoundary; x <= upperXBoundary; x++) {

					// Assign the value of the current element in the object array to its
					// corresponding position in the new object array.
					tempObject[x - lowerXBoundary][y - lowerYBoundary] = toReduce[x][y];
				}
			}

			return tempObject;

		}

	}
}
