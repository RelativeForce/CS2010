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

public final class Noob extends AI {
	
	/**
	 * The number of milliseconds between each action of this {@link Noob}. If
	 * this is zero or lower then the {@link Noob} will perform its
	 * actions at the frame rate of the display.
	 */
	private static final int SPEED = 100;
	
	/**
	 * The name of this {@link AI}.
	 */
	private static final String NAME = "Noob";

	/**
	 * Constructs a new {@link Ocelot} {@link AI}.
	 * 
	 * @param api
	 *            The {@link AIController} that this {@link AI} will use to query the
	 *            state of the game.
	 */
	public Noob(AIController api) {
		super(NAME, SPEED, api);
	}

	@Override
	protected boolean processReinforce(AIController api) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * This {@link Noob} will attack the largest thread at its borders first.
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

	@Override
	protected boolean processFortify(AIController api) {
		// TODO Auto-generated method stub
		return false;
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
