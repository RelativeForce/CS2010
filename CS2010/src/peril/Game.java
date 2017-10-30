package peril;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import peril.board.Board;
import peril.io.MapReader;
import peril.multiThread.Action;
import peril.multiThread.ProcessTransfer;
import peril.ui.UserInterface;

/**
 * Encapsulate the main game logic.
 * 
 * @author Joshua_Eddy
 *
 */
public class Game {

	/**
	 * The name of the current map, this will be used to locate all the map
	 * resources.
	 */
	private static final String BOARD_NAME = "Europe";

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
	 * The {@link UserInterface} for the {@link Game}.
	 */
	private UserInterface ui;
	
	private MapReader mapReader;

	private Thread background;

	private AppGameContainer agc;

	/**
	 * Constructs a new {@link Game}.
	 * 
	 * @param ui
	 *            The {@link UserInterface} of the {@link Game}.
	 */
	private Game() {

		this.mapReader = new MapReader(new File(System.getProperty("user.dir")).getPath() + File.separatorChar + BOARD_NAME) ;
		this.currentPlayerIndex = 0;
		this.players = new Player[] { Player.PLAYERONE, Player.PLAYERTWO, Player.PLAYERTHREE, Player.PLAYERFOUR };
		this.currentRound = 0;
		this.processTransfer = ProcessTransfer.getInstane();
		this.endTurn = false;
		this.run = true;
		this.board = new Board();
		this.ui = new UserInterface(this);

		try {
			agc = new AppGameContainer(ui);
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
					if (!processTransfer.isEmpty()) {
						processTransfer.poll();
					}
				}
			}
		};
		this.background.start();

		// this.challenges = ChallengeReader.getChallenges(currentDirectory.getPath(),
		// "Earth");

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

			displayTurn(getCurrentPlayer());

			executeTurn(getCurrentPlayer());

			// Go to next player.
			nextPlayer();

			// If the last player just had their turn.
			if (currentPlayerIndex == 0) {
				endRound();
			}
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

		// While the user has his turn. This loop end when the action that is added to
		// the queue by the ui sets endTurn to true.
		while (!endTurn && run) {

			executeAction();

		}

		endTurn = false;

		// Check challenge completion.
		checkChallenges(player);

	}

	/**
	 * Waits until the {@link Player} performs a {@link Action} on the
	 * {@link UserInterface} and it is added to the queue of {@link Action}s to be
	 * executed. When one is added it is executed.
	 */
	private void executeAction() {

		while (processTransfer.isEmpty() && run) {
			// Wait for the player to perform an action.
			run();
		}

		// Execute the users command.
		processTransfer.poll();

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
