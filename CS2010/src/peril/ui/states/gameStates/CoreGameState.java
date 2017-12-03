package peril.ui.states.gameStates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.board.Army;
import peril.board.Board;
import peril.board.Country;
import peril.ui.Region;
import peril.ui.components.lists.ToolTipList;
import peril.ui.components.menus.HelpMenu;
import peril.ui.components.menus.PauseMenu;
import peril.ui.states.InteractiveState;

/**
 * A {@link InteractiveState} which displays the {@link Board} from the
 * {@link Game} to the user.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 */
public abstract class CoreGameState extends InteractiveState {

	/**
	 * The {@link Point} pan direction vector this state will pan at.
	 */
	private Point panDirection;

	/**
	 * The current {@link Country} that the player has highlighted.
	 */
	private Country highlightedCountry;

	/**
	 * Holds the tool tip that will be displayed to the user.
	 */
	private ToolTipList toolTipList;

	/**
	 * The {@link PauseMenu} for this {@link CoreGameState}.
	 */
	private PauseMenu pauseMenu;

	/**
	 * The {@link Music} that is played in the background of the
	 * {@link CoreGameState}s.
	 */
	private List<Music> backgroundMusic;

	/**
	 * Constructs a new {@link CoreGameState}.
	 * 
	 * @param game
	 *            The {@link Game} this state is a part of.
	 * @param stateName
	 *            Holds the name of a specific {@link CoreGameState}.
	 * @param id
	 *            The id of this {@link CoreGameState}.
	 */
	protected CoreGameState(Game game, String stateName, int id) {
		super(game, stateName, id);
		this.highlightedCountry = null;

		this.pauseMenu = game.menus.pauseMenu;
		this.panDirection = null;
		this.toolTipList = new ToolTipList(new Point(130, 15));

		super.addComponent(pauseMenu);
		super.addComponent(toolTipList);
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

	/**
	 * Enters this {@link CoreGameState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {

		// If the music has been turned off. Turn it on and play the current states
		// music.
		if (!gc.isMusicOn()) {

			gc.setMusicOn(true);

			// If there is music play it.
			if (getMusic() != null) {
				getMusic().play();
			}
		}
	}

	/**
	 * Renders this {@link CoreGameState}.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		getGame().board.draw(g);

		// If there is a highlight country
		if (highlightedCountry != null) {

			// Holds the highlight image
			Image highlightImage = highlightedCountry.getImage();

			// If there is an image in the highlighted country
			if (highlightImage != null) {
				g.drawImage(highlightImage, highlightedCountry.getPosition().x, highlightedCountry.getPosition().y);
			}
		}

		toolTipList.draw(g);

	}

	/**
	 * Updates this {@link CoreGameState} between frames.
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		super.update(gc, sbg, delta);
		toolTipList.elapseTime(delta);

		if (panDirection != null && !pauseMenu.isVisible()) {
			pan(panDirection);
		}

		// Hide the about widow if the pause menu is visible
		if (pauseMenu.isVisible() && getHelp() != null) {
			getHelp().hide();
		}

	}

	/**
	 * Initialises the visual elements of this {@link CoreGameState}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		int numberOfSongs = 3;

		backgroundMusic = new ArrayList<>(numberOfSongs);

		for (int songIndex = 1; songIndex <= numberOfSongs; songIndex++) {
			backgroundMusic.add(getGame().io.musicHelper.read("game" + songIndex));
		}
	}

	/**
	 * Performs the exit operations of this {@link CoreGameState}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);

		toolTipList.clear();

		// Remove the highlight effect on the currently highlighted country
		removeHighlightFrom(highlightedCountry);
		highlightedCountry = null;

		// Stop the state from panning after it has been exited.
		panDirection = null;

		pauseMenu.hide();
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
	public void removeHighlightFrom(Country country) {

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
				country.setImage(position, region.convert(ruler.color));
			} else {
				country.setImage(position, region.convert(Color.white));
			}
		}

	}

	/**
	 * Processes a button click on a {@link CoreGameState}.
	 */
	@Override
	public void parseButton(int key, char c, Point mousePosition) {

		// Movement increment
		int increment = 50;

		switch (key) {
		case Input.KEY_UP:
			getGame().board.move(new Point(0, +increment));
			break;
		case Input.KEY_DOWN:
			getGame().board.move(new Point(0, -increment));
			break;
		case Input.KEY_LEFT:
			getGame().board.move(new Point(+increment, 0));
			break;
		case Input.KEY_RIGHT:
			getGame().board.move(new Point(-increment, 0));
			break;
		case Input.KEY_ENTER:
			pauseMenu.toggleVisibility();
			break;
		default:
			break;

		}

	}

	/**
	 * Processes a click at a specified {@link Point} on this {@link CoreGameState}.
	 */
	@Override
	public void parseClick(int button, Point click) {

		// If the player hasn't clicked the pause menu
		if (!clickPauseMenu(click) && !clickedHelp(click)) {

			// If the player hasn't clicked a UI Button in the state, they must've clicked
			// board.
			if (!super.clickedButton(click)) {
				clickBoard(click);
			}
		}

	}

	/**
	 * Retrieves the {@link Music} played by this {@link CoreGameState}.
	 */
	@Override
	public Music getMusic() {
		return backgroundMusic.get(new Random().nextInt(backgroundMusic.size()));
	}

	/**
	 * Adds a String as a tool tip to this {@link CoreGameState} to be displayed to
	 * the user.
	 * 
	 * @param toolTip
	 *            <code>String</code>
	 */
	public void show(String toolTip) {

		if (toolTip == null) {
			throw new NullPointerException("Popup cannot be null.");
		}

		// Display the tool tip for 8 seconds
		toolTipList.add(toolTip, 8000);

	}

	/**
	 * Assigns the pan direction of the {@link CoreGameState}.
	 * 
	 * @param mousePosition
	 *            {@link Point} position of the mosue.
	 */
	public void parseMouse(Point mousePosition) {

		// Holds the dimensions of the game container.
		int screenWidth = getGame().getContainer().getWidth();
		int screenHeight = getGame().getContainer().getHeight();

		// Set the padding of the window
		int xPadding = screenWidth / 10;
		int yPadding = screenHeight / 10;

		// The pixels per frame the state will pan at.
		int panSpeed = 4;

		int x = 0;
		int y = 0;

		// If the x is within the padding pan left or right
		if (mousePosition.x < xPadding) {
			x = panSpeed;
		} else if (mousePosition.x > screenWidth - xPadding) {
			x = -panSpeed;
		}

		// If the y is within the padding pan up or down
		if (mousePosition.y < yPadding) {
			y = panSpeed;
		} else if (mousePosition.y > screenHeight - yPadding) {
			y = -panSpeed;
		}

		// If there is a pan direction set the pan direction as that vector. Otherwise
		// set the pan direction as null.
		if (x != 0 || y != 0) {
			panDirection = new Point(x, y);
		} else {
			panDirection = null;
		}

	}

	/**
	 * Draws the {@link Player}'s name in the {@link Player}'s {@link Color}.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	protected void drawPlayerName(Graphics g) {
		Player p = getGame().players.getCurrent();
		g.drawImage(p.getImage(), p.getPosition().x, p.getPosition().y);
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
		Board board = getGame().board;

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
		getGame().board.getContinents().forEach(continent -> continent.getCountries().forEach(country -> {

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
	 * Pans the {@link Game#board} according to the
	 * {@link CoreGameState#panDirection}.
	 * 
	 * @param panVector
	 */
	protected void pan(Point panVector) {
		getGame().board.move(panDirection);
	}

	/**
	 * Draws the {@link PauseMenu} on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	protected void drawPauseMenu(Graphics g) {
		pauseMenu.draw(g);
	}

	/**
	 * Retrieves the width of the oval that will be displayed behind the army of a
	 * country.
	 * 
	 * @param armySize
	 *            <code>int</code> size of the army
	 * @return <code>int</code> width of oval in pixels.
	 */
	protected int getOvalWidth(int armySize) {
		return (((int) Math.log10(armySize)) + 1) * 15;
	}

	/**
	 * Retrieves the {@link Point} position that an {@link Army} will be displayed
	 * at on the screen relative to the top left corner.
	 * 
	 * @param country
	 * @return
	 */
	protected Point getArmyPosition(Country country) {

		// Sets x and y as the central width and height of the current country.
		int x = country.getPosition().x + country.getWidth() / 2;
		int y = country.getPosition().y + country.getHeight() / 2;

		Army army = country.getArmy();

		// Move the (x,y) by the offset
		x += army.getOffset().x;
		y += army.getOffset().y;

		return new Point(x, y);
	}

	/**
	 * Retrieves the centre of the oval behind the {@link Amry} of a specified
	 * {@link Country}.
	 * 
	 * @param country
	 *            {@link Country}
	 * @return {@link Point} position
	 */
	protected Point getCenterArmyPosition(Country country) {

		Point armyPos = getArmyPosition(country);

		// Add the distance to the centre of the oval.
		int x = armyPos.x + (getOvalWidth((country.getArmy().getSize()) * 4) / 5);
		int y = armyPos.y + 12;

		return new Point(x, y);
	}

	/**
	 * Performs a mouse click at a specified {@link Point} position on the
	 * {@link PauseMenu}.
	 * 
	 * @param click
	 *            {@link Point}
	 * @return Whether or not the {@link PauseMenu} was clicked or not.
	 */
	private boolean clickPauseMenu(Point click) {

		// If the pause menu is invisible then it cannot be clicked.
		if (pauseMenu.isVisible()) {

			// If the pause menu was clicked then parse the click.
			if (pauseMenu.isClicked(click)) {
				pauseMenu.parseClick(click);
				return true;
			}
			pauseMenu.hide();
		}
		return false;
	}

	/**
	 * Draws the {@link army} in its current state over the {@link Country} it is
	 * located.
	 * 
	 * @param g
	 *            A graphics context that can be used to render primitives to the
	 *            accelerated canvas provided by LWJGL.
	 */
	protected void drawArmies(Graphics g) {

		// Iterate across every country on the game board.
		getGame().board.getContinents().forEach(continent -> continent.getCountries().forEach(country -> {

			// Draw a background oval with the rulers colour. If no ruler found default to
			// light grey.
			if (country.getRuler() != null) {
				g.setColor(country.getRuler().color);
			} else {
				g.setColor(Color.lightGray);
			}

			// Holds the position that army will be drawn at
			Point armyPosition = getArmyPosition(country);

			// Holds the size of the current countries army
			int troopNumber = country.getArmy().getSize();

			drawArmyOval(armyPosition, troopNumber, g);

			g.setColor(Color.white);

			// Draw a string representing the number of troops
			// within that army at (x,y).

			g.drawString(Integer.toString(troopNumber), armyPosition.x, armyPosition.y);

		}));
	}

	/**
	 * Retrieves whether or not the {@link HelpMenu} window was clicked or not.
	 * 
	 * @param click
	 *            {@link Point}
	 * @return
	 */
	private boolean clickedHelp(Point click) {

		if (getHelp() == null)
			return false;

		HelpMenu help = getHelp();

		// If the pause menu is invisible then it cannot be clicked.
		if (help.isVisible()) {

			// If the pause menu was clicked then parse the click.
			if (help.isClicked(click)) {
				help.parseClick(click);
				return true;
			}

			help.hide();
		}

		return false;
	}

	/**
	 * Draws the oval that is displayed behind the {@link Army} on a
	 * {@link Country}.
	 * 
	 * @param position
	 *            {@link Point}
	 * @param size
	 *            size of the {@link Army}
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawArmyOval(Point position, int size, Graphics g) {

		int width = getOvalWidth(size);

		int offset = width / 5;

		g.fillOval(position.x - offset, position.y - 3, width, 25);

	}

}
