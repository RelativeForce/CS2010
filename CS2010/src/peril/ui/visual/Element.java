package peril.ui.visual;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Point;

/**
 * 
 * An element of the {@link VisualList} that can be displayed on screen using
 * {@link Element#draw(Graphics, Font)}.
 * 
 * @author Joshua_Eddy
 *
 * @param <T>
 *            The type of the {@link Element#payload}
 */
public class Element<T> extends Clickable {

	/**
	 * The {@link T} that will be returned if this {@link Element} is selected.
	 */
	private T payload;

	/**
	 * The text representation of the {@link Element}.
	 */
	private String text;

	/**
	 * Constructs a new {@link Element}.
	 * 
	 * @param payload
	 *            The {@link T} that will be returned if this {@link Element} is
	 *            selected.
	 * @param text
	 *            The text representation of the {@link Element}.
	 */
	public Element(String text, T payload) {
		super();
		this.payload = payload;
		this.text = text;
	}

	/**
	 * Retrieves the {@link Element#payload}.
	 * 
	 * @return {@link T}
	 */
	public T get() {
		return payload;
	}

	/**
	 * Initialises the {@link Element}.
	 * 
	 * @param x
	 *            <code>int</code> x coordinate of the {@link Element} on screen.
	 * @param y
	 *            <code>int</code> y coordinate of the {@link Element} on screen.
	 * @param width
	 *            <code>int</code> width of the {@link Element} on screen.
	 * @param height
	 *            <code>int</code> height of the {@link Element} on screen.
	 */
	public void init(int x, int y, int width, int height) {
		setRegion(new Region(width, height, new Point(x, y)));
		setImage(getRegion().getPosition(), getRegion().convert(Color.yellow));
	}

	/**
	 * Draws the name of the {@link Element} on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 * @param font
	 *            {@link Font}
	 * @param padding
	 *            The number of pixels to the left of this {@link Element}s
	 *            {@link Point} position this will be displayed.
	 */
	public void draw(Graphics g, Font font, int padding) {
		font.draw(g, text, getPosition().x + padding, getPosition().y);
	}
}
