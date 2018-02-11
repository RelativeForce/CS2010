package peril.model.board;

import peril.controllers.api.Unit;

/**
 * A unit that is part of a {@link ModelArmy}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class ModelUnit implements Comparable<ModelUnit>, Unit {

	/**
	 * The strength of this {@link ModelUnit}.
	 */
	public final int strength;

	/**
	 * The name of this {@link ModelUnit}.
	 */
	public final String name;
	
	/**
	 * The file name of the image that represents this object on screen.
	 */
	public final String fileName;

	/**
	 * Constructs an new {@link ModelUnit}.
	 * 
	 * @param name
	 * @param strength
	 */
	public ModelUnit(String name, int strength, String fileName) {
		this.strength = strength;
		this.name = name;
		this.fileName = fileName;
	}

	/**
	 * Compares one {@link ModelUnit} to another. <br>
	 * 
	 * Strength above: 1<br>
	 * Strength below: -1<br>
	 * Strength the same: 0
	 */
	@Override
	public int compareTo(ModelUnit unit) {

		if (unit.strength > this.strength) {
			return 1;
		} else if (unit.strength < this.strength) {
			return -1;
		}

		return 0;
	}

	@Override
	public int getStrength() {
		return strength;
	}

}
