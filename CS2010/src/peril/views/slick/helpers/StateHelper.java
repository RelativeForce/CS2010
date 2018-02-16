package peril.views.slick.helpers;

import org.newdawn.slick.SlickException;

import peril.Game;
import peril.views.slick.SlickGame;
import peril.views.slick.board.SlickBoard;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.components.MiniMap;
import peril.views.slick.components.menus.WarMenu;
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
	 * The {@link MainMenu} of the {@link Game}.
	 */
	public final MainMenu mainMenu;

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
	 * The {@link MovementState} which lets the user move {@link ModelArmy}s from
	 * one {@link SlickCountry} to another.
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
	
	public final Opening opening;

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
	 */
	public StateHelper(Opening opening,MainMenu mainMenu, CombatState combat, ReinforcementState reinforcement, SetupState setup,
			MovementState movement, EndState end, LoadingScreen loadingScreen, PlayerSelection playerSelection) {
		this.opening = opening;
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
	 * @param slickBoard
	 * @param eventHandler
	 * @throws SlickException
	 */
	public void init(SlickGame game, SlickBoard slickBoard) throws SlickException {

		// Add starting state to the game container.
		//game.addState(opening);
		game.addState(mainMenu);
		game.addState(playerSelection);
		game.addState(loadingScreen);
		game.addState(end);

		// Add all the game states to game container.
		game.addState(setup);
		game.addState(reinforcement);
		game.addState(combat);
		game.addState(movement);
	
	}
	
	public void addMiniMap(SlickBoard slickBoard, SlickGame game) {
		
		final MiniMap miniMap = new MiniMap(slickBoard, game.getScreenWidth(), game.getScreenHeight());
		
		setup.setMiniMap(miniMap);
		reinforcement.setMiniMap(miniMap);
		combat.setMiniMap(miniMap);
		movement.setMiniMap(miniMap);
		
	}
	
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

		throw new NullPointerException(name + " is not a valid game state.");
	}
}
