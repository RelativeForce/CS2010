package peril.views.slick;

import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.controllers.GameController;
import peril.helpers.PlayerHelper;
import peril.io.FileParser;
import peril.io.SaveFile;
import peril.model.ModelPlayer;
import peril.model.states.ModelState;
import peril.views.ModelView;
import peril.views.View;
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
import peril.views.slick.states.gameStates.multiSelectState.*;

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

	/**
	 * Holds all the {@link SlickPlayer}'s {@link Image} icons in this {@link Game}.
	 */
	private final Map<Integer, Image> playerIcons;

	/**
	 * The {@link Color}s of all the {@link SlickPlayer}s.
	 */
	private final Color[] colors;

	private Game game;

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

		states.init(this);
		
		modelView.init(game.getGameController());

		SlickHazard.initIcons(game.assets.ui);

		initPlayers(game.assets.ui);

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
		if (width >= agc.getScreenWidth() && height >= agc.getScreenHeight()) {
			agc.setDisplayMode(agc.getScreenWidth(), agc.getScreenHeight(), true);
		} else {

			agc.setDisplayMode(width, height, false);
		}

		menus.center(agc.getWidth() / 2, agc.getHeight() / 2);

		// Reset the board
		game.board.reset();

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
	public void init(Game game) throws Exception {
		this.game = game;
		// Construct the container for the game as a Slick2D state based game.
		try {

			// Set the icons of the game
			String[] icons = new String[] { game.assets.ui + "goat16.png", game.assets.ui + "goat32.png" };
			agc.setIcons(icons);

		} catch (SlickException e) {
			e.printStackTrace();
			throw new IllegalStateException("The game must have a game container.");
		}

		// Initialise games overlay menus
		WarMenu warMenu = new WarMenu(new Point(100, 100), game);
		PauseMenu pauseMenu = new PauseMenu(new Point(100, 100), game);
		HelpMenu helpMenu = new HelpMenu(new Point(100, 100), game);
		ChallengeMenu challengeMenu = new ChallengeMenu(new Point(100, 100), game);

		this.menus = new MenuHelper(pauseMenu, warMenu, helpMenu, challengeMenu);

		GameController gc = game.getGameController();
		
		// Initialise the game states.
		MainMenu mainMenu = new MainMenu(gc, 0);
		PlayerSelection playerSelection = new PlayerSelection(gc, 1);

		SetupState setup = new SetupState(gc, 2, game.states.setup);
		ReinforcementState reinforcement = new ReinforcementState(gc, 3, game.states.reinforcement);
		CombatState combat = new CombatState(gc, 4, game.states.combat);
		MovementState movement = new MovementState(gc, 5, game.states.movement);
		EndState end = new EndState(gc, 6);
		LoadingScreen loadingScreen = new LoadingScreen(gc, 7);

		this.states = new StateHelper(mainMenu, combat, reinforcement, setup, movement, end, loadingScreen,
				playerSelection);

		// Set the containers that ui elements will be loaded into.
		Container[] containers = new Container[] { challengeMenu, helpMenu, pauseMenu, loadingScreen, warMenu, mainMenu,
				combat, setup, reinforcement, movement, end, playerSelection };

		this.io = new IOHelper(game, containers);

		this.menus.createHelpPages(this);

		this.music = new MusicHelper(this, game.assets.music);

	}

	@Override
	public void setWinner(ModelPlayer winner) {
		states.end.addToTop(modelView.getVisualPlayer(winner));
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
	 * @param uiPath
	 *            The path to the folder with the image files in.
	 */
	public void initPlayers(String uiPath) {

		for (int index = 1; index <= PlayerHelper.MAX_PLAYERS; index++) {

			String path = uiPath + "player" + index + "Icon.png";

			playerIcons.put(index, ImageReader.getImage(path).getScaledCopy(90, 40));

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
		menus.warMenu.show();
		menus.warMenu.selectMaxDice();
		menus.warMenu.attack();
	}

	@Override
	public void setHelpMenuPage(int pageId) {
		menus.helpMenu.changePage(pageId);

	}

	@Override
	public ModelView getModelView() {
		return modelView;
	}

	@Override
	public void updateChallenges() {
		menus.challengeMenu.refreshChallenges();
	}

	@Override
	public void addLoser(ModelPlayer player) {
		states.end.addToTop(modelView.getVisualPlayer(player));
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
		menus.challengeMenu.setVisibility(state);
	}

	@Override
	public void togglePauseMenu(boolean state) {
		menus.pauseMenu.setVisibility(state);
	}

	@Override
	public void toggleWarMenu(boolean state) {
		menus.warMenu.setVisibility(state);
	}

	@Override
	public void save() {
		menus.pauseMenu.save();
	}

	@Override
	public void toggleHelpMenu(boolean state) {
		menus.helpMenu.setVisibility(state);
	}

	@Override
	public void nextHelpPage() {
		menus.helpMenu.nextPage();

	}

	@Override
	public void previousHelpPage() {
		menus.helpMenu.previousPage();
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
		return new MapReader(mapPath, game, save);
	}

}
