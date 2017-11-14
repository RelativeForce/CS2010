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
import peril.ui.components.PauseMenu;
import peril.ui.states.gameStates.CoreGameState;

/**
 * Encapsulates the behaviour of the 'Combat' state of the game. In this state
 * the {@link Game#getCurrentPlayer()} chooses which of their {@link Country}s
 * they will attack other {@link Country}s with.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 *
 */
public final class CombatState extends MultiSelectState {

	/**
	 * The name of a specific {@link CombatState}.
	 */
	private static final String STATE_NAME = "Combat";

	/**
	 * Whether or not {@link CombatState} is currently after or before a country has
	 * been conquered.
	 */
	private boolean isPostCombat;

	/**
	 * Constructs a new {@link CombatState}.
	 * 
	 * @param game
	 *            The {@link Game} that houses this {@link CombatState}.
	 * @param id
	 *            The ID of this {@link CombatState}
	 * @param pauseMenu
	 *            The {@link PauseMenu} for this {@link CombatState}.
	 */
	public CombatState(Game game, int id, PauseMenu pauseMenu) {
		super(game, STATE_NAME, id, pauseMenu);
		isPostCombat = false;
	}

	/**
	 * Sets the state if this {@link CombatState} to after a {@link Country} has
	 * been conquered.
	 */
	public void setPostCombat() {
		isPostCombat = true;
	}

	/**
	 * Sets the state if this {@link CombatState} to before a {@link Country} has
	 * been conquered.
	 */
	public void setPreCombat() {
		isPostCombat = false;
	}

	/**
	 * Highlights a specified {@link Country}. If there is a friendly
	 * {@link Country} already highlighted then this will check if the
	 * {@link Country} is a valid target and allow highlighting accordingly.
	 */
	@Override
	public void highlightCountry(Country country) {

		if (isPostCombat) {
			highlightCountryPostCombat(country);
		} else {
			highlightCounrtyPreCombat(country);
		}

	}

	/**
	 * Displays
	 * {@link CoreGameState#render(GameContainer, StateBasedGame, Graphics) } then
	 * the current {@link Player}s name and then the enemy {@link Country}.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		super.render(gc, sbg, g);
		super.drawPlayerName(g);
		
		this.drawValidTargets(g);

	}

	/**
	 * Retrieves the {@link Country} the
	 * {@link CoreGameState#getHighlightedCountry()} will sent troops too when
	 * attacking.
	 * 
	 * @return {@link Country}
	 */
	public Country getEnemyCountry() {
		return super.getSecondaryHightlightedCounrty();
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
		if (getHighlightedCountry() != null) {

			// if the country is a valid target of the primary country..
			if (isValidTarget(super.getHighlightedCountry(), country)) {

				super.unhighlightCountry(super.getSecondaryHightlightedCounrty());
				super.setSecondaryHighlightedCountry(country);

			}
			// If the player owns the other country
			else if (player.equals(ruler)) {

				// Remove the highlight from the currently highlighted countries and highlight
				// the clicked country.
				super.unhighlightCountry(super.getSecondaryHightlightedCounrty());
				super.unhighlightCountry(super.getHighlightedCountry());
				super.highlightCountry(country);
			}

		}
		// If the player does not ruler the country
		else if (!player.equals(ruler)) {
			// DO NOTHING
		}
		// If the country clicked is to be the new primary country and is owned by the
		// player.
		else {

			// Remove the highlight from the currently highlighted countries and highlight
			// the clicked country.
			super.unhighlightCountry(super.getSecondaryHightlightedCounrty());
			super.unhighlightCountry(super.getHighlightedCountry());
			super.highlightCountry(country);
		}
	}

	/**
	 * If a {@link Player} has not conquered a new {@link Country} since they last
	 * clicked a {@link Country}.
	 * 
	 * @param country
	 *            {@link Country} that was clicked.
	 */
	private void highlightCounrtyPreCombat(Country country) {

		// If the country is null then set the primary highlighted as null and
		// unhighlight the current enemy country.
		if (country != null) {

			// Holds the current player
			Player player = getGame().getCurrentPlayer();

			// Holds the ruler of the country
			Player ruler = country.getRuler();

			processCountry(country, player, ruler);

		} else {
			super.unhighlightCountry(super.getSecondaryHightlightedCounrty());
			super.setSecondaryHighlightedCountry(null);
			super.unhighlightCountry(super.getHighlightedCountry());
			super.highlightCountry(country);
		}
	}

	/**
	 * If a {@link Player} has not conquered a new {@link Country} since they last
	 * clicked a {@link Country}. Remove the highlight effect on both the primary
	 * and secondary country.
	 * 
	 * @param country
	 */
	private void highlightCountryPostCombat(Country country) {

		super.unhighlightCountry(country);
		super.setSecondaryHighlightedCountry(null);
		super.unhighlightCountry(getHighlightedCountry());
		super.highlightCountry(null);

		setPreCombat();
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
		// ruler of the country is not the player.
		if (!primaryCountry.getRuler().equals(secondaryTarget.getRuler())) {

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
}
