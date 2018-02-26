package peril.views.slick.components;

import org.newdawn.slick.Color;
import peril.views.slick.Frame;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * Encapsulates the behaviour of a ScrollBar.
 * 
 * @author Ezekiel_Trinidad, Joshua_Eddy, Joseph_Rolli
 * 
 * @since 2018-02-26
 * @version 1.02.01
 * 
 * @see Clickable
 * @see Component
 */
public final class ScrollBar extends Clickable implements Component {

	/**
	 * The {@link Slider} that moves along this {@link ScrollBar}.
	 */
	private final Slider slider;

	/**
	 * The {@link Font} used for the text labelling the {@link ScrollBar}.
	 */
	private final Font textFont;

	/**
	 * The maximum index of the {@link ScrollBar}.
	 */
	private final int maxValue;

	/**
	 * The spacing between each position on the {@link ScrollBar}.
	 */
	private final int spacing;

	/**
	 * Whether or not this {@link ScrollBar} is visible or not.
	 */
	private boolean visible;

	/**
	 * Whether or not the numbers are displayed next to the {@link ScrollBar}.
	 */
	private boolean numbers;

	/**
	 * The current index of the {@link ScrollBar}.
	 */
	private int index;

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
	 * Processes a click on the {@link ScrollBar}.
	 * 
	 * @param mouse
	 *            The {@link Point} position of the mouse on screen.
	 * 
	 */
	public void click(Point mouse) {
		if (isClicked(mouse)) {

			final int newPosition = mouse.y;
			final int space = Math.floorDiv(newPosition, spacing);

			setIndex(space);
		}
	}

	/**
	 * Sets the number as visible for the {@link ScrollBar}.
	 */
	public void showNumbers() {
		this.numbers = true;
	}

	/**
	 * Sets the number as invisible for the {@link ScrollBar}.
	 */
	public void hideNumbers() {
		this.numbers = false;
	}

	/**
	 * Moves the {@link ScrollBar} up one index.
	 */
	public void up() {
		if (index > 0) {
			setIndex(index - 1);
		}
	}

	/**
	 * Moves the {@link ScrollBar} down one index.
	 */
	public void down() {
		if (index < maxValue) {
			setIndex(index + 1);
		}
	}

	/**
	 * Retrieves the current index of the {@link ScrollBar}.
	 * 
	 * @return The current index of the {@link ScrollBar}.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index of the {@link ScrollBar} based on the specified slider index.
	 * 
	 * @param newIndex
	 *            The new index.
	 */
	private void setIndex(int newIndex) {
		slider.setPosition(new Point(slider.getPosition().x, newIndex * spacing));
		index = 1 + Math.floorDiv((slider.getPosition().y - this.getPosition().y), spacing);
	}

	/**
	 * Draws the number on the {@link ScrollBar} to indicate the values on specific
	 * points.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays this {@link ScrollBar} to the
	 *            user.
	 */
	private void drawNumbers(Frame frame) {

		if (!numbers) {
			return;
		}

		for (int i = 0; i <= maxValue - 1; i++) {

			frame.draw(textFont, String.valueOf(i + 1), getPosition().x + getWidth(), (spacing * i) + getPosition().y);

		}

	}

	/**
	 * The slider that appears on the {@link ScrollBar}.
	 * 
	 * @author Ezekiel_Trinidad, Joshua_Eddy, Joseph_Rolli
	 * 
	 * @since 2018-02-26
	 * @version 1.01.01
	 * 
	 * @see Clickable
	 *
	 */
	private final class Slider extends Clickable {

		/**
		 * Constructs a new {@link Slider}.
		 * 
		 * @param position
		 *            The initial {@link Point} of the {@link Slider}.
		 * @param width
		 *            The width of the {@link Slider}.
		 * @param height
		 *            The height of the {@link Slider}.
		 */
		public Slider(Point position, int width, int height) {
			super(new Region(width, height, position));
		}

		/**
		 * Draws the {@link Slider} on screen.
		 * 
		 * @param frame
		 *            The {@link Frame} that draws the {@link Slider} on screen.
		 */
		public void draw(Frame frame) {
			frame.setColor(Color.lightGray);
			frame.fillRect(this.getPosition().x, this.getPosition().y, this.getWidth(), this.getHeight());
		}
	}

}
