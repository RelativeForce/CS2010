package peril.controllers;

import java.util.function.Consumer;

import peril.controllers.api.Board;
import peril.controllers.api.Country;
import peril.controllers.api.Player;

/**
 * The API for all AI interactions with the game. This API is unable to directly
 * change any aspects of the game but it does serve as proxy between the AI and
 * the user controlled {@link Player}s.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-02-06
 *
 */
public interface AIController {

	/**
	 * Retrieves the {@link Board}.
	 */
	Board getBoard();

	/**
	 * Selects the {@link Country} from the current state.
	 * 
	 * @param country
	 *            {@link Country} to select.
	 * @return Whether or not the {@link Country} was selected.
	 */
	boolean select(Country country);

	/**
	 * Performs a {@link Consumer} function on all the {@link Country}s on the
	 * {@link Board}.
	 * 
	 * @param task
	 *            {@link Consumer} of type {@link Country}.
	 */
	void forEachCountry(Consumer<Country> task);

	/**
	 * Reinforces the {@link Country} selected using
	 * {@link AIController#select(Country)} with one unit.
	 */
	void reinforce();

	/**
	 * Performs an attack from the first {@link Country} to the second
	 * {@link Country} that was selected using {@link AIController#select(Country)}.
	 * This method also handles hiding an showing the UI elements of combat.<br>
	 * <br>
	 * This method will throw exceptions in the following scenarios:
	 * <ul>
	 * <li>The games current state is not 'Combat'.</li>
	 * <li>There is not two countries selected using
	 * {@link AIController#select(Country)}.</li>
	 * </ul>
	 *
	 */
	void attack();

	/**
	 * Fortifies the second {@link Country} with one unit from the first
	 * {@link Country} to that was selected using
	 * {@link AIController#select(Country)}.
	 * 
	 * <br>
	 * This method will throw exceptions in the following scenarios:
	 * <ul>
	 * <li>The games current state is not 'Fortify'.</li>
	 * <li>There is not two countries selected using
	 * {@link AIController#select(Country)}.</li>
	 * </ul>
	 */
	void fortify();

	/**
	 * A pre-check for {@link AIController#fortify()}, this retrieves whether or not
	 * there is a valid path between two {@link Country}s owned by the same
	 * {@link Player}.
	 * 
	 * @param a
	 *            The first {@link Country}
	 * @param b
	 *            The second {@link Country}
	 * @return <code>boolean</code>
	 */
	boolean isPathBetween(Country a, Country b);

	/**
	 * Clears the currently selected {@link Country}(s) from all the states.
	 */
	void clearSelected();

	/**
	 * Retrieves the current {@link Player}.
	 * 
	 * @return {@link Player}
	 */
	Player getCurrentPlayer();
}
