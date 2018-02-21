package peril.controllers;

import java.util.List;
import java.util.function.Consumer;

import peril.Challenge;
import peril.Game;
import peril.ai.AI;
import peril.helpers.AIHelper;
import peril.helpers.ModelStateHelper;
import peril.helpers.UnitHelper;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.states.Attack;
import peril.model.states.Fortify;
import peril.model.states.ModelState;
import peril.model.states.Reinforce;
import peril.model.states.Setup;
import peril.views.View;

/**
 * This controller handles all interactions between the {@link View} and
 * {@link Game}. This realises {@link GameController}.
 * 
 * @author Joshua_Eddy
 * 
 * @see GameController
 * 
 * @version 1.01.03
 * @since 2018-02-06
 *
 */
public final class GameHandler implements GameController {

	/**
	 * The instance of the {@link Game} that this {@link GameHandler} uses.
	 */
	private final Game game;

	/**
	 * Constructs a new {@link GameHandler}.
	 * 
	 * @param game
	 *            The instance of the {@link Game} that this {@link GameHandler}
	 *            uses.
	 */
	public GameHandler(Game game) {
		this.game = game;
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
	public void resetGame() {
		
		// Delete all the players and clear the challenges.
		game.players.emptyPlaying();
		game.players.challenges.clear();
		
		// Reset the game back to round zero
		game.setRoundNumber(0);
		
		// Remove all the units.
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
		return game.getAIController();
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
		return game.states.attack;
	}

	@Override
	public Reinforce getReinforce() {
		return game.states.reinforce;
	}

	@Override
	public Fortify getFortify() {
		return game.states.fortify;
	}

	@Override
	public Setup getSetup() {
		return game.states.setup;
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

		// Call the garbage collector
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

			if (view.isCurrentState(states.reinforce) && !current.ai.reinforce(delta)) {
				view.enterCombat();
			} else if (view.isCurrentState(states.attack) && !current.ai.attack(delta)) {
				view.enterFortify();
			} else if (view.isCurrentState(states.fortify) && !current.ai.fortify(delta)) {
				view.enterReinforce();
				nextPlayer();
			}

		}

	}

	@Override
	public void addPoints(int points) {
		
		// Add the specified number of points to the current player.
		final ModelPlayer currentPlayer = game.players.getCurrent();
		currentPlayer.setPoints(currentPlayer.getPoints());
	}

	@Override
	public Directory getDirectory() {
		return game.assets;
	}

	@Override
	public ModelState getCurrentState() {
		
		final ModelStateHelper states = game.states;
		final View view = game.view;
		
		if (view.isCurrentState(states.reinforce)) {
			return states.reinforce;
		} else if (view.isCurrentState(states.attack)) {
			return states.attack;
		} else if (view.isCurrentState(states.fortify)) {
			return states.fortify;
		}
		
		return null;
	}

	@Override
	public AIHelper getAIs() {
		return game.aiHelper;
	}

}
