package peril.model;

import peril.controllers.api.Country;
import peril.controllers.api.Player;

/**
 * A consumable item that allows a {@link Player} to block a path from one
 * {@link Country} to another. Disabling the ability to attack for either
 * {@link Country}s.
 * 
 * @author Ezekiel_Trinidad
 *
 */
public class BlockerConsumable extends ModelConsumable{

	public BlockerConsumable(String name) {
		super(name);
	}

	@Override
	public void use() {
		
	}

	
	
}
