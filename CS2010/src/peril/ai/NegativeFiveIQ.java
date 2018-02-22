package peril.ai;

import java.util.HashMap;
import java.util.Map;

import peril.controllers.AIController;
import peril.controllers.api.Country;
import peril.controllers.api.Player;

/**
 * 
 * An {@link AI} that's about as smart as an actual Duckling.
 * 
 * @author Ezekiel_Trinidad
 *
 */

public class NegativeFiveIQ extends AI {

	private static final String NAME = "Duckling";

	private static final int SPEED = 100;

	public NegativeFiveIQ(AIController api) {
		super(NAME, SPEED, api);

	}

	@Override
	protected boolean processReinforce(AIController api) {

		Map<Integer, Country> countries = getReinforceWeightings(api);

		int strongest = 0;

		for (int value : countries.keySet()) {
			if (value > strongest) {
				strongest = value;
			}
		}

		api.select(countries.get(strongest));
		api.reinforce();

		return true;
	}

	@Override
	protected boolean processAttack(AIController api) {
		
		Map<Integer, Entry> countries = getAttackWeightings(api);

		int highest = Integer.MIN_VALUE;

		for (int value : countries.keySet()) {
			highest = value > highest ? value : highest;
		}

		if (highest == Integer.MIN_VALUE) {
			return false;
		}

		api.select(countries.get(highest).a);
		api.select(countries.get(highest).b);

		api.attack();

		return true;
	}

	@Override
	protected boolean processFortify(AIController api) {
		
		Map<Integer, Country> countries = fortify(api);
		
		int weakest = 1;
		
		for (int value : countries.keySet()) {
			if(value < weakest) {
				value = weakest;
			}
		}
		
		return true;
	}

	// Assisted
	private Map<Integer, Country> getReinforceWeightings(AIController api) {
		Map<Integer, Country> countries = new HashMap<>();

		Player current = api.getCurrentPlayer();

		api.forEachCountry(country -> {

			if (current.equals(country.getOwner())) {

				int value = -country.getArmyStrength();

				for (Country neighbour : country.getNeighbours()) {

					if (!current.equals(neighbour.getOwner())) {
						value += neighbour.getArmyStrength();
					}
				}

				if (value != -country.getArmyStrength()) {
					countries.put(value, country);
				}

			}
		});

		return countries;
	}

	private Map<Integer, Entry> getAttackWeightings(AIController api) {

		Map<Integer, Entry> countries = new HashMap<>();

		Player current = api.getCurrentPlayer();

		api.forEachCountry(country -> {

			if (current.equals(country.getOwner()) && country.getArmy().getNumberOfUnits() > 1) {

				for (Country neighbour : country.getNeighbours()) {

					if(api.hasOpenLinkBetween(country, neighbour)) {
						
						int value = country.getArmyStrength();

						if (!current.equals(neighbour.getOwner())) {

							value -= neighbour.getArmyStrength();

							countries.put(value, new Entry(country, neighbour));
						}
					}
					

				}
			}
		});

		return countries;
	}
	
	private Map<Integer, Country> fortify(AIController api) {
		Map<Integer, Country> friendlies = new HashMap<>();
		
		Player current = api.getCurrentPlayer();
		
		api.forEachCountry(country -> {
			if(current.equals(country.getOwner())) {
				friendlies.put(country.getArmyStrength(), country);
			}
		});
		
		return friendlies;
	}
	
	//just gonna borrow this class, lmao
	/**
	 * Holds a pair of {@link Country}s.
	 * 
	 * @author Joshua_Eddy
	 */
	private class Entry {

		/**
		 * {@link Country} a
		 */
		public final Country a;

		/**
		 * {@link Country} b
		 */
		public final Country b;

		/**
		 * Constructs a new {@link Entry}.
		 * 
		 * @param a
		 * @param b
		 */
		public Entry(Country a, Country b) {
			this.a = a;
			this.b = b;
		}

	}
}
