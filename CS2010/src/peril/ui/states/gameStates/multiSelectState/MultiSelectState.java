package peril.ui.states.gameStates.multiSelectState;

import peril.Game;
import peril.Point;
import peril.board.Country;
import peril.ui.states.gameStates.CoreGameState;

public abstract class MultiSelectState extends CoreGameState{

	private Country highlightedCounrty;

	protected MultiSelectState(Game game, String stateName, int id) {
		super(game, stateName, id);
		highlightedCounrty = null;
	}
	
	protected void setSecondaryCountry(Country country) {
		highlightedCounrty = country;
	}
	
	protected Country getSecondaryHightlightedCounrty() {
		return highlightedCounrty;
	}

	@Override
	public abstract void parseClick(int button, Point click);

	@Override
	public void unhighlightCountry(Country country) {

		// Unhighlight both highlighted countries when this method is called from a
		// external class.
		super.unhighlightCountry(getSecondaryHightlightedCounrty());
		setSecondaryCountry(null);
		super.unhighlightCountry(country);

	}
}
