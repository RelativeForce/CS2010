package peril.views.slick.components;

import org.newdawn.slick.Graphics;

import peril.views.slick.Frame;
import peril.views.slick.states.InteractiveState;
import peril.views.slick.util.Point;

/**
 * Defines the default behaviour of a visual component that cane be used in an
 * {@link InteractiveState}.
 * 
 * @author Joshua_Eddy
 *
 */
public interface Component {

	/**
	 * Initialises this {@link Component}s visual elements. This must be executed
	 * before it is used.
	 */
	void init();

	/**
	 * Draws this {@link Component} on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	void draw(Frame frame);

	/**
	 * Changes the {@link Point} position of the {@link Component} to the one
	 * specified.
	 * 
	 * @param position
	 *            {@link Point}
	 */
	void setPosition(Point position);

	/**
	 * Retrieves the {@link Point} position of this {@link Component}.
	 * 
	 * @return {@link Point}
	 */
	Point getPosition();
}
