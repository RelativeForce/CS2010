package peril.board;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;

import peril.Player;
import peril.ui.visual.Clickable;

/**
 * Encapsulates the behaviour of a Country. Countries:
 * <ul>
 * <li>Have and manage {@link Army}s.</li>
 * <li>Have a name.</li>
 * <li>Depend of the countries they are linked to.</li>
 * <li>Have a {@link Player} that rules them.</li>
 * </ul>
 * 
 * @author Joshua_Eddy, James_Rowntree
 * 
 * @see java.util.LinkedList
 * @see Java.util.List
 *
 */
public class Country extends Clickable {

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
	 * Holds the name of the {@link Country}.
	 */
	private String name;

	/**
	 * Constructs a new {@link Country}.
	 * 
	 * @param name
	 *            of the {@link Country}.
	 */
	public Country(String name) {
		this.neighbours = new LinkedList<Country>();
		this.ruler = null;
		this.army = new Army(1);
		this.name = name;
	}

	/**
	 * Returns the name of the {@link Country}.
	 * 
	 * @return name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the current {@link Ruler} of this {@link Country}.
	 * 
	 * @param ruler
	 *            {@link Player}
	 */
	public void setRuler(Player ruler) {
		this.ruler = ruler;
		if (ruler != null) {
			this.setImage(getRegion().getPosition(), getRegion().convert(ruler.getColor()));
		} else {
			this.setImage(getRegion().getPosition(), getRegion().convert(Color.white));
		}

	}

	/**
	 * Set an {@link Country#army} to the specifies army.
	 * 
	 * @param army
	 */
	public void setArmy(Army army) {
		if (army == null) {
			throw new NullPointerException("Current army cannnot be null");
		}
		this.army = army;
	}

	/**
	 * Retrieves the {@link Army} at this {@link Country}.
	 * 
	 * @return {@link Country#army}.
	 */
	public Army getArmy() {
		return army;
	}

	/**
	 * Retrieves the {@link Country#neighbours}.
	 * 
	 * @return {@link List} of type {@link Country}.
	 */
	public List<Country> getNeighbours() {
		return neighbours;
	}

	/**
	 * Adds a {@link Country} that this country is linked to.
	 * 
	 * @param neighbour
	 *            {@link Country}
	 */
	public void addNeighbour(Country neighbour) {
		if (neighbour == null) {
			throw new NullPointerException("The neighbour cannot be null");
		}
		neighbours.add(neighbour);
	}

	/**
	 * Performs the end round operation for this {@link Country}.
	 */
	public void endRound(EnvironmentalHazard hazard) {
		hazard.act(army);
	}

	/**
	 * Retrieves the {@link Player} the rules <code>this</code> {@link Country}.
	 * 
	 * @return {@link Country#ruler}.
	 */
	public Player getRuler() {
		return ruler;
	}

}
