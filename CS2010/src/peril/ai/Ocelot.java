package peril.ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import peril.ai.api.Country;
import peril.ai.api.Player;
import peril.ai.AIController;

/**
 * A hopefully somewhat clever AI. We'll see. {@link AI}.
 * 
 * @author Joseph_Rolli
 * 
 * @version 1.01.01
 * @since 2018-02-10
 *
 */
public final class Ocelot extends AI {

	/**
	 * The name of this {@link Ocelot}.
	 */
	private static final String NAME = "Ocelot";

	/**
	 * Constructs a new {@link Ocelot} {@link AI}.
	 * 
	 * @param api
	 *            The {@link AIController} that this {@link AI} will use to query
	 *            the state of the game.
	 */
	public Ocelot(AIController api) {
		super(NAME, MAX_SPEED, api);
	}

	/**
	 * This {@link Ocelot} will reinforce all its countries that border with hostile
	 * countries based on the size of the enemy armies at those countries.
	 */
	@Override
	public AIOperation processReinforce(AIController api) {

		final AIOperation op = new AIOperation();

		for (Country country : calcBiggestTerritory(api)) {
			if (api.getCurrentPlayer().getDistributableArmy().getStrength() != 0) {

				if (!op.select.isEmpty()) {
					op.select.clear();
				}
				op.select.add(country);
				op.processAgain = true;

			}
		}

		return op;

	}

	/**
	 * This {@link Ocelot} will attack the largest thread at its borders first.
	 */
	@Override
	public AIOperation processAttack(AIController api) {

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

	/**
	 * This {@link Ocelot} will move all its units to the country that it can use to
	 * attack the most neighbouring countries.
	 */
	@Override
	public AIOperation processFortify(AIController api) {

		final Set<Country> internal = new HashSet<>();

		final Map<Country, Integer> frontline = new HashMap<>();

		defineFrontline(api, internal, frontline);

		final Map<Integer, Entry> possibleMoves = getFortifyWeightings(api, internal, frontline);

		final Integer[] weights = possibleMoves.keySet().toArray(new Integer[possibleMoves.keySet().size()]);

		// Sort the weights roles into descending order.
		Arrays.sort(weights, Collections.reverseOrder());

		final AIOperation op = new AIOperation();

		// If there is no weighted pairs.
		if (weights.length == 0) {
			op.processAgain = false;
			return op;
		}

		final Country safe = possibleMoves.get(weights[0]).a;
		final Country border = possibleMoves.get(weights[0]).b;

		if (!op.select.isEmpty()) {
			op.select.clear();
		}

		op.select.add(safe);
		op.select.add(border);
		op.processAgain = true;

		return op;

	}

	// Return a hashset containing all countries directly connected to the parameter
	// country.
	public HashSet<Country> calcTerritory(AIController api, Country country1) {
		HashSet<Country> territory = new HashSet<Country>();
		territory.add(country1);
		api.forEachFriendlyCountry(api.getCurrentPlayer(), country -> {
			if ((api.isPathBetween(country1, country)) && (!territory.contains(country))) {
				territory.add(country);
			}
		});

		return territory;
	}

	// Return a hashset containing the biggest collection of directly connected
	// countries
	public HashSet<Country> calcBiggestTerritory(AIController api) {
		HashSet<Country> biggestTerritory = new HashSet<Country>();
		HashSet<Country> tempTerritory = new HashSet<Country>();

		api.forEachFriendlyCountry(api.getCurrentPlayer(), country -> {
			tempTerritory.addAll(calcTerritory(api, country));
			if (tempTerritory.size() >= biggestTerritory.size()) {
				biggestTerritory.removeAll(biggestTerritory);
				biggestTerritory.addAll(tempTerritory);
				tempTerritory.removeAll(tempTerritory);
			}
		});
		return biggestTerritory;
	}

	/**
	 * Retrieves the weighting for a country that the {@link Ocelot} may attack.
	 * 
	 * @param api
	 *            The {@link AIController} that this {@link AI} will use to query
	 *            the state of the game.
	 * @return weighting
	 */
	private Map<Integer, Entry> getAttackWeightings(AIController api) {

		Map<Integer, Entry> countries = new HashMap<>();

		Player current = api.getCurrentPlayer();

		api.forEachCountry(country -> {

			if (current.equals(country.getOwner()) && country.getArmy().getNumberOfUnits() > 1) {

				for (Country neighbour : country.getNeighbours()) {

					int value = country.getArmy().getNumberOfUnits();

					if (!current.equals(neighbour.getOwner())) {

						value -= neighbour.getArmy().getStrength();

						countries.put(value, new Entry(country, neighbour));
					}

				}
			}
		});

		return countries;
	}

	/**
	 * Retrieves the weighting for every possible fortification possible on the
	 * board..
	 * 
	 * @param api
	 *            {@link AIController} The AIControllor for interaction.
	 * @param internal
	 *            {@link Country}s that border NO enemy {@link Country}s
	 * @param frontline
	 *            {@link Country}s that border enemy {@link Country}s
	 * @return weightings
	 */
	private Map<Integer, Entry> getFortifyWeightings(AIController api, Set<Country> internal,
			Map<Country, Integer> frontline) {

		Map<Integer, Entry> possibleMoves = new HashMap<>();

		frontline.keySet().forEach(f -> internal.forEach(i -> {

			// If there is a path between the countries.
			if (api.isPathBetween(i, f)) {
				possibleMoves.put(frontline.get(f), new Entry(i, f));
			}

		}));

		return possibleMoves;
	}

	/**
	 * Iterates through each {@link Country} on the ModelBoard and adds the
	 * {@link Country}s that border enemy {@link Country}s to 'frontline' and
	 * {@link Country}s that border NO enemy {@link Country}s to 'internal'.
	 * 
	 * @param api
	 *            {@link AIController} The AIControllor for interaction.
	 *            
	 * @param internal
	 * 				Set of countries bordering no enemies.
	 * 
	 * @param frontline
	 * 				Set of countries bordering enemies.
	 * 
	 */
	private void defineFrontline(AIController api, Set<Country> internal, Map<Country, Integer> frontline) {

		Player current = api.getCurrentPlayer();

		/*
		 * Iterate through each country on the board and if it is owned by the current
		 * player check if it has any enemy countries as neighbours. If so, then add the
		 * country to 'frontline' and specify how many enemies it has otherwise add it
		 * to 'internal'.
		 */
		api.forEachCountry(country -> {

			if (current.equals(country.getOwner())) {

				// The default weight of this country
				final int defaultValue = -country.getArmy().getNumberOfUnits();

				// The current value.
				int value = defaultValue;

				// Iterate through all the country's neighbours.
				for (Country neighbour : country.getNeighbours()) {
					if (!current.equals(neighbour.getOwner())) {
						value += neighbour.getArmy().getNumberOfUnits();
					}
				}

				// If the current country is an internal country.
				if (value == defaultValue) {
					if (country.getArmy().getNumberOfUnits() > 1) {
						internal.add(country);
					}
				} else {
					frontline.put(country, value);
				}
			}
		});
	}

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
		 * 		Country a.
		 * @param b
		 * 		Country b.
		 */
		public Entry(Country a, Country b) {
			this.a = a;
			this.b = b;
		}

	}
}