package peril.model.states.combat;

import static org.junit.Assert.*;

import java.util.List;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

import peril.Challenge;
import peril.ai.AI;
import peril.controllers.AIController;
import peril.controllers.Directory;
import peril.controllers.GameController;
import peril.helpers.AIHelper;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.board.links.ModelLinkState;
import peril.model.states.Fortify;
import peril.model.states.ModelState;
import peril.model.states.Reinforce;
import peril.model.states.Setup;
import peril.views.View;

/**
 * 
 * This tests the {@link Attack} functions properly.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-03-12
 * @version 1.01.01
 * 
 * @see Attack
 * @see GameController
 *
 */
public final class Test_Attack {

	/**
	 * The {@link Attack} that is the subject of these tests.
	 */
	private Attack testAttack;

	/**
	 * The {@link ModelPlayer} that is currently active in the game. This is the
	 * {@link ModelPlayer} that will be returned from
	 * {@link TestGC#getCurrentModelPlayer()}.
	 */
	private ModelPlayer testCurrentPlayer;

	/**
	 * Another {@link ModelPlayer} that serves as the enemy to the
	 * {@link #testCurrentPlayer}.
	 */
	private ModelPlayer testEnemyPlayer;

	/**
	 * Sets up the elements that will be used by every test to test {@link Attack}.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.testCurrentPlayer = new ModelPlayer(1, AI.USER);
		this.testEnemyPlayer = new ModelPlayer(2, AI.USER);
		this.testAttack = new Attack(new TestGC());

	}

	/**
	 * This method asserts that two valid {@link ModelCountry} can be selected by
	 * the {@link Attack}. The primary country should be owned by the current player
	 * and the second should be the target for the attack. The target should be
	 * neutral or an enemy and be a neighbour with an open {@link ModelLinkState}
	 * from the primary. There must be a primary in order for the target to be
	 * selected.
	 */
	@Test
	public void test_select_normal() {
		fail("Not yet implemented");
	}

	/**
	 * This method asserts that two {@link ModelCountry}s ruled by the same
	 * {@link ModelPlayer} can not be selected at the same time.
	 */
	@Test
	public void test_select_friendlyCountries() {
		fail("Not yet implemented");
	}

	/**
	 * This method asserts that a valid enemy {@link ModelCountry} cannot be
	 * selected before the valid friendly {@link ModelCountry}.
	 */
	@Test
	public void test_select_wrongOrder() {
		fail("Not yet implemented");
	}

	/**
	 * This method asserts that two otherwise valid {@link ModelCountry}s cannot be
	 * selected if they are not neighbours of each other.
	 */
	@Test
	public void test_select_notNeighbours() {
		fail("Not yet implemented");
	}

	/**
	 * This method asserts that two otherwise valid {@link ModelCountry}s cannot be
	 * selected if they is a blockade between them.
	 */
	@Test
	public void test_select_blockadedLink() {
		fail("Not yet implemented");
	}

	/**
	 * A version of the {@link GameController} that is tailored to only provide the
	 * functionality used for {@link Test_Attack}. All operations that are should
	 * not be required by {@link Attack} will throw an
	 * {@link UnsupportedOperationException}.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @version 1.01.01
	 * @since 2018-03-12
	 *
	 */
	private final class TestGC implements GameController {

		@Override
		public ModelPlayer getCurrentModelPlayer() {
			// Return the test current player.
			return testCurrentPlayer;
		}

		@Override
		public ModelBoard getModelBoard() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public ModelPlayer getModelPlayer(int playerNumber) {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public Attack getAttack() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public Reinforce getReinforce() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public Fortify getFortify() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public Setup getSetup() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public List<Challenge> getChallenges() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public View getView() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public AIController getAIController() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public Directory getDirectory() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void setHelpMenuPage(int pageId) {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void resetGame() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void setBoardName(String name) {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void addPlayer(ModelPlayer player) {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void forEachModelCountry(Consumer<ModelCountry> task) {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public boolean isPlaying(int playerNumber) {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void checkChallenges() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void nextPlayer() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void checkWinner() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void checkContinentRulership() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void setCurrentPlayer(ModelPlayer model) {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void setRoundNumber(int parseInt) {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void addChallenge(Challenge challenge) {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void forEachModelPlayer(Consumer<ModelPlayer> task) {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void forEachLoser(Consumer<ModelPlayer> task) {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public int getRoundNumber() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void confirmReinforce() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void confirmCombat() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void confirmSetup() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void confirmMovement() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void autoDistributeCountries() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void setLoser(ModelPlayer player) {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void addPoints(int points) {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public void processAI(int delta) {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public ModelState getCurrentState() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}

		@Override
		public AIHelper getAIs() {
			throw new UnsupportedOperationException("Attack should not require this method.");
		}
	}

}
