package peril;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.board.Army;
import peril.board.Board;
import peril.board.Country;
import peril.io.AssetReader;
import peril.io.ChallengeReader;
import peril.io.MapReader;
import peril.io.MusicReader;
import peril.ui.UIEventHandler;
import peril.ui.states.InteractiveState;
import peril.ui.states.gameStates.*;
import peril.ui.states.gameStates.multiSelectState.CombatState;
import peril.ui.states.gameStates.multiSelectState.MovementState;
import peril.ui.states.menuStates.MainMenuState;

/**
 * Encapsulate the main game logic for Peril. This also extends
 * {@link StateBasedGame}.
 * 
 * @author Joshua_Eddy
 *
 */
public class Game extends StateBasedGame implements MusicListener{

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
	public final CombatState combat;

	/**
	 * The {@link MainMenuState} of the {@link Game}.
	 */
	public final MainMenuState mainMenu;

	/**
	 * The {@link SetupState} that will allow the user to set up which
	 * {@link Player} owns which {@link Country}.
	 */
	public final SetupState setup;

	/**
	 * The {@link ReinforcementState} that allows the {@link Player} to distribute
	 * their {@link Army} to the {@link Country}s they rule.
	 */
	public final ReinforcementState reinforcement;

	/**
	 * The {@link MovementState} which lets the user move {@link Army}s from one
	 * {@link Country} to another.
	 */
	public final MovementState movement;

	/**
	 * The {@link EndState} that displays the results of the {@link Game}.
	 */
	public final EndState end;

	/**
	 * The {@link UIEventHandler} that processes all of the user inputs and triggers
	 * the appropriate operations.
	 */
	private final UIEventHandler eventHandler;

	/**
	 * The {@link CombatHandler} that processes all of the game's combat.
	 */
	private final CombatHandler combatHandler;

	/**
	 * The instance of the {@link Board} used for this game.
	 */
	private Board board;

	/**
	 * The current turn of the {@link Game}. Initially zero;
	 */
	private volatile int currentRound;

	/**
	 * Holds all the {@link Player}s in this {@link Game}.
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

	public final MusicReader musicHelper;

	private String mapsDirectory;

	/**
	 * Constructs a new {@link Game}.
	 */
	private Game() {
		super("PERIL: A Turn Based Strategy Game");

		// Initialise the the players array.
		this.players = new Player[] { Player.PLAYERONE, Player.PLAYERTWO, Player.PLAYERTHREE, Player.PLAYERFOUR };

		Player.PLAYERONE.setDistributableArmySize(3);

		// Set the game indexes to there initial values.
		this.currentPlayerIndex = 0;
		this.currentRound = 0;

		// Assign the game to run.
		this.endTurn = false;
		this.run = true;
		this.isLoaded = false;

		// Construct the board.
		this.board = new Board(this);

		// Initialise the game states.

		this.setup = new SetupState(this, 1);
		this.reinforcement = new ReinforcementState(this, 2);
		this.combat = new CombatState(this, 3);
		this.movement = new MovementState(this, 4);
		this.end = new EndState(this, 5);

		// Initialise the event handler.
		this.eventHandler = new UIEventHandler(this);

		// Initialise games combatHandler
		this.combatHandler = new CombatHandler();

		// Holds the directory this game is operating in.
		String baseDirectory = new File(System.getProperty("user.dir")).getPath();

		// Create the ui_assets file path
		StringBuilder ui_assestsPath = new StringBuilder(baseDirectory);
		ui_assestsPath.append(File.separatorChar);
		ui_assestsPath.append("ui_assets");

		this.assetReader = new AssetReader(new InteractiveState[] { combat, setup, reinforcement, movement, end },
				ui_assestsPath.toString());

		// Create the map file path
		StringBuilder game_assetsPath = new StringBuilder(baseDirectory);
		game_assetsPath.append(File.separatorChar);
		game_assetsPath.append("game_assets");

		this.challengeReader = new ChallengeReader(this, game_assetsPath.toString());

		StringBuilder musicPath = new StringBuilder(game_assetsPath);
		musicPath.append(File.separatorChar);
		musicPath.append("music");

		this.musicHelper = new MusicReader(musicPath.toString(), this);

		game_assetsPath.append(File.separatorChar);
		game_assetsPath.append("maps");

		this.mainMenu = new MainMenuState(this, 0, game_assetsPath.toString());

		// Construct the container for the game as a Slick2D state based game. And parse
		// the details of the map from the maps file.
		try {
			agc = new AppGameContainer(this);
			agc.setDisplayMode(300, 200, false);

		} catch (SlickException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		// Add the path to the map's folder
		game_assetsPath.append(File.separatorChar);
		mapsDirectory = game_assetsPath.toString();

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

		// Add starting state to the game container.
		super.addState(mainMenu);

		// Add all other states to game container.
		super.addState(setup);
		super.addState(reinforcement);
		super.addState(combat);
		super.addState(movement);
		super.addState(end);

		// Assign Key and Mouse Listener as the UIEventhandler
		container.getInput().addKeyListener(eventHandler);
		container.getInput().addMouseListener(eventHandler);

		// Hide FPS counter
		container.setShowFPS(false);
		container.setVSync(true);
	}

	/**
	 * Starts the UI and reads the Board.
	 * 
	 * @throws SlickException
	 */
	public void loadAssets(String mapName, int width, int height) throws SlickException {

		// If the assests are not already loaded
		if (!isLoaded) {

			agc.setDisplayMode(width, height, false);
			// Initialise the map reader and the players array.
			this.mapReader = new MapReader(mapsDirectory + mapName, board);

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
	 * Retrieves the combat handler.
	 * 
	 * @return <code>CombatHandler</code>
	 */
	public CombatHandler getCombatHandler() {
		return combatHandler;
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

		if (getRoundNumber() > 0) {
			checkChallenges(getCurrentPlayer());
		}

		if (getCurrentPlayer().getCountriesRuled() <= 11) {
			getCurrentPlayer().setDistributableArmySize(getCurrentPlayer().getDistributableArmySize() + 3);
		} else {
			getCurrentPlayer().setDistributableArmySize(
					getCurrentPlayer().getDistributableArmySize() + (getCurrentPlayer().getCountriesRuled() / 3));
		}

		if (currentPlayerIndex == 0) {
			endRound();
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
	 * Runs the game.
	 * 
	 * @param args
	 *            Unused.
	 */
	public static void main(String[] args) {

		// Create the instance of the game.
		new Game();

	}

	@Override
	public void musicEnded(Music previousMusic) {
		
		Music stateMusic = getCurrentState().getMusic();
		
		if(stateMusic != null) {
			stateMusic.play();
		}
		
	}

	@Override
	public void musicSwapped(Music music1, Music music2) {

	}


}
