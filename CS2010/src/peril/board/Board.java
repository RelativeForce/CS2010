package peril.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import peril.Game;
import peril.Point;
import peril.ui.components.Viewable;

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
	 * The number of {@link Country}s in the {@link Board}.
	 */
	private int numberOfCountries;

	/**
	 * Holds the name of this {@link Borad}.
	 */
	private String name;

	/**
	 * Constructs a {@link Board}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link Board} is a part of.
	 * @param name
	 *            The name of the {@link Board}.
	 */
	public Board(Game game, String name) {

		if (game == null) {
			throw new NullPointerException("Continents is null.");
		}
		this.continents = null;
		this.game = game;
		this.numberOfCountries = 0;
		this.name = name;
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

		for (Continent continent : continents) {
			numberOfCountries += continent.getCountries().size();
		}

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
	 * 
	 * @return {@link Point} vector that the {@link Board} moved.
	 */
	public Point move(Point vector) {

		// Null check
		if (vector == null) {
			throw new NullPointerException("Vector cannot be null.");
		}

		// Holds the screen bounds in terms of x and y values.
		int upperYBound = 0;
		int lowerYBound = game.getContainer().getHeight() - getHeight();
		int upperXBound = 0;
		int lowerXBound = game.getContainer().getWidth() - getWidth();

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
			continents.forEach(continent -> {

				// Move the current continent along the valid vector.
				continent.setPosition(
						new Point(continent.getPosition().x + valid.x, continent.getPosition().y + valid.y));

				// Iterate through all the countries in the current continent.
				continent.getCountries().forEach(country -> {

					// Move the current country along the valid vector.
					country.setPosition(
							new Point(country.getPosition().x + valid.x, country.getPosition().y + valid.y));

				});
			});

		}

		return valid;

	}

	/**
	 * Retrieves the number of {@link Country}s on this {@link Board}.
	 * 
	 * @return <code>int</code>
	 */
	public int getNumberOfCountries() {
		return numberOfCountries;
	}

	/**
	 * Draws this {@link Board} on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	public void draw(Graphics g) {

		// If the board has a visual representation, render it in the graphics context.
		if (hasImage()) {
			g.drawImage(getImage(), getPosition().x, getPosition().y);
		}

		// Holds the hazards that will be drawn on screen.
		Map<Point, Image> hazards = new HashMap<>();

		// For every country on the board.
		getContinents().forEach(continent -> continent.getCountries().forEach(country -> {

			// Holds the image of the country
			Image image = country.getImage();

			int x = country.getPosition().x;
			int y = country.getPosition().y;

			// Draw the image of the country on top of the board.
			if (image != null) {
				g.drawImage(image, x, y);
			}

			// If a hazard has occurred
			if (country.hasHazard()) {

				// Define the hazards visual details
				Image hazard = country.getHazard();
				hazard = hazard.getScaledCopy(30, 30);
				
				int hazardX = x + (country.getWidth() / 2) + (hazard.getWidth() / 2) + country.getArmy().getOffset().x;
				int hazardY = y + (country.getHeight() / 2) - hazard.getHeight() + country.getArmy().getOffset().y;

				// Add the hazard to the map to be drawn.
				hazards.put(new Point(hazardX, hazardY), hazard);
			}
		}));

		// Draw all the hazards on screen.
		hazards.forEach((position, hazardIcon) -> {
			g.drawImage(hazardIcon, position.x, position.y);
		});

	}

	/**
	 * Retrieves the name of this {@link Board}.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Performs a {@link Consumer} function on each {@link Country} on the
	 * {@link Board}.
	 * 
	 * @param task
	 *            {@link Consumer}
	 */
	public void forEachCountry(Consumer<Country> task) {
		continents.forEach(continent -> continent.getCountries().forEach(task));
	}

	/**
	 * Set the name of this {@link Board}.
	 * 
	 * @param name
	 *            name of the {@link Board}.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Clear all the {@link Continent}s from the {@link Game} thus deleting all the
	 * {@link Country}s and reseting the {@link Board} to its initial state.
	 */
	public void reset() {

		if (continents != null) {
			continents.clear();
		}
		numberOfCountries = 0;
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

	/**
	 * Checks whether {@link Board#continents} is <code>null</code>. If it is this
	 * method will throw {@link IllegalStateException}.
	 */
	private void checkContinents() {
		if (continents == null) {
			throw new IllegalStateException("There are no Continents on this board");
		}
	}

}
