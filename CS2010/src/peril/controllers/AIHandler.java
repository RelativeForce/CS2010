package peril.controllers;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import peril.Game;
import peril.controllers.api.Board;
import peril.controllers.api.Country;
import peril.controllers.api.Player;
import peril.helpers.PointHelper;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.states.ModelState;

/**
 * The controller for all AI -> {@link Game} interactions.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.03
 * @since 2018-02-06
 * 
 * @see AIController
 *
 */
public final class AIHandler implements AIController {

	/**
	 * The {@link Game} that this {@link AIHandler} interacts with.
	 */
	private final Game game;

	/**
	 * Constructs an new {@link AIHandler}.
	 * 
	 * @param game
	 *            The {@link Game} that this {@link AIHandler} interacts with.
	 */
	public AIHandler(Game game) {
		this.game = game;
	}

	/**
	 * Retrieves the {@link Board}.
	 */
	@Override
	public Board getBoard() {
		return game.board;
	}

	/**
	 * Selects the {@link Country} from the current state.
	 */
	@Override
	public boolean select(Country country) {

		// Ensure that the parameter Country is a valid country, This should never be
		// false.
		if (!(country instanceof ModelCountry)) {
			throw new IllegalArgumentException("The parmameter country is not a valid country.");
		}

		// Cast to a model country
		final ModelCountry checkedCountry = (ModelCountry) country;

		// The current model state
		final ModelState state = game.getGameController().getCurrentState();

		// If the state is null then the current state is not a game state.
		if (state != null) {
			return state.select(checkedCountry, game.getGameController());
		} else {
			throw new IllegalStateException("The current state is not a valid game state.");
		}
	}

	/**
	 * Performs a {@link Consumer} task on each {@link Country} on the
	 * {@link Board}.
	 */
	@Override
	public void forEachCountry(Consumer<Country> task) {
		getBoard().getCountries().forEach(task);
	}

	/**
	 * Performs an attack between the two selected {@link Country}s
	 */
	@Override
	public void attack() {

		// Check correct state
		if (!game.view.isCurrentState(game.states.attack)) {
			throw new IllegalStateException("You can only attack during the combat state.");
		}

		// Check both countries are selected.
		if (game.states.attack.getPrimary() == null || game.states.attack.getSecondary() == null) {
			throw new IllegalStateException("There is NOT two countries selected. Select two valid countries.");
		}

		game.view.attack();

	}

	/**
	 * Fortifies one {@link Country} with one unit from another friendly
	 * {@link Country}.
	 */
	@Override
	public void fortify() {

		// Check correct state
		if (!game.view.isCurrentState(game.states.fortify)) {
			throw new IllegalStateException("You can only attack during the fortify state.");
		}

		// Cast to model countries.
		final ModelCountry primary = game.states.fortify.getPrimary();
		final ModelCountry secondary = game.states.fortify.getSecondary();

		// Check both countries are selected.
		if (primary == null || secondary == null) {
			throw new IllegalStateException("There is NOT two countries selected. Select two valid countries.");
		}

		// Fortify
		game.states.fortify.fortify();
	}

	/**
	 * Checks if there is a valid path between two friendly {@link Country}s.
	 */
	@Override
	public boolean isPathBetween(Country source, Country destination) {

		// Ensure that both the parameter Country is a valid model country, This should
		// never be false.
		if (!(source instanceof ModelCountry)) {
			throw new IllegalArgumentException("The parmameter 'source' country is not a valid country.");
		} else if (!(destination instanceof ModelCountry)) {
			throw new IllegalArgumentException("The parmameter 'destination' country is not a valid country.");
		}

		final ModelCountry checkedSource = (ModelCountry) source;
		final ModelCountry checkedDestination = (ModelCountry) destination;

		// Whether of not there is a unit selected in the current country's army.
		final boolean unitSelected = checkedSource.getArmy().getSelected() != null;

		// If there is a selected unit fortify the country with that otherwise use the
		// weakest unit in the army.
		final ModelUnit unit = unitSelected ? checkedSource.getArmy().getSelected()
				: checkedSource.getArmy().getWeakestUnit();

		// Return whether or not the path between the countries is not empty. If the
		// path is empty then there is no valid path.
		return !game.states.fortify.getPathBetween(checkedSource, checkedDestination, unit).isEmpty();
	}

	/**
	 * Clears the selected {@link Country}s from all the game states.
	 */
	public void clearSelected() {
		game.states.clearAll();
	}

	/**
	 * Reinforces the selected {@link Country} with one unit.
	 */
	@Override
	public void reinforce() {

		// Check the current state is reinforce
		if (!game.view.isCurrentState(game.states.reinforce)) {
			throw new IllegalStateException("You can only reinforce during the reinforcement state.");
		}

		// Check there is a country selected.
		if (game.states.reinforce.numberOfSelected() == 0) {
			throw new IllegalStateException("There is no country selected.");
		}

		game.states.reinforce.reinforce(game.getGameController());

	}

	/**
	 * Retrieves the current {@link Player} active in the game.
	 */
	@Override
	public Player getCurrentPlayer() {
		return game.players.getCurrent();
	}

	/**
	 * Performs a {@link Consumer} task on every {@link Country} that is owned by
	 * the specified {@link Player}.
	 */
	@Override
	public void forEachFriendlyCountry(Player player, Consumer<Country> task) {

		// Check parameters
		if (player == null) {
			throw new NullPointerException("Player cannot be null.");
		} else if (task == null) {
			throw new NullPointerException("Task cannot be null.");
		}

		// Iterate over every country on the board
		getBoard().getCountries().forEach(country -> {

			// If the country is owned by the current player perform the task.
			if (player.equals(country.getOwner())) {
				task.accept(country);
			}
		});

	}

	/**
	 * Retrieves all the {@link Player}s that are currently active in the game.
	 */

	@Override
	public Set<? extends Player> getPlayers() {

		final Set<Player> players = new HashSet<>();

		// Add all the current players to the set of players
		game.getGameController().forEachModelPlayer(player -> players.add(player));

		return players;
	}

	/**
	 * Performs a {@link Consumer} task on every {@link Country} that is not owned
	 * by the {@link Player} owner of the specified {@link Country} and is a
	 * neighbour of that {@link Country}.
	 */

	@Override
	public void forEachEnemyNeighbour(Country country, Consumer<Country> task) {

		if (country.getOwner() == null) {
			throw new NullPointerException("The specifed country must be rulled by a player.");
		}

		// Iterate over every neighbour of the specified country
		country.getNeighbours().forEach(neighbour -> {
			
			// If the neighbour is an enemy country then perform the task.
			if (!country.getOwner().equals(neighbour.getOwner())) {
				task.accept(neighbour);
			}
		});

	}
	
	/**
	 * Retrieves the {@link PointHelper} handling all the point reward values for actions in the game.
	 * 
	 * @return {@link PointHelper}
	 */
	@Override
	public Points getPoints() {
		return PointHelper.getInstance();
	}

}
