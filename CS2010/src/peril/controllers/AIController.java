package peril.controllers;

import java.util.Set;
import java.util.function.Consumer;

import peril.controllers.api.Army;
import peril.controllers.api.Board;
import peril.controllers.api.Country;
import peril.controllers.api.Player;
import peril.controllers.api.Unit;

/**
 * The API for all AI interactions with the game. This API is unable to directly
 * change any aspects of the game but it does serve as proxy between the AI and
 * the user controlled {@link Player}s.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.04
 * @since 2018-02-21
 *
 */
public interface AIController {

	/**
	 * Retrieves the {@link Board}.
	 * 
	 * @return <{@link Board} 
	 * 			Returns the board.
	 * 
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

	/**
	 * Performs a {@link Consumer} task on every {@link Country} that is owned by
	 * the specified {@link Player}.
	 * 
	 * @param player
	 *            {@link Player}
	 * @param task
	 *            {@link Consumer}
	 */
	void forEachFriendlyCountry(Player player, Consumer<Country> task);

	/**
	 * Retrieves all the {@link Player}s that are currently active in the game.
	 * 
	 * @return {@link Set} of {@link Player}s
	 */
	Set<? extends Player> getPlayers();

	/**
	 * Performs a {@link Consumer} task on every {@link Country} that is not owned
	 * by the {@link Player} owner of the specified {@link Country} and is a
	 * neighbour of that {@link Country}.
	 * 
	 * @param country
	 *            {@link Country}
	 * @param task
	 *            {@link Consumer}
	 */
	void forEachEnemyNeighbour(Country country, Consumer<Country> task);

	/**
	 * Retrieves all the point reward values for actions in the game.
	 * 
	 * @return {@link Points}
	 */
	Points getPoints();

	/**
	 * Converts as many of the {@link Unit}s from this {@link Army} into the
	 * {@link Unit} above in terms of strength. If no {@link Unit}s were traded then
	 * this method will return false;
	 * 
	 * @param unit
	 *            Specified {@link Unit} to trade up.
	 * @param country
	 *            The {@link Country} containing the unit.
	 * @return Whether or not any {@link Unit}s were traded up.
	 */
	boolean tradeUnit(Country country, Unit unit);

	/**
	 * Retrieves whether the link between two {@link Country}s is open.
	 * 
	 * @param country
	 *            The {@link Country} that is will be the source of the traversal.
	 * @param neighbour
	 *            The {@link Country} that is will be the destination of the
	 *            traversal.
	 * @return Whether the link between two {@link Country}s is open.
	 */
	boolean hasOpenLinkBetween(Country country, Country neighbour);

	/**
	 * Creates a blockade that prevents the neighbour {@link Country} from
	 * transferring units to the source {@link Country}. This also prevents attack
	 * from the neighbour however the source can still attack the neighbour.
	 * Blockades last 3 rounds.
	 * 
	 * @param source
	 *            The {@link Country} that is will be the source of the blockade.
	 * @param neighbour
	 *            The {@link Country} that is will be blocked from transferring
	 *            units.
	 */
	void createBlockade(Country source, Country neighbour);

	/**
	 * Retrieves the {@link Unit} above the specified {@link Unit} in terms of
	 * strength.
	 * 
	 * @return {@link Unit}
	 * @param unit
	 *            The {@link Unit} above the specifed unit in terms of strength.
	 */
	Unit getUnitAbove(Unit unit);
}
