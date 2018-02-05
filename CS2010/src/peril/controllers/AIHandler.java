package peril.controllers;

import java.util.function.Consumer;

import peril.Game;
import peril.controllers.api.Board;
import peril.controllers.api.Country;
import peril.controllers.api.Player;
import peril.helpers.UnitHelper;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;

public final class AIHandler implements AIController {

	private final Game game;
	
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

		ModelCountry checkedCountry = (ModelCountry) country;

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
		
		final ModelCountry primary = game.states.movement.getPrimary();
		final ModelCountry secondary = game.states.movement.getSecondary();

		// Check both countries are selected.
		if (primary == null || secondary == null) {
			throw new IllegalStateException("There is NOT two countries selected. Select two valid countries.");
		}
		
		ModelUnit unit = UnitHelper.getInstance().getWeakest();
		
		// If there is a selected unit fortify the country with that.
		if(primary.getArmy().getSelected() != null) {
			unit = primary.getArmy().getSelected();
		}

		game.states.movement.fortify(unit);
	}

	/**
	 * Checks if there is a valid path between two friendly {@link Country}s.
	 */
	@Override
	public boolean isPathBetween(Country a, Country b) {

		// Ensure that the parameter Country is a valid country, This should never be
		// false.
		if (!(a instanceof ModelCountry)) {
			throw new IllegalArgumentException("The parmameter 'A' country is not a valid country.");
		}

		final ModelCountry checkedA = (ModelCountry) a;

		// Ensure that the parameter Country is a valid country, This should never be
		// false.
		if (!(b instanceof ModelCountry)) {
			throw new IllegalArgumentException("The parmameter 'B' country is not a valid country.");
		}

		final ModelCountry checkedB = (ModelCountry) b;

		return !game.states.movement.getPathBetween(checkedA, checkedB, UnitHelper.getInstance().getWeakest()).isEmpty();
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

		// Check correct state
		if (!game.view.isCurrentState(game.states.reinforcement)) {
			throw new IllegalStateException("You can only attack during the reinforcement state.");
		}

		// Check both countries are selected.
		if (game.states.reinforcement.getSelected(0) == null) {
			throw new IllegalStateException("There is valid country selected.");
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