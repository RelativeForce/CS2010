package peril.ai;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import peril.ai.api.Board;
import peril.ai.api.Country;
import peril.ai.api.Player;

/**
 * An AI player of the game. This AI will perform an action based on the current
 * state of the {@link Board} and the current state of the game. Any concrete
 * instances of this must be state-less as the may be used by multiple
 * {@link Player}s.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-03-08
 * @version 1.01.02
 * 
 * @see AIController
 *
 */
public abstract class AI {

	/**
	 * The {@link AI} for a user controlled player. This {@link AI} does not support
	 * any operations and therefore can be used to detect a user controlled player.
	 */
	public static final AI USER = new AI() {

		@Override
		public AIOperation processReinforce(AIController api) {
			throw new UnsupportedOperationException("This is a user contrlled player.");
		}

		@Override
		public AIOperation processAttack(AIController api) {
			throw new UnsupportedOperationException("This is a user contrlled player.");
		}

		@Override
		public AIOperation processFortify(AIController api) {
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
	 * The {@link Callable} which invokes the
	 * {@link #processReinforce(AIController)}.
	 */
	private final Callable<AIOperation> reinforce;

	/**
	 * The {@link Function} that is called when {@link #reinforce} is completed.
	 */
	private final Function<AIOperation, Boolean> completeReinforce;

	/**
	 * The {@link Callable} which invokes the {@link #processAttack(AIController)}.
	 */
	private final Callable<AIOperation> attack;

	/**
	 * The {@link Function} that is called when {@link #attack} is completed.
	 */
	private final Function<AIOperation, Boolean> completeAttack;

	/**
	 * The {@link Callable} which invokes the {@link #processFortify(AIController)}.
	 */
	private final Callable<AIOperation> fortify;

	/**
	 * The {@link Function} that is called when {@link #fortify} is completed.
	 */
	private final Function<AIOperation, Boolean> completeFortify;

	/**
	 * The {@link Executor} that performs the {@link AI} operations.
	 */
	private final ExecutorService executor;

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
	 * The result of the AI operation on another thread.
	 */
	private Future<AIOperation> future;

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
	 *            The {@link AIController} that this {@link AI} will use to query
	 *            the state of the game.
	 */
	public AI(String name, int defaultSpeed, AIController api) {
		this.name = name;
		this.wait = 0;
		this.api = api;
		setSpeed(defaultSpeed);
		this.future = null;
		this.executor = Executors.newSingleThreadExecutor();

		// The function that will be called when the reinforce function is completed.
		this.completeReinforce = result -> {

			if (result.processAgain) {
				api.select(result.select.get(0));
				api.reinforce();
			}

			return result.processAgain;

		};

		// The function that is to be completed when the AI reinforces.
		this.reinforce = () -> {
			return processReinforce(api);
		};

		// The function that will be called when the attack function is completed.
		this.completeAttack = result -> {

			if (result.processAgain) {

				api.select(result.select.get(0));
				api.select(result.select.get(1));
				api.attack();

			}

			return result.processAgain;

		};

		// The function that is to be completed when the AI attacks.
		this.attack = () -> {
			return processAttack(api);
		};

		// The function that will be called when the fortify function is completed.
		this.completeFortify = result -> {

			if (result.processAgain) {

				api.select(result.select.get(0));
				api.select(result.select.get(1));
				api.fortify();

			}

			return result.processAgain;

		};

		// The function that is to be completed when the AI fortify.
		this.fortify = () -> {
			return processFortify(api);
		};

	}

	/**
	 * Constructs the {@link AI#USER}.
	 */
	private AI() {
		this("None", MAX_SPEED, null);
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

		if (api.getCurrentPlayer().getDistributableArmy().getNumberOfUnits() == 0) {
			return false;
		}

		wait -= delta;
		return performOperation(reinforce, completeReinforce);
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

		wait -= delta;
		return performOperation(attack, completeAttack);

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

		wait -= delta;
		return performOperation(fortify, completeFortify);

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
	 * Retrieves the speed of this {@link AI}.
	 * 
	 * @return {@link AI} speed.
	 */
	public final int getSpeed() {
		return speed;
	}

	/**
	 * Perform the reinforce operation using the specified {@link AIController}.
	 * This operation should be specific to the specialised instance of the
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
	 * <li>Select a {@link Country} by adding one {@link Counrty} to
	 * {@link AIOperation#select} that is returned.</li>
	 * <li>Set {@link AIOperation#processAgain}, which is whether or not this
	 * {@link AI} wishes to perform another operation or not. If not the
	 * {@link AIController}</li>
	 * </ol>
	 * 
	 * @param api
	 *            {@link AIController}
	 * @return The {@link AIOperation} for this {@link AI}'s reinforce.
	 */
	protected abstract AIOperation processReinforce(AIController api);

	/**
	 * Perform the attack operation using the specified {@link AIController}. This
	 * operation should be specific to the specialised instance of the
	 * {@link AI}.<br>
	 * <br>
	 * Key aspects:
	 * <ul>
	 * <li>Only attack the target {@link Country} <strong>ONCE</strong>.</li>
	 * <li>The operation should be completely functional and rely on
	 * <strong>NO</strong> data that is not provided by the
	 * {@link AIController}.</li>
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
	 * <li>Select a primary {@link Country} by adding it first to
	 * {@link AIOperation#select} that is returned.</li>
	 * <li>Select a target {@link Country} by adding it second to
	 * {@link AIOperation#select} that is returned.</li>
	 * <li>Set {@link AIOperation#processAgain}, which is whether or not this
	 * {@link AI} wishes to perform another operation or not. If not the
	 * {@link AIController}</li>
	 * </ol>
	 * 
	 * The order that the {@link Country}s are selected matters. The primary
	 * {@link Country} <strong>MUST</strong> be selected first. <br>
	 * 
	 * @param api
	 *            {@link AIController}
	 * @return The {@link AIOperation} for this {@link AI}'s attack.
	 */
	protected abstract AIOperation processAttack(AIController api);

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
	 * <strong>NO</strong> data that is not provided by the
	 * {@link AIController}.</li>
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
	 * <li>Select a primary {@link Country} by adding it first to
	 * {@link AIOperation#select} that is returned.</li>
	 * <li>Select a target {@link Country} by adding it second to
	 * {@link AIOperation#select} that is returned.</li>
	 * <li>Set {@link AIOperation#processAgain}, which is whether or not this
	 * {@link AI} wishes to perform another operation or not. If not the
	 * {@link AIController}</li>
	 * </ol>
	 * 
	 * The order that the {@link Country}s are selected matters. The primary
	 * {@link Country} <strong>MUST</strong> be selected first. <br>
	 * 
	 * @param api
	 *            {@link AIController}
	 * @return The {@link AIOperation} for this {@link AI}'s fortify.
	 */
	protected abstract AIOperation processFortify(AIController api);

	/**
	 * Performs the specified {@link Callable} operation and stored the result in
	 * {@link #future}. When {@link #future} {@link Future#isDone()} this method
	 * will perform the specified {@link Function}.
	 * 
	 * @param operation
	 *            The {@link Callable} that contains the AIs calculations.
	 * @param onComplete
	 *            The {@link Function} that will be performed when the operation is
	 *            completed.
	 * @return Whether or not this {@link AI} wishes to perform another operation or
	 *         not.
	 */
	private boolean performOperation(Callable<AIOperation> operation, Function<AIOperation, Boolean> onComplete) {

		// If there is a future.
		if (future != null) {

			// If the AI operation has been completed.
			if (future.isDone()) {

				// Try to parse the result of the operation.
				try {

					// Retrieve the AIs operation and perform it.
					final AIOperation result = future.get();

					// Empty the future.
					future = null;

					return onComplete.apply(result);

				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

			}

		}
		// If the AI has waited the time between operations.
		else if (wait <= 0) {

			wait = speed;
			api.clearSelected();

			future = executor.submit(operation);

		}

		return true;
	}
}
