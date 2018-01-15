package peril.model.states;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;

import peril.Player;
import peril.board.Country;
import peril.controllers.ModelController;
import peril.ui.states.gameStates.CoreGameState;

public abstract class ModelState {

	/**
	 * The current {@link Country} that the player has highlighted.
	 */
	private List<Country> selected;

	public ModelState() {
		selected = new LinkedList<>();
	}

	/**
	 * Sets the currently selected {@link Country} as <code>null</code>.
	 */
	public final void deselectAll() {
		selected.clear();
	}

	/**
	 * Sets the currently selected {@link Country} as <code>null</code>.
	 */
	protected final void deselectAt(int index) {

		if (selected.isEmpty()) {
			throw new IllegalStateException("There are no countries selected.");
		} else if (index < 0 && index > selected.size() - 1) {
			throw new IllegalArgumentException(index + " is not a valid index.");
		}
		
		selected.remove(index);

	}

	/**
	 * Returns a the current highlighted {@link Country} in this state.
	 * 
	 */
	public Country getSelected(int index) {
		return selected.get(index);
	}

	/**
	 * Set the current {@link Country} that the player has highlighted.
	 */
	protected void addSelected(Country country, int index) {
		selected.add(index, country);
	}

	public final int numberOfSelected() {
		return selected.size();
	}

	/**
	 * Determines whether or not the specified {@link Country} has been selected by
	 * this {@link CoreGameState}. If the specified {@link Country} is
	 * <code>null</code> then this should return <code>false</code>.
	 * 
	 * @param country
	 *            {@link Country}
	 * @return Whether or not the specified {@link Country} has been selected by
	 *         this {@link ModelState}.
	 */
	public abstract boolean select(Country country, ModelController api);

	/**
	 * Adds the highlight effect to a {@link Country} assuming the country is not
	 * <code>null</code>.
	 * 
	 * @param country
	 *            {@link Country}
	 */
	protected final void addHighlight(Country country) {
		if (country != null) {
			country.replaceImage(country.getRegion().convert(Color.yellow));
		}
	}

	/**
	 * Removes the highlight colouring effect on {@link CoreGameState#selected}.
	 * 
	 * @param country
	 *            {@link Country} to unhighlight.
	 */
	protected final void removeHighlight(Country country) {
	
		// If there is a highlighted country
		if (country != null) {
	
			// Holds the ruler of the country
			Player ruler = country.getRuler();
	
			// If there is a ruler then return the colour of the country to that of the
			// ruler. Otherwise remove the highlight effect.
			country.replaceImage(ruler != null ? country.getRegion().convert(ruler.color)
					: country.getRegion().convert(Color.white));
	
		}
	
	}

}
