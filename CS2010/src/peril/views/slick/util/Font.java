package peril.views.slick.util;

import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Encapsulates the behaviours of a font that can be used to display text on
 * screen. This font must be initialised using {@link Font#init()}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-16
 * @version 1.01.01
 * 
 * @see Color
 * @see UnicodeFont
 * @see Graphics
 *
 */
public final class Font {

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
	 * The {@link UnicodeFont} for this {@link Font}.
	 */
	private UnicodeFont uFont;

	/**
	 * The {@link Color} of the {@link Font}.
	 */
	private Color color;

	/**
	 * Constructs the new {@link Font}.
	 * 
	 * @param fontName
	 *            The name of the {@link Font}.
	 * @param color
	 *            The {@link Color} of the {@link Font}.
	 * @param fontSize
	 *            The <code>int</code> size of the {@link Font}.
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
	@SuppressWarnings("unchecked")
	public void init() {

		// If the font is not already.
		if (!initialised) {

			// Create a AWT font from the parameters the user defined.
			final java.awt.Font font = new java.awt.Font(fontName, java.awt.Font.PLAIN, fontSize);

			// Use the AWT font to construct a unicode font.
			uFont = new UnicodeFont(font);

			// The colour effect that makes the font visible.
			final ColorEffect effect = new ColorEffect(new java.awt.Color(color.r, color.g, color.b));
			uFont.getEffects().add(effect);

			// Load the glyphs from the default location.
			try {
				uFont.addAsciiGlyphs();
				uFont.loadGlyphs();
			} catch (SlickException e) {
				e.printStackTrace();
				throw new IllegalStateException("Font Exception - Failed to load glyphs from memory.");
			}

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

		// Store the current colour and font of the graphics
		final Color originalColour = g.getColor();
		final org.newdawn.slick.Font originalFont = g.getFont();

		// Set the new font and colour.
		g.setColor(color);
		g.setFont(uFont);

		// Draw the text
		g.drawString(text, x, y);

		// Revert the font an colour back to original font and colour.
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
	 *            The string of text that will have its width measured.
	 * @return The width of the specified text.
	 */
	public int getWidth(String text) {
		return uFont.getWidth(text);
	}

	/**
	 * Retrieves the line height of the '|' in this {@link Font}. For a more
	 * accurate height use {@link Font#getHeight(String)}.
	 * 
	 * @return The height of the text.
	 */
	public int getHeight() {
		return uFont.getHeight("|");
	}

	/**
	 * Retrieves the line height of this {@link Font} for a specified string.
	 * 
	 * @param text
	 *            The text that will be displayed in this {@link Font}.
	 * @return The height of the text.
	 * 
	 */
	public int getHeight(String text) {
		return uFont.getHeight(text);
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
