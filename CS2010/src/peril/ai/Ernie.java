package peril.ai;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import peril.controllers.AIController;
import peril.controllers.api.Country;
import peril.controllers.api.Player;

public class Ernie extends AI {

	private Random rand;

	public Ernie(AIController api) {
		super("Ernie", MAX_SPEED, api);
		// TODO Auto-generated constructor stub
		rand = new Random(10);
	}

	@Override
	protected AIOperation processReinforce(AIController api) {

		// for loop to see what countries are connected to each other and put them in an
		// array
		// give them all a value
		// the country with the lowest value ie the lowest amount of troops
		// choose a random amount of troops to give to them

		Map<Integer, Country> countries = new HashMap<>();
		Player current = api.getCurrentPlayer();

		int highest = Integer.MIN_VALUE;

		api.forEachCountry(country -> {
			if (current.equals(country.getOwner())) {
				int value = rand.nextInt(10);
				countries.put(value, country);
			}
		});

		for (int value : countries.keySet()) {
			highest = value > highest ? value : highest;
		}
		if (highest == Integer.MIN_VALUE) {
			throw new IllegalStateException("There are no countries");
		}

		final AIOperation op = new AIOperation();

		op.select.add(countries.get(highest));
		op.processAgain = true;

		return op;
	}

	@Override
	protected AIOperation processAttack(AIController api) {

		Map<Integer, Entry> countries = new HashMap<>();
		Player current = api.getCurrentPlayer();
		// go through each of the countries owned by that player
		// see the neighbouring countries of each
		// put them in array with values for the amount of troops
		// find own country and neighbouring non friendly country with biggest gap
		// between the two
		// attack it
		int highest = Integer.MIN_VALUE;

		api.forEachCountry(country -> {
			if (current.equals(country.getOwner())) {
				if (country.getArmy().getNumberOfUnits() > 1) {
					for (Country neighbour : country.getNeighbours()) {

						if (!current.equals(neighbour.getOwner())) {
							int value = rand.nextInt(10);
							countries.put(value, new Entry(country, neighbour));
						}
					}
				}
			}
		});

		for (int value : countries.keySet()) {
			highest = value > highest ? value : highest;
		}

		final AIOperation op = new AIOperation();
		
		if (highest == Integer.MIN_VALUE) {
			op.processAgain = false;
		}else {
			op.select.add(countries.get(highest).a);
			op.select.add(countries.get(highest).b);
			op.processAgain = true;
		}

		return op;

	}

	@Override
	protected AIOperation processFortify(AIController api) {
		return new AIOperation();
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
