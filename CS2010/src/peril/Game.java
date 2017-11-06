package peril;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.board.Army;
import peril.board.Board;
import peril.board.Country;
import peril.io.AssetReader;
import peril.io.ChallengeReader;
import peril.io.MapReader;
import peril.io.TextFileReader;
import peril.ui.UIEventHandler;
import peril.ui.states.*;

/**
 * Encapsulate the main game logic for Peril. This also extends
 * {@link StateBasedGame}.
 * 
 * @author Joshua_Eddy
 *
 */
public class Game extends StateBasedGame {

	/**
	 * The name of the current map, this will be used to locate all the map
	 * resources.
	 */
	public static final String BOARD_NAME = "Europe";

	/**
	 * Whether the game is running or not.
	 */
	public volatile boolean run;

	/**
	 * Whether or not the current users turn is over or not.
	 */
	public volatile boolean endTurn;

	/**
	 * The state that displays combat to the user. This is heavily couples with
	 * {@link CombatHandler}.
	 */
	public final CombatState combatState;

	/**
	 * The {@link SetupState} that will allow the user to set up which
	 * {@link Player} owns which {@link Country}.
	 */
	public final SetupState setupState;

	/**
	 * The {@link ReinforcementState} that allows the {@link Player} to distribute
	 * their {@link Army} to the {@link Country}s they rule.
	 */
	public final ReinforcementState reinforcementState;

	/**
	 * The {@link MovementState} which lets the user move {@link Army}s from one
	 * {@link Country} to another.
	 */
	public final MovementState movementState;

	/**
	 * The {@link EndState} that displays the results of the {@link Game}.
	 */
	public final EndState endState;

	/**
	 * The {@link UIEventHandler} that processes all of the user inputs and triggers
	 * the appropriate operations.
	 */
	private final UIEventHandler eventHandler;

	/**
	 * The instance of the {@link Board} used for this game.
	 */
	private Board board;

	/**
	 * The current turn of the {@link Game}. Initially zero;
	 */
	private volatile int currentRound;

	/**
	 * Holds all the players in an iterable array.
	 */
	private final Player[] players;

	/**
	 * Whether all the assets are loaded or not.
	 */
	private volatile boolean isLoaded;

	/**
	 * The {@link Player} who's turn it is.
	 */
	private volatile int currentPlayerIndex;

	/**
	 * Contains all the objectives that a {@link Player} can attain in the game.
	 */
	private List<Challenge> challenges;

	/**
	 * The {@link MapReader} that reads the {@link Board} from external memory.
	 */
	private MapReader mapReader;

	/**
	 * The {@link AppGameContainer} that contains this {@link Game}.
	 */
	private AppGameContainer agc;

	/**
	 * The {@link AssetReader} that loads all the {@link CoreGameState} states
	 * buttons into the game from memory.
	 */
	private AssetReader assetReader;

	/**
	 * {@link ChallengeReader} that loads the {@link Challenge}s from memory.
	 */
	private ChallengeReader challengeReader;

	/**
	 * Constructs a new {@link Game}.
	 */
	private Game() {
		super("PERIL: A Turn Based Strategy Game");

		// Initialise the the players array.
		this.players = new Player[] { Player.PLAYERONE, Player.PLAYERTWO, Player.PLAYERTHREE, Player.PLAYERFOUR };

		Player.PLAYERONE.award(new Army(5));

		// Set the game indexes to there initial values.
		this.currentPlayerIndex = 0;
		this.currentRound = 0;

		// Assign the game to run.
		this.endTurn = false;
		this.run = true;
		this.isLoaded = false;

		// Construct the board.
		this.board = new Board();

		// this.challenges = ChallengeReader.getChallenges(currentDirectory.getPath(),
		// "Earth");

		// Initialise the game states.
		this.combatState = new CombatState(this);
		this.setupState = new SetupState(this);
		this.reinforcementState = new ReinforcementState(this);
		this.movementState = new MovementState(this);
		this.endState = new EndState(this);
		this.eventHandler = new UIEventHandler(this);

		// Holds the directory this game is operating in.
		String baseDirectory = new File(System.getProperty("user.dir")).getPath();

		// Create the ui_assets file path
		StringBuilder assestsPath = new StringBuilder(baseDirectory);
		assestsPath.append(File.separatorChar);
		assestsPath.append("ui_assets");

		this.assetReader = new AssetReader(
				new CoreGameState[] { combatState, setupState, reinforcementState, movementState, endState },
				assestsPath.toString());

		// Create the map file path
		StringBuilder mapPath = new StringBuilder(baseDirectory);
		mapPath.append(File.separatorChar);
		mapPath.append("game_assets");

		this.challengeReader = new ChallengeReader(this, mapPath.toString());

		mapPath.append(File.separatorChar);
		mapPath.append("maps");

		// Construct the container for the game as a Slick2D state based game. And parse
		// the details of the map from the maps file.
		try {
			agc = new AppGameContainer(this);
			setAppGameContainerDimensions(mapPath.toString());

		} catch (SlickException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		// Add the path to the map's folder
		mapPath.append(File.separatorChar);
		mapPath.append(BOARD_NAME);

		// Initialise the map reader and the players array.
		this.mapReader = new MapReader(mapPath.toString(), board);

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

		// Add all the game states to the game to the game container.
		super.addState(setupState);
		super.addState(combatState);
		super.addState(reinforcementState);
		super.addState(movementState);
		super.addState(endState);
		this.enterState(setupState.getID());

		// Assign Key and Mouse Listener as the UIEventhandler
		container.getInput().addKeyListener(eventHandler);
		container.getInput().addMouseListener(eventHandler);

		// Hide FPS counter
		container.setShowFPS(false);
		container.setVSync(true);
	}

	/**
	 * Starts the UI and reads the Board.
	 */
	public void loadAssets() {

		// If the assests are not already loaded
		if (!isLoaded) {
			mapReader.read();
			assetReader.read();
			challengeReader.read();
			isLoaded = true;
		}
	}

	/**
	 * Retrieves the {@link Player} who's current turn it is.
	 * 
	 * @return {@link Player}
	 */
	public Player getCurrentPlayer() {
		return players[currentPlayerIndex];
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
	 * Retrieves the {@link Board} in <code>this</code> {@link Game}.
	 * 
	 * @return {@link Board}.
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Iterates to the next player.
	 */
	public void nextPlayer() {
		currentPlayerIndex = (currentPlayerIndex + 1) % players.length;

		// Calculate and set player's number of distributable reinforcements.
		for (Player player : players) {
			if (player.getCountriesRuled() <= 11) {
				player.setDistributableArmySize(3);
			} else {
				player.setDistributableArmySize(player.getCountriesRuled() / 3);
			}
		}

		if (currentPlayerIndex == 0) {
			endRound();
		}
	}

	@Override
	public CoreGameState getCurrentState() {

		// Holds the current game state.
		GameState state = super.getCurrentState();

		// If the current state is a CoreGameState return it as a CoreGameState
		if (state instanceof CoreGameState) {
			return (CoreGameState) state;
		}
		throw new IllegalStateException(state.getID() + " is not a valid state as it is not a CoreGameState.");
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
	 * Performs all the tasks that occur at the end of a round.
	 */
	private void endRound() {

		board.endRound();
		currentRound++;
	}

	/**
	 * Reads the maps file and assigns the width and height of the window based on
	 * the data stored in this file. This method exists due to the fact that Slick2D
	 * is single threaded and does not allow the processing of images before the
	 * window is loaded.
	 * 
	 * @param mapsFilePath
	 *            The file path of the folder the maps file is inside.
	 * @throws SlickException
	 *             Thrown by
	 *             {@link AppGameContainer#setDisplayMode(int, int, boolean)}
	 */
	private void setAppGameContainerDimensions(String mapsFilePath) throws SlickException {

		// Iterate through each line in the maps file.
		for (String line : TextFileReader.scanFile(mapsFilePath, "maps.txt")) {

			// Split the line by the commas
			String[] details = line.split(",");

			// Holds the maps name.
			String mapName;

			// Parse the map name. If it is invalid throw the appropriate error.
			try {
				mapName = details[0];
			} catch (Exception e) {
				throw new IllegalArgumentException("The map name is not present.");
			}

			// If the map's name is the same as the boards name.
			if (mapName.equals(BOARD_NAME)) {

				// Holds the dimensions of the map.
				int width;
				int height;

				// Parse the dimensions of the map and if they are invalid throw the appropriate
				// exception.
				try {
					width = Integer.parseInt(details[1]);

					if (width <= 0) {
						throw new IllegalArgumentException("Width must be greater than zero.");
					}

				} catch (Exception e) {
					throw new IllegalArgumentException("Width of the map is not a valid integer.");
				}

				try {
					height = Integer.parseInt(details[2]);

					if (height <= 0) {
						throw new IllegalArgumentException("Height must be greater than zero.");
					}

				} catch (Exception e) {
					throw new IllegalArgumentException("Height of the map is not a valid integer.");
				}

				// If the dimensions are valid assign them to the window.
				agc.setDisplayMode(width, height, false);
				return;
			}

		}

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
}
