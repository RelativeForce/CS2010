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
				
			int value = rand.nextInt(10);
			countries.put(value, country);
			
			});
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
		if(rand.nextInt()>5 && rand.nextInt()<10) {
			return true;}
		return false;
		
		//go through each of the countries owned by that player
		//see the neighbouring countries of each
		//put them in array with values for the amount of troops
		// find own country and neighbouring non friendly country with biggest gap between the two
		//attack it
	}

	@Override
	protected boolean processFortify(AIController api) {
		if(rand.nextInt()>5 && rand.nextInt()<10) {
			return true;}
		return false;
	}

}
