package peril.model.board;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.function.Consumer;

import peril.Game;
import peril.Update;
import peril.controllers.api.Board;
import peril.controllers.api.Country;

/**
 * Encapsulates the behaviour of the the game board in the {@link Game}. This
 * realises {@link Viewable} allowing it to be displayed by the
 * {@link UserInterface}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class ModelBoard extends Observable implements Board {

	/**
	 * The {@link ModelContinent}s in this {@link ModelBoard}.
	 */
	private final Map<String, ModelContinent> continents;

	/**
	 * The number of {@link ModelCountry}s in the {@link ModelBoard}.
	 */
	private int numberOfCountries;

	/**
	 * Holds the name of this {@link ModelBoard}.
	 */
	private String name;

	/**
	 * Constructs a {@link ModelBoard}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link ModelBoard} is a part of.
	 * @param name
	 *            The name of the {@link ModelBoard}.
	 */
	public ModelBoard(String name) {
		this.continents = new HashMap<>();
		this.numberOfCountries = 0;
		this.name = name;
	}

	/**
	 * Sets this {@link ModelBoard}'s {@link List} of {@link ModelContinent}s.
	 * 
	 * @param newContinents
	 *            new {@link List} of {@link ModelContinent}s.
	 */
	public void setContinents(Set<ModelContinent> newContinents) {

		if (newContinents == null) {
			throw new NullPointerException("Continents is null.");
		}

		reset();
		newContinents.forEach(continent -> continents.put(continent.getName(), continent));

		for (ModelContinent continent : newContinents) {
			numberOfCountries += continent.getCountries().size();
		}

		setChanged();
		notifyObservers(new Update("continents", continents));

	}

	/**
	 * Retrieves the {@link ModelContinent}s on this {@link ModelBoard}.
	 * 
	 * @return {@link List} of {@link ModelContinent}s.
	 * 
	 * @see java.util.List
	 */
	public Map<String, ModelContinent> getContinents() {
		return continents;
	}

	/**
	 * Iterates through each {@link ModelContinent} in the {@link ModelBoard} and
	 * {@link ModelContinent#executeTurn()}.
	 */
	public void endRound() {
		continents.values().forEach(continent -> continent.endRound());
	}

	/**
	 * Retrieves the number of {@link ModelCountry}s on this {@link ModelBoard}.
	 * 
	 * @return <code>int</code>
	 */
	public int getNumberOfCountries() {
		return numberOfCountries;
	}

	/**
	 * Retrieves the name of this {@link ModelBoard}.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Performs a {@link Consumer} function on each {@link ModelCountry} on the
	 * {@link ModelBoard}.
	 * 
	 * @param task
	 *            {@link Consumer}
	 */
	public void forEachCountry(Consumer<ModelCountry> task) {
		continents.values().forEach(continent -> continent.getCountries().forEach(task));
	}

	/**
	 * Set the name of this {@link ModelBoard}.
	 * 
	 * @param name
	 *            name of the {@link ModelBoard}.
	 */
	public void setName(String name) {
		this.name = name;

		setChanged();
		notifyObservers(new Update("name", name));
	}

	/**
	 * Clear all the {@link ModelContinent}s from the {@link Game} thus deleting all
	 * the {@link ModelCountry}s and reseting the {@link ModelBoard} to its initial
	 * state.
	 */
	public void reset() {
		continents.clear();
		numberOfCountries = 0;
	}

	/**
	 * Retrieves the {@link Set} of all the {@link Country}s that are on this
	 * {@link Board}.
	 */
	@Override
	public Set<? extends Country> getCountries() {

		HashSet<Country> countries = new HashSet<>();

		// Add each continents countries to the set
		continents.forEach((continentName, continent) -> countries.addAll(continent.getCountries()));

		return countries;
	}

}
