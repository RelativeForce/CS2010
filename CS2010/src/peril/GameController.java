package peril;

import java.util.List;
import java.util.function.Consumer;

import peril.ai.AIController;
import peril.helpers.AIHelper;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.states.Attack;
import peril.model.states.Fortify;
import peril.model.states.ModelState;
import peril.model.states.Reinforce;
import peril.model.states.Setup;
import peril.views.View;

/**
 * 
 * The API for all {@link View} to {@link Game} interactions. Any invalid
 * operations will throw the appropriate exceptions.
 * 
 * @author Joshua_Eddy, James_Rowntree
 * 
 * @version 1.01.03
 * @since 2018-02-06
 *
 */
public interface GameController {

	/**
	 * Retrieves the current {@link ModelPlayer}.
	 * 
	 * @return {@link ModelPlayer}.
	 */
	ModelPlayer getCurrentModelPlayer();

	/**
	 * Retrieves the {@link Game}'s {@link ModelBoard}.
	 * 
	 * @return {@link ModelBoard}
	 */
	ModelBoard getModelBoard();

	/**
	 * Retrieves a {@link ModelPlayer} using a specified player number.
	 * 
	 * @param playerNumber
	 *            number of the player.
	 * @return {@link ModelPlayer}.
	 */
	ModelPlayer getModelPlayer(int playerNumber);

	/**
	 * Returns the state that displays combat phase to the user.
	 * 
	 * @return {@link Attack}.
	 */
	Attack getAttack();

	/**
	 * Returns the state that displays reinforcement phase to the user.
	 * 
	 * @return {@link Reinforce}.
	 */
	Reinforce getReinforce();

	/**
	 * Returns the state that displays the fortification(fortify) phase to the user.
	 * 
	 * @return {@link Fortify}.
	 */
	Fortify getFortify();

	/**
	 * Returns the state that displays the setup phase to the user.
	 * 
	 * @return {@link Setup}.
	 */
	Setup getSetup();

	/**
	 * Gets the list of all {@link Challenge}s the {@link ModelPlayer}s can
	 * complete.
	 * 
	 * @return The {@link List} of {@link Challenge}s.
	 */
	List<Challenge> getChallenges();

	/**
	 * Retrieves the {@link View} that is currently in use by the {@link Game}.
	 * 
	 * @return {@link View}
	 */
	View getView();

	/**
	 * Retrieves the {@link Game}'s {@link AIController}.
	 * 
	 * @return {@link AIController}
	 */
	AIController getAIController();

	/**
	 * Retrieves the {@link Directory} that denotes the file system of the assets of
	 * the game.
	 * 
	 * @return {@link Directory}
	 */
	Directory getDirectory();

	/**
	 * Sets the {@link Game} help menu to the specified page id.
	 * 
	 * @param pageId
	 *            The id of the help page.
	 */
	void setHelpMenuPage(int pageId);

	/**
	 * Resets the state of the {@link Game} back to its initial state. All data is
	 * erased.
	 */
	void resetGame();

	/**
	 * Sets the {@link ModelBoard}'s name.
	 * 
	 * @param name
	 *            New name of the {@link ModelBoard}.
	 */
	void setBoardName(String name);

	/**
	 * Adds a {@link ModelPlayer} to the {@link Game}.
	 * 
	 * @param player
	 *            {@link ModelPlayer}
	 */
	void addPlayer(ModelPlayer player);

	/**
	 * Performs a {@link Consumer} task on each {@link ModelCountry} on the
	 * {@link ModelBoard}.
	 * 
	 * @param task
	 *            {@link Consumer} task
	 */
	void forEachModelCountry(Consumer<ModelCountry> task);

	/**
	 * Checks whether a {@link ModelPlayer} with the specified number is currently
	 * playing or not.
	 * 
	 * @param playerNumber
	 *            number of the {@link ModelPlayer}
	 * @return Whether a {@link ModelPlayer} with the specified number is currently
	 *         playing or not.
	 */
	boolean isPlaying(int playerNumber);

	/**
	 * Refreshes the state of all the challenges based on the current state of all
	 * the {@link ModelPlayer}s.
	 */
	void checkChallenges();

	/**
	 * Changes the current {@link ModelPlayer} to the next in the sequence.
	 */
	void nextPlayer();

	/**
	 * Checks whether the game has been won if so it will cause the game to move
	 * into the end game state.
	 */
	void checkWinner();

	/**
	 * Checks whether the continents ruler ship has changed. This is a separate as
	 * it is a complex function.
	 */
	void checkContinentRulership();

	/**
	 * Sets the current {@link ModelPlayer} for this round.
	 * 
	 * @param model
	 *            The current {@link ModelPlayer} who's turn it is.
	 */
	void setCurrentPlayer(ModelPlayer model);

	/**
	 * Sets the current round number.
	 * 
	 * @param parseInt
	 *            The number of the current round.
	 */
	void setRoundNumber(int parseInt);

	/**
	 * Adds a {@link Challenge} to the game for the {@link ModelPlayer}s to achieve.
	 *
	 * @param challenge
	 *            The {@link Challenge} to be added.
	 */
	void addChallenge(Challenge challenge);

	/**
	 * Performs a specified task for each {@link ModelPlayer}.
	 * 
	 * @param task
	 *            The task to be performed on each {@link ModelPlayer}.
	 */
	void forEachModelPlayer(Consumer<ModelPlayer> task);

	/**
	 * Performs a specified task for each loser {@link ModelPlayer}.
	 * 
	 * @param task
	 *            The task to be performed on each {@link ModelPlayer} (loser).
	 */
	void forEachLoser(Consumer<ModelPlayer> task);

	/**
	 * Gets the current round number.
	 * 
	 * @return int value of the current round number.
	 */
	int getRoundNumber();

	/**
	 * Confirms that the {@link Reinforce} state is finished and moves onto the next
	 * state.
	 */
	void confirmReinforce();

	/**
	 * Confirms that the {@link Attack}(Combat) state is finished and moves onto the
	 * next state.
	 */
	void confirmCombat();

	/**
	 * Confirms that the {@link Setup} state is finished and moves onto the next
	 * state.
	 */
	void confirmSetup();

	/**
	 * Confirms that the {@link Fortify} state is finished and moves onto the next
	 * state. Also moves the next {@link ModelPlayer}.
	 */
	void confirmMovement();

	/**
	 * Automatically distributes the {@link ModelCountry}s for each {@link ModelPlayer}
	 * in the game.
	 */
	void autoDistributeCountries();

	/**
	 * Sets a {@link ModelPlayer} as a loser.
	 * 
	 * @param player
	 *            the {@link ModelPlayer} who is a loser.
	 */
	void setLoser(ModelPlayer player);

	/**
	 * Adds point to the current {@link ModelPlayer}. Kill a unit = 1 point Conquer
	 * a country = 10 points
	 * 
	 * @param points
	 * 			The points to be added.
	 */
	void addPoints(int points);

	/**
	 * Processes the turn of the AI.
	 * 
	 * @param delta
	 *            The amount of milliseconds that have passed since the last time
	 *            this method was called.
	 */
	void processAI(int delta);

	/**
	 * Retrieves the current {@link ModelState} of the game.
	 * 
	 * @return {@link ModelState} or null if the current state is not a
	 *         {@link ModelState}.
	 * 
	 */
	ModelState getCurrentState();

	/**
	 * Retrieves the {@link AIHelper} of the game.
	 * 
	 * @return {@link AIHelper}
	 */
	AIHelper getAIs();

}
