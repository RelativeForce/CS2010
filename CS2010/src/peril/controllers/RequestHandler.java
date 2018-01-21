package peril.controllers;

import java.util.function.Consumer;
import peril.Game;
import peril.controllers.api.Board;
import peril.controllers.api.Country;
import peril.controllers.api.Player;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.views.View;

/**
 * This controller handles all interactions that an {@link AI} could have with
 * the {@link Game}.
 * 
 * @author Joshua_Eddy
 *
 */
public class RequestHandler implements AIController, GameController {

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
		if (!(a instanceof ModelCountry)) {
			throw new IllegalArgumentException("The parmameter 'A' country is not a valid country.");
		}

		ModelCountry checkedA = (ModelCountry) a;

		// Ensure that the parameter Country is a valid country, This should never be
		// false.
		if (!(b instanceof ModelCountry)) {
			throw new IllegalArgumentException("The parmameter 'B' country is not a valid country.");
		}

		ModelCountry checkedB = (ModelCountry) b;

		return !game.states.movement.getPathBetween(checkedA, checkedB).isEmpty();
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

	@Override
	public void setHelpMenuPage(int pageId) {
		game.view.setHelpMenuPage(pageId);
	}

	@Override
	public View getView() {
		return game.view;
	}
	
	@Override
	public String getUIPath() {
		return game.assets.ui;
	}

	@Override
	public String getMapsPath() {
		return game.assets.maps;
	}

	@Override
	public void resetGame() {
		game.players.emptyPlaying();
		game.setRoundNumber(0);
		game.players.challenges.clear();
		
	}

	@Override
	public void setBoardName(String name) {
		game.board.setName(name);
	}

	@Override
	public void addPlayer(ModelPlayer player) {
		game.players.addPlayer(player);
	}

	@Override
	public AIController getAIController() {
		return this;
	}

	@Override
	public ModelBoard getModelBoard() {
		return game.board;
	}

	@Override
	public ModelPlayer getCurrentModelPlayer() {
		return game.players.getCurrent();
	}

	@Override
	public void forEachModelCountry(Consumer<ModelCountry> task) {
		game.board.forEachCountry(task);
	}

	@Override
	public boolean isPlaying(int playerNumber) {
		return game.players.isPlaying(playerNumber);
	}

	@Override
	public ModelPlayer getModelPlayer(int playerNumber) {
		return game.players.getPlayer(playerNumber);
	}

	@Override
	public void checkChallenges() {
		game.players.checkChallenges();
	}

	@Override
	public void nextPlayer() {
		game.players.nextPlayer();
	}

}
