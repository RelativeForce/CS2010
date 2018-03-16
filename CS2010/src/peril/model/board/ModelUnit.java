package peril.model.board;

import peril.controllers.api.Unit;

/**
 * A unit that is part of a {@link ModelArmy}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-14
 * @version 1.01.01
 * 
 * @see Comparable
 * @see Unit
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
	 *            The name of the {@link ModelUnit}.
	 * @param strength
	 *            The strength of this {@link ModelUnit}.
	 * @param fileName
	 *            The file name of the image that denotes this {@link ModelUnit}s
	 *            visual representation.
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

	/**
	 * Checks if a {@link Object} is identical to <code>this</code>.
	 */
	@Override
	public boolean equals(Object other) {

		// If the object is a unit
		if (other instanceof ModelUnit) {

			// Cast to model unit
			final ModelUnit otherUnit = (ModelUnit) other;

			// Check all the fields are the same.
			if (strength == otherUnit.strength && name.equals(otherUnit.name) && fileName.equals(otherUnit.fileName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Create a new object that is identical to <code>this</code>.
	 */
	@Override
	protected ModelUnit clone() {
		return new ModelUnit(name, strength, fileName);
	}

	/**
	 * Retrieves the strength of this {@link Unit}.
	 */
	@Override
	public int getStrength() {
		return strength;
	}

}
