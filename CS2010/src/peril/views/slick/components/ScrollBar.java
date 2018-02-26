package peril.views.slick.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.views.slick.Frame;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

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

	private boolean numbers;

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
		this.index = 1;
		this.spacing = getHeight() / (maxValue);
		this.slider = new Slider(new Point(position.x, position.y), width, spacing);
		this.numbers = true;
	}

	/**
	 * Draws the {@link ScrollBar} on the screen.
	 * 
	 * @param g
	 *            {@link Graphics}.
	 */
	public void draw(Frame frame) {
		if (visible) {
			frame.setColor(Color.darkGray);
			frame.fillRect(getPosition().x, getPosition().y, getWidth(), getHeight());
			drawNumbers(frame);
			slider.draw(frame);
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
	public void click(Point clickPosition) {
		if (isClicked(clickPosition)) {
			moveSlider(clickPosition);
		}
	}

	public void showNumbers() {
		this.numbers = true;
	}

	public void hideNumbers() {
		this.numbers = false;
	}

	public void up() {
		if(index > 0) {
			setIndex(index - 1);
		}
	}

	public void down() {
		if(index < maxValue) {
			setIndex(index + 1);
		}
	}

	public int getIndex() {
		return index;
	}

	/**
	 * Moves the slider of the {@link ScrollBar} along the Bar, rounding down an
	 * index.
	 */
	public void moveSlider(Point clickPosition) {

		final int newPosition = clickPosition.y;
		final int space = Math.floorDiv(newPosition, spacing);

		setIndex(space);

	}

	private void setIndex(int space) {
		slider.setPosition(new Point(slider.getPosition().x, space * spacing));
		index = 1 + Math.floorDiv((slider.getPosition().y - this.getPosition().y), spacing);
	}

	/**
	 * Draws the number on the {@link ScrollBar} to indicate the values on specific
	 * points.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawNumbers(Frame frame) {

		if(numbers) {
			return;
		}
		
		for (int i = 0; i <= maxValue - 1; i++) {

			frame.draw(textFont, String.valueOf(i + 1), getPosition().x + getWidth(), (spacing * i) + getPosition().y);

		}

	}

	private class Slider extends Clickable {

		private Slider(Point position, int width, int height) {

			super(new Region(width, height, position));
		}

		public void draw(Frame frame) {
			frame.setColor(Color.lightGray);
			frame.fillRect(this.getPosition().x, this.getPosition().y, this.getWidth(), this.getHeight());
		}
	}

}
