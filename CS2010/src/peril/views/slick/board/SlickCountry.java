package peril.views.slick.board;

import java.util.Observable;
import java.util.Observer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import peril.Update;
import peril.model.ModelPlayer;
import peril.model.board.ModelCountry;
import peril.model.board.ModelHazard;
import peril.views.slick.SlickModelView;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

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
	 * The {@link Point} offset from the centre of the this {@link SlickCountry}
	 * that the {@link ModelArmy} will be displayed at.
	 */
	private final Point armyOffset;

	/**
	 * The {@link ModelCountry} that this {@link SlickCountry} displays.
	 */
	public final ModelCountry model;

	/**
	 * The {@link SlickModelView} that is used to map the {@link ModelCountry} to
	 * {@link SlickCountry}.
	 */
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
	public SlickCountry(Region region, ModelCountry model, SlickModelView view) {
		this(region, new Point(0, 0), model, view);
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
	public SlickCountry(Region region, Point armyOffset, ModelCountry model, SlickModelView view) {
		super(region);
		this.hazardIcon = null;
		this.armyOffset = armyOffset;
		this.model = model;
		this.view = view;

		model.addObserver(this);

		if (model.getRuler() == null) {
			changeColour(Color.white);
		} else {
			changeColour(view.getVisual(model.getRuler()).color);
		}

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
	private void updateRuler(Update update) {

		if (update.newValue == null) {
			changeColour(Color.white);
		} else if (update.newValue instanceof ModelPlayer) {
			changeColour(view.getVisual((ModelPlayer) update.newValue).color);
		} else {
			throw new IllegalArgumentException(
					"For ruler update, newValue must be an instance of peril.views.slick.Player");
		}

	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg == null) {
			return;
		}

		if (arg instanceof Update) {

			Update update = (Update) arg;

			switch (update.property) {
			case "ruler":
				updateRuler(update);
				break;
			case "hazard":
				updateHazard(update);
				break;
			case "neighbours":
				break;
			default:
				throw new IllegalArgumentException(
						update.property + " is not a valid propetry of peril.views.slick.board.Country");
			}

		} else {
			throw new IllegalArgumentException("Property must be instance of Update");
		}
	}

	private void updateHazard(Update update) {

		if (update.newValue == null) {
			hazardIcon = null;
		} else if (update.newValue instanceof ModelHazard) {

			SlickHazard h = view.getVisual((ModelHazard) update.newValue);

			if (h != null) {
				hazardIcon = h.getIcon();
			}
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

	/**
	 * Retrieves the {@link Point} position that an {@link ModelArmy} will be
	 * displayed at on the screen relative to the top left corner.
	 * 
	 * @return
	 */
	public Point getArmyPosition() {

		// Sets x and y as the central width and height of the current country.
		int x = getPosition().x + (getWidth() / 2) + getArmyOffset().x;
		int y = getPosition().y + (getHeight() / 2) + getArmyOffset().y;

		return new Point(x, y);
	}

	public Image getHazard() {
		return hazardIcon;
	}

}
