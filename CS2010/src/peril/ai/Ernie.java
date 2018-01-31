package peril.ai;

import peril.controllers.AIController;
import peril.controllers.api.Player;

public class Ernie extends AI {

	public Ernie(AIController api) {
		super("Ernie", 100, api);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean processReinforce(AIController api) {
		Player player = api.getCurrentPlayer();
		return false;
	}

	@Override
	protected boolean processAttack(AIController api) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean processFortify(AIController api) {
		// TODO Auto-generated method stub
		return false;
	}

}
