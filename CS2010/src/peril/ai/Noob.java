package peril.ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import peril.ai.api.Country;
import peril.ai.api.Player;

/**
 * An {@link AI} that is basically a copy of monkey.
 * 
 * @author Adrian_Wong
 *
 * @since 2018-02-20
 * @version 1.01.02
 */
public final class Noob extends AI {

	/**
	 * The name of this {@link AI}.
	 */
	private static final String NAME = "Noob";

	/**
	 * Constructs a new {@link Noob} {@link AI}.
	 * 
	 * @param api
	 *            The {@link AIController} that this {@link AI} will use to query
	 *            the state of the game.
	 */
	public Noob(AIController api) {
		super(NAME, MAX_SPEED, api);
	}

	@Override
	protected AIOperation processReinforce(AIController api) {

		HashMap<Integer, Country> countries = getReinforceWeightings(api);

		int highest = Integer.MIN_VALUE;
		int lowest = Integer.MAX_VALUE + 1;

		// Find the highest weight
		for (int value : countries.keySet()) {
			highest = value > highest ? value : highest;
		}

		// Find the lowest weight
		for (int value : countries.keySet()) {
			lowest = value > lowest ? value : lowest;
		}

		final AIOperation op = new AIOperation();

		op.select.add(countries.get(lowest));
		op.processAgain = true;

		return op;

	}

	/**
	 * This {@link Noob} will attack the largest thread at its borders first.
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

			if (current.equals(country.getOwner()) && country.getArmy().getStrength() > 1) {

				for (Country neighbour : country.getNeighbours()) {

					int value = country.getArmy().getStrength();

					if (!current.equals(neighbour.getOwner())) {

						value -= neighbour.getArmy().getStrength();

						countries.put(value, new Entry(country, neighbour));
					}

				}
			}
		});

		return countries;
	}

	private static HashMap<Integer, Country> getReinforceWeightings(AIController api) {

		HashMap<Integer, Country> countries = new HashMap<>();
		Player current = api.getCurrentPlayer();

		// Get the weightings of each country on the board.
		api.forEachCountry(country -> {

			// If the country is friendly.
			if (current.equals(country.getOwner())) {

				int value = -country.getArmy().getStrength();

				// Iterate through all the neighbour countries.
				for (Country neighbour : country.getNeighbours()) {

					// If the neighbour is an enemy country.
					if (!current.equals(neighbour.getOwner())) {
						value += neighbour.getArmy().getStrength();
					}
				}

				// If the current country has enemy countries.
				if (value != -country.getArmy().getStrength()) {
					countries.put(value, country);
				}

			}
		});

		return countries;
	}

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
				final int defaultValue = -country.getArmy().getStrength();

				// The current value.
				int value = defaultValue;

				// Iterate through all the country's neighbours.
				for (Country neighbour : country.getNeighbours()) {
					if (!current.equals(neighbour.getOwner())) {
						value += neighbour.getArmy().getStrength();
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
	 * 
	 */
	public class Entry {

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
