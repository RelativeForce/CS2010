package peril;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

import peril.board.Board;
import peril.io.MapReader;
import peril.io.ChallengeReader;
import peril.ui.UserInterface;

/**
 * Encapsulate the main game logic.
 * 
 * @author Joshua_Eddy
 *
 */
public class Game {

	/**
	 * Whether or not the background thread is running or not.
	 */
	public volatile boolean run;

	/**
	 * This {@link Queue} holds instructions stored as objects for the background
	 * thread to execute. This allows for an asynchronous execution resulting is a
	 * responsive {@link UserInterface}.
	 * 
	 * @see Queue
	 * @see LinkedTransferQueue
	 */
	private Queue<Action<?>> buffer;

	/**
	 * The instance of the {@link Board} used for this game.
	 */
	private Board board;

	/**
	 * The current turn of the {@link Game}. Initially zero;
	 */
	private int currentRound;

	/**
	 * Holds all the players in an iterable array.
	 */
	private final Player[] players;

	/**
	 * The {@link Player} who's turn it is.
	 */
	private int currentPlayerIndex;

	/**
	 * Contains all the objectives that a {@link Player} can attain in the game.
	 */
	private List<Challenge> objectives;

	/**
	 * Constructs a new {@link Game}.
	 */
	private Game() {

		this.currentPlayerIndex = 0;
		this.players = Player.values();
		this.currentRound = 0;
		this.buffer = new LinkedTransferQueue<>();
		this.run = true;

		// Read the Board and Objectives from the files.
		File currentDirectory = new File(System.getProperty("user.dir"));
		this.board = MapReader.getBoard(currentDirectory.getPath(), "Earth");
		this.objectives = ChallengeReader.getChallenges(currentDirectory.getPath(), "Earth");

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
	public int getTurnNumber() {
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
	 * Adds a {@link Action} to the background thread queue.
	 * 
	 * @param action
	 *            {@link Action} to perform.
	 */
	public void addAction(Action<?> action) {

		// Null check.
		if (action == null) {
			throw new IllegalArgumentException("Action cannot be null.");
		}

		buffer.add(action);

	}

	/**
	 * Starts the background {@link Game} loop that will end when {@link Game#run}
	 * is <code>false</code>.
	 */
	private void run() {

		// Background thread loop.
		while (run) {

			// If there is an item in the buffer.
			if (!buffer.isEmpty()) {
				buffer.poll().execute();
			}

			// TODO Run music and check for inactivity

		}
	}

	/**
	 * Parses a click on the {@link Game#board}.
	 * 
	 * @param click
	 *            A {@link Point} on the {@link Game#board} that the player clicked.
	 *            If the {@link Point} is outside the {@link Board} bounds this
	 *            throws an {@link IllegalArgumentException}.
	 */
	public void parseInput(Point click) {
		// TODO Parse click
	}

	/**
	 * Executes a turn for the {@link Player} specified by
	 * {@link Game#currentPlayerIndex}.
	 * 
	 * @param player
	 *            {@link Player}.
	 */
	public void executeTurn() {

		Player currentPlayer = players[currentPlayerIndex];

		// TODO turn operations.

		// Iterate though all the objectives to see if the the current player has
		// completed them.
		objectives.forEach(objective -> objective.hasCompleted(currentPlayer));

		nextPlayer();

		/**
		 * If the last player just had their turn.
		 */
		if (currentPlayerIndex == 0) {
			endRound();
		}
	}

	/**
	 * Parses a key press on the {@link Game#board}.
	 * 
	 * @param code
	 *            A integer representation of a key using
	 *            {@link org.lwjgl.input.Keyboard}. If the key pressed in not valid
	 *            this method does nothing.
	 */
	public void parseInput(int code) {
		// TODO Parse input
	}

	/*
	 * Performs all the tasks that occur at the end of a round.
	 */
	private void endRound() {
		// TODO end round operations.

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

		// Create the instance of the user interface.
		UserInterface ui = new UserInterface(game);

		// Start the background thread.
		game.run();
	}

}
