package peril.board;

import peril.ui.Veiwable;
import peril.ui.VisualRepresenation;

/**
 * Encapsulates the behaviour of a continent on the {@link Board}.
 * Continents;<br>
 * <ul>
 * <li>Group {@link Country}s</li>
 * <li>Apply a special visual effect when all the {@link Country}s with in it
 * are ruled by the same {@link Player}.</li>
 * <li>Award {@link Player} with bonuses when they rule all the {@link Country}s
 * within.</li>
 * </ul>
 * 
 * @author Joshua_Eddy
 *
 */
public final class Continent implements Veiwable {

	/**
	 * Holds the {@link Countries} that comprise this {@link Continent}.
	 */
	private Country[] countries;
	
	/**
	 * Holds the visual representation of the {@link Continent}.
	 * @see VisualRepresenation
	 */
	private VisualRepresenation visual;

	/**
	 * Constructs a new {@link Continent}.
	 * @param countries
	 */
	public Continent(Country[] countries) {

		this.countries = countries;
		this.visual = new Visual();

	}

	/**
	 * The visual representation of the {@link Continent}.
	 * 
	 * @author Joshua_Eddy
	 *
	 *@see VisualRepresenation
	 */
	private class Visual extends VisualRepresenation {

	}

	@Override
	public VisualRepresenation getVisual() {
		return visual;
	}

}
