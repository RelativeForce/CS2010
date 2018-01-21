package peril.model;

import peril.ai.AI;
import peril.controllers.api.Player;
import peril.model.board.ModelArmy;

/**
 * The internal representation of a user of the system. This object will hold
 * all of the details about a users game such as the number of {@link ModelCountry}s
 * that user has.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad
 *
 */
public final class ModelPlayer implements Player {

	/**
	 * Numerical Representation of the {@link ModelPlayer}.
	 */
	public final int number;

	/**
	 * The total {@link ModelArmy} of the {@link ModelPlayer}.
	 * 
	 */
	public final ModelArmy totalArmy;

	/**
	 * The {@link ModelArmy} to be distributed by the {@link ModelPlayer} at the start of the
	 * next turn.
	 * 
	 */
	public final ModelArmy distributableArmy;

	/**
	 * The {@link AI} that will control this {@link ModelPlayer}. Assigned
	 * {@link AI#USER} for a user controlled player.
	 */
	public final AI ai;

	/**
	 * The number of the {@link ModelCountry}s the {@link ModelPlayer} owns.
	 */
	private int countries;

	/**
	 * The number of {@link ModelContinent}s the {@link ModelPlayer} owns.
	 */
	private int continents;

	/**
	 * Constructs a new {@link ModelPlayer}.
	 * 
	 * @param number
	 *            The number of this player
	 */
	public ModelPlayer(int number, AI ai) {
		this.countries = 0;
		this.continents = 0;
		this.distributableArmy = new ModelArmy(0);
		this.totalArmy = new ModelArmy(0);
		this.ai = ai;
		this.number = number;
	}

	/**
	 * Sets the {@link ModelCountry}s owned by the {@link ModelPlayer}.
	 * 
	 * @param countriesOwned
	 *            The number of {@link ModelCountry}s the {@link ModelPlayer} now owns.
	 */
	public void setCountriesRuled(int countriesOwned) {
		countries = countriesOwned;
	}

	/**
	 * Gets the number of {@link ModelCountry}s ruled by the {@link ModelPlayer}.
	 * 
	 * @return countries Number of {@link ModelCountry}s.
	 */
	public int getCountriesRuled() {
		return countries;
	}

	/**
	 * Sets the {@link ModelContinent}s ruled by the {@link ModelPlayer}.
	 * 
	 * @param continentsRuled
	 *            The number of {@link ModelContinent}s the {@link ModelPlayer} now owns.
	 */
	public void setContinentsRuled(int continentsRuled) {
		continents = continentsRuled;
	}

	/**
	 * Gets the number of {@link ModelContinent}s owned by the {@link ModelPlayer}.
	 * 
	 * @return continents Number of {@link ModelContinent}s.
	 */
	public int getContinentsRuled() {
		return continents;
	}

	/**
	 * Gets the name of this {@link ModelPlayer}.
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
	 * Retrieves the total size of this {@link ModelPlayer}s army.
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
