package peril.views.slick.states.gameStates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;
import peril.controllers.GameController;
import peril.model.states.ModelState;
import peril.views.slick.board.SlickBoard;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.components.lists.ToolTipList;
import peril.views.slick.components.menus.ChallengeMenu;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.components.menus.PauseMenu;
import peril.views.slick.states.InteractiveState;

/**
 * A {@link InteractiveState} which displays the {@link SlickBoard} from the
 * {@link Game} to the user.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 */
public abstract class CoreGameState extends InteractiveState {

	/**
	 * Holds the tool tip that will be displayed to the user.
	 */
	private final ToolTipList toolTipList;

	/**
	 * Displays the challenges to the user.
	 */
	private final ChallengeMenu challengeMenu;

	public final ModelState model;

	/**
	 * The {@link PauseMenu} for this {@link CoreGameState}.
	 */
	private final PauseMenu pauseMenu;

	/**
	 * The {@link Music} that is played in the background of the
	 * {@link CoreGameState}s.
	 */
	private final List<Music> backgroundMusic;

	/**
	 * The {@link Point} pan direction vector this state will pan at.
	 */
	private Point panDirection;

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
	public CoreGameState(GameController game, String stateName, int id, ModelState model) {

		// The id of the state is used as the id for the help page
		super(game, stateName, id, id);

		this.model = model;
		this.pauseMenu = slick.menus.pauseMenu;
		this.challengeMenu = slick.menus.challengeMenu;
		this.panDirection = null;
		this.toolTipList = new ToolTipList(new Point(210, 60));
		this.backgroundMusic = new ArrayList<>();

		super.addComponent(toolTipList);
	}

	/**
	 * Enters this {@link CoreGameState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {

		slick.menus.helpMenu.changePage(getID());

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
	 * Renders this {@link CoreGameState} which is the {@link Game#board}.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		drawBoard(g);

		g.setLineWidth(3f);

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
		if (pauseMenu.isVisible()) {
			slick.toggleHelpMenu(false);
		}

	}

	/**
	 * Initialises the visual elements of this {@link CoreGameState}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		int numberOfSongs = 3;

		for (int songIndex = 1; songIndex <= numberOfSongs; songIndex++) {
			backgroundMusic.add(slick.music.read("game" + songIndex));
		}
	}

	/**
	 * Performs the exit operations of this {@link CoreGameState}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);

		toolTipList.clear();

		model.deselectAll();

		// Stop the state from panning after it has been exited.
		panDirection = null;

		pauseMenu.hide();
	}

	/**
	 * Processes a button click on a {@link CoreGameState}.
	 */
	@Override
	public void parseButton(int key, char c, Point mousePosition) {

		// Movement increment
		int increment = 50;
		int width = slick.getContainer().getWidth();
		int height = slick.getContainer().getHeight();

		switch (key) {
		case Input.KEY_UP:
			slick.modelView.getVisualBoard(game.getModelBoard()).move(new Point(0, +increment), width, height);
			break;
		case Input.KEY_DOWN:
			slick.modelView.getVisualBoard(game.getModelBoard()).move(new Point(0, -increment), width, height);
			break;
		case Input.KEY_LEFT:
			slick.modelView.getVisualBoard(game.getModelBoard()).move(new Point(+increment, 0), width, height);
			break;
		case Input.KEY_RIGHT:
			slick.modelView.getVisualBoard(game.getModelBoard()).move(new Point(-increment, 0), width, height);
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
		if (!clickedChallenges(click) && !clickPauseMenu(click) && !clickedHelp(click)) {

			// If the player hasn't clicked a UI Button in the state, they must've clicked
			// board.
			if (!super.clickedButton(click)) {
				clickBoard(button, click);
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
	public void showToolTip(String toolTip) {

		if (toolTip == null) {
			throw new NullPointerException("Popup cannot be null.");
		}

		// Display the tool tip for 8 seconds
		toolTipList.add(toolTip, 8000);

	}

	/**
	 * Moves all the visual elements contained within this {@link CoreGameState}
	 * along a specified {@link Point} vector.
	 * 
	 * @param panVector
	 *            {@link Point}
	 */
	protected abstract void panElements(Point panVector);

	/**
	 * Removes the highlight colouring effect on {@link CoreGameState#selected}.
	 * 
	 * @param country
	 *            {@link SlickCountry} to unhighlight.
	 */
	protected void removeHighlight(SlickCountry country) {

		// If there is a highlighted country
		if (country != null) {

			// Holds the ruler of the country
			SlickPlayer ruler = slick.modelView.getVisualPlayer(country.model.getRuler());

			// If there is a ruler then return the colour of the country to that of the
			// ruler. Otherwise remove the highlight effect.
			country.replaceImage(ruler != null ? country.getRegion().convert(ruler.color)
					: country.getRegion().convert(Color.white));

		}

	}

	/**
	 * Adds the highlight effect to a {@link SlickCountry} assuming the country is
	 * not <code>null</code>.
	 * 
	 * @param country
	 *            {@link SlickCountry}
	 */
	protected final void addHighlight(SlickCountry country) {
		if (country != null) {
			country.replaceImage(country.getRegion().convert(Color.yellow));
		}
	}

	/**
	 * Assigns the pan direction of the {@link CoreGameState}.
	 * 
	 * @param mousePosition
	 *            {@link Point} position of the mouse.
	 */
	public void parseMouse(Point mousePosition) {

		// Holds the dimensions of the game container.
		int screenWidth = slick.getContainer().getWidth();
		int screenHeight = slick.getContainer().getHeight();

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
	 * Draws the {@link SlickPlayer}'s name in the {@link SlickPlayer}'s
	 * {@link Color}.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	protected void drawPlayerName(Graphics g) {
		SlickPlayer p = slick.modelView.getVisualPlayer(game.getCurrentModelPlayer());
		g.drawImage(p.getImage(), 15, 45);
	}

	/**
	 * Draws the {@link ChallengeMenu} on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	protected void drawChallengeMenu(Graphics g) {
		challengeMenu.draw(g);
	}

	/**
	 * Simulates a click at a {@link Point} on the {@link SlickBoard} and highlights
	 * the {@link SlickCountry} that clicked.
	 * 
	 * @param click
	 *            {@link Point}
	 * @param button
	 *            The mouse button that was clicked.
	 */
	protected void clickBoard(int button, Point click) {

		// Holds the game board
		SlickBoard board = slick.modelView.getVisualBoard(game.getModelBoard());

		// If there is a game board
		if (board != null) {

			// Get the country that is clicked.
			SlickCountry clicked = board.getCountry(click);

			/*
			 * If the board was clicked with the left mouse and it is a valid country to
			 * highlight then highlight the clicked country, otherwise de-selected the
			 * highlighted country.
			 */
			if (button == Input.MOUSE_LEFT_BUTTON) {
				model.select(clicked.model, game);
			} else if (button == Input.MOUSE_RIGHT_BUTTON) {
				model.deselectAll();
			}

		}
	}

	/**
	 * Draws all the links between all the {@link SlickCountry}s.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	protected void drawAllLinks(Graphics g) {

		// If the links are toggled off do nothing
		if (!slick.menus.pauseMenu.showAllLinks()) {
			return;
		}

		g.setColor(Color.black);

		Set<SlickCountry> drawn = new HashSet<>();

		// Get all the countries from the board.
		game.forEachModelCountry(model -> {

			final SlickCountry country = slick.modelView.getVisualCountry(model);

			// Sets x and y as the central width and height of the current country.
			final int countryX = getCenterArmyPosition(country).x;
			final int countryY = getCenterArmyPosition(country).y;

			drawn.add(country);

			// For each neighbour of that country draw the link from the neighbour to the
			// current country
			model.getNeighbours().forEach(modelNeighbour -> {

				final SlickCountry neighbour = slick.modelView.getVisualCountry(modelNeighbour);

				if (!drawn.contains(neighbour)) {
					// Sets x and y as the central width and height of the neighbour country.
					final int neighbourX = getCenterArmyPosition(neighbour).x;
					final int neighbourY = getCenterArmyPosition(neighbour).y;

					// Draw the line from the country to the neighbour
					g.drawLine(countryX, countryY, neighbourX, neighbourY);
				}
			});
		});

	}

	/**
	 * Draws the {@link ToolTipList} containing the pop ups of the
	 * {@link CoreGameState} on screen.
	 * 
	 * @param g
	 */
	protected void drawPopups(Graphics g) {
		toolTipList.draw(g);
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
	 * Retrieves the {@link Point} position that an {@link ModelArmy} will be
	 * displayed at on the screen relative to the top left corner.
	 * 
	 * @param country
	 * @return
	 */
	private Point getArmyPosition(SlickCountry country) {

		// Sets x and y as the central width and height of the current country.
		int x = country.getPosition().x + (country.getWidth() / 2) + country.getArmyOffset().x;
		int y = country.getPosition().y + (country.getHeight() / 2) + country.getArmyOffset().y;

		return new Point(x, y);
	}

	/**
	 * Retrieves the centre of the oval behind the {@link Amry} of a specified
	 * {@link SlickCountry}.
	 * 
	 * @param country
	 *            {@link SlickCountry}
	 * @return {@link Point} position
	 */
	protected Point getCenterArmyPosition(SlickCountry country) {

		Point armyPos = getArmyPosition(country);

		// Add the distance to the centre of the oval.
		int x = armyPos.x + (getOvalWidth((country.model.getArmy().getSize()) * 4) / 5);
		int y = armyPos.y + 12;

		return new Point(x, y);
	}

	/**
	 * Draws the {@link army} in its current state over the {@link SlickCountry} it
	 * is located.
	 * 
	 * @param g
	 *            A graphics context that can be used to render primitives to the
	 *            accelerated canvas provided by LWJGL.
	 */
	protected void drawArmies(Graphics g) {

		// Iterate across every country on the game board.
		game.getModelBoard().getContinents().values().forEach(continent -> continent.getCountries().forEach(model -> {

			final SlickCountry country = slick.modelView.getVisualCountry(model);

			// Draw a background oval with the rulers colour. If no ruler found default to
			// light grey.
			if (country.model.getRuler() != null) {
				g.setColor(slick.modelView.getVisualPlayer(country.model.getRuler()).color);
			} else {
				g.setColor(Color.lightGray);
			}

			// Holds the position that army will be drawn at
			Point armyPosition = getArmyPosition(country);

			// Holds the size of the current countries army
			int troopNumber = country.model.getArmy().getSize();

			drawArmyOval(armyPosition, troopNumber, g);

			g.setColor(Color.white);

			// Draw a string representing the number of troops
			// within that army at (x,y).

			g.drawString(Integer.toString(troopNumber), armyPosition.x, armyPosition.y);

		}));
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
	 * Retrieves whether or not the {@link HelpMenu} window was clicked or not.
	 * 
	 * @param click
	 *            {@link Point}
	 * @return
	 */
	private boolean clickedHelp(Point click) {

		// If the pause menu is invisible then it cannot be clicked.
		if (slick.menus.helpMenu.isVisible()) {

			// If the pause menu was clicked then parse the click.
			if (slick.menus.helpMenu.isClicked(click)) {
				slick.menus.helpMenu.parseClick(click);
				return true;
			}

			slick.menus.helpMenu.hide();
		}

		return false;
	}

	/**
	 * Retrieves whether or not the {@link HelpMenu} window was clicked or not.
	 * 
	 * @param click
	 *            {@link Point}
	 * @return
	 */
	private boolean clickedChallenges(Point click) {

		// If the pause menu is invisible then it cannot be clicked.
		if (slick.menus.challengeMenu.isVisible()) {

			// If the pause menu was clicked then parse the click.
			if (slick.menus.challengeMenu.isClicked(click)) {
				slick.menus.challengeMenu.parseClick(click);
				return true;
			}

			slick.menus.challengeMenu.hide();
		}

		return false;
	}

	/**
	 * Draws the oval that is displayed behind the {@link ModelArmy} on a
	 * {@link SlickCountry}.
	 * 
	 * @param position
	 *            {@link Point}
	 * @param size
	 *            size of the {@link ModelArmy}
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawArmyOval(Point position, int size, Graphics g) {

		int width = getOvalWidth(size);

		int offset = width / 5;

		g.fillOval(position.x - offset, position.y - 3, width, 25);

	}

	/**
	 * Pans the {@link Game#board} according to the
	 * {@link CoreGameState#panDirection}.
	 * 
	 * @param panVector
	 */
	private void pan(Point panVector) {

		int width = slick.getContainer().getWidth();
		int height = slick.getContainer().getHeight();

		// The vector the board actually moved along.
		Point actualVector = slick.modelView.getVisualBoard(game.getModelBoard()).move(panDirection, width, height);

		// If the board actually moved.
		if (actualVector.x != 0 || actualVector.y != 0) {
			panElements(actualVector);
		}

	}

	/**
	 * Draws this {@link SlickBoard} on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawBoard(Graphics g) {

		SlickBoard board = slick.modelView.getVisualBoard(game.getModelBoard());

		// If the board has a visual representation, render it in the graphics context.
		if (board.hasImage()) {
			g.drawImage(board.getImage(), board.getPosition().x, board.getPosition().y);
		}

		// Holds the hazards that will be drawn on screen.
		Map<Point, Image> hazards = new HashMap<>();

		// For every country on the board.
		board.model.forEachCountry(model -> {
			
			SlickCountry country = slick.modelView.getVisualCountry(model);

			final int x = country.getPosition().x;
			final int y = country.getPosition().y;

			// Draw the image of the country on top of the board.
			if (country.hasImage()) {
				g.drawImage(country.getImage(), x, y);
			}

			// If a hazard has occurred
			if (country.hasHazard()) {

				// Define the hazards visual details
				Image hazard = country.getHazard();
				hazard = hazard.getScaledCopy(30, 30);

				final int hazardX = x + (country.getWidth() / 2) + (hazard.getWidth() / 2) + country.getArmyOffset().x;
				final int hazardY = y + (country.getHeight() / 2) - hazard.getHeight() + country.getArmyOffset().y;

				// Add the hazard to the map to be drawn.
				hazards.put(new Point(hazardX, hazardY), hazard);
			}

		});

		// Draw all the hazards on screen.
		hazards.forEach((position, hazardIcon) -> {
			g.drawImage(hazardIcon, position.x, position.y);
		});

	}
}
