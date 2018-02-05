package peril.views.slick.states.gameStates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
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
import peril.Update;
import peril.controllers.GameController;
import peril.model.board.ModelCountry;
import peril.model.states.ModelState;
import peril.views.slick.Point;
import peril.views.slick.board.*;
import peril.views.slick.components.lists.ToolTipList;
import peril.views.slick.components.menus.Menu;
import peril.views.slick.components.menus.PauseMenu;
import peril.views.slick.helpers.MenuHelper;
import peril.views.slick.states.InteractiveState;

/**
 * A {@link InteractiveState} which displays the {@link SlickBoard} to the user.
 * This is an {@link Observer} to a {@link ModelState}.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 * @version 1.01.01
 * @since 2018-02-05
 * 
 * @see InteractiveState
 * @see Observer
 * @see ModelState
 */
public abstract class CoreGameState extends InteractiveState implements Observer {

	/**
	 * The {@link ModelState} that this {@link CoreGameState} observes.
	 */
	public final ModelState model;

	/**
	 * The {@link MenuHelper} that contains all the {@link Menu}s.
	 */
	protected final MenuHelper menus;

	/**
	 * Holds all the {@link SlickCountry}s that are currently selected by this
	 * {@link CoreGameState}.
	 */
	protected final List<SlickCountry> selected;

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
	 * Holds the tool tip that will be displayed to the user.
	 */
	private final ToolTipList toolTipList;

	/**
	 * Constructs a new {@link CoreGameState}.
	 * 
	 * @param game
	 *            The {@link Game} this state is a part of.
	 * @param stateName
	 *            Holds the name of a specific {@link CoreGameState}.
	 * @param id
	 *            The id of this {@link CoreGameState}.
	 * @param model
	 *            The {@link ModelState} that this {@link CoreGameState} observes.
	 */
	public CoreGameState(GameController game, String stateName, int id, ModelState model) {

		// The id of the state is used as the id for the help page
		super(game, stateName, id, id);
		this.menus = slick.menus;
		this.model = model;
		this.panDirection = null;
		this.toolTipList = new ToolTipList(new Point(210, 60));
		this.backgroundMusic = new ArrayList<>();
		this.selected = new ArrayList<>();

		super.addComponent(toolTipList);
	}

	/**
	 * Enters this {@link CoreGameState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {

		menus.changeHelpPage(getID());

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
	 * Renders this {@link CoreGameState} which is the {@link SlickBoard}.
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

		// Elapse the time of the tool tips.
		toolTipList.elapseTime(delta);

		// If there is no menu visible and there is a pan direction, pan.
		if (panDirection != null && !menus.menuVisible()) {
			pan(panDirection);
		}
	}

	/**
	 * Initialises the visual elements of this {@link CoreGameState}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		/*
		 * Read all the songs from the music folder that will be played over the core
		 * game state.
		 */
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

		collapseSelected();

		model.deselectAll();

		// Stop the state from panning after it has been exited.
		panDirection = null;
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
			slick.modelView.getVisual(game.getModelBoard()).move(new Point(0, +increment), width, height);
			break;
		case Input.KEY_DOWN:
			slick.modelView.getVisual(game.getModelBoard()).move(new Point(0, -increment), width, height);
			break;
		case Input.KEY_LEFT:
			slick.modelView.getVisual(game.getModelBoard()).move(new Point(+increment, 0), width, height);
			break;
		case Input.KEY_RIGHT:
			slick.modelView.getVisual(game.getModelBoard()).move(new Point(-increment, 0), width, height);
			break;
		case Input.KEY_ESCAPE:
			menus.show(PauseMenu.NAME);
			break;
		case Input.KEY_E:
			expandSelected();
			break;
		case Input.KEY_C:
			collapseSelected();
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
		if (!menus.clicked(click))

			// If the player hasn't clicked a UI Button in the state, they must've clicked
			// board.
			if (!super.clickedButton(click)) {

				// holds whether a unit was clicked.
				boolean unitClicked = false;

				for (final SlickCountry country : selected) {

					final SlickArmy army = slick.modelView.getVisual(country.model.getArmy());

					if (army.isClicked(click, country.getArmyPosition(), slick.modelView)) {
						unitClicked = true;
					}

				}

				// If there was no unit clicked.
				if (!unitClicked) {
					clickBoard(button, click);
				}

			}

	}

	/**
	 * Retrieves the {@link Music} played by this {@link CoreGameState}.
	 */
	@Override
	public final Music getMusic() {
		return backgroundMusic.get(new Random().nextInt(backgroundMusic.size()));
	}

	/**
	 * Adds a String as a tool tip to this {@link CoreGameState} to be displayed to
	 * the user.
	 * 
	 * @param toolTip
	 *            <code>String</code>
	 */
	public final void showToolTip(String toolTip) {

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
	 * Updates this {@link CoreGameState} as an {@link Observer}.
	 */
	@Override
	public void update(Observable o, Object arg) {

		// If the model state was updated
		if (o instanceof ModelState) {

			if (!(arg instanceof Update)) {
				throw new IllegalArgumentException("The property must be an update.");
			}

			Update update = (Update) arg;

			switch (update.property) {

			case "selected":
				updateSelected(update);
				break;

			}
		}

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
	protected final void removeHighlight(SlickCountry country) {

		// If there is a highlighted country
		if (country != null) {

			// Holds the ruler of the country
			SlickPlayer ruler = slick.modelView.getVisual(country.model.getRuler());

			// If there is a ruler then return the colour of the country to that of the
			// ruler. Otherwise remove the highlight effect.
			country.changeColour(ruler != null ? ruler.color : Color.white);

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
	 * Expands the selected {@link SlickCountry}'s {@link SlickArmy}.
	 */
	protected final void expandSelected() {
		selected.forEach(country -> slick.modelView.getVisual(country.model.getArmy()).expand());
	}

	/**
	 * Collapses the selected {@link SlickCountry}'s {@link SlickArmy}.
	 */
	protected final void collapseSelected() {
		selected.forEach(country -> slick.modelView.getVisual(country.model.getArmy()).collapse());
	}

	/**
	 * Draws the {@link SlickPlayer}'s name in the {@link SlickPlayer}'s
	 * {@link Color}.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	protected final void drawPlayerName(Graphics g) {
		SlickPlayer p = slick.modelView.getVisual(game.getCurrentModelPlayer());
		g.drawImage(p.getImage(), 15, 45);
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
	protected final void clickBoard(int button, Point click) {

		// Holds the game board
		SlickBoard board = slick.modelView.getVisual(game.getModelBoard());

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
				model.select(clicked == null ? null : clicked.model, game);
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
	protected final void drawAllLinks(Graphics g) {

		// If the links are toggled off do nothing
		if (!menus.linksVisible()) {
			return;
		}

		g.setColor(Color.black);

		// Get all the countries from the board.
		game.forEachModelCountry(model -> {

			// The position of the current country's army.
			final Point countryPosition = getCenterArmyPosition(slick.modelView.getVisual(model));

			// For each neighbour of that country draw the link from the neighbour to the
			// current country
			model.getNeighbours().forEach(modelNeighbour -> {

				// The position of the neighbour's army.
				final Point neighbourPosition = getCenterArmyPosition(slick.modelView.getVisual(modelNeighbour));

				// The link from the country to its neighbour
				final SlickLinkState link = slick.modelView.getVisual(model.getLinkTo(modelNeighbour).getState());

				// Draw the link
				link.draw(g, countryPosition, neighbourPosition);

			});
		});

	}

	/**
	 * Draws the {@link ToolTipList} containing the pop ups of the
	 * {@link CoreGameState} on screen.
	 * 
	 * @param g
	 */
	protected final void drawPopups(Graphics g) {
		toolTipList.draw(g);
	}

	/**
	 * Retrieves the centre of the oval behind the {@link Amry} of a specified
	 * {@link SlickCountry}.
	 * 
	 * @param country
	 *            {@link SlickCountry}
	 * @return {@link Point} position
	 */
	protected final Point getCenterArmyPosition(SlickCountry country) {

		Point armyPos = country.getArmyPosition();

		SlickArmy army = slick.modelView.getVisual(country.model.getArmy());

		// Add the distance to the centre of the oval.
		int x = armyPos.x + (army.getOvalWidth((army.model.getStrength()) * 4) / 5);
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
	protected final void drawArmies(Graphics g) {

		// Iterate across every country on the game board.
		game.getModelBoard().getContinents().values().forEach(continent -> continent.getCountries().forEach(model -> {

			final SlickCountry country = slick.modelView.getVisual(model);

			final SlickArmy army = slick.modelView.getVisual(country.model.getArmy());

			final SlickPlayer ruler = slick.modelView.getVisual(country.model.getRuler());

			army.draw(g, country.getArmyPosition(), ruler, slick.modelView);

		}));
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
		Point actualVector = slick.modelView.getVisual(game.getModelBoard()).move(panDirection, width, height);

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

		SlickBoard board = slick.modelView.getVisual(game.getModelBoard());

		// If the board has a visual representation, render it in the graphics context.
		if (board.hasImage()) {
			g.drawImage(board.getImage(), board.getPosition().x, board.getPosition().y);
		}

		// Holds the hazards that will be drawn on screen.
		Map<Point, Image> hazards = new HashMap<>();

		// For every country on the board.
		board.model.forEachCountry(model -> {

			SlickCountry country = slick.modelView.getVisual(model);

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

	/**
	 * Updates the currently selected {@link SlickCountry}.
	 * 
	 * @param update
	 *            {@link Update} where {@link Update#newValue} is a
	 *            {@link List}<{@link ModelCountry}>.
	 */
	private void updateSelected(Update update) {

		if (!(update.newValue instanceof List<?>)) {
			throw new IllegalArgumentException("The update new value must be a list of model countries.");
		}

		// De select the currently selected
		selected.forEach(country -> {
			removeHighlight(country);
			slick.modelView.getVisual(country.model.getArmy()).collapse();
		});

		// Clear the selected.
		selected.clear();

		// Iterate over each object in the list and highlight them.
		for (Object obj : (List<?>) update.newValue) {

			if (!(obj instanceof ModelCountry)) {
				throw new IllegalArgumentException("The list must contain model countries.");
			}

			// Holds the slick country assigned to the model country from the list.
			final SlickCountry country = slick.modelView.getVisual((ModelCountry) obj);

			// Expand the army and highlight the new country
			slick.modelView.getVisual(country.model.getArmy()).expand();
			addHighlight(country);

			// Add the country to the list of selected countries.
			selected.add(country);

		}

	}
}
