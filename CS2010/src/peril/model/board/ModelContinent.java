package peril.model.board;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import peril.Update;
import peril.ai.api.Continent;
import peril.ai.api.Player;
import peril.model.ModelPlayer;

/**
 * Encapsulates the behaviour of a continent on the {@link ModelBoard} which
 * consists of a {@link Set} of {@link ModelCountry}s.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-03-16
 * @version 1.01.02
 * 
 * @see ModelCountry
 * @see Observable
 * @see Observer
 * @see Continent
 *
 */
public final class ModelContinent extends Observable implements Continent, Observer {

	/**
	 * The {@link ModelHazard} that will effect the {@link ModelCountry} at the end
	 * of each round.
	 */
	public final ModelHazard hazard;

	/**
	 * Holds the {@link ModelCountry} that comprise this {@link ModelContinent}.
	 */
	private final Set<ModelCountry> countries;

	/**
	 * The name of the {@link ModelContinent}.
	 */
	private final String name;

	/**
	 * The current {@link ModelPlayer} that rules all {@link ModelCountry}s in this
	 * {@link ModelContinent}. If all the {@link ModelCountry}s in this
	 * {@link ModelContinent} are NOT ruled by the same {@link Player} then this is
	 * <code>null</code>. This is determined in {@link ModelContinent#isRuled()}.
	 */
	private ModelPlayer ruler;

	/**
	 * Constructs a new {@link ModelContinent} with no default ruler.
	 * 
	 * @param hazard
	 *            The {@link ModelHazard} that will effect the {@link ModelCountry}
	 *            at the end of each round.
	 * @param name
	 *            The name of the {@link ModelContinent}.
	 */
	public ModelContinent(ModelHazard hazard, String name) {

		this.countries = new HashSet<ModelCountry>();
		this.hazard = hazard;
		this.ruler = null;
		this.name = name;

	}

	/**
	 * Iterates through all the @{@link ModelCountry}s in the {@link ModelContinent}
	 * and performs their end of round operations.
	 */
	public void endRound() {
		countries.forEach(currentCountry -> currentCountry.endRound(hazard));
	}

	/**
	 * Adds a {@link ModelCountry} to the {@link ModelContinent}.
	 * 
	 * @param country
	 *            The {@link ModelCountry} that is to be added.
	 */
	public void addCountry(ModelCountry country) {

		countries.add(country);

		// Add this continent as an observer of the country so that when its changed the
		// continent is also changed.
		country.addObserver(this);

		setChanged();
		notifyObservers(new Update("countries", countries));
	}

	/**
	 * Updates this {@link ModelContinent} from an {@link Observable} changes.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof ModelCountry) {
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Returns the name of the {@link ModelContinent}.
	 * 
	 * @return The name of this {@link ModelContinent}.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Determines and retrieves whether or not one {@link Player} rules all the
	 * {@link ModelCountry}s in this {@link ModelContinent}.
	 * 
	 * @return whether the ruler rules all {@link ModelCountry}s in the
	 *         {@link ModelContinent}.
	 */
	public boolean isRuled() {

		// Check if this country is ruled.
		checkRuler();

		return ruler != null;
	}

	/**
	 * Returns the current {@link Player} ruling this {@link ModelContinent}.
	 * 
	 * @return {@link Player}.
	 */
	public ModelPlayer getRuler() {

		checkRuler();

		return ruler;
	}

	/**
	 * Retrieve the {@link Set} of {@link ModelCountry}s for this
	 * {@link ModelContinent}.
	 * 
	 * @return {@link Set} of {@link ModelCountry}s
	 */
	public Set<ModelCountry> getCountries() {
		return countries;
	}

	/**
	 * Retrieves the {@link Player} owner of this€ {@link Continent}.
	 */
	@Override
	public Player getOwner() {

		checkRuler();

		return ruler;
	}

	/**
	 * Checks if this {@link ModelContinent} is ruled by a single
	 * {@link ModelPlayer}.
	 */
	private void checkRuler() {

		// Holds the country that was last iterated over.
		ModelCountry previous = null;

		// Iterate through all the countries.
		for (ModelCountry country : countries) {

			/*
			 * If this is the first iteration of the loop set the ruler of this continent as
			 * the ruler of the current country.
			 */
			if (previous == null) {
				ruler = country.getRuler();
			}

			previous = country;

			/*
			 * If there is only one country then the ruler of that country will be the ruler
			 * of the continent otherwise check through all the countries and if an of them
			 * differ from that first countries ruler then there is not a single ruler of
			 * the continent. In that case the ruler should be set as null. If all the
			 * countries have the same ruler then set then the ruler of the first country
			 * ruler them all.
			 */
			if (ruler == null || !ruler.equals(previous.getRuler())) {
				ruler = null;
				break;
			}

		}

		setChanged();
		notifyObservers(new Update("ruler", ruler));

	}

	/**
	 * Retrieves {@link #hazard} {@link ModelHazard#maxCasualties}.
	 */
	@Override
	public int getMaxHazardCausalties() {
		return (hazard == null) ? 0 : hazard.maxCasualties;
	}

	/**
	 * Retrieves {@link #hazard} {@link ModelHazard#chance}.
	 */

	@Override
	public int getHazardFrequency() {
		return (hazard == null) ? 0 : hazard.chance;
	}

}
