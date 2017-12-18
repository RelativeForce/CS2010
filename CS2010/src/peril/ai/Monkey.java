package peril.ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import peril.ai.api.Controller;
import peril.ai.api.Country;
import peril.ai.api.Player;

/**
 * A very stupid and predictable {@link AI}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class Monkey extends AI {

	/**
	 * The number of milliseconds between each action of this {@link Monkey}. If
	 * this is zero or lower then the then the {@link Monkey} will perform its
	 * actions at the frame rate of the display.
	 */
	private static final int SPEED = 100;

	/**
	 * Constructs a new {@link Monkey} {@link AI}.
	 * 
	 * @param api
	 *            The {@link Controller} that this {@link AI} will use to query the
	 *            state of the game.
	 */
	public Monkey(Controller api) {
		super(SPEED, api);
	}

	/**
	 * This {@link Monkey} will reinforce all its countries that border with hostile
	 * countries based on the size of the enemy armies at those countries.
	 */
	@Override
	public boolean processReinforce(Controller api) {

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

		// Select the country with the highest weight then reinforce it.
		api.select(countries.get(highest));
		api.reinforce();

		return true;

	}

	/**
	 * This {@link Monkey} will attack the largest thread at its borders first.
	 */
	@Override
	public boolean processAttack(Controller api) {

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

	/**
	 * This {@link Monkey} will move all its units to the country that it can use to
	 * attack the most neighbouring countries.
	 */
	@Override
	public boolean processFortify(Controller api) {

		final Set<Country> internal = new HashSet<>();

		final Map<Country, Integer> frontline = new HashMap<>();

		defineFrontline(api, internal, frontline);

		final Map<Integer, Entry> possibleMoves = getFortifyWeightings(api, internal, frontline);

		final Integer[] weights = possibleMoves.keySet().toArray(new Integer[possibleMoves.keySet().size()]);

		// Sort the weights roles into descending order.
		Arrays.sort(weights, Collections.reverseOrder());

		// If there was no weighted pairs.
		if (weights.length == 0) {
			return false;
		}

		for (int index = 0; index < weights.length; index++) {

			final Country safe = possibleMoves.get(weights[index]).a;
			final Country border = possibleMoves.get(weights[index]).b;

			api.clearSelected();
			
			// If there was a valid link between the safe and border then the secondary will
			// be the border.
			if (api.select(safe)) {
				if (api.select(border)) {
					api.fortify();
					return true;
				}
			}

		}

		// If there was weighted pairs
		return false;

	}

	/**
	 * Retrieves the weighting for every possible fortification possible on the
	 * board..
	 * 
	 * @param internal
	 *            {@link Country}s that border NO enemy {@link Country}s
	 * @param frontline
	 *            {@link Country}s that border enemy {@link Country}s
	 * @return weightings
	 */
	private Map<Integer, Entry> getFortifyWeightings(Controller api, Set<Country> internal,
			Map<Country, Integer> frontline) {

		Map<Integer, Entry> possibleMoves = new HashMap<>();

		frontline.keySet().forEach(bordering -> internal.forEach(safe -> {

			final int weight = frontline.get(bordering) - safe.getArmySize();

			// If there is a path between the countries.
			if (api.isPathBetween(safe, bordering)) {
				possibleMoves.put(weight, new Entry(safe, bordering));
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
	 *            {@link Controller}
	 * 
	 */
	private void defineFrontline(Controller api, Set<Country> internal, Map<Country, Integer> frontline) {

		Player current = api.getCurrentPlayer();

		/*
		 * Iterate through each country on the board and if it is owned by the current
		 * player check if it has any enemy countries as neighbours. If so, then add the
		 * country to 'frontline' and specify how many enemies it has otherwise add it
		 * to 'internal'.
		 */
		api.forEachCountry(country -> {

			if (current.equals(country.getOwner())) {

				int enemies = 0;

				// Iterate through all the country's neighbours.
				for (Country neighbour : country.getNeighbours()) {
					if (!current.equals(neighbour.getOwner())) {
						enemies += neighbour.getArmySize();
					}
				}

				// Add the country to the appropriate collection.
				if (enemies == 0) {
					if (country.getArmySize() > 1) {
						internal.add(country);
					}
				} else {
					frontline.put(country, enemies);
				}
			}
		});
	}

	/**
	 * Retrieves the weighting for a country that the {@link Monkey} may reinforce.
	 * 
	 * @param api
	 *            The {@link Controller} that this {@link AI} will use to query the
	 *            state of the game.
	 * @return weighting
	 */
	private Map<Integer, Country> getReinforceWeightings(Controller api) {

		Map<Integer, Country> countries = new HashMap<>();
		Player current = api.getCurrentPlayer();

		// Get the weightings of each country on the board.
		api.forEachCountry(country -> {

			if (current.equals(country.getOwner())) {

				int value = -country.getArmySize();

				for (Country c : country.getNeighbours()) {
					if (!current.equals(c.getOwner())) {
						value += c.getArmySize();
					}
				}

				countries.put(value, country);
			}
		});

		return countries;
	}

	/**
	 * Retrieves the weighting for a country that the {@link Monkey} may attack.
	 * 
	 * @param api
	 *            The {@link Controller} that this {@link AI} will use to query the
	 *            state of the game.
	 * @return weighting
	 */
	private Map<Integer, Entry> getAttackWeightings(Controller api) {

		Map<Integer, Entry> countries = new HashMap<>();

		Player current = api.getCurrentPlayer();

		api.forEachCountry(country -> {

			if (current.equals(country.getOwner()) && country.getArmySize() > 1) {

				for (Country neighbour : country.getNeighbours()) {

					int value = -country.getArmySize();

					if (!current.equals(neighbour.getOwner())) {

						value += neighbour.getArmySize();

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