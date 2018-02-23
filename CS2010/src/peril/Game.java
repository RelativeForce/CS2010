package peril;

import java.io.File;
import peril.controllers.*;
import peril.helpers.*;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelContinent;
import peril.model.states.Fortify;
import peril.model.states.ModelState;
import peril.model.states.Reinforce;
import peril.model.states.Setup;
import peril.model.states.combat.Attack;
import peril.views.View;
import peril.views.slick.SlickGame;

/**
 * Encapsulate the main game logic for Peril.
 * 
 * @author Joshua_Eddy, James_Rowntree
 * 
 * @version 1.01.03
 * @since 2018-02-23
 *
 */
public final class Game {

	/**
	 * The {@link PlayerHelper} that holds all this {@link Game}s
	 * {@link ModelPlayer}s.
	 */
	public final PlayerHelper players;

	/**
	 * The {@link ModelBoard} used for this game.
	 */
	public final ModelBoard board;

	/**
	 * The {@link DirectoryHelper} that holds all the sub directories of the assets
	 * folder.
	 */
	public final DirectoryHelper assets;

	/**
	 * The {@link ModelStateHelper} that contains all this {@link ModelState}s.
	 */
	public final ModelStateHelper states;

	/**
	 * The {@link View} that displays this {@link Game} on screen.
	 */
	public final View view;

	/**
	 * The {@link AIHelper} for this game.
	 */
	public final AIHelper aiHelper;

	/**
	 * The {@link GameController} that allows the {@link View} to interact with the
	 * {@link Game}.
	 */
	private final GameController game;

	/**
	 * The {@link AIController} that allows the AIs to interact with the
	 * {@link Game}.
	 */
	private final AIController ai;

	/**
	 * The current turn of the {@link Game}. Initially zero;
	 */
	private int currentRound;

	/**
	 * Constructs a new {@link Game}.
	 */
	private Game(View view) {

		this.view = view;
		// Holds the path of the peril assets
		final StringBuilder assetsPath = new StringBuilder(new File(System.getProperty("user.dir")).getPath())
				.append(File.separatorChar).append("assets");

		this.assets = new DirectoryHelper(assetsPath.toString());
		this.game = new GameHandler(this);
		this.ai = new AIHandler(this);
		this.board = new ModelBoard("NOT ASSIGNED");
		this.players = new PlayerHelper(this);
		this.aiHelper = new AIHelper(game);

		// Construct model states
		final Setup setup = new Setup(game);
		final Attack attack = new Attack(game);
		final Fortify fortify = new Fortify(game);
		final Reinforce reinforce = new Reinforce(game);
		this.states = new ModelStateHelper(attack, reinforce, setup, fortify);

		// Set the initial round to zero
		this.currentRound = 0;

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

	/**
	 * Starts the {@link Game}.
	 */
	public void start() {

		try {
			view.init(game);
			view.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Retrieves the {@link AIController} for the {@link Game}.
	 * 
	 * @return {@link AIController}
	 */
	public AIController getAIController() {
		return ai;
	}

	/**
	 * Retrieves the {@link GameController} for the {@link Game}.
	 * 
	 * @return {@link GameController}
	 */
	public GameController getGameController() {
		return game;
	}

	/**
	 * Checks all the {@link ModelContinent}s on the {@link ModelBoard} to see if
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
	 * Checks if there is only one {@link ModelPlayer} in play. If this is the case
	 * then that {@link ModelPlayer} has won.
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
