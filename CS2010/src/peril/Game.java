package peril;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.board.Board;
import peril.board.Country;
import peril.io.MapReader;
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
	private final CombatState combatState;

	/**
	 * The {@link SetupState} that will allow the user to set up which
	 * {@link Player} owns which {@link Country}.
	 */
	private final SetupState setupState;

	/**
	 * The {@link ReinforcementState} that allows the {@link Player} to distribute
	 * their {@link Army} to the {@link Country}s they rule.
	 */
	private final ReinforcementState reinforcementState;

	/**
	 * The {@link MovementState} which lets the user move {@link Army}s from one
	 * {@link Country} to another.
	 */
	private final MovementState movementState;

	/**
	 * The {@link EndState} that displays the results of the {@link Game}.
	 */
	private final EndState endState;

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
	 * The {@link Thread} that run the the background tasks.
	 */
	private Thread background;

	/**
	 * The {@link AppGameContainer} that contains this {@link Game}.
	 */
	private AppGameContainer agc;

	/**
	 * Constructs a new {@link Game}.
	 * 
	 * @param ui
	 *            The {@link UserInterface} of the {@link Game}.
	 */
	private Game() {
		super("PERIL: A Turn Based Strategy Game");

		// Initialise the map reader and the players.
		this.mapReader = new MapReader(
				new File(System.getProperty("user.dir")).getPath() + File.separatorChar + BOARD_NAME);
		this.players = new Player[] { Player.PLAYERONE, Player.PLAYERTWO, Player.PLAYERTHREE, Player.PLAYERFOUR };

		// Set the game indexes to there initial values.
		this.currentPlayerIndex = 0;
		this.currentRound = 0;

		// Assign the game to run.
		this.endTurn = false;
		this.run = true;

		// Construct the board.
		this.board = new Board();

		// Initialise the game states.
		this.combatState = new CombatState(this);
		this.setupState = new SetupState(this);
		this.reinforcementState = new ReinforcementState(this);
		this.movementState = new MovementState(this);
		this.endState = new EndState(this);
		this.eventHandler = new UIEventHandler(this);

		// Construct and launch the game as a Slick2D state based game.
		try {
			agc = new AppGameContainer(this);
			mapReader.setAppGameContainerDimensions(agc);
			agc.setTargetFrameRate(60);
			agc.start();
		} catch (SlickException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		// Initialise and start the background thread.
		this.background = new Thread() {

			@Override
			public void run() {

				while (run) {
					// TODO Background tasks
				}
			}
		};
		this.background.start();

		// this.challenges = ChallengeReader.getChallenges(currentDirectory.getPath(),
		// "Earth");

	}

	/**
	 * Highlights a {@link Country} on the screen by adding it to the
	 * {@link CoreGameState} for the state to render.
	 * 
	 * @param country
	 *            {@link Country}
	 */
	public void highlight(Country country) {

		// Get the current state of the game.
		GameState state = super.getCurrentState();

		// Check that the state is a CoreGameState and if it is not throw an exception
		// as there is an invalid state in the game.
		if (state instanceof CoreGameState) {

			// Highlight the country
			((CoreGameState) state).highlight(country);
		} else {
			throw new IllegalStateException("The current state is not a CoreGameState.");
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

		// Assign Key and Mouse Listener as the UIEventhandler
		container.getInput().addKeyListener(eventHandler);
		container.getInput().addMouseListener(eventHandler);
	}

	/**
	 * Starts the UI and reads the Board.
	 */
	public void loadAssets() {
		mapReader.parseBoard(board);
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
	 * Executes the core game play loop.
	 */
	public void play() {

		// While the game is being played.
		while (run) {

			this.enterState(reinforcementState.getID());
			// TODO this.getCurrentState().display(getCurrentPlayer());

			// Go to next player.
			nextPlayer();

			// If the last player just had their turn.
			if (currentPlayerIndex == 0) {
				endRound();
			}

			checkChallenges(getCurrentPlayer());
		}

		displayWinner(getCurrentPlayer());

	}

	/**
	 * Performs the actions of the background loop. Any time the game thread is
	 * waiting for an for an {@link Action} to execute or to be executed, call this
	 * method.
	 */
	private void run() {
		// TODO Background stuff.
	}

	/**
	 * Creates an {@link Action} that displays the specified winning {@link Player}
	 * on the {@link UserInterface}. This {@link Action} is then passed to the
	 * {@link UserInterface} thread while this thread waits for it to be completed
	 * using {@link Action#isDone()}.
	 * 
	 * @param player
	 *            {@link Player}
	 */
	private void displayWinner(Player player) {

		// TODO display winning player

	}

	/**
	 * Creates an {@link Action} that displays the turn of the specified
	 * {@link Player} on the {@link UserInterface}. This {@link Action} is then
	 * passed to the {@link UserInterface} thread while this thread waits for it to
	 * be completed using {@link Action#isDone()}.
	 * 
	 * @param player
	 *            {@link Player}
	 */
	private void displayTurn(Player player) {
		// TODO display turn
	}

	/**
	 * Iterates thought all the available {@link Challenge}s to see if the specified
	 * {@link Player} has completed them or not.
	 * 
	 * @param currentPlayer
	 *            {@link Player}
	 */
	private void checkChallenges(Player currentPlayer) {

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
	 * Performs all the tasks that occur at the end of a round.
	 */
	private void endRound() {

		board.endRound();

		currentRound++;
	}

	/**
	 * Iterates to the next player.
	 */
	private void nextPlayer() {
		currentPlayerIndex = currentPlayerIndex++ % players.length;
	}

	/**
	 * Runs the game.
	 * 
	 * @param args
	 *            Unused.
	 */
	public static void main(String[] args) {

		// Create the instance of the game.
		Game game = new Game();
		game.play();

	}

}
