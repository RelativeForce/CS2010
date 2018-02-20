package peril.views.slick.components.menus;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Game;
import peril.controllers.GameController;
import peril.model.ModelPlayer;
import peril.model.board.ModelCountry;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.components.TextField;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * Encapsulates the behaviour of a window that displays information to the user.

 * 
 * @author Joseph_Rolli
 * 
 * @since 2018-02-20
 * @version 1.01.02
 *
 */
public class StatsMenu extends Menu {

	/**
	 * The uniquely identifying string name of the {@link StatsMenu}.
	 */
	public final static String NAME = "Stats Menu";

	/**
	 * The width of the {@link StatsMenu}
	 */
	private static final int WIDTH = 600;

	/**
	 * The height of the {@link StatsMenu}.
	 */
	private static final int HEIGHT = 600;

	/**
	 * The padding in the horizontal direction between the edge of the
	 * {@link StatsMenu} and the edge of the {@link TextField}.
	 */
	private static final int PADDING_X = WIDTH / 12;

	/**
	 * The padding in the vertical direction between the edge of the
	 * {@link StatsMenu} and the edge of the {@link TextField}.
	 */
	private static final int PADDING_Y = HEIGHT / 10;

	/**
	 * The {@link Font} for the text of the text of the {@link StatsMenu}.
	 */
	private final Font textFont;

	
	/**
	 * The {@link ModelPlayer} that is currently active.
	 */
	private ModelPlayer currentPlayer;
	
	
	/**
	 * Constructs a new {@link StatsMenu}.
	 * 
	 * @param position
	 *            {@link Point} position of the {@link StatsMenu}.
	 * @param game
	 *            The {@link Game} the {@link StatsMenu} is associated with.
	 */
	public StatsMenu(Point position, GameController game) {
		super(NAME, game, new Region(600, 600, position));

		final Font toggleFont = new Font("Arial", Color.black, 20);

		this.textFont = new Font("Arial", Color.black, 20);


	}

	/**
	 * Initialises all the visual elements off {@link StatsMenu}.
	 */
	public void init() {

		textFont.init();

	}
	
	/**
	 * Sets this {@link StatsMenu} as visible.
	 */
	@Override
	public void show() {
		super.show();
		
		currentPlayer = game.getCurrentModelPlayer();
		
	}

	/**
	 * Draws thus {@link StatsMenu} on screen. If this {@link StatsMenu} is hidden,
	 * this with do nothing.
	 * 
	 * @param f
	 *            {@link Frame}
	 */
	public void draw(Frame f) {
		if (!isVisible())
			return;	
		super.draw(f);


	}



	/**
	 * Process a click.
	 */
	public void parseClick(Point click) {

	}

	/**
	 * Moves all the components in this {@link StatsMenu}.
	 */
	@Override
	public void moveComponents(Point vector) {

	}
	
	

}
