package peril.views.slick;

import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;

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
	 * The <code>boolean[]</code> where if a pixel from the specified {@link Image}
	 * is the specified {@link Color} the it is assigned true, otherwise it is
	 * false.
	 */
	private final boolean[] object;

	/**
	 * The <code>int</code> width of this {@link Region}.
	 */
	private int width;

	/**
	 * The <code>int</code> height of this {@link Region}.
	 */
	private int height;

	/**
	 * The {@link Point} vector from the (0,0) of the image this {@link Region} is a
	 * part of to (0,0) of this {@link Region}.
	 */
	private Point position;

	/**
	 * Constructs a new {@link Region} using an {@link Image}.
	 * 
	 * @param image
	 *            {@link Image} that contains the {@link Region} of the of a
	 *            specified {@link Color}.
	 * @param colour
	 *            {@link Color} of the specified {@link Region}.
	 */
	public Region(Image image, Color colour) {
		this(getRegion(image, colour), image.getWidth(), image.getHeight());
	}

	/**
	 * Constructs a combined {@link Region}. Only used by
	 * {@link Region#combine(List)}.
	 * 
	 * @param object
	 *            <code>boolean[]</code> where if a pixel from the specified
	 *            {@link BufferedImage} is the specified {@link Color} the it is
	 *            assigned true, otherwise it is false.
	 * @param width
	 *            This width of the image this region is a part of.
	 * @param height
	 *            This height of the image this region is a part of.
	 * 
	 */
	public Region(boolean[] object, int width, int height) {
		this.object = new Reducer(object, width, height).reduce();
	}

	/**
	 * Constructs a new {@link Region} using an {@link Image}.
	 * 
	 * @param image
	 *            {@link Image}
	 */
	public Region(Image image) {
		this(getRegion(image), image.getWidth(), image.getHeight());
	}

	/**
	 * Constructs a new {@link Region} that is a rectangle.
	 * 
	 * @param width
	 *            <code>int</code> width of the rectangle.
	 * @param height
	 *            <code>int</code> height of the rectangle.
	 * @param position
	 *            {@link Point}
	 */
	public Region(int width, int height, Point position) {

		// Initialise the fields
		this.position = position;
		this.object = new boolean[width * height];
		this.width = width;
		this.height = height;

		// Assign all the points in the array as true;
		for (int index = 0; index < object.length; index++) {
			object[index] = true;
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

		// Check parameters
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
		final boolean[] base = new boolean[width * height];

		// Iterate through all the regions in the list.
		for (final Region region : list) {

			// Iterate through every element in the region.
			for (int y = 0; y < region.height; y++) {
				for (int x = 0; x < region.width; x++) {

					// If the current region object element is true set the corresponding base
					// element to true.
					if (region.object[getIndex(x, y, region.height)]) {

						// The index of the current region element
						final int index = getIndex(region.position.x + x, region.position.y + y, height);

						if (index > base.length) {
							throw new IllegalArgumentException(
									index + " is out of bounds of Region. width: " + width + " height: " + height);
						}

						base[index] = true;
					}
				}
			}
		}

		return new Region(base, width, height);
	}

	/**
	 * Determines whether the two specified {@link Region}s overlap or not at the
	 * pixel level.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean overlap(Region a, Region b) {

		boolean overlap = false;

		final boolean xLeft = a.position.x < b.position.x + b.width;
		final boolean xRight = a.position.x + a.width > b.position.x;

		final boolean yTop = a.position.y < b.position.y + b.height;
		final boolean yBottom = a.position.y + a.height > b.position.y;

		final boolean borderOverlapX = xLeft || xRight;
		final boolean borderOverlapY = yTop || yBottom;

		// If the regions' borders overlap then they may over lap at pixel level.
		if (borderOverlapX && borderOverlapY) {

			// Determine the x and y boundaries based on where a and b overlap
			final int minX = xLeft ? a.position.x : b.position.x;
			final int maxX = xRight ? a.position.x + a.width : b.position.x + b.width;
			final int minY = yTop ? a.position.y : b.position.y;
			final int maxY = yBottom ? a.position.y + a.height : b.position.y + b.height;

			// Iterate over the overlapping region and check if any of the pixels in that
			// region overlap.
			for (int x = minX; x < maxX; x++) {

				for (int y = minY; y < maxY; y++) {

					if (a.isInside(new Point(x, y)) && b.isInside(new Point(x, y))) {
						overlap = true;
						break;
					}

				}
				
				if(overlap) {
					break;
				}
			}

		}

		return overlap;
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
		if (point.x < position.x || point.x >= position.x + width || point.y < position.y
				|| point.y >= position.y + height) {
			return false;
		}

		return object[getIndex(point.x - position.x, point.y - position.y, height)];
	}

	/**
	 * Retrieves the {@link Point} position of the {@link Region}.
	 * 
	 * @return {@link Point}
	 */
	public Point getPosition() {
		return position;
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
	public boolean[] getObject() {
		return object;
	}

	/**
	 * Converts this {@link Region} into a {@link Image} that is stored on
	 * {@link Viewable#image}.
	 * 
	 * @param region
	 *            {@link Region}
	 * @return {@link Image}
	 */
	public Image convert(Color color) {
		return convert(color, 180);
	}

	/**
	 * Converts this {@link Region} into a {@link Image} that is stored on
	 * {@link Viewable#image}.
	 * 
	 * @param region
	 *            {@link Region}
	 * @param transparency
	 *            The transparency of the image.
	 * @return {@link Image}
	 */
	public Image convert(Color color, int transparency) {

		if (color == null) {
			throw new NullPointerException("Colour cannot be null");
		}

		// Holds the image of the region.
		ImageBuffer imagebuffer = new ImageBuffer(width, height);

		// Set the colour of the visual and get its rgb value.
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();

		// Iterate through every pixel in the object[] and if it is true set the
		// colour of the visual to the specified value.
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				if (object[getIndex(x, y, height)]) {
					imagebuffer.setRGBA(x, y, r, g, b, transparency);
				}
			}
		}
		return imagebuffer.getImage();

	}

	/**
	 * Sets the {@link Region#position}
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
	 * Retrieves a <code>boolean[][]</code> where if a pixel from the specified
	 * {@link Image} is not {@link Color#transparent} it is assigned true, otherwise
	 * it is false.
	 * 
	 * @param image
	 *            {@link Image}
	 * @return <code>boolean[][]</code>
	 */
	private static boolean[] getRegion(Image image) {
		return getRegion(image, null);
	}

	/**
	 * Retrieves a <code>boolean[]</code> where if a pixel from the specified
	 * {@link Image} is the specified {@link Color} the it is assigned true,
	 * otherwise it is false <br>
	 * <strong>OR</strong> if the specified colour is null,<br>
	 * Retrieves a <code>boolean[][]</code> where if a pixel from the specified
	 * {@link Image} is not {@link Color#transparent} it is assigned true, otherwise
	 * it is false.
	 * 
	 * @param image
	 *            {@link Image}
	 * @param color
	 *            {@link Color}
	 * @return <code>boolean[][]</code>
	 */
	private static boolean[] getRegion(Image image, Color color) {

		// The dimensions of the image
		final int imageHeight = image.getHeight();
		final int imageWidth = image.getWidth();

		// Holds the array of booleans initially all elements are set to false
		boolean[] object = new boolean[imageWidth * imageHeight];

		/*
		 * Iterate through all pixel in the image and if a pixel is the same colour as
		 * the one specified set that pixels position to true.
		 */
		for (int y = 0; y < imageHeight; y++) {
			for (int x = 0; x < imageWidth; x++) {

				/*
				 * If the colour is null then filter out all the pixels that are transparent
				 * otherwise filter out all the pixels that are NOT the specified colour.
				 */
				if (color == null) {
					if (!image.getColor(x, y).equals(Color.transparent)) {
						object[getIndex(x, y, imageHeight)] = true;
					}
				} else {
					if (color.equals(image.getColor(x, y))) {
						object[getIndex(x, y, imageHeight)] = true;
					}
				}

			}
		}

		return object;
	}

	/**
	 * Retrieves the index of a specified coordinate inside the
	 * {@link Region#object}.
	 * 
	 * @param x
	 * @param y
	 * @param height
	 *            of the {@link Region#object}
	 */
	private static int getIndex(int x, int y, int height) {
		return (x * height) + y;
	}

	/**
	 * Designed to reduce a <code>boolean[]</code> from a specified <code>int</code>
	 * width and <code>int</code> height to the smallest possible
	 * <code>boolean[]</code> without loosing any data. This will minimise the
	 * storage space an increase the efficiency greatly. It also helps construct the
	 * parent class {@link Region}.<br>
	 * Using {@link Reducer#reduce()}<br>
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
		 * The array that this {@link Reducer} will shrink to save memory space.
		 */
		private boolean[] toReduce;

		/**
		 * The initial height of the {@link Region}.
		 */
		private final int initialHeight;

		/**
		 * The initial width of the {@link Region}.
		 */
		private final int initialWidth;

		/**
		 * Constructs a new {@link Reducer}.Reduces a specified <code>boolean[]</code>
		 * from the specified width and height to the its smallest possible size without
		 * loss of data. This also assigns {@link Region#width}, {@link Region#height},
		 * {@link Region#x} and {@link Region#y}.
		 * 
		 * @param toReduce
		 *            The <code>boolean[]</code> to reduce in size.
		 * @param width
		 *            Initial <code>int</code> width of the <code>boolean[][]</code>
		 *            array.
		 * @param height
		 *            Initial <code>int</code> height of the <code>boolean[][]</code>
		 *            array.
		 * @return <code>boolean[][]</code> reduced array.
		 */
		public Reducer(boolean[] toReduce, int width, int height) {

			// Initial value is the opposite edge of the image.
			this.lowerXBoundary = width;
			this.lowerYBoundary = height;
			this.upperXBoundary = 0;
			this.upperYBoundary = 0;
			this.initialWidth = width;
			this.initialHeight = height;
			this.toReduce = toReduce;

			setYBoundaries();
			setXBoundaries();

		}

		/**
		 * Shrinks the size of a specified <code>boolean[][]</code> to its smallest
		 * possible size based on {@link Reducer#lowerXBoundary},
		 * {@link Reducer#lowerYBoundary}, {@link Reducer#upperXBoundary} and
		 * {@link Reducer#upperYBoundary}. Assigns {@link Region#x} and
		 * {@link Region#y}.
		 * 
		 * @return <code>boolean[][]</code> smallest version of the specified array.
		 */
		public boolean[] reduce() {

			width = upperXBoundary - lowerXBoundary + 1;
			height = upperYBoundary - lowerYBoundary + 1;

			// Create the new object array to be the size of the space between the
			// boundaries.
			boolean[] tempObject = new boolean[width * height];

			// Set the position of the region to the lower boundaries.
			position = new Point(lowerXBoundary, lowerYBoundary);

			// Iterate through each row of the object[][]
			for (int y = lowerYBoundary; y <= upperYBoundary; y++) {

				// Iterate through each column of the object[][]
				for (int x = lowerXBoundary; x <= upperXBoundary; x++) {

					// Assign the value of the current element in the object array to its
					// corresponding position in the new object array.
					tempObject[getIndex(x - lowerXBoundary, y - lowerYBoundary, height)] = toReduce[getIndex(x, y,
							initialHeight)];
				}
			}

			return tempObject;
		}

		/**
		 * Assigns the outer horizontal boundary of the region of pixels.
		 */
		private void setXBoundaries() {

			/*
			 * IMPORTATNT CONCEPT: This methods iterates from the left edge of the image
			 * (assuming that x grows as you go to the right on the screen) to find the left
			 * most edge of the shape in the image. Then repeat the process from the right
			 * edge. The result is lowest and and highest x values of valid elements.
			 */

			// Iterate through each row of the image.
			for (int rowNum = 0; rowNum < initialHeight; rowNum++) {

				getLowerXBoundary(rowNum);

				getUpperXBoundary(rowNum);

			}

		}

		/**
		 * Assigns the outer vertical boundaries of the region of pixels.
		 */
		private void setYBoundaries() {

			/*
			 * IMPORTANT CONCEPT: This methods iterates from the bottom edge of the image
			 * (assuming that y grows as you go down the screen) to find the lowest edge of
			 * the shape in the image. Then repeat the process from the top edge. The result
			 * is lowest and and highest y values of valid elements.
			 */

			// Iterate through each row of the image.
			for (int colNum = 0; colNum < initialWidth; colNum++) {

				getLowerYBoundary(colNum);

				getUpperYBoundary(colNum);

			}

		}

		/**
		 * Assigns {@link Region#lowerXBoundary} by iterating through all the columns in
		 * the specified row.
		 * 
		 * @param rowNum
		 *            <code>int</code> index of the row currently being processed by
		 *            {@link Reducer#setXBoundaries(boolean[][], int, int)}.
		 */
		private void getLowerXBoundary(int rowNum) {

			// Iterate through each column on the current row from the right.
			for (int x = 0; x < initialWidth; x++) {

				// If the current column is further left that the current left boundary set it
				// as the new left boundary. And exit the loop.
				if (toReduce[getIndex(x, rowNum, initialHeight)]) {
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
		 * @param rowNum
		 *            <code>int</code> index of the row currently being processed by
		 *            {@link Reducer#setXBoundaries(boolean[][], int, int)}.
		 */
		private void getUpperXBoundary(int rowNum) {

			// Iterate through each column on the current row from the left.
			for (int x = initialWidth - 1; x >= 0; x--) {

				// If the current column is further right that the current right boundary set it
				// as the new right boundary. And exit the loop.
				if (toReduce[getIndex(x, rowNum, initialHeight)]) {
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
		 * @param colNum
		 *            <code>int</code> index of the column currently being processed by
		 *            {@link Reducer#setYBoundaries(boolean[][], int, int)}.
		 */
		private void getUpperYBoundary(int colNum) {

			// Iterate through each row on the current column from the top.
			for (int y = initialHeight - 1; y >= 0; y--) {

				if (toReduce[getIndex(colNum, y, initialHeight)]) {
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
		 * @param colNum
		 *            <code>int</code> index of the column currently being processed by
		 *            {@link Reducer#setYBoundaries(boolean[][], int, int)}.
		 */
		private void getLowerYBoundary(int colNum) {
			// Iterate through each row on the current column from the bottom.
			for (int y = 0; y < initialHeight; y++) {
				if (toReduce[getIndex(colNum, y, initialHeight)]) {

					if (y < lowerYBoundary) {
						lowerYBoundary = y;
					}
					break;
				}
			}
		}
	}
}
