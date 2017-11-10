package peril.ui.visual;

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
	 * Constructs the new {@link Font}.
	 * 
	 * @param fontName
	 *            The name of the {@link Font}.
	 * @param color
	 *            The {@link Color} of the {@link Font}.
	 * @param size
	 *            The <code>int</code> size of the {@link Font}. Seems to break over
	 *            56
	 */
	public Font(String fontName, Color color, int size) {
		this.color = color;
		java.awt.Font font = new java.awt.Font(fontName, java.awt.Font.PLAIN, size);
		ttFont = new TrueTypeFont(font, false);
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

}
