package peril.ui.components;

import org.newdawn.slick.Graphics;

import peril.ui.states.InteractiveState;

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
	void draw(Graphics g);

}
