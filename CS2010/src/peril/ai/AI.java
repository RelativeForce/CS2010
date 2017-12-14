package peril.ai;

import peril.Game;

public abstract class AI {

	public static final AI USER = new AI(0) {

		@Override
		public boolean processReinforce(Game game) {
			throw new UnsupportedOperationException("This is a user contrlled player.");
		}

		@Override
		public boolean processAttack(Game game) {
			throw new UnsupportedOperationException("This is a user contrlled player.");
		}

		@Override
		public boolean processFortify(Game game) {
			throw new UnsupportedOperationException("This is a user contrlled player.");
		}
	};

	protected final int speed;

	protected int wait;

	public AI(int speed) {
		this.speed = speed;
		this.wait = 0;
	}

	public boolean reinforce(Game game, int delta) {

		if (game.players.getCurrent().distributableArmy.getSize() == 0) {
			return false;
		}

		if (wait <= 0) {
			wait = speed;
			return processReinforce(game);
		}

		wait -= delta;
		return true;

	}

	public boolean attack(Game game, int delta) {
		
		if (game.players.getCurrent().totalArmy.getSize() == game.players.getCurrent().getCountriesRuled()) {
			game.menus.warMenu.hide();
			return false;
		}

		if (wait <= 0) {
			wait = speed;
			return processAttack(game);
		}

		wait -= delta;
		return true;
	}

	public boolean fortify(Game game, int delta) {
		
		if (wait <= 0) {
			wait = speed;
			return processFortify(game);
		}

		wait -= delta;
		
		return true;
	}

	protected abstract boolean processReinforce(Game game);

	protected abstract boolean processAttack(Game game);

	protected abstract boolean processFortify(Game game);

}
