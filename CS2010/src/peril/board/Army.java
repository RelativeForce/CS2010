package peril.board;

import peril.Point;

/**
 * Encapsulates the behaviours of a collection of units. This will be aggregated
 * by the {@link WarMenu} and composes the {@link Country}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class Army {

	/**
	 * The {@link Point} offset from the centre of the country this army will be
	 * displayed at.
	 */
	private Point offset;

	/**
	 * The size of the army.
	 */
	private volatile int size;

	/**
	 * Constructs a new empty {@link Army} with an offset of (0,0).
	 */
	public Army() {
		setSize(1);
		offset = new Point(0, 0);
	}

	/**
	 * Constructs a new {@link Army} of a specified size with an offset of (0,0).
	 * 
	 * @param size
	 *            The size of the {@link Army}. Must be greater than zero.
	 */
	public Army(int size) {
		setSize(size);
		offset = new Point(0, 0);
	}

	/**
	 * Assigns a new size to this {@link Army}.
	 * 
	 * @param size
	 *            The new size of the {@link Army}. Must be greater than zero.
	 */
	public void setSize(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Size must be greater than zero");
		}
		this.size = size;
	}

	/**
	 * Retrieves the {@link Point} offset from the centre of the country this army
	 * will be displayed at.
	 * 
	 * @return {@link Point} offset.
	 */
	public Point getOffset() {
		return offset;
	}

	/**
	 * Retrieves the size of the {@link Army}.
	 * 
	 * @return
	 */
	public int getSize() {
		return size;
	}

	public void add(int amount) {
		size += amount;
	}

	public void remove(int amount) {
		if (size - amount < 0) {
			throw new IllegalStateException("Army size cannot be less than zero");
		}
		size -= amount;
	}

	public void setOffset(Point offset) {

		if (offset == null) {
			throw new NullPointerException("Offset cannot be null.");
		}

		this.offset = offset;

	}
}
