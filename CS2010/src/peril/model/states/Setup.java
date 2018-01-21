package peril.model.states;

import peril.controllers.GameController;
import peril.model.board.ModelCountry;

public class Setup extends ModelState{

	/**
	 * During {@link SetupState} any country is select-able.
	 */
	public boolean select(ModelCountry country, GameController api) {
		
		deselectAt(0);
		addSelected(country, 0);
		
		return true;
	}

}
