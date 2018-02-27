package peril.ai;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

import peril.controllers.AIController;
import peril.controllers.api.Country;
import peril.controllers.api.Player;

public final class Ernie extends AI {

	private Random rand;
	
	private static final String NAME = "Easy";

	public Ernie(AIController api) {
		super(NAME, MAX_SPEED, api);
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
		} else {
			op.select.add(countries.get(highest).a);
			op.select.add(countries.get(highest).b);
			op.processAgain = true;
		}

		return op;

	}

	@Override
	protected AIOperation processFortify(AIController api) {

		final Map<Integer, Entry> countries = new HashMap<>();
		final Player current = api.getCurrentPlayer();

		// A country is internal if it is ruled by the current player, has no enemy
		// neighbours and has more than one unit.
		final Predicate<Country> isInternal = c -> current.equals(c.getOwner())
				&& c.getNeighbours().stream().filter(n -> !current.equals(n.getOwner())).count() == 0
				&& c.getArmy().getNumberOfUnits() > 1;

		// A country is a front line country if it has enemy neighbours.
		final Predicate<Country> isFrontline = c -> c.getNeighbours().stream()
				.filter(n -> !current.equals(n.getOwner())).count() > 0;

		// Iterate over all the internal countries
		api.getBoard().getCountries().stream().filter(isInternal).forEach(i -> {

			// Is true if there is a path between the current internal and the country.
			final Predicate<Country> isConnected = c -> api.isPathBetween(i, c);

			// Iterate over all the front line countries that are also connected to i.
			api.getBoard().getCountries().stream().filter(isFrontline).filter(isConnected).forEach(f -> {

				countries.put(rand.nextInt(10), new Entry(i, f));

			});

		});
		
		// The value of the best entry.
		final Optional<Integer> result = countries.keySet().stream().max(Integer::compareTo);

		final AIOperation op = new AIOperation();
		
		if(result.isPresent()) {
			
			final Entry best = countries.get(result.get());
			
			op.processAgain = true;
			op.select.add(best.a);
			op.select.add(best.b);
			
		}

		return op;
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
