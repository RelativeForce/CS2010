package peril.board;

import java.util.List;

import peril.Point;
import peril.ui.visual.Clickable;

/**
 * Encapsulates the behaviour of the the game board in the {@link Game}. This
 * realises {@link Viewable} allowing it to be displayed by the
 * {@link UserInterface}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class Board extends Clickable {

	/**
	 * The {@link Continent}s in this {@link Board}.
	 */
	private List<Continent> continents;

	/**
	 * Constructs a {@link Board}.
	 */
	public Board(List<Continent> continents) {
		this.continents = continents;
	}

	/**
	 * Retrieves a {@link Country} that is specified by the parameter {@link Point}.
	 * 
	 * @param click
	 *            {@link Point} on the board.
	 * @return {@link Country}.
	 */
	public Country getCountry(Point click) {

		// Iterate through all the continents on the board.
		for (Continent continent : continents) {

			// If the click is inside the continents region get the country from the region.
			if (continent.isClicked(click)) {
				return continent.getCountry(click);
			}
		}

		// If the point is not on the board return null.
		return null;

	}

	/**
	 * Retrieves the {@link Continent}s on this {@link Board}.
	 * 
	 * @return {@link List} of {@link Continent}s.
	 * 
	 * @see java.util.List
	 */
	public List<Continent> getContinents() {
		return continents;
	}

	/**
	 * Iterates through each {@link Continent} in the {@link Board} and
	 * {@link Continent#executeTurn()}.
	 */
	public void endRound() {
		continents.forEach(continent -> continent.endRound());
	}
}
