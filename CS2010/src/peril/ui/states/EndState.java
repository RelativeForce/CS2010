package peril.ui.states;

import peril.Game;
import peril.Player;
import peril.Point;

public class EndState extends CoreGameState{

	/**
	 * The ID of this {@link EndState}
	 */
	private static final int ID = 5;
	
	public EndState(Game game) {
		super(game);
		stateName = "EndState";
	}
	
	public void displayWinner(Player player) {
		
	}

	@Override
	public int getID() {
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
