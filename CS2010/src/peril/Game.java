package peril;

import java.io.File;
import java.util.Random;

import peril.controllers.*;
import peril.helpers.*;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.states.Attack;
import peril.model.states.Fortify;
import peril.model.states.Reinforce;
import peril.model.states.Setup;
import peril.views.View;
import peril.views.slick.SlickGame;

/**
 * Encapsulate the main game logic for Peril. This also extends
 * {@link StateBasedGame}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class Game {

	/**
	 * The {@link PlayerHelper} that holds all this {@link Game}s
	 * {@link SlickPlayer}s.
	 */
	public final PlayerHelper players;

	/**
	 * The instance of the {@link SlickBoard} used for this game.
	 */
	public final ModelBoard board;

	/**
	 * The {@link DirectoryHelper} that holds all the sub directories of the assets
	 * folder.
	 */
	public final DirectoryHelper assets;

	public final ModelStateHelper states;

	/**
	 * The {@link AIController} that allows the user/AI to interact with the
	 * {@link Game}.
	 */
	private final RequestHandler api;

	public final View view;

	/**
	 * The current turn of the {@link Game}. Initially zero;
	 */
	private int currentRound;

	/**
	 * Constructs a new {@link Game}.
	 */
	private Game(View view) {

		// Holds the path of the peril assets
		StringBuilder assetsPath = new StringBuilder(new File(System.getProperty("user.dir")).getPath())
				.append(File.separatorChar).append("assets");

		this.assets = new DirectoryHelper(assetsPath.toString());

		this.api = new RequestHandler(this);

		// Construct the board with the initial name.
		this.board = new ModelBoard("NOT ASSIGNED");

		this.players = new PlayerHelper(this);

		Setup setup = new Setup();
		Attack attack = new Attack();
		Fortify fortify = new Fortify();
		Reinforce reinforce = new Reinforce();

		this.states = new ModelStateHelper(attack, reinforce, setup, fortify);

		// Set the initial round to zero
		this.currentRound = 0;

		this.view = view;

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
	 * Runs the game.
	 * 
	 * @param args
	 *            Unused.
	 */
	public static void main(String[] args) {

		// Create the instance of the game.
		Game peril = new Game(new SlickGame("PERIL"));

		peril.start();

	}

	public void start() {

		try {
			view.init(api);
			view.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public AIController getAIController() {
		return api;
	}

	public GameController getGameController() {
		return api;
	}

	/**
	 * Checks all the {@link SlickContinent}s on the {@link SlickBoard} to see if
	 * they are ruled. This is o(n^2) complexity
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
	 * Checks if there is only one {@link SlickPlayer} in play. If this is the case
	 * then that {@link SlickPlayer} has won.
	 */
	public void checkWinner() {
		if (players.numberOfPlayers() == 1) {
			view.setWinner(players.getCurrent());
		}
	}

	/**
	 * Performs all the tasks that occur at the end of a round.
	 */
	public void endRound() {
		board.endRound();
		currentRound++;
	}

}
