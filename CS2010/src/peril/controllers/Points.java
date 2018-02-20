package peril.controllers;

/**
 * 
 * Provides the point reward values of given actions in the game.
 * 
 * @author Joseph_Rolli
 * 
 * @version 1.01.01
 * @since 2018-02-20
 *
 */

public interface Points {
	
	/**
	 * The number of points a player recieves when they conquer a country.
	 * 
	 * @return Points rewarded for conquering.
	 */
	int getConquer();
}
