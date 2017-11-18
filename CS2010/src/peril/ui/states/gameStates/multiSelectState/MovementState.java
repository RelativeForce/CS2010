package peril.ui.states.gameStates.multiSelectState;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.board.Country;
import peril.ui.Button;
import peril.ui.components.menus.PauseMenu;
import peril.ui.states.gameStates.CoreGameState;

/**
 * Encapsulates the behaviour of the 'Movement' state of the game. In this state
 * the {@link Game#getCurrentPlayer()} chooses which of their {@link Country}s
 * they will move units to another {@link Country}.
 * 
 * @author Joshua_Eddy, Joseph_Rolli
 *
 */
public final class MovementState extends MultiSelectState {

	/**
	 * The name of a specific {@link MovementState}.
	 */
	private static final String STATE_NAME = "Movement";

	/**
	 * Holds the instance of the fortify {@link Button}.
	 */
	private Button fortifyButton;

	/**
	 * Constructs a new {@link MovementState}.
	 * 
	 * @param game
	 *            The {@link Game} that houses this {@link MovementState}.
	 * @param id
	 *            The ID of this {@link MovementState}.
	 * @param pauseMenu
	 *            The {@link PauseMenu} for this {@link MovementState}.
	 * 
	 */
	public MovementState(Game game, int id, PauseMenu pauseMenu) {
		super(game, STATE_NAME, id, pauseMenu);
	}

	/**
	 * Highlights a specified {@link Country} if it is friendly or a
	 * {@link Country#getNeighbours()} of the
	 * {@link CoreGameState#getHighlightedCountry()}..
	 */
	@Override
	public void highlightCountry(Country country) {

		// If the country is null then set the primary highlighted as null and
		// unhighlight the current enemy country.
		if (country != null) {

			// Holds the current player
			Player player = getGame().getCurrentPlayer();

			// Holds the ruler of the country
			Player ruler = country.getRuler();

			processCountry(country, player, ruler);

		} else {
			fortifyButton.hide();
			super.unhighlightCountry(super.getSecondaryHightlightedCounrty());
			super.setSecondaryHighlightedCountry(null);
			super.unhighlightCountry(super.getHighlightedCountry());
			super.highlightCountry(country);
		}

	}

	@Override
	public void addButton(Button button) {
		super.addButton(button);

		if (button.getId().equals("fortify")) {
			fortifyButton = button;
			fortifyButton.hide();
		}
	}

	/**
	 * Pans this {@link MovementState}.
	 */
	@Override
	protected void pan(Point panVector) {

		Point old = fortifyButton.getPosition();

		Point vector = getGame().board.move(panVector);

		if (vector.x != 0 || vector.y != 0) {
			fortifyButton.setPosition(new Point(old.x + vector.x, old.y + vector.y));
		}

	}

	/**
	 * Render the {@link MovementState}.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		super.drawPlayerName(g);
		
		this.drawValidTargets(g);
		
		super.drawImages(g);
		super.drawButtons(g);

		drawPauseMenu(g);
	}

	/**
	 * Retrieves the {@link Country} the
	 * {@link CoreGameState#getHighlightedCountry()} will sent troops too when
	 * fortify is clicked.
	 * 
	 * @return {@link Counrty}
	 */
	public Country getTargetCountry() {
		return super.getSecondaryHightlightedCounrty();
	}

	/**
	 * Hide the fortify {@link Button}.
	 */
	public void hideFortifyButton() {
		fortifyButton.hide();
	}

	/**
	 * Processes whether a {@link Country} is a valid target for the
	 * {@link CoreGameState#getHighlightedCountry()} to attack. This is based on the
	 * {@link Player} ruler and the {@link Player} ({@link Game#getCurrentPlayer()})
	 * 
	 * @param country
	 *            {@link Country}
	 * @param player
	 *            {@link Player}
	 * @param ruler
	 *            {@link Player}
	 */
	protected void processCountry(Country country, Player player, Player ruler) {

		// If there is a primary friendly country and the target is not null and the
		// ruler of the country is not the player.
		if (getHighlightedCountry() != null && player.equals(ruler)) {

			// if the country is a neighbour of the primary highlighted country then it is a
			// valid target.
			if (isValidTarget(getHighlightedCountry(), country)) {

				super.unhighlightCountry(super.getSecondaryHightlightedCounrty());
				super.setSecondaryHighlightedCountry(country);
				moveFortifyButton(getArmyPosition(getHighlightedCountry()), getArmyPosition(country));
				fortifyButton.show();

			} else {
				// DO NOTHING
			}

		}
		// If the country clicked is to be the new primary country but is not ruler by
		// the player
		else if (!player.equals(ruler)) {
			// DO NOTHING
		}
		// If the country clicked is to be the new primary country and is owned by the
		// player.
		else {
			fortifyButton.hide();
			super.unhighlightCountry(super.getSecondaryHightlightedCounrty());
			super.highlightCountry(country);
		}
	}

	/**
	 * Retrieves whether or not the secondary {@link Country} is a valid targets for
	 * the primary {@link Country}.
	 * 
	 * @param primaryCountry
	 *            is the primary {@link Country}
	 * @param secondaryTarget
	 *            is the second {@link Country}
	 * @return <code>boolean</code> if it is a valid target.
	 */
	private boolean isValidTarget(Country primaryCountry, Country secondaryTarget) {

		// If there is a primary friendly country and the target is not null and the
		// ruler of the country is the player.
		if (primaryCountry.getRuler().equals(secondaryTarget.getRuler())) {

			// if the country is a neighbour of the primary highlighted country then it is a
			// valid target.
			if (primaryCountry.isNeighbour(secondaryTarget)) {

				// If the army size of the primary country is greater than 1.
				if (primaryCountry.getArmy().getSize() > 1) {
					return true;
				}

			}

		}

		return false;
	}

	/**
	 * Draws a line between the {@link CoreGameState#getHighlightedCountry()} and or
	 * all its valid targets.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawValidTargets(Graphics g) {

		// If there is a country highlighted.
		if (super.getHighlightedCountry() != null) {

			// Assign the line colour.
			g.setColor(Color.green);

			for (Country country : super.getHighlightedCountry().getNeighbours()) {

				// if it is a valid target highlight the country and draw a line from the
				// highlighted country to the neighbour country.
				if (isValidTarget(super.getHighlightedCountry(), country)) {

					Point enemy = super.getArmyPosition(country);
					Point selected = super.getArmyPosition(super.getHighlightedCountry());
					g.drawLine(enemy.x, enemy.y, selected.x, selected.y);
				}
			}

		}
	}

	/**
	 * Moves the reinforce {@link Button} to be positioned at the top left of the
	 * current country.
	 * 
	 * @param country
	 *            {@link Country}
	 */
	private void moveFortifyButton(Point p1, Point p2) {

		int x = ((p2.x - p1.x) / 2) + p1.x;
		int y = ((p2.y - p1.y) / 2) + p1.y;

		fortifyButton.setPosition(new Point(x, y));

	}
}
