package peril.views.slick.board;

import java.util.Observable;
import java.util.Observer;

import peril.model.board.ModelContinent;
import peril.model.board.ModelCountry;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;
import peril.views.slick.SlickModelView;

/**
 * The visual representation of a {@link ModelContinent}. This observes the
 * {@link ModelContinent} and is updated when the model changes.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-17
 * @version 1.01.01
 * 
 * @see Observer
 * @see Clickable
 * @see SlickModelView
 *
 */
public final class SlickContinent extends Clickable implements Observer {

	/**
	 * The {@link ModelContinent} that this {@link SlickContinent} displays to the
	 * user.
	 */
	public final ModelContinent model;

	/**
	 * The {@link SlickModelView} that is used to map the {@link ModelContinent} to
	 * the {@link SlickContinent}.
	 */
	private final SlickModelView view;

	/**
	 * Constructs a new {@link SlickContinent}.
	 * 
	 * @param region
	 *            The {@link Region} that represents this continent on screen.
	 * @param model
	 *            The {@link ModelContinent} that this {@link SlickContinent}
	 *            displays to the user.
	 * @param view
	 *            The {@link SlickModelView} that is used to map the
	 *            {@link ModelContinent} to the {@link SlickContinent}.
	 */
	public SlickContinent(Region region, ModelContinent model, SlickModelView view) {
		super(region);

		this.model = model;
		this.view = view;

		model.addObserver(this);
	}

	/**
	 * Retrieves the country using a specified {@link Point} by iterating through
	 * all the {@link SlickCountry}s in the {@link SlickContinent} and checks if the
	 * specifies {@link Point} is inside a {@link SlickCountry}.
	 * 
	 * @param click
	 *            The {@link Point} position of the click.
	 * @return The {@link SlickCountry} that was clicked or null if non were
	 *         clicked.
	 */
	public SlickCountry getCountry(Point click) {

		// Iterates through all the countries in this continent.
		for (ModelCountry modelCountry : model.getCountries()) {

			SlickCountry country = view.getVisual(modelCountry);

			// Checks if the specifies click is inside the bounds of the current country.
			if (country.isClicked(click)) {
				return country;
			}
		}
		// Will return null if the click is not inside a country.
		return null;
	}

	/**
	 * Updates this {@link SlickContinent}.
	 */
	@Override
	public void update(Observable o, Object arg) {
		// Do nothing
	}

}
