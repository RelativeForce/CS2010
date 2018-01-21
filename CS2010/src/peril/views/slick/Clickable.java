package peril.views.slick;

import org.newdawn.slick.Image;

import peril.Point;

/**
 * Encapsulates the behaviour of being clicked by the mouse. Any object that
 * extends this will be able to be clicked by the mouse.
 * 
 * @author Joshua_Eddy
 * 
 * @see Viewable
 *
 */
public abstract class Clickable extends Viewable {

	/**
	 * The {@link Region} that this encompasses on the screen.
	 */
	private Region region;

	/**
	 * Constructs a new {@link Clickable}.
	 * 
	 * @param region
	 *            {@link Region} that can be clicked.
	 * @param position
	 *            The {@link Point} position of the {@link Clickable}.
	 */
	public Clickable(Region region) {		
		super(region.getPosition());
		this.region = region;
	}

	/**
	 * Constructs a new {@link Clickable}.
	 * 
	 * @param region
	 *            {@link Region} that can be clicked.
	 * @param position
	 *            The {@link Point} position of the {@link Clickable}.
	 * @param image
	 *            The {@link Image} of this {@link Clickable}
	 */
	public Clickable(Region region, Image image) {
		super(image, region.getPosition());
		this.region = region;
	}

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
		return region.isInside(point);
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
	 * Retrieves the {@link Point} position of this {@link Clickable}. If the
	 * {@link Region} of this {@link Clickable} is not null retrieve the
	 * {@link Point} position of the {@link Region}.
	 */
	@Override
	public Point getPosition() {
		return region.getPosition();
	}

	/**
	 * Sets the {@link Point} position of this {@link Clickable}.
	 * 
	 * @param position
	 *            {@link Point}
	 */
	public void setPosition(Point position) {
		super.setPosition(position);
		region.setPosition(position);
	}

	/**
	 * Retrieves the width of the {@link Region} in this {@link Clickable}. If the
	 * {@link Region} is null this returns {@link Viewable#getWidth()}.
	 * 
	 * @return <code>int</code>
	 */
	public int getWidth() {
		return region.getWidth();
	}

	/**
	 * Retrieves the height of the {@link Region} in this {@link Clickable}. If the
	 * {@link Region} is null this returns {@link Viewable#getHeight()}.
	 * 
	 * @return <code>int</code>
	 */
	public int getHeight() {
		return region.getHeight();
	}

}
