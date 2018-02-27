package peril.ai;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import peril.controllers.AIController;
import peril.controllers.api.Army;
import peril.controllers.api.Country;
import peril.controllers.api.Player;
import peril.controllers.api.Unit;

/**
 * An {@link AI} built with the sole intention of out performing all other
 * {@link AI}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-27
 * @version 1.01.02
 * 
 * @see AI
 * @see AIController
 *
 */
public final class FinalBoss extends AI {

	/**
	 * The name of this {@link AI}.
	 */
	private static final String NAME = "Hard";

	/**
	 * Denotes how much larger a enemy army must be in order for the
	 * {@link FinalBoss} to block the link between those two armies.
	 */
	private static final double DEFENSE_FACTOR = 1.1;

	/**
	 * The weighting that the size of an army has over a decision.
	 */
	private static final double UNIT_FACTOR = 2.5;

	/**
	 * The weighting that the standing of a player has over a decision.
	 */
	private static final double PLAYER_FACTOR = 1.0;

	/**
	 * The weighting that defines how long the {@link FinalBoss} will wait before it
	 * trades a unit for the stronger one.
	 */
	private static final double TRADE_FACTOR = 0.7;

	/**
	 * Constructs a new {@link FinalBoss}.
	 * 
	 * @param api
	 *            The {@link AIController} that allows this {@link FinalBoss} to
	 *            interact with the game.
	 */
	public FinalBoss(AIController api) {
		super(NAME, AI.MAX_SPEED, api);
	}

	/**
	 * This {@link FinalBoss} will reinforce all its countries.
	 */
	@Override
	public AIOperation processReinforce(AIController api) {

		// The country that this AI will reinforce.
		final Country country = new ReinforceHandler().getCountry(api);

		// Define the operation that will be returned
		final AIOperation op = new AIOperation();

		op.select.add(country);
		op.processAgain = true;

		return op;

	}

	/**
	 * This {@link FinalBoss} will attack countries.
	 */
	@Override
	public AIOperation processAttack(AIController api) {

		// The country pair that denotes the attack.
		final Entry attack = new AttackHandler().getAttack(api);
		final AIOperation op = new AIOperation();

		// If the attack handler cannot find a valid attack don't process again.
		if (attack == null) {
			op.processAgain = false;
		} else {
			op.select.add(attack.a);
			op.select.add(attack.b);
			op.processAgain = true;
		}
		return op;

	}

	/**
	 * This {@link FinalBoss} will move all its units to the country that it can use
	 * to attack the most neighbouring countries.
	 */
	@Override
	public AIOperation processFortify(AIController api) {

		// The country pair that denotes the fortify.
		final Entry fortify = new FortifyHandler().getFortify(api);
		final AIOperation op = new AIOperation();

		// If the fortify handler cannot find a valid fortify don't process again.
		if (fortify == null) {
			op.processAgain = false;
		} else {
			op.select.add(fortify.a);
			op.select.add(fortify.b);
			op.processAgain = true;
		}

		return op;

	}

	/**
	 * Calculates the maximum amount of damage that the specified {@link Army} could
	 * inflict in one attack.
	 * 
	 * @param army
	 *            The {@link Army}
	 * @return The maximum amount of damage that the specified {@link Army} could
	 *         inflict in one attack.
	 */
	private static int getMaxOneAttackDamage(Army army) {

		// The units in the army
		final Set<? extends Unit> units = army.getUnits();

		// The max damage
		int maxDamage = 0;

		// The number of units that can be in a squad.
		int remainingUnits = 3;

		// Whether the army has units
		boolean flag = units.isEmpty();

		// Iterate until there a no more units in the army set or the squad is full.
		while (!flag && remainingUnits > 0) {

			// The unit with the highest strength.
			final Unit unit = units.stream()
					.max((unit1, unit2) -> Integer.compare(unit1.getStrength(), unit2.getStrength())).get();
			final int numberOfUnit = army.getNumberOf(unit);

			// If there is more than one of that unit in the army.
			if (numberOfUnit > 0) {

				final int toAdd = numberOfUnit >= remainingUnits ? remainingUnits : numberOfUnit;

				maxDamage += (toAdd * unit.getStrength());
				remainingUnits -= toAdd;

			}

			units.remove(unit);
			flag = units.isEmpty();
		}

		return maxDamage;
	}

	/**
	 * Retrieves a rating for the specified {@link Player} that ranges from 0 to 100
	 * where 100 is the strongest possible {@link Player}.
	 * 
	 * @param player
	 *            The {@link Player} to rate.
	 * @param api
	 *            The {@link AIController} that allows the {@link FinalBoss} to
	 *            query the state of the game.
	 * @return The rating of the {@link Player}.
	 */
	private static int playerRating(Player player, AIController api) {

		// The rating for the specified player.
		int rating = 0;

		// Add the percentage of all the countries that this player owns.
		rating = (player.getCountriesRuled() * api.getBoard().getNumberOfCountries()) / 100;

		// Total strength of all the units in the game combined.
		int totalArmyStrength = api.getPlayers().stream().mapToInt(p -> p.getTotalArmy().getStrength()).sum();

		// Add the percentage of the player army strength from the all the units in the
		// game.
		rating += (player.getTotalArmy().getStrength() * totalArmyStrength) / 100;

		// Divide by 2 so its out of 100.
		rating /= 2;

		return rating;
	}

	/**
	 * Retrieves the best {@link Unit} from a {@link Country}'s {@link Army}.
	 * 
	 * @param api
	 *            The {@link AIController} that allows the {@link FinalBoss} to
	 *            query the state of the game.
	 * @param country
	 *            The {@link Country} that will be evaluated.
	 * @return The best {@link Unit} for this {@link Country} to trade up. This can
	 *         be null if there are no possible units to trade.
	 */
	private static Unit getBestUnitToTrade(AIController api, Country country) {

		final Army army = country.getArmy();

		Unit bestUnit = null;
		int valueToBeat = 0;

		// Iterate over each unit in the army.
		for (Unit unit : army.getUnits()) {

			final Unit above = api.getUnitAbove(unit);

			if (above != null) {

				// The strength of the unit above the current one.
				final int aboveStrength = above.getStrength();

				// The strength of the current unit.
				final int unitStrength = unit.getStrength();

				// The number of the current unit required to make one of the unit above.
				final int ratio = aboveStrength / unitStrength;

				// The number of the current unit in the
				final int numberOfUnit = army.getNumberOf(unit);

				// Whether there are enough of current unit to make at least one of the unit
				// above.
				final boolean hasEnough = numberOfUnit >= ratio;

				if (hasEnough) {

					// The number of the unit above that will be created.
					final int numberTraded = numberOfUnit / ratio;

					// If there is a best unit already.
					if (bestUnit == null) {

						bestUnit = unit;
						valueToBeat = numberTraded;

					} else {

						// Check that the current unit is better than the specified best unit.
						final boolean isStronger = bestUnit.getStrength() < unit.getStrength();
						final boolean isBetter = valueToBeat < numberTraded
								|| (valueToBeat == numberTraded && isStronger);

						// If the unit is better or the first;
						if (isBetter) {
							bestUnit = unit;
							valueToBeat = numberTraded;
						}

					}

				}
			}

		}

		return bestUnit;
	}

	/**
	 * This handles all the reinforce operations for the {@link FinalBoss}.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @since 2018-02-27
	 * @version 1.01.01
	 * 
	 * @see FinalBoss
	 */
	private static class ReinforceHandler {

		/**
		 * Constructs a new {@link ReinforceHandler}.
		 */
		public ReinforceHandler() {

		}

		/**
		 * Retrieves the {@link Country} that the {@link FinalBoss} should reinforce.
		 * 
		 * @param api
		 *            The {@link AIController} that this {@link AI} will use to query
		 *            the state of the game.
		 * @return The {@link Country} that the {@link FinalBoss} should reinforce.
		 */
		public Country getCountry(AIController api) {

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

			final Country country = countries.get(highest);

			final int points = api.getCurrentPlayer().getPoints();
			final int cost = api.getPoints().getUnitTrade();

			if (points >= cost) {

				final Unit unit = getBestUnitToTrade(api, country);

				if (unit != null) {

					final double countriesSize = countries.size();

					final boolean shouldTrade = points / (cost * countriesSize) >= TRADE_FACTOR;

					if (shouldTrade) {
						api.tradeUnit(country, unit);
					}

				}
			}

			return country;
		}

		/**
		 * Retrieves the weighting for a country that the {@link FinalBoss} may
		 * reinforce.
		 * 
		 * @param api
		 *            The {@link AIController} that this {@link AI} will use to query
		 *            the state of the game.
		 * @return weighting
		 */
		private Map<Integer, Country> getReinforceWeightings(AIController api) {

			final Map<Integer, Country> countries = new HashMap<>();
			final Player current = api.getCurrentPlayer();

			// Get the weightings of each country on the board.
			api.forEachFriendlyCountry(current, country -> {

				// The base value is the maximum damage for one combat attack.
				final int baseRating = (int) -(UNIT_FACTOR * getMaxOneAttackDamage(country.getArmy()));

				int rating = baseRating;

				// Iterate through all the neighbour countries.
				for (Country neighbour : country.getNeighbours()) {

					final Player owner = neighbour.getOwner();

					// If the neighbour is an enemy country.
					if (!current.equals(owner)) {
						rating += (UNIT_FACTOR * getMaxOneAttackDamage(neighbour.getArmy()));

						// Add Player Rating if the country is ruled.
						if (owner != null) {
							rating += (PLAYER_FACTOR * playerRating(owner, api));
						}
					}
				}

				// If the current country has enemy countries.
				if (rating != baseRating) {
					countries.put(rating, country);
				}

			});

			return countries;
		}

	}

	/**
	 * This handles all the attack operations for the {@link FinalBoss}.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @since 2018-02-27
	 * @version 1.01.01
	 * 
	 * @see FinalBoss
	 */
	private static class AttackHandler {

		/**
		 * Constructs an new {@link AttackHandler}.
		 */
		public AttackHandler() {
		}

		/**
		 * Retrieves the best attack {@link Entry} that can be performed with the
		 * current state of the game.
		 * 
		 * @param api
		 *            The {@link AIController} that allows the {@link FinalBoss} to
		 *            query the state of the game.
		 * @return The {@link Entry} attack that should performed.
		 */
		public Entry getAttack(AIController api) {

			// Retrieves all the possible attacks.
			final Map<Integer, Entry> attacks = getAttacks(api);

			// The value of the best entry.
			final Optional<Integer> result = attacks.keySet().stream().max(Integer::compareTo);

			// Only return entry if a result value was found.
			return result.isPresent() ? attacks.get(result.get()) : null;

		}

		/**
		 * Retrieves the weighting for a country that the {@link FinalBoss} may attack.
		 * 
		 * @param api
		 *            The {@link AIController} that this {@link FinalBoss} will use to
		 *            query the state of the game.
		 * @return All the possible attacks and their ratings.
		 */
		private Map<Integer, Entry> getAttacks(AIController api) {

			// All the possible attacks
			final Map<Integer, Entry> attacks = new HashMap<>();

			// The current player
			final Player current = api.getCurrentPlayer();

			// For each enemy neighbour of every friendly country.
			api.getBoard().getCountries().stream()

					// Filter out all the enemy countries and countries that have a 1 unit army.
					.filter(country -> current.equals(country.getOwner()) && country.getArmy().getNumberOfUnits() > 1)

					// Iterate over all the valid countries.
					.forEach(country -> country.getNeighbours().stream()

							// Filter out all the friendly neighbours
							.filter(neighbour -> !current.equals(neighbour.getOwner()))

							// Iterate over each enemy neighbour.
							.forEach(neighbour -> {

								// If the link should be blocked.
								if (shouldBlockLink(country, neighbour, api)) {
									api.createBlockade(country, neighbour);
								}

								// If the country can attack.
								if (api.hasOpenLinkBetween(country, neighbour)) {
									attacks.put(getRating(api, country, neighbour), new Entry(country, neighbour));
								}

							}));

			return attacks;
		}

		/**
		 * Retrieves whether or not the link from an enemy neighbour should be blocked
		 * according to {@link FinalBoss}.
		 * 
		 * @param country
		 *            A friendly {@link Country}.
		 * @param neighbour
		 *            A enemy neighbour {@link Country}.
		 * @param api
		 *            The {@link AIController} that this {@link FinalBoss} will use to
		 *            query the state of the game.
		 * @return Whether or not the link from an enemy neighbour should be blocked
		 *         according to {@link FinalBoss}.
		 */
		private boolean shouldBlockLink(Country country, Country neighbour, AIController api) {

			// The current player
			final Player current = api.getCurrentPlayer();

			// The strength of the current country.
			final int countryStrength = country.getArmy().getStrength();

			// The strength of the current neighbours army.
			final int neighbourStrength = neighbour.getArmy().getStrength();

			// Whether the current country's army is overwhelmed by the enemy army
			final boolean isOverWhelmed = DEFENSE_FACTOR * countryStrength <= neighbourStrength;

			// Whether the current country is vulnerable to attack from the neighbour
			final boolean isVulnerable = api.hasOpenLinkBetween(neighbour, country);

			// If the player can afford to blockade the link
			final boolean sufficentPoints = current.getPoints() >= api.getPoints().getBlockade();

			return isOverWhelmed && isVulnerable && sufficentPoints;

		}

		/**
		 * Calculates the rating of an attack between a specified friendly
		 * {@link Country} and a specified enemy neighbour {@link Country}.
		 * 
		 * @param country
		 *            A friendly {@link Country}.
		 * @param neighbour
		 *            A enemy neighbour {@link Country}.
		 * @param api
		 *            The {@link AIController} that this {@link FinalBoss} will use to
		 *            query the state of the game.
		 * @return The attack rating.
		 */
		private int getRating(AIController api, Country country, Country neighbour) {

			// The owner of the attacking country
			final Player owner = neighbour.getOwner();

			// The rating of the attack.
			int rating = 0;

			rating += (int) (UNIT_FACTOR * getMaxOneAttackDamage(country.getArmy()));
			rating -= (int) (UNIT_FACTOR * getMaxOneAttackDamage(neighbour.getArmy()));

			// Add Player Rating if the country is ruled.
			if (owner != null) {
				rating -= (PLAYER_FACTOR * playerRating(owner, api));
			}

			return rating;
		}
	}

	/**
	 * This handles all the fortify operations for the {@link FinalBoss}.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @since 2018-02-27
	 * @version 1.01.01
	 * 
	 * @see FinalBoss
	 */
	private static class FortifyHandler {

		/**
		 * Constructs a new {@link FortifyHandler}.
		 */
		public FortifyHandler() {
		}

		/**
		 * Receives the best possible {@link Entry} fortify for {@link FinalBoss} to
		 * perform.
		 * 
		 * @param api
		 *            The {@link AIController} that this {@link FinalBoss} will use to
		 *            query the state of the game.
		 * @return The best possible {@link Entry} fortify.
		 */
		public Entry getFortify(AIController api) {

			final Set<Country> internal = new HashSet<>();
			final Map<Country, Integer> frontline = new HashMap<>();

			// Define which countries are internal and which are on the front lines.
			defineFrontline(api, internal, frontline);

			// All the possible fortifications based on the current state of the game.
			final Map<Integer, Entry> possibleMoves = getFortifies(api, internal, frontline);

			// The value of the best entry.
			final Optional<Integer> result = possibleMoves.keySet().stream().max(Integer::compareTo);

			// Only return entry if a result value was found.
			return result.isPresent() ? possibleMoves.get(result.get()) : null;

		}

		/**
		 * Retrieves all the possible fortifications based on the current state of the
		 * game.
		 * 
		 * @param api
		 *            The {@link AIController} that this {@link FinalBoss} will use to
		 *            query the state of the game.
		 * @param internal
		 *            The {@link Country}s that border NO enemy {@link Country}s
		 * @param frontline
		 *            The {@link Country}s that border enemy {@link Country}s
		 * @return All the possible fortifications based on the current state of the
		 *         game.
		 */
		private Map<Integer, Entry> getFortifies(AIController api, Set<Country> internal,
				Map<Country, Integer> frontline) {

			final Map<Integer, Entry> possibleFortifies = new HashMap<>();

			// Iterate over each internal country.
			internal.forEach(internalCountry -> frontline.keySet().stream()

					// Filter out all the front line countries the internal cannot fortify.
					.filter(frontlineCountry -> api.isPathBetween(internalCountry, frontlineCountry))

					// Iterate over each possible fortification.
					.forEach(frontlineCountry -> {

						final Entry fortify = new Entry(internalCountry, frontlineCountry);

						possibleFortifies.put(frontline.get(frontlineCountry), fortify);

					}));

			return possibleFortifies;
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

			final Player current = api.getCurrentPlayer();

			api.forEachFriendlyCountry(current, country -> {

				// The combined strength of all the enemy countries armies.
				final int borderingArmyStrength = country.getNeighbours().stream()

						// Filter out all the friendly countries.
						.filter(neighbour -> !current.equals(neighbour.getOwner()))

						// Sum all the army strengths.
						.mapToInt(neighbour -> neighbour.getArmy().getStrength()).sum();

				// If the current country is an internal country.
				if (borderingArmyStrength == 0) {
					if (country.getArmy().getNumberOfUnits() > 1) {
						internal.add(country);
					}
				} else {

					// The rating for a specific front line country.
					final int rating = borderingArmyStrength - country.getArmy().getStrength();

					frontline.put(country, rating);
				}

			});
		}

	}

	/**
	 * Holds a pair of {@link Country}s.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @since 2018-02-27
	 * @version 1.01.01
	 * 
	 * @see FinalBoss
	 */
	private static class Entry {

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
