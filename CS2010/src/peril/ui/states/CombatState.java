package peril.ui.states;

import peril.Game;
import peril.Player;
import peril.ui.UserInterface;

public class CombatState extends CoreGameState {

	public CombatState(Game game, UserInterface ui) {
		super(game, ui);
		stateName = "Combat";
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 2;
	}

	
	
}
