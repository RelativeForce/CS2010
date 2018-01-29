package peril.controllers;

import java.util.List;
import java.util.function.Consumer;

import peril.Challenge;
import peril.Game;
import peril.controllers.api.Player;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.states.Attack;
import peril.model.states.Fortify;
import peril.model.states.Reinforce;
import peril.model.states.Setup;
import peril.views.View;

/**
 * 
 * The API for all {@link Game} - {@link View} interactions. Any invalid
 * operations will throw the appropriate exceptions.
 * 
 * @author Joshua_Eddy
 *
 */
public interface GameController {

	/**
	 * Retrieves the current {@link Player}.
	 * 
	 * @return
	 */
	ModelPlayer getCurrentModelPlayer();

	/**
	 * Sets the {@link Game} help menu to the specified page id.
	 * 
	 * @param pageId
	 */
	void setHelpMenuPage(int pageId);

	/**
	 * Retrieves the {@link View} that is currently in use by the {@link Game}.
	 * 
	 * @return
	 */
	View getView();

	/**
	 * The file path string to the UI components folder.
	 * 
	 * @return file path/
	 */
	String getUIPath();

	/**
	 * The file path string to the maps folder.
	 * 
	 * @return file path/
	 */
	String getMapsPath();

	/**
	 * Resets the state of the {@link Game} back to its initial state. All data is
	 * erased.
	 */
	void resetGame();

	/**
	 * Sets the {@link ModelBoard}'s name.
	 * 
	 * @param name
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
	 * Retrieves the {@link Game}'s {@link AIController}.
	 * 
	 * @return {@link AIController}
	 */
	AIController getAIController();

	/**
	 * Retrieves the {@link Game}'s {@link ModelBoard}.
	 * 
	 * @return
	 */
	ModelBoard getModelBoard();

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
	 * @return
	 */
	boolean isPlaying(int playerNumber);

	/**
	 * Retrieves a {@link ModelPlayer} using a specified player number.
	 * 
	 * @param playerNumber
	 * @return
	 */
	ModelPlayer getModelPlayer(int playerNumber);

	/**
	 * Refreshes the state of all the challenges based on the current state of all
	 * the {@link ModelPlayer}s.
	 */
	void checkChallenges();

	/**
	 * Changes the current {@link ModelPlayer} to the next.
	 */
	void nextPlayer();

	/**
	 * Checks whether the game has been won.
	 */
	void checkWinner();

	/**
	 * Checks whether the continents ruler ship has changed.
	 */
	void checkContinentRulership();

	Attack getAttack();

	Reinforce getReinforce();

	Fortify getFortify();

	Setup getSetup();

	String getMusicPath();

	void setCurrentPlayer(ModelPlayer model);

	void setRoundNumber(int parseInt);

	void addChallenge(Challenge challenge);

	List<Challenge> getChallenges();

	void forEachModelPlayer(Consumer<ModelPlayer> task);

	void forEachLoser(Consumer<ModelPlayer> task);

	int getRoundNumber();

	void confirmReinforce();

	void confirmCombat();

	void confirmSetup();

	void confirmMovement();

	void autoDistributeCountries();

	void setLoser(ModelPlayer player);

}
