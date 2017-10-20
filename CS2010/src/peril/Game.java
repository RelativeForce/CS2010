package peril;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

import peril.board.Board;
import peril.io.MapReader;
import peril.ui.UserInterface;

/**
 * Encapsulate the main game logic. Using the Singleton pattern.
 * 
 * @author Joshua_Eddy
 *
 */
public class Game {

	public Queue<Object> buffer;

	public volatile boolean run;

	private Board board;

	/**
	 * Constructs a new {@link Game}.
	 */
	private Game() {

		this.buffer = new LinkedTransferQueue<>();
		this.run = true;

		// Read the Board from the files.
		File currentDirectory = new File(System.getProperty("user.dir"));
		this.board = MapReader.getBoard(currentDirectory.getPath(), "Earth");

	}

	/**
	 * Starts the background {@link Game} loop that will end when {@link Game#run}
	 * is <code>false</code>.
	 */
	private void run() {
		while (run) {

			// The background game loop

		}
	}

	public void parseInput(Point click) {

	}

	public void parseInput(int code) {

	}

	public void executeTurn() {

	}

	public Board getBoard() {
		return board;
	}

	/**
	 * Runs the game.
	 * 
	 * @param args
	 *            Unused.
	 */
	public static void main(String[] args) {

		Game game = new Game();
		game.run();
		UserInterface ui = new UserInterface(game);

	}

}
