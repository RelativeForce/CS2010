package peril.ai;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import peril.ai.api.Board;
import peril.ai.api.Country;
import peril.ai.api.Player;

/**
 * A very stupid and predictable {@link AI}.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.04
 * @since 2018-03-16
 * 
 * @see AI
 */
public final class Monkey extends AI {

	/**
	 * The name of this {@link Monkey}.
	 */
	private static final String NAME = "Monkey";

	/**
	 * Constructs a new {@link Monkey} {@link AI}.
	 * 
	 * @param api
	 *            The {@link AIController} that this {@link AI} will use to query
	 *            the state of the game.
	 */
	public Monkey(AIController api) {
		super(NAME, MAX_SPEED, api);
	}

	/**
	 * This {@link Monkey} will reinforce all its countries that border with hostile
	 * countries based on the size of the enemy armies at those countries.
	 */
	@Override
	public AIOperation processReinforce(AIController api) {

		Map<Integer, Country> countries = getReinforceWeightings(api);

		int highest = Integer.MIN_VALUE;

		// Find the highest weight
		for (int value : countries.keySet()) {
			highest = value > highest ? value : highest;
		}

		// If there is no weighting the there must be no friendly countries meaning this
		// AI has been invoked at an invalid time.
		if (highest == Integer.MIN_VALUE) {
			throw new IllegalStateException("There are no countries");
		}

		final AIOperation op = new AIOperation();

		// Select the country with the highest weight then reinforce it.
		// api.select(countries.get(highest));
		// api.reinforce();

		op.select.add(countries.get(highest));
		op.processAgain = true;

		return op;

	}

	/**
	 * This {@link Monkey} will attack the largest thread at its borders first.
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
	 * This {@link Monkey} will move all its units to the country that it can use to
	 * attack the most neighbouring countries.
	 */
	@Override
	public AIOperation processFortify(AIController api) {

		final Set<Country> internal = new HashSet<>();

		final Map<Country, Integer> frontline = new HashMap<>();

		defineFrontline(api, internal, frontline);

		final Map<Integer, Entry> possibleMoves = getFortifyWeightings(api, internal, frontline);

		int highest = Integer.MIN_VALUE;

		for (int value : possibleMoves.keySet()) {
			highest = value > highest ? value : highest;
		}

		final AIOperation op = new AIOperation();

		if (highest == Integer.MIN_VALUE) {

			op.processAgain = false;

		} else {

			op.select.add(possibleMoves.get(highest).a);
			op.select.add(possibleMoves.get(highest).b);
			op.processAgain = true;
		}

		return op;

	}

	/**
	 * Retrieves the weighting for every possible fortification possible on the
	 * board..
	 * 
	 * @param api
	 *            The {@link AIController}.
	 * @param internal
	 *            {@link Country}s that border NO enemy {@link Country}s
	 * @param frontline
	 *            {@link Country}s that border enemy {@link Country}s
	 * @return weightings The {@link Map} of weighting to the entry.
	 */
	private Map<Integer, Entry> getFortifyWeightings(AIController api, Set<Country> internal,
			Map<Country, Integer> frontline) {

		final Map<Integer, Entry> possibleMoves = new HashMap<>();

		frontline.keySet().forEach(f -> internal.forEach(i -> {

			// If there is a path between the countries.
			if (api.isPathBetween(i, f)) {
				possibleMoves.put(frontline.get(f), new Entry(i, f));
			}

		}));

		return possibleMoves;
	}

	/**
	 * Iterates through each {@link Country} on the {@link Board} and adds the
	 * {@link Country}s that border enemy {@link Country}s to 'frontline' and
	 * {@link Country}s that border NO enemy {@link Country}s to 'internal'.
	 * 
	 * @param api
	 *            {@link AIController}
	 * @param internal
	 *            The {@link Country}s that have no enemy {@link Country}s
	 *            neighbouring them.
	 * @param frontline
	 *            The {@link Country}s that have enemy {@link Country}s neighbouring
	 *            them. The value is the number of enemies the {@link Country} has.
	 * 
	 */
	private void defineFrontline(AIController api, Set<Country> internal, Map<Country, Integer> frontline) {

		final Player current = api.getCurrentPlayer();

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
	 * Retrieves the weighting for a country that the {@link Monkey} may reinforce.
	 * 
	 * @param api
	 *            The {@link AIController} that this {@link AI} will use to query
	 *            the state of the game.
	 * @return weighting
	 */
	private Map<Integer, Country> getReinforceWeightings(AIController api) {

		Map<Integer, Country> countries = new HashMap<>();
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

	/**
	 * Retrieves the weighting for a country that the {@link Monkey} may attack.
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

	/**
	 * Holds a pair of {@link Country}s.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @version 1.01.3
	 * @since 2018-03-16
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
