package peril.ai.api;

import java.util.Set;

/**
 * Provides the information of a country.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.02
 * @since 2018-02-11
 *
 */
public interface Country {

	/**
	 * @deprecated use {@link Country#getArmy()} then {@link Army#getStrength()}
	 * 
	 * Returns the strength of this {@link Country}'s army.
	 * 
	 * @return strength of army.
	 */
	int getArmyStrength();
	
	/**
	 * Returns this {@link Country}'s {@link Army}.
	 * 
	 * @return {@link Army}
	 */
	Army getArmy();

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
