package peril.ui.states;

import peril.Game;
import peril.Player;
import peril.ui.UserInterface;

public class EndState extends CoreGameState{

	public EndState(Game game, UserInterface ui) {
		super(game, ui);
		stateName = "EndState";
	}
	
	public void displayWinner(Player player) {
		
	}

	@Override
	public int getID() {
		return 4;
	}

}
