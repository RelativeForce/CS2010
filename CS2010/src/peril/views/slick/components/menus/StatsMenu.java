package peril.views.slick.components.menus;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Game;
import peril.controllers.GameController;
import peril.model.ModelPlayer;
import peril.views.slick.Frame;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * Encapsulates the behaviour of a window that displays information to the user.
 * 
 * 
 * @author Joseph_Rolli
 * 
 * @since 2018-02-20
 * @version 1.01.04
 *
 */
public class StatsMenu extends Menu {

	/**
	 * The uniquely identifying string name of the {@link StatsMenu}.
	 */
	public final static String NAME = "Stats Menu";

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
	 * Draws the current player stats on the {@link StatsMenu}.
	 * 
	 * @param frame
	 *            {@link Graphics}
	 */
	private void drawStats(Frame frame) {
		String countriesRuled = "Countries owned: " + currentPlayer.getCountriesRuled();
		String armyStrength = "Total army strength: " + currentPlayer.getTotalArmyStrength();
		String continentsRuled = "Continents owned: " + currentPlayer.getContinentsRuled();
		String armiesDestroyed = "Armies destroyed: " + currentPlayer.getArmiesDestroyed();
		String pointsSpent = "Points spent: " + currentPlayer.getPointsSpent();
		String countriesTaken = "Countries taken: " + currentPlayer.getCountriesTaken();

		frame.draw(textFont, countriesRuled, getPosition().x + 100, getPosition().y + 150);
		frame.draw(textFont, armyStrength, getPosition().x + 100, getPosition().y + 200);
		frame.draw(textFont, continentsRuled, getPosition().x + 100, getPosition().y + 250);
		frame.draw(textFont, armiesDestroyed, getPosition().x + 100, getPosition().y + 300);
		frame.draw(textFont, pointsSpent, getPosition().x + 100, getPosition().y + 350);
		frame.draw(textFont, countriesTaken, getPosition().x + 100, getPosition().y + 400);

	}

	/**
	 * Draws thus {@link StatsMenu} on screen. If this {@link StatsMenu} is hidden,
	 * this with do nothing.
	 * 
	 * @param f
	 *            {@link Frame}
	 */
	public void draw(Frame frame) {
		if (!isVisible())
			return;
		super.draw(frame);
		drawStats(frame);

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
