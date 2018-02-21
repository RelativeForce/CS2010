package peril.views.slick;

import org.newdawn.slick.Input;

import peril.views.slick.util.Point;

/**
 * Defines the behaviours of an object that when drawn on the {@link Frame} has
 * specific actions to be performed when a certain event occurs to it.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-16
 * @version 1.01.01
 * 
 * @see Point
 * @see Frame
 *
 */
public interface EventListener {

	/**
	 * Process a mouse button click at a specified {@link Point} position on screen.
	 * 
	 * @param mouse
	 *            The mouse {@link Point} position.
	 * @param mouseButton
	 *            The mouse button that was clicked {@link Input}.
	 */
	void mouseClick(Point mouse, int mouseButton);

	/**
	 * Process the mouse hovering at a certain {@link Point} position on screen.
	 * 
	 * @param mouse
	 *            The mouse position {@link Point}.0
	 * @param delta
	 *            The amount of milliseconds that have elapsed at that position.
	 */
	void mouseHover(Point mouse, int delta);

	/**
	 * Processes a button press while the mouse is at the specified position on
	 * screen.
	 * 
	 * @param key
	 *            The key number
	 * @param mouse
	 *            The mouse {@link Point}
	 * 
	 * @see Input
	 */
	void buttonPress(int key, Point mouse);

	/**
	 * Draws the sub elements of the parent object that this {@link EventListener}
	 * is coupled to.
	 * 
	 * @param frame
	 *            The {@link Frame} the elements will be drawn on.
	 */
	void draw(Frame frame);

}
