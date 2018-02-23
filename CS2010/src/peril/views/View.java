package peril.views;

import java.util.function.Consumer;

import peril.Challenge;
import peril.controllers.GameController;
import peril.controllers.api.Player;
import peril.io.FileParser;
import peril.io.SaveFile;
import peril.model.ModelPlayer;
import peril.model.board.ModelCountry;
import peril.model.board.links.ModelLink;
import peril.model.states.Fortify;
import peril.model.states.ModelState;
import peril.model.states.Reinforce;
import peril.model.states.combat.Attack;

/**
 * This defines the behaviours of a object that will display the state of the
 * {@link Game} to the user. The model elements should have their own visual
 * counter parts that define how they are visualised and using {@link Observer}
 * observe the model objects. The {@link View#getModelView()} is used to
 * retrieve the {@link ModelView} which allows this mapping.
 * 
 * @author Joshua_Eddy, Joseph_Rolli, Adrian_Wong
 * 
 * @since 2018-02-20
 * @version 1.01.03
 * 
 * @see ModelView
 * @see FileParser
 * @see GameController
 *
 */
public interface View {

	/**
	 * Starts the game and any visual {@link Thread}s. This should be run after
	 * {@link View#init(GameController)}.
	 * 
	 * @throws Exception
	 *             The {@link Exception} that may occur if the game fails to start
	 *             correctly.
	 */
	void start() throws Exception;

	/**
	 * Initialises the {@link View} using the {@link GameController} that allows
	 * interaction between the model and the {@link View}.
	 * 
	 * @param game
	 *            The {@link GameController} that allows the {@link View} to
	 *            interact with the model of the game.
	 * @throws Exception
	 *             The {@link Exception} that may occur if the game fails to
	 *             initialise correctly.
	 */
	void init(GameController game) throws Exception;

	/**
	 * Causes the game to load from a map file.
	 * 
	 * @throws Exception
	 *             The {@link Exception} that may be thrown from a corrupt map file.
	 */
	void loadGame() throws Exception;

	/**
	 * Set a {@link ModelPlayer} as the winner of the game. This method will be
	 * called when the game determines that a {@link ModelPlayer} has won the game
	 * and this method should move the {@link View} to a visctory state.
	 * 
	 * @param winner
	 *            The {@link ModelPlayer} winner of the game.
	 */
	void setWinner(ModelPlayer winner);

	/**
	 * Set the {@link View} to display the help page with a specific id to the user.
	 * 
	 * @param pageId
	 *            The id of the page to be displayed.
	 */
	void setHelpMenuPage(int pageId);

	/**
	 * Refresh the displayed versions of the {@link Challenge}s. This will only be
	 * performed when a {@link Challenge} has been completed or removed.
	 */
	void updateChallenges();

	/**
	 * Sets a {@link ModelPlayer} as a loser of the game. The {@link View} should
	 * track the order in which the {@link Player}s lost.
	 * 
	 * @param player
	 *            The {@link ModelPlayer} that has lost.
	 */
	void addLoser(ModelPlayer player);

	/**
	 * Displays a tool tip in a appropriate place that will convey an message to the
	 * user.
	 * 
	 * @param message
	 *            The message that will be displayed to the user.
	 */
	void showToolTip(String message);

	/**
	 * Toggles the state of the games music.
	 * 
	 * @param state
	 *            The state of the game's music, true is on.
	 */
	void toggleMusic(boolean state);

	/**
	 * Toggles whether the menu that displays the {@link Challenge}s to the user
	 * between hidden and visible.
	 * 
	 * @param state
	 *            The visibility state of the menu.
	 */
	void toggleChallengeMenu(boolean state);

	/**
	 * Toggles whether the menu that displays the pause options to the user between
	 * hidden and visible. When the pause menu is visible {@link View#isPaused()}
	 * should return true.
	 * 
	 * @param state
	 *            The visibility state of the menu.
	 */
	void togglePauseMenu(boolean state);

	/**
	 * Toggles whether the menu that displays the combat to the user between hidden
	 * and visible.
	 * 
	 * @param state
	 *            The visibility state of the menu.
	 */
	void toggleWarMenu(boolean state);

	/**
	 * Toggles whether the menu that displays the {@link ModelPlayer} statistics to
	 * the user between hidden and visible.
	 * 
	 * @param state
	 *            The visibility state of the menu.
	 */
	void toggleStatsMenu(boolean state);

	/**
	 * Toggles whether the menu that displays the upgrades to a {@link ModelCountry}
	 * to the user between hidden and visible.
	 * 
	 * @param state
	 *            The visibility state of the menu.
	 */
	void toggleUpgradeMenu(boolean state);

	/**
	 * Toggles whether the menu that displays the help pages to the user between
	 * hidden and visible.
	 * 
	 * @param state
	 *            The visibility state of the menu.
	 */
	void toggleHelpMenu(boolean state);

	/**
	 * Moves the help menu to the next page in the chain if there is one.
	 */
	void nextHelpPage();

	/**
	 * Moves the help menu to the previous page in the chain if there is one.
	 */
	void previousHelpPage();

	/**
	 * Cause the view to save the current state of the game. This should throw an
	 * {@link Exception} if called outside the core game loop.
	 */
	void save();

	/**
	 * Exits the game immediately.
	 */
	void exit();

	/**
	 * Causes the {@link View} to enter the main menu.
	 */
	void enterMainMenu();

	/**
	 * Causes the {@link View} to enter the {@link Reinforce}.
	 */
	void enterReinforce();

	/**
	 * Causes the {@link View} to enter the {@link Attack}.
	 */
	void enterCombat();

	/**
	 * Causes the {@link View} to enter the {@link Fortify}.
	 */
	void enterFortify();

	/**
	 * Causes the {@link View} to display a round of combat to the user.
	 */
	void attack();

	/**
	 * Causes the {@link View} to display a round of combat to the user where the
	 * attacking {@link ModelPlayer} is controlled by an AI.
	 */
	void AIattack();

	/**
	 * This should centre the board on the screen.
	 */
	void centerBoard();

	/**
	 * Performs a {@link Consumer} task on every {@link ModelPlayer} that has lost
	 * the game.
	 * 
	 * @param task
	 *            {@link Consumer}
	 */
	void forEachLoser(Consumer<ModelPlayer> task);

	/**
	 * Retrieves the x coordinate of the army that is displayed over the visual
	 * counter part of the specified {@link ModelCountry}.
	 * 
	 * @param country
	 *            The {@link ModelCountry} that has the army.
	 * @return The x coordinate of the army.
	 */
	int getArmyOffsetX(ModelCountry country);

	/**
	 * Retrieves the y coordinate of the army that is displayed over the visual
	 * counter part of the specified {@link ModelCountry}.
	 * 
	 * @param country
	 *            The {@link ModelCountry} that has the army.
	 * @return The y coordinate of the army.
	 */
	int getArmyOffsetY(ModelCountry country);

	/**
	 * Retrieves the width of the screen NOT the window that the game is displayed
	 * in.
	 * 
	 * @return The width of the screen.
	 */
	int getScreenWidth();

	/**
	 * Retrieves the height of the screen NOT the window that the game is displayed
	 * in.
	 * 
	 * @return The height of the screen.
	 */
	int getScreenHeight();

	/**
	 * Retrieves the width of the window that displays the game. If the
	 * {@link View#isFullScreen()} then this will be {@link View#getScreenWidth()}.
	 * 
	 * @return The width of the window that displays the game
	 */
	int getWindowWidth();

	/**
	 * Retrieves the height of the window that displays the game. If the
	 * {@link View#isFullScreen()} then this will be {@link View#getScreenWidth()}.
	 * 
	 * @return The height of the window that displays the game
	 */
	int getWindowHeight();

	/**
	 * Retrieves whether there music is currently playing.
	 * 
	 * @return The music state.
	 */
	boolean isMusicOn();

	/**
	 * Retrieves whether the game is currently paused or not.
	 * 
	 * @return The pause state.
	 */
	boolean isPaused();

	/**
	 * Retrieves whether or not the specified {@link ModelState} is the current
	 * {@link ModelState} that the {@link View} is displaying to the user.
	 * 
	 * @param state
	 *            The {@link ModelState} to check.
	 * @return Retrieves whether or not the specified {@link ModelState} is the
	 *         current {@link ModelState}.
	 */
	boolean isCurrentState(ModelState state);

	/**
	 * Retrieves whether the game is full screen or not.
	 * 
	 * @return The full screen state.
	 */
	boolean isFullScreen();

	/**
	 * Retrieves the {@link ModelView} that allows the reverse mapping from model
	 * object to their visual counter points.
	 * 
	 * @return The {@link ModelView}.
	 */
	ModelView getModelView();

	/**
	 * Retrieves the {@link FileParser} that will load the game from memory. This
	 * {@link FileParser} should also generate the visual object and add them to the
	 * {@link ModelView}.
	 * 
	 * @param mapPath
	 *            The file path to the map folder.
	 * @param save
	 *            The {@link SaveFile} that is to be loaded.
	 * @return The {@link FileParser} that will load the game from memory.
	 */
	FileParser getMapLoader(String mapPath, SaveFile save);

	/**
	 * Allows to enter the credits page.
	 */
	void enterCredits();

	/**
	 * Blocks the {@link ModelLink} between two countries that are selected.
	 */
	void blockLink();

}
