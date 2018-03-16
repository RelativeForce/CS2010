package peril.ai;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

import peril.ai.api.Country;
import peril.ai.api.Player;

/**
 * A {@link AI} that acts in a completely random manner. This {@link AI} will
 * not block links or upgrade units. Named after my dog.
 * 
 * @author Hannah_Miller
 *
 * @version 1.02.01
 * @since 2018-03-12
 */
public final class Ernie extends AI {

	/**
	 * The display name of {@link Ernie}.
	 */
	private static final String NAME = "Easy";

	/**
	 * The {@link Random} that is used to determine the actions of {@link Ernie}.
	 */
	private final Random rand;

	/**
	 * Constructs a new {@link Ernie}.
	 * 
	 * @param api
	 *            The {@link AIController} that allows {@link Ernie} to query the
	 *            state of the game.
	 */
	public Ernie(AIController api) {
		super(NAME, MAX_SPEED, api);

		this.rand = new Random(10);
	}

	/**
	 * Randomly reinforces friendly countries until it runs out of units.
	 */
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

	/**
	 * Randomly attacks until there are no more valid attacks left.
	 */
	@Override
	protected AIOperation processAttack(AIController api) {

		final Map<Integer, Entry> countries = new HashMap<>();
		final Player current = api.getCurrentPlayer();

		int highest = Integer.MIN_VALUE;

		api.forEachFriendlyCountry(current, country -> {

			if (country.getArmy().getNumberOfUnits() > 1) {

				country.getNeighbours().stream()

						// Filter out all the invalid targets.
						.filter(neighbour -> !current.equals(neighbour.getOwner())
								&& api.hasOpenLinkBetween(country, neighbour))

						// Iterate over each valid target.
						.forEach(neighbour -> {

							// Assign a random value to this attack.
							final int value = rand.nextInt(10);
							countries.put(value, new Entry(country, neighbour));

						});
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

	/**
	 * Randomly move units from safe {@link Country}s to {@link Country}s that
	 * border enemy {@link Country}s.
	 */
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

		if (result.isPresent()) {

			final Entry best = countries.get(result.get());

			op.processAgain = true;
			op.select.add(best.a);
			op.select.add(best.b);

		}

		return op;
	}

	/**
	 * Borrowed this from Josh.
	 * 
	 * @author Hannah_Miller
	 *
	 */
	private final class Entry {

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
