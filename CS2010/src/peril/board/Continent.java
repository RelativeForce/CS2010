package peril.board;

import java.util.LinkedList;
import java.util.List;

import org.junit.experimental.theories.Theories;

import peril.Player;
import peril.ui.Viewable;
import peril.ui.VisualRepresentation;

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
public final class Continent implements Viewable {

	/**
	 * Holds the {@link Countries} that comprise this {@link Continent}.
	 */
	private List<Country> countries;

	/**
	 * The {@link EnvironmentalHazard} that may affect this {@link Continent}.
	 */
	private EnvironmentalHazard hazard;

	/**
	 * The current {@link Player} that rules all {@link Country}s in this
	 * {@link Continent}. If all the {@link Country}s in this {@link Continent} are
	 * NOT ruled by the same {@link Player} then this is <code>null</code>. This is
	 * determined in {@link Continent#isRuled()}.
	 * 
	 */
	private Player ruler;

	/**
	 * Holds the visual representation of the {@link Continent}.
	 * 
	 * @see VisualRepresentation
	 */
	private VisualRepresentation visual;

	/**
	 * Constructs a new {@link Continent}.
	 * 
	 * @param countries
	 */
	public Continent(EnvironmentalHazard hazard) {

		this.countries = new LinkedList<Country>();
		this.hazard = hazard;
		this.visual = new Visual();
		this.ruler = null;

	}

	/**
	 * The visual representation of the {@link Continent}.
	 * 
	 * @author Joshua_Eddy
	 *
	 * @see VisualRepresentation
	 */
	private class Visual extends VisualRepresentation {

	}

	/**
	 * Determines and retrieves whether or not one {@link Player} rules all the
	 * {@link Country}s in this {@link Continent}.
	 * 
	 * @return whether the ruler rules all {@link Country}s in the
	 *         {@link Continent}.
	 */
	public boolean isRuled() {
		// TODO: Whether all the countries are ruled by a particular player.
		return ruler != null;
	}

	/**
	 * Returns the current {@link Player} ruling this {@link Continent}.
	 * 
	 * @return {@link Player}.
	 */
	public Player getRuler() {
		return ruler;
	}

	/**
	 * Iterates through all the {@link Continent} and executes their turn. Also
	 * causes each {@link Country} to experience this {@link Continent}s
	 * {@link EnvironmentalHazard}.
	 */
	public void executeTurn() {
		// Iterates through all countries in the continent.
		for (Country currentCountry : countries) {
			currentCountry.endRound();
			hazard.act(currentCountry.getArmy());
		}

	}

	@Override
	public VisualRepresentation getVisual() {
		return visual;
	}

}
