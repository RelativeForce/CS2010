package peril.model.board;

/**
 * A unit that is part of a {@link ModelArmy}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class ModelUnit implements Comparable<ModelUnit> {

	/**
	 * The strength of this {@link ModelUnit}.
	 */
	public final int strength;

	/**
	 * The name of this {@link ModelUnit}.
	 */
	public final String name;

	/**
	 * Constructs an new {@link ModelUnit}.
	 * 
	 * @param name
	 * @param strength
	 */
	public ModelUnit(String name, int strength) {
		this.strength = strength;
		this.name = name;
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

}
