package peril.ui.states;

import peril.Game;
import peril.ui.UserInterface;

public class ReinforcementState extends CoreGameState {

	public ReinforcementState(Game game, UserInterface ui) {
		super(game, ui);
		stateName = "Reinforcement";
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 1;
	}
}
