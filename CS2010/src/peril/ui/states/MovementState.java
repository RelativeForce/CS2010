package peril.ui.states;

import peril.Game;
import peril.Point;

public class MovementState extends CoreGameState {

	/**
	 * The ID of this {@link MovementState}.
	 */
	private static final int ID = 4;
	
	public MovementState(Game game) {
		super(game);
		stateName = "Movement";
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
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
