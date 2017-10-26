package peril.ui.states;

import peril.Game;
import peril.Player;

public class CombatState extends CoreGameState {

	public CombatState(Game game) {
		super(game);
		stateName = "Combat";
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 2;
	}

	
	
}
