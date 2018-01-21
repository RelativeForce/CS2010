package peril.views.slick.board;

import peril.Game;
import peril.Point;
import peril.model.board.ModelBoard;
import peril.model.board.ModelContinent;
import peril.views.slick.SlickModelView;
import peril.views.slick.Viewable;

/**
 * Encapsulates the behaviour of the the game board in the {@link Game}. This
 * realises {@link Viewable} allowing it to be displayed by the
 * {@link UserInterface}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class SlickBoard extends Viewable{
	
	public final ModelBoard model;
	
	public final SlickModelView view;

	/**
	 * Constructs a {@link SlickBoard}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link SlickBoard} is a part of.
	 * @param name
	 *            The name of the {@link SlickBoard}.
	 */
	public SlickBoard(ModelBoard model, SlickModelView view) {
		super(new Point(0, 0));
		this.model = model;
		this.view = view;
	}

	/**
	 * Retrieves a {@link SlickCountry} that is specified by the parameter {@link Point}.
	 * 
	 * @param click
	 *            {@link Point} on the board.
	 * @return {@link SlickCountry}.
	 */
	public SlickCountry getCountry(Point click) {

		// Iterate through all the continents on the board.
		for (ModelContinent modelContinent : model.getContinents().values()) {

			SlickContinent continent = view.getVisualContinent(modelContinent);
			
			// If the click is inside the continents region get the country from the region.
			if (continent.isClicked(click)) {
				return continent.getCountry(click);
			}
		}

		// If the point is not on the board return null.
		return null;

	}

	/**
	 * Moves this {@link SlickBoard} and all its components along a specified
	 * {@link Point} vector. If the {@link SlickBoard} is at a boundary and this method
	 * is called it will do nothing.
	 * 
	 * @param vector
	 *            {@link Point}
	 * 
	 * @return {@link Point} vector that the {@link SlickBoard} moved.
	 */
	public Point move(Point vector, int containerWidth, int containerHeight) {

		// Null check
		if (vector == null) {
			throw new NullPointerException("Vector cannot be null.");
		}

		// Holds the screen bounds in terms of x and y values.
		int upperYBound = 0;
		int lowerYBound = containerHeight - getHeight();
		int upperXBound = 0;
		int lowerXBound = containerWidth - getWidth();

		// Holds the x and y of the board.
		int currentX = getPosition().x;
		int currentY = getPosition().y;

		// Holds the new valid vector based on whether the board has hit a boundary.
		Point valid = new Point(getValidVector(vector.x, currentX, lowerXBound, upperXBound),
				getValidVector(vector.y, currentY, lowerYBound, upperYBound));

		// If the valid vector specifies movement in x or y
		if (valid.x != 0 || valid.y != 0) {

			// Move the Board along the valid vector.
			this.setPosition(new Point(valid.x + currentX, valid.y + currentY));

			// Iterate through all the continents on the board.
			model.getContinents().values().forEach(modelContinent -> {

				SlickContinent continent = view.getVisualContinent(modelContinent);
				
				// Move the current continent along the valid vector.
				continent.setPosition(
						new Point(continent.getPosition().x + valid.x, continent.getPosition().y + valid.y));

				// Iterate through all the countries in the current continent.
				modelContinent.getCountries().forEach(modelCountry -> {

					SlickCountry country = view.getVisualCountry(modelCountry);
					
					// Move the current country along the valid vector.
					country.setPosition(
							new Point(country.getPosition().x + valid.x, country.getPosition().y + valid.y));

				});
			});

		}

		return valid;

	}

	/**
	 * Retrieves the a valid single dimension vector based on whether the specifed
	 * vector will hit a boundary or not. If the specified vector will move the
	 * board outside of the specified boundaries. The vector returned will be such
	 * that this will not happen.
	 * 
	 * @param vector
	 *            The vector of to move the board in. + or -
	 * @param current
	 *            The current position of the board within the boundaries.
	 * @param lowerBound
	 *            The value of the lower boundary.
	 * @param upperBound
	 *            The value of the upper boundary.
	 * @return The valid vector.
	 */
	private int getValidVector(int vector, int current, int lowerBound, int upperBound) {

		// If the board remains within the boundaries when it is moved along the vector.
		if (current + vector <= upperBound && current + vector >= lowerBound) {
			return vector;
		}
		// Otherwise set the return vector so that is moves the board in to the boundary
		// in the direction of the original vector.
		else {
			if (vector > 0) {
				return upperBound - current;
			} else if (vector < 0) {
				return lowerBound - current;

			}
		}

		return 0;

	}

}
