package peril.ai;

import java.util.HashMap;
import java.util.Map;

import peril.ai.api.Country;
import peril.ai.api.Player;

/**
 * 
 * An {@link AI} that's about as smart as an actual Duckling.
 * 
 * @author Ezekiel_Trinidad
 * 
 * @version 1.01.02
 * @since 2018-03-11
 *
 * @see AI
 */

public final class NegativeFiveIQ extends AI {

	private static final String NAME = "Duckling";

	public NegativeFiveIQ(AIController api) {
		super(NAME, MAX_SPEED, api);

	}

	@Override
	protected AIOperation processReinforce(AIController api) {

		Map<Integer, Country> countries = getReinforceWeightings(api);

		int strongest = 0;

		for (int value : countries.keySet()) {
			if (value > strongest) {
				strongest = value;
			}
		}

		final AIOperation op = new AIOperation();
		op.select.add(countries.get(strongest));
		op.processAgain = true;

		return op;
	}

	@Override
	protected AIOperation processAttack(AIController api) {

		Map<Integer, Entry> countries = getAttackWeightings(api);

		int highest = Integer.MIN_VALUE;

		for (int value : countries.keySet()) {
			highest = value > highest ? value : highest;
		}

		final AIOperation op = new AIOperation();

		if (highest == Integer.MIN_VALUE) {
			op.processAgain = false;
		} else {
			op.select.add(countries.get(highest).a);
			op.select.add(countries.get(highest).b);
			op.processAgain = true;
		}

		return op;
	}

	@Override
	protected AIOperation processFortify(AIController api) {

		Map<Integer, Country> countries = fortify(api);

		int weakest = 1;

		for (int value : countries.keySet()) {
			if (value < weakest) {
				value = weakest;
			}
		}

		return new AIOperation();
	}

	// Assisted
	private Map<Integer, Country> getReinforceWeightings(AIController api) {
		Map<Integer, Country> countries = new HashMap<>();

		Player current = api.getCurrentPlayer();

		api.forEachCountry(country -> {

			if (current.equals(country.getOwner())) {

				int value = -country.getArmy().getStrength();

				for (Country neighbour : country.getNeighbours()) {

					if (!current.equals(neighbour.getOwner())) {
						value += neighbour.getArmy().getStrength();
					}
				}

				if (value != -country.getArmy().getStrength()) {
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

					if (api.hasOpenLinkBetween(country, neighbour)) {

						int value = country.getArmy().getStrength();

						if (!current.equals(neighbour.getOwner())) {

							value -= neighbour.getArmy().getStrength();

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
			if (current.equals(country.getOwner())) {
				friendlies.put(country.getArmy().getStrength(), country);
			}
		});

		return friendlies;
	}

	// just gonna borrow this class, lmao
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
		 *            {@link Country} a
		 * @param b
		 *            {@link Country} b
		 */
		public Entry(Country a, Country b) {
			this.a = a;
			this.b = b;
		}

	}
}
