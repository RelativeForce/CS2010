package peril.ai;

import java.security.KeyStore.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


import peril.controllers.AIController;
import peril.controllers.api.Country;
import peril.controllers.api.Player;

public class Ernie extends AI {

	private Random rand;

	public Ernie(AIController api) {
		super("Ernie", 100, api);
		// TODO Auto-generated constructor stub
		rand = new Random(10);
	}

	@Override
	protected boolean processReinforce(AIController api) {
		
		//for loop to see what countries are connected to each other and put them in an array
		// give them all a value
		//the country with the lowest value ie the lowest amount of troops
		// choose a random amount of troops to give to them
	
		
			Map<Integer, Country> countries = new HashMap<>();
			Player current = api.getCurrentPlayer();
			
			int highest = Integer.MIN_VALUE;
			
			api.forEachCountry(country -> {
			if (current.equals(country.getOwner())) {
				int value = rand.nextInt(10);
				countries.put(value, country);
			}});
			
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
	protected boolean processAttack(AIController api) {
		// TODO Auto-generated method stub
		
		Map<Integer, Entry> countries = new HashMap<>();	
		Player current = api.getCurrentPlayer();
		//go through each of the countries owned by that player
		//see the neighbouring countries of each
		//put them in array with values for the amount of troops
		// find own country and neighbouring non friendly country with biggest gap between the two
		//attack it
		int highest = Integer.MIN_VALUE;
		
		api.forEachCountry(country -> {
			if (current.equals(country.getOwner())) {
				if(country.getArmy().getNumberOfUnits() > 1)) {
				for (Country neighbour : country.getNeighbours()) {
					
					if (!current.equals(neighbour.getOwner())) {
						int value = rand.nextInt(10);
						countries.put(value, new Entry(country, neighbour));
					}}
				}}});	
	
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
		if(rand.nextInt()>5 && rand.nextInt()<10) {
			return true;}
		return false;
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
