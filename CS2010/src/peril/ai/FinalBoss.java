package peril.ai;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
 */
public class FinalBoss extends AI {

	/**
	 * The name of this {@link AI}.
	 */
	private static final String NAME = "Final Boss";

	/**
	 * Denotes how much larger a enemy army must be in order for the
	 * {@link FinalBoss} to block the link between those two armies.
	 */
	private static final double DEFENSE_FACTOR = 1.25;

	/**
	 * The weighting that the size of an army has over a decision.
	 */
	private static final double UNIT_FACTOR = 2.5;

	/**
	 * THe weighting that the standing of a player has over a decision.
	 */
	private static final double PLAYER_FACTOR = 1.0;

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
	public boolean processReinforce(AIController api) {

		api.clearSelected();
		final Country country = ReinforceHandler.getCountry(api);

		// Select the country with the highest weight then reinforce it.
		if (api.select(country)) {
			api.reinforce();
		}

		return true;

	}

	/**
	 * This {@link FinalBoss} will attack countries.
	 */
	@Override
	public boolean processAttack(AIController api) {

		api.clearSelected();
		final Entry attack = AttackHandler.getEntry(api);

		if (attack == null) {
			return false;
		}

		if (api.select(attack.a)) {
			if (api.select(attack.b)) {
				api.attack();
				return true;
			}
		}

		return false;

	}

	/**
	 * This {@link FinalBoss} will move all its units to the country that it can use
	 * to attack the most neighbouring countries.
	 */
	@Override
	public boolean processFortify(AIController api) {

		api.clearSelected();
		Entry fortify = FortifyHandler.getEntry(api);

		api.clearSelected();

		if (fortify == null) {
			return false;
		}

		// If there was a valid link between the safe and border then the secondary will
		// be the border.
		if (api.select(fortify.a)) {
			if (api.select(fortify.b)) {
				api.fortify();
				return true;
			}
		}

		return false;

	}

	private static int getMaxOneAttackDamage(Army army) {

		Set<? extends Unit> units = army.getUnits();

		int maxDamage = 0;

		int remainingUnits = 3;

		boolean flag = units.isEmpty();

		while (!flag && remainingUnits > 0) {

			final Unit unit = army.getStrongestUnit();
			final int numberOfUnit = army.getNumberOf(unit);

			if (numberOfUnit > 0) {

				final int toAdd = numberOfUnit >= remainingUnits ? remainingUnits : numberOfUnit;

				maxDamage += (toAdd * unit.getStrength());
				remainingUnits -= toAdd;

			}

			units.remove(unit);

			if (units.isEmpty()) {
				flag = true;
			}
		}

		return maxDamage;
	}

	private static int playerRating(Player player, AIController api) {

		int rating = 0;

		rating = (player.getCountriesRuled() * api.getBoard().getNumberOfCountries()) / 100;

		int totalArmyStrength = 0;

		for (Player p : api.getPlayers()) {
			totalArmyStrength += p.getTotalArmy().getStrength();
		}

		rating += (player.getTotalArmy().getStrength() * totalArmyStrength) / 100;

		// Divide by 2 so its out of 100.
		rating /= 2;

		return rating;
	}

	/**
	 * This handles all the reinforce operations for the {@link FinalBoss}.
	 * 
	 * @author Joshua_Eddy
	 */
	private static class ReinforceHandler {

		/**
		 * Retrieves the {@link Country} that the {@link FinalBoss} should reinforce.
		 * 
		 * @param api
		 *            The {@link AIController} that this {@link AI} will use to query
		 *            the state of the game.
		 * @return {@link Country} that the {@link FinalBoss} should reinforce.
		 */
		public static Country getCountry(AIController api) {

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

			return countries.get(highest);
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
		private static Map<Integer, Country> getReinforceWeightings(AIController api) {

			final Map<Integer, Country> countries = new HashMap<>();
			final Player current = api.getCurrentPlayer();

			// Get the weightings of each country on the board.
			api.forEachCountry(country -> {

				// If the country is friendly.
				if (current.equals(country.getOwner())) {

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

				}
			});

			return countries;
		}

	}

	/**
	 * This handles all the attack operations for the {@link FinalBoss}.
	 * 
	 * @author Joshua_Eddy
	 */
	private static class AttackHandler {

		public static Entry getEntry(AIController api) {

			Map<Integer, Entry> countries = getAttackWeightings(api);

			int highest = Integer.MIN_VALUE;

			for (int value : countries.keySet()) {
				highest = value > highest ? value : highest;
			}

			if (highest == Integer.MIN_VALUE) {
				return null;
			}

			return countries.get(highest);

		}

		/**
		 * Retrieves the weighting for a country that the {@link FinalBoss} may attack.
		 * 
		 * @param api
		 *            The {@link AIController} that this {@link AI} will use to query
		 *            the state of the game.
		 * @return weighting
		 */
		private static Map<Integer, Entry> getAttackWeightings(AIController api) {

			final Map<Integer, Entry> countries = new HashMap<>();

			final Player current = api.getCurrentPlayer();

			api.forEachFriendlyCountry(current, country -> {

				if (country.getArmy().getNumberOfUnits() > 1) {

					final int countryStrength = country.getArmy().getStrength();

					api.forEachEnemyNeighbour(country, neighbour -> {

						// The strength of the current neighbours army.
						final int neighbourStrength = neighbour.getArmy().getStrength();

						// Whether the current country's army is overwhelmed by the enemy army
						final boolean isOverWhelmed = DEFENSE_FACTOR * countryStrength <= neighbourStrength;

						// Whether the current country is vulnerable to attack from the neighbour
						final boolean isVulnerable = api.hasOpenLinkBetween(neighbour, country);

						// If the player can afford to blockade the link
						final boolean sufficentPoints = current.getPoints() >= api.getPoints().getBlockade();

						// If the country is vulnerable to attack and can block the link, block the
						// link.
						if (isOverWhelmed && isVulnerable && sufficentPoints) {
							api.createBlockade(country, neighbour);
						}

						// If the country can attack.
						if (api.hasOpenLinkBetween(country, neighbour)) {

							final Player owner = neighbour.getOwner();
							
							int rating = (int) (UNIT_FACTOR * getMaxOneAttackDamage(country.getArmy()));
							rating -= (int)(UNIT_FACTOR * getMaxOneAttackDamage(neighbour.getArmy()));

							// Add Player Rating if the country is ruled.
							if (owner != null) {
								rating -= (PLAYER_FACTOR * playerRating(owner, api));
							}
							
							countries.put(rating, new Entry(country, neighbour));
						}

					});
				}
			});

			return countries;
		}
	}

	/**
	 * This handles all the fortify operations for the {@link FinalBoss}.
	 * 
	 * @author Joshua_Eddy
	 */
	private static class FortifyHandler {

		public static Entry getEntry(AIController api) {

			final Set<Country> internal = new HashSet<>();

			final Map<Country, Integer> frontline = new HashMap<>();

			defineFrontline(api, internal, frontline);

			final Map<Integer, Entry> possibleMoves = getFortifyWeightings(api, internal, frontline);

			int highest = Integer.MIN_VALUE;

			for (int value : possibleMoves.keySet()) {
				highest = value > highest ? value : highest;
			}

			if (highest == Integer.MIN_VALUE) {
				return null;
			}

			return possibleMoves.get(highest);

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
		private static Map<Integer, Entry> getFortifyWeightings(AIController api, Set<Country> internal,
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
		private static void defineFrontline(AIController api, Set<Country> internal, Map<Country, Integer> frontline) {

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

	}

	/**
	 * Holds a pair of {@link Country}s.
	 * 
	 * @author Joshua_Eddy
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
