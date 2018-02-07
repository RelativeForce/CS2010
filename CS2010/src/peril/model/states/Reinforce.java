package peril.model.states;

import peril.controllers.GameController;
import peril.controllers.api.Player;
import peril.helpers.UnitHelper;
import peril.model.ModelPlayer;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.views.slick.states.gameStates.ReinforcementState;

public class Reinforce extends ModelState {
	
	/**
	 * The name of a specific {@link ReinforcementState}.
	 */
	private static final String STATE_NAME = "Reinforcement";

	public Reinforce() {
		super(STATE_NAME);
	}

	/**
	 * The {@link SlickCountry} that is to be selected must be owned by the current
	 * {@link Player} in order to be selected.
	 */
	public boolean select(ModelCountry country, GameController api) {

		if (country != null) {

			// Holds the current player
			ModelPlayer player = api.getCurrentModelPlayer();

			// Holds the ruler of the country
			ModelPlayer ruler = country.getRuler();

			if (player.equals(ruler)) {

				// De-select the current selected
				deselectAll();

				// Select the new country
				addSelected(country, 0);
				return true;

			}

		} else {
			deselectAll();
			return true;
		}

		deselectAll();
		return false;
	}

	/**
	 * Reinforces the selected {@link SlickCountry}.
	 */
	public void reinforce(GameController game) {

		// Holds the currently highlighted country
		ModelCountry highlightedCountry = getSelected(0);

		// Holds the current player.
		ModelPlayer player = game.getCurrentModelPlayer();

		// If there is a country highlighted.
		if (highlightedCountry != null) {

			// If the player has any units to place
			if (player.distributableArmy.getNumberOfUnits() > 0) {

				ModelPlayer ruler = highlightedCountry.getRuler();

				// If the highlighted country has a ruler and it is that player
				if (ruler != null && ruler.equals(player)) {

					final ModelUnit weakest = UnitHelper.getInstance().getWeakest();
					
					// Remove the unit from the list of units to place.
					player.distributableArmy.remove(weakest);
					player.totalArmy.add(weakest);
					
					// Get that country's army and increase its size by one.
					highlightedCountry.getArmy().add(weakest);
					
					game.checkChallenges();


				} else {
					System.out.println(player.toString() + " does not rule this country");
				}

			} else {
				System.out.println("No units to distribute.");
			}

		} else {
			System.out.println("No country selected.");
		}

	}
}
