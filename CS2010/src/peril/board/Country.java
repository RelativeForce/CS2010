package peril.board;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import peril.Point;
import peril.ai.Player;
import peril.ui.Clickable;
import peril.ui.Region;

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
	 * Holds the {@link Image} icon of the {@link EnvironmentalHazard} that has most
	 * recently occurred on the {@link Country}.
	 */
	private Image hazardIcon;

	/**
	 * Holds the {@link Country}(s) that this country is linked to.
	 * 
	 * @see java.util.List
	 */
	private List<Country> neighbours;

	/**
	 * The {@link Color} that denoted this {@link Country} in the map files.
	 */
	private final Color color;

	/**
	 * The {@link Point} offset from the centre of the this {@link Country} that the
	 * {@link Army} will be displayed at.
	 */
	private Point armyOffset;

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
	 * Constructs a new {@link Country} with no army offset.
	 * 
	 * @param name
	 *            of the {@link Country}
	 * @param region
	 *            {@link Region} of the country on screen.
	 * @param color
	 *            The colour that denotes this {@link Country} in the countries
	 *            image.
	 */
	public Country(String name, Region region, Color color) {
		this(name, region, color, new Point(0, 0));
	}

	/**
	 * Constructs a new {@link Country} with a specified army offset.
	 * 
	 * @param name
	 *            of the {@link Country}
	 * @param region
	 *            {@link Region} of the country on screen.
	 * @param color
	 *            The colour that denotes this {@link Country} in the countries
	 *            image.
	 * @param armyOffset
	 *            The {@link Point} offset from this {@link Country}'s center.
	 */
	public Country(String name, Region region, Color color, Point armyOffset) {
		super(region);

		this.neighbours = new LinkedList<Country>();
		this.ruler = null;
		this.army = new Army(1);
		this.name = name;
		this.color = color;
		this.hazardIcon = null;
		this.armyOffset = armyOffset;

	}

	/**
	 * Retrieves the {@link Color} that denoted this {@link Country} in the map
	 * files.
	 * 
	 * @return {@link Color}
	 */
	public Color getColor() {
		return color;
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
			this.setImage(getRegion().getPosition(), getRegion().convert(ruler.color));
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
	 * Retrieves the {@link Point} offset from the centre of the country this army
	 * will be displayed at.
	 * 
	 * @return {@link Point} offset.
	 */
	public Point getArmyOffset() {
		return armyOffset;
	}

	/**
	 * Sets the {@link Point} vector offset of this {@link Army} on screen.
	 * 
	 * @param offset
	 */
	public void setArmyPosition(Point offset) {

		if (offset == null) {
			throw new NullPointerException("Offset cannot be null.");
		}

		this.armyOffset = offset;

	}

	/**
	 * Retrieves the {@link Image} icon of an {@link EnvironmentalHazard} that has
	 * most recently occurred at this {@link Country}.
	 */
	public Image getHazard() {
		return hazardIcon;
	}

	/**
	 * Retrieves whether this {@link Country} has had an {@link EnvironmentalHazard}
	 * occur.
	 * 
	 * @return <code>boolean</code>
	 */
	public boolean hasHazard() {
		return hazardIcon != null;
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
	 * Checks if the {@link Country} is a neighbour of this {@link Country}.
	 * 
	 * @param country
	 *            {@link Country} to check
	 * @return Whether it is a neighbour or not.
	 */
	public boolean isNeighbour(Country country) {
		return neighbours.contains(country);
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
		hazardIcon = hazard.act(army) ? hazard.getIcon() : null;
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
