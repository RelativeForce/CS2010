package peril.ui.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Game;
import peril.Point;
import peril.ui.Clickable;
import peril.ui.Font;
import peril.ui.Region;

/**
 * Encapsulates the behaviour of a ScrollBar
 * 
 * @author Ezekiel_Trinidad
 */
public class ScrollBar extends Clickable implements Component{

	/**
	 * The {@link Game} this ScrollBar is part of.
	 */
	private Game game;

	/**
	 * The Name of the {@link ScrollBar}.
	 */
	private String name;

	private Slider slider;
	
	/**
	 * Whether or not this {@link ScrollBar} is visible or not.
	 */
	private boolean visible;

	/**
	 * The {@link Font} used for the text labelling the {@link ScrollBar}.
	 */
	private Font textFont;

	/**
	 * Constructs a {@link ScrollBar}.
	 * 
	 * @param name
	 *            The name of the {@link ScrollBar}.
	 * @param position
	 *            The {@link Point} position of the {@link ScrollBar}.
	 * @param width
	 *            The width of the {@link ScrollBar}.
	 * @param height
	 *            The width of the {@link ScrollBar}.
	 * @param game
	 *            The {@link Game} the {@link ScrollBar} is associated with.
	 */
	public ScrollBar(String name, Point position, int width, int height, Game game) {
		super(new Region(width, height, position));
		this.game = game;
		this.name = name;
		this.visible = false;
		this.textFont = new Font("Arial", Color.white, 12);
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

			String unitsLabel = "Units to Attack:";

			textFont.draw(g, unitsLabel, (getPosition().x - textFont.getWidth(unitsLabel)), (getPosition().y + 10));
			drawNumbers(g);
			drawSlider(g);
		}
	}

	/**
	 * Initialises all Visual Elements of the {@link ScrollBar}.
	 */
	public void init() {
		textFont.init();
	}

	/**
	 * 
	 * 
	 * @param oldPosition
	 * @param newPosition
	 */
	public void processMouseDrag(Point oldPosition, Point newPosition) {
		
	}
	
	/**
	 * Moves the slider of the {@link ScrollBar} along the Bar
	 */
	public void moveSlider(Point click) {
		
	}

	/**
	 * Returns the width of the {@link ScrollBar}.
	 * 
	 * @return the <code>int</code> width value of the {@link ScrollBar}.
	 */
	private int getBarWidth() {
		//used as a getter method for the to Construct the Slider
		return getWidth();
	}
	
	/**
	 * Returns the height of the {@link ScrollBar}.
	 * 
	 * @return the <code>int</code> height value of the {@link ScrollBar}.
	 */
	private int getBarHeight() {
		return getHeight();
	}
	
	/**
	 * Returns the position of the {@link ScrollBar}.
	 * 
	 * @return
	 */
	private Point getBarPosition() {
		return getPosition();
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
		for (int i = 1; i <= 5; i++) {
			textFont.draw(g, String.valueOf(i), xLabelPosition, getPosition().y + getHeight());
			// places a number every 1/5th of the ScrollBar
			xLabelPosition += getWidth() / 5;
			
		}
	}

	/**
	 * Draws the slider for the {@link ScrollBar}.
	 * 
	 * @param g
	 */
	private void drawSlider(Graphics g) {
		g.setColor(Color.lightGray);
		g.fillRect(getPosition().x, getPosition().y, getWidth() / 10, getHeight());

	}
	
	private class Slider extends Clickable{
		
		private Slider(Graphics g) {
			//this is wrong I know
			super(new Region(getBarWidth(), getBarHeight(), getBarPosition()));
		}
		
		public void testMethod() {}
	}

}
