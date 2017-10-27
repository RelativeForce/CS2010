package peril.ui;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.ui.states.CombatState;
import peril.ui.states.EndState;
import peril.ui.states.MovementState;
import peril.ui.states.ReinforcementState;
import peril.ui.states.SetupState;

/**
 * The object that allows the user to interact with the {@link Game}. Follows
 * the factory pattern use {@link UserInterface#newUI()} to construct an new
 * {@link UserInterface}.
 * 
 * @author Joshua_Eddy, Joseph_Rolli
 */
public class UserInterface extends StateBasedGame {

	/**
	 * The instance of {@link Game} that is running <code>this</code>
	 * {@link UserInterface}.
	 */
	private Game game;

	private static AppGameContainer agc;

	/**
	 * The basic state that the game is in.
	 */
	private final CombatState combatState;
	private final SetupState setupState;
	private final ReinforcementState reinforcementState;
	private final MovementState movementState;
	private final EndState endState;


	/**
	 * Constructs a new {@link UserInterface}.
	 * 
	 * @param game
	 *            The instance of {@link Game} that is running <code>this</code>
	 *            {@link UserInterface}.
	 */
	private UserInterface(Game game) {
		super("PERIL: A Turn Based Strategy Game");
		this.combatState = new CombatState(game, this);
		this.setupState = new SetupState(game, this);
		this.reinforcementState = new ReinforcementState(game, this);
		this.movementState = new MovementState(game, this);
		this.endState = new EndState(game, this);
		this.game = game;
	}

	/**
	 * Displays the specified {@link Player} as the winner of the game.
	 * 
	 * @param player
	 *            The winning {@link Player}.
	 */
	public void displayWinner(Player player) {

		// If the current game state is the same as the core game state then...
		if (super.getCurrentState().getID() == combatState.getID()) {

			// Handle displaying the winner of the game.
			endState.displayWinner(player);
		}

	}

	/**
	 * Displays turn of the current player, or setup/end gamestate.
	 * 
	 * @param player
	 *            {@link Player} who's turn it is.
	 */
	public void displayTurn(Player player) {

	}

	/**
	 * Generates a new {@link UserInterface} and displays it to the user. Pattern:
	 * Factory
	 * 
	 * @param game
	 *            The instance of {@link Game} that is running <code>this</code>
	 *            {@link UserInterface}.
	 * 
	 * @return {@link UserInterface}
	 */
	public static UserInterface newUI(Game game) {

		// Create a new user interface.
		UserInterface ui = new UserInterface(game);

		// Open that user interface on the screen.
		try {
			agc = new AppGameContainer(ui);
			agc.setDisplayMode(1200, 800, false);
			agc.setTargetFrameRate(60);
		} catch (SlickException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return ui;
	}

	public void start() {
		try {
			agc.start();
		} catch (SlickException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Adds states to the game.
	 */
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(setupState);
		addState(combatState);
		addState(reinforcementState);
		addState(movementState);
		addState(endState);
	}

}
