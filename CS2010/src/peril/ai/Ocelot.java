package peril.ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import peril.controllers.AIController;
import peril.controllers.api.Country;
import peril.controllers.api.Player;

/**
 * A hopefully somewhat clever AI. We'll see. {@link AI}.
 * 
 * @author Joseph Rolli
 *
 */
public final class Ocelot extends AI {

	/**
	 * The number of milliseconds between each action of this {@link Ocelot}. If
	 * this is zero or lower then the {@link Ocelot} will perform its
	 * actions at the frame rate of the display.
	 */
	private static final int SPEED = 100;

	/**
	 * The name of this {@link Ocelot}.
	 */
	private static final String NAME = "Ocelot";

	/**
	 * Constructs a new {@link Ocelot} {@link AI}.
	 * 
	 * @param api
	 *            The {@link AIController} that this {@link AI} will use to query the
	 *            state of the game.
	 */
	public Ocelot(AIController api) {
		super(NAME, SPEED, api);
	}

	/**
	 * This {@link Ocelot} will reinforce all its countries that border with hostile
	 * countries based on the size of the enemy armies at those countries.
	 */
	@Override
	public boolean processReinforce(AIController api) {
		while(api.getCurrentPlayer().getDistributableArmySize()!=0) {
			api.forEachCountry(country -> {
				if(api.getCurrentPlayer().equals(country.getOwner())) {
					api.select(country);
					api.reinforce();
				}
			});
		}

		return true;

	}

	/**
	 * This {@link Ocelot} will attack the largest thread at its borders first.
	 */
	@Override
	public boolean processAttack(AIController api) {

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
	 * This {@link Ocelot} will move all its units to the country that it can use to
	 * attack the most neighbouring countries.
	 */
	@Override
	public boolean processFortify(AIController api) {

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
	 * Iterates through each {@link Country} on the {@link ModelBoard} and adds the
	 * {@link Country}s that border enemy {@link Country}s to 'frontline' and
	 * {@link Country}s that border NO enemy {@link Country}s to 'internal'.
	 * 
	 * @param api
	 *            {@link AIController}
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
				final int defaultValue = -country.getArmyStrength();

				// The current value.
				int value = defaultValue;

				// Iterate through all the country's neighbours.
				for (Country neighbour : country.getNeighbours()) {
					if (!current.equals(neighbour.getOwner())) {
						value += neighbour.getArmyStrength();
					}
				}

				// If the current country is an internal country.
				if (value == defaultValue) {
					if (country.getArmyStrength() > 1) {
						internal.add(country);
					}
				} else {
					frontline.put(country, value);
				}
			}
		});
	}


	/**
	 * Retrieves the weighting for a country that the {@link Ocelot} may attack.
	 * 
	 * @param api
	 *            The {@link AIController} that this {@link AI} will use to query the
	 *            state of the game.
	 * @return weighting
	 */
	private Map<Integer, Entry> getAttackWeightings(AIController api) {

		Map<Integer, Entry> countries = new HashMap<>();

		Player current = api.getCurrentPlayer();

		api.forEachCountry(country -> {

			if (current.equals(country.getOwner()) && country.getArmyStrength() > 1) {

				for (Country neighbour : country.getNeighbours()) {

					int value = country.getArmyStrength();

					if (!current.equals(neighbour.getOwner())) {

						value -= neighbour.getArmyStrength();

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
