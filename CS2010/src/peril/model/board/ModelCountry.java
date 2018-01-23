package peril.model.board;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import peril.Update;
import peril.controllers.api.Country;
import peril.controllers.api.Player;
import peril.model.ModelPlayer;

/**
 * Encapsulates the behaviour of a Country. Countries:
 * <ul>
 * <li>Have and manage {@link ModelArmy}s.</li>
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
public final class ModelCountry extends Observable implements Country {

	/**
	 * Holds the {@link Player} that rules this {@link ModelCountry}.
	 */
	private ModelPlayer ruler;

	/**
	 * Holds the {@link ModelCountry}(s) that this country is linked to.
	 * 
	 * @see java.util.List
	 */
	private final List<ModelCountry> neighbours;

	/**
	 * Holds the army occupying this {@link ModelCountry}.
	 * 
	 * @see ModelArmy {@link ModelArmy}
	 */
	private final ModelArmy army;

	/**
	 * Holds the name of the {@link ModelCountry}.
	 */
	private final String name;

	/**
	 * Constructs a new {@link ModelCountry} with a specified army offset.
	 * 
	 * @param name
	 *            of the {@link ModelCountry}
	 * @param region
	 *            {@link Region} of the country on screen.
	 * @param color
	 *            The colour that denotes this {@link ModelCountry} in the countries
	 *            image.
	 * @param armyOffset
	 *            The {@link Point} offset from this {@link ModelCountry}'s center.
	 */
	public ModelCountry(String name) {

		this.neighbours = new LinkedList<ModelCountry>();
		this.ruler = null;
		this.army = new ModelArmy(1);
		this.name = name;

	}

	/**
	 * Returns the name of the {@link ModelCountry}.
	 * 
	 * @return name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the current {@link Ruler} of this {@link ModelCountry}.
	 * 
	 * @param ruler
	 *            {@link Player}
	 */
	public void setRuler(ModelPlayer ruler) {
		this.ruler = ruler;
		
		setChanged();
		notifyObservers(new Update("ruler", ruler));
	}

	/**
	 * Returns the size of this {@link ModelCountry}'s {@link ModelArmy}.
	 */
	public int getArmySize() {
		return army.getSize();
	}

	/**
	 * Retrieves the {@link ModelArmy} at this {@link ModelCountry}.
	 * 
	 * @return {@link ModelCountry#army}.
	 */
	public ModelArmy getArmy() {
		return army;
	}

	/**
	 * Retrieves the {@link ModelCountry#neighbours}.
	 * 
	 * @return {@link List} of type {@link ModelCountry}.
	 */
	public List<ModelCountry> getNeighbours() {
		return neighbours;
	}

	/**
	 * Checks if the {@link ModelCountry} is a neighbour of this
	 * {@link ModelCountry}.
	 * 
	 * @param country
	 *            {@link ModelCountry} to check
	 * @return Whether it is a neighbour or not.
	 */
	public boolean isNeighbour(ModelCountry country) {
		return neighbours.contains(country);
	}

	/**
	 * Adds a {@link ModelCountry} that this country is linked to.
	 * 
	 * @param neighbour
	 *            {@link ModelCountry}
	 */
	public void addNeighbour(ModelCountry neighbour) {

		if (neighbour == null) {
			throw new NullPointerException("The neighbour cannot be null");
		}

		neighbours.add(neighbour);

		setChanged();
		notifyObservers(new Update("neighbours", neighbours));
	}

	/**
	 * Performs the end round operation for this {@link ModelCountry}.
	 */
	public void endRound(ModelHazard hazard) {
		
		// Holds whether the hazard has occurred or not.
		boolean occurred = hazard.act(army);

		// If the hazard occurred update the most recent hazard.
		setChanged();
		notifyObservers(new Update("hazard", occurred ? hazard : null));

	}

	/**
	 * Retrieves the {@link Player} the rules <code>this</code>
	 * {@link ModelCountry}.
	 * 
	 * @return {@link ModelCountry#ruler}.
	 */
	public ModelPlayer getRuler() {
		return ruler;
	}

	/**
	 * Retrieves the {@link Player} the rules <code>this</code>
	 * {@link ModelCountry}.
	 * 
	 * @return {@link ModelCountry#ruler}.
	 */
	public Player getOwner() {
		return ruler;
	}

}
