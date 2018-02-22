package peril.model.board;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import peril.Update;
import peril.controllers.api.Army;
import peril.controllers.api.Unit;
import peril.helpers.UnitHelper;

/**
 * Encapsulates the behaviour of a collection of {@link ModelUnit}s. This army
 * is {@link Iterable} and will iterate through all the {@link ModelUnit} types
 * it contains, to get the number of each type of {@link ModelUnit} use
 * {@link ModelArmy#getNumberOf(ModelUnit)}.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.05
 * @since 2018-02-22
 * 
 * @see Observable
 * @see Iterable
 * @see Army
 * @see UnitHelper
 *
 */
public final class ModelArmy extends Observable implements Iterable<ModelUnit>, Army {

	/**
	 * Contains the {@link ModelUnit}s that this {@link ModelArmy} consists of. The
	 * value of each key is the number of that {@link ModelUnit} in the
	 * {@link ModelArmy}.
	 */
	private final Map<String, Integer> units;

	/**
	 * Holds the currently selected {@link ModelUnit}.
	 */
	private ModelUnit selected;

	/**
	 * Constructs a new empty {@link ModelArmy}.
	 */
	public ModelArmy() {
		this(0);
	}

	/**
	 * Constructs an new {@link ModelArmy} with a specified strength.
	 * 
	 * @param strength
	 *            The strength of the {@link ModelArmy}.
	 */
	public ModelArmy(int strength) {

		units = new HashMap<>();
		selected = null;

		populateArmy(strength);
	}

	/**
	 * Retrieves the combined strength of all the {@link ModelUnit}s in this
	 * {@link ModelArmy}.
	 * 
	 * @return Strength of the {@link ModelArmy}.
	 */
	public int getStrength() {

		int strength = 0;

		for (String unitName : units.keySet()) {

			ModelUnit unit = UnitHelper.getInstance().get(unitName);

			strength += units.get(unitName) * unit.strength;
		}

		return strength;
	}

	/**
	 * Retrieves the currently selected {@link ModelUnit}.
	 * 
	 * @return {@link ModelUnit} or null if there is no {@link ModelUnit} selected.
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

		// If the unit is in this army then it can be selected.
		if (units.containsKey(unit.name)) {

			setSelected(unit);
			return true;

		}

		// If the unit is not in the army then de-select the current one.
		setSelected(null);
		return false;
	}

	/**
	 * De-selected the currently selected {@link ModelUnit}.
	 */
	public void deselect() {
		setSelected(null);
	}

	/**
	 * Removes a specified {@link ModelUnit} from this {@link ModelArmy}.
	 * 
	 * @param unit
	 *            The {@link ModelUnit} that is to be removed. NOT NULL
	 */
	public void remove(ModelUnit unit) {

		final String unitName = unit.name;

		/*
		 * If the unit is in the army then remove it, otherwise remove the units
		 * strength.
		 */
		if (units.containsKey(unitName)) {

			/*
			 * If there is only one of the specified unit type then remove the entry in the
			 * units map.
			 */
			if (units.get(unitName) == 1) {
				units.remove(unitName);
			} else {
				units.replace(unitName, units.get(unitName) - 1);
			}

		} else {
			removeRandomUnit();
		}

		// Notify observers.
		setChanged();
		notifyObservers();

	}

	/**
	 * Retrieves a whether a specific type of {@link ModelUnit} is currently in this
	 * {@link ModelArmy}.
	 * 
	 * @param unit
	 *            {@link ModelArmy}
	 * @return Whether a specific type of {@link ModelUnit} is currently in this
	 *         {@link ModelArmy}.
	 */
	public boolean hasUnit(ModelUnit unit) {

		/*
		 * If the unit is not in the map or if the unit is in the map and there is zero
		 * of the unit type, return false.
		 */
		return units.get(unit.name) != null && units.get(unit.name) != 0;
	}

	/**
	 * Retrieves a number of a specific {@link ModelUnit} that are currently in this
	 * {@link ModelArmy}.
	 * 
	 * @param unit
	 *            {@link ModelUnit} that is in the {@link ModelArmy}. Use
	 *            {@link ModelArmy#hasUnit(ModelUnit)} to determine this.
	 * @return Number of a specific {@link ModelUnit}
	 */
	public int getNumberOf(ModelUnit unit) {
		return units.get(unit.name);
	}

	/**
	 * Trades the {@link ModelUnit}s of the specified type for as many of the
	 * {@link ModelUnit} above them according to
	 * {@link UnitHelper#getUnitAbove(ModelUnit)} as possible. <br>
	 * <br>
	 * Will return false if:
	 * <ul>
	 * <li>There is no {@link ModelUnit} above.</li>
	 * <li>There is not enough of that {@link ModelUnit} in the {@link ModelArmy} to
	 * make one of the above {@link ModelUnit}.</li>
	 * <li>The unit is not in the {@link ModelArmy}.</li>
	 * </ul>
	 * 
	 * 
	 * @param unit
	 *            {@link ModelUnit} to trade up.
	 * @return Whether or not the {@link ModelUnit} was traded up or not.
	 */
	public boolean tradeUp(ModelUnit unit) {

		// If the unit is not in this army.
		if (!hasUnit(unit)) {
			return false;
		}

		// Holds the unit above the specified unit.
		final ModelUnit above = UnitHelper.getInstance().getUnitAbove(unit);

		// If there is no unit above the specified unit.
		if (above == null) {
			return false;
		}

		// The current number of the specified unit.
		final int numberOfUnit = units.get(unit.name);

		// The current combine strength of all the specified units.
		final int strengthCombined = numberOfUnit * unit.strength;

		// Whether the combined strength of all the units is enough to make one of the
		// unit above.
		final boolean strongerThanAbove = strengthCombined > above.strength;

		// If the combined strength of all the units is not enough to make one of the
		// unit above.
		if (!strongerThanAbove) {
			return false;
		}

		// The number of the unit above that can be made.
		final int numberOfAbove = strengthCombined / above.strength;

		// The number of remaining unit strength left by creating the units above.
		final int remainder = strengthCombined % above.strength;

		// Remove all the specified units from the army
		for (int index = 0; index < numberOfUnit; index++) {
			remove(unit);
		}

		// Add the number of the above unit that can be created.
		for (int index = 0; index < numberOfAbove; index++) {
			add(above);
		}

		// Convert the remaining strength into units and then add them to the army.
		generateUnits(remainder).forEach(smaller -> add(smaller));

		return true;

	}

	/**
	 * Returns the number of different types of {@link ModelUnit}s in this
	 * {@link ModelArmy}.
	 * 
	 * @return Variety of {@link ModelUnit}s.
	 */
	public int getVarietyOfUnits() {
		return units.size();
	}

	/**
	 * Retrieves the {@link Iterator} for this {@link ModelArmy} of all the
	 * {@link ModelUnit}s inside. The number of each {@link ModelUnit} in specific
	 * can be found using {@link ModelArmy#getNumberOf(ModelUnit)}.
	 */
	@Override
	public Iterator<ModelUnit> iterator() {

		Stream<ModelUnit> unitStream = units.keySet().stream().map(name -> UnitHelper.getInstance().get(name));

		return unitStream.collect(Collectors.toSet()).iterator();
	}

	/**
	 * Adds a {@link ModelUnit} to this {@link ModelArmy}.
	 * 
	 * @param unit
	 *            {@link ModelUnit} to add.
	 */
	public void add(ModelUnit unit) {

		final String unitName = unit.name;

		// The number of the specified unit currently in the army
		final int currentNumber = units.get(unitName) == null ? 0 : units.get(unitName);

		// Set the new number of the specified unit.
		units.put(unitName, currentNumber + 1);

		setChanged();
		notifyObservers();

	}

	/**
	 * Adds a {@link List} of {@link ModelUnit}s to this {@link ModelArmy}.
	 * 
	 * @param unitList
	 *            {@link List} of {@link ModelUnit}s to add.
	 */
	public void add(List<ModelUnit> unitList) {

		// Iterate through all the units and add them to the army.
		unitList.forEach(unit -> add(unit));
	}

	/**
	 * Adds all of the {@link ModelUnit}s from the specified {@link ModelArmy} into
	 * this {@link ModelArmy}. This will not remove the {@link ModelUnit}s from the
	 * specified {@link ModelArmy}.
	 * 
	 * @param army
	 *            The {@link ModelArmy} to be merged.
	 */
	public void merge(ModelArmy army) {

		// For every unit in the specified army add the number of that unit to this
		// army.
		for (ModelUnit unit : army) {

			for (int index = 0; index < army.getNumberOf(unit); index++) {
				add(unit);
			}
		}
	}

	/**
	 * Set strength of this {@link ModelArmy} and the {@link ModelUnit}s will be
	 * generated using {@link ModelArmy#generateUnits(int)}.
	 * 
	 * @param strength
	 *            The new strength of the {@link ModelArmy}.
	 */
	public void setStrength(int strength) {

		// Check the parameters.
		if (strength < 0) {
			throw new IllegalArgumentException("New strength can not be less than zero.");
		}

		// Clear the units from the army.
		clearUnits();

		// Populate the army with new units.
		populateArmy(strength);

		// Notify the observers that the army has changed.
		setChanged();
		notifyObservers();

	}

	/**
	 * Set this {@link ModelArmy} to have one of the weakest {@link ModelUnit}
	 * according to {@link UnitHelper#getWeakest()}.
	 */
	public void setWeakest() {

		// Remove all the units
		clearUnits();

		// Set the army to its weakest not empty value.
		units.put(UnitHelper.getInstance().getWeakest().name, 1);

		setChanged();
		notifyObservers();

	}

	/**
	 * Retrieves the weakest {@link ModelUnit} in this {@link ModelArmy}.
	 * 
	 * @return Weakest {@link ModelUnit} in this {@link ModelArmy}.
	 */
	@Override
	public ModelUnit getWeakestUnit() {

		/**
		 * Starting from the weakest unit currently in the game step up in strength
		 * until a unit of the current type is in the army.
		 */

		ModelUnit current = UnitHelper.getInstance().getWeakest();

		while (current != null) {

			if (hasUnit(current)) {
				return current;
			} else {
				current = UnitHelper.getInstance().getUnitAbove(current);
			}
		}

		return null;
	}

	/**
	 * Retrieves the strongest {@link ModelUnit} in this {@link ModelArmy}.
	 * 
	 * @return Strongest {@link ModelUnit} in this {@link ModelArmy}.
	 */
	@Override
	public ModelUnit getStrongestUnit() {

		/**
		 * Starting from the strongest unit currently in the game step down in strength
		 * until a unit of the current type is in the army.
		 */
		ModelUnit current = UnitHelper.getInstance().getStrongest();

		while (current != null) {
			if (hasUnit(current)) {
				return current;
			} else {
				current = UnitHelper.getInstance().getUnitBelow(current);
			}
		}

		return null;
	}

	/**
	 * Removes a amount of {@link ModelUnit} strength from this {@link ModelArmy}.
	 * If the loss in strength would reduce the {@link ModelArmy}'s strength to less
	 * than zero then the {@link ModelArmy} will be emptied.
	 * 
	 * @param strength
	 *            The amount of {@link ModelUnit} strength to remove.
	 */
	public void remove(int strength) {

		// The strength of the army post strength removal.
		final int newStrength = getStrength() - strength;

		// If the new strength of the army is more that one of the weakest units then
		// populate the army. Otherwise clear the army.
		if (newStrength >= UnitHelper.getInstance().getWeakest().strength) {
			populateArmy(newStrength);
		} else {
			clearUnits();
		}

		// Notify observers of the change.
		setChanged();
		notifyObservers();

	}

	/**
	 * Removes a random {@link ModelUnit} from this {@link ModelArmy}.
	 */
	public void removeRandomUnit() {

		final int size = units.size();
		final int item = new Random().nextInt(size);

		int i = 0;
		ModelUnit toRemove = null;

		for (ModelUnit unit : this) {
			if (i == item) {
				toRemove = unit;
			}
			i++;
		}

		remove(toRemove);

	}

	/**
	 * Removes all the {@link ModelUnit}s from this {@link ModelArmy} provided there
	 * are {@link ModelUnit}s to remove.
	 */
	private void clearUnits() {

		// Clear the current map of units if there are units to clear.
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

		// Notify observers that the selected has changed.
		setChanged();
		notifyObservers(new Update("selected", selected));

	}

	/**
	 * Re-populates this {@link ModelArmy} based on the specified new strength of
	 * the {@link ModelArmy}.
	 * 
	 * @param strength
	 *            The new strength of this {@link ModelArmy}.
	 */
	private void populateArmy(int strength) {

		// De-select the currently selected unit.
		deselect();

		// Clear the current list of units.
		clearUnits();

		if (strength == 0) {
			return;
		}

		final ModelUnit weakest = UnitHelper.getInstance().getWeakest();

		final int numberOfWeakest = strength / weakest.strength;

		for (int i = 0; i < numberOfWeakest; i++) {

			// Generate the units then add them to the current army.
			add(weakest);

		}

	}

	/**
	 * Generates a {@link List} of {@link ModelUnit}s who's combined strength is
	 * equal to the specified strength. This method will prioritise the strongest
	 * {@link ModelUnit}s first according to {@link UnitHelper#getStrongest()}. The
	 * Specified strength should not be or result in a strength lower than the
	 * weakest {@link ModelUnit}'s strength according to
	 * {@link UnitHelper#getWeakest()}.
	 * 
	 * @param strength
	 *            The combined strength of the {@link List} of {@link ModelUnit}s.
	 * 
	 * @return {@link List} of {@link ModelUnit}.
	 */
	public static List<ModelUnit> generateUnits(int strength) {

		// List that will contain the generated units.
		final List<ModelUnit> units = new LinkedList<>();

		// Holds the current strongest unit that is smaller than the army size.
		ModelUnit unit = UnitHelper.getInstance().getStrongest();

		// The strength remaining to construct units
		int remianingStrength = strength;

		// While the army can be further divided.
		while (remianingStrength > 0) {

			// If the unit is null then there is no unit weaker than the previous but there
			// is still strength remaining then throw an exception as there is no way to use
			// the strength.
			if (unit == null) {
				throw new IllegalStateException("There is not unit teir that the army can be divide further into.");
			}

			// If the current unit fits into the army add one of that unit to the
			// army that can be fit.
			if (unit.strength <= remianingStrength) {
				units.add(unit);
				remianingStrength -= unit.strength;
			} else {
				// Move to the unit below in strength.
				unit = UnitHelper.getInstance().getUnitBelow(unit);
			}

		}

		return units;

	}

	/**
	 * Retrieves the number of {@link ModelUnit}s in this {@link ModelArmy}.
	 * 
	 * @return Number of {@link ModelUnit}s
	 */
	public int getNumberOfUnits() {

		int number = 0;

		// Iterate through all the numbers of each unit type and add them up.
		for (Integer amount : units.values()) {
			number += amount;
		}

		return number;
	}

	@Override
	public Set<? extends Unit> getUnits() {
		// Copy the key set into a new map so that this army cannot be modified.
		return new HashSet<Unit>(
				units.keySet().stream().map(name -> UnitHelper.getInstance().get(name)).collect(Collectors.toSet()));
	}

	@Override
	public int getNumberOf(Unit unit) {
		return getNumberOf((ModelUnit) unit);
	}

	@Override
	public boolean hasUnit(Unit unit) {
		return hasUnit((ModelUnit) unit);
	}

	/**
	 * Trades all the {@link ModelUnit}s in the {@link ModelArmy} up to make this
	 * {@link ModelArmy} as strong as possible.
	 * 
	 * @return Whether any of the {@link ModelUnit}s in this {@link ModelArmy} were
	 *         traded up.
	 */
	public boolean tradeUnitsUp() {

		// If there are no units then trade up failed.
		if (units.isEmpty()) {
			return false;
		}

		// Holds whether any units have been traded up.
		boolean hasTraded = false;

		// Holds the current unit starting with the weakest.
		ModelUnit current = getWeakestUnit();

		/*
		 * Iterate over all the units in this army starting at the weakest and working
		 * up in terms of strength and attempt to trade each of them up.
		 */
		while (current != null) {

			if (hasUnit(current)) {
				if (tradeUp(current)) {
					hasTraded = true;
				}
			}

			current = UnitHelper.getInstance().getUnitAbove(current);

		}

		return hasTraded;
	}

}
