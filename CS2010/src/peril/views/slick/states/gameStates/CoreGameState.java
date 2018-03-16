package peril.views.slick.states.gameStates;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.GameController;
import peril.Update;
import peril.ai.AI;
import peril.model.board.ModelCountry;
import peril.model.board.links.ModelLinkState;
import peril.model.states.ModelState;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.SlickGame;
import peril.views.slick.board.*;
import peril.views.slick.components.MiniMap;
import peril.views.slick.components.menus.Menu;
import peril.views.slick.components.menus.PauseMenu;
import peril.views.slick.components.menus.StatsMenu;
import peril.views.slick.helpers.MenuHelper;
import peril.views.slick.states.InteractiveState;
import peril.views.slick.util.Button;
import peril.views.slick.util.Point;

/**
 * A {@link InteractiveState} which displays the {@link SlickBoard} to the user.
 * This is an {@link Observer} to a {@link ModelState}.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 * 
 * @since 2018-03-15
 * @version 1.01.08
 * 
 * @see InteractiveState
 * @see Observer
 * @see ModelState
 */
public abstract class CoreGameState extends InteractiveState implements Observer {

	/**
	 * The number of pixels per second that this {@link CoreGameState} will pan.
	 */
	private static final int PAN_SPEED = 50;

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
	 * The {@link MiniMap} that is currently displayed over the {@link SlickBoard}.
	 */
	private MiniMap miniMap;

	/**
	 * Constructs a new {@link CoreGameState}.
	 * 
	 * @param game
	 *            The {@link GameController} that allows this {@link CoreGameState}
	 *            to query the state of the game.
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
		this.backgroundMusic = new ArrayList<>();
		this.selected = new ArrayList<>();

	}

	/**
	 * Sets the {@link MiniMap} that the {@link CoreGameState} displays.
	 * 
	 * @param miniMap
	 *            The {@link MiniMap} that the {@link CoreGameState} displays.
	 */
	public final void setMiniMap(MiniMap miniMap) {
		this.miniMap = miniMap;
	}

	/**
	 * Retrieves the {@link Music} played by this {@link CoreGameState}.
	 */
	@Override
	public final Music getMusic() {
		return backgroundMusic.get(new Random().nextInt(backgroundMusic.size()));
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

		miniMap.repositionWindow();
	}

	/**
	 * Renders this {@link CoreGameState} which is the {@link SlickBoard}.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {

		drawBoard(frame);

		frame.setLineWidth(3f);

	}

	/**
	 * Updates this {@link CoreGameState} between frames.
	 */
	@Override
	public void update(GameContainer gc, int delta, Frame frame) {

		final Input input = gc.getInput();

		final Point mouse = new Point(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());

		final Point panDirection = processPan(mouse);

		// If there is no menu visible and there is a pan direction, pan.
		if (panDirection != null && !menus.menuVisible()) {

			final float ratio = (((float) 1000f) / (float) SlickGame.FPS) / ((float) delta);

			final int x = (int) ((float) panDirection.x / ratio);
			final int y = (int) ((float) panDirection.y / ratio);

			frame.panToolTips(pan(new Point(x, y)));
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

		super.clearToolTips();

		collapseSelected();

		model.deselectAll();

	}

	/**
	 * Processes a button click on a {@link CoreGameState}.
	 */
	@Override
	public void parseButton(int key, Point mousePosition) {
		super.parseButton(key, mousePosition);

		// Movement increment
		final int increment = 50;

		switch (key) {
		case Input.KEY_UP:

			if (!slick.menus.menuVisible()) {
				pan(new Point(0, +increment));
			}

			break;
		case Input.KEY_DOWN:
			if (!slick.menus.menuVisible()) {
				pan(new Point(0, -increment));
			}

			break;
		case Input.KEY_LEFT:
			if (!slick.menus.menuVisible()) {
				pan(new Point(+increment, 0));
			}

			break;
		case Input.KEY_RIGHT:
			if (!slick.menus.menuVisible()) {
				pan(new Point(-increment, 0));
			}

			break;
		case Input.KEY_ESCAPE:
			menus.show(PauseMenu.NAME);
			break;
		default:
			break;

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
			final SlickPlayer ruler = slick.modelView.getVisual(country.model.getRuler());

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
			country.changeColour(Color.yellow);
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
	 * @param frame
	 *            The {@link Frame} that displays this {@link CoreGameState} to the
	 *            user.
	 */
	protected final void drawPlayerName(Frame frame) {

		final SlickPlayer player = slick.modelView.getVisual(game.getCurrentModelPlayer());

		frame.draw(player, new EventListener() {

			@Override
			public void mouseHover(Point mouse) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {
				slick.menus.show(StatsMenu.NAME);

			}

			@Override
			public void draw(Frame frame) {
				frame.draw(player.getImage(), player.getPosition().x, player.getPosition().y);
			}

			@Override
			public void buttonPress(int key, Point mouse) {
				// Do nothing
			}
		});
	}

	/**
	 * Draws all the links between all the {@link SlickCountry}s.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays this {@link CoreGameState} to the
	 *            user.
	 */
	protected final void drawAllLinks(Frame frame) {

		frame.setColor(Color.black);

		// Get all the countries from the board.
		game.forEachModelCountry(model -> {

			// The position of the current country's army.
			final Point countryPosition = slick.modelView.getVisual(model).getArmyPosition();

			// For each neighbour of that country draw the link from the neighbour to the
			// current country
			model.getNeighbours().forEach(modelNeighbour -> {

				// The position of the neighbour's army.
				final Point neighbourPosition = slick.modelView.getVisual(modelNeighbour).getArmyPosition();

				// The link from the country to its neighbour
				final SlickLinkState link = slick.modelView.getVisual(model.getLinkTo(modelNeighbour).getState());

				// If the links are toggled on or the link is blocked
				if (menus.linksVisible() || link.model == ModelLinkState.BLOCKADE) {

					// Draw the link
					link.draw(frame, countryPosition, neighbourPosition);
				}
			});
		});

	}

	/**
	 * Draws the {@link SlickArmy} in its current state over the
	 * {@link SlickCountry} it is located.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays this {@link CoreGameState} to the
	 *            user.
	 */
	protected final void drawArmies(Frame frame) {

		// Iterate across every country on the game board.
		game.getModelBoard().getContinents().values().forEach(continent -> continent.getCountries().forEach(model -> {

			final SlickCountry country = slick.modelView.getVisual(model);

			final SlickArmy army = slick.modelView.getVisual(country.model.getArmy());

			final SlickPlayer ruler = slick.modelView.getVisual(country.model.getRuler());

			army.draw(frame, country.getArmyPosition(), ruler, slick.modelView);

		}));
	}

	/**
	 * If there is a {@link MiniMap} then display then draw it.
	 * 
	 * @param frame
	 *            The {@link Frame} that will display the {@link MiniMap} to the
	 *            user.
	 */
	protected final void drawMiniMap(Frame frame) {

		if (miniMap == null) {
			return;
		}

		frame.draw(miniMap, new EventListener() {

			@Override
			public void mouseHover(Point mouse) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {
				miniMap.parseClick(mouse);
			}

			@Override
			public void draw(Frame frame) {
				miniMap.draw(frame);
			}

			@Override
			public void buttonPress(int key, Point mouse) {
				// Do nothing
			}
		});
	}

	/**
	 * Pans a {@link Button} by a specified {@link Point} pan vector.
	 * 
	 * @param button
	 *            The {@link Button} to be panned.
	 * @param panVector
	 *            The direction that the {@link Button} will be panned.
	 */
	protected final void panButton(Button button, Point panVector) {
		final Point current = button.getPosition();
		button.setPosition(new Point(current.x + panVector.x, current.y + panVector.y));
	}

	/**
	 * Moves the specified {@link Button} to the position that the upgrade
	 * {@link Button} is displayed over the primary country selected.
	 * 
	 * @param upgrade
	 *            The upgrade button.
	 */
	protected final void showUpgradeButton(Button upgrade) {

		final Point army = selected.get(0).getArmyPosition();
		upgrade.setPosition(new Point(army.x - upgrade.getWidth(), army.y + (SlickUnit.HEIGHT / 2) + 5));
		upgrade.show();
	}

	/**
	 * Retrieves the {@link Point} vector that the {@link SlickBoard} panned when
	 * the specified {@link Point} pan vector was applied.
	 * 
	 * @param panVector
	 *            The {@link Point} vector defining how the far the
	 *            {@link SlickBoard} should move and in what direction.
	 * @return The actual {@link Point} vector that the screen and its elements
	 *         panned.
	 */
	private Point pan(Point panVector) {

		final int width = slick.getContainer().getWidth();
		final int height = slick.getContainer().getHeight();

		// The vector the board actually moved along.
		final Point actualVector = slick.modelView.getVisual(game.getModelBoard()).move(panVector, width, height);

		// If the board actually moved.
		if (actualVector.x != 0 || actualVector.y != 0) {
			panElements(actualVector);
			miniMap.repositionWindow();
		}

		return actualVector;

	}

	/**
	 * Draws this {@link SlickBoard} on screen.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays this {@link CoreGameState} to the
	 *            user.
	 */
	private void drawBoard(Frame frame) {

		final SlickBoard board = slick.modelView.getVisual(game.getModelBoard());

		frame.draw(board, new EventListener() {

			@Override
			public void mouseHover(Point mouse) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {

				if (slick.menus.menuVisible()) {
					return;
				} else if (!(CoreGameState.this instanceof SlickSetup) && game.getCurrentModelPlayer().ai != AI.USER) {
					return;
				}

				// Get the country that is clicked.
				final SlickCountry clicked = board.getCountry(mouse);

				/*
				 * If the board was clicked with the left mouse and it is a valid country to
				 * highlight then highlight the clicked country, otherwise de-selected the
				 * highlighted country.
				 */
				if (mouseButton == Input.MOUSE_LEFT_BUTTON) {
					model.select(clicked == null ? null : clicked.model);
				} else if (mouseButton == Input.MOUSE_RIGHT_BUTTON) {
					model.deselectAll();
				}

			}

			@Override
			public void draw(Frame frame) {
				board.draw(frame);
			}

			@Override
			public void buttonPress(int key, Point mouse) {
				// Do nothing
			}
		});

	}

	/**
	 * Assigns the pan direction of the {@link CoreGameState}.
	 * 
	 * @param mousePosition
	 *            {@link Point} position of the mouse.
	 * @return The pan direction {@link Point} vector.
	 */
	private Point processPan(Point mousePosition) {

		final SlickBoard board = slick.modelView.getVisual(game.getModelBoard());

		// Holds the dimensions of the game container.
		final int screenWidth = slick.getScreenWidth();
		final int screenHeight = slick.getScreenHeight();

		// Set the padding of the window
		final int xPadding = screenWidth / 20;
		final int yPadding = screenHeight / 20;

		float widthRatio = (float) screenWidth / board.getWidth();
		float heightRatio = (float) screenHeight / board.getHeight();

		int x = 0;
		int y = 0;

		// If the x is within the padding pan left or right
		if (mousePosition.x < xPadding) {
			x = (int) (widthRatio * PAN_SPEED);
		} else if (mousePosition.x > screenWidth - xPadding) {
			x = (int) (widthRatio * -PAN_SPEED);
		}

		// If the y is within the padding pan up or down
		if (mousePosition.y < yPadding) {
			y = (int) (heightRatio * PAN_SPEED);
		} else if (mousePosition.y > screenHeight - yPadding) {
			y = (int) (heightRatio * -PAN_SPEED);
		}

		return new Point(x, y);

	}

	/**
	 * Updates the currently selected {@link SlickCountry}.
	 * 
	 * @param update
	 *            A {@link Update} where {@link Update#newValue} is a
	 *            {@link List}&lt;{@link ModelCountry}&gt;.
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

			// If the object is not a null country
			if (obj != null) {

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
}
