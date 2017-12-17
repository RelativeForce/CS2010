package peril.ai.api;

import java.util.function.Consumer;

import peril.Game;

/**
 * This controller handles all interactions that an {@link AI} could have with
 * the {@link Game}.
 * 
 * @author Joshua_Eddy
 *
 */
public class RequestHandler implements Controller {

	/**
	 * The instance of the {@link Game} that this {@link RequestHandler} uses.
	 */
	private final Game game;

	/**
	 * Constructs a new {@link RequestHandler}.
	 * 
	 * @param game
	 *            The instance of the {@link Game} that this {@link RequestHandler}
	 *            uses.
	 */
	public RequestHandler(Game game) {
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

		// Get the id of the current state.
		int currentStateId = game.getCurrentStateID();

		// Ensure that the parameter Country is a valid country, This should never be
		// false.
		if (!(country instanceof peril.board.Country)) {
			throw new IllegalArgumentException("The parmameter country is not a valid country.");
		}

		peril.board.Country checkedCountry = (peril.board.Country) country;

		if (currentStateId == game.states.reinforcement.getID()) {
			return game.states.reinforcement.select(checkedCountry);
		} else if (currentStateId == game.states.combat.getID()) {
			return game.states.combat.select(checkedCountry);
		} else if (currentStateId == game.states.movement.getID()) {
			return game.states.movement.select(checkedCountry);
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
		if (game.getCurrentStateID() != game.states.combat.getID()) {
			throw new IllegalStateException("You can only attack during the combat state.");
		}

		// Check both countries are selected.
		if (game.states.combat.getPrimary() == null || game.states.combat.getSecondary() == null) {
			throw new IllegalStateException("There is NOT two countries selected. Select two valid countries.");
		}

		game.menus.warMenu.show();
		game.menus.warMenu.selectMaxDice();
		game.menus.warMenu.attack();

		// Cant attack any more hide the war menu.
		if (game.states.combat.getPrimary() == null || game.states.combat.getSecondary() == null) {
			game.menus.warMenu.hide();
			game.states.movement.removeSelected();
		}

	}

	/**
	 * Fortifies one {@link Country} with one unit from another friendly
	 * {@link Country}.
	 */
	@Override
	public void fortify() {

		// Check correct state
		if (game.getCurrentStateID() != game.states.movement.getID()) {
			throw new IllegalStateException("You can only attack during the fortify state.");
		}

		// Check both countries are selected.
		if (game.states.movement.getPrimary() == null || game.states.movement.getSecondary() == null) {
			throw new IllegalStateException("There is NOT two countries selected. Select two valid countries.");
		}

		game.states.movement.fortify();

	}

	/**
	 * Checks if there is a valid path between two friendly {@link Country}s.
	 */
	@Override
	public boolean isPathBetween(Country a, Country b) {

		// Ensure that the parameter Country is a valid country, This should never be
		// false.
		if (!(a instanceof peril.board.Country)) {
			throw new IllegalArgumentException("The parmameter 'A' country is not a valid country.");
		}

		peril.board.Country checkedA = (peril.board.Country) a;

		// Ensure that the parameter Country is a valid country, This should never be
		// false.
		if (!(a instanceof peril.board.Country)) {
			throw new IllegalArgumentException("The parmameter border country is not a valid country.");
		}

		peril.board.Country checkedB = (peril.board.Country) b;

		return game.states.movement.isPathBetween(checkedA, checkedB);
	}

	/**
	 * Clears the selected {@link Country}s from all the game states.
	 */
	public void clearSelected() {
		game.states.movement.removeSelected();
		game.states.combat.removeSelected();
		game.states.reinforcement.removeSelected();
	}

	/**
	 * Reinforces the selected {@link Country} with one unit.
	 */
	@Override
	public void reinforce() {

		// Check correct state
		if (game.getCurrentStateID() != game.states.reinforcement.getID()) {
			throw new IllegalStateException("You can only attack during the reinforcement state.");
		}

		// Check both countries are selected.
		if (game.states.reinforcement.getSelected() == null) {
			throw new IllegalStateException("There is valid country selected.");
		}
		game.states.reinforcement.reinforce();

	}

	/**
	 * Retrieves the current {@link Player} active in the game.
	 */
	@Override
	public Player getCurrentPlayer() {
		return game.players.getCurrent();
	}

}
