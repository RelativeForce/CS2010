package peril.views.slick.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;

/**
 * This denotes the region of pixels given on screen where each pixel is given a
 * boolean value based on a predetermined set of conditions. The way that this
 * object is constructed defines these conditions. When
 * {@link Region#isValid(Point)} is called the boolean returned will be based on
 * the pixels validity defined upon {@link Region} construction.
 * 
 * <br>
 * <br>
 * The {@link Reducer} takes the {@link Region} defined by the {@link Image} and
 * reduces the size of the {@link Region} so that there is the minimum memory
 * over head.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-18
 * @version 1.01.02
 * 
 * @see Image
 * @see Reducer
 *
 */
public final class Region {

	/**
	 * The default transparency of the {@link Color}ed pixel on the {@link Region}
	 * equivalent {@link Image}. Bounds: 0 - 255
	 */
	private static final int DEFAULT_TRANSPARENCY = 180;

	/**
	 * The <code>boolean[]</code> where if a pixel from the specified {@link Image}
	 * is valid then it is assigned true, otherwise it is false. The valid value for
	 * a pixel is determined by how the {@link Region} is constructed.
	 */
	private final boolean[] object;

	private final Map<Color, Image> versions;

	/**
	 * The <code>int</code> width of this {@link Region}.
	 */
	private int width;

	/**
	 * The <code>int</code> height of this {@link Region}.
	 */
	private int height;

	/**
	 * The {@link Point} position of this {@link Region} on screen.
	 */
	private Point position;

	/**
	 * Constructs a new {@link Region} using an {@link Image} where the valid pixel
	 * value is any value except transparent.
	 * 
	 * @param image
	 *            The {@link Image} that will be converted to a {@link Region}.
	 */
	public Region(Image image) {
		this(getRegion(image), image.getWidth(), image.getHeight());
	}

	/**
	 * Constructs a new {@link Region} using an {@link Image} where the valid pixel
	 * value is the specified {@link Color}.
	 * 
	 * @param image
	 *            The {@link Image} that contains the {@link Region} of the of a
	 *            specified {@link Color}.
	 * @param colour
	 *            The {@link Color} of the specified {@link Region}.
	 */
	public Region(Image image, Color colour) {
		this(getRegion(image, colour), image.getWidth(), image.getHeight());
	}

	/**
	 * Constructs a combined {@link Region} where the valid pixel value is
	 * <code>true</code> as the specified <code>boolean[]</code> is already 'not
	 * reduced' region.
	 * 
	 * @param object
	 *            The <code>boolean[]</code> where if a pixel from the specified
	 *            {@link Image} is valid then it is assigned true, otherwise it is
	 *            false.
	 * @param width
	 *            This width of the image this {@link Region} is a part of.
	 * @param height
	 *            This height of the image this {@link Region} is a part of.
	 * 
	 */
	public Region(boolean[] object, int width, int height) {
		this.object = new Reducer(object, width, height).reduce();
		this.versions = new HashMap<>();
	}

	/**
	 * Constructs a new {@link Region} that is a rectangle where the whole region is
	 * the valid value by definition.
	 * 
	 * @param width
	 *            The <code>int</code> width of the rectangular {@link Region}.
	 * @param height
	 *            The <code>int</code> height of the rectangular {@link Region}.
	 * @param position
	 *            The {@link Point} position of the {@link Region}.
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

		this.versions = new HashMap<>();

	}

	/**
	 * Combines a {@link List} of {@link Region} into ONE {@link Region}.
	 * 
	 * @param list
	 *            The {@link List} of {@link Region}s to be combined.
	 * @param width
	 *            The <code>int</code> width of the space the {@link Region}s are
	 *            in.
	 * @param height
	 *            The <code>int</code> height of the space the {@link Region}s are
	 *            in.
	 * @return The combined {@link Region}.
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
		for (Region region : list) {

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
	 *            {@link Region} A
	 * @param b
	 *            {@link Region} B
	 * @return Whether the two specified {@link Region}s overlap or not at the pixel
	 *         level.
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

					if (a.isValid(new Point(x, y)) && b.isValid(new Point(x, y))) {
						overlap = true;
						break;
					}

				}

				if (overlap) {
					break;
				}
			}

		}

		return overlap;
	}

	/**
	 * Sets the {@link Point} of this {@link Region}.
	 * 
	 * @param position
	 *            The {@link Point} of this {@link Region}.
	 */
	public void setPosition(Point position) {

		if (position == null) {
			throw new NullPointerException("Position cannot be null.");
		}

		this.position = position;
	}

	/**
	 * Retrieves the {@link Point} position of the {@link Region}.
	 * 
	 * @return The {@link Point} position of the {@link Region}.
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
	 * Retrieves whether or not the specified {@link Point} is inside this
	 * {@link Region} and if it is then whether the pixel at that position has a
	 * valid value based on the conditions that were defined when the {@link Region}
	 * was constructed.
	 * 
	 * @param point
	 *            The {@link Point} to be validated.
	 * @return The validity of the specified {@link Point} position.
	 */
	public boolean isValid(Point point) {

		// Null parameter check.
		if (point == null) {
			throw new IllegalArgumentException("Point cannot be null");
		}

		// If the x and y are out side the bounds of the region return false;
		if (point.x < position.x || point.x >= position.x + width || point.y < position.y
				|| point.y >= position.y + height) {
			return false;
		}

		// Return the value of the pixel.
		return object[getIndex(point.x - position.x, point.y - position.y, height)];
	}

	/**
	 * Retrieves the <code>boolean[]</code> where if a pixel from the specified
	 * {@link Image} is valid then it is assigned true, otherwise it is false. The
	 * valid value for a pixel is determined by how the {@link Region} is
	 * constructed.
	 * 
	 * @return <code>boolean[]</code>
	 */
	public boolean[] getObject() {
		return object;
	}

	/**
	 * Converts this {@link Region} into a {@link Image} where all the valid pixels
	 * in the {@link Region} are set to the specified {@link Color} at the
	 * {@link Region#DEFAULT_TRANSPARENCY}.
	 * 
	 * @param color
	 *            The {@link Color} of the {@link Region} in the {@link Image}.
	 * @return The {@link Image} equivalent of the {@link Region}.
	 */
	public Image convert(Color color) {
		return convert(color, DEFAULT_TRANSPARENCY);
	}

	/**
	 * Converts this {@link Region} into a {@link Image} where all the valid pixels
	 * in the {@link Region} are set to the specified {@link Color} at the specified
	 * transparency.
	 * 
	 * @param color
	 *            The {@link Color} of the {@link Region} in the {@link Image}.
	 * @param transparency
	 *            The transparency of the {@link Color} for each valid pixel in the
	 *            {@link Region}.
	 * @return The {@link Image} equivalent of the {@link Region}.
	 */
	public Image convert(Color color, int transparency) {

		if (color == null) {
			throw new NullPointerException("Colour cannot be null");
		}

		// If the version already exists.
		if (versions.containsKey(color)) {
			return versions.get(color);
		}

		// Holds the image of the region.
		final ImageBuffer imagebuffer = new ImageBuffer(width, height);

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

		final Image version = imagebuffer.getImage();

		versions.put(color, version);

		return version;

	}

	/**
	 * Finalises this {@link Region} for garbage collection.
	 */
	@Override
	protected void finalize() throws Throwable {

		versions.values().forEach(image -> {
			try {
				image.destroy();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		});

		super.finalize();
	}

	/**
	 * Retrieves the index of a specified coordinate inside the
	 * {@link Region#object}.
	 * 
	 * @param x
	 *            The x coordinate of the position.
	 * @param y
	 *            The y coordinate of the position.
	 * @param height
	 *            The height of the {@link Region#object}.
	 * 
	 * @return The index in a boolean[] based on the specified parameters.
	 */
	private static int getIndex(int x, int y, int height) {
		return (x * height) + y;
	}

	/**
	 * Retrieves a <code>boolean[]</code> where if a pixel from the specified
	 * {@link Image} is not {@link Color#transparent} it is assigned true, otherwise
	 * it is false.
	 * 
	 * @param image
	 *            The {@link Image} to be converted into an {@link Image}.
	 * @return <code>boolean[]</code>
	 */
	private static boolean[] getRegion(Image image) {
		return getRegion(image, null);
	}

	/**
	 * Retrieves a <code>boolean[]</code> where if a pixel from the specified
	 * {@link Image} is the specified {@link Color} the it is assigned true,
	 * otherwise it is false<br>
	 * <strong>OR</strong> if the specified colour is null,<br>
	 * Retrieves a <code>boolean[][]</code> where if a pixel from the specified
	 * {@link Image} is not {@link Color#transparent} it is assigned true, otherwise
	 * it is false.
	 * 
	 * @param image
	 *            The {@link Image} to be converted into an {@link Image}.
	 * @param color
	 *            The {@link Color} that is valid.
	 * @return <code>boolean[][]</code>
	 */
	private static boolean[] getRegion(Image image, Color color) {

		// The dimensions of the image
		final int imageHeight = image.getHeight();
		final int imageWidth = image.getWidth();

		// Holds the array of booleans initially all elements are set to false
		final boolean[] object = new boolean[imageWidth * imageHeight];

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
	 * The helper is designed to reduce a <code>boolean[]</code> from a specified
	 * width and height to the smallest possible <code>boolean[]</code> without
	 * loosing any data. This will minimise the storage space an increase the
	 * efficiency greatly. It also helps construct the parent class {@link Region}.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @since 2018-02-17
	 * @version 1.01.01
	 * 
	 * @see Reducer#reduce()
	 *
	 */
	private final class Reducer {

		/**
		 * The array that this {@link Reducer} will shrink to save memory space.
		 */
		private final boolean[] toReduce;

		/**
		 * The initial height of the {@link Region}.
		 */
		private final int initialHeight;

		/**
		 * The initial width of the {@link Region}.
		 */
		private final int initialWidth;

		/**
		 * Holds the lower x boundary.
		 */
		private int lowerXBoundary;

		/**
		 * Holds the upper x boundary.
		 */
		private int upperXBoundary;

		/**
		 * Holds the lower y boundary.
		 */
		private int lowerYBoundary;

		/**
		 * Holds the upper y boundary.
		 */
		private int upperYBoundary;

		/**
		 * Constructs a new {@link Reducer}.Reduces a specified <code>boolean[]</code>
		 * from the specified width and height to the its smallest possible size without
		 * loss of data. This also assigns {@link Region#width}, {@link Region#height},
		 * {@link Region#position}.
		 * 
		 * @param toReduce
		 *            The <code>boolean[]</code> to reduce in size.
		 * @param width
		 *            The initial width of the <code>boolean[]</code> array.
		 * @param height
		 *            The initial height of the <code>boolean[]</code> array.
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
		 * Shrinks the size of a specified <code>boolean[]</code> to its smallest
		 * possible size based on {@link Reducer#lowerXBoundary},
		 * {@link Reducer#lowerYBoundary}, {@link Reducer#upperXBoundary} and
		 * {@link Reducer#upperYBoundary}. Assigns {@link Region#position}.
		 * 
		 * @return <code>boolean[]</code> smallest version of the specified array.
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
		 * Assigns {@link Reducer#lowerXBoundary} by iterating through all the columns
		 * in the specified row.
		 * 
		 * @param rowNum
		 *            The <code>int</code> index of the row currently being processed.
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
		 * Assigns {@link Reducer#upperXBoundary} by iterating through all the columns
		 * in the specified row.
		 * 
		 * @param rowNum
		 *            The <code>int</code> index of the row currently being processed.
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
		 * Assigns {@link Reducer#upperYBoundary} by iterating through all the rows in
		 * the specified column.
		 *
		 * @param colNum
		 *            The <code>int</code> index of the column currently being
		 *            processed.
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
		 * Assigns {@link Reducer#lowerYBoundary} by iterating through all the rows in
		 * the specified column.
		 * 
		 * @param colNum
		 *            The <code>int</code> index of the column currently being
		 *            processed.
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
