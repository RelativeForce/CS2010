package peril.controllers.api;

import java.util.Set;

/**
 * Provides the information of a country.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-02-06
 *
 */
public interface Country {

	/**
	 * Returns the strength of this {@link Country}'s army.
	 * 
	 * @return strength of army.
	 */
	int getArmyStrength();

	/**
	 * Retrieves the {@link Player} the rules <code>this</code> {@link Country}.
	 * <code>null</code> is the {@link Country} is not ruled.
	 * 
	 * @return {@link Player}
	 */
	Player getOwner();

	/**
	 * Retrieves the {@link Set} of neighbours of this {@link Country}.
	 * 
	 * @return {@link Set} of {@link Country}s.
	 */
	Set<? extends Country> getNeighbours();
}
