package peril;

import org.newdawn.slick.Color;

import peril.ai.AI;
import peril.board.Army;
import peril.ui.Viewable;

/**
 * The internal representation of a user of the system. This object will hold
 * all of the details about a users game such as the number of {@link Country}s
 * that user has.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad
 *
 */
public final class Player extends Viewable implements peril.controllers.api.Player {

	/**
	 * Numerical Representation of the {@link Player}.
	 */
	public final int number;

	/**
	 * The {@link Color} of this {@link Player}s overlay on the
	 * {@link UserInterface}.
	 */
	public final Color color;

	/**
	 * The total {@link Army} of the {@link Player}.
	 * 
	 */
	public final Army totalArmy;

	/**
	 * The {@link Army} to be distributed by the {@link Player} at the start of the
	 * next turn.
	 * 
	 */
	public final Army distributableArmy;

	/**
	 * The {@link AI} that will control this {@link Player}. Assigned
	 * {@link AI#USER} for a user controlled player.
	 */
	public final AI ai;

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
	 * @param number
	 *            The number of this player
	 */
	public Player(int number, Color color, AI ai) {
		super(new Point(15, 45));
		this.countries = 0;
		this.continents = 0;
		this.color = color;
		this.distributableArmy = new Army(0);
		this.totalArmy = new Army(0);
		this.ai = ai;
		this.number = number;
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
	 * Gets the number of {@link Country}s ruled by the {@link Player}.
	 * 
	 * @return countries Number of {@link Country}s.
	 */
	public int getCountriesRuled() {
		return countries;
	}

	/**
	 * Sets the {@link Continent}s ruled by the {@link Player}.
	 * 
	 * @param continentsRuled
	 *            The number of {@link Continent}s the {@link Player} now owns.
	 */
	public void setContinentsRuled(int continentsRuled) {
		continents = continentsRuled;
	}

	/**
	 * Gets the number of {@link Continent}s owned by the {@link Player}.
	 * 
	 * @return continents Number of {@link Continent}s.
	 */
	public int getContinentsRuled() {
		return continents;
	}

	/**
	 * Gets the name of this {@link Player}.
	 */
	@Override
	public String toString() {
		return "Player " + number;
	}

	/**
	 * Resents this players fields back to their default values.
	 */
	public void reset() {

		totalArmy.setSize(0);
		distributableArmy.setSize(0);
		countries = 0;
		continents = 0;

	}

	/**
	 * Retrieves the total size of this {@link Player}s army.
	 */
	public int getTotalArmySize() {
		return totalArmy.getSize();
	}

	/**
	 * Retrieves the distribute-able army size.
	 */
	@Override
	public int getDistributableArmySize() {
		return distributableArmy.getSize();
	}

}
