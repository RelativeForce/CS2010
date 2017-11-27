package peril;

import peril.board.Board;

/**
 * An objective that a {@link Player} can complete to gain a bonus.
 * 
 * @author Joshua_Eddy
 *
 */
public abstract class Challenge {

	public final String type;
	public final int goal;
	public final int reward;
	
	public Challenge(String type, int goal, int reward) {
		this.type = type;
		this.goal = goal;
		this.reward = reward;
	}
	
	/**
	 * Checks if specified {@link Player} has completed an {@link Challenge} or not.
	 * If the {@link Challenge} has been completed then the specified {@link Player}
	 * will receive a reward.
	 * 
	 * @param player
	 *            {@link Player} that will be checked.
	 * @param board
	 *            The {@link Board} the {@link Player} is on.
	 * @return <code>boolean</code> {@link Challenge} completion status.
	 */
	public abstract boolean hasCompleted(Player player, Board board);
	
	@Override
	public String toString() {
		return "Challenge Completed: ";
	}
	
	

}
