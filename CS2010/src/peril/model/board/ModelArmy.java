package peril.model.board;

import java.util.HashMap;
import java.util.Iterator;
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
public final class ModelArmy extends Observable implements Iterable<ModelUnit> {

	/**
	 * The size of the army.
	 */
	private int strength;

	/**
	 * Contains the {@link ModelUnit}s that this {@link ModelArmy} consists of.
	 */
	private final Map<ModelUnit, Integer> units;

	/**
	 * Holds the currently selected {@link ModelUnit}.
	 */
	private ModelUnit selected;

	/**
	 * Constructs a new {@link ModelArmy} with strength of 1.
	 */
	public ModelArmy() {
		this(1);
	}

	/**
	 * Constructs a new {@link ModelArmy} of a specified strength.
	 * 
	 * @param strength
	 *            The size of the {@link ModelArmy}. Must be greater than zero.
	 */
	public ModelArmy(int strength) {
		units = new HashMap<>();
		selected = null;
		setStrength(strength);
		computeUnits();
	}

	/**
	 * Assigns a new strength to this {@link ModelArmy} overwriting the old value.
	 * 
	 * @param strength
	 *            The new size of the {@link ModelArmy}. Must be greater than zero.
	 */
	public void setStrength(int strength) {
		if (strength < 0) {
			throw new IllegalArgumentException("Size must be greater than zero");
		}
		this.strength = strength;
		computeUnits();

		setChanged();
		notifyObservers(new Update("size", strength));

	}

	/**
	 * Retrieves the size of the {@link ModelArmy}.
	 * 
	 * @return
	 */
	public int getStrength() {
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

		computeUnits();

		setChanged();
		notifyObservers(new Update("size", strength));
	}

	/**
	 * Retrieves the currently selected {@link ModelUnit}.
	 * 
	 * @return {@link ModelUnit}
	 */
	public ModelUnit getSelected() {
		return selected;
	}

	/**
	 * Selects a specified unit from this {@link ModelArmy}.
	 * 
	 * @param unit
	 *            {@link ModelUnit}
	 * @return Returns whether the {@link ModelUnit} was selected or not.
	 */
	public boolean select(ModelUnit unit) {

		// If the unit is in this army then it canbe selected.
		if (units.containsKey(unit)) {

			setSelected(unit);
			return true;

		}

		setSelected(null);
		return false;
	}

	/**
	 * De-selected the currently selected unit.
	 */
	public void deselect() {
		setSelected(null);
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
		return units.get(unit) != null && units.get(unit) != 0;
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
	 * Returns the number of different types of {@link ModelUnit}s in this
	 * {@link ModelArmy}.
	 * 
	 * @return
	 */
	public int getUnitType() {

		int numberOfUnitTypes = 0;

		for (int unitAmount : units.values()) {
			if (unitAmount > 0)
				numberOfUnitTypes++;
		}

		return numberOfUnitTypes;
	}

	/**
	 * Retreieves the {@link Iterator} for this {@link ModelArmy} of all the
	 * {@link ModelUnit}s inside.
	 */
	@Override
	public Iterator<ModelUnit> iterator() {
		return units.keySet().iterator();
	}

	/**
	 * Sets the selected {@link ModelUnit}.
	 * 
	 * @param unit
	 *            {@link ModelUnit}
	 */
	private void setSelected(ModelUnit unit) {

		selected = unit;

		setChanged();
		notifyObservers(new Update("selected", selected));
	}

	/**
	 * Determines the units that this {@link ModelArmy} can contain.
	 */
	private void computeUnits() {

		// De-select the currently selected unit.
		deselect();

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
