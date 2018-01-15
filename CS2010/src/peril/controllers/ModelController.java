package peril.controllers;

import peril.controllers.api.Player;

public interface ModelController {
	
	/**
	 * Retrieves the current {@link Player}.
	 * 
	 * @return
	 */
	Player getCurrentPlayer();
	
	
}
