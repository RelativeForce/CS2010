package peril.ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//import peril.ai.TheDarkKnight.Entry;
import peril.controllers.AIController;
import peril.controllers.api.Country;
import peril.controllers.api.Player;

/**
 * A very basic {@link AI} which will hopefully win the battle, we can only pray
 * 
 * @author Gurdeep_Pol
 * 
 * @since 2018-02-23
 * 
 * @version 1.01.04
 * 
 * @see AI
 *
 */
public final class Knight extends AI {

	/**
	 * The name of this {@link Knight}.
	 */
	private static final String NAME = "Knight";

	/**
	 * Constructs a new {@link Knight} {@link AI}.
	 * 
	 * @param api
	 *            The {@link AIController} that this {@link AI} will use to query
	 *            the state of the game.
	 */
	public Knight(AIController api) {
		super(NAME, AI.MAX_SPEED, api);
	}

	@Override
	protected AIOperation processFortify(AIController api) {

		final Set<Country> internal = new HashSet<>();

		final Map<Country, Integer> frontline = new HashMap<>();

		defineFrontline(api, internal, frontline);

		final Map<Integer, Entry> possibleMoves = getFortifyWeightings(api, internal, frontline);

		final Integer[] weights = possibleMoves.keySet().toArray(new Integer[possibleMoves.keySet().size()]);

		// weights ordered in descending order.
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

	@Override
	protected AIOperation processAttack(AIController api) {
		// TODO Auto-generated method stub
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
	protected AIOperation processReinforce(AIController api) {
		// TODO Auto-generated method stub
		Map<Integer, Country> countries = getReinforceWeightings(api);

		int highest = Integer.MIN_VALUE;

		for (int value : countries.keySet()) {
			highest = value > highest ? value : highest;
		}

		if (highest == Integer.MIN_VALUE) {
			throw new IllegalStateException("No friendly countries");
		}

		final AIOperation op = new AIOperation();

		op.select.add(countries.get(highest));
		op.processAgain = true;

		return op;
	}

	private void defineFrontline(AIController api, Set<Country> internal, Map<Country, Integer> frontline) {

		Player current = api.getCurrentPlayer();

		/*
		 * Go through each country, if it is owned by you, check if the neighbours are
		 * enemies. If they are then the country becomes the front line.
		 */
		api.forEachCountry(country -> {

			if (current.equals(country.getOwner())) {

				// default weight of this country
				final int defaultValue = -country.getArmyStrength();

				// The current value.
				int value = defaultValue;

				// Iterate through current country's neighbours.
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

	private Map<Integer, Entry> getFortifyWeightings(AIController api, Set<Country> internal,
			Map<Country, Integer> frontline) {

		Map<Integer, Entry> possibleMoves = new HashMap<>();

		frontline.keySet().forEach(f -> internal.forEach(i -> {

			// Check if there is a path between the countries.
			if (api.isPathBetween(i, f)) {
				possibleMoves.put(frontline.get(f), new Entry(i, f));
			}

		}));

		return possibleMoves;
	}

	private Map<Integer, Entry> getAttackWeightings(AIController api) {

		Map<Integer, Entry> countries = new HashMap<>();

		Player current = api.getCurrentPlayer();

		api.forEachCountry(country -> {

			if (current.equals(country.getOwner()) && country.getArmy().getNumberOfUnits() > 1) {

				for (Country neighbour : country.getNeighbours()) {

					int value = country.getArmyStrength();

					if (!current.equals(neighbour.getOwner()) && api.hasOpenLinkBetween(country, neighbour)) {

						value -= neighbour.getArmyStrength();

						countries.put(value, new Entry(country, neighbour));
					}

				}
			}
		});

		return countries;
	}

	private Map<Integer, Country> getReinforceWeightings(AIController api) {

		Map<Integer, Country> countries = new HashMap<>();
		Player current = api.getCurrentPlayer();

		// Get the weightings of each country on the board.
		api.forEachCountry(country -> {

			// Check if the country is friendly.
			if (current.equals(country.getOwner())) {

				int value = -country.getArmyStrength();

				// Search through all the neighbour countries.
				for (Country neighbour : country.getNeighbours()) {

					// Check if the neighbour is an enemy country.
					if (!current.equals(neighbour.getOwner())) {
						value += neighbour.getArmyStrength();
					}
				}

				// If the current country has enemy countries.
				if (value != -country.getArmyStrength()) {
					countries.put(value, country);
				}

			}
		});

		return countries;
	}

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
