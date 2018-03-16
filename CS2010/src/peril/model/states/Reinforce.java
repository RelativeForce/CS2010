package peril.model.states;

import peril.GameController;
import peril.helpers.UnitHelper;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;

/**
 * The logic for the {@link ModelState} where the current {@link ModelPlayer}
 * can select one of their {@link ModelCountry}s and reinforce the
 * {@link ModelCountry}'s {@link ModelArmy} with {@link ModelUnit}(s).
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-03-15
 * @version 1.01.03
 * 
 * @see ModelState
 *
 */
public final class Reinforce extends ModelState {

	/**
	 * The name of the {@link Reinforce} state.
	 */
	private static final String STATE_NAME = "Reinforce";

	/**
	 * Constructs a new {@link Reinforce}.
	 * 
	 * @param game
	 *            The {@link GameController} that allows this {@link ModelState} to
	 *            query the state of the game.
	 */
	public Reinforce(GameController game) {
		super(STATE_NAME, game);
	}

	/**
	 * During {@link Reinforce} only one {@link ModelCountry} can be selected at
	 * once and that {@link ModelCountry} most be ruled by the current
	 * {@link ModelPlayer} as specified by
	 * {@link GameController#getCurrentModelPlayer()}.
	 */
	@Override
	public boolean select(ModelCountry country) {

		// If the country is not null.
		if (country != null) {

			// Holds the current player
			final ModelPlayer player = game.getCurrentModelPlayer();

			// Holds the ruler of the country
			final ModelPlayer ruler = country.getRuler();

			// If the current player rules the country.
			if (player.equals(ruler)) {

				// De-select the current selected country.
				deselectAll();

				// Select the new country
				addSelected(country, 0);
				return true;

			}
		}

		deselectAll();
		return false;
	}

	/**
	 * Reinforces the currently selected {@link ModelCountry}.
	 * 
	 */
	public void reinforce() {

		// Holds the currently highlighted country
		final ModelCountry selectedCountry = getSelected(0);

		// Holds the current player.
		final ModelPlayer player = game.getCurrentModelPlayer();

		// If there is a country selected.
		if (selectedCountry != null) {

			// If the player has any units to distribute.
			if (player.distributableArmy.getNumberOfUnits() > 0) {

				// Holds the ruler of the specified country
				final ModelPlayer ruler = selectedCountry.getRuler();

				// If the selected country's ruler is the current player.
				if (player.equals(ruler)) {

					// Holds the weakest unit in the game.
					final ModelUnit weakest = UnitHelper.getInstance().getWeakest();

					// Remove the unit from the list of units to place.
					player.distributableArmy.remove(weakest);
					player.totalArmy.add(weakest);

					// Get that country's army and increase its size by one.
					selectedCountry.getArmy().add(weakest);

					// Check if any challenges have been completed.
					game.checkChallenges();

				} else {
					throw new IllegalStateException(player.toString() + " does not rule this country");
				}

			} else {
				throw new IllegalStateException("No units to distribute.");
			}

		} else {
			throw new IllegalStateException("No country selected.");
		}

	}
}
