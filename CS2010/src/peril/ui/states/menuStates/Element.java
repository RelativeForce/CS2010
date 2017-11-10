package peril.ui.states.menuStates;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Point;
import peril.ui.states.Font;
import peril.ui.visual.Clickable;
import peril.ui.visual.Region;

public class Element<T> extends Clickable {

	/**
	 * The {@link T} that will be returned if this {@link Element} is selected.
	 */
	private T payload;

	private String text;

	/**
	 * Constructs a new {@link Element}.
	 * 
	 * @param payload
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
	 */
	public void draw(Graphics g, Font font) {
		font.draw(g, text, getPosition().x, getPosition().y);
	}
}
