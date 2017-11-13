package peril.ui.states.gameStates.multiSelectState;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.board.Country;
import peril.ui.states.gameStates.CoreGameState;

/**
 * Encapsulates the behaviours of a {@link CoreGameState} that can select two
 * {@link Country}s.
 * 
 * @author Joshua_Eddy
 *
 */
public abstract class MultiSelectState extends CoreGameState {

	/**
	 * The secondary {@link Country} that can be selected based on conditions in the
	 * sub-classes.
	 */
	private Country highlightedCounrty;

	/**
	 * Constructs a new {@link MultiSelectState}.
	 * 
	 * @param game
	 *            The {@link Game} that houses this {@link MultiSelectState}.
	 * @param id
	 *            The ID of this {@link MultiSelectState}.
	 * @param stateName
	 *            The name of this {@link MultiSelectState}.
	 * 
	 */
	public MultiSelectState(Game game, String stateName, int id) {
		super(game, stateName, id);
		highlightedCounrty = null;
	}

	/**
	 * Parse a click at a {@link Point} on the screen.
	 */
	@Override
	public abstract void parseClick(int button, Point click);

	/**
	 * Removes the highlight effect on the primary and secondary highlighted
	 * {@link Country}s.
	 */
	@Override
	public void unhighlightCountry(Country country) {

		// Unhighlight both highlighted countries when this method is called from a
		// external class.
		super.unhighlightCountry(getSecondaryHightlightedCounrty());
		setSecondaryCountry(null);
		super.unhighlightCountry(country);

	}

	/**
	 * Sets the secondary {@link MultiSelectState#highlightedCounrty}
	 * 
	 * @param country
	 *            {@link Country}.
	 */
	protected void setSecondaryCountry(Country country) {
		highlightedCounrty = country;
	}

	/**
	 * Retrieves the secondary {@link MultiSelectState#highlightedCounrty}.
	 * 
	 * @return Secondary {@link Country}
	 */
	protected Country getSecondaryHightlightedCounrty() {
		return highlightedCounrty;
	}

	/**
	 * Processes whether a {@link Country} is a valid
	 * {@link MultiSelectState#highlightedCounrty} based on
	 * {@link CoreGameState#getHighlightedCountry()}.
	 * 
	 * @param country
	 *            {@link Country}
	 * @param player
	 *            {@link Player}
	 * @param ruler
	 *            {@link Player} of the specified {@link Country}.
	 */
	protected abstract void processCountry(Country country, Player player, Player ruler);
}