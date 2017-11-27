package peril.ui.components;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Point;
import peril.ui.Clickable;
import peril.ui.Font;
import peril.ui.Region;

/**
 * This object is a visual component that can be used to display a block of text
 * on the screen.
 * 
 * @author Joshua_Eddy
 *
 */
public final class TextField extends Clickable implements Component {

	/**
	 * {@link List} of the lines in this {@link TextField}.
	 */
	private List<String> lines;

	/**
	 * The {@link Font} of this {@link TextField}.
	 */
	private Font font;

	/**
	 * Constructs a new {@link TextField}.
	 * 
	 * @param width
	 *            of the {@link TextField}.
	 * @param height
	 *            of the {@link TextField}.
	 * @param position
	 *            {@link Point} of the {@link TextField}.
	 */
	public TextField(int width, int height, Point position) {
		super(new Region(width, height, position));
		font = new Font("Arial", Color.black, 10);
		lines = new ArrayList<>();
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

		// Separate the text by word
		final String[] words = text.split(lineWrap);

		// The maximum width of a line in the text field
		final int maxWidth = this.getWidth() - 30;

		// Holds the line currently being created.
		StringBuilder line = new StringBuilder();

		// Iterate through all the words in the text the user added. Start a new line if
		// the length of the line is larger that the max width of the line.
		for (String word : words) {

			// Holds the current line if the current word was added.
			final String testLine = line.toString() + " " + word;

			// If the line with the word added is larger than max width end the current line
			// and and start the next.
			if (font.getWidth(testLine) >= maxWidth) {

				lines.add(line.toString());

				line = new StringBuilder();

			}

			if (!line.toString().isEmpty()) {
				line.append(' ');
			}

			line.append(word);
		}

		// Add the last line to the list of lines.
		lines.add(line.toString());

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
	public void draw(Graphics g) {

		g.setColor(Color.gray);
		g.fillRect(getPosition().x, getPosition().y, getWidth(), getHeight());

		int x = getPosition().x + 15;
		int y = getPosition().y + 15;
		int numberOfLines = (getHeight() - 30) / font.getHeight();

		for (int index = 0; index < (lines.size() > numberOfLines ? numberOfLines : lines.size()); index++) {
			font.draw(g, lines.get(index), x, y);
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

}
