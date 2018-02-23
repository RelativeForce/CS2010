package peril.ai;

import peril.controllers.AIController;

public class Goat extends AI {

	private static final String NAME = "Goat";
	
	private static final int SPEED = 100;

	public Goat(AIController api) {
		super(NAME, SPEED, api);
	}

	@Override
	protected AIOperation processReinforce(AIController api) {
		// TODO Auto-generated method stub
		return new AIOperation();
	}

	@Override
	protected AIOperation processAttack(AIController api) {
		// TODO Auto-generated method stub
		return new AIOperation();
	}

	@Override
	protected AIOperation processFortify(AIController api) {
		// TODO Auto-generated method stub
		return new AIOperation();
	}

}
