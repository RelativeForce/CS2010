package peril.views.slick.board;

import java.util.Observable;
import java.util.Observer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import peril.Update;
import peril.model.ModelPlayer;
import peril.model.board.ModelCountry;
import peril.model.board.ModelHazard;
import peril.model.board.ModelArmy;
import peril.views.slick.SlickModelView;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * The visual representation of a {@link ModelCountry} which observes an
 * aggregated {@link ModelCountry} and is updated when the model version is.
 * 
 * @author Joshua_Eddy, James_Rowntree
 * 
 * @since 2018-02-19
 * @version 1.01.02
 * 
 * @see Clickable
 * @see Observer
 * @see ModelCountry
 *
 */
public final class SlickCountry extends Clickable implements Observer {

	/**
	 * The {@link ModelCountry} that this {@link SlickCountry} displays.
	 */
	public final ModelCountry model;

	/**
	 * The {@link Point} offset from the centre of the this {@link SlickCountry}
	 * that the {@link ModelArmy} will be displayed at.
	 */
	private final Point armyOffset;

	/**
	 * The {@link SlickModelView} that is used to map the {@link ModelCountry} to
	 * {@link SlickCountry}.
	 */
	private final SlickModelView view;

	/**
	 * Holds the {@link Clickable} icon of the {@link SlickHazard} that has most
	 * recently occurred on the {@link SlickCountry}.
	 */
	private Clickable hazardIcon;

	/**
	 * Holds the {@link SlickHazard} that has most recently occurred on the
	 * {@link SlickCountry}.
	 */
	private SlickHazard hazard;

	/**
	 * Constructs a new {@link SlickCountry} with no army offset.
	 * 
	 * @param region
	 *            The {@link Region} of the country on screen.
	 * @param model
	 *            The {@link ModelCountry} that this {@link SlickCountry} displays.
	 * @param view
	 *            The {@link SlickModelView} that is used to map the
	 *            {@link ModelCountry} to {@link SlickCountry}.
	 * 
	 */
	public SlickCountry(Region region, ModelCountry model, SlickModelView view) {
		this(region, new Point(0, 0), model, view);
	}

	/**
	 * Constructs a new {@link SlickCountry} with a specified army offset.
	 * 
	 * @param region
	 *            The {@link Region} of the country on screen.
	 * 
	 * @param armyOffset
	 *            The {@link Point} offset from this {@link SlickCountry}'s centre.
	 * @param model
	 *            The {@link ModelCountry} that this {@link SlickCountry} displays.
	 * @param view
	 *            The {@link SlickModelView} that is used to map the
	 *            {@link ModelCountry} to {@link SlickCountry}.
	 */
	public SlickCountry(Region region, Point armyOffset, ModelCountry model, SlickModelView view) {
		super(region);

		this.armyOffset = armyOffset;
		this.model = model;
		this.view = view;
		this.hazardIcon = null;
		this.hazard = null;

		model.addObserver(this);

		changeColour(model.getRuler() == null ? Color.white : view.getVisual(model.getRuler()).color);

	}

	/**
	 * Changes the {@link Color} of this {@link SlickCountry}.
	 * 
	 * @param newColor
	 *            The new {@link Color}.
	 */
	public void changeColour(Color newColor) {
		this.swapImage(getRegion().convert(newColor));
	}

	/**
	 * Updates this {@link SlickCountry} from the {@link ModelCountry}.
	 */
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

	/**
	 * Retrieves the {@link Point} vector of the {@link SlickArmy} from the center
	 * of the {@link SlickCountry}.
	 * 
	 * @return The {@link Point} vector of the {@link SlickArmy}.
	 */
	public Point getArmyOffset() {
		return armyOffset;
	}

	/**
	 * Retrieves the {@link Point} position that an {@link ModelArmy} will be
	 * displayed at on the screen relative to the top left corner.
	 * 
	 * @return The army's {@link Point} position.
	 */
	public Point getArmyPosition() {

		// Sets x and y as the central width and height of the current country.
		int x = getPosition().x + (getWidth() / 2) + getArmyOffset().x;
		int y = getPosition().y + (getHeight() / 2) + getArmyOffset().y;

		return new Point(x, y);
	}

	/**
	 * Retrieves the {@link SlickHazard} on this {@link SlickCountry}.
	 * 
	 * @return The {@link SlickHazard}.
	 */
	public SlickHazard getHazard() {
		return hazard;
	}

	/**
	 * Retrieves the hazard icon over the {@link SlickHazard}.
	 * 
	 * @return The hazard icon {@link Clickable}.
	 */
	public Clickable getHazardIcon() {
		return hazardIcon;
	}

	/**
	 * Retrieves whether there current is {@link Image} icon over the
	 * {@link SlickCountry}.
	 * 
	 * @return Whether or not there current is {@link Image} icon over the
	 *         {@link SlickCountry}.
	 */
	public boolean hasHazard() {
		return hazardIcon != null;
	}

	/**
	 * Updates the current {@link SlickHazard} that is displayed over this
	 * {@link SlickCountry}.
	 * 
	 * @param update
	 *            The {@link Update} that specifies the new {@link SlickHazard}.
	 */
	private void updateHazard(Update update) {

		if (update.newValue == null) {
			hazardIcon = null;
		} else if (update.newValue instanceof ModelHazard) {

			final SlickHazard hazard = view.getVisual((ModelHazard) update.newValue);

			if (hazard != null) {
				this.hazardIcon = new Clickable(hazard.getIcon());
				this.hazard = hazard;
			}
		} else {
			throw new IllegalArgumentException("For hazard update, newValue must be an Image.");
		}

	}

	/**
	 * Updates the current {@link SlickPlayer} of this {@link SlickCountry}.
	 * 
	 * @param update
	 *            The {@link Update} that specifies the new ruler.
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

}
