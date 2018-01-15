package peril.model.states;

import peril.ai.api.Controller;
import peril.board.Country;
import peril.ui.states.gameStates.SetupState;

public class Setup extends ModelState{

	/**
	 * During {@link SetupState} any country is select-able.
	 */
	public boolean select(Country country, Controller api) {
		
		removeHighlight(getSelected(0));
		deselectAt(0);
		
		addHighlight(country);
		addSelected(country, 0);
		
		return true;
	}

}
