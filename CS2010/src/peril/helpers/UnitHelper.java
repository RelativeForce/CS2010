package peril.helpers;

import java.util.LinkedList;

import peril.model.board.ModelUnit;

/**
 * Stores the {@link ModelUnit}s that are currently in use. Singleton
 * 
 * @author Joshua_Eddy
 *
 */
public final class UnitHelper {

	/**
	 * The singleton instance of this {@link UnitHelper}.
	 */
	private final static UnitHelper INSTANCE = new UnitHelper();

	/**
	 * The {@link OrderedList} that holds all the {@link ModelUnit}.
	 */
	private OrderedList<ModelUnit> units;

	/**
	 * Constructs a new {@link UnitHelper}.
	 */
	private UnitHelper() {
		units = new OrderedList<>();
	}

	/**
	 * Retrieves the singleton instance of this {@link UnitHelper}.
	 * 
	 * @return {@link UnitHelper}
	 */
	public static UnitHelper getInstance() {
		return INSTANCE;
	}

	/**
	 * Adds a new type of {@link ModelUnit}.
	 * 
	 * @param unit
	 *            {@link ModelUnit}
	 */
	public void addUnit(ModelUnit unit) {
		units.add(unit);
	}

	/**
	 * Adds a new type of {@link ModelUnit}.
	 * 
	 * @param name
	 *            of the {@link ModelUnit}.
	 * @param strength
	 *            of the {@link ModelUnit}.
	 */
	public void addUnit(String name, int strength) {
		units.add(new ModelUnit(name, strength));
	}

	/**
	 * Removes all the {@link ModelUnit} that this {@link UnitHelper} contains.
	 */
	public void clear() {
		units.clear();
	}

	/**
	 * Retrieves the strongest {@link ModelUnit} contained in this
	 * {@link UnitHelper}.
	 * 
	 * @return {@link ModelUnit}
	 */
	public ModelUnit getStrongest() {
		return units.getLast();
	}

	/**
	 * Retrieves the weakest {@link ModelUnit} contained in this {@link UnitHelper}.
	 * 
	 * @return {@link ModelUnit}
	 * 
	 */
	public ModelUnit getWeakest() {
		return units.getFirst();
	}

	/**
	 * Retrieves the {@link ModelUnit} above the specified {@link ModelUnit} in
	 * terms of strength that is contained in this {@link UnitHelper}.
	 * 
	 * @return {@link ModelUnit}
	 * @param unit
	 *            {@link ModelUnit}
	 */
	public ModelUnit getUnitAbove(ModelUnit unit) {

		int index = units.indexOf(unit);

		if (index < 0 || index > units.size() - 1) {
			return null;
		}

		return units.get(index + 1);
	}

	/**
	 * Retrieves the {@link ModelUnit} below the specified {@link ModelUnit} in
	 * terms of strength that is contained in this {@link UnitHelper}.
	 * 
	 * @return {@link ModelUnit}
	 * @param unit
	 *            {@link ModelUnit}
	 */
	public ModelUnit getUnitBelow(ModelUnit unit) {
		int index = units.indexOf(unit);

		if (index - 1 < 0) {
			return null;
		}

		return units.get(index - 1);
	}

	/**
	 * A ordered {@link LinkedList}.
	 * 
	 * @author Joshua_Eddy
	 *
	 * @param <T>
	 *            An object that is {@link Comparable}.
	 */
	private class OrderedList<T extends Comparable<T>> extends LinkedList<T> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8011586922814965872L;

		/**
		 * Positions the elements in the {@link LinkedList} based on its
		 * {@link T#compareTo(Comparable)} value.
		 */
		@Override
		public boolean add(T element) {

			int index = 0;
			boolean found = false;

			/**
			 * Find the elements position in the list
			 */
			while (this.iterator().hasNext() && !found) {

				if (iterator().next().compareTo(element) < 0 || index == size()) {
					found = true;
				} else {
					index++;
				}
			}

			super.add(index, element);

			return true;
		}

	}

}
