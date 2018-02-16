package peril.views.slick.util;

/**
 * A coordinate that has an x and y value. Immutable pattern
 * 
 * @author Joshua_Eddy
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

		if (o instanceof Point) {

			Point other = (Point) o;

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
	 *            {@link Point}
	 * @param b
	 *            {@link Point}
	 * @return {@link Point}
	 */
	public static Point getMiddle(Point a, Point b) {

		final int midX = ((b.x - a.x) / 2) + a.x;
		final int midY = ((b.y - a.y) / 2) + a.y;
		return new Point(midX, midY);

	}

}
