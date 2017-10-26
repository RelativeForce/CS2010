package peril.ui.states;

import peril.Game;
import peril.ui.UserInterface;

public class MovementState extends CoreGameState {

	public MovementState(Game game, UserInterface ui) {
		super(game, ui);
		stateName = "Movement";
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 3;
	}

}
