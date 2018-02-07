package peril.views.slick.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.views.slick.Clickable;
import peril.views.slick.Font;
import peril.views.slick.Point;
import peril.views.slick.Region;

/**
 * Encapsulates the behaviour of a ScrollBar
 * 
 * @author Ezekiel_Trinidad, Joshua_Eddy, Joseph Rolli
 */
public class ScrollBar extends Clickable implements Component {

	private Slider slider;

	/**
	 * Whether or not this {@link ScrollBar} is visible or not.
	 */
	private boolean visible;

	private int index;

	private int maxValue;

	private int spacing;

	/**
	 * The {@link Font} used for the text labelling the {@link ScrollBar}.
	 */
	private Font textFont;

	/**
	 * Constructs a {@link ScrollBar}.
	 * 
	 * @param position
	 *            The {@link Point} position of the {@link ScrollBar}.
	 * @param width
	 *            The width of the {@link ScrollBar}.
	 * @param height
	 *            The height of the {@link ScrollBar}.
	 */
	public ScrollBar(Point position, int width, int height, int maxValue) {
		super(new Region(width, height, position));
		this.visible = true;
		this.textFont = new Font("Arial", Color.white, 12);
		this.maxValue = maxValue;
		index = 1;
		spacing = getHeight() / (maxValue);
		slider = new Slider(new Point(position.x, position.y), width, spacing);
	}

	/**
	 * Draws the {@link ScrollBar} on the screen.
	 * 
	 * @param g
	 *            {@link Graphics}.
	 */
	public void draw(Graphics g) {
		if (visible) {
			g.setColor(Color.darkGray);
			g.fillRect(getPosition().x, getPosition().y, getWidth(), getHeight());
			drawNumbers(g);
			slider.draw(g);
		}
	}

	/**
	 * Initialises all Visual Elements of the {@link ScrollBar}.
	 */
	public void init() {
		textFont.init();
	}

	/**
	 * Moves the slider when the mouse is dragged.
	 * 
	 * @param oldPosition
	 *            {@link Point} old position of the mouse.
	 * @param newPosition
	 * 
	 */
	public void processMouseClick(Point clickPosition) {
		// check that when starting to drag it starts at the oldPosition
		if (isClicked(clickPosition)) {
			// Test if its being clicked or not
			moveSlider(clickPosition);
			System.out.println("THE SLIDER HAS BEEN CLICKED");
			System.out.println("SliderY:" + slider.getPosition().y + "Index:" + index);
		}
	}

	public int getIndex() {
		return index;
	}

	/**
	 * Moves the slider of the {@link ScrollBar} along the Bar, rounding down an index.
	 */
	public void moveSlider(Point clickPosition) {
		int newPosition = clickPosition.y;
		int space = Math.floorDiv(newPosition, spacing);
		slider.setPosition(new Point(slider.getPosition().x, space*spacing));
		index = 1 + Math.floorDiv((slider.getPosition().y-getPosition().y), spacing);

	}

	/**
	 * Draws the number on the {@link ScrollBar} to indicate the values on specific
	 * points.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawNumbers(Graphics g) {

		float yLabelPosition = getPosition().y;

		for (int i = 0; i <= maxValue-1; i++) {

			textFont.draw(g, String.valueOf(i+1),getPosition().x + getWidth(), 
					(spacing * i)+getPosition().y);
			yLabelPosition += spacing;

		}

	}

	private class Slider extends Clickable {

		private Slider(Point position, int width, int height) {

			super(new Region(width, height, position));
		}

		public void draw(Graphics g) {
			g.setColor(Color.lightGray);
			g.fillRect(this.getPosition().x, this.getPosition().y, this.getWidth(), this.getHeight());
		}
	}

}
