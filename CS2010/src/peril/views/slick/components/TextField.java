package peril.views.slick.components;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.views.slick.Frame;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;

/**
 * This object is a visual component that can be used to display a block of text
 * on the screen.
 * 
 * @author Joshua_Eddy
 *
 */
public final class TextField implements Component {

	/**
	 * {@link List} of the lines in this {@link TextField}.
	 */
	private final List<String> lines;

	/**
	 * The {@link Font} of this {@link TextField}.
	 */
	private final Font font;

	/**
	 * The {@link Font} of this {@link TextField}.
	 */
	private Point position;

	/**
	 * The maximum width of this {@link TextField}.
	 */
	private final int maxWidth;

	/**
	 * The padding between the text and the board of the backing box.
	 */
	private final int padding;

	/**
	 * The actual width of the {@link TextField}.
	 */
	private int width;

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

	}

	/**
	 * Adds a section of text to a next line in this {@link TextField}. If the text
	 * is longer than the maximum value it will wrap onto the following line using
	 * at the SPACE character. {@link TextField#init()} must be called before this
	 * method.
	 * 
	 * @param text
	 *            to be added to the {@link TextField}
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
	 *            to be added to the {@link TextField}
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
		;

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
	 * @param g
	 *            {@link Graphics}
	 */
	public void draw(Frame frame) {

		int y = position.y + padding;
		final int x = position.x + padding;
		final int height = getHeight();

		// Draw the font on the back found.
		frame.setColor(Color.lightGray);
		frame.fillRect(position.x, position.y, width, height);

		// Draw the text on screen.
		for (int index = 0; index < lines.size(); index++) {
			frame.draw(font, lines.get(index), x, y);
			y += font.getHeight();
		}

	}

	/**
	 * Processes a mouse click at a {@link Point} position on this
	 * {@link TextField}.
	 */
	public void parseMouseClick(Point click) {

	}

	/**
	 * Process a mouse drag on this {@link TextField} from one {@link Point} to
	 * another.
	 * 
	 * @param oldPosition
	 *            {@link Point}
	 * @param newPosition
	 *            {@link Point}
	 */
	public void parseMouseDrag(Point oldPosition, Point newPosition) {

	}

	/**
	 * Assigns the new {@link Point} position of this {@link TextField}.
	 * 
	 * @param position
	 *            {@link Point}
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * Retrieves the {@link Point} position of this {@link TextField}.
	 * 
	 * @return
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
