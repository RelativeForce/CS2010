package peril.model.states;

import peril.board.Country;
import peril.controllers.ModelController;
import peril.ui.states.gameStates.SetupState;

public class Setup extends ModelState{

	/**
	 * During {@link SetupState} any country is select-able.
	 */
	public boolean select(Country country, ModelController api) {
		
		removeHighlight(getSelected(0));
		deselectAt(0);
		
		addHighlight(country);
		addSelected(country, 0);
		
		return true;
	}

}
