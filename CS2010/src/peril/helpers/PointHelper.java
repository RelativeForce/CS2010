package peril.helpers;

import peril.controllers.Points;

/**
 * Provides the point reward amounts for in-game actions. Realises
 * {@link Points}.
 * 
 * @author Joseph_Rolli
 * 
 * @since 2018-02-21
 * @version 1.01.02
 * 
 * @see Points
 *
 */

public final class PointHelper implements Points {

	private static final PointHelper INSTANCE = new PointHelper();

	/**
	 * Points rewarded for conquering a country.
	 */
	public static final int CONQUER_COST = 2;

	public static final int BLOCKADE_COST = 4;

	public static final int TRADE_UNIT_COST = 3;

	/**
	 * Constructs a new {@link PointHelper}.
	 */
	private PointHelper() {
		// Do nothing
	}

	public static PointHelper getInstance() {
		return INSTANCE;
	}

	@Override
	public int getConquer() {
		return CONQUER_COST;
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
