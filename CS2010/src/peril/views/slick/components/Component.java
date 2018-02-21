package peril.views.slick.components;

import peril.views.slick.Frame;
import peril.views.slick.util.Point;

/**
 * Defines the default behaviour of a visual component that can be initialised
 * and drawn.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-19
 * @version 1.01.01
 * 
 * @see Frame
 * @see Point
 *
 */
public interface Component {

	/**
	 * Initialises this {@link Component}s visual elements. This must be executed
	 * before it is drawn using {@link #draw(Frame)}.
	 */
	void init();

	/**
	 * Draws this {@link Component} on screen. {@link #init()} must be called before
	 * this.
	 * 
	 * @param frame
	 *            The {@link Frame} used to draw the {@link Component} on screen.
	 */
	void draw(Frame frame);

	/**
	 * Changes the {@link Point} position of the {@link Component} to the one
	 * specified.
	 * 
	 * @param position
	 *            The {@link Point} position of this {@link Component}.
	 */
	void setPosition(Point position);

	/**
	 * Retrieves the {@link Point} position of this {@link Component}.
	 * 
	 * @return The new {@link Point} position of the {@link Component}.
	 */
	Point getPosition();
}
