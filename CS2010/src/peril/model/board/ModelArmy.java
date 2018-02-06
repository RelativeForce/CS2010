package peril.model.board;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
	 * Contains the {@link ModelUnit}s that this {@link ModelArmy} consists of.
	 */
	private final Map<ModelUnit, Integer> units;

	/**
	 * Holds the currently selected {@link ModelUnit}.
	 */
	private ModelUnit selected;

	/**
	 * Constructs a new {@link ModelArmy} of a specified strength.
	 * 
	 * @param strength
	 *            The size of the {@link ModelArmy}. Must be greater than zero.
	 */
	public ModelArmy() {
		// Add one of the weakest units to this army
		this(0);

	}

	public ModelArmy(int strength) {

		units = new HashMap<>();
		selected = null;

		populateArmy(strength);
	}

	/**
	 * Retrieves the size of the {@link ModelArmy}.
	 * 
	 * @return
	 */
	public int getStrength() {

		int strength = 0;

		for (ModelUnit unit : units.keySet()) {
			strength += units.get(unit) * unit.strength;
		}

		return strength;
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
	public void remove(ModelUnit unit) {

		if (units.containsKey(unit)) {

			if (units.get(unit) == 1) {
				units.remove(unit);
			} else {
				units.replace(unit, units.get(unit) - 1);
			}
		} else {
			remove(unit.strength);
		}

		setChanged();
		notifyObservers();

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
	public int getNumberOf(ModelUnit unit) {
		return units.get(unit);
	}

	public boolean tradeUp(ModelUnit unit) {
		if (!hasUnit(unit)) {
			return false;
		}

		final ModelUnit above = UnitHelper.getInstance().getUnitAbove(unit);

		if (above == null) {
			return false;
		}

		final int numberOfUnit = units.get(unit);

		final int strengthCombined = numberOfUnit * unit.strength;

		final boolean strongerThanAbove = strengthCombined > above.strength;

		if (!strongerThanAbove) {
			return false;
		}

		final int numberOfAbove = strengthCombined / above.strength;

		final int remainder = strengthCombined % above.strength;

		for (int index = 0; index < numberOfUnit; index++) {
			remove(unit);
		}
		
		for(int index = 0; index < numberOfAbove; index++) {
			add(above);
		}
		
		generateUnits(remainder).forEach(smaller -> add(smaller));

		return true;

	}

	/**
	 * Returns the number of different types of {@link ModelUnit}s in this
	 * {@link ModelArmy}.
	 * 
	 * @return
	 */
	public int getVarietyOfUnits() {
		return units.size();
	}

	/**
	 * Retrieves the {@link Iterator} for this {@link ModelArmy} of all the
	 * {@link ModelUnit}s inside.
	 */
	@Override
	public Iterator<ModelUnit> iterator() {
		return units.keySet().iterator();
	}

	public void add(ModelUnit unit) {

		int currentNumber = units.get(unit) == null ? 0 : units.get(unit);

		units.put(unit, currentNumber + 1);

		setChanged();
		notifyObservers();

	}

	public void add(List<ModelUnit> unitList) {
		unitList.forEach(unit -> add(unit));
	}

	public void setStrength(int strength) {

		if (strength < 0) {
			throw new IllegalArgumentException("New strength can not be less than zero.");
		}

		clearUnits();

		populateArmy(strength);

		setChanged();
		notifyObservers();

	}

	public void setWeakest() {
		clearUnits();

		units.put(UnitHelper.getInstance().getWeakest(), 1);

		setChanged();
		notifyObservers();

	}

	public ModelUnit getWeakestUnit() {

		ModelUnit current = UnitHelper.getInstance().getWeakest();

		while (current != null) {
			if (hasUnit(current)) {
				return current;
			}

			current = UnitHelper.getInstance().getUnitAbove(current);
		}

		throw new IllegalStateException("There are no units in this army.");
	}

	public ModelUnit getStrongestUnit() {

		ModelUnit current = UnitHelper.getInstance().getStrongest();

		while (current != null) {
			if (hasUnit(current)) {
				return current;
			}

			current = UnitHelper.getInstance().getUnitBelow(current);
		}

		throw new IllegalStateException("There are no units in this army.");
	}

	public void remove(int strength) {

		final int newStrength = getStrength() - strength;

		if (newStrength >= UnitHelper.getInstance().getWeakest().strength) {
			populateArmy(newStrength);
		} else {
			clearUnits();
		}

		setChanged();
		notifyObservers();

	}

	private void clearUnits() {
		// Clear the current list of units.
		if (!units.isEmpty())
			units.clear();

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
	private void populateArmy(int strength) {

		// De-select the currently selected unit.
		deselect();

		// Clear the current list of units.
		clearUnits();

		// Generate the units then add them to the current army.
		add(generateUnits(strength));

	}

	public static List<ModelUnit> generateUnits(int strength) {

		List<ModelUnit> units = new LinkedList<>();

		// Holds the current strongest unit that is smaller than the army size.
		ModelUnit unit = UnitHelper.getInstance().getStrongest();

		int armySize = strength;

		// While the army can be further divided.
		while (armySize > 0) {

			// If the unit is null then there is no unit weaker than the previous.
			if (unit == null) {
				throw new IllegalStateException("There is not unit teir that the army can be divide further into.");
			}

			// If the current unit fits into the army add one of that unit to the
			// army that can be fit.
			if (unit.strength <= armySize) {
				units.add(unit);
				armySize = armySize - unit.strength;
			} else {
				// Move to the unit below.
				unit = UnitHelper.getInstance().getUnitBelow(unit);
			}

		}

		return units;

	}

	public int getNumberOfUnits() {

		int number = 0;

		for (Integer amount : units.values()) {
			number += amount;
		}

		return number;
	}

}
