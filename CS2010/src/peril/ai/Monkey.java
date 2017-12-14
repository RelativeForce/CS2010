package peril.ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import peril.Game;
import peril.board.Country;

public class Monkey extends AI {

	public Monkey() {
		super(500);
	}

	@Override
	public boolean processReinforce(Game game) {

		Map<Integer, Country> countries = new HashMap<>();

		Player current = game.players.getCurrent();

		game.board.forEachCountry(country -> {
			if (current.equals(country.getRuler())) {
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

		game.states.reinforcement.select(countries.get(highest));
		game.states.reinforcement.reinfoce();

		return true;

	}

	@Override
	public boolean processAttack(Game game) {

		class Entry {

			public final Country friendly;
			public final Country enemy;

			public Entry(Country friendly, Country enemy) {
				this.friendly = friendly;
				this.enemy = enemy;
			}

		}

		Map<Integer, Entry> countries = new HashMap<>();

		Player current = game.players.getCurrent();

		game.board.forEachCountry(country -> {

			if (current.equals(country.getRuler()) && country.getArmy().getSize() > 1) {

				for (Country neighbour : country.getNeighbours()) {

					int value = -country.getArmy().getSize();

					if (!current.equals(neighbour.getRuler())) {

						value += neighbour.getArmy().getSize();

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
			if (game.states.combat.getSelected() == null) {
				game.menus.warMenu.hide();
			}
			return false;
		}

		game.states.combat.select(countries.get(highest).friendly);
		game.states.combat.select(countries.get(highest).enemy);

		if (!game.menus.warMenu.isVisible()) {
			game.menus.warMenu.show();
		}

		game.menus.warMenu.attack();

		if (game.states.combat.getSelected() == null) {
			game.menus.warMenu.hide();
			game.states.movement.removeSelected();
		}

		return true;

	}

	@Override
	public boolean processFortify(Game game) {

		class Entry {

			public final Country safe;
			public final Country border;

			public Entry(Country safe, Country border) {
				this.safe = safe;
				this.border = border;
			}

		}

		Player current = game.players.getCurrent();

		Set<Country> internal = new HashSet<>();

		Map<Country, Integer> frontline = new HashMap<>();

		game.board.forEachCountry(country -> {

			if (current.equals(country.getRuler())) {

				int enemies = 0;

				for (Country neighbour : country.getNeighbours()) {

					if (!current.equals(neighbour.getRuler())) {
						enemies += neighbour.getArmy().getSize();
					}
				}

				if (enemies == 0) {
					if (country.getArmy().getSize() > 1) {
						internal.add(country);
					}
				} else {
					frontline.put(country, enemies);
				}
			}
		});

		Map<Integer, Entry> possibleMoves = new HashMap<>();

		frontline.keySet().forEach(bordering -> internal.forEach(safe -> {

			int weight = frontline.get(bordering) - safe.getArmy().getSize();

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
			if (game.states.movement.isPathBetween(safe, border)) {
				game.states.movement.select(safe);
				game.states.movement.select(border);
				game.states.movement.fortify();
				game.states.movement.removeSelected();
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

		int value = -country.getArmy().getSize();

		for (Country c : country.getNeighbours()) {
			if (!current.equals(c.getRuler())) {
				value += c.getArmy().getSize();
			}
		}

		if (value == -country.getArmy().getSize()) {
			return Integer.MIN_VALUE;
		}

		return value;
	}
}
