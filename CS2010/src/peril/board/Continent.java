package peril.board;

import java.util.LinkedList;
import java.util.List;

import peril.Player;
import peril.Point;
import peril.ui.Viewable;

/**
 * Encapsulates the behaviour of a continent on the {@link Board}.
 * Continents;<br>
 * <ul>
 * <li>Group {@link Country}s</li>
 * <li>Apply a special visual effect when all the {@link Country}s with in it
 * are ruled by the same {@link Player}.</li>
 * <li>Award {@link Player} with bonuses when they rule all the {@link Country}s
 * within.</li>
 * </ul>
 * 
 * @author Joshua_Eddy
 *
 */
public final class Continent extends Viewable {

	/**
	 * Holds the {@link Countries} that comprise this {@link Continent}.
	 */
	private List<Country> countries;

	/**
	 * The {@link EnvironmentalHazard} that may affect this {@link Continent}.
	 */
	private EnvironmentalHazard hazard;

	/**
	 * The {@link name} of the {@link Continent}.
	 */
	private String name;

	/**
	 * The current {@link Player} that rules all {@link Country}s in this
	 * {@link Continent}. If all the {@link Country}s in this {@link Continent} are
	 * NOT ruled by the same {@link Player} then this is <code>null</code>. This is
	 * determined in {@link Continent#isRuled()}.
	 * 
	 */
	private Player ruler;

	/**
	 * Constructs a new {@link Continent}.
	 * 
	 * @param countries
	 */
	public Continent(EnvironmentalHazard hazard, String name) {

		this.countries = new LinkedList<Country>();
		this.hazard = hazard;
		this.ruler = null;
		this.name = name;

	}

	/**
	 * Adds a {@link Country} to the {@link Continent}.
	 * 
	 * @param country
	 */
	public void addCountry(Country country) {
		countries.add(country);
	}

	/**
	 * Returns the {@link Name} of the {@link Continent}.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Determines and retrieves whether or not one {@link Player} rules all the
	 * {@link Country}s in this {@link Continent}.
	 * 
	 * @return whether the ruler rules all {@link Country}s in the
	 *         {@link Continent}.
	 */
	public boolean isRuled() {
		// TODO: Whether all the countries are ruled by a particular player.
		return ruler != null;
	}

	/**
	 * Returns the current {@link Player} ruling this {@link Continent}.
	 * 
	 * @return {@link Player}.
	 */
	public Player getRuler() {
		return ruler;
	}

	/**
	 * Retrieves the country using a specified {@link Point} by iterating through
	 * all the {@link Country}s in the {@link Continent} and checks if the specifies
	 * {@link Point} is inside a {@link Country}.
	 * 
	 * @param click
	 *            {@link Point}.
	 * @return {@link Country}.
	 */
	public Country getCountry(Point click) {

		// Iterates through all the countries in this continent.
		for (Country currentCountry : countries) {

			// Checks if the specifies click is inside the bounds of the current country.
			if (currentCountry.isClicked(click)) {
				return currentCountry;
			}
		}
		// Will return null if the click is not inside a country.
		return null;
	}

	/**
	 * Iterates through all the @{@link Country}s in the {@link Continent} and
	 * performs their end of round operations.
	 */
	public void endRound() {
		countries.forEach(currentCountry -> currentCountry.endRound(hazard));
	}

	public void executeTurn() {

	}
}
