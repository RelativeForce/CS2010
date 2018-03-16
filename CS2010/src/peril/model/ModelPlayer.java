package peril.model;

import java.util.Observable;

import peril.Update;
import peril.ai.AI;
import peril.ai.api.Army;
import peril.ai.api.Player;
import peril.model.board.ModelArmy;
import peril.model.board.ModelCountry;
import peril.model.board.ModelContinent;

/**
 * The internal representation of a player of the game. This object will hold
 * all of the tracked statistics about a player.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad, Joseph_Rolli
 * 
 * @since 2018-03-09
 * @version 1.01.04
 * 
 * @see Player
 * @see Observable
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
	 * @see ModelArmy
	 */
	public final ModelArmy totalArmy;

	/**
	 * The {@link ModelArmy} to be distributed by the {@link ModelPlayer} at the
	 * start of the next turn.
	 * 
	 * @see ModelArmy
	 * 
	 */
	public final ModelArmy distributableArmy;

	/**
	 * The {@link AI} that will control this {@link ModelPlayer}. Assigned
	 * {@link AI#USER} for a user controlled player.
	 * 
	 * @see AI
	 * @see AI#USER
	 */
	public final AI ai;

	/**
	 * The amount of points the {@link ModelPlayer} has to spend.
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
	 * The number of units the {@link ModelPlayer} has killed.
	 */
	private int unitsKilled;

	/**
	 * The number of {@link ModelCountry}s the {@link ModelPlayer} has conquered.
	 */
	private int countriesTaken;

	/**
	 * The number of points the {@link ModelPlayer} has spent.
	 */
	private int pointsSpent;

	/**
	 * Constructs a new {@link ModelPlayer}.
	 * 
	 * @param number
	 *            The number of this player
	 * @param ai
	 *            The {@link AI} that will control this {@link ModelPlayer}.
	 *            Assigned {@link AI#USER} for a user controlled player.
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
	@Override
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
	@Override
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
	@Override
	public int getPoints() {
		return this.points;
	}

	/**
	 * Sets the amount of points this {@link ModelPlayer} has.
	 * 
	 * @param points
	 *            The new amount of points the {@link ModelPlayer} will have.
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * Adds points to this {@link ModelPlayer}.
	 * 
	 * @param points
	 *            points to add.
	 */
	public void addPoints(int points) {
		this.points += points;
	}

	/**
	 * Retrieves the distribute-able {@link Army} of this {@link ModelPlayer}.
	 */
	@Override
	public Army getDistributableArmy() {
		return distributableArmy;
	}

	/**
	 * Retrieves the total {@link Army} of this {@link ModelPlayer}.
	 */
	@Override
	public Army getTotalArmy() {
		return totalArmy;
	}

	/**
	 * Retrieves the total number of units killed by this {@link ModelPlayer}.
	 * 
	 * @return The number of units killed.
	 */
	public int getUnitsKilled() {
		return unitsKilled;
	}

	/**
	 * Sets the total units killed by this {@link ModelPlayer}.
	 * 
	 * @param unitsKilled
	 *            The number of unit killed.
	 */
	public void setUnitsKilled(int unitsKilled) {
		this.unitsKilled = unitsKilled;
	}

	/**
	 * Retrieves the total {@link ModelCountry}s this {@link ModelPlayer} has taken.
	 * 
	 * @return The number of {@link ModelCountry}s taken.
	 */
	public int getCountriesTaken() {
		return countriesTaken;
	}

	/**
	 * Sets the total {@link ModelCountry}s this {@link ModelPlayer} has taken.
	 * 
	 * @param countriesTaken
	 *            The number of {@link ModelCountry}s taken.
	 */
	public void setCountriesTaken(int countriesTaken) {
		this.countriesTaken = countriesTaken;
	}

	/**
	 * Retrieves the total points this {@link ModelPlayer} has spent.
	 * 
	 * @return The number of points spend.
	 */
	public int getPointsSpent() {
		return pointsSpent;
	}

	/**
	 * Spends a specified amount to the players points. The number of points the
	 * player has {@link #getPoints()} must remain greater than or equal to zero.
	 * 
	 * @param points
	 *            The number of points to spend.
	 */
	public void spendPoints(int points) {

		if (this.points - points < 0) {
			throw new IllegalArgumentException("This player has insufficent points to spend " + points + " points.");
		}
		this.points -= points;
		this.pointsSpent += points;
	}

	/**
	 * Sets the total points this {@link ModelPlayer} has spent.
	 * 
	 * @param pointsSpent
	 *            The number of points spent.
	 */
	public void setPointsSpent(int pointsSpent) {
		this.pointsSpent = pointsSpent;
	}

	/**
	 * Adds units killed to this {@link ModelPlayer}'s total units killed.
	 * 
	 * @param unitsKilled
	 *            The number of units killed.
	 */
	public void addUnitsKilled(int unitsKilled) {
		this.unitsKilled += unitsKilled;
	}
}
