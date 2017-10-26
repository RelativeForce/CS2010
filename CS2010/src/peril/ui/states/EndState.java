package peril.ui.states;

import peril.Game;
import peril.Player;

public class EndState extends CoreGameState{

	public EndState(Game game) {
		super(game);
		stateName = "EndState";
	}
	
	public void displayWinner(Player player) {
		
	}

	@Override
	public int getID() {
		return 4;
	}

}
