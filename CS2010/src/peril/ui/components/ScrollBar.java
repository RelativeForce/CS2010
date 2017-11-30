package peril.ui.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Point;
import peril.ui.Clickable;
import peril.ui.Font;
import peril.ui.Region;

/**
 * Encapsulates the behaviour of a ScrollBar
 * 
 * @author Ezekiel_Trinidad, Joshua_Eddy
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
	 *            The width of the {@link ScrollBar}.
	 */
	public ScrollBar(Point position, int width, int height, int maxValue) {
		super(new Region(width, height, position));
		this.visible = true;
		this.textFont = new Font("Arial", Color.white, 12);
		this.maxValue = maxValue;
		index = 0;
		spacing = getWidth() / (maxValue);
		slider = new Slider(new Point(position.x, position.y), spacing, height);
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
	public void processMouseDrag(Point newPosition) {
		// check that when starting to drag it starts at the oldPosition
		if (isClicked(newPosition)) {
			// Test if its being clicked or not
			System.out.println("THE SLIDER IS BEING Dragged");

			int change = (newPosition.x - slider.getPosition().x) / slider.getWidth();
			moveSlider(change);
		}
	}

	public int getIndex() {
		return index;
	}

	/**
	 * Moves the slider of the {@link ScrollBar} along the Bar
	 */
	public void moveSlider(int change) {
		// moves to the right
		if (change > 0) {

			if ((index + change) < maxValue) {
				index += change;
				slider.setPosition(
						new Point(slider.getPosition().x + (slider.getWidth() * change), slider.getPosition().y));
			} else {
				index = maxValue - 1;
				slider.setPosition(
						new Point(this.getPosition().x + this.getWidth() - slider.getWidth(), slider.getPosition().y));
			}
		}
		// moves to the left
		else if (change < 0) {

			if ((index - change) > 0) {
				index -= change;
				slider.setPosition(
						new Point(slider.getPosition().x + (slider.getWidth() * change), slider.getPosition().y));

			} else {
				index = 0;
				slider.setPosition(new Point(this.getPosition().x, slider.getPosition().y));
			}
		}
		// set new position of slider
	}

	/**
	 * Draws the number on the {@link ScrollBar} to indicate the values on specific
	 * points.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawNumbers(Graphics g) {

		float xLabelPosition = getPosition().x;

		for (int i = 1; i <= maxValue; i++) {

			textFont.draw(g, String.valueOf(i), xLabelPosition - (textFont.getWidth(String.valueOf(i)) / 2),
					getPosition().y + getHeight());
			xLabelPosition += spacing;

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
