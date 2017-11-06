package peril.ui.states;

import peril.Game;
import peril.Point;

public class CombatState extends CoreGameState {

	/**
	 * The ID of this {@link CombatState}
	 */
	private static final int ID = 3;
	
	public CombatState(Game game) {
		super(game);
		stateName = "Combat";
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
