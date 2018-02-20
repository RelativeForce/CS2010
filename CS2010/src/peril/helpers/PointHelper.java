package peril.helpers;

import peril.controllers.Points;

/**
 * Provides the point reward amounts for in-game actions. Realises
 * {@link Points}.
 * 
 * @author Joseph_Rolli
 * 
 * @since 2018-02-20
 * @version 1.01.01
 * 
 * @see Points
 *
 */

public final class PointHelper implements Points {

	private static final PointHelper INSTANCE = new PointHelper();

	/**
	 * Points rewarded for conquering a country.
	 */
	private static final int CONQUER = 2;

	/**
	 * Constructs a new {@link PointHelper}.
	 */
	private PointHelper() {

	}

	@Override
	public int getConquer() {
		return CONQUER;
	}

	public static PointHelper getInstance() {
		return INSTANCE;
	}

}
