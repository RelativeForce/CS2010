package peril.ai.api;

import java.util.List;

/**
 * Encapsulates a country on the {@link Board}.
 * @author Joshua_Eddy
 *
 */
public interface Country {

	/**
	 * Returns the size of this {@link Country}'s army.
	 * 
	 * @return size of army.
	 */
	int getArmySize();

	/**
	 * Retrieves the {@link Player} the rules <code>this</code> {@link Country}.
	 * 
	 * @return {@link Player}
	 */
	Player getOwner();

	/**
	 * Retrieves the {@link Country#neighbours}.
	 * 
	 * @return {@link List} of type {@link Country}.
	 */
	List<? extends Country> getNeighbours();
}
