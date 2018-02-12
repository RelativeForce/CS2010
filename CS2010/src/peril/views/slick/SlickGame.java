package peril.views.slick;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.controllers.Directory;
import peril.controllers.GameController;
import peril.helpers.PlayerHelper;
import peril.io.FileParser;
import peril.io.SaveFile;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.states.ModelState;
import peril.views.ModelView;
import peril.views.View;
import peril.views.slick.board.SlickBoard;
import peril.views.slick.board.SlickHazard;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.components.menus.*;
import peril.views.slick.helpers.IOHelper;
import peril.views.slick.helpers.MenuHelper;
import peril.views.slick.helpers.MusicHelper;
import peril.views.slick.helpers.StateHelper;
import peril.views.slick.io.ImageReader;
import peril.views.slick.io.MapReader;
import peril.views.slick.states.*;
import peril.views.slick.states.gameStates.*;

public class SlickGame extends StateBasedGame implements View {

	/**
	 * The {@link AppGameContainer} that contains this {@link Game}.
	 */
	private final AppGameContainer agc;

	/**
	 * The {@link StateHelper} that holds all this {@link Game}'s states.
	 */
	public StateHelper states;

	/**
	 * The {@link IOHelper} that holds the input out put objects of the
	 * {@link Game}.
	 */
	public IOHelper io;

	/**
	 * The {@link MenuHelper} that holds all the {@link Menu}s for this
	 * {@link Game}.
	 */
	public MenuHelper menus;

	/**
	 * The {@link MusicHelper} the handles music and looping songs.
	 */
	public MusicHelper music;

	public final SlickModelView modelView;

	public MiniMap miniMap;

	/**
	 * Holds all the {@link SlickPlayer}'s {@link Image} icons in this {@link Game}.
	 */
	private final Map<Integer, Image> playerIcons;

	/**
	 * The {@link Color}s of all the {@link SlickPlayer}s.
	 */
	private final Color[] colors;

	private GameController game;

	public SlickGame(String title) {
		super(title);

		this.playerIcons = new HashMap<>();
		this.colors = new Color[] { Color.red, Color.blue, Color.green, Color.pink.multiply(Color.pink) };
		this.game = null;
		this.modelView = new SlickModelView();

		// Construct the container for the game as a Slick2D state based game.
		try {
			agc = new AppGameContainer(this);
			agc.setDisplayMode(620, 500, false);
			agc.setTargetFrameRate(60);
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

		final Directory directory = game.getDirectory();

		modelView.init(game);

		states.init(this, modelView.getVisual(game.getModelBoard()));

		SlickHazard.initIcons(directory.getHazardsPath());

		initPlayers(directory.getPlayersPath());

		UIEventHandler eventHandler = new UIEventHandler(this);

		// Assign Key and Mouse Listener as the UIEventhandler
		container.getInput().addKeyListener(eventHandler);
		container.getInput().addMouseListener(eventHandler);

		// Hide FPS counter
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
	 * Changes the dimensions of the {@link Game#agc} to the dimensions specified.
	 * If the new dimensions are larger than the displays dimensions then this will
	 * cause the game to go full screen.
	 * 
	 * @param width
	 *            <code>int</code> new width of the screen.
	 * @param height
	 *            <code>int</code> new height of the screen.
	 * @throws SlickException
	 */
	public void reSize(int width, int height) throws SlickException {

		// Change the window to the specified size.
		if (width >= agc.getScreenWidth() || height >= agc.getScreenHeight()) {
			agc.setDisplayMode(agc.getScreenWidth(), agc.getScreenHeight(), true);
		} else {
			agc.setDisplayMode(width, height, false);
		}

		menus.center(agc.getWidth() / 2, agc.getHeight() / 2);

	}

	@Override
	public void centerBoard() {

		final SlickBoard board = modelView.getVisual(game.getModelBoard());

		// Unlock both x and y panning
		board.unlock();

		// The centre position of the board
		final int x = ((agc.getWidth() - board.getWidth()) / 2);
		final int y = ((agc.getHeight() - board.getHeight()) / 2);

		board.setPosition(new Point(x, y));

		// Lock panning if the board is to small for the screen.
		if (board.getWidth() <= agc.getWidth()) {
			board.lockX();
		}

		if (board.getHeight() <= agc.getHeight()) {
			board.lockY();
		}

	}

	/**
	 * Enters the next {@link InteractiveState} of the {@link Game}.
	 * 
	 * @param nextState
	 */
	public void enterState(InteractiveState nextState) {
		this.enterState(nextState.getID());
	}

	/**
	 * Retrieves the current {@link InteractiveState} of the {@link Game}. This will
	 * throw {@link IllegalArgumentException} if the {@link GameState} is not a
	 * {@link InteractiveState}.
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

	/**
	 * Retrieves whether or not the music is on or off.
	 * 
	 * @return <code>boolean</code>
	 */
	public boolean isMusicOn() {
		return getContainer().getMusicVolume() == 1f;
	}

	@Override
	public void start() throws Exception {

		// Construct the container for the game as a Slick2D state based game.
		try {
			agc.start();
		} catch (SlickException e) {
			e.printStackTrace();
			throw new IllegalStateException("The game must have a game container.");
		}

	}

	@Override
	public void init(GameController game) throws Exception {
		this.game = game;

		final Directory directory = game.getDirectory();

		// Construct the container for the game as a Slick2D state based game.
		try {

			// Set the icons of the game
			String[] icons = new String[] { directory.getUIPath() + "goat16.png",
					directory.getUIPath() + "goat32.png" };
			agc.setIcons(icons);

		} catch (SlickException e) {
			e.printStackTrace();
			throw new IllegalStateException("The game must have a game container.");
		}

		// Initialise games overlay menus
		final WarMenu warMenu = new WarMenu(new Point(100, 100), game);
		final PauseMenu pauseMenu = new PauseMenu(new Point(100, 100), game);
		final HelpMenu helpMenu = new HelpMenu(new Point(100, 100), game);
		final ChallengeMenu challengeMenu = new ChallengeMenu(new Point(100, 100), game);
		final StatsMenu statsMenu = new StatsMenu(new Point(100, 100), game);
		final UnitMenu unitMenu = new UnitMenu(new Point(100, 100), game);
		final UpgradeMenu upgradeMenu = new UpgradeMenu(new Point(100, 100), game);
		final PointsMenu pointsMenu = new PointsMenu(new Point(100, 100), game);

		this.menus = new MenuHelper(pauseMenu, warMenu, helpMenu, challengeMenu, statsMenu, unitMenu, upgradeMenu,
				pointsMenu);

		// Initialise the slick states.
		final MainMenu mainMenu = new MainMenu(game, 0);
		final PlayerSelection playerSelection = new PlayerSelection(game, 1);
		final EndState end = new EndState(game, 6);
		final LoadingScreen loadingScreen = new LoadingScreen(game, 7);

		// Initialise the game play states
		final SetupState setup = new SetupState(game, 2, game.getSetup());
		final ReinforcementState reinforcement = new ReinforcementState(game, 3, game.getReinforce());
		final CombatState combat = new CombatState(game, 4, game.getAttack());
		final MovementState movement = new MovementState(game, 5, game.getFortify());

		// Subscribe the states to the board
		final ModelBoard board = game.getModelBoard();
		board.addObserver(setup);
		board.addObserver(reinforcement);
		board.addObserver(combat);
		board.addObserver(movement);

		this.states = new StateHelper(mainMenu, combat, reinforcement, setup, movement, end, loadingScreen,
				playerSelection);

		// Set the containers that ui elements will be loaded into.
		Container[] containers = new Container[] { challengeMenu, helpMenu, pauseMenu, loadingScreen, warMenu, mainMenu,
				combat, setup, reinforcement, movement, end, playerSelection, statsMenu, unitMenu, upgradeMenu,
				pointsMenu };

		this.io = new IOHelper(game, containers);

		this.menus.createHelpPages(states);

		this.music = new MusicHelper(this, directory.getMusicPath());

	}

	@Override
	public void setWinner(ModelPlayer winner) {
		states.end.addToTop(modelView.getVisual(winner));
		enterState(states.end.getID());
	}

	@Override
	public boolean isCurrentState(ModelState state) {

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
	 * Retrieves the {@link Color} associated with a {@link SlickPlayer}
	 * 
	 * @param playerNumber
	 * @return
	 */
	public Color getColor(int playerNumber) {
		return colors[playerNumber - 1];
	}

	@Override
	public void attack() {
		menus.autoAttack();
	}

	@Override
	public void setHelpMenuPage(int pageId) {
		menus.changeHelpPage(pageId);

	}

	@Override
	public ModelView getModelView() {
		return modelView;
	}

	@Override
	public void updateChallenges() {
		menus.refreshChallenges();
	}

	@Override
	public void addLoser(ModelPlayer player) {
		states.end.addToTop(modelView.getVisual(player));
	}

	@Override
	public void showToolTip(String text) {

		if (!(getCurrentState() instanceof CoreGameState)) {
			throw new IllegalStateException("ToolTips may only be shown in game.");
		}

		((CoreGameState) getCurrentState()).showToolTip(text);

	}

	@Override
	public void loadGame() throws SlickException {
		if (getCurrentState() == states.playerSelection) {
			states.playerSelection.loadGame();
		} else {
			states.mainMenu.loadGame();
		}

	}

	@Override
	public void toggleChallengeMenu(boolean state) {
		toggleMenu(state, ChallengeMenu.NAME);
	}

	@Override
	public void togglePauseMenu(boolean state) {
		toggleMenu(state, PauseMenu.NAME);
	}

	@Override
	public void toggleWarMenu(boolean state) {
		toggleMenu(state, WarMenu.NAME);
	}

	@Override
	public void toggleStatsMenu(boolean state) {
		toggleMenu(state, StatsMenu.NAME);
	}

	@Override
	public void save() {
		menus.save();
	}

	@Override
	public void toggleHelpMenu(boolean state) {
		toggleMenu(state, HelpMenu.NAME);
	}
	
	@Override
	public void togglePointsMenu(boolean state) {
		toggleMenu(state, PointsMenu.NAME);
	}
	
	@Override
	public void toggleUpgradeMenu(boolean state) {
		toggleMenu(state, PointsMenu.NAME);
	}
	
	@Override
	public void toggleUnitMenu(boolean state) {
		toggleMenu(state, PointsMenu.NAME);
	}

	private void toggleMenu(boolean state, String menuName) {
		if (state) {
			menus.show(menuName);
		} else {
			menus.hide(menuName);
		}
	}

	@Override
	public void nextHelpPage() {
		menus.nextHelpPage();

	}

	@Override
	public void previousHelpPage() {
		menus.previousHelpPage();
	}

	@Override
	public void exit() {
		getContainer().exit();
	}

	@Override
	public void enterMainMenu() {
		enterState(states.mainMenu);
	}

	@Override
	public void enterReinforce() {
		enterState(states.reinforcement);
	}

	@Override
	public void enterCombat() {
		enterState(states.combat);
	}

	@Override
	public void enterFortify() {
		enterState(states.movement);

	}

	@Override
	public FileParser getMapLoader(String mapPath, SaveFile save) {
		return new MapReader(mapPath, game, save, agc.getScreenWidth(), agc.getScreenHeight());
	}

	@Override
	public void forEachLoser(Consumer<ModelPlayer> task) {
		states.end.forEachLoser(task);
	}

	@Override
	public boolean isPaused() {
		return menus.isPaused();
	}

	@Override
	public int getArmyOffsetX(ModelCountry country) {
		return modelView.getVisual(country).getArmyOffset().x;
	}

	@Override
	public int getArmyOffsetY(ModelCountry country) {
		return modelView.getVisual(country).getArmyOffset().y;
	}

	@Override
	public void AIattack() {
		menus.autoAttack();
	}

	public int getScreenWidth() {
		return agc.getScreenWidth();
	}

	public int getScreenHeight() {
		return agc.getScreenHeight();
	}

	public void initMiniMap() {
		states.addMiniMap(modelView.getVisual(game.getModelBoard()), this);
	}

}
