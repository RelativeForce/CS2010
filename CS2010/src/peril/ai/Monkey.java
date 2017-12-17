package peril.ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import peril.ai.api.AIController;
import peril.ai.api.Country;
import peril.ai.api.Player;

public class Monkey extends AI {

	public Monkey(AIController api) {
		super(100, api);
	}

	@Override
	public boolean processReinforce(AIController api) {

		Map<Integer, Country> countries = new HashMap<>();

		Player current = api.getCurrentPlayer();

		api.forEachCountry(country -> {
			if (current.equals(country.getOwner())) {
				countries.put(getReinforceWeight(country, current), country);
			}
		});

		int highest = Integer.MIN_VALUE;

		for (int value : countries.keySet()) {
			highest = value > highest ? value : highest;
		}

		if (highest == Integer.MIN_VALUE) {
			throw new IllegalStateException("There are no countries");
		}

		api.select(countries.get(highest));
		api.reinforce();

		return true;

	}

	@Override
	public boolean processAttack(AIController api) {

		class Entry {

			public final Country friendly;
			public final Country enemy;

			public Entry(Country friendly, Country enemy) {
				this.friendly = friendly;
				this.enemy = enemy;
			}

		}

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

		int highest = Integer.MIN_VALUE;

		for (int value : countries.keySet()) {
			highest = value > highest ? value : highest;
		}

		if (highest == Integer.MIN_VALUE) {
			return false;
		}

		api.select(countries.get(highest).friendly);
		api.select(countries.get(highest).enemy);

		api.attack();

		return true;

	}

	@Override
	public boolean processFortify(AIController api) {

		class Entry {

			public final Country safe;
			public final Country border;

			public Entry(Country safe, Country border) {
				this.safe = safe;
				this.border = border;
			}

		}

		Player current = api.getCurrentPlayer();

		Set<Country> internal = new HashSet<>();

		Map<Country, Integer> frontline = new HashMap<>();

		api.forEachCountry(country -> {

			if (current.equals(country.getOwner())) {

				int enemies = 0;

				for (Country neighbour : country.getNeighbours()) {

					if (!current.equals(neighbour.getOwner())) {
						enemies += neighbour.getArmySize();
					}
				}

				if (enemies == 0) {
					if (country.getArmySize() > 1) {
						internal.add(country);
					}
				} else {
					frontline.put(country, enemies);
				}
			}
		});

		Map<Integer, Entry> possibleMoves = new HashMap<>();

		frontline.keySet().forEach(bordering -> internal.forEach(safe -> {

			int weight = frontline.get(bordering) - safe.getArmySize();

			possibleMoves.put(weight, new Entry(safe, bordering));

		}));

		Integer[] weights = possibleMoves.keySet().toArray(new Integer[possibleMoves.keySet().size()]);

		// Sort the weights roles into descending order.
		Arrays.sort(weights, Collections.reverseOrder());

		if (weights.length == 0) {
			return false;
		}

		for (int index = 0; index < weights.length; index++) {

			final Country safe = possibleMoves.get(weights[index]).safe;
			final Country border = possibleMoves.get(weights[index]).border;

			// If there was a valid link between the safe and border then the secondary will
			// be the border.
			if (api.isPathBetween(safe, border)) {
				api.select(safe);
				api.select(border);
				api.fortify();
				return true;
			}

		}

		// If there was weighted pairs
		if (weights.length > 0) {
			return false;
		}

		return true;

	}

	private int getReinforceWeight(Country country, Player current) {

		int value = -country.getArmySize();

		for (Country c : country.getNeighbours()) {
			if (!current.equals(c.getOwner())) {
				value += c.getArmySize();
			}
		}

		if (value == -country.getArmySize()) {
			return Integer.MIN_VALUE;
		}

		return value;
	}
}
