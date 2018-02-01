package peril.ai;

import peril.ai.api.Controller;
import peril.ai.api.Country;

public class Duckling extends AI {

	public Duckling(Controller api) {
		super(500, api);

	}

	@Override
	protected boolean processReinforce(Controller api) {
		
		 //figure out which country to reinforce
		
		
		return false;
	}

	@Override
	protected boolean processAttack(Controller api) {

		return false;
	}

	@Override
	protected boolean processFortify(Controller api) {

		return false;
	}

}
