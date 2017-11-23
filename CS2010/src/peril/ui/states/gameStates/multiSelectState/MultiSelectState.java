package peril.ui.states.gameStates.multiSelectState;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.board.Country;
import peril.ui.components.menus.PauseMenu;
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
	private Country highlightedCountry;

	/**
	 * Constructs a new {@link MultiSelectState}.
	 * 
	 * @param game
	 *            The {@link Game} that houses this {@link MultiSelectState}.
	 * @param id
	 *            The ID of this {@link MultiSelectState}.
	 * @param stateName
	 *            The name of this {@link MultiSelectState}.
	 * @param pauseMenu
	 *            The {@link PauseMenu} for this {@link MultiSelectState}.
	 * 
	 */
	public MultiSelectState(Game game, String stateName, int id, PauseMenu pauseMenu) {
		super(game, stateName, id, pauseMenu);
		highlightedCountry = null;
	}

	/**
	 * Removes the highlight effect on the primary and secondary highlighted
	 * {@link Country}s.
	 */
	@Override
	public void unhighlightCountry(Country country) {

		// Unhighlight both highlighted countries when this method is called from a
		// external class.
		super.unhighlightCountry(getSecondaryHightlightedCounrty());
		setSecondaryHighlightedCountry(null);
		super.unhighlightCountry(country);

	}

	/**
	 * Processes a mouse click a {@link Point} on this {@link MultiSelectState}.
	 */
	@Override
	public void parseClick(int button, Point click) {

		if (button == Input.MOUSE_RIGHT_BUTTON) {
			unhighlightCountry(highlightedCountry);
			highlightedCountry = null;
			unhighlightCountry(getHighlightedCountry());
			super.highlightCountry(null);
		}

		super.parseClick(button, click);
	}

	/**
	 * Performs the exit state operations of this {@link MultiSelectState}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);

		// Remove the highlight effect on the current highlighted country.
		unhighlightCountry(highlightedCountry);
		highlightedCountry = null;
	}

	/**
	 * Sets the secondary {@link MultiSelectState#highlightedCountry}
	 * 
	 * @param country
	 *            {@link Country}.
	 */
	protected void setSecondaryHighlightedCountry(Country country) {
		highlightedCountry = country;

		if (highlightedCountry != null) {
			// Highlight the country
			highlightedCountry.setImage(highlightedCountry.getRegion().getPosition(),
					highlightedCountry.getRegion().convert(Color.yellow));
		}
	}

	/**
	 * Retrieves the secondary {@link MultiSelectState#highlightedCountry}.
	 * 
	 * @return Secondary {@link Country}
	 */
	protected Country getSecondaryHightlightedCounrty() {
		return highlightedCountry;
	}

	/**
	 * Processes whether a {@link Country} is a valid
	 * {@link MultiSelectState#highlightedCountry} based on
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
