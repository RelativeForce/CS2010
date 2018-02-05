package peril.controllers;

import java.util.List;
import java.util.function.Consumer;

import peril.Challenge;
import peril.Game;
import peril.ai.AI;
import peril.controllers.api.Board;
import peril.controllers.api.Country;
import peril.controllers.api.Player;
import peril.helpers.ModelStateHelper;
import peril.helpers.UnitHelper;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.states.Attack;
import peril.model.states.Fortify;
import peril.model.states.Reinforce;
import peril.model.states.Setup;
import peril.views.View;

/**
 * This controller handles all interactions that an {@link AI} or {@link View}
 * could have with the {@link Game}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class RequestHandler implements AIController, GameController {

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
		UnitHelper.getInstance().clear();
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

	@Override
	public void checkWinner() {
		game.checkWinner();
	}

	@Override
	public void checkContinentRulership() {
		game.checkContinentRulership();
	}

	@Override
	public Attack getAttack() {
		return game.states.combat;
	}

	@Override
	public Reinforce getReinforce() {
		return game.states.reinforcement;
	}

	@Override
	public Fortify getFortify() {
		return game.states.movement;
	}

	@Override
	public Setup getSetup() {
		return game.states.setup;
	}

	@Override
	public String getMusicPath() {
		return game.assets.music;
	}

	@Override
	public void setCurrentPlayer(ModelPlayer model) {
		game.players.setCurrent(model);
	}

	@Override
	public void setRoundNumber(int roundNumber) {
		game.setRoundNumber(roundNumber);
	}

	@Override
	public void addChallenge(Challenge challenge) {
		game.players.addChallenge(challenge);
	}

	@Override
	public List<Challenge> getChallenges() {
		return game.players.challenges;
	}

	@Override
	public void forEachModelPlayer(Consumer<ModelPlayer> task) {
		game.players.forEach(task);
	}

	@Override
	public void forEachLoser(Consumer<ModelPlayer> task) {
		game.view.forEachLoser(task);
	}

	@Override
	public int getRoundNumber() {
		return game.getRoundNumber();
	}

	@Override
	public void confirmReinforce() {
		game.view.enterCombat();
	}

	public void confirmCombat() {
		game.view.enterFortify();
	}

	public void confirmSetup() {

		// Checks the ownership of the continents
		checkContinentRulership();

		// Change the state of the game to reinforcement and give player one their units
		// based on the countries they own.
		game.players.reinforceCurrent();

		game.view.enterReinforce();
	}

	public void confirmMovement() {

		// Move to the next player
		nextPlayer();

		// Enter the reinforce state
		game.view.enterReinforce();
		
		System.gc();
	}

	@Override
	public void autoDistributeCountries() {
		getSetup().autoDistributeCountries(getModelBoard(), game.players);
	}

	@Override
	public void setLoser(ModelPlayer player) {
		game.players.setLoser(player);
		game.view.addLoser(player);
	}

	@Override
	public void processAI(int delta) {

		if (getCurrentModelPlayer().ai != AI.USER && !game.view.isPaused()) {
			
			final View view = game.view;
			final ModelStateHelper states = game.states;
			final ModelPlayer current = getCurrentModelPlayer();

			if (view.isCurrentState(states.reinforcement) && !current.ai.reinforce(delta)) {
				view.enterCombat();
			} else if (view.isCurrentState(states.combat) && !current.ai.attack(delta)) {
				view.enterFortify();
			} else if (view.isCurrentState(states.movement) && !current.ai.fortify(delta)) {
				view.enterReinforce();
				nextPlayer();
			}

		}

	}
	
	@Override
	public void addPoints(int points) {
		ModelPlayer currentPlayer = game.players.getCurrent();
		currentPlayer.setPoints(currentPlayer.getPoints());
	}

}
