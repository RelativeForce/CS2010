package peril.board;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import peril.Player;
import peril.ui.Veiwable;
import peril.ui.VisualRepresenation;

/**
 * Encapsulates the behaviour of a Country. Countries:
 * <ul>
 * <li>Have and manage {@link Army}s.</li>
 * <li>Have a name.</li>
 * <li>Depend of the countries they are linked to.</li>
 * <li>Have a {@link Player} that rules them.</li>
 * </ul>
 * 
 * @author Joshua_Eddy
 * 
 * @see java.util.LinkedList
 * @see Java.util.List
 *
 */
public class Country implements Veiwable {

	/**
	 * Holds the {@link Player} that rules this {@link Country}.
	 */
	private Player ruler;

	/**
	 * Holds the {@link Country}(s) that this country is linked to.
	 * 
	 * @see java.util.List
	 */
	private List<Country> neighbours;

	/**
	 * Holds the army occupying this {@link Country}.
	 * 
	 * @see Army {@link Army}
	 */
	private Army army;

	/**
	 * Holds the visual representation of the {@link Country}.
	 * 
	 * @see VisualRepresenation
	 */
	private VisualRepresenation visual;

	/**
	 * Constructs a new {@link Continent}.
	 * 
	 * @param ruler
	 *            The {@link Player} ruler of this {@link Country}.
	 * @param army
	 *            The {@link Army} stationed at this {@link Country}.
	 * @param neighbours
	 *            The array of {@link Country}(s) that this country is linked
	 *            to.
	 */
	public Country(Player ruler, Army army, Country[] neighbours) {
		constrcutor(ruler);

		// Adds all the neighbours to the list.
		this.neighbours.addAll(Arrays.asList(neighbours));
	}

	/**
	 * Constructs a new {@link Continent} with a specified initial ruler and
	 * army.
	 * 
	 * @param ruler
	 *            The {@link Player} ruler of this {@link Country}.
	 * @param army
	 *            The {@link Army} stationed at this {@link Country}.
	 */
	public Country(Player ruler, Army army) {
		constrcutor(ruler);

	}

	/**
	 * Adds a {@link Country} that this country is linked to.
	 * 
	 * @param neighbour
	 *            {@link Country}
	 */
	public void addNeighbour(Country neighbour) {
		neighbours.add(neighbour);
	}

	/**
	 * Performs the actions shared by all the constructors of {@link Country}.
	 * 
	 * @param ruler
	 *            The {@link Player} ruler of this {@link Country}.
	 * @see java.util.LinkedList
	 */
	private void constrcutor(Player ruler) {

		// Assign the common fields.
		this.neighbours = new LinkedList<Country>();
		this.ruler = ruler;
		this.army = new Army();
		this.visual = new Visual();
	}

	@Override
	public VisualRepresenation getVisual() {
		return visual;
	}

	/**
	 * The visual representation of the {@link Country}.
	 * 
	 * @author Joshua_Eddy
	 *
	 * @see VisualRepresenation
	 */
	private class Visual extends VisualRepresenation {

	}

}
