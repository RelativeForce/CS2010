package peril.ai.api;

/**
 * 
 * Provides the point reward values of given actions in the game.
 * 
 * @author Joseph_Rolli. Joshua_Eddy
 * 
 * @version 1.01.02
 * @since 2018-02-21
 *
 */

public interface Points {

	/**
	 * The number of points a player receives when they conquer a country.
	 * 
	 * @return Points rewarded for conquering.
	 */
	int getConquer();

	/**
	 * The number of points that is required to trade a number of units for a more
	 * powerful unit.
	 * 
	 * @return The number of points.
	 */
	int getUnitTrade();

	/**
	 * The number of points that is required to block a link from one country to
	 * another.
	 * 
	 * @return The number of points.
	 */
	int getBlockade();
}
