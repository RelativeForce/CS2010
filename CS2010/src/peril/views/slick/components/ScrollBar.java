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
 * @since 2018-02-27
 * @version 1.02.02
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
	private final float spacing;

	/**
	 * Whether or not this {@link ScrollBar} is visible or not.
	 */
	private boolean visible;

	/**
	 * Whether or not the numbers are displayed next to the {@link ScrollBar}.
	 */
	private boolean showNumbers;

	/**
	 * The current index of the {@link ScrollBar}.
	 */
	private int index;

	/**
	 * Constructs a new {@link ScrollBar}.
	 * 
	 * @param position
	 *            The {@link Point} position of the {@link ScrollBar}.
	 * @param width
	 *            The width of the {@link ScrollBar}.
	 * @param height
	 *            The height of the {@link ScrollBar}.
	 * @param maxValue
	 *            The maximum index for the {@link ScrollBar}.
	 */
	public ScrollBar(Point position, int width, int height, int maxValue) {
		super(new Region(width, height, position));

		this.visible = true;
		this.textFont = new Font("Arial", Color.white, 12);
		this.maxValue = maxValue;
		this.index = 0;
		this.spacing = (float) this.getHeight() / (maxValue + 1);
		this.slider = new Slider(new Point(position.x, position.y), width, (int) spacing);
		this.showNumbers = true;
	}

	/**
	 * Draws the {@link ScrollBar} on the screen.
	 */
	public void draw(Frame frame) {
		if (visible) {

			frame.setColor(Color.darkGray);

			final int width = (getWidth() / 5);
			final int height = getHeight() - (int) spacing;
			final int x = getPosition().x + (getWidth() / 2) - (width / 2);
			final int y = getPosition().y + (int) (spacing / 2);

			frame.fillRect(x, y, width, height);

			drawNumbers(frame);
			slider.draw(frame);
		}
	}

	/**
	 * Initialises all visual elements of the {@link ScrollBar}.
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

			// Mouse position relative to start of scroll bar.
			final int y = mouse.y - getPosition().y;

			final int index = Math.floorDiv(y, (int) spacing);

			setIndex(index);
		}
	}

	/**
	 * Sets the number as visible for the {@link ScrollBar}.
	 */
	public void showNumbers() {
		this.showNumbers = true;
	}

	/**
	 * Sets the number as invisible for the {@link ScrollBar}.
	 */
	public void hideNumbers() {
		this.showNumbers = false;
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
	 *            The new index of the {@link ScrollBar}.
	 */
	public void setIndex(int newIndex) {

		// If the new index is valid.
		if (newIndex >= 0 && newIndex <= maxValue) {

			final int x = slider.getPosition().x;
			final int y = this.getPosition().y + (int) (spacing * newIndex);

			this.slider.setPosition(new Point(x, y));
			this.index = newIndex;
		}

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

		// If the number are invisible don't proceed.
		if (!showNumbers) {
			return;
		}

		// For each index draw its number on screen.
		for (int index = 0; index < maxValue; index++) {

			final int x = getPosition().x + getWidth();
			final int y = (int) (index * spacing) + getPosition().y;

			frame.draw(textFont, String.valueOf(index + 1), x, y);

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
	 * @see ScrollBar
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
