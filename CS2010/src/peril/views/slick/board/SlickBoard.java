package peril.views.slick.board;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import peril.Game;
import peril.controllers.GameController;
import peril.model.board.ModelBoard;
import peril.model.board.ModelContinent;
import peril.model.states.ModelState;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.SlickGame;
import peril.views.slick.SlickModelView;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Point;
import peril.views.slick.util.Viewable;

/**
 * Encapsulates the behaviour of the the game board in the {@link Game}. This
 * realises {@link Viewable} allowing it to be displayed by the
 * {@link UserInterface}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class SlickBoard extends Clickable {

	public final ModelBoard model;

	public final SlickModelView view;

	/**
	 * Whether or not this {@link SlickBoard} is locked in the x direction when
	 * using {@link SlickBoard#move(Point, int, int)}.
	 */
	private boolean lockedX;

	/**
	 * Whether or not this {@link SlickBoard} is locked in the y direction when
	 * using {@link SlickBoard#move(Point, int, int)}.
	 */
	private boolean lockedY;

	/**
	 * Constructs a {@link SlickBoard}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link SlickBoard} is a part of.
	 * @param name
	 *            The name of the {@link SlickBoard}.
	 */
	public SlickBoard(ModelBoard model, SlickModelView view) {
		super();
		this.model = model;
		this.view = view;
		this.lockedX = false;
		this.lockedY = false;
	}

	public void lockX() {
		this.lockedX = true;
	}

	public void unlockX() {
		this.lockedX = false;
	}

	public void lockY() {
		this.lockedY = true;
	}

	public void unlockY() {
		this.lockedY = false;
	}

	public void unlock() {
		this.lockedX = false;
		this.lockedY = false;
	}

	public void lock() {
		this.lockedX = true;
		this.lockedY = true;
	}

	public void draw(Frame frame, GameController game, SlickGame slick, ModelState state) {
		SlickBoard board = slick.modelView.getVisual(game.getModelBoard());

		// If the board has a visual representation, render it in the graphics context.
		if (board.hasImage()) {
			frame.draw(board.getImage(), board.getPosition().x, board.getPosition().y);
		}

		// Holds the hazards that will be drawn on screen.
		Map<Point, Image> hazards = new HashMap<>();

		// For every country on the board.
		board.model.forEachCountry(modelCountry -> {

			SlickCountry country = slick.modelView.getVisual(modelCountry);

			final int x = country.getPosition().x;
			final int y = country.getPosition().y;

			// Draw the image of the country on top of the board.
			if (country.hasImage()) {
				frame.draw(country.getImage(), x, y);
				
			}

			// If a hazard has occurred
			if (country.hasHazard()) {

				// Define the hazards visual details
				Image hazard = country.getHazard();
				hazard = hazard.getScaledCopy(60, 60);

				final int hazardX = x + (country.getWidth() / 2) + (hazard.getWidth() / 2) + country.getArmyOffset().x;
				final int hazardY = y + (country.getHeight() / 2) - hazard.getHeight() + country.getArmyOffset().y;

				// Add the hazard to the map to be drawn.
				hazards.put(new Point(hazardX, hazardY), hazard);
			}

		});

		// Draw all the hazards on screen.
		hazards.forEach((position, hazardIcon) -> {
			frame.draw(hazardIcon, position.x, position.y);
		});
	}
	
	/**
	 * Retrieves a {@link SlickCountry} that is specified by the parameter
	 * {@link Point}.
	 * 
	 * @param click
	 *            {@link Point} on the board.
	 * @return {@link SlickCountry}.
	 */
	public SlickCountry getCountry(Point click) {

		// Iterate through all the continents on the board.
		for (ModelContinent modelContinent : model.getContinents().values()) {

			SlickContinent continent = view.getVisual(modelContinent);

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
	 * {@link Point} vector. If the {@link SlickBoard} is at a boundary and this
	 * method is called it will do nothing.
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
		final int upperXBound = 0;
		final int lowerXBound = containerWidth - getWidth();
		final int upperYBound = 0;
		final int lowerYBound = containerHeight - getHeight();

		// Holds the x and y of the board.
		final int currentX = getPosition().x;
		final int currentY = getPosition().y;

		/*
		 * If the board is to small to fill the display then don't move it Otherwise get
		 * the valid vector.
		 */
		final int x = lockedX ? 0 : getValidVector(vector.x, currentX, lowerXBound, upperXBound);
		final int y = lockedY ? 0 : getValidVector(vector.y, currentY, lowerYBound, upperYBound);

		// Holds the new valid vector based on whether the board has hit a boundary.
		final Point valid = new Point(x, y);

		// If the valid vector specifies movement in x or y
		if (valid.x != 0 || valid.y != 0) {

			// Move the Board along the valid vector.
			setPosition(new Point(valid.x + currentX, valid.y + currentY));

		}

		return valid;

	}

	/**
	 * Assigns a new {@link Point} position to this {@link SlickBoard} and moves all
	 * the {@link SlickContinent}s and {@link SlickCountry}s within by the change.
	 * The {@link SlickBoard} will not be bound on screen and may cause
	 */
	@Override
	public void setPosition(Point position) {

		final Point old = super.getPosition();

		super.setPosition(position);

		moveComponents(new Point(position.x - old.x, position.y - old.y));
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

	private void moveComponents(Point vector) {

		// Iterate through all the continents on the board.
		model.getContinents().values().forEach(modelContinent -> {

			final SlickContinent continent = view.getVisual(modelContinent);

			// Move the current continent along the valid vector.
			continent
					.setPosition(new Point(continent.getPosition().x + vector.x, continent.getPosition().y + vector.y));

			// Iterate through all the countries in the current continent.
			modelContinent.getCountries().forEach(modelCountry -> {

				final SlickCountry country = view.getVisual(modelCountry);

				// Move the current country along the valid vector.
				country.setPosition(new Point(country.getPosition().x + vector.x, country.getPosition().y + vector.y));

			});
		});

	}
}
