package peril.views.slick.util;

import org.newdawn.slick.Image;

/**
 * Encapsulates the behaviour of an {@link Viewable} object that can clicked by
 * the mouse. This object wraps {@link Region} under a {@link Viewable} so that
 * the object can be both seen and clicked.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-18
 * @version 1.02.01
 *
 * @see Viewable
 * @see Region
 * @see Image
 */
public class Clickable extends Viewable {

	/**
	 * The {@link Region} that this encompasses on the screen. This should be the
	 * {@link Region} version of the {@link Viewable#getImage()}. However if the
	 * {@link Image} is <code>null</code> the region may be not <code>null</code>.
	 */
	private Region region;

	/**
	 * Constructs a new {@link Clickable} with no {@link image}.
	 * 
	 * @param region
	 *            The {@link Region} that can be clicked.
	 */
	public Clickable(Region region) {
		super(region.getPosition());
		this.region = region;
	}

	/**
	 * Constructs a new {@link Clickable} with an {@link image}.
	 * 
	 * @param region
	 *            The {@link Region} that can be clicked.
	 * @param image
	 *            The {@link Image} of this {@link Clickable}
	 */
	public Clickable(Region region, Image image) {
		super(image, region.getPosition());
		this.region = region;
	}

	/**
	 * Constructs a empty {@link Clickable} item with no {@link Region} or
	 * {@link image}.
	 */
	public Clickable() {
		super(new Point(0, 0));
	}

	/**
	 * Constructs a new {@link Clickable} using an {@link Image}.
	 * 
	 * @param image
	 *            The image that will be converted into a {@link Clickable}.
	 */
	public Clickable(Image image) {
		super(image, new Point(0, 0));
		this.region = new Region(image);
	}

	/**
	 * Sets the {@link Point} position of this {@link Clickable} and {@link Region}.
	 * 
	 * @param position
	 *            The new {@link Point} position of the {@link Clickable} and
	 *            {@link Region}.
	 */
	public void setPosition(Point position) {
		super.setPosition(position);

		// If the clickable has a region change that regions position.
		if (hasRegion())
			region.setPosition(position);
	}

	/**
	 * Swaps the current {@link Image} of the {@link Clickable} with another and
	 * also replaces the {@link Region} as well.
	 */
	@Override
	public final void swapImage(Image image) {
		super.swapImage(image);
		swapRegion(image);
	}

	/**
	 * Replaces the current {@link Image} of the {@link Clickable} with another and
	 * also replaces the {@link Region} as well.
	 */
	@Override
	public final void replaceImage(Image image) {
		super.replaceImage(image);
		swapRegion(image);
	}

	/**
	 * Retrieves the width of the {@link Region} in this {@link Clickable}. If the
	 * {@link Region} is null this returns {@link Viewable#getWidth()}.
	 * 
	 * @return <code>int</code>
	 */
	public final int getWidth() {
		return hasRegion() ? region.getWidth() : super.getWidth();
	}

	/**
	 * Retrieves the height of the {@link Region} in this {@link Clickable}. If the
	 * {@link Region} is null this returns {@link Viewable#getHeight()}.
	 * 
	 * @return <code>int</code>
	 */
	public final int getHeight() {
		return hasRegion() ? region.getHeight() : super.getHeight();
	}

	/**
	 * Retrieves whether or not the {@link Clickable} has a {@link Region}.
	 * 
	 * @return Whether or not the {@link Clickable} has a {@link Region}.
	 */
	public final boolean hasRegion() {
		return region != null;
	}

	/**
	 * Whether or not a mouse click at a specified {@link Point} will be inside this
	 * objects {@link Region}. If the {@link Region} is <code>null</code> then this
	 * will return <code>false</code>.
	 * 
	 * @param point
	 *            The {@link Point} position of the mouse.
	 * @return Whether the {@link Region} was clicked or not.
	 */
	public final boolean isClicked(Point point) {
		return hasRegion() ? region.isValid(point) : false;
	}

	/**
	 * Retrieves the {@link Point} position of this {@link Clickable}. If the
	 * {@link Region} of this {@link Clickable} is not null retrieve the
	 * {@link Point} position of the {@link Region}.
	 * 
	 * @return The {@link Point} of the {@link Clickable}
	 */
	@Override
	public final Point getPosition() {
		return hasRegion() ? region.getPosition() : super.getPosition();
	}

	/**
	 * Retrieves the {@link Region} at this {@link Clickable}.
	 * 
	 * @return The {@link Region} of this {@link Clickable}.
	 */
	public final Region getRegion() {
		return region;
	}

	/**
	 * Generates a {@link Region} from the specified {@link Image} and uses that
	 * region to replace the current one.
	 * 
	 * @param image
	 *            The new {@link Image} of the {@link Clickable}.
	 */
	private void swapRegion(Image image) {

		// Holds the current position of the region.
		final Point current = getPosition();

		// Generate the new region and return it to the current position.
		this.region = new Region(image);
		this.setPosition(current);

	}

}
