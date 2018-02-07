package peril.model;

import java.util.Observable;

import peril.Update;
import peril.ai.AI;
import peril.controllers.api.Player;
import peril.model.board.ModelArmy;

/**
 * The internal representation of a user of the system. This object will hold
 * all of the details about a users game such as the number of
 * {@link ModelCountry}s that user has.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad
 *
 */
public final class ModelPlayer extends Observable implements Player {

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
	 * The {@link ModelArmy} to be distributed by the {@link ModelPlayer} at the
	 * start of the next turn.
	 * 
	 */
	public final ModelArmy distributableArmy;

	/**
	 * The {@link AI} that will control this {@link ModelPlayer}. Assigned
	 * {@link AI#USER} for a user controlled player.
	 */
	public final AI ai;

	/**
	 * The amount of points the {@link ModelPlayer} will have.
	 */
	private int points;

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
		this.distributableArmy = new ModelArmy();
		this.totalArmy = new ModelArmy();
		this.ai = ai;
		this.number = number;
		this.points = 0;
	}

	/**
	 * Sets the {@link ModelCountry}s owned by the {@link ModelPlayer}.
	 * 
	 * @param countriesRuled
	 *            The number of {@link ModelCountry}s the {@link ModelPlayer} now
	 *            owns.
	 */
	public void setCountriesRuled(int countriesRuled) {
		countries = countriesRuled;

		notifyObservers(new Update("countries", countries));
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
	 *            The number of {@link ModelContinent}s the {@link ModelPlayer} now
	 *            owns.
	 */
	public void setContinentsRuled(int continentsRuled) {
		continents = continentsRuled;

		notifyObservers(new Update("continents", continentsRuled));
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

		totalArmy.setStrength(0);
		distributableArmy.setStrength(0);

		// Reset countries ruled.
		countries = 0;
		notifyObservers(new Update("countries", countries));

		// Reset continents ruled.
		continents = 0;
		notifyObservers(new Update("continents", continents));

	}

	/**
	 * Retrieves the total size of this {@link ModelPlayer}s army.
	 */
	public int getTotalArmyStrength() {
		return totalArmy.getStrength();
	}

	/**
	 * Retrieves the distribute-able army size.
	 */
	@Override
	public int getDistributableArmyStrength() {
		return distributableArmy.getStrength();
	}

	/**
	 * Retrieves the amount of points this {@link ModelPlayer} has.
	 */
	public int getPoints() {
		return this.points;
	}

	/**
	 * Sets the amount of points this {@link ModelPlayer} has.
	 * @param points The new amount of points the {@link ModelPlayer} will have.
	 */
	public void setPoints(int points) {
		this.points = points;
	}

}
