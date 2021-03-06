package peril.views.slick.components;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import peril.views.slick.Frame;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;

/**
 * This object is a visual component that can be used to display a block of text
 * on the screen.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-03-14
 * @version 1.01.03
 * 
 * @see Component
 *
 */
public final class TextField implements Component {

	/**
	 * The {@link List} of the lines in this {@link TextField}.
	 */
	private final List<String> lines;

	/**
	 * The {@link Font} of this {@link TextField}.
	 */
	private final Font font;

	/**
	 * The maximum width of this {@link TextField}.
	 */
	private final int maxWidth;

	/**
	 * The padding between the text and the board of the backing box.
	 */
	private final int padding;

	/**
	 * The {@link Font} of this {@link TextField}.
	 */
	private Point position;

	/**
	 * The actual width of the {@link TextField}.
	 */
	private int width;

	/**
	 * Whether or not this {@link TextField} will draw a background box.
	 */
	private boolean drawBackground;

	/**
	 * Constructs a new {@link TextField}.
	 * 
	 * @param maxWidth
	 *            The maximum width of the {@link TextField}.
	 * @param position
	 *            The {@link Point} position of the {@link TextField}.
	 */
	public TextField(int maxWidth, Point position) {
		this.position = position;
		this.padding = 15;
		this.maxWidth = maxWidth;
		this.width = 0;
		this.font = new Font("Arial", Color.black, 25);
		this.lines = new ArrayList<>();
		this.drawBackground = true;
	}

	/**
	 * Adds a section of text to a next line in this {@link TextField}. If the text
	 * is longer than the maximum value it will wrap onto the following line using
	 * at the SPACE character. {@link TextField#init()} must be called before this
	 * method.
	 * 
	 * @param text
	 *            The text to be added to the {@link TextField}
	 */
	public void addText(String text) {
		addText(text, " ");
	}

	/**
	 * Adds a section of text to a next line in this {@link TextField}. If the text
	 * is longer than the maximum value it will wrap onto the following line using
	 * at the specified character. {@link TextField#init()} must be called before
	 * this method.
	 * 
	 * @param text
	 *            The text to be added to the {@link TextField}
	 * @param lineWrap
	 *            The character used to separate the words in the text.
	 */
	public void addText(String text, String lineWrap) {

		final int maxWidth = this.maxWidth - (2 * padding);

		// Separate the text by word
		final String[] words = text.split(lineWrap);

		// Holds the line currently being created.
		StringBuilder line = new StringBuilder();

		// Iterate through all the words in the text the user added. Start a new line if
		// the length of the line is larger that the max width of the line.
		for (String word : words) {

			// Holds the current line if the current word was added.
			final String testLine = line.toString() + lineWrap + word;

			final int lineWidth = font.getWidth(testLine);

			if (lineWidth > width) {
				width = lineWidth;
			}

			// If the line with the word added is larger than max width end the current line
			// and and start the next.
			if (lineWidth >= maxWidth) {

				lines.add(line.toString());
				line = new StringBuilder();
				width = this.maxWidth;

			}

			if (!line.toString().isEmpty()) {
				line.append(lineWrap);
			}

			line.append(word);

		}

		// Add the last line to the list of lines.
		lines.add(line.toString());

		if (width <= this.maxWidth) {
			int newWidth = width + (2 * padding);
			width = newWidth >= this.maxWidth ? this.maxWidth : newWidth;
		}

	}

	/**
	 * Clears all the lines from this {@link TextField}.
	 */
	public void clear() {
		lines.clear();
	}

	/**
	 * Initialises this {@link TextField}.
	 */
	public void init() {
		font.init();
	}

	/**
	 * Draws this {@link TextField} on the screen. {@link TextField#init()} must be
	 * called before this method.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays this {@link TextField} on screen.
	 */
	public void draw(Frame frame) {

		int y = position.y + padding;
		final int x = position.x + padding;
		final int height = getHeight();

		if (drawBackground) {
			// Draw the font on the back found.
			frame.setColor(Color.lightGray);
			frame.fillRect(position.x, position.y, width, height);
		}

		// Draw the text on screen.
		for (int index = 0; index < lines.size(); index++) {
			frame.draw(font, lines.get(index), x, y);
			y += font.getHeight();
		}

	}

	/**
	 * Shows the background box of this {@link TextField}.
	 */
	public void showBackground() {
		this.drawBackground = true;
	}

	/**
	 * Hides the background box of this {@link TextField}.
	 */
	public void hideBackground() {
		this.drawBackground = false;
	}

	/**
	 * Assigns the new {@link Point} position of this {@link TextField}.
	 * 
	 * @param position
	 *            The new {@link Point} position of the {@link TextField}.
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * Retrieves the {@link Point} position of this {@link TextField}.
	 * 
	 * @return The position of the {@link TextField}.
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * Retrieves the height of this {@link TextField}.
	 * 
	 * @return The height of the {@link TextField}.
	 */
	public int getHeight() {
		return (lines.size() * font.getHeight()) + (padding * 2);
	}
}
