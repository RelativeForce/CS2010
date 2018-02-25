package peril.model.states.combat;

import java.util.Observable;

import peril.model.board.ModelUnit;

/**
 * A member of a {@link ModelSquad} this object wraps a {@link ModelUnit} an
 * gives it the additional state of being dead or alive.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-22
 * @version 1.01.01
 * 
 * @see ModelSquad
 * @see ModelUnit
 *
 */
public final class ModelSquadMember extends Observable {

	/**
	 * The {@link ModelUnit} that denotes this member of the squad.
	 */
	public final ModelUnit unit;

	/**
	 * The {@link ModelSquadMemberState} of this {@link ModelSquadMember}.
	 */
	public ModelSquadMemberState state;

	/**
	 * Constructs an new {@link ModelSquadMember}.
	 * 
	 * @param unit
	 *            The {@link ModelUnit} that denotes this member of the squad.
	 * @param state
	 *            The {@link ModelSquadMemberState} of this
	 *            {@link ModelSquadMember}.
	 */
	public ModelSquadMember(ModelUnit unit, ModelSquadMemberState state) {
		this.unit = unit;
		this.state = state;
	}

}
