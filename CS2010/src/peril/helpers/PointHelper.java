package peril.helpers;

import peril.controllers.Points;

/**
 * Provides the point reward and cost amounts for in-game transactions..
 * Realises {@link Points}.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 * 
 * @since 2018-03-06
 * @version 1.01.04
 * 
 * @see Points
 *
 */

public final class PointHelper implements Points {

	/**
	 * The point reward for conquering a country.
	 */
	public static final int CONQUER_REWARD = 2;

	/**
	 * The point cost for blockading a link between two countries.
	 */
	public static final int BLOCKADE_COST = 4;

	/**
	 * The point cost for trading a set of units for the unit directly above that
	 * set of units in terms of strength.
	 */
	public static final int TRADE_UNIT_COST = 3;

	/**
	 * The singleton instance of the {@link PointHelper}.
	 */
	private static final PointHelper INSTANCE = new PointHelper();

	/**
	 * Constructs a new {@link PointHelper}.
	 */
	private PointHelper() {
		// Do nothing
	}

	/**
	 * Retrieves the singleton instance of {@link PointHelper}.
	 * 
	 * @return The {@link PointHelper}.
	 */
	public static PointHelper getInstance() {
		return INSTANCE;
	}

	@Override
	public int getConquer() {
		return CONQUER_REWARD;
	}

	@Override
	public int getUnitTrade() {
		return TRADE_UNIT_COST;
	}

	@Override
	public int getBlockade() {
		return BLOCKADE_COST;
	}

}
