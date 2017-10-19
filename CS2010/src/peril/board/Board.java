package peril.board;

import java.util.LinkedList;
import java.util.List;

import peril.Point;
import peril.ui.Viewable;
import peril.ui.VisualRepresenation;

/**
 * Encapsulates the behaviour of the the game board in the {@link Game}. This
 * realises {@link Viewable} allowing it to be displayed by the
 * {@link UserInterface}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class Board implements Viewable {

	/**
	 * Holds the {@link VisualRepresenation} of the {@link Board}.
	 */
	private Visual visual;

	/**
	 * Holds the {@link Continents} on the {@link Board}.
	 * 
	 * @see java.util.List
	 */
	private List<Continent> continents;

	/**
	 * Constructs a {@link Board}.
	 */
	private Board() {
		visual = new Visual();
		continents = new LinkedList<>();
	}

	/**
	 * Retrieves a {@link Country} that is specified by the parameter {@link Point}.
	 * 
	 * @param click
	 *            {@link Point} on the board.
	 * @return {@link Country}.
	 */
	private Country getCountry(Point click) {

		// TODO: Add the implementation that searches through all the continents regions
		// and if the the point is inside the region then search all the countries in
		// that continent.

		// Iterate through all the continents on the board.
		for (Continent continent : continents) {

			// If the click is inside the continents region get the country from the region.
			if (continent.getVisual().isClicked(click)) {
				return continent.getCountryByClick(click);
			}
		}

		// If the point is not on the board return null.
		return null;

	}

	/**
	 * The visual representation of the {@link Board}.
	 * 
	 * @author Joshua_Eddy
	 *
	 */
	private class Visual extends VisualRepresenation {

	}

	@Override
	public VisualRepresenation getVisual() {
		return visual;
	}

}
