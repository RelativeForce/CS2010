package peril.ai.api;

import java.util.function.Consumer;

/**
 * The API for all user/AI interactions with the game states.
 * 
 * @author Joshua_Eddy
 *
 */
public interface Controller {

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
	 * Performs an attack from the first {@link Country} to the second
	 * {@link Country} that was selected using {@link Controller#select(Country)}.
	 * This method also handles hiding an showing the UI elements of combat.<br>
	 * <br>
	 * This method will throw exceptions in the following scenarios:
	 * <ul>
	 * <li>The games current state is not 'Combat'.</li>
	 * <li>There is not two countries selected using
	 * {@link Controller#select(Country)}.</li>
	 * </ul>
	 *
	 */
	void attack();

	/**
	 * Fortifies the second {@link Country} with one unit from the first
	 * {@link Country} to that was selected using
	 * {@link Controller#select(Country)}.
	 * 
	 * <br>
	 * This method will throw exceptions in the following scenarios:
	 * <ul>
	 * <li>The games current state is not 'Fortify'.</li>
	 * <li>There is not two countries selected using
	 * {@link Controller#select(Country)}.</li>
	 * </ul>
	 */
	void fortify();

	/**
	 * A pre-check for {@link Controller#fortify()}, this retrieves whether or not
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
	 * Reinforces the {@link Country} selected using
	 * {@link Controller#select(Country)} with one unit.
	 */
	void reinforce();

	/**
	 * Retrieves the current {@link Player}.
	 * 
	 * @return
	 */
	Player getCurrentPlayer();
}
