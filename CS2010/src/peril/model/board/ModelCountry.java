package peril.model.board;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import peril.Update;
import peril.controllers.api.Country;
import peril.controllers.api.Player;
import peril.model.ModelColor;
import peril.model.ModelPlayer;
import peril.model.board.links.ModelLink;
import peril.model.board.links.ModelLinkState;

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
public final class ModelCountry extends Observable implements Country, Observer {

	/**
	 * Holds the {@link Player} that rules this {@link ModelCountry}.
	 */
	private ModelPlayer ruler;

	/**
	 * Holds the {@link ModelCountry}(s) that this country is linked to.
	 * 
	 * @see java.util.List
	 */
	private final Map<ModelCountry, ModelLink> neighbours;

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
	 * The {@link ModelColor} of this {@link ModelCountry} in the level file.
	 */
	private final ModelColor color;

	/**
	 * Constructs a new {@link ModelCountry} with a specified army offset.
	 * 
	 * @param name
	 *            of the {@link ModelCountry}
	 * @param color
	 *            The colour that denotes this {@link ModelCountry} in the countries
	 *            image.
	 */
	public ModelCountry(String name, ModelColor color) {

		this.neighbours = new HashMap<ModelCountry, ModelLink>();
		this.ruler = null;
		this.army = new ModelArmy(1);
		this.name = name;
		this.color = color;

		this.army.addObserver(this);

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
	 *            {@link ModelPlayer}
	 */
	public void setRuler(ModelPlayer ruler) {
		this.ruler = ruler;

		setChanged();
		notifyObservers(new Update("ruler", ruler));
	}

	/**
	 * Returns the strength of this {@link ModelCountry}'s {@link ModelArmy}.
	 */
	public int getArmyStrength() {
		return army.getStrength();
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
	 * @return {@link Set} of type {@link ModelCountry}.
	 */
	public Set<ModelCountry> getNeighbours() {
		return neighbours.keySet();
	}

	/**
	 * Retrieves the {@link ModelColor} for this {@link ModelCountry}.
	 * 
	 * @return {@link ModelColor}
	 */
	public ModelColor getColor() {
		return color;
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
		return neighbours.keySet().contains(country);
	}

	/**
	 * Changes the {@link ModelLinkState} of the {@link ModelLink} between this
	 * {@link ModelCountry} and a specified neighbour {@link ModelCountry}.
	 * 
	 * @param country
	 *            {@link ModelCountry}
	 * @param linkState
	 *            {@link ModelLinkState}
	 * @param duration
	 *            Number of rounds before this link will reset back to its default.
	 */
	public void changeLinkTo(ModelCountry country, ModelLinkState linkState, int duration) {

		// If the specified country is a neighbour
		if (!isNeighbour(country)) {

			// Set the new link duration
			neighbours.get(country).setState(linkState, duration);

		} else {
			throw new IllegalArgumentException(country.name + " is not a neighbour of " + name);
		}
	}

	/**
	 * Adds a {@link ModelCountry} that this country is linked to.
	 * 
	 * @param neighbour
	 *            {@link ModelCountry}
	 * @param link
	 *            The {@link ModelLink} between the two {@link ModelCountry}s.
	 */
	public void addNeighbour(ModelCountry neighbour, ModelLink link) {

		if (neighbour == null) {
			throw new NullPointerException("The neighbour cannot be null");
		}

		neighbours.put(neighbour, link);

		link.addObserver(this);

		setChanged();
		notifyObservers(new Update("neighbours", neighbours));
	}

	/**
	 * Retrieves the {@link ModelLink} between this {@link ModelCountry} and the
	 * specified {@link ModelCountry}.
	 * 
	 * @param country
	 *            {@link ModelCountry}
	 * @return {@link ModelLink}
	 */
	public ModelLink getLinkTo(ModelCountry country) {
		return neighbours.get(country);
	}

	/**
	 * Performs the end round operation for this {@link ModelCountry}.
	 */
	public void endRound(ModelHazard hazard) {

		neighbours.forEach((counrty, link) -> link.elapse());

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

	/**
	 * Updates this {@link ModelCountry}.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof ModelArmy || o instanceof ModelLink) {
			setChanged();
			notifyObservers();
		}
	}

}
