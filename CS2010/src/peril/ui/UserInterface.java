package peril.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.board.Country;
import peril.ui.states.CombatState;
import peril.ui.states.CoreGameState;
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

	/**
	 * The basic state that the game is in.
	 */
	private final CombatState combatState;
	private final SetupState setupState;
	private final ReinforcementState reinforcementState;
	private final MovementState movementState;
	private final EndState endState;

	private final UIEventHandler eventHandler;

	/**
	 * Constructs a new {@link UserInterface}.
	 * 
	 * @param game
	 *            The instance of {@link Game} that is running <code>this</code>
	 *            {@link UserInterface}.
	 */
	public UserInterface(Game game) {
		super("PERIL: A Turn Based Strategy Game");
		this.combatState = new CombatState(game, this);
		this.setupState = new SetupState(game, this);
		this.reinforcementState = new ReinforcementState(game, this);
		this.movementState = new MovementState(game, this);
		this.endState = new EndState(game, this);
		this.game = game;
		this.eventHandler = new UIEventHandler(this, game);
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

	public void highlight(Country country) {
		GameState state = super.getCurrentState();
		if (state instanceof CoreGameState) {
			((CoreGameState) state).highlight(country);
			;
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
		container.getInput().addKeyListener(eventHandler);
		container.getInput().addMouseListener(eventHandler);
	}

}
