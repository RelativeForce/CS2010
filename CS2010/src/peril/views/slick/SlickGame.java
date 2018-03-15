package peril.views.slick;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.controllers.Directory;
import peril.controllers.GameController;
import peril.helpers.PlayerHelper;
import peril.io.FileParser;
import peril.io.SaveFile;
import peril.Game;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.states.ModelState;
import peril.views.ModelView;
import peril.views.View;
import peril.views.slick.board.SlickBoard;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickHazard;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.components.menus.*;
import peril.views.slick.helpers.*;
import peril.views.slick.io.ImageReader;
import peril.views.slick.io.MapReader;
import peril.views.slick.states.*;
import peril.views.slick.states.gameStates.*;
import peril.views.slick.util.Point;

/**
 * The Slick 2D {@link View} that displays the game to the user. This is the
 * facade between the main game and the Slick 2D visual representation. The main
 * game should not interact with anything bar the {@link View} and
 * {@link ModelView}.
 * 
 * @author Joshua_Eddy, Joseph Rolli
 * 
 * @since 2018-03-07
 * @version 1.01.14
 * 
 * @see StateBasedGame
 * @see View
 *
 */
public final class SlickGame extends StateBasedGame implements View {

	/**
	 * The target FPS of the game.
	 */
	public static final int FPS = 60;
	
	/**
	 * The {@link SlickModelView} that maps the model objects to their slick object
	 * counterparts.
	 * 
	 * @see ModelView
	 */
	public final SlickModelView modelView;

	/**
	 * The {@link StateHelper} that holds all this game's {@link InteractiveState}s.
	 */
	public StateHelper states;

	/**
	 * The {@link IOHelper} that holds the input out put objects of the
	 * {@link SlickGame}.
	 */
	public IOHelper io;

	/**
	 * The {@link MenuHelper} that holds all the {@link Menu}s for this
	 * {@link SlickGame}.
	 */
	public MenuHelper menus;

	/**
	 * The {@link MusicHelper} the handles music and looping songs.
	 */
	public MusicHelper music;

	/**
	 * Holds all the {@link SlickPlayer}'s {@link Image} icons in this {@link Game}.
	 */
	private final Map<Integer, Image> playerIcons;

	/**
	 * The {@link Color}s of all the {@link SlickPlayer}s.
	 */
	private final Color[] colors;

	/**
	 * The {@link AppGameContainer} that contains this {@link SlickGame}.
	 */
	private final AppGameContainer agc;

	/**
	 * The {@link GameController} that allows the {@link SlickGame} to communicate
	 * with the game model.
	 */
	private GameController game;

	/**
	 * Constructs a new {@link SlickGame}.
	 * 
	 * @param title
	 *            The title to be displayed at the top of the game window.
	 */
	public SlickGame(String title) {
		super(title);

		this.playerIcons = new HashMap<>();
		this.colors = new Color[] { Color.red, Color.blue, Color.green, Color.pink.multiply(Color.pink) };
		this.game = null;
		this.modelView = new SlickModelView();

		// Construct the container for the game as a Slick2D state based game.
		try {
			agc = new AppGameContainer(this);
			agc.setDisplayMode(MainMenu.WIDTH, MainMenu.HEIGHT, false);
			agc.setSmoothDeltas(true);
			agc.setTargetFrameRate(FPS);
		} catch (SlickException e) {
			e.printStackTrace();
			throw new IllegalStateException("The game must have a game container.");
		}
	}

	/**
	 * Adds {@link CoreGameState}s to the {@link GameContainer} for this
	 * {@link Game}.
	 */
	@Override
	public void initStatesList(GameContainer container) throws SlickException {

		// The assets directory
		final Directory directory = game.getDirectory();

		// Initialise the model view
		modelView.init(game);

		// Initialise the states
		states.init(this);

		// Initialise the hazard icons
		SlickHazard.initIcons(directory.getHazardsPath());

		// Initialise the player icons.
		initPlayers(directory.getPlayersPath());

		// The event handler that handles the input events on the window.
		final UIEventHandler eventHandler = new UIEventHandler(this);

		// Assign Key and Mouse Listener as the event listener
		container.getInput().addKeyListener(eventHandler);
		container.getInput().addMouseListener(eventHandler);

		// Hide FPS counter and enable v sync
		container.setShowFPS(false);
		container.setVSync(true);
	}

	/**
	 * Set the music on or off based on the specified boolean value.
	 * 
	 * @param state
	 *            <code>boolean</code> on or off
	 */
	@Override
	public void toggleMusic(boolean state) {
		getContainer().setMusicVolume(state ? 1f : 0f);
	}

	/**
	 * Changes the dimensions of the {@link AppGameContainer} to the dimensions
	 * specified. If the new dimensions are larger than the displays dimensions then
	 * this will cause the game to go full screen.
	 * 
	 * @param width
	 *            <code>int</code> new width of the screen.
	 * @param height
	 *            <code>int</code> new height of the screen.
	 * @throws SlickException
	 * 			Exception thrown if window cannot be resized.
	 */
	public void reSize(int width, int height) throws SlickException {

		// Change the window to the specified size.
		if (width >= agc.getScreenWidth() || height >= agc.getScreenHeight()) {
			agc.setDisplayMode(agc.getScreenWidth(), agc.getScreenHeight(), true);
		} else {
			agc.setDisplayMode(width, height, false);
		}

		// Centre the menus based on the new screen position.
		menus.center(agc.getWidth() / 2, agc.getHeight() / 2);

	}

	/**
	 * Moves the current {@link SlickBoard} to the centre of the screen.
	 */
	@Override
	public void centerBoard() {

		// Holds the current slick board.
		final SlickBoard board = modelView.getVisual(game.getModelBoard());

		// Unlock both x and y panning
		board.unlock();

		// The centre position of the board
		final int x = ((agc.getWidth() - board.getWidth()) / 2);
		final int y = ((agc.getHeight() - board.getHeight()) / 2);
		board.setPosition(new Point(x, y));

		// Lock panning if the board is to small for the screen in width.
		if (board.getWidth() <= agc.getWidth()) {
			board.lockX();
		}

		// Lock panning if the board is to small for the screen in height.
		if (board.getHeight() <= agc.getHeight()) {
			board.lockY();
		}

	}

	/**
	 * Enters the next {@link InteractiveState} of the {@link SlickGame}.
	 * 
	 * @param nextState
	 *            Next {@link InteractiveState}.
	 */
	public void enterState(InteractiveState nextState) {
		this.enterState(nextState.getID());
	}

	/**
	 * Starts the game. This performs {@link AppGameContainer#start()}.
	 */
	@Override
	public void start() throws Exception {

		// Start the container for the game as a Slick2D state based game.
		try {
			agc.start();
		} catch (SlickException e) {
			e.printStackTrace();
			throw new IllegalStateException("The game container failed to start.");
		}

	}

	/**
	 * Initialises this {@link SlickGame}.
	 */
	@Override
	public void init(GameController game) throws Exception {

		// Null check.
		if (game == null) {
			throw new NullPointerException("The game controller cannot be null.");
		}

		this.game = game;

		// The assets directory
		final Directory directory = game.getDirectory();

		try {
			// Set the icons of the game window
			final String[] icons = new String[] { directory.getUIPath() + "goat16.png",
					directory.getUIPath() + "goat32.png" };
			agc.setIcons(icons);

		} catch (SlickException e) {
			e.printStackTrace();
			throw new IllegalStateException("The game must have a game container.");
		}

		// Initialise games menus.
		final WarMenu warMenu = new WarMenu(new Point(100, 100), game);
		final PauseMenu pauseMenu = new PauseMenu(new Point(100, 100), game);
		final HelpMenu helpMenu = new HelpMenu(new Point(100, 100), game);
		final ChallengeMenu challengeMenu = new ChallengeMenu(new Point(100, 100), game);
		final StatsMenu statsMenu = new StatsMenu(new Point(100, 100), game);
		final UpgradeMenu upgradeMenu = new UpgradeMenu(new Point(100, 100), game);

		// Holds all the menus
		final Set<Menu> menus = new HashSet<>();
		menus.add(warMenu);
		menus.add(pauseMenu);
		menus.add(helpMenu);
		menus.add(challengeMenu);
		menus.add(statsMenu);
		menus.add(upgradeMenu);

		// Add all the menus to the menu helper.
		this.menus = new MenuHelper(menus);

		// Initialise the slick states.
		final Opening opening = new Opening(game, 12);
		final MainMenu mainMenu = new MainMenu(game, 0);
		final PlayerSelection playerSelection = new PlayerSelection(game, 1);
		final EndState end = new EndState(game, 6);
		final LoadingScreen loadingScreen = new LoadingScreen(game, 7);

		// Initialise the game play states
		final SlickSetup setup = new SlickSetup(game, 2, game.getSetup());
		final SlickReinforce reinforcement = new SlickReinforce(game, 3, game.getReinforce());
		final SlickAttack combat = new SlickAttack(game, 4, game.getAttack());
		final SlickFortify movement = new SlickFortify(game, 5, game.getFortify());
		final Credits credits = new Credits(game, 8);
		final InstructionsState help = new InstructionsState(game, 15);

		// Subscribe the core game states to the board
		final ModelBoard board = game.getModelBoard();
		board.addObserver(setup);
		board.addObserver(reinforcement);
		board.addObserver(combat);
		board.addObserver(movement);

		// Add all the states to the state helper.
		this.states = new StateHelper(opening, mainMenu, combat, reinforcement, setup, movement, end, loadingScreen,
				playerSelection, credits, help);

		// Set the containers that visual elements will be loaded into.

		final Set<Container> containers = new HashSet<>();
		containers.add(challengeMenu);
		containers.add(helpMenu);
		containers.add(pauseMenu);
		containers.add(loadingScreen);
		containers.add(warMenu);
		containers.add(mainMenu);
		containers.add(combat);
		containers.add(setup);
		containers.add(reinforcement);
		containers.add(movement);
		containers.add(end);
		containers.add(playerSelection);
		containers.add(statsMenu);
		containers.add(upgradeMenu);
		containers.add(credits);
		containers.add(help);

		// User the containers to create the IO helper.
		this.io = new IOHelper(game, containers);

		// Create the help pages using the states.
		this.menus.createHelpPages(states, directory);

		// Create the music helper.
		this.music = new MusicHelper(this, directory.getMusicPath());

	}

	/**
	 * Set the {@link ModelPlayer} as the winner by adding them to the top of the
	 * podium.
	 */
	@Override
	public void setWinner(ModelPlayer winner) {
		states.end.addToTop(modelView.getVisual(winner));
		enterState(states.end.getID());
	}

	/**
	 * Initialises the images of a {@link SlickPlayer}.
	 * 
	 * @param playersPath
	 *            The path to the folder with the image files in.
	 */
	public void initPlayers(String playersPath) {

		for (int index = 1; index <= PlayerHelper.MAX_PLAYERS; index++) {

			String path = playersPath + "player" + index + "Icon.png";

			playerIcons.put(index, ImageReader.getImage(path).getScaledCopy(180, 80));

		}

	}

	/**
	 * Causes the {@link SlickGame} to display an attack to the user.
	 */
	@Override
	public void attack() {
		menus.autoAttack();
	}

	/**
	 * Save the game to the file specified by the pause menu.
	 */
	@Override
	public void save() {
		menus.save();
	}

	/**
	 * Exits the game using {@link AppGameContainer#exit()}.
	 */
	@Override
	public void exit() {
		agc.exit();
	}

	/**
	 * Change the help menu page.
	 */
	@Override
	public void setHelpMenuPage(int pageId) {
		menus.changeHelpPage(pageId);
	}

	/**
	 * Update the challenges on the challenge menu.
	 */
	@Override
	public void updateChallenges() {
		menus.refreshChallenges();
	}

	/**
	 * Add a {@link ModelPlayer} to the podium.
	 */
	@Override
	public void addLoser(ModelPlayer player) {
		states.end.addToTop(modelView.getVisual(player));
	}

	/**
	 * Show a tool tip on the current {@link InteractiveState}.
	 */
	@Override
	public void showToolTip(String text) {

		if (getCurrentState() instanceof CoreGameState) {
			getCurrentState().showToolTip(text, new Point(410, 100));
		} else {
			getCurrentState().showToolTip(text, new Point(0, 0));
		}
	}

	/**
	 * Show a tool tip on the current {@link InteractiveState}.
	 * 
	 * @param text
	 *            The message of the tool tip.
	 * @param position
	 *            The {@link Point} position of the tool tip.
	 */
	public void showToolTip(String text, Point position) {
		getCurrentState().showToolTip(text, position);
	}

	/**
	 * Loads the game by entering the loading screen.
	 */
	@Override
	public void loadGame() throws SlickException {
		if (getCurrentState() == states.playerSelection) {
			states.playerSelection.loadGame();
		} else {
			states.mainMenu.loadGame();
		}

	}

	/**
	 * Toggle the visibility of the challenge menu.
	 */
	@Override
	public void toggleChallengeMenu(boolean state) {
		toggleMenu(state, ChallengeMenu.NAME);
	}

	/**
	 * Toggle the visibility of the pause menu.
	 */
	@Override
	public void togglePauseMenu(boolean state) {
		toggleMenu(state, PauseMenu.NAME);
	}

	/**
	 * Toggle the visibility of the war menu.
	 */
	@Override
	public void toggleWarMenu(boolean state) {
		toggleMenu(state, WarMenu.NAME);
	}

	/**
	 * Toggle the visibility of the stats menu.
	 */
	@Override
	public void toggleStatsMenu(boolean state) {
		toggleMenu(state, StatsMenu.NAME);
	}

	/**
	 * Toggle the visibility of the help menu.
	 */
	@Override
	public void toggleHelpMenu(boolean state) {
		toggleMenu(state, HelpMenu.NAME);
	}

	/**
	 * Toggle the visibility of the upgrade menu.
	 */
	@Override
	public void toggleUpgradeMenu(boolean state) {
		toggleMenu(state, UpgradeMenu.NAME);
	}

	/**
	 * Moves to the next help page.
	 */
	@Override
	public void nextHelpPage() {
		menus.nextHelpPage();

	}

	/**
	 * Moves to the previous help page.
	 */
	@Override
	public void previousHelpPage() {
		menus.previousHelpPage();
	}

	/**
	 * Enter the main menu.
	 */
	@Override
	public void enterMainMenu() {
		enterState(states.mainMenu);
	}

	/**
	 * Enter the reinforce state.
	 */
	@Override
	public void enterReinforce() {
		enterState(states.reinforcement);
	}

	/**
	 * Enter the combat state.
	 */
	@Override
	public void enterCombat() {
		enterState(states.combat);
	}

	/**
	 * Enter the fortify state.
	 */
	@Override
	public void enterFortify() {
		enterState(states.movement);

	}

	/**
	 * Performs a {@link Consumer} on every {@link SlickPlayer} on the podium.
	 */
	@Override
	public void forEachLoser(Consumer<ModelPlayer> task) {
		states.end.forEachLoser(task);
	}

	/**
	 * Sets the Mini-Map on the board but if the map is smaller than the screen
	 * remove the mini-map.
	 */
	public void initMiniMap() {

		// Holds the slick board
		final SlickBoard board = modelView.getVisual(game.getModelBoard());

		// Change the window to the specified size.
		if (board.getWidth() >= getScreenWidth() || board.getHeight() >= getScreenHeight()) {
			states.addMiniMap(board, getScreenWidth(), getScreenHeight(), game);
		} else {
			states.removeMiniMap();
		}

	}

	/**
	 * Performs a AI attack where the squad is auto selected.
	 */
	@Override
	public void AIattack() {
		menus.autoAttack();
	}

	/**
	 * Returns the y coordinate of the {@link SlickCountry} army offset.
	 */
	@Override
	public int getArmyOffsetX(ModelCountry country) {
		return modelView.getVisual(country).getArmyOffset().x;
	}

	/**
	 * Returns the x coordinate of the {@link SlickCountry} army offset.
	 */
	@Override
	public int getArmyOffsetY(ModelCountry country) {
		return modelView.getVisual(country).getArmyOffset().y;
	}

	/**
	 * Returns the screen width of the {@link AppGameContainer}.
	 */
	@Override
	public int getScreenWidth() {
		return agc.getScreenWidth();
	}

	/**
	 * Returns the screen height of the {@link AppGameContainer}.
	 */
	@Override
	public int getScreenHeight() {
		return agc.getScreenHeight();
	}

	/**
	 * Returns the width of the {@link AppGameContainer}.
	 */
	@Override
	public int getWindowWidth() {
		return agc.getWidth();
	}

	/**
	 * Returns the height of the {@link AppGameContainer}.
	 */
	@Override
	public int getWindowHeight() {
		return agc.getHeight();
	}

	/**
	 * Returns whether the {@link AppGameContainer} is full screen or not.
	 */
	@Override
	public boolean isFullScreen() {
		return agc.isFullscreen();
	}

	/**
	 * Returns whether the pause menu is displays on screen.
	 */
	@Override
	public boolean isPaused() {
		return menus.isPaused();
	}

	/**
	 * Retrieves whether or not the music is on or off.
	 * 
	 * @return <code>boolean</code>
	 */
	public boolean isMusicOn() {
		return getContainer().getMusicVolume() == 1f;
	}

	/**
	 * Check if the current state is a {@link CoreGameState} and if so check if the
	 * current state's {@link ModelState} is the same as the specified one.
	 */
	@Override
	public boolean isCurrentState(ModelState state) {

		// Return if the current core game states model is the current state.
		if (getCurrentState() instanceof CoreGameState) {
			return ((CoreGameState) getCurrentState()).model == state;
		}

		return false;
	}

	/**
	 * Retrieves the {@link Image} icon for a {@link SlickPlayer}
	 * 
	 * @param player
	 *            {@link SlickPlayer}
	 * @return {@link Image} icon.
	 */
	public Image getPlayerIcon(int player) {
		return playerIcons.get(player);
	}

	/**
	 * Retrieves the {@link Color} associated with a {@link SlickPlayer}
	 * 
	 * @param playerNumber
	 * 			The number of the associated player.
	 * @return
	 * 			The {@link Color} associated with the player.
	 */
	public Color getColor(int playerNumber) {
		return colors[playerNumber - 1];
	}

	/**
	 * Retrieves the {@link SlickModelView} of this {@link SlickGame}.
	 */
	@Override
	public ModelView getModelView() {
		return modelView;
	}

	/**
	 * Retrieves the {@link MapReader} that will load the map.
	 */
	@Override
	public FileParser getMapLoader(String mapPath, SaveFile save) {
		return new MapReader(mapPath, game, save);
	}

	/**
	 * Retrieves the current {@link InteractiveState} of the {@link SlickGame}. This
	 * will throw {@link IllegalArgumentException} if the {@link GameState} is not a
	 * {@link InteractiveState}.
	 * 
	 * @return The current {@link InteractiveState}.
	 */
	@Override
	public InteractiveState getCurrentState() {

		// Holds the current game state.
		GameState state = super.getCurrentState();

		// If the current state is a CoreGameState return it as a InteractiveState
		if (state instanceof InteractiveState) {
			return (InteractiveState) state;
		}
		throw new IllegalStateException(state.getID() + " is not a valid state as it is not a InteractiveState.");
	}
	
	@Override
	public void enterCredits() {
		enterState(states.credits);
	}
	
	@Override
	public void enterHelp() {
		enterState(states.help);
	}
	

	@Override
	public void blockLink() {
		menus.blockLink();		
	}

	/**
	 * Toggles the visibility of a {@link Menu} specified by its name.
	 * 
	 * @param state
	 *            The visibility of the {@link Menu}
	 * @param menuName
	 *            The name of the menu.
	 */
	private void toggleMenu(boolean state, String menuName) {

		// If the menu should be visible.
		if (state) {
			menus.show(menuName);
		} else {
			menus.hide(menuName);
		}
	}

}
