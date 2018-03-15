package peril;

import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;

/**
 * An objective that a {@link ModelPlayer} can complete to gain a bonus.
 * 
 * @author Joshua_Eddy
 *
 * @version 1.01.01
 * @since 2018-02-06
 */
public abstract class Challenge {

	/**
	 * The type of the {@link Challenge} stored, such as countries owned or troops
	 * accumulated
	 */
	public final String type;

	/**
	 * The goal of the {@link Challenge} to be reached by the players
	 */
	public final int goal;

	/**
	 * The extra troops received for completing the goal in the {@link Challenge}
	 */
	public final int reward;

	/**
	 * Constructs a new {@link Challenge}.
	 * 
	 * @param type
	 *            The type of {@link Challenge}.
	 * @param goal
	 *            The number goal of the {@link Challenge}.
	 * @param reward
	 *            The troop reward of the {@link Challenge}.
	 */
	public Challenge(String type, int goal, int reward) {
		this.type = type;
		this.goal = goal;
		this.reward = reward;
	}

	/**
	 * Checks if specified {@link ModelPlayer} has completed an {@link Challenge} or
	 * not. If the {@link Challenge} has been completed then the specified
	 * {@link ModelPlayer} will receive a reward.
	 * 
	 * @param player
	 *            {@link ModelPlayer} that will be checked.
	 * @param board
	 *            The {@link ModelBoard} the {@link ModelPlayer} is on.
	 * @return <code>boolean</code> {@link Challenge} completion status.
	 */
	public abstract boolean hasCompleted(ModelPlayer player, ModelBoard board);

	/**
	 * The string representation of the {@link Challenge} when it has been
	 * completed.
	 * 
	 * @return <code>String</code>
	 */
	public String completed() {
		return "Challenge Completed: " + this.toString();
	}

}
