package peril;

import java.io.File;
import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.board.Board;
import peril.board.Continent;
import peril.board.Country;
import peril.board.EnvironmentalHazard;
import peril.helpers.*;
import peril.ui.Container;
import peril.ui.UIEventHandler;
import peril.ui.components.menus.*;
import peril.ui.states.*;
import peril.ui.states.gameStates.*;
import peril.ui.states.gameStates.multiSelectState.*;
import peril.ui.states.menuStates.*;

/**
 * Encapsulate the main game logic for Peril. This also extends
 * {@link StateBasedGame}.
 * 
 * @author Joshua_Eddy
 *
 */
public class Game extends StateBasedGame implements MusicListener {

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

	public final MenuHelper menus;
	
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

		// Set the initial round to zero
		this.currentRound = 0;

		// Construct the board with the initial name.
		this.board = new Board(this, "NOT ASSIGNED");

		// Initialise games overlay menus
		WarMenu warMenu = new WarMenu(new Point(100, 100), this);
		PauseMenu pauseMenu = new PauseMenu(new Point(100, 100), this);
		HelpMenu helpMenu = new HelpMenu(this, new Point(100, 100), 300, 300);

		this.menus = new MenuHelper(pauseMenu, warMenu, helpMenu);

		// Initialise the game states.
		MainMenuState mainMenu = new MainMenuState(this, 0);
		SetupState setup = new SetupState(this, 1);
		ReinforcementState reinforcement = new ReinforcementState(this, 2);
		CombatState combat = new CombatState(this, 3);
		MovementState movement = new MovementState(this, 4);
		EndState end = new EndState(this, 5);
		LoadingScreen loadingScreen = new LoadingScreen(this, 6);

		// Set the containers that ui elements will be loaded into.
		Container[] containers = new Container[] { helpMenu, pauseMenu, loadingScreen, warMenu, mainMenu, combat, setup,
				reinforcement, movement, end };

		this.states = new StateHelper(mainMenu, combat, reinforcement, setup, movement, end, loadingScreen);

		this.io = new IOHelper(this, containers);

		this.players = new PlayerHelper(this);

		// Construct the container for the game as a Slick2D state based game.
		try {
			agc = new AppGameContainer(this);
			agc.setDisplayMode(400, 300, false);
			agc.setTargetFrameRate(60);
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
		states.initGame(container, this, new UIEventHandler(this));
		EnvironmentalHazard.initIcons(assets.ui);
		players.init();
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

		players.resetAll();

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

		// If the current state is a CoreGameState return it as a CoreGameState
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
	 * When the currently playing {@link Music} has finished, start the
	 * {@link Music} of the {@link Game#getCurrentState()} if the
	 * {@link InteractiveState} has {@link Music}.
	 */
	@Override
	public void musicEnded(Music previousMusic) {

		Music stateMusic = getCurrentState().getMusic();

		if (stateMusic != null) {
			stateMusic.play();
		}

	}

	/**
	 * When the {@link Music} playing in this {@link Game} has changed.
	 */
	@Override
	public void musicSwapped(Music music1, Music music2) {

	}

	/**
	 * Checks all the {@link Continent}s on the {@link Board} to see if they are
	 * ruled. This is o(n^2) complexity
	 */
	public void checkContinentRulership() {

		players.forEach(player -> player.setContinentsRuled(0));

		board.getContinents().forEach(continent -> {

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
		if (state) {
			getContainer().setMusicVolume(1f);
		} else {
			getContainer().setMusicVolume(0f);
		}
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
