package peril.ai.api;

/**
 * 
 * Provides the point reward values of given actions in the game.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 * 
 * @version 1.01.02
 * @since 2018-03-16
 *
 */
public interface Points {

	/**
	 * The number of points a player receives when they conquer a country.
	 * 
	 * @return The points rewarded for conquering.
	 */
	int getConquer();

	/**
	 * The number of points that is required to trade a number of {@link Unit}s for a more
	 * powerful {@link Unit}.
	 * 
	 * @return The number of points required to trade.
	 */
	int getUnitTrade();

	/**
	 * The number of points that is required to block a link from one {@link Country} to
	 * another.
	 * 
	 * @return The number of points.
	 */
	int getBlockade();
}
