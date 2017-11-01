package peril.ui.states;

import peril.Game;

public class ReinforcementState extends CoreGameState {

	public ReinforcementState(Game game) {
		super(game);
		stateName = "Reinforcement";
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 1;
	}
}
