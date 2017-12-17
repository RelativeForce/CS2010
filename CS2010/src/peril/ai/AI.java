package peril.ai;

import peril.ai.api.Board;
import peril.ai.api.Controller;

/**
 * An AI player of the game. This AI will perform an action based on the current
 * state of the {@link Board} and the current state of the game.
 * 
 * @author Joshua_Eddy
 *
 */
public abstract class AI {

	/**
	 * The {@link AI} for a user controlled player. This {@link AI} does not support
	 * any operations and therefore can be used to detect and user controlled
	 * player.
	 */
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

	/**
	 * The number of milliseconds between each action of this {@link AI}. If this is
	 * zero or lower then the then the {@link AI} will perform its actions at the
	 * frame rate of the display.
	 */
	private final int speed;

	/**
	 * The {@link Controller} that this {@link AI} will use to query the state of
	 * the game.
	 */
	private final Controller api;

	/**
	 * The number of milliseconds that this {@link AI} will wait before performing
	 * another operation.
	 */
	private int wait;

	/**
	 * Constructs a new {@link AI}.
	 * 
	 * @param speed
	 *            The number of milliseconds between each action of this {@link AI}.
	 *            If this is zero or lower then the then the {@link AI} will perform
	 *            its actions at the frame rate of the display.
	 * @param api
	 *            The {@link Controller} that this {@link AI} will use to query the
	 *            state of the game.
	 */
	public AI(int speed, Controller api) {
		this.speed = speed;
		this.wait = 0;
		this.api = api;
	}

	/**
	 * Constructs the {@link AI#USER}.
	 */
	private AI() {
		this.speed = 0;
		this.wait = 0;
		this.api = null;
	}

	/**
	 * Performs the reinforce operations of this {@link AI}.
	 * 
	 * @param delta
	 *            The time (in milliseconds) that has elapsed since the last time
	 *            this method was called.
	 * @return Whether or not this {@link AI} wishes to perform another operation or
	 *         not.
	 */
	public boolean reinforce(int delta) {

		if (api.getCurrentPlayer().getDistributableArmySize() == 0) {
			return false;
		}

		if (wait <= 0) {
			wait = speed;
			api.clearSelected();
			return processReinforce(api);
		}

		wait -= delta;
		return true;

	}

	/**
	 * Performs the attack operations of this {@link AI}.
	 * 
	 * @param delta
	 *            The time (in milliseconds) that has elapsed since the last time
	 *            this method was called.
	 * @return Whether or not this {@link AI} wishes to perform another operation or
	 *         not.
	 */
	public boolean attack(int delta) {

		if (wait <= 0) {
			wait = speed;
			api.clearSelected();
			return processAttack(api);
		}

		wait -= delta;
		return true;
	}

	/**
	 * Performs the fortify operations of this {@link AI}.
	 * 
	 * @param delta
	 *            The time (in milliseconds) that has elapsed since the last time
	 *            this method was called.
	 * @return Whether or not this {@link AI} wishes to perform another operation or
	 *         not.
	 */
	public boolean fortify(int delta) {

		if (wait <= 0) {
			wait = speed;
			api.clearSelected();
			return processFortify(api);
		}

		wait -= delta;

		return true;
	}

	/**
	 * Perform the reinforce operation using the specified {@link Controller}.
	 * 
	 * @param api
	 *            {@link Controller}
	 * @return Whether or not this {@link AI} wishes to perform another operation or
	 *         not.
	 */
	protected abstract boolean processReinforce(Controller api);

	/**
	 * Perform the attack operation using the specified {@link Controller}.
	 * 
	 * @param api
	 *            {@link Controller}
	 * @return Whether or not this {@link AI} wishes to perform another operation or
	 *         not.
	 */
	protected abstract boolean processAttack(Controller api);

	/**
	 * Perform the fortify operation using the specified {@link Controller}.
	 * 
	 * @param api
	 *            {@link Controller}
	 * @return Whether or not this {@link AI} wishes to perform another operation or
	 *         not.
	 */
	protected abstract boolean processFortify(Controller api);

}
