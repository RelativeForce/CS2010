package peril.views.slick.util;

/**
 * A coordinate that has an x and y value. Immutable pattern
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-17
 * @version 1.01.01
 *
 */
public final class Point {

	/**
	 * The x coordinate of this {@link Point}.
	 */
	public final int x;

	/**
	 * The y coordinate of this {@link Point}.
	 */
	public final int y;

	/**
	 * Constructs a new {@link Point}.
	 * 
	 * @param x
	 *            The x coordinate of this {@link Point}.
	 * @param y
	 *            The y coordinate of this {@link Point}.
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Checks the equality of two {@link Point}s
	 */
	@Override
	public boolean equals(Object o) {

		// If both the objects are points
		if (o instanceof Point) {

			// Cast to point
			final Point other = (Point) o;

			// If both the points have the same x and y.
			if (other.x == this.x && other.y == this.y) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieves the {@link Point} half way between two specified {@link Point}s.
	 * 
	 * @param a
	 *            {@link Point} A
	 * @param b
	 *            {@link Point} B
	 * @return The {@link Point} half way between two specified {@link Point}s.
	 */
	public static Point getMiddle(Point a, Point b) {

		final int midX = ((b.x - a.x) / 2) + a.x;
		final int midY = ((b.y - a.y) / 2) + a.y;
		
		// Return the mid point.
		return new Point(midX, midY);

	}

}
