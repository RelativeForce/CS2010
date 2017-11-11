package peril.board;

import java.util.List;

import peril.Game;
import peril.Point;
import peril.ui.visual.Viewable;

/**
 * Encapsulates the behaviour of the the game board in the {@link Game}. This
 * realises {@link Viewable} allowing it to be displayed by the
 * {@link UserInterface}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class Board extends Viewable {

	/**
	 * The {@link Continent}s in this {@link Board}.
	 */
	private List<Continent> continents;

	/**
	 * The {@link Game} this {@link Board} is a part of.
	 */
	private final Game game;

	/**
	 * Constructs a {@link Board}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link Board} is a part of.
	 */
	public Board(Game game) {

		if (game == null) {
			throw new NullPointerException("Continents is null.");
		}
		this.continents = null;
		this.game = game;
	}

	/**
	 * Sets this {@link Board}'s {@link List} of {@link Continent}s.
	 * 
	 * @param continents
	 *            new {@link List} of {@link Continent}s.
	 */
	public void setContinents(List<Continent> continents) {

		if (continents == null) {
			throw new NullPointerException("Continents is null.");
		}

		this.continents = continents;
	}

	/**
	 * Retrieves a {@link Country} that is specified by the parameter {@link Point}.
	 * 
	 * @param click
	 *            {@link Point} on the board.
	 * @return {@link Country}.
	 */
	public Country getCountry(Point click) {

		checkContinents();

		// Iterate through all the continents on the board.
		for (Continent continent : continents) {

			// If the click is inside the continents region get the country from the region.
			if (continent.isClicked(click)) {
				return continent.getCountry(click);
			}
		}

		// If the point is not on the board return null.
		return null;

	}

	/**
	 * Retrieves the {@link Continent}s on this {@link Board}.
	 * 
	 * @return {@link List} of {@link Continent}s.
	 * 
	 * @see java.util.List
	 */
	public List<Continent> getContinents() {

		checkContinents();

		return continents;
	}

	/**
	 * Checks whether {@link Board#continents} is <code>null</code>. If it is this
	 * method will throw {@link IllegalStateException}.
	 */
	private void checkContinents() {
		if (continents == null) {
			throw new IllegalStateException("There are no Continents on this board");
		}
	}

	/**
	 * Iterates through each {@link Continent} in the {@link Board} and
	 * {@link Continent#executeTurn()}.
	 */
	public void endRound() {

		checkContinents();

		continents.forEach(continent -> continent.endRound());
	}

	/**
	 * Moves this {@link Board} and all its components along a specified
	 * {@link Point} vector. If the {@link Board} is at a boundary and this method
	 * is called it will do nothing.
	 * 
	 * @param vector
	 *            {@link Point}
	 */
	public void move(Point vector) {

		// Param check
		if (vector == null) {
			throw new NullPointerException("Vector cannot be null.");
		}

		// Hols the screen bounds in terms of x and y values.
		int upperYBound = 0;
		int lowerYBound = game.getContainer().getHeight() - getHeight();
		int upperXBound = 0;
		int lowerXBound = game.getContainer().getWidth() - getWidth();

		// Holds the x and y of the board.
		int currentX = getPosition().x;
		int currentY = getPosition().y;

		// Holds the new valid vector based on whether the board has hit a boundary.
		int validX = getValidVector(vector.x, currentX, lowerXBound, upperXBound);
		int validY = getValidVector(vector.y, currentY, lowerYBound, upperYBound);

		// If the valid vector specifies movement in x or y
		if (validX != 0 || validY != 0) {

			// Move the Board along the valid vector.
			setPosition(new Point(validX + currentX, validY + currentY));

			// Iterate through all the continents on the board.
			continents.forEach(continent -> {

				// Move the current continent along the valid vector.
				continent
						.setPosition(new Point(continent.getPosition().x + validX, continent.getPosition().y + validY));

				// Iterate through all the countries in the current continent.
				continent.getCountries().forEach(country -> {

					// Move the current country along the valid vector.
					country.setPosition(new Point(country.getPosition().x + validX, country.getPosition().y + validY));

				});
			});

		}

		// Do nothing

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
