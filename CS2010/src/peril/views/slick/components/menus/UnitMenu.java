package peril.views.slick.components.menus;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Game;
import peril.controllers.GameController;
import peril.io.MapWriter;
import peril.io.SaveFile;
import peril.views.slick.Frame;
import peril.views.slick.components.VisualList;
import peril.views.slick.util.Button;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * Encapsulates the behaviour of a Pause Menu
 * 
 * @author Joseph Rolli
 *
 */
public class UnitMenu extends Menu {

	/**
	 * Holds the name of this {@link UnitMenu}.
	 */
	public final static String NAME = "Unit Menu";


	/**
	 * The {@link Font} for the text of the text of the {@link UnitMenu}.
	 */
	private final Font textFont;

	/**
	 * Constructs a new {@link UnitMenu}.
	 * 
	 * @param position
	 *            {@link Point} position of the {@link UnitMenu}.
	 * @param game
	 *            The {@link Game} the {@link UnitMenu} is associated with.
	 */
	public UnitMenu(Point position, GameController game) {
		super(NAME, game, new Region(600, 600, position));

		final Font toggleFont = new Font("Arial", Color.black, 20);
		
		this.textFont = new Font("Arial", Color.black, 20);

	}

	/**
	 * Sets this {@link UnitMenu} a visible.
	 */
	@Override
	public void show() {
		super.show();
	}

	/**
	 * Draws the {@link UnitMenu} on screen.
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
	 * Initialises all the visual elements off {@link UnitMenu}.
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
	 * Moves all the components in this {@link UnitMenu}.
	 */
	@Override
	public void moveComponents(Point vector) {

	}

}
