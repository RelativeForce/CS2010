package peril;

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

}
