package peril.model.states;

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
import peril.helpers.UnitHelper;
import peril.model.ModelColor;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.board.links.ModelLink;
import peril.model.board.links.ModelLinkState;
import peril.views.View;

/**
 * Tests that {@link Fortify} functions properly.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-03-15
 * @version 1.01.01
 * 
 * @see Fortify
 *
 */
public final class Test_Fortify {

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
	 * The start of the {@link ModelCountry} chain.
	 */
	private ModelCountry testSource;

	/**
	 * The end of the {@link ModelCountry} chain.
	 */
	private ModelCountry testDestination;

	/**
	 * The {@link Fortify} being tested.
	 */
	private Fortify testFortify;

	/**
	 * Sets up the elements that will be used by every test to test {@link Fortify}.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.testCurrentPlayer = new ModelPlayer(1, AI.USER);
		this.testEnemyPlayer = new ModelPlayer(2, AI.USER);
		this.testFortify = new Fortify(new TestGC());

		// Set the players as owning one country
		this.testCurrentPlayer.setCountriesRuled(1);
		this.testEnemyPlayer.setCountriesRuled(1);

		final ModelUnit testUnit1 = new ModelUnit("testUnit1", 1, "na");

		UnitHelper.getInstance().clear();
		UnitHelper.getInstance().addUnit(testUnit1);

		final ModelColor color = new ModelColor(0, 0, 0);

		this.testSource = new ModelCountry("source", color);
		this.testSource.setRuler(testCurrentPlayer);

		final ModelCountry chainMember1 = new ModelCountry("test2", color);
		chainMember1.setRuler(testCurrentPlayer);

		final ModelCountry chainMember2 = new ModelCountry("test3", color);
		chainMember2.setRuler(testCurrentPlayer);

		final ModelCountry chainMember3 = new ModelCountry("test4", color);
		chainMember3.setRuler(testCurrentPlayer);

		final ModelCountry chainMember4 = new ModelCountry("test5", color);
		chainMember4.setRuler(testCurrentPlayer);

		final ModelCountry chainMember5 = new ModelCountry("test6", color);
		chainMember5.setRuler(testCurrentPlayer);

		this.testDestination = new ModelCountry("destination", color);
		this.testDestination.setRuler(testCurrentPlayer);

		// Create the links from source -> destination
		testSource.addNeighbour(chainMember1, new ModelLink(ModelLinkState.OPEN));
		chainMember1.addNeighbour(chainMember2, new ModelLink(ModelLinkState.OPEN));
		chainMember2.addNeighbour(chainMember3, new ModelLink(ModelLinkState.OPEN));
		chainMember3.addNeighbour(chainMember4, new ModelLink(ModelLinkState.OPEN));
		chainMember4.addNeighbour(chainMember5, new ModelLink(ModelLinkState.OPEN));
		chainMember5.addNeighbour(testDestination, new ModelLink(ModelLinkState.OPEN));

		// Create the links from destination -> source
		testDestination.addNeighbour(chainMember5, new ModelLink(ModelLinkState.OPEN));
		chainMember5.addNeighbour(chainMember4, new ModelLink(ModelLinkState.OPEN));
		chainMember4.addNeighbour(chainMember3, new ModelLink(ModelLinkState.OPEN));
		chainMember3.addNeighbour(chainMember2, new ModelLink(ModelLinkState.OPEN));
		chainMember2.addNeighbour(chainMember1, new ModelLink(ModelLinkState.OPEN));
		chainMember1.addNeighbour(testSource, new ModelLink(ModelLinkState.OPEN));

	}

	/**
	 * Tests that
	 * {@link Fortify#getPathBetween(ModelCountry, ModelCountry, ModelUnit)}
	 * functions properly.
	 */
	@Test
	public void test_getPathBetween() {
		final List<ModelCountry> path = testFortify.getPathBetween(testSource, testDestination,
				UnitHelper.getInstance().getWeakest());

		assertTrue(!path.isEmpty());
		assertTrue(path.size() == 7);
		assertTrue(path.get(0) == testSource);
		assertTrue(path.get(6) == testDestination);

	}

	/**
	 * A version of the {@link GameController} that is tailored to only provide the
	 * functionality used for {@link Test_Fortify}. All operations that are should
	 * not be required by {@link Fortify} will throw an
	 * {@link UnsupportedOperationException}.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @version 1.01.01
	 * @since 2018-03-15
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
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public ModelPlayer getModelPlayer(int playerNumber) {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public Fortify getFortify() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public Reinforce getReinforce() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public Attack getAttack() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public Setup getSetup() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public List<Challenge> getChallenges() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public View getView() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public AIController getAIController() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public Directory getDirectory() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void setHelpMenuPage(int pageId) {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void resetGame() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void setBoardName(String name) {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void addPlayer(ModelPlayer player) {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void forEachModelCountry(Consumer<ModelCountry> task) {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public boolean isPlaying(int playerNumber) {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void checkChallenges() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void nextPlayer() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void checkWinner() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void checkContinentRulership() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void setCurrentPlayer(ModelPlayer model) {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void setRoundNumber(int parseInt) {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void addChallenge(Challenge challenge) {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void forEachModelPlayer(Consumer<ModelPlayer> task) {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void forEachLoser(Consumer<ModelPlayer> task) {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public int getRoundNumber() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void confirmReinforce() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void confirmCombat() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void confirmSetup() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void confirmMovement() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void autoDistributeCountries() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void setLoser(ModelPlayer player) {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void addPoints(int points) {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public void processAI(int delta) {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public ModelState getCurrentState() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}

		@Override
		public AIHelper getAIs() {
			throw new UnsupportedOperationException("Fortify should not require this method.");
		}
	}

}
