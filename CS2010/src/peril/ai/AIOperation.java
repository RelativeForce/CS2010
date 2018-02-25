package peril.ai;

import java.util.LinkedList;
import java.util.List;

import peril.controllers.AIController;
import peril.controllers.api.Country;

/**
 * 
 * Defines a instruction for the {@link AIController} to execute on the
 * graphical thread.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-25
 * @version 1.01.01
 * 
 * @see AI
 *
 */
public final class AIOperation {

	/**
	 * Whether the {@link AI} should perform another operation or move to the next
	 * state.
	 */
	public boolean processAgain;

	/**
	 * The {@link Country}'s the {@link AI} has specified to be selected in the
	 * order they appear in the list. Index 0 first.
	 */
	public final List<Country> select;

	/**
	 * Constructs a new {@link AIOperation}.
	 */
	public AIOperation() {
		this.select = new LinkedList<>();
		this.processAgain = false;
	}

}