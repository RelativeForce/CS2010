package peril.model.board;

import java.util.HashMap;
import java.util.HashSet;
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
 * Encapsulates the behaviour of a country that has a {@link ModelArmy}.
 * 
 * @author Joshua_Eddy, James_Rowntree
 * 
 * @since 2018-03-09
 * @version 1.01.02
 * 
 * @see Observable
 * @see Observer
 * @see Country
 *
 */
public final class ModelCountry extends Observable implements Country, Observer {

	/**
	 * Holds the {@link ModelCountry}s that this {@link ModelCountry} is linked to.
	 * The link between each of the {@link ModelCountry}s is defined by
	 * {@link ModelLink}.
	 */
	private final Map<ModelCountry, ModelLink> neighbours;

	/**
	 * Holds the {@link ModelArmy} at this {@link ModelCountry}.
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
	 * Holds the {@link ModelPlayer} that rules this {@link ModelCountry}.
	 */
	private volatile ModelPlayer ruler;

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
		this.army = new ModelArmy();
		this.name = name;
		this.color = color;

		// Add this country as an observer of the army so that the country knows when it
		// is changed.
		this.army.addObserver(this);

	}

	/**
	 * Performs the end round operation for this {@link ModelCountry}.
	 * 
	 * @param hazard
	 *            The {@link ModelHazard} that should act on this
	 *            {@link ModelCountry}.
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
		if (isNeighbour(country)) {

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
	 * Sets the current ruler of this {@link ModelCountry}.
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
	 * Updates this {@link ModelCountry}.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof ModelArmy || o instanceof ModelLink) {
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Returns the strength of this {@link ModelCountry}'s {@link ModelArmy}.
	 */
	@Override
	public int getArmyStrength() {
		return army.getStrength();
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
	 * Retrieves the name of the {@link ModelCountry}.
	 * 
	 * @return name The name of the {@link ModelCountry}.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the {@link ModelArmy} at this {@link ModelCountry}.
	 * 
	 * @return {@link ModelCountry#army}.
	 */
	@Override
	public ModelArmy getArmy() {
		return army;
	}

	/**
	 * Retrieves the {@link ModelCountry#neighbours}.
	 * 
	 * @return {@link Set} of type {@link ModelCountry}.
	 */
	@Override
	public Set<ModelCountry> getNeighbours() {
		return new HashSet<>(neighbours.keySet());
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
	 * Retrieves the {@link ModelPlayer} the rules <code>this</code>
	 * {@link ModelCountry}.
	 * 
	 * @return {@link ModelCountry#ruler}.
	 */
	public ModelPlayer getRuler() {
		return ruler;
	}

	/**
	 * Retrieves the {@link ModelPlayer} the rules <code>this</code>
	 * {@link ModelCountry}.
	 * 
	 * @return The {@link ModelPlayer} the rules <code>this</code>
	 *         {@link ModelCountry}.
	 */
	@Override
	public Player getOwner() {
		return ruler;
	}

}
