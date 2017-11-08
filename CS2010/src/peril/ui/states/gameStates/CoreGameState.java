package peril.ui.states.gameStates;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.board.Army;
import peril.board.Board;
import peril.board.Country;
import peril.ui.states.InteractiveState;
import peril.ui.visual.Region;

/**
 * A {@link InteractiveState} which displays the {@link Board} from the
 * {@link Game} to the user.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 */
public abstract class CoreGameState extends InteractiveState {

	/**
	 * The current {@link Country} that the player has highlighted.
	 */
	private Country highlightedCountry;

	/**
	 * Constructs a new {@link CoreGameState}.
	 * 
	 * @param game
	 *            The {@link Game} this state is a part of.
	 * @param stateName
	 *            Holds the name of a specific {@link CoreGameState}.
	 */
	protected CoreGameState(Game game, String stateName) {
		super(game, stateName);
		this.highlightedCountry = null;
	}

	/**
	 * Set the current {@link Country} that the player has highlighted.
	 */
	public void highlightCountry(Country country) {

		highlightedCountry = country;

		if (highlightedCountry != null) {
			// Highlight the country
			highlightedCountry.setImage(highlightedCountry.getRegion().getPosition(),
					highlightedCountry.getRegion().convert(Color.yellow));
		}

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		// If the board has a visual representation, render it in the graphics context.
		if (getGame().getBoard().hasImage()) {
			g.drawImage(getGame().getBoard().getImage(), getGame().getBoard().getPosition().x,
					getGame().getBoard().getPosition().y);
		}
		// For every country on the board.
		getGame().getBoard().getContinents().forEach(continent -> continent.getCountries().forEach(country -> {

			// Holds the image of the country
			Image image = country.getImage();

			// Draw the image of the country on top of the board.
			if (image != null) {
				g.drawImage(image, country.getPosition().x, country.getPosition().y);
			}
		}));

		// If there is a highlight country
		if (highlightedCountry != null) {

			// Holds the highlight image
			Image highlightImage = highlightedCountry.getImage();

			// If there is an image in the highlighted country
			if (highlightImage != null) {
				g.drawImage(highlightImage, highlightedCountry.getPosition().x, highlightedCountry.getPosition().y);
			}
		}

		// Draw all the clickable objects.
		super.render(gc, sbg, g);

		drawStateBox(g);

		drawArmies(g);
	}

	/**
	 * Returns a the current highlighted {@link Country} in this state.
	 * 
	 */
	public Country getHighlightedCountry() {
		return highlightedCountry;
	}

	/**
	 * Removes the highlight colouring effect on
	 * {@link CoreGameState#highlightedCountry}.
	 * 
	 * @param country
	 *            {@link Country} to unhighlight.
	 */
	public void unhighlightCountry(Country country) {

		// If there is a highlighted country
		if (country != null) {

			// Holds the position of the country
			Point position = country.getRegion().getPosition();

			// Holds the region of the country
			Region region = country.getRegion();

			// Holds the ruler of the country
			Player ruler = country.getRuler();

			// If there is a ruler then return the colour of the country to that of the
			// ruler. Otherwise remove the highlight effect.
			if (ruler != null) {
				country.setImage(position, region.convert(ruler.getColor()));
			} else {
				country.setImage(null, null);
			}
		}

	}

	/**
	 * Simulates a click at a {@link Point} on the {@link Board} and highlights the
	 * {@link Country} that clicked.
	 * 
	 * @param click
	 *            {@link Point}
	 */
	protected void clickBoard(Point click) {

		// Holds the game board
		Board board = getGame().getBoard();

		// If there is a game board
		if (board != null) {

			// Get the country that is clicked.
			highlightCountry(board.getCountry(click));

		}
	}

	/**
	 * Draws all the links between all the {@link Country}s.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	protected void drawAllLinks(Graphics g) {

		// Get all the countries from the board.
		getGame().getBoard().getContinents().forEach(continent -> continent.getCountries().forEach(country -> {

			Army army = country.getArmy();

			// Sets x and y as the central width and height of the current country.
			final int countryX = country.getPosition().x + (country.getWidth() / 2) + army.getOffset().x;
			final int countryY = country.getPosition().y + (country.getHeight() / 2) + army.getOffset().y;

			// For each neighbour of that country draw the link from the neighbour to the
			// current country
			country.getNeighbours().forEach(neighbour -> {

				Army neighbourArmy = neighbour.getArmy();

				// Sets x and y as the central width and height of the neighbour country.
				final int neighbourX = neighbour.getPosition().x + (neighbour.getWidth() / 2)
						+ neighbourArmy.getOffset().x;
				final int neighbourY = neighbour.getPosition().y + (neighbour.getHeight() / 2)
						+ neighbourArmy.getOffset().y;

				// Draw the line from the country to the neighbour
				g.drawLine(countryX, countryY, neighbourX, neighbourY);
			});
		}));

	}

	/**
	 * Draws the {@link army} in its current state over the {@link Country} it is
	 * located.
	 * 
	 * @param g
	 *            A graphics context that can be used to render primitives to the
	 *            accelerated canvas provided by LWJGL.
	 */
	private void drawArmies(Graphics g) {

		// Iterate across every country on the game board.
		getGame().getBoard().getContinents().forEach(continent -> continent.getCountries().forEach(country -> {

			// Sets x and y as the central width and height of the current country.
			int x = country.getPosition().x + country.getWidth() / 2;
			int y = country.getPosition().y + country.getHeight() / 2;

			Army army = country.getArmy();

			// Move the (x,y) by the offset
			x += army.getOffset().x;
			y += army.getOffset().y;

			// Draw a background oval with the rulers colour. If no ruler found default to
			// light grey.
			if (country.getRuler() != null) {
				g.setColor(country.getRuler().getColor());
			} else {
				g.setColor(Color.lightGray);
			}

			int troopNumber = army.getSize();

			if (troopNumber > 99) {
				g.fillOval(x - 9, y - 3, 45, 25);
			} else if (troopNumber > 9) {
				g.fillOval(x - 6, y - 3, 30, 25);
			} else {
				g.fillOval(x - 3, y - 3, 15, 25);
			}

			g.setColor(Color.black);

			// Draw a string representing the number of troops
			// within that army at (x,y).

			g.drawString(Integer.toString(troopNumber), x, y);

		}));
	}

	/**
	 * Draws the {@link Player} and {@link CoreGameState#stateName} on the screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawStateBox(Graphics g) {

		// Draw the background box
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, 130, 60);

		// Draw state name
		g.setColor(Color.black);
		g.drawString(getStateName(), 5, 5);

	}

}
