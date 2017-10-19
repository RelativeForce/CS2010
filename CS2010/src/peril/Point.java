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

}
