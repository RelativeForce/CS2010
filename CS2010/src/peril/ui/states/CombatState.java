package peril.ui.states;

import peril.Game;
import peril.Point;

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

	@Override
	public void parseClick(int button, Point click) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parseButton(int key, char c) {
		// TODO Auto-generated method stub
		
	}

	
	
}
