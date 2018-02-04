package peril.model.states;

import java.util.LinkedList;
import java.util.Observable;

import peril.Update;
import peril.controllers.GameController;
import peril.helpers.UnitHelper;
import peril.model.ModelPlayer;
import peril.model.board.ModelCountry;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickPlayer;

public abstract class ModelState extends Observable {

	/**
	 * The current {@link SlickCountry} that the player has highlighted.
	 */
	private LinkedList<ModelCountry> selected;

	public ModelState() {
		selected = new LinkedList<>();
	}

	/**
	 * Sets the currently selected {@link SlickCountry} as <code>null</code>.
	 */
	public final void deselectAll() {

		selected.clear();

		setChanged();
		notifyObservers(new Update("selected", selected));
	}

	/**
	 * Sets the currently selected {@link SlickCountry} as <code>null</code>.
	 */
	protected final void deselectAt(int index) {

		if (selected.isEmpty() || index < 0 || index > selected.size() - 1) {
			return;
		}

		selected.remove(index);

		setChanged();
		notifyObservers(new Update("selected", selected));

	}

	/**
	 * Returns a the current highlighted {@link SlickCountry} in this state.
	 * 
	 */
	public ModelCountry getSelected(int index) {
		if (selected.isEmpty() || index >= selected.size()) {
			return null;
		}

		return selected.get(index);
	}

	/**
	 * Set the current {@link SlickCountry} that the player has highlighted.
	 */
	protected void addSelected(ModelCountry country, int index) {

		selected.add(index, country);

		setChanged();
		notifyObservers(new Update("selected", selected));
	}

	/**
	 * Returns the current number of currently selected {@link ModelCountry}s in
	 * this {@link ModelState}.
	 * 
	 * @return int
	 */
	public final int numberOfSelected() {
		return selected.size();
	}

	/**
	 * Determines whether or not the specified {@link SlickCountry} has been
	 * selected by this {@link CoreGameState}. If the specified {@link SlickCountry}
	 * is <code>null</code> then this should return <code>false</code>.
	 * 
	 * @param country
	 *            {@link SlickCountry}
	 * @return Whether or not the specified {@link SlickCountry} has been selected
	 *         by this {@link ModelState}.
	 */
	public abstract boolean select(ModelCountry country, GameController api);

	/**
	 * Swaps the {@link SlickPlayer} ruler of a specified {@link SlickCountry} for a
	 * new {@link player}.
	 * 
	 * @param country
	 *            {@link SlickCountry}
	 * @param newRuler
	 *            {@link SlickPlayer} new ruler of the {@link SlickCountry}.
	 */
	public void swapRuler(ModelCountry country, ModelPlayer newRuler) {

		// Holds the old ruler of the country
		ModelPlayer oldRuler = country.getRuler();

		// If the country has a ruler reduce the number of countries that player owns by
		// one.
		if (oldRuler != null) {
			oldRuler.setCountriesRuled(oldRuler.getCountriesRuled() - 1);
			oldRuler.totalArmy.remove(UnitHelper.getInstance().getWeakest());
		}

		// Reassign the ruler of the country.
		country.setRuler(newRuler);

		// If the country has a new ruler increase the number of countries that player
		// owns by one.
		if (newRuler != null) {
			newRuler.setCountriesRuled(newRuler.getCountriesRuled() + 1);
			newRuler.totalArmy.add(UnitHelper.getInstance().getWeakest());
		}

	}

}