package peril.views.slick.board;

import peril.model.board.ModelContinent;
import peril.model.board.ModelCountry;
import peril.views.slick.Clickable;
import peril.views.slick.Point;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.Region;
import peril.views.slick.SlickModelView;

/**
 * Encapsulates the behaviour of a continent on the {@link SlickBoard}.
 * Continents;<br>
 * <ul>
 * <li>Group {@link SlickCountry}s</li>
 * <li>Apply a special visual effect when all the {@link SlickCountry}s with in
 * it are ruled by the same {@link SlickPlayer}.</li>
 * <li>Award {@link SlickPlayer} with bonuses when they rule all the
 * {@link SlickCountry}s within.</li>
 * </ul>
 * 
 * @author Joshua_Eddy
 *
 */
public final class SlickContinent extends Clickable {

	public final ModelContinent model;

	private final SlickModelView view;

	/**
	 * Constructs a new {@link SlickContinent}.
	 * 
	 * @param countries
	 */
	public SlickContinent(Region region, ModelContinent model, SlickModelView view) {
		super(region);
		this.model = model;
		this.view = view;
	}

	/**
	 * Retrieves the country using a specified {@link Point} by iterating through
	 * all the {@link SlickCountry}s in the {@link SlickContinent} and checks if the
	 * specifies {@link Point} is inside a {@link SlickCountry}.
	 * 
	 * @param click
	 *            {@link Point}.
	 * @return {@link SlickCountry}.
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

}
