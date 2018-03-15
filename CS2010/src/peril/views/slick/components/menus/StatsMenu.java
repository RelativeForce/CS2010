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
 * @since 2018-03-15
 * @version 1.01.08
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

		// Draw the statistics
		drawStat(frame, "AI:", currentPlayer.model.ai.name, 190);
		drawStat(frame, "Countries owned:", Integer.toString(currentPlayer.model.getCountriesRuled()), 230);
		drawStat(frame, "Total army strength:", Integer.toString(currentPlayer.model.getTotalArmyStrength()), 270);
		drawStat(frame, "Units killed:", Integer.toString(currentPlayer.model.getUnitsKilled()), 310);
		drawStat(frame, "Current points:", Integer.toString(currentPlayer.model.getPoints()), 350);
		drawStat(frame, "Points spent:", Integer.toString(currentPlayer.model.getPointsSpent()), 390);
		drawStat(frame, "Countries taken:", Integer.toString(currentPlayer.model.getCountriesTaken()), 430);

	}

	/**
	 * Draws a specific statistic on screen.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link StatsMenu} to the user.
	 * @param stat
	 *            The string statistic.
	 * @param value
	 *            The value of that statistic.
	 * @param yOffset
	 *            The y off set of the text on screen.
	 */
	private void drawStat(Frame frame, String stat, String value, int yOffset) {

		final int y = getPosition().y + yOffset;
		final int statX = getPosition().x + 100;
		final int valueX = getPosition().x + getWidth() - 100 - textFont.getWidth(value);

		frame.draw(textFont, stat, statX, y);
		frame.draw(textFont, value, valueX, y);
	}

}
