package peril.model.states;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import peril.Update;
import peril.controllers.GameController;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelCountry;

/**
 * 
 * A state of the core game play loop where many {@link ModelCountry}s can be
 * selected and the sub-types define which {@link ModelCountry}s can be
 * selected.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-14
 * @version 1.01.02
 * 
 * @see Observable
 *
 */
public abstract class ModelState extends Observable {

	/**
	 * The currently selected {@link ModelCountry}s.
	 */
	private final List<ModelCountry> selected;

	/**
	 * Holds the name of this {@link ModelState}.
	 */
	private final String stateName;

	/**
	 * Constructs an new {@link ModelState}.
	 * 
	 * @param stateName
	 *            The name of the {@link ModelState}.
	 */
	public ModelState(String stateName) {
		this.selected = new LinkedList<>();
		this.stateName = stateName;
	}

	/**
	 * Determines whether or not the specified {@link ModelCountry} has been
	 * selected by this {@link ModelState}. If the specified {@link ModelCountry} is
	 * <code>null</code> then this should return <code>false</code>.
	 * 
	 * @param country
	 *            The {@link ModelCountry} that can be selected.
	 * @param game
	 *            The {@link GameController} that allows this {@link ModelState} to
	 *            query the state of the game.
	 * @return Whether or not the specified {@link ModelCountry} has been selected
	 *         by this {@link ModelState}.
	 */
	public abstract boolean select(ModelCountry country, GameController game);

	/**
	 * Retrieves the name of a specific {@link ModelState}.
	 * 
	 * @return The name of the {@link ModelState}.
	 */
	public final String getName() {
		return stateName;
	}

	/**
	 * Removes all the currently selected {@link ModelCountry}s from this
	 * {@link ModelState}.
	 */
	public final void deselectAll() {

		if (!selected.isEmpty()) {
			selected.clear();
		}

		setChanged();
		notifyObservers(new Update("selected", selected));
	}

	/**
	 * Returns a the current highlighted {@link SlickCountry} in this state.
	 * 
	 */
	public final ModelCountry getSelected(int index) {

		// If the index maps to a selected country.
		if (selected.isEmpty() || index >= selected.size()) {
			return null;
		}

		return selected.get(index);
	}

	/**
	 * Returns the current number of currently selected {@link ModelCountry}s in
	 * this {@link ModelState}.
	 * 
	 * @return Current number of currently selected {@link ModelCountry}s.
	 */
	public final int numberOfSelected() {
		return selected.size();
	}

	/**
	 * Swaps the ruler of a specified {@link ModelCountry} for a new
	 * {@link ModelPlayer}.
	 * 
	 * @param country
	 *            {@link ModelCountry}
	 * @param newRuler
	 *            The new {@link ModelPlayer} ruler of the {@link ModelCountry}.
	 */
	public final void swapRuler(ModelCountry country, ModelPlayer newRuler) {

		// Holds the old ruler of the country
		ModelPlayer oldRuler = country.getRuler();

		// If the country has a ruler reduce the number of countries that player owns by
		// one.
		if (oldRuler != null) {
			oldRuler.setCountriesRuled(oldRuler.getCountriesRuled() - 1);
			oldRuler.totalArmy.remove(country.getArmy().getStrength());
		}

		// Reassign the ruler of the country.
		country.setRuler(newRuler);

		// If the country has a new ruler increase the number of countries that player
		// owns by one.
		if (newRuler != null) {
			newRuler.setCountriesRuled(newRuler.getCountriesRuled() + 1);
			newRuler.totalArmy.add(ModelArmy.generateUnits(country.getArmy().getStrength()));
		}

	}

	/**
	 * Sets the currently selected {@link ModelCountry} as <code>null</code>.
	 */
	protected final void deselectAt(int index) {

		// If the index does not map to a selected country do nothing.
		if (selected.isEmpty() || index < 0 || index > selected.size() - 1) {
			return;
		}

		selected.remove(index);

		setChanged();
		notifyObservers(new Update("selected", selected));

	}

	/**
	 * Set the {@link ModelCountry} as the selected {@link ModelCountry} at the
	 * specified index.
	 */
	protected final void addSelected(ModelCountry country, int index) {

		selected.add(index, country);

		setChanged();
		notifyObservers(new Update("selected", selected));
	}

}
