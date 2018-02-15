package peril.ai;

import peril.controllers.api.Country;
import peril.controllers.api.Player;
import peril.controllers.AIController;
import peril.controllers.api.Board;

/**
 * An AI player of the game. This AI will perform an action based on the current
 * state of the {@link Board} and the current state of the game. Any concrete
 * instances of this must be state-less as the may be used by multiple
 * {@link Player}s.
 * 
 * @author Joshua_Eddy
 *
 */
public abstract class AI {

	/**
	 * The {@link AI} for a user controlled player. This {@link AI} does not support
	 * any operations and therefore can be used to detect a user controlled player.
	 */
	public static final AI USER = new AI() {

		@Override
		public boolean processReinforce(AIController api) {
			throw new UnsupportedOperationException("This is a user contrlled player.");
		}

		@Override
		public boolean processAttack(AIController api) {
			throw new UnsupportedOperationException("This is a user contrlled player.");
		}

		@Override
		public boolean processFortify(AIController api) {
			throw new UnsupportedOperationException("This is a user contrlled player.");
		}
	};

	/**
	 * The minimum number of milliseconds between each action of this {@link AI}.
	 */
	public static final int MAX_SPEED = 100;

	/**
	 * The name of this {@link AI}.
	 */
	public final String name;

	/**
	 * The {@link AIController} that this {@link AI} will use to query the state of
	 * the game.
	 */
	private final AIController api;

	/**
	 * The number of milliseconds that this {@link AI} will wait before performing
	 * another operation.
	 */
	private int wait;

	/**
	 * The number of milliseconds between each action of this {@link AI}. If this is
	 * zero or lower then the then the {@link AI} will perform its actions at the
	 * frame rate of the display.
	 */
	private int speed;

	/**
	 * Constructs a new {@link AI}.
	 * 
	 * @param name
	 *            The name of this {@link AI}.
	 * @param defaultSpeed
	 *            The number of milliseconds between each action of this {@link AI}.
	 *            If this is zero or lower then the then the {@link AI} will perform
	 *            its actions at the frame rate of the display.
	 * @param api
	 *            The {@link AIController} that this {@link AI} will use to query the
	 *            state of the game.
	 */
	public AI(String name, int defaultSpeed, AIController api) {
		this.name = name;
		this.wait = 0;
		this.api = api;
		setSpeed(defaultSpeed);
	}

	/**
	 * Constructs the {@link AI#USER}.
	 */
	private AI() {
		this.speed = 0;
		this.wait = 0;
		this.api = null;
		this.name = "None";
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
	public final boolean reinforce(int delta) {

		if (api.getCurrentPlayer().getDistributableArmyStrength() == 0) {
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
	public final boolean attack(int delta) {

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
	public final boolean fortify(int delta) {

		if (wait <= 0) {
			wait = speed;
			api.clearSelected();
			return processFortify(api);
		}

		wait -= delta;

		return true;
	}

	/**
	 * Set the number of milliseconds between each action of this {@link AI}.
	 * 
	 * @param speed
	 */
	public final void setSpeed(int speed) {
		if (speed < MAX_SPEED) {
			throw new IllegalArgumentException(
					speed + " is not a valid speed. The specified speed must be greater than " + MAX_SPEED + ".");
		}

		this.speed = speed;
	}

	/**
	 * Perform the reinforce operation using the specified {@link AIController}. This
	 * operation should be specific to the specialised instance of the
	 * {@link AI}.<br>
	 * <br>
	 * Key aspects:
	 * <ul>
	 * <li>Only reinforce the selected {@link Country} <strong>ONCE</strong>.</li>
	 * <li>The operation should be completely functional and rely on
	 * <strong>ANY</strong> data that is not provided by the
	 * {@link AIController}.</li>
	 * <li>The selected {@link Country} must be owned by the current {@link Player}
	 * which can be retrieved using {@link AIController#getCurrentPlayer()}.</li>
	 * </ul>
	 * 
	 * This method must:
	 * <ol>
	 * <li>Select a {@link Country} using {@link AIController#select(Country)}.</li>
	 * <li>Reinforce that {@link Country} using {@link AIController#reinforce()}.</li>
	 * </ol>
	 * 
	 * @param api
	 *            {@link AIController}
	 * @return Whether or not this {@link AI} wishes to perform another operation or
	 *         not. If not the {@link AIController}
	 */
	protected abstract boolean processReinforce(AIController api);

	/**
	 * Perform the attack operation using the specified {@link AIController}. This
	 * operation should be specific to the specialised instance of the
	 * {@link AI}.<br>
	 * <br>
	 * Key aspects:
	 * <ul>
	 * <li>Only attack the target {@link Country} <strong>ONCE</strong>.</li>
	 * <li>The operation should be completely functional and rely on
	 * <strong>NO</strong> data that is not provided by the {@link AIController}.</li>
	 * <li>The primary {@link Country} should have more than a one unit army.</li>
	 * <li>The primary {@link Country} that is selected must be owned by the current
	 * {@link Player}.</li>
	 * <li>The target {@link Country} should be a neighbour of the primary
	 * {@link Country} and owned by another {@link Player}.</li>
	 * </ul>
	 * <br>
	 * 
	 * This method must:
	 * <ol>
	 * <li>Select a primary {@link Country} using
	 * {@link AIController#select(Country)}.</li>
	 * <li>Select a target {@link Country} using
	 * {@link AIController#select(Country)}.</li>
	 * <li>Attack that {@link Country} using {@link AIController#attack()}.</li>
	 * </ol>
	 * 
	 * The order that the {@link Country}s are selected matters. The primary
	 * {@link Country} <strong>MUST</strong> be selected first. The selected
	 * {@link Country}s can be cleared using {@link AIController#clearSelected()}.
	 * <br>
	 * 
	 * @param api
	 *            {@link AIController}
	 * @return Whether or not this {@link AI} wishes to perform another attack or
	 *         not.
	 */
	protected abstract boolean processAttack(AIController api);

	/**
	 * Perform the fortify operation using the specified {@link AIController}. This
	 * operation should be specific to the specialised instance of the
	 * {@link AI}.<br>
	 * <br>
	 * 
	 * Key aspects:
	 * <ul>
	 * <li>Only fortify the target {@link Country} <strong>ONCE</strong>.</li>
	 * <li>The operation should be completely functional and rely on
	 * <strong>NO</strong> data that is not provided by the {@link AIController}.</li>
	 * <li>The primary {@link Country} must have more than a one unit army.</li>
	 * <li>The primary and target {@link Country}s must be owned by the same
	 * {@link Player}.</li>
	 * <li>The target {@link Country} must have a valid path between is and the
	 * primary {@link Country}. Use
	 * {@link AIController#isPathBetween(Country, Country)} to check this.</li>
	 * </ul>
	 * <br>
	 * 
	 * This method must:
	 * <ol>
	 * <li>Select a primary {@link Country} using
	 * {@link AIController#select(Country)}.</li>
	 * <li>Select a target {@link Country} using
	 * {@link AIController#select(Country)}.</li>
	 * <li>Fortify that {@link Country} using {@link AIController#fortify()}.</li>
	 * </ol>
	 * 
	 * The order that the {@link Country}s are selected matters. The primary
	 * {@link Country} <strong>MUST</strong> be selected first. The selected
	 * {@link Country}s can be cleared using {@link AIController#clearSelected()}}.
	 * <br>
	 * 
	 * @param api
	 *            {@link AIController}
	 * @return Whether or not this {@link AI} wishes to perform another fortify or
	 *         not.
	 */
	protected abstract boolean processFortify(AIController api);

}
