package peril.views.slick.components.menus;

import org.newdawn.slick.Color;
import peril.controllers.GameController;
import peril.model.ModelPlayer;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * A {@link Menu} that displays {@link ModelPlayer}'s information to the user.
 * 
 * @author Joseph_Rolli
 * 
 * @since 2018-03-09
 * @version 1.01.07
 * 
 * @see Menu
 * @see ModelPlayer
 *
 */
public final class StatsMenu extends Menu {

	/**
	 * The name of the {@link StatsMenu}.
	 */
	public final static String NAME = "Stats Menu";

	/**
	 * The {@link Font} for the text of the text of the {@link StatsMenu}.
	 */
	private final Font textFont;

	/**
	 * The {@link SlickPlayer} that is currently active.
	 */
	private SlickPlayer currentPlayer;

	/**
	 * Constructs a new {@link StatsMenu}.
	 * 
	 * @param position
	 *            The {@link Point} position of the {@link StatsMenu}.
	 * @param game
	 *            The {@link GameController} that allows this {@link StatsMenu} to
	 *            query the state of the game.
	 */
	public StatsMenu(Point position, GameController game) {
		super(NAME, game, new Region(600, 600, position));
		this.textFont = new Font("Arial", Color.black, 35);

	}

	/**
	 * Initialises all the visual elements off {@link StatsMenu}.
	 */
	@Override
	public void init() {

		textFont.init();

	}

	/**
	 * Sets this {@link StatsMenu} as visible.
	 */
	@Override
	public void show() {
		super.show();

		currentPlayer = slick.modelView.getVisual(game.getCurrentModelPlayer());

	}

	/**
	 * Draws thus {@link StatsMenu} on screen. If this {@link StatsMenu} is hidden,
	 * this with do nothing.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link StatsMenu} to the user.
	 */
	@Override
	public void draw(Frame frame) {

		super.draw(frame);

		if (!isVisible()) {
			return;
		}

		final int x = getPosition().x + (getWidth() / 2) - (currentPlayer.getWidth() / 2);
		final int y = getPosition().y - (currentPlayer.getHeight() / 2) + 140;
		frame.draw(currentPlayer.getImage(), x, y);

		drawStats(frame);

	}

	/**
	 * Process a click at a specified {@link Point} position.
	 * 
	 * @param click
	 *            The {@link Point} position of the mouse click.
	 */
	public void parseClick(Point click) {
		// Do nothing
	}

	/**
	 * Moves all the components in this {@link StatsMenu}.
	 */
	@Override
	public void moveComponents(Point vector) {
		// Do nothing
	}

	/**
	 * Draws the current player statistics on the {@link StatsMenu}.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link StatsMenu} to the user.
	 */
	private void drawStats(Frame frame) {

		final String countriesRuled = "Countries owned: " + currentPlayer.model.getCountriesRuled();
		final String armyStrength = "Total army strength: " + currentPlayer.model.getTotalArmyStrength();
		final String unitsKilled = "Units killed: " + currentPlayer.model.getUnitsKilled();
		final String currentPoints = "Current points: " + currentPlayer.model.getPoints();
		final String pointsSpent = "Points spent: " + currentPlayer.model.getPointsSpent();
		final String countriesTaken = "Countries taken: " + currentPlayer.model.getCountriesTaken();
		final int x = getPosition().x + 100;

		// Draw the statistics
		frame.draw(textFont, countriesRuled, x, getPosition().y + 190);
		frame.draw(textFont, armyStrength, x, getPosition().y + 240);
		frame.draw(textFont, unitsKilled, x, getPosition().y + 290);
		frame.draw(textFont, currentPoints, x, getPosition().y + 340);
		frame.draw(textFont, pointsSpent, x, getPosition().y + 390);
		frame.draw(textFont, countriesTaken, x, getPosition().y + 440);

	}

}
