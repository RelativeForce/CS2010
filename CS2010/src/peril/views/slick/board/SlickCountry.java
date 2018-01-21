package peril.views.slick.board;

import java.util.Observable;
import java.util.Observer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import peril.Point;
import peril.Update;
import peril.model.board.ModelCountry;
import peril.views.slick.Clickable;
import peril.views.slick.Region;
import peril.views.slick.SlickModelView;

/**
 * Encapsulates the behaviour of a Country. Countries:
 * <ul>
 * <li>Have and manage {@link ModelArmy}s.</li>
 * <li>Have a name.</li>
 * <li>Depend of the countries they are linked to.</li>
 * <li>Have a {@link SlickPlayer} that rules them.</li>
 * </ul>
 * 
 * @author Joshua_Eddy, James_Rowntree
 * 
 * @see java.util.LinkedList
 * @see Java.util.List
 *
 */
public class SlickCountry extends Clickable implements Observer {

	/**
	 * Holds the {@link Image} icon of the {@link SlickHazard} that has most
	 * recently occurred on the {@link SlickCountry}.
	 */
	private Image hazardIcon;

	/**
	 * The {@link Color} that denoted this {@link SlickCountry} in the map files.
	 */
	private final Color color;

	/**
	 * The {@link Point} offset from the centre of the this {@link SlickCountry}
	 * that the {@link ModelArmy} will be displayed at.
	 */
	private Point armyOffset;

	public final ModelCountry model;

	private final SlickModelView view;

	/**
	 * Constructs a new {@link SlickCountry} with no army offset.
	 * 
	 * @param name
	 *            of the {@link SlickCountry}
	 * @param region
	 *            {@link Region} of the country on screen.
	 * @param color
	 *            The colour that denotes this {@link SlickCountry} in the countries
	 *            image.
	 */
	public SlickCountry(Region region, Color color, ModelCountry model, SlickModelView view) {
		this(region, color, new Point(0, 0), model, view);
	}

	/**
	 * Constructs a new {@link SlickCountry} with a specified army offset.
	 * 
	 * @param name
	 *            of the {@link SlickCountry}
	 * @param region
	 *            {@link Region} of the country on screen.
	 * @param color
	 *            The colour that denotes this {@link SlickCountry} in the countries
	 *            image.
	 * @param armyOffset
	 *            The {@link Point} offset from this {@link SlickCountry}'s center.
	 */
	public SlickCountry(Region region, Color color, Point armyOffset, ModelCountry model, SlickModelView view) {
		super(region);
		this.color = color;
		this.hazardIcon = null;
		this.armyOffset = armyOffset;
		this.model = model;
		this.view = view;
	}

	public void changeColour(Color newColor) {
		this.replaceImage(getRegion().convert(newColor));
	}

	/**
	 * Sets the current {@link Ruler} of this {@link SlickCountry}.
	 * 
	 * @param ruler
	 *            {@link SlickPlayer}
	 * 
	 */
	private void updateColour(Update<?> update) {

		if (update.newValue == null) {
			changeColour(Color.white);
		} else if (update.newValue instanceof SlickPlayer) {
			changeColour(((SlickPlayer) update.newValue).color);
		} else {
			throw new IllegalArgumentException(
					"For ruler update, newValue must be an instance of peril.views.slick.Player");
		}

	}

	/**
	 * Sets the {@link Point} vector offset of this {@link ModelArmy} on screen.
	 * 
	 * @param offset
	 */
	public void setArmyPosition(Point offset) {

		if (offset == null) {
			throw new NullPointerException("Offset cannot be null.");
		}

		this.armyOffset = offset;

	}

	public void draw(Graphics g) {

	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Update) {

			Update<?> update = (Update<?>) arg;

			switch (update.property) {
			case "ruler":
				updateColour(update);
				break;
			case "hazard":
				updateHazard(update);
				break;
			default:
				throw new IllegalArgumentException(
						update.property + " is not a valid propetry of peril.views.slick.board.Country");
			}

		} else {
			throw new IllegalArgumentException("Property must be instance of Update");
		}
	}

	private void updateHazard(Update<?> update) {

		if (update.newValue == null) {
			hazardIcon = null;
		} else if (update.newValue instanceof SlickPlayer) {
			hazardIcon = (Image) update.newValue;
		} else {
			throw new IllegalArgumentException("For hazard update, newValue must be an Image.");
		}

	}

	public boolean hasHazard() {
		return hazardIcon != null;
	}
	
	public Point getArmyOffset() {
		return armyOffset;
	}

	public Image getHazard() {
		return hazardIcon;
	}

}
