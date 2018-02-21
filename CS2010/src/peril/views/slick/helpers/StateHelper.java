package peril.views.slick.helpers;

import org.newdawn.slick.SlickException;

import peril.views.slick.SlickGame;
import peril.views.slick.board.SlickBoard;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.components.MiniMap;
import peril.views.slick.components.menus.WarMenu;
import peril.views.slick.states.Credits;
import peril.views.slick.states.EndState;
import peril.views.slick.states.InteractiveState;
import peril.views.slick.states.LoadingScreen;
import peril.views.slick.states.MainMenu;
import peril.views.slick.states.Opening;
import peril.views.slick.states.PlayerSelection;
import peril.views.slick.states.gameStates.CombatState;
import peril.views.slick.states.gameStates.CoreGameState;
import peril.views.slick.states.gameStates.MovementState;
import peril.views.slick.states.gameStates.ReinforcementState;
import peril.views.slick.states.gameStates.SetupState;

/**
 * A helper class for {@link SlickGame} this object stores the instances of the
 * various {@link InteractiveState}s of the {@link SlickGame}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-17
 * @version 1.01.01
 * 
 * @see InteractiveState
 * @see SlickGame
 *
 */
public final class StateHelper {

	/**
	 * The splash screen of the game.
	 */
	public final Opening opening;

	/**
	 * The {@link MainMenu} of the {@link SlickGame}.
	 */
	public final MainMenu mainMenu;

	/**
	 * The {@link PlayerSelection} that allows the user to select how many players
	 * will be in the game.
	 */
	public final PlayerSelection playerSelection;

	/**
	 * The {@link LoadingScreen} that will load the map specified files from memory.
	 */
	public final LoadingScreen loadingScreen;

	/**
	 * The {@link SetupState} that will allow the user to set up which
	 * {@link SlickPlayer} owns which {@link SlickCountry}.
	 */
	public final SetupState setup;

	/**
	 * The {@link ReinforcementState} that allows the {@link SlickPlayer} to
	 * distribute their {@link ModelArmy} to the {@link SlickCountry}s they rule.
	 */
	public final ReinforcementState reinforcement;

	/**
	 * The state that displays combat to the user. This is heavily couples with
	 * {@link WarMenu}.
	 */
	public final CombatState combat;

	/**
	 * The {@link MovementState} which lets the user move {@link ModelArmy}s from
	 * one {@link SlickCountry} to another.
	 */
	public final MovementState movement;

	/**
	 * The {@link EndState} that displays the results of the {@link SlickGame}.
	 */
	public final EndState end;

	/**
	 * The {@link Credits} page for this game. 
	 */
	public final Credits credits;

	/**
	 * Constructs a new {@link StateHelper}.
	 * 
	 * @param mainMenu
	 *            The {@link MainMenu} of the {@link Game}.
	 * @param combat
	 *            The state that displays combat to the user. This is heavily
	 *            couples with {@link WarMenu}.
	 * @param reinforcement
	 *            The {@link ReinforcementState} that allows the {@link SlickPlayer}
	 *            to distribute their {@link ModelArmy} to the {@link SlickCountry}s
	 *            they rule.
	 * @param setup
	 *            The {@link SetupState} that will allow the user to set up which
	 *            {@link SlickPlayer} owns which {@link SlickCountry}.
	 * @param movement
	 *            The {@link MovementState} which lets the user move
	 *            {@link ModelArmy}s from one {@link SlickCountry} to another.
	 * @param end
	 *            The {@link EndState} that displays the results of the
	 *            {@link Game}.
	 * @param loadingScreen
	 *            The {@link LoadingScreen} that will load the map specified files
	 *            from memory.
	 * @param playerSelection
	 *            The {@link PlayerSelection} that allows the user to select how
	 *            many players will be in the game.
	 * @param opening
	 *            The splash screen of the game.
	 * @param credits
	 *            The credits page for this game.
	 */
	public StateHelper(Opening opening, MainMenu mainMenu, CombatState combat, ReinforcementState reinforcement,
			SetupState setup, MovementState movement, EndState end, LoadingScreen loadingScreen,
			PlayerSelection playerSelection, Credits credits) {
		this.opening = opening;
		this.mainMenu = mainMenu;
		this.end = end;
		this.combat = combat;
		this.reinforcement = reinforcement;
		this.setup = setup;
		this.movement = movement;
		this.loadingScreen = loadingScreen;
		this.playerSelection = playerSelection;
		this.credits = credits;
	}

	/**
	 * Adds {@link InteractiveState}s to the {@link SlickGame}.
	 * 
	 * @param game
	 *            The {@link SlickGame} that the {@link InteractiveState} will be
	 *            added to.
	 * 
	 * @throws SlickException
	 *             Thrown if there is a problem with adding a state.
	 */
	public void init(SlickGame game) throws SlickException {

		// Add starting state to the game container.
		// game.addState(opening);
		game.addState(mainMenu);
		game.addState(playerSelection);
		game.addState(loadingScreen);
		game.addState(end);

		// Add all the game states to game container.
		game.addState(setup);
		game.addState(reinforcement);
		game.addState(combat);
		game.addState(movement);
		game.addState(credits);

	}

	/**
	 * Adds a {@link MiniMap} to all the {@link CoreGameState}s.
	 * 
	 * @param slickBoard
	 *            The {@link SlickBoard} that the {@link MiniMap} will display.
	 * @param screenWidth
	 *            The width of the screen.
	 * @param screenHeight
	 *            The height of the screen.
	 */
	public void addMiniMap(SlickBoard slickBoard, int screenWidth, int screenHeight) {

		final MiniMap miniMap = new MiniMap(slickBoard, screenWidth, screenHeight);

		setup.setMiniMap(miniMap);
		reinforcement.setMiniMap(miniMap);
		combat.setMiniMap(miniMap);
		movement.setMiniMap(miniMap);

	}

	/**
	 * Removes the {@link MiniMap} from all the {@link CoreGameState}s.
	 */
	public void removeMiniMap() {

		setup.setMiniMap(null);
		reinforcement.setMiniMap(null);
		combat.setMiniMap(null);
		movement.setMiniMap(null);

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

		throw new NullPointerException(name + " is not a valid game state to save in.");
	}
}
