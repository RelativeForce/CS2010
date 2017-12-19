package peril;

import java.io.File;
import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.ai.api.Controller;
import peril.ai.api.RequestHandler;
import peril.board.*;
import peril.helpers.*;
import peril.ui.Container;
import peril.ui.UIEventHandler;
import peril.ui.components.menus.*;
import peril.ui.states.*;
import peril.ui.states.gameStates.*;
import peril.ui.states.gameStates.multiSelectState.*;

/**
 * Encapsulate the main game logic for Peril. This also extends
 * {@link StateBasedGame}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class Game extends StateBasedGame {

	/**
	 * The {@link StateHelper} that holds all this {@link Game}'s states.
	 */
	public final StateHelper states;

	/**
	 * The {@link IOHelper} that holds the input out put objects of the
	 * {@link Game}.
	 */
	public final IOHelper io;

	/**
	 * The {@link PlayerHelper} that holds all this {@link Game}s {@link Player}s.
	 */
	public final PlayerHelper players;

	/**
	 * The instance of the {@link Board} used for this game.
	 */
	public final Board board;

	/**
	 * The {@link DirectoryHelper} that holds all the sub directories of the assets
	 * folder.
	 */
	public final DirectoryHelper assets;

	/**
	 * The {@link MenuHelper} that holds all the {@link Menu}s for this
	 * {@link Game}.
	 */
	public final MenuHelper menus;

	/**
	 * The {@link MusicHelper} the handles music and looping songs.
	 */
	public final MusicHelper music;

	/**
	 * The {@link Controller} that allows the user/AI to interact with the
	 * {@link Game}.
	 */
	public final Controller api;

	/**
	 * The current turn of the {@link Game}. Initially zero;
	 */
	private int currentRound;

	/**
	 * The {@link AppGameContainer} that contains this {@link Game}.
	 */
	private final AppGameContainer agc;

	/**
	 * Constructs a new {@link Game}.
	 */
	private Game() {
		super("PERIL");

		// Holds the path of the peril assets
		StringBuilder assetsPath = new StringBuilder(new File(System.getProperty("user.dir")).getPath())
				.append(File.separatorChar).append("assets");

		this.assets = new DirectoryHelper(assetsPath.toString());

		this.api = new RequestHandler(this);

		// Set the initial round to zero
		this.currentRound = 0;

		// Construct the board with the initial name.
		this.board = new Board(this, "NOT ASSIGNED");

		this.players = new PlayerHelper(this);

		// Initialise games overlay menus
		WarMenu warMenu = new WarMenu(new Point(100, 100), this);
		PauseMenu pauseMenu = new PauseMenu(new Point(100, 100), this);
		HelpMenu helpMenu = new HelpMenu(new Point(100, 100), this);
		ChallengeMenu challengeMenu = new ChallengeMenu(new Point(100, 100), this);

		this.menus = new MenuHelper(pauseMenu, warMenu, helpMenu, challengeMenu);

		// Initialise the game states.
		MainMenuState mainMenu = new MainMenuState(this, 0);
		PlayerSelection playerSelection = new PlayerSelection(this, 1);
		SetupState setup = new SetupState(this, 2);
		ReinforcementState reinforcement = new ReinforcementState(this, 3);
		CombatState combat = new CombatState(this, 4);
		MovementState movement = new MovementState(this, 5);
		EndState end = new EndState(this, 6);
		LoadingScreen loadingScreen = new LoadingScreen(this, 7);

		this.states = new StateHelper(mainMenu, combat, reinforcement, setup, movement, end, loadingScreen,
				playerSelection);

		// Set the containers that ui elements will be loaded into.
		Container[] containers = new Container[] { challengeMenu, helpMenu, pauseMenu, loadingScreen, warMenu, mainMenu,
				combat, setup, reinforcement, movement, end, playerSelection };

		this.io = new IOHelper(this, containers);

		this.menus.createHelpPages(this);

		this.music = new MusicHelper(this);

		// Construct the container for the game as a Slick2D state based game.
		try {
			agc = new AppGameContainer(this);
			agc.setDisplayMode(400, 300, false);
			agc.setTargetFrameRate(40);

			// Set the icons of the game
			String[] icons = new String[] { assets.ui + "goat16.png", assets.ui + "goat32.png" };
			agc.setIcons(icons);

			agc.start();
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
		
		EnvironmentalHazard.initIcons(assets.ui);
		
		players.init(assets.ui);

		UIEventHandler eventHandler = new UIEventHandler(this);
		
		// Assign Key and Mouse Listener as the UIEventhandler
		container.getInput().addKeyListener(eventHandler);
		container.getInput().addMouseListener(eventHandler);

		// Hide FPS counter
		container.setShowFPS(false);
		container.setVSync(true);
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
		board.reset();

	}

	/**
	 * Distributes the countries between the {@link Game#players} equally.
	 */
	public void autoDistributeCountries() {

		players.forEach(player -> {
			player.setCountriesRuled(0);
			player.setContinentsRuled(0);
			player.totalArmy.setSize(0);
		});

		// Iterate through each country on the board.
		board.forEachCountry(country -> assignPlayer(country));

	}

	/**
	 * Retrieves the current turn number.
	 * 
	 * @return <code>int</code>
	 */
	public int getRoundNumber() {
		return currentRound;
	}

	/**
	 * Assigns the current round number to this {@link Game}
	 * 
	 * @param roundNumber
	 */
	public void setRoundNumber(int roundNumber) {
		this.currentRound = roundNumber;
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
	 * Enters the next {@link InteractiveState} of the {@link Game}.
	 * 
	 * @param nextState
	 */
	public void enterState(InteractiveState nextState) {
		this.enterState(nextState.getID());
	}

	/**
	 * Runs the game.
	 * 
	 * @param args
	 *            Unused.
	 */
	public static void main(String[] args) {

		// Create the instance of the game.
		new Game();

	}

	/**
	 * Checks all the {@link Continent}s on the {@link Board} to see if they are
	 * ruled. This is o(n^2) complexity
	 */
	public void checkContinentRulership() {

		players.forEach(player -> player.setContinentsRuled(0));

		board.getContinents().values().forEach(continent -> {

			continent.isRuled();

			if (continent.getRuler() != null) {
				continent.getRuler().setContinentsRuled(continent.getRuler().getCountriesRuled() + 1);
			}
		});

	}

	/**
	 * Checks if there is only one {@link Player} in play. If this is the case then
	 * that {@link Player} has won.
	 */
	public void checkWinner() {
		if (players.numberOfPlayers() == 1) {
			states.end.addToTop(players.getCurrent());
			enterState(states.end.getID());
		}
	}

	/**
	 * Set the music on or off based on the specified boolean value.
	 * 
	 * @param state
	 *            <code>boolean</code> on or off
	 */
	public void toggleMusic(boolean state) {
		getContainer().setMusicVolume(state ? 1f : 0f);
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
	 * Performs all the tasks that occur at the end of a round.
	 */
	public void endRound() {
		board.endRound();
		currentRound++;
	}

	/**
	 * Assigns a {@link Player} ruler to a {@link Country} using a parameter
	 * {@link Random}.
	 * 
	 * @param country
	 *            {@link Country} that is to be ruled.
	 */
	private void assignPlayer(Country country) {

		// Holds whether the country has assigned a player ruler.
		boolean set = false;

		while (!set) {

			// Holds the number of players in the game.
			int numberOfPlayers = players.numberOfPlayers();

			// Get the player at the random index.
			Player player = players.getRandomPlayer();

			// Holds the number of countries on the board.
			int numberOfCountries = board.getNumberOfCountries();

			// Holds the maximum countries this player can own so that all the players nd up
			// with the same number of countries.
			int maxCountries;

			/*
			 * If the number of countries on this board can be equally divided between the
			 * players then set the max number of countries of that a player can own to the
			 * equal amount for each player. Otherwise set the max number of countries of
			 * that a player can own to one above the normal proportion so the to account
			 * for the left over countries.
			 */
			if (numberOfCountries % numberOfPlayers == 0) {
				maxCountries = numberOfCountries / numberOfPlayers;
			} else {
				maxCountries = numberOfCountries / numberOfPlayers + 1;
			}

			// If the player owns more that their fair share of the maps countries assign
			// the player again.
			if (player.getCountriesRuled() < maxCountries) {
				set = true;
				country.setRuler(player);
				player.setCountriesRuled(player.getCountriesRuled() + 1);
				player.totalArmy.add(1);
			}

		}

	}

}
