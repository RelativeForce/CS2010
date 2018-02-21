package peril.views;

import peril.controllers.GameController;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelBoard;
import peril.model.board.ModelContinent;
import peril.model.board.ModelCountry;
import peril.model.board.ModelHazard;
import peril.model.board.ModelUnit;
import peril.model.board.links.ModelLinkState;
import peril.model.states.ModelState;

/**
 * 
 * Provides a mapping from model objects to their visual counter parts. All the
 * objects returned by the 'getVisual' methods should return the {@link View}
 * specific objects. This must be initialised using
 * {@link ModelView#init(GameController)}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-16
 * @version 1.01.01
 *
 */
public interface ModelView {

	/**
	 * Initialises the {@link ModelView} visual elements that should be loaded when
	 * the game is initialised.
	 * 
	 * @param game
	 *            The {@link GameController} that allows the {@link ModelView} to
	 *            interact with the model of the game.
	 */
	void init(GameController game);

	/**
	 * Clears all the visual elements mappings except those between
	 * {@link ModelLinkState}s and {@link ModelBoard}.
	 */
	void clear();

	/**
	 * Adds a visual counterpart to a {@link ModelUnit}. The model counter part
	 * should be aggregated by the visual and accessible.
	 * 
	 * @param unit
	 *            The visual counterpart to a {@link ModelUnit}.
	 */
	void addUnit(Object unit);

	/**
	 * Adds a visual counterpart to a {@link ModelCountry}. The model counter part
	 * should be aggregated by the visual and accessible.
	 * 
	 * @param country
	 *            The visual counterpart to a {@link ModelCountry}.
	 */
	void addCountry(Object country);

	/**
	 * Adds a visual counterpart to a {@link ModelContinent}. The model counter part
	 * should be aggregated by the visual and accessible.
	 * 
	 * @param continent
	 *            The visual counterpart to a {@link ModelContinent}.
	 */
	void addContinent(Object continent);

	/**
	 * Adds a visual counterpart to a {@link ModelPlayer}. The model counter part
	 * should be aggregated by the visual and accessible.
	 * 
	 * @param player
	 *            The visual counterpart to a {@link ModelPlayer}.
	 */
	void addPlayer(Object player);

	/**
	 * Adds a visual counterpart to a {@link ModelArmy}. The model counter part
	 * should be aggregated by the visual and accessible.
	 * 
	 * @param army
	 *            The visual counterpart to a {@link ModelArmy}.
	 */
	void addArmy(Object army);

	/**
	 * Adds a visual counterpart to a {@link ModelBoard}. The model counter part
	 * should be aggregated by the visual and accessible.
	 * 
	 * @param board
	 *            The visual counterpart to a {@link ModelBoard}.
	 */
	void addBoard(Object board);

	/**
	 * Adds a visual counterpart to a {@link ModelState}. The model counter part
	 * should be aggregated by the visual and accessible.
	 * 
	 * @param state
	 *            The visual counterpart to a {@link ModelState}.
	 */
	void addState(Object state);

	/**
	 * Adds a visual counterpart to a {@link ModelHazard}. The model counter part
	 * should be aggregated by the visual and accessible.
	 * 
	 * @param hazard
	 *            The visual counterpart to a {@link ModelHazard}.
	 */
	void addHazard(Object hazard);

	/**
	 * Adds a visual counterpart to a {@link ModelLinkState}. The model counter part
	 * should be aggregated by the visual and accessible.
	 * 
	 * @param linkState
	 *            The visual counterpart to a {@link ModelLinkState}.
	 */
	void addLinkState(Object linkState);

	/**
	 * Retrieves the visual counterpart to the specified {@link ModelCountry}.
	 * 
	 * @param country
	 *            The {@link ModelCountry}.
	 * @return The {@link View} specific object for displaying the specified
	 *         {@link ModelCountry}.
	 */
	Object getVisual(ModelCountry country);

	/**
	 * Retrieves the visual counterpart to the specified {@link ModelContinent}.
	 * 
	 * @param The
	 *            {@link ModelContinent}.
	 * @return The {@link View} specific object for displaying the specified
	 *         {@link ModelContinent}.
	 */
	Object getVisual(ModelContinent continent);

	/**
	 * Retrieves the visual counterpart to the specified {@link ModelBoard}
	 * 
	 * @param board
	 *            The {@link ModelBoard}.
	 * @return The {@link View} specific object for displaying the specified
	 *         {@link ModelBoard}.
	 */
	Object getVisual(ModelBoard board);

	/**
	 * Retrieves the visual counterpart to the specified {@link ModelHazard}.
	 * 
	 * @param hazard
	 *            The {@link ModelHazard}.
	 * 
	 * @return The {@link View} specific object for displaying the specified
	 *         {@link ModelHazard}.
	 */
	Object getVisual(ModelHazard hazard);

	/**
	 * Retrieves the visual counterpart to the specified {@link ModelArmy}.
	 * 
	 * @param army
	 *            The {@link ModelArmy}.
	 * @return The {@link View} specific object for displaying the specified
	 *         {@link ModelArmy}.
	 */
	Object getVisual(ModelArmy army);

	/**
	 * Retrieves the visual counterpart to the specified {@link ModelState}.
	 * 
	 * @param state
	 *            The {@link ModelState}.
	 * @return The {@link View} specific object for displaying the specified
	 *         {@link ModelState}.
	 */
	Object getVisual(ModelState state);

	/**
	 * Retrieves the visual counterpart to the specified {@link ModelPlayer}.
	 * 
	 * @param player
	 *            The {@link ModelPlayer}.
	 * @return The {@link View} specific object for displaying the specified
	 *         {@link ModelPlayer}.
	 */
	Object getVisual(ModelPlayer player);

	/**
	 * Retrieves the visual counterpart to the specified {@link ModelUnit}.
	 * 
	 * @param unit
	 *            The {@link ModelUnit}.
	 * @return The {@link View} specific object for displaying the specified
	 *         {@link ModelUnit}.
	 */
	Object getVisual(ModelUnit unit);

	/**
	 * Retrieves the visual counterpart to the specified {@link ModelLinkState}.
	 * 
	 * @param link
	 *            The {@link ModelLinkState}.
	 * @return The {@link View} specific object for displaying the specified
	 *         {@link ModelLinkState}.
	 */
	Object getVisual(ModelLinkState link);

}
