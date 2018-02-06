package peril.controllers;

import java.util.function.Consumer;

import peril.Game;
import peril.controllers.api.Board;
import peril.controllers.api.Country;
import peril.controllers.api.Player;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;

/**
 * The controller for all AI -> {@link Game} interactions.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
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

		// Determine the current state and then select the check country in it.
		if (game.view.isCurrentState(game.states.reinforcement)) {
			return game.states.reinforcement.select(checkedCountry, game.getGameController());
		} else if (game.view.isCurrentState(game.states.combat)) {
			return game.states.combat.select(checkedCountry, game.getGameController());
		} else if (game.view.isCurrentState(game.states.movement)) {
			return game.states.movement.select(checkedCountry, game.getGameController());
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
		if (!game.view.isCurrentState(game.states.combat)) {
			throw new IllegalStateException("You can only attack during the combat state.");
		}

		// Check both countries are selected.
		if (game.states.combat.getPrimary() == null || game.states.combat.getSecondary() == null) {
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
		if (!game.view.isCurrentState(game.states.movement)) {
			throw new IllegalStateException("You can only attack during the fortify state.");
		}

		// Cast to model countries.
		final ModelCountry primary = game.states.movement.getPrimary();
		final ModelCountry secondary = game.states.movement.getSecondary();

		// Check both countries are selected.
		if (primary == null || secondary == null) {
			throw new IllegalStateException("There is NOT two countries selected. Select two valid countries.");
		}

		// Whether of not there is a unit selected in the current country's army.
		final boolean unitSelected = primary.getArmy().getSelected() != null;

		// If there is a selected unit fortify the country with that otherwise use the
		// weakest unit in the army.
		final ModelUnit unit = unitSelected ? primary.getArmy().getSelected() : primary.getArmy().getWeakestUnit();

		// Fortify
		game.states.movement.fortify(unit);
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
		return !game.states.movement.getPathBetween(checkedSource, checkedDestination, unit).isEmpty();
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
		if (!game.view.isCurrentState(game.states.reinforcement)) {
			throw new IllegalStateException("You can only reinforce during the reinforcement state.");
		}

		// Check there is a country selected.
		if (game.states.reinforcement.numberOfSelected() == 0) {
			throw new IllegalStateException("There is no country selected.");
		}

		game.states.reinforcement.reinforce(game.getGameController());

	}

	/**
	 * Retrieves the current {@link Player} active in the game.
	 */
	@Override
	public Player getCurrentPlayer() {
		return game.players.getCurrent();
	}

}
