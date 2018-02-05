package peril.controllers;

import java.util.List;
import java.util.function.Consumer;

import javax.jws.WebParam.Mode;

import peril.Challenge;
import peril.Game;
import peril.controllers.api.Country;
import peril.controllers.api.Player;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.states.Attack;
import peril.model.states.Fortify;
import peril.model.states.Reinforce;
import peril.model.states.Setup;
import peril.views.View;
import peril.views.slick.states.gameStates.multiSelectState.CombatState;

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

	/**
	 * Returns the state that displays combat phase to the user.
	 * 
	 * @return Returns an {@link Attack} state.
	 */
	Attack getAttack();

	/**
	 * Returns the state that displays reinforcement phase to the user.
	 * 
	 * @return Returns a {@link Reinforce} state.
	 */
	Reinforce getReinforce();

	/**
	 * Returns the state that displays the fortification(fortify) phase to the user.
	 * 
	 * @return Returns a {@link Fortify} state.
	 */
	Fortify getFortify();

	/**
	 * Returns the state that displays the setup phase to the user.
	 * 
	 * @return Returns a {@link Setup} state.
	 */
	Setup getSetup();

	/**
	 * Returns a String that is the path to the music assets.
	 * 
	 * @return String path to the music assets.
	 */
	String getMusicPath();

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
	 * Gets the list of all {@link Challenge}s the {@link ModelPlayer}s can
	 * complete.
	 * 
	 * @return The {@link List} of {@link Challenge}s.
	 */
	List<Challenge> getChallenges();

	/**
	 * Performs a specified task for each {@link ModelPlayer}.
	 * 
	 * @param task
	 *            The task to be performed on each {@link ModelPlayer.}
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
	 * 
	 */
	void confirmReinforce();

	/**
	 * Confirms that the {@link Attack}(Combat) state is finished and moves onto the
	 * next state.
	 * 
	 */
	void confirmCombat();

	/**
	 * Confirms that the {@link Setup} state is finished and moves onto the next
	 * state.
	 * 
	 */
	void confirmSetup();

	/**
	 * Confirms the movement of troops of the current {@link ModelPlayer} and moves
	 * onto the next {@link ModelPlayer}.
	 */
	void confirmMovement();

	/**
	 * Automatically distributes the {@link Country}s for each {@link ModelPlayer}
	 * in the game.
	 */
	void autoDistributeCountries();

	/**
	 * Sets a {@link ModelPlayer} as a loser.
	 * 
	 * @param player the {@link ModelPlayer} who is a loser.
	 */
	void setLoser(ModelPlayer player);

	/**
	 * Adds point to the current player. Kill a unit = 1 point Conquer a country =
	 * 10 points
	 * 
	 * @param points
	 */
	void addPoints(int points);

	/**
	 * Processes the turn of the AI.
	 * 
	 * @param delta Delta time delay for the AI to process its turn.
	 */
	void processAI(int delta);

	void fortify();

}
