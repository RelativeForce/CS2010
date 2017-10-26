package peril.ui.states;

import peril.Game;

public class MovementState extends CoreGameState {

	public MovementState(Game game) {
		super(game);
		stateName = "Movement";
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 3;
	}

}
