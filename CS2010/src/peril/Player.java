package peril;

import peril.board.Army;

/**
 * The internal representation of a user of the system. This object will hold
 * all of the details about a users game such as the number of {@link Country}s
 * that user has.
 * 
 * @author Joshua_Eddy
 * @author Ezekiel_Trinidad
 *
 */
public final class Player {

	/**
	 * Static instance of the first {@link Player}.
	 * 
	 */
	public static final Player PLAYERONE = new Player("Player 1");

	/**
	 * Static instance of the second {@link Player}.
	 * 
	 */
	public static final Player PLAYERTWO = new Player("Player 2");

	/**
	 * Static instance of the third {@link Player}.
	 * 
	 */
	public static final Player PLAYERTHREE = new Player("Player 3");

	/**
	 * Static instance of the fourth {@link Player}.
	 * 
	 */
	public static final Player PLAYERFOUR = new Player("Player 4");

	/**
	 * String Representation of the {@link Player}.
	 */
	private final String name;

	/**
	 * The {@link Army} of the {@link Player}.
	 * 
	 */
	private Army totalArmy;

	/**
	 * The number of the {@link Country}s the {@link Player} owns.
	 */
	private int countries;

	/**
	 * The number of {@link Continent}s the {@link Player} owns.
	 */
	private int continents;

	/**
	 * Constructs a new {@link Player}.
	 * 
	 * @param name
	 *            String Representation of the {@link Player}.
	 */
	private Player(String name) {
		this.name = name;
		countries = 0;
		continents = 0;
	}

	/**
	 * Awards the {@link Player} an {@link Army}.
	 * 
	 * @param army
	 *            The army that will be awarded to the {@link Player}.
	 */
	public void award(Army army) {
		
		// TODO Add army to the next turns set army that the player can distribute.

	}

	/**
	 * Sets the {@link Country}s owned by the {@link Player}.
	 * 
	 * @param countriesOwned
	 *            The number of {@link Country}s the {@link Player} now owns.
	 */
	public void setCountriesRuled(int countriesOwned) {
		countries = countriesOwned;
	}

	/**
	 * Gets the number of {@link Country}s owned by the {@link Player}.
	 * 
	 * @return countries Number of {@link Country}s.
	 */
	public int getCountriesOwned() {
		return countries;
	}

	/**
	 * Sets the {@link Continent}s owned by the {@link Player}.
	 * 
	 * @param continentsOwned
	 *            The number of {@link Continent}s the {@link Player} now owns.
	 */
	public void setContinentsOwned(int continentsOwned) {
		continents = continentsOwned;
	}

	/**
	 * Gets the number of {@link Continent}s owned by the {@link Player}.
	 * 
	 * @return continents Number of {@link Continent}s.
	 */
	public int getContinentsOwned() {
		return continents;
	}

	@Override
	public String toString() {
		return name;
	}
}
