package peril.model.combat;

import peril.model.board.ModelArmy;

/**
 * The states that a {@link ModelSquadMember} can have in a {@link ModelSquad}.
 * @author Joshua_Eddy
 * 
 * @since 2018-02-25
 * @version 1.01.01
 * 
 * @see ModelSquad
 * @see ModelSquadMember
 *
 */
public enum ModelSquadMemberState {

	/**
	 * The {@link ModelSquadMember} is alive and present in the {@link ModelSquad}.
	 */
	ALIVE,
	/**
	 * The {@link ModelSquadMember} is dead but still in the {@link ModelSquad}.
	 */
	DEAD,
	/**
	 * The {@link ModelSquadMember} has been returned to its {@link ModelArmy} and
	 * is not longer in the {@link ModelSquad}.
	 */
	RETURNED;

}
