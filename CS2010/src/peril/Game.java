package peril;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

import peril.board.Board;
import peril.io.MapReader;
import peril.multiThread.Action;
import peril.multiThread.ProcessTransfer;
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
	 * Whether the game is running or not.
	 */
	public volatile boolean run;

	/**
	 * Whether or not the current users turn is over or not.
	 */
	public volatile boolean endTurn;

	/**
	 * {@link ProcessTransfer#getInstane()}
	 */
	private ProcessTransfer processTransfer;

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
	private List<Challenge> challenges;

	/**
	 * The {@link UserInterface} for the {@link Game}.
	 */
	private UserInterface ui;

	/**
	 * Constructs a new {@link Game}.
	 * 
	 * @param ui
	 *            The {@link UserInterface} of the {@link Game}.
	 */
	private Game() {

		this.currentPlayerIndex = 0;
		this.players = Player.values();
		this.currentRound = 0;
		this.processTransfer = ProcessTransfer.getInstane();
		this.endTurn = false;
		this.run = true;

		// Read the Board and Objectives from the files.
		File currentDirectory = new File(System.getProperty("user.dir"));
		this.board = MapReader.getBoard(currentDirectory.getPath(), "Earth");
		this.challenges = ChallengeReader.getChallenges(currentDirectory.getPath(), "Earth");

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
	 * Executes the core game play loop.
	 */
	public void play() {

		ui = new UserInterface(this);

		Player currentPlayer = players[currentPlayerIndex];
		// While the game is being played.
		while (run) {

			currentPlayer = players[currentPlayerIndex];

			displayTurn(currentPlayer);

			executeTurn(currentPlayer);

			// Go to next player.
			nextPlayer();

			// If the last player just had their turn.
			if (currentPlayerIndex == 0) {
				endRound();
			}
		}

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

		// Display the winner to the user.
		Action<UserInterface> action = new Action<>(ui, ui -> ui.displayWinner(player));

		// Transfer the action to the ui.
		processTransfer.transfer(action);

		// Wait for winner to be displayed.
		while (!action.isDone() && run) {
			run();
		}

	}

	/**
	 * Executes a turn for a specified {@link Player}.
	 * 
	 * @param player
	 *            {@link Player}.
	 */
	private void executeTurn(Player player) {

		// While the user has his turn.
		while (!endTurn && run) {

			while (processTransfer.isEmpty() && run) {
				// Wait for the player to perform an action.
				run();
			}

			// Execute the users command.
			processTransfer.poll();

		}

		endTurn = false;

		// Check challenge completion.
		checkChallenges(player);

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

		// Display the turn to the user.
		Action<UserInterface> action = new Action<>(ui, ui -> ui.displayTurn(player));

		// Transfer the action to the ui.
		processTransfer.transfer(action);

		// Wait for the ui to display the turn to the player.
		while (!action.isDone() && run) {
			run();
		}
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

	/*
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
