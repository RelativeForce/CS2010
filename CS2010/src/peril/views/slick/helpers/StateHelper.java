package peril.views.slick.helpers;

import org.newdawn.slick.SlickException;

import peril.GameController;
import peril.model.states.Attack;
import peril.views.slick.SlickGame;
import peril.views.slick.board.SlickBoard;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.components.MiniMap;
import peril.views.slick.states.Credits;
import peril.views.slick.states.EndState;
import peril.views.slick.states.Instructions;
import peril.views.slick.states.InteractiveState;
import peril.views.slick.states.LoadingScreen;
import peril.views.slick.states.MainMenu;
import peril.views.slick.states.Opening;
import peril.views.slick.states.PlayerSelection;
import peril.views.slick.states.gameStates.SlickAttack;
import peril.views.slick.states.gameStates.CoreGameState;
import peril.views.slick.states.gameStates.SlickFortify;
import peril.views.slick.states.gameStates.SlickReinforce;
import peril.views.slick.states.gameStates.SlickSetup;

/**
 * A helper class for {@link SlickGame} this object stores the instances of the
 * various {@link InteractiveState}s of the {@link SlickGame}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-03-16
 * @version 1.01.04
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
	 * The {@link SlickSetup} that will allow the user to set up which
	 * {@link SlickPlayer} owns which {@link SlickCountry}.
	 */
	public final SlickSetup setup;

	/**
	 * The {@link SlickReinforce}.
	 */
	public final SlickReinforce reinforce;

	/**
	 * The {@link SlickAttack}.
	 */
	public final SlickAttack attack;

	/**
	 * The {@link SlickFortify}.
	 */
	public final SlickFortify fortify;

	/**
	 * The {@link EndState} that displays the results of the {@link SlickGame}.
	 */
	public final EndState end;

	/**
	 * The {@link Credits} page for this game.
	 */
	public final Credits credits;

	/**
	 * The {@link Instructions} that displays the instructions of the
	 * {@link SlickGame}.
	 */
	public final Instructions instructions;

	/**
	 * Constructs a new {@link StateHelper}.
	 * 
	 * @param mainMenu
	 *            The {@link MainMenu} of the {@link SlickGame}.
	 * @param attack
	 *            The {@link Attack}
	 * @param reinforce
	 *            The {@link SlickReinforce}.
	 * @param setup
	 *            The {@link SlickSetup}.
	 * @param fortify
	 *            The {@link SlickFortify}.
	 * @param end
	 *            The {@link EndState} that displays the results of the
	 *            {@link SlickGame}.
	 * @param loadingScreen
	 *            The {@link LoadingScreen} that will load the map specified files
	 *            from memory.
	 * @param playerSelection
	 *            The {@link PlayerSelection} that allows the user to select how
	 *            many players will be in the game.
	 * @param opening
	 *            The {@link Opening} splash screen of the game.
	 * @param credits
	 *            The {@link Credits} page for this game.
	 * @param instructions
	 *            The {@link Instructions} for the game.
	 */
	public StateHelper(Opening opening, MainMenu mainMenu, SlickAttack attack, SlickReinforce reinforce,
			SlickSetup setup, SlickFortify fortify, EndState end, LoadingScreen loadingScreen,
			PlayerSelection playerSelection, Credits credits, Instructions instructions) {
		this.opening = opening;
		this.mainMenu = mainMenu;
		this.end = end;
		this.attack = attack;
		this.reinforce = reinforce;
		this.setup = setup;
		this.fortify = fortify;
		this.loadingScreen = loadingScreen;
		this.playerSelection = playerSelection;
		this.credits = credits;
		this.instructions = instructions;
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
		game.addState(opening);
		game.addState(mainMenu);
		game.addState(playerSelection);
		game.addState(loadingScreen);
		game.addState(end);

		// Add all the game states to game container.
		game.addState(setup);
		game.addState(reinforce);
		game.addState(attack);
		game.addState(fortify);
		game.addState(credits);
		game.addState(instructions);

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
	 * @param game
	 *            The {@link GameController} that allows the {@link MiniMap} to
	 *            query the state of the game.
	 */
	public void addMiniMap(SlickBoard slickBoard, int screenWidth, int screenHeight, GameController game) {

		final MiniMap miniMap = new MiniMap(slickBoard, screenWidth, screenHeight, game);

		setup.setMiniMap(miniMap);
		reinforce.setMiniMap(miniMap);
		attack.setMiniMap(miniMap);
		fortify.setMiniMap(miniMap);

	}

	/**
	 * Removes the {@link MiniMap} from all the {@link CoreGameState}s.
	 */
	public void removeMiniMap() {

		setup.setMiniMap(null);
		reinforce.setMiniMap(null);
		attack.setMiniMap(null);
		fortify.setMiniMap(null);

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

		if (attack.getName().equals(name)) {
			return attack;
		} else if (fortify.getName().equals(name)) {
			return fortify;
		} else if (reinforce.getName().equals(name)) {
			return reinforce;
		}

		throw new NullPointerException(name + " is not a valid game state to save in.");
	}
}
