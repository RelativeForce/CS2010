package peril.ai;

import peril.controllers.AIController;

public class Duckling extends AI {

	public Duckling(AIController api) {
		super("Duckling", 100, api);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean processReinforce(AIController api) {
		// TODO Auto-generated method stub
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
