package peril.views.slick.board;

import java.util.HashMap;
import java.util.Map;
import peril.model.board.ModelBoard;
import peril.model.board.ModelContinent;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.SlickModelView;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Point;

/**
 * The visual representation of the {@link ModelBoard}.
 * 
 * @author Joshua_Eddy, Gurdeep_Pol
 * 
 * @since 2018-02-26
 * @version 1.01.03
 * 
 * @see Clickable
 * @see ModelBoard
 *
 */
public final class SlickBoard extends Clickable {

	/**
	 * The {@link ModelBoard} that this {@link SlickBoard} displays to the user.
	 */
	public final ModelBoard model;

	/**
	 * The {@link SlickModelView} that is used to map the {@link ModelBoard} to the
	 * {@link SlickBoard}.
	 */
	private final SlickModelView view;

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
	 * @param model
	 *            The {@link ModelBoard} that this {@link SlickBoard} displays to
	 *            the user.
	 * @param view
	 *            The {@link SlickModelView} that is used to map the
	 *            {@link ModelBoard} to the {@link SlickBoard}.
	 */
	public SlickBoard(ModelBoard model, SlickModelView view) {
		super();
		this.model = model;
		this.view = view;
		this.lockedX = false;
		this.lockedY = false;
	}

	/**
	 * Locks the board in the x direction so it can no longer pan left to right;
	 */
	public void lockX() {
		this.lockedX = true;
	}

	/**
	 * Unlocks the board in the x direction so it can pan left to right;
	 */
	public void unlockX() {
		this.lockedX = false;
	}

	/**
	 * Locks the board in the y direction so it can no longer pan up to down;
	 */
	public void lockY() {
		this.lockedY = true;
	}

	/**
	 * Unlocks the board in the y direction so it can pan up to down;
	 */
	public void unlockY() {
		this.lockedY = false;
	}

	/**
	 * Locks the board so it can no longer pan in any direction.
	 */
	public void lock() {
		lockX();
		lockY();
	}

	/**
	 * Unlocks the board so it can pan in any direction.
	 */
	public void unlock() {
		unlockX();
		unlockY();
	}

	/**
	 * Assigns a new {@link Point} position to this {@link SlickBoard} and moves all
	 * the {@link SlickContinent}s and {@link SlickCountry}s within by the change.
	 */
	@Override
	public void setPosition(Point position) {

		final Point old = super.getPosition();

		super.setPosition(position);

		moveComponents(new Point(position.x - old.x, position.y - old.y));
	}

	/**
	 * Draws this {@link SlickBoard} on the specified {@link Frame}.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link SlickBoard} to the
	 *            user.s
	 */
	public void draw(Frame frame) {

		// If the board has a visual representation, render it in the graphics context.
		if (hasImage()) {
			frame.draw(getImage(), getPosition().x, getPosition().y);
		}

		// Holds the hazards that will be drawn on screen.
		final Map<Clickable,SlickHazard > hazards = new HashMap<>();

		// For every country on the board.
		model.forEachCountry(modelCountry -> {

			final SlickCountry country = view.getVisual(modelCountry);

			final int x = country.getPosition().x;
			final int y = country.getPosition().y;

			// Draw the image of the country on top of the board.
			if (country.hasImage()) {
				frame.draw(country.getImage(), x, y);

			}

			// If a hazard has occurred
			if (country.hasHazard()) {

				// Define the hazards visual details
				final SlickHazard hazard = country.getHazard();
				final Clickable icon = country.getHazardIcon();

				final int hazardX = country.getArmyPosition().x + icon.getWidth() + 10;
				final int hazardY = country.getArmyPosition().y - (icon.getHeight() / 2);

				icon.setPosition(new Point(hazardX, hazardY));

				// Add the hazard to the map to be drawn.
				hazards.put(icon,hazard );
			}

		});

		// Draw all the hazards on screen.
		hazards.forEach((icon, hazard )-> {

			frame.draw(icon, new EventListener() {

				@Override
				public void mouseHover(Point mouse, int delta) {
					// Do nothing

				}

				@Override
				public void mouseClick(Point mouse, int mouseButton) {
					
					final String text = hazard.model.name + " - upto " + hazard.model.maxCasualties + "% casualties at " + hazard.model.chance + "% chance";
					
					frame.addToolTip(text, new Point(icon.getPosition().x, icon.getPosition().y - 70), 5000, true);
				}

				@Override
				public void draw(Frame frame) {
					frame.draw(icon.getImage(), icon.getPosition().x, icon.getPosition().y);
				}

				@Override
				public void buttonPress(int key, Point mouse) {
					// Do nothing
				}
			});

		});
	}

	/**
	 * Retrieves a {@link SlickCountry} that is specified by the parameter
	 * {@link Point}.
	 * 
	 * @param click
	 *            The {@link Point} on the {@link SlickBoard}.
	 * @return The {@link SlickCountry} that was clicked if there was one.
	 */
	public SlickCountry getCountry(Point click) {

		// Iterate through all the continents on the board.
		for (ModelContinent modelContinent : model.getContinents().values()) {

			final SlickContinent continent = view.getVisual(modelContinent);

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
	 *            The {@link Point} vector that the {@link SlickBoard} will be
	 *            moved.
	 * @param screenWidth
	 *            The width of the screen.
	 * @param screenHeight
	 *            The height of the screen.
	 * 
	 * @return The {@link Point} vector that the {@link SlickBoard} moved.
	 */
	public Point move(Point vector, int screenWidth, int screenHeight) {

		// Null check
		if (vector == null) {
			throw new NullPointerException("Vector cannot be null.");
		}

		// Holds the screen bounds in terms of x and y values.
		final int upperXBound = 0;
		final int lowerXBound = screenWidth - getWidth();
		final int upperYBound = 0;
		final int lowerYBound = screenHeight - getHeight();

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
	 * Moves all the {@link SlickContinent}s and {@link SlickCountry}s on this
	 * {@link SlickBoard} by the specified {@link Point} vector.
	 * 
	 * @param vector
	 *            The {@link Point} that the {@link SlickBoard} was moved by.
	 */
	private void moveComponents(Point vector) {

		// Iterate through all the continents on the board.
		model.getContinents().values().forEach(modelContinent -> {

			final SlickContinent continent = view.getVisual(modelContinent);

			final int continentX = continent.getPosition().x + vector.x;
			final int continentY = continent.getPosition().y + vector.y;

			// Move the current continent along the valid vector.
			continent.setPosition(new Point(continentX, continentY));

			// Iterate through all the countries in the current continent.
			modelContinent.getCountries().forEach(modelCountry -> {

				final SlickCountry country = view.getVisual(modelCountry);

				final int countryX = country.getPosition().x + vector.x;
				final int countryY = country.getPosition().y + vector.y;

				// Move the current country along the valid vector.
				country.setPosition(new Point(countryX, countryY));

			});
		});

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
