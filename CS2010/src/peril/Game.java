package peril;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
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
import peril.io.fileParsers.AssetReader;
import peril.io.fileParsers.ChallengeReader;
import peril.io.fileReaders.MusicReader;
import peril.ui.Container;
import peril.ui.UIEventHandler;
import peril.ui.components.menus.PauseMenu;
import peril.ui.components.menus.WarMenu;
import peril.ui.states.InteractiveState;
import peril.ui.states.LoadingScreen;
import peril.ui.states.gameStates.*;
import peril.ui.states.gameStates.multiSelectState.CombatState;
import peril.ui.states.gameStates.multiSelectState.MovementState;
import peril.ui.states.menuStates.EndState;
import peril.ui.states.menuStates.MainMenuState;

/**
 * Encapsulate the main game logic for Peril. This also extends
 * {@link StateBasedGame}.
 * 
 * @author Joshua_Eddy
 *
 */
public class Game extends StateBasedGame implements MusicListener {

	/**
	 * Whether the game is running or not.
	 */
	public volatile boolean run;

	/**
	 * Whether or not the current users turn is over or not.
	 */
	public volatile boolean endTurn;

	/**
	 * The {@link PauseMenu} that will be used by all the {@link CoreGameState}s.
	 */
	public final PauseMenu pauseMenu;

	/**
	 * Holds the path to the directory containing the maps file.
	 */
	public final String mapsDirectory;

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
	 * The instance of the {@link Board} used for this game.
	 */
	public final Board board;

	/**
	 * The {@link WarMenu} that processes all of the game's combat.
	 */
	private final WarMenu warMenu;

	/**
	 * The current turn of the {@link Game}. Initially zero;
	 */
	private volatile int currentRound;

	/**
	 * Holds all the {@link Player}s in this {@link Game}.
	 */
	private List<Player> players;

	/**
	 * The {@link Player} who's turn it is.
	 */
	private volatile int currentPlayerIndex;

	/**
	 * Contains all the objectives that a {@link Player} can attain in the game.
	 */
	private List<Challenge> challenges;

	/**
	 * The {@link AppGameContainer} that contains this {@link Game}.
	 */
	private AppGameContainer agc;

	/**
	 * Constructs a new {@link Game}.
	 */
	private Game() {
		super("PERIL: A Turn Based Strategy Game");

		// Set the game indexes to there initial values.
		this.currentPlayerIndex = 0;
		this.currentRound = 0;

		// Assign the game to run.
		this.endTurn = false;
		this.run = true;

		// Construct the board.
		this.board = new Board(this);

		// Construct the container for the game as a Slick2D state based game. And parse
		// the details of the map from the maps file.
		try {
			agc = new AppGameContainer(this);
			agc.setDisplayMode(400, 300, false);
		} catch (SlickException e) {
			e.printStackTrace();
		}

		// Initialise games Combat Handler
		this.warMenu = new WarMenu(new Point(100, 100), this);

		// Initialise the pause menu all the states will use
		this.pauseMenu = new PauseMenu(new Point(100, 100), this);

		LoadingScreen loadingScreen = new LoadingScreen(this, 6);

		// Initialise the game states.
		SetupState setup = new SetupState(this, 1, pauseMenu);
		ReinforcementState reinforcement = new ReinforcementState(this, 2, pauseMenu);
		CombatState combat = new CombatState(this, 3, pauseMenu, warMenu);
		MovementState movement = new MovementState(this, 4, pauseMenu);
		EndState end = new EndState(this, 5);

		// Holds the directory this game is operating in.
		String baseDirectory = new File(System.getProperty("user.dir")).getPath();

		// Create the map file path
		StringBuilder game_assetsPath = new StringBuilder(baseDirectory);
		game_assetsPath.append(File.separatorChar);
		game_assetsPath.append("game_assets");

		ChallengeReader challengeLoader = new ChallengeReader(this, game_assetsPath.toString());

		StringBuilder musicPath = new StringBuilder(game_assetsPath);
		musicPath.append(File.separatorChar);
		musicPath.append("music");

		MusicReader musicHelper = new MusicReader(musicPath.toString(), this);

		game_assetsPath.append(File.separatorChar);
		game_assetsPath.append("maps");

		MainMenuState mainMenu = new MainMenuState(this, 0, game_assetsPath.toString());

		// Create the ui_assets file path
		StringBuilder ui_assestsPath = new StringBuilder(baseDirectory);
		ui_assestsPath.append(File.separatorChar);
		ui_assestsPath.append("ui_assets");

		Container[] containers = new Container[] { pauseMenu, loadingScreen, warMenu, mainMenu, combat, setup,
				reinforcement, movement, end };

		AssetReader gameLoader = new AssetReader(containers, ui_assestsPath.toString(), "game.txt", this);

		AssetReader mainMenuLoader = new AssetReader(containers, ui_assestsPath.toString(), "menu.txt", this);

		// Add the path to the map's folder
		game_assetsPath.append(File.separatorChar);
		mapsDirectory = game_assetsPath.toString();

		this.states = new StateHelper(mainMenu, combat, reinforcement, setup, movement, end, loadingScreen);
		this.io = new IOHelper(gameLoader, musicHelper, mainMenuLoader, challengeLoader);
		// Start the display.
		try {
			agc.setTargetFrameRate(60);
			agc.start();
		} catch (SlickException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Adds {@link CoreGameState}s to the {@link GameContainer} for this
	 * {@link Game}.
	 */
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		states.initGame(container, this, new UIEventHandler(this));
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
		if (width == agc.getScreenWidth() && height == agc.getScreenHeight()) {
			agc.setDisplayMode(width, height, true);
		} else {

			agc.setDisplayMode(width, height, false);
		}

		int pauseMenuX = (width / 2) - (pauseMenu.getWidth() / 2);
		int pauseMenuY = (height / 2) - (pauseMenu.getHeight() / 2);

		pauseMenu.setPosition(new Point(pauseMenuX, pauseMenuY));

		// Reset the board
		board.reset();

	}

	/**
	 * Distributes the countries between the {@link Game#players} equally.
	 */
	public void autoDistributeCountries() {

		Random rand = new Random();

		// Reset the all players number of countries to zero.
		for (Player player : players) {
			player.setCountriesRuled(0);
			player.setTotalArmySize(0);
		}

		// Iterate through each country on the board.
		board.getContinents().forEach(continent -> continent.getCountries().forEach(country -> {

			assignPlayer(country, rand);

		}));

	}

	/**
	 * Retrieves the {@link Player} who's current turn it is.
	 * 
	 * @return {@link Player}
	 */
	public Player getCurrentPlayer() {

		if (players == null) {
			throw new NullPointerException("List of playing Players is null!");
		}

		return players.get(currentPlayerIndex);
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
	 * Iterates to the next player.
	 */
	public void nextPlayer() {

		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

		if (getRoundNumber() > 0) {
			checkChallenges(getCurrentPlayer());
		}

		reinforce(getCurrentPlayer());

		if (currentPlayerIndex == 0) {
			endRound();
		}
	}

	/**
	 * Gives the specified {@link Player} reinforcements based on the number of
	 * countries they own.
	 * 
	 * @param player
	 *            {@link Player}
	 */
	public void reinforce(Player player) {

		if (player.getCountriesRuled() < 12) {
			player.setDistributableArmySize(player.getDistributableArmySize() + 3);
		} else {
			player.setDistributableArmySize(player.getDistributableArmySize() + (player.getCountriesRuled() / 3));
		}
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
	 * Iterates thought all the available {@link Challenge}s to see if the specified
	 * {@link Player} has completed them or not.
	 * 
	 * @param currentPlayer
	 *            {@link Player}
	 */
	public void checkChallenges(Player currentPlayer) {

		// Holds the completed challenges
		List<Challenge> toRemove = new LinkedList<>();

		// Iterate though all the objectives to see if the the current player has
		// completed them.
		for (Challenge challenge : challenges) {

			// If the current player has completed the challenge remove it from the list of
			// available challenges.
			if (challenge.hasCompleted(currentPlayer, board)) {
				toRemove.add(challenge);

				if (getCurrentState() instanceof CoreGameState) {
					((CoreGameState) getCurrentState()).show(challenge);
				}
			}
		}

		// Remove the completed challenges.
		toRemove.forEach(challenge -> challenges.remove(challenge));
	}

	/**
	 * Set the {@link List} of {@link Challenge}s for this {@link Game}.
	 * 
	 * @param challenges
	 *            {@link List} NOT NULL
	 */
	public void setChallenges(List<Challenge> challenges) {

		if (challenges == null) {
			throw new NullPointerException("Challenge list cannot be null.");
		}

		this.challenges = challenges;
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
	 * Assigns an array of {@link Player} to this {@link Game}.
	 * 
	 * @param players
	 *            {@link Player}s in the {@link Game}. NOT EMPTY
	 */
	public void setPlayers(List<Player> players) {

		if (players == null || players.isEmpty()) {
			throw new IllegalArgumentException("players array cannot be empty");
		}

		this.players = players;
		this.currentPlayerIndex = 0;
	}

	/**
	 * Retrieves whether or not a specified player is in this {@link Game}.
	 * 
	 * @param player
	 *            {@link Player}
	 * @return <code>boolean</code>
	 */
	public boolean isPlaying(Player player) {

		// Iterate through all the players in the game.
		for (Player currentPlayer : players) {

			// If the player is playing then return true.
			if (currentPlayer.equals(player)) {
				return true;
			}
		}

		return false;
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
	 * Sets a specified {@link Player} as a loser which removes it from the
	 * {@link Game#players} and adds it to the podium in the {@link EndState}.
	 * 
	 * @param player
	 *            {@link Player} that has lost.
	 */
	public void setLoser(Player player) {

		// If the loser player is before the current player in the list, reduce the
		// player index to account for the player being removed and the list's size
		// changing.
		if (this.currentPlayerIndex > players.indexOf(player)) {
			this.currentPlayerIndex--;
		}

		// Add the player to the podium and remove it from the players in play.
		states.end.addPlayerToPodium(player);
		players.remove(player);

	}

	/**
	 * Checks if there is only one {@link Player} in play. If this is the case then
	 * that {@link Player} has won.
	 */
	public void checkWinner() {
		if (players.size() == 1) {
			states.end.addPlayerToPodium(players.get(0));
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
	 * Assigns a {@link Player} ruler to a {@link Country} using a parameter
	 * {@link Random}.
	 * 
	 * @param country
	 *            {@link Country} that is to be ruled.
	 * @param rand
	 *            {@link Random}
	 */
	private void assignPlayer(Country country, Random rand) {

		// Holds whether the country has assigned a player ruler.
		boolean set = false;

		while (!set) {

			// Get the player at the random index.
			Player player = players.get(rand.nextInt(players.size()));

			// Holds the number of countries on the board.
			int numberOfCountries = board.getNumberOfCountries();

			// Holds the number of players in the game.
			int numberOfPlayers = players.size();

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
				player.setTotalArmySize(player.getTotalArmySize() + 1);
			}

		}

	}

	/**
	 * Performs all the tasks that occur at the end of a round.
	 */
	private void endRound() {
		board.endRound();
		currentRound++;
	}

}
