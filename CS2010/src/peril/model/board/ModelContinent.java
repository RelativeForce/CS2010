package peril.model.board;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import peril.Update;
import peril.controllers.api.Continent;
import peril.controllers.api.Player;
import peril.model.ModelPlayer;

/**
 * Encapsulates the behaviour of a continent on the {@link ModelBoard}.
 * Continents;<br>
 * <ul>
 * <li>Group {@link ModelCountry}s</li>
 * <li>Apply a special visual effect when all the {@link ModelCountry}s with in
 * it are ruled by the same {@link Player}.</li>
 * <li>Award {@link Player} with bonuses when they rule all the
 * {@link ModelCountry}s within.</li>
 * </ul>
 * 
 * @author Joshua_Eddy
 *
 */
public final class ModelContinent extends Observable implements Continent, Observer {

	/**
	 * The {@link ModelHazard} that may affect this {@link ModelContinent}.
	 */
	public final ModelHazard hazard;

	/**
	 * Holds the {@link Countries} that comprise this {@link ModelContinent}.
	 */
	private final List<ModelCountry> countries;

	/**
	 * The {@link name} of the {@link ModelContinent}.
	 */
	private final String name;

	/**
	 * The current {@link Player} that rules all {@link ModelCountry}s in this
	 * {@link ModelContinent}. If all the {@link ModelCountry}s in this
	 * {@link ModelContinent} are NOT ruled by the same {@link Player} then this is
	 * <code>null</code>. This is determined in {@link ModelContinent#isRuled()}.
	 * 
	 */
	private ModelPlayer ruler;

	/**
	 * Constructs a new {@link ModelContinent}.
	 * 
	 * @param countries
	 */
	public ModelContinent(ModelHazard hazard, String name) {

		this.countries = new LinkedList<ModelCountry>();
		this.hazard = hazard;
		this.ruler = null;
		this.name = name;

	}

	/**
	 * Adds a {@link ModelCountry} to the {@link ModelContinent}.
	 * 
	 * @param country
	 */
	public void addCountry(ModelCountry country) {
		countries.add(country);
		
		country.addObserver(this);
		
		setChanged();
		notifyObservers(new Update("countries", countries));
	}

	/**
	 * Returns the {@link Name} of the {@link ModelContinent}.
	 * 
	 * @return
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

		setChanged();
		notifyObservers(new Update("ruler", ruler));

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
	 * Iterates through all the @{@link ModelCountry}s in the {@link ModelContinent}
	 * and performs their end of round operations.
	 */
	public void endRound() {
		countries.forEach(currentCountry -> currentCountry.endRound(hazard));
	}

	/**
	 * Retrieve the {@link List} of {@link ModelCountry}s for this
	 * {@link ModelContinent}.
	 * 
	 * @return {@link List} of {@link ModelCountry}s
	 */
	public List<ModelCountry> getCountries() {
		return countries;
	}

	/**
	 * Checks if this {@link ModelContinent} is ruled by a single {@link Player}.
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

	}

	/**
	 * Retrieves the {@link Player} owner of this€ {@link Continent}.
	 */
	@Override
	public Player getOwner() {
		return ruler;
	}

	
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof ModelCountry) {
			setChanged();
			notifyObservers();
		}
	}
}
