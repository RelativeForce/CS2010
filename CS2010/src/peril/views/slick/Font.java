package peril.views.slick;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Encapsulates a font on the screen.
 * 
 * @author Joshua_Eddy
 *
 */
public class Font {

	/**
	 * The {@link TrueTypeFont} for this {@link Font}.
	 */
	private TrueTypeFont ttFont;

	/**
	 * The {@link Color} of the {@link Font}.
	 */
	private Color color;

	/**
	 * Holds the font size of this {@link Font}.
	 */
	private final int fontSize;

	/**
	 * Holds the font name for this {@link Font}.
	 */
	private final String fontName;

	/**
	 * Whether this {@link Font} has been initialised or not.
	 */
	private boolean initialised;

	/**
	 * Constructs the new {@link Font}.
	 * 
	 * @param fontName
	 *            The name of the {@link Font}.
	 * @param color
	 *            The {@link Color} of the {@link Font}.
	 * @param fontSize
	 *            The <code>int</code> size of the {@link Font}. Seems to break over
	 *            56
	 */
	public Font(String fontName, Color color, int fontSize) {
		this.color = color;
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.initialised = false;

	}

	/**
	 * Initialises this {@link Font}.
	 */
	public void init() {

		if (!initialised) {
			java.awt.Font font = new java.awt.Font(fontName, java.awt.Font.PLAIN, fontSize);
			ttFont = new TrueTypeFont(font, false);
			initialised = true;
		}

	}

	/**
	 * Draws the text in the {@link Font} on the {@link Graphics} and then reverts
	 * back.
	 * 
	 * @param g
	 *            {@link Graphics} of the screen.
	 * @param text
	 *            The text to be written.
	 * @param x
	 *            The x coordinate of the text on the screen.
	 * @param y
	 *            The y coordinate of the text on the screen.
	 */
	public void draw(Graphics g, String text, float x, float y) {

		Color originalColour = g.getColor();
		org.newdawn.slick.Font originalFont = g.getFont();

		// Set the new font and color.
		g.setColor(color);
		g.setFont(ttFont);

		// Draw the text
		g.drawString(text, x, y);

		// Revert the font an color back to original font and color.
		g.setColor(originalColour);
		g.setFont(originalFont);

	}

	/**
	 * Retrieves whether this {@link Font} has been initialised or not.
	 * 
	 * @return boolean
	 */
	public boolean isInitialised() {
		return initialised;
	}

	/**
	 * Retrieves the <code>int</code> width of a specified string.
	 * 
	 * @param text
	 * @return
	 */
	public int getWidth(String text) {
		return ttFont.getWidth(text);
	}

	/**
	 * Retrieves the line height of this {@link Font}.
	 * 
	 * @return
	 */
	public int getHeight() {
		return ttFont.getHeight();
	}

	/**
	 * Assigns new {@link Color} to this {@link Font}.
	 * 
	 * @param color
	 *            {@link Color}
	 */
	public void setColor(Color color) {
		if (color == null) {
			throw new NullPointerException("Color cannot be null.");
		}
		this.color = color;
	}

}
