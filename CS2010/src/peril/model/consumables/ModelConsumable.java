package peril.model.consumables;

import java.util.Observable;

import peril.model.ModelPlayer;

/**
 * This class Models a Consumable item that {@link ModelPlayer}s can use.
 * 
 * @author Ezekiel_Trinidad
 *
 */
public abstract class ModelConsumable extends Observable
{

	/**
	 * The name of the consumable.
	 */
	private String name;
	
	public ModelConsumable(String name) {
		this.name = name;
	}
	
	public abstract void use();
	
}
