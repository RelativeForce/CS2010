package peril.helpers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import peril.Game;
import peril.Player;
import peril.board.Army;
import peril.board.Country;
import peril.ui.UIEventHandler;
import peril.ui.components.menus.WarMenu;
import peril.ui.states.EndState;
import peril.ui.states.InteractiveState;
import peril.ui.states.LoadingScreen;
import peril.ui.states.MainMenuState;
import peril.ui.states.PlayerSelection;
import peril.ui.states.gameStates.CoreGameState;
import peril.ui.states.gameStates.ReinforcementState;
import peril.ui.states.gameStates.SetupState;
import peril.ui.states.gameStates.multiSelectState.CombatState;
import peril.ui.states.gameStates.multiSelectState.MovementState;

/**
 * A helper class for {@link Game} this object stores the instances of the
 * various {@link InteractiveState}s of the {@link Game}.
 * 
 * @author Joshua_Eddy
 *
 */
public class StateHelper {

	/**
	 * The state that displays combat to the user. This is heavily couples with
	 * {@link WarMenu}.
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
	 * The {@link LoadingScreen} that will load the map specified files from memory.
	 */
	public final LoadingScreen loadingScreen;

	/**
	 * The {@link PlayerSelection} that allows the user to select how many players
	 * will be in the game.
	 */
	public final PlayerSelection playerSelection;

	/**
	 * Constructs a new {@link StateHelper}.
	 * 
	 * @param mainMenu
	 *            The {@link MainMenuState} of the {@link Game}.
	 * @param combat
	 *            The state that displays combat to the user. This is heavily
	 *            couples with {@link WarMenu}.
	 * @param reinforcement
	 *            The {@link ReinforcementState} that allows the {@link Player} to
	 *            distribute their {@link Army} to the {@link Country}s they rule.
	 * @param setup
	 *            The {@link SetupState} that will allow the user to set up which
	 *            {@link Player} owns which {@link Country}.
	 * @param movement
	 *            The {@link MovementState} which lets the user move {@link Army}s
	 *            from one {@link Country} to another.
	 * @param end
	 *            The {@link EndState} that displays the results of the
	 *            {@link Game}.
	 * @param loadingScreen
	 *            The {@link LoadingScreen} that will load the map specified files
	 *            from memory.
	 * @param playerSelection
	 *            The {@link PlayerSelection} that allows the user to select how
	 *            many players will be in the game.
	 */
	public StateHelper(MainMenuState mainMenu, CombatState combat, ReinforcementState reinforcement, SetupState setup,
			MovementState movement, EndState end, LoadingScreen loadingScreen, PlayerSelection playerSelection) {
		this.mainMenu = mainMenu;
		this.end = end;
		this.combat = combat;
		this.reinforcement = reinforcement;
		this.setup = setup;
		this.movement = movement;
		this.loadingScreen = loadingScreen;
		this.playerSelection = playerSelection;
	}

	/**
	 * Adds {@link CoreGameState}s to the {@link GameContainer} for this
	 * {@link Game}.
	 */
	/**
	 * 
	 * @param container
	 * @param game
	 * @param eventHandler
	 * @throws SlickException
	 */
	public void initGame(GameContainer container, Game game, UIEventHandler eventHandler) throws SlickException {

		// Add starting state to the game container.
		game.addState(mainMenu);
		game.addState(playerSelection);
		game.addState(loadingScreen);

		// Add all other states to game container.
		game.addState(setup);
		game.addState(reinforcement);
		game.addState(combat);
		game.addState(movement);
		game.addState(end);

		// Assign Key and Mouse Listener as the UIEventhandler
		container.getInput().addKeyListener(eventHandler);
		container.getInput().addMouseListener(eventHandler);

		// Hide FPS counter
		container.setShowFPS(false);
		container.setVSync(true);
	}

	/**
	 * Retrieves the {@link CoreGameState} game state that the game can be saved in
	 * by name.
	 * 
	 * @param name
	 *            The name of the {@link CoreGameState}
	 * @return {@link CoreGameState}.
	 */
	public CoreGameState getSaveState(String name) {

		if (combat.getName().equals(name)) {
			return combat;
		} else if (movement.getName().equals(name)) {
			return movement;
		} else if (reinforcement.getName().equals(name)) {
			return reinforcement;
		}

		throw new NullPointerException(name + " is not a valid game state.");
	}
}
