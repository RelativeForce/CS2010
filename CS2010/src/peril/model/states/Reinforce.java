package peril.model.states;

import peril.ai.api.Player;
import peril.ai.api.Controller;
import peril.board.Country;

public class Reinforce extends ModelState {

	/**
	 * The {@link Country} that is to be selected must be owned by the current
	 * {@link Player} in order to be selected.
	 */
	public boolean select(Country country, Controller api) {

		if (country != null) {

			// Holds the current player
			Player player = api.getCurrentPlayer();

			// Holds the ruler of the country
			Player ruler = country.getRuler();

			if (player.equals(ruler)) {

				// De-select the current selected
				removeHighlight(getSelected(0));
				deselectAll();

				// Select the new country
				addHighlight(country);
				addSelected(country, 0);
				return true;

			}

		} else {
			removeHighlight(getSelected(0));
			deselectAll();
			return true;
		}

		deselectAll();
		return false;
	}

}
