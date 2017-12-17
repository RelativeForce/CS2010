package peril.ai;

import peril.ai.api.Controller;

public abstract class AI {

	public static final AI USER = new AI() {

		@Override
		public boolean processReinforce(Controller api) {
			throw new UnsupportedOperationException("This is a user contrlled player.");
		}

		@Override
		public boolean processAttack(Controller api) {
			throw new UnsupportedOperationException("This is a user contrlled player.");
		}

		@Override
		public boolean processFortify(Controller api) {
			throw new UnsupportedOperationException("This is a user contrlled player.");
		}
	};

	private final int speed;

	private int wait;
	
	private final Controller api;

	public AI(int speed, Controller api) {
		this.speed = speed;
		this.wait = 0;
		this.api = api;
	}

	private AI() {
		this.speed = 0;
		this.wait = 0;
		this.api = null;
	}
	
	public boolean reinforce(int delta) {

		if (api.getCurrentPlayer().getDistributableArmySize() == 0) {
			return false;
		}

		if (wait <= 0) {
			wait = speed;
			return processReinforce(api);
		}

		wait -= delta;
		return true;

	}

	public boolean attack(int delta) {

		if (wait <= 0) {
			wait = speed;
			return processAttack(api);
		}

		wait -= delta;
		return true;
	}

	public boolean fortify(int delta) {
		
		if (wait <= 0) {
			wait = speed;
			return processFortify(api);
		}

		wait -= delta;
		
		return true;
	}

	protected abstract boolean processReinforce(Controller api);

	protected abstract boolean processAttack(Controller api);

	protected abstract boolean processFortify(Controller api);

}
