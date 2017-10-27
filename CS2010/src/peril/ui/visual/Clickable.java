package peril.ui.visual;

import peril.Point;

/**
 * Encapsulates the behaviour of being clicked by the mouse. Any object that
 * extends this will be able to be clicked by the mouse.
 * 
 * @author Joshua_Eddy
 *
 */
public abstract class Clickable {

	/**
	 * The region that this encompasses on the screen.
	 */
	private Region region;

	/**
	 * Whether or not a mouse click at a specified {@link Point} will be inside this
	 * objects {@link Clickable#region}. If the {@link Region} is <code>null</code>
	 * then this will return <code>false</code>.
	 * 
	 * @param point
	 *            {@link Point}
	 * @return Clicked or not.
	 */
	public boolean isClicked(Point point) {
		if (!hasRegion()) {
			throw new NullPointerException("Region is null.");
		}
		return region.isInside(point);
	}

	/**
	 * Assigns the {@link Region} to this.
	 * 
	 * @param region
	 *            {@link Region}.
	 */
	public void setRegion(Region region) {
		if (region == null) {
			throw new IllegalArgumentException("The specified Region cannot be null.");
		}

		this.region = region;
	}

	/**
	 * Retrieves the {@link Region} at this {@link Clickable}.
	 * 
	 * @return {@link Region}
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * Whether or not this {@link Clickable} has a {@link Region} or not.
	 * 
	 * @return <code>boolean</code>
	 */
	public boolean hasRegion() {
		return region != null;
	}

}
