package peril.controllers.api;

/**
 * Provides information about a player.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.03
 * @since 2018-02-20
 *
 */
public interface Player {

	/**
	 * Retrieves the strength of all the units in the {@link Country}s that this
	 * {@link Player} owns combined.
	 * 
	 * @return Total army strength.
	 * 
	 * @deprecated use {@link Player#getTotalArmy()} then {@link Army#getStrength()}
	 */
	int getTotalArmyStrength();

	/**
	 * Retrieves the number of {@link Country}s this {@link Player} rules.
	 * 
	 * @return Number of {@link Country}s ruled.
	 */
	int getCountriesRuled();

	/**
	 * Retrieves the number of {@link Continent}s this {@link Player} rules
	 * 
	 * @return Number of {@link Continent}s ruled.
	 */
	int getContinentsRuled();

	/**
	 * Retrieves the strength of the army that this {@link Player} has available to
	 * distribute during the reinforce state of the game.
	 * 
	 * @return Distribute-able army strength
	 * 
	 * @deprecated use {@link Player#getDistributableArmy()} then
	 *             {@link Army#getStrength()}
	 */
	int getDistributableArmyStrength();

	/**
	 * Retrieves the {@link Army} that this {@link Player} has available to
	 * distribute during the reinforce state of the game.
	 * 
	 * @return Distribute-able {@link Army}
	 * 
	 */
	Army getDistributableArmy();

	/**
	 * Retrieves all the units in the {@link Country}s that this {@link Player} owns
	 * combined.
	 * 
	 * @return Total {@link Army}.
	 * 
	 */
	Army getTotalArmy();

	/**
	 * Retrieves all the number of points that this {@link Player} has.
	 * 
	 * @return Number of points player has.
	 * 
	 */
	int getPoints();

}
