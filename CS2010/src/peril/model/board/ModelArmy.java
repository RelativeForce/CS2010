package peril.model.board;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import peril.Update;
import peril.helpers.UnitHelper;

/**
 * Encapsulates the behaviours of a collection of units. This will be aggregated
 * by the {@link WarMenu} and composes the {@link ModelCountry}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class ModelArmy extends Observable {

	/**
	 * The size of the army.
	 */
	private int strength;

	/**
	 * Contains the {@link ModelUnit}s that this {@link ModelArmy} consists of.
	 */
	private final Map<ModelUnit, Integer> units;

	/**
	 * Constructs a new {@link ModelArmy} with size of 1 and an offset of (0,0).
	 */
	public ModelArmy() {
		this(1);
	}

	/**
	 * Constructs a new {@link ModelArmy} of a specified size with an offset of
	 * (0,0).
	 * 
	 * @param size
	 *            The size of the {@link ModelArmy}. Must be greater than zero.
	 */
	public ModelArmy(int size) {
		units = new HashMap<>();
		setSize(size);
	}

	/**
	 * Assigns a new size to this {@link ModelArmy} overwriting the old value.
	 * 
	 * @param size
	 *            The new size of the {@link ModelArmy}. Must be greater than zero.
	 */
	public void setSize(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Size must be greater than zero");
		}
		this.strength = size;
		computeUnits();

		setChanged();
		notifyObservers(new Update("size", size));

	}

	/**
	 * Retrieves the size of the {@link ModelArmy}.
	 * 
	 * @return
	 */
	public int getSize() {
		return strength;
	}

	/**
	 * Adds a specified number of units to this {@link ModelArmy}.
	 * 
	 * @param amount
	 *            of units to add to this {@link ModelArmy}
	 */
	public void add(int amount) {
		strength += amount;

		setChanged();
		notifyObservers(new Update("size", strength));
	}

	/**
	 * Removes a specified amount of units from this {@link ModelArmy}.
	 * 
	 * @param amount
	 *            of units to remove.
	 */
	public void remove(int amount) {
		if (strength - amount < 0) {
			throw new IllegalStateException("Army size cannot be less than zero");
		}

		// Change to the army
		strength -= amount;

		computeUnits();

		setChanged();
		notifyObservers(new Update("size", strength));
	}

	/**
	 * Retrieves a whether a specific type of {@link ModelUnit} is currently in this
	 * {@link ModelArmy}.
	 * 
	 * @param unit
	 *            {@link ModelArmy}
	 * @return
	 */
	public boolean hasUnit(ModelUnit unit) {
		return units.get(unit) != 0;
	}

	/**
	 * Retrieves a number of a specific type of {@link ModelUnit} that are currently
	 * in this {@link ModelArmy}.
	 * 
	 * @param unit
	 *            {@link ModelUnit}.
	 * @return
	 */
	public int getUnit(ModelUnit unit) {
		return units.get(unit);
	}

	/**
	 * Determines the units that this {@link ModelArmy} can contain.
	 */
	private void computeUnits() {

		// Clear the current list of units.
		if (!units.isEmpty())
			units.clear();

		int armySize = strength;

		// Holds the current strongest unit that is smaller than the army size.
		ModelUnit unit = UnitHelper.getInstance().getStrongest();

		// While the army can be further divided.
		while (armySize > 0) {

			// If the unit is null then there is no unit weaker than the previous.
			if (unit == null) {
				throw new IllegalStateException("There is not unit teir that the army can be divide further into.");
			}

			// If the current unit fits into the army add the max number of that unit to the
			// army that can be fit.
			if (unit.strength <= armySize) {
				units.put(unit, armySize / unit.strength);
				armySize = armySize % unit.strength;
			}

			// Move to the unit below.
			unit = UnitHelper.getInstance().getUnitBelow(unit);

		}

	}
}
