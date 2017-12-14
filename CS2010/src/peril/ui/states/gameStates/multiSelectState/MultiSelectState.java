package peril.ui.states.gameStates.multiSelectState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
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
	private Country selected;

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
		selected = null;
	}

	/**
	 * Processes a mouse click a {@link Point} on this {@link MultiSelectState}.
	 */
	@Override
	public void parseClick(int button, Point click) {

		if (button == Input.MOUSE_RIGHT_BUTTON) {
			removeHighlight(selected);
			selected = null;
			removeHighlight(getSelected());
			removeSelected();
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
		removeHighlight(selected);
		selected = null;
	}

	/**
	 * Determines whether the specified {@link Country} has been selected or not as
	 * a primary or secondary selected {@link Country}. There must be a primary
	 * {@link Country} selected in order for there to be a secondary selected.
	 */
	@Override
	public boolean select(Country country) {

		final boolean selectPrimary = selectPrimary(country);
		final boolean selectSecondary = selectSecondary(country);

		// If it a valid primary or secondary country
		if (selectPrimary || selectSecondary) {

			// The secondary is dependent on the primary so as the country is valid primary
			// or secondary the current secondary must be de-highlighted.
			removeHighlight(getSecondary());

			// If the country is a valid primary then the old primary is de-highlighted
			if (selectPrimary && !selectSecondary) {
				removeHighlight(getPrimary());
				setPrimary(country);
			}
			// Otherwise the the secondary is de-highlighted and then the country is
			// highlighted.
			else {
				setSecondary(country);
			}

			addHighlight(country);

			return true;
		}

		// Otherwise remove the highlight from both primary and secondary.
		removeHighlight(getPrimary());
		removeHighlight(getSecondary());
		removeSelected();
		return false;
	}

	/**
	 * Removes both primary and secondary selected {@link Country}s.
	 */
	@Override
	public void removeSelected() {
		super.removeSelected();
		super.removeHighlight(selected);
		selected = null;
	}

	/**
	 * Retrieves the secondary selected {@link Country}.
	 * 
	 * @return Secondary selected {@link Country}
	 */
	public Country getSecondary() {
		return selected;
	}

	/**
	 * Retrieves the secondary selected {@link Country}.
	 * 
	 * @return Primary selected {@link Country}
	 */
	public Country getPrimary() {
		return getSelected();
	}

	/**
	 * Determines whether or not the specified {@link Country} is selected as
	 * primary {@link Country} based on the current state of this
	 * {@link MultiSelectState}. Should return false if the specified
	 * {@link Country} is null.
	 * 
	 * @param country
	 *            {@link Country}
	 * @return Whether or not the specified {@link Country} is selected based on the
	 *         current state of this {@link MultiSelectState}.
	 */
	protected abstract boolean selectPrimary(Country country);

	/**
	 * Determines whether or not the specified {@link Country} is selected as
	 * secondary {@link Country} based on the current state of this
	 * {@link MultiSelectState}. Should return false if the specified
	 * {@link Country} is null. There must be a primary {@link Country} in order for
	 * the {@link Country} to be a valid secondary.
	 * 
	 * @param country
	 *            {@link Country}
	 * @return Whether or not the specified {@link Country} is selected based on the
	 *         current state of this {@link MultiSelectState}.
	 */
	protected abstract boolean selectSecondary(Country country);

	/**
	 * Sets the secondary selected {@link Country}..
	 * 
	 * @param country
	 *            {@link Country}.
	 */
	protected void setSecondary(Country country) {
		selected = country;
	}

	/**
	 * Sets the primary selected {@link Country}..
	 * 
	 * @param country
	 *            {@link Country}.
	 */
	protected void setPrimary(Country country) {
		super.setSelected(country);
	}

}
