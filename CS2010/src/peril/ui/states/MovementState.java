package peril.ui.states;

import peril.Game;
import peril.Point;

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

	@Override
	public void parseClick(int button, Point click) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parseButton(int key, char c) {
		// TODO Auto-generated method stub
		
	}

}
