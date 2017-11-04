package peril;

import java.nio.channels.NetworkChannel;

import org.newdawn.slick.Color;

import peril.board.Army;
import peril.ui.UserInterface;

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
	public static final Player PLAYERONE = new Player("Player 1", Color.red);

	/**
	 * Static instance of the second {@link Player}.
	 * 
	 */
	public static final Player PLAYERTWO = new Player("Player 2", Color.blue);

	/**
	 * Static instance of the third {@link Player}.
	 * 
	 */
	public static final Player PLAYERTHREE = new Player("Player 3", Color.green);

	/**
	 * Static instance of the fourth {@link Player}.
	 * 
	 */
	public static final Player PLAYERFOUR = new Player("Player 4", Color.yellow);

	/**
	 * String Representation of the {@link Player}.
	 */
	private final String name;

	/**
	 * The {@link Color} of this {@link Player}s overlay on the
	 * {@link UserInterface}.
	 */
	private final Color color;

	/**
	 * The total {@link Army} of the {@link Player}.
	 * 
	 */
	private Army totalArmy;

	
	/**
	 * The amount of distributable {@link Army} the {@link Player} has.
	 * 
	 */
	private Army distributableArmy;
	
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
	private Player(String name, Color color) {
		this.name = name;
		this.countries = 0;
		this.continents = 0;
		this.color = color;
	}

	/**
	 * Awards the {@link Player} an {@link Army}.
	 * 
	 * @param army
	 *            The army that will be awarded to the {@link Player}.
	 */
	public void award(Army army) {

		// TODO Add army to the next turns set army that the player can distribute.
		distributableArmy.setSize(distributableArmy.getSize() + army.getSize());

	}
	
	/**
	 * Sets the distributable {@link Army} the {@link Player} will have.
	 * 
	 * @param newArmy The new distributable {@link Army}.
	 */
	public void setDistributableArmy(Army newArmy) {
		distributableArmy = newArmy;
	}
	
	
	public Army getDistributableArmy() {
		return distributableArmy;
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
	 * Gets the {@link Player}'s color
	 * 
	 */
	public Color getColor() {
		return color;
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
