package peril.views.slick.components.menus;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Game;
import peril.controllers.GameController;
import peril.io.MapWriter;
import peril.io.SaveFile;
import peril.views.slick.Button;
import peril.views.slick.Font;
import peril.views.slick.Frame;
import peril.views.slick.Point;
import peril.views.slick.Region;
import peril.views.slick.components.lists.VisualList;

/**
 * Encapsulates the behaviour of a Pause Menu
 * 
 * @author Joseph Rolli
 *
 */
public class PointsMenu extends Menu {

	/**
	 * Holds the name of this {@link PointsMenu}.
	 */
	public final static String NAME = "Points Menu";


	/**
	 * The {@link Font} for the text of the text of the {@link PointsMenu}.
	 */
	private final Font textFont;

	/**
	 * Constructs a new {@link PointsMenu}.
	 * 
	 * @param position
	 *            {@link Point} position of the {@link PointsMenu}.
	 * @param game
	 *            The {@link Game} the {@link PointsMenu} is associated with.
	 */
	public PointsMenu(Point position, GameController game) {
		super(NAME, game, new Region(600, 600, position));

		final Font toggleFont = new Font("Arial", Color.black, 20);
		
		this.textFont = new Font("Arial", Color.black, 20);

	}

	/**
	 * Sets this {@link PointsMenu} a visible.
	 */
	@Override
	public void show() {
		super.show();
	}

	/**
	 * Draws the {@link PointsMenu} on screen.
	 * 
	 * @param f
	 *            {@link Graphics}
	 */
	public void draw(Frame f) {

		super.draw(f);

		if (isVisible()) {

			f.setColor(Color.white);

		}

	}

	/**
	 * Initialises all the visual elements off {@link PointsMenu}.
	 */
	public void init() {

		textFont.init();

	}

	/**
	 * Process a click.
	 */
	public void parseClick(Point click) {


		}


	/**
	 * Moves all the components in this {@link PointsMenu}.
	 */
	@Override
	public void moveComponents(Point vector) {

	}

}
