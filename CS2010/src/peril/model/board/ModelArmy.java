package peril.model.board;

import java.util.Observable;

import peril.Update;

/**
 * Encapsulates the behaviours of a collection of units. This will be aggregated
 * by the {@link WarMenu} and composes the {@link ModelCountry}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class ModelArmy extends Observable{

	/**
	 * The size of the army.
	 */
	private int size;

	/**
	 * Constructs a new {@link ModelArmy} with size of 1 and an offset of (0,0).
	 */
	public ModelArmy() {
		setSize(1);
	}

	/**
	 * Constructs a new {@link ModelArmy} of a specified size with an offset of (0,0).
	 * 
	 * @param size
	 *            The size of the {@link ModelArmy}. Must be greater than zero.
	 */
	public ModelArmy(int size) {
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
		this.size = size;
		
		setChanged();
		notifyObservers(new Update("size", size));
	}

	/**
	 * Retrieves the size of the {@link ModelArmy}.
	 * 
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Adds a specified number of units to this {@link ModelArmy}.
	 * 
	 * @param amount
	 *            of units to add to this {@link ModelArmy}
	 */
	public void add(int amount) {
		size += amount;
		
		setChanged();
		notifyObservers(new Update("size", size));
	}

	/**
	 * Removes a specified amount of units from this {@link ModelArmy}.
	 * 
	 * @param amount
	 *            of units to remove.
	 */
	public void remove(int amount) {
		if (size - amount < 0) {
			throw new IllegalStateException("Army size cannot be less than zero");
		}
		size -= amount;
		
		setChanged();
		notifyObservers(new Update("size", size));
	}
}
