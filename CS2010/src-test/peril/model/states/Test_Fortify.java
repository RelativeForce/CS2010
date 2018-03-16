package peril.model.states;

import static org.junit.Assert.*;

import java.util.List;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

import peril.Challenge;
import peril.Directory;
import peril.GameController;
import peril.ai.AI;
import peril.ai.AIController;
import peril.helpers.AIHelper;
import peril.helpers.UnitHelper;
import peril.model.ModelColor;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
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
 * @since 2018-03-16
 * @version 1.01.03
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
	 *             If setup fails throw Exception.
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
		chainMember4.addNeighbour(chainMember3, new ModelLink(ModelLinkState.BLOCKADE));
		chainMember3.addNeighbour(chainMember2, new ModelLink(ModelLinkState.OPEN));
		chainMember2.addNeighbour(chainMember1, new ModelLink(ModelLinkState.OPEN));
		chainMember1.addNeighbour(testSource, new ModelLink(ModelLinkState.OPEN));

	}

	/**
	 * Tests that
	 * {@link Fortify#getPathBetween(ModelCountry, ModelCountry, ModelUnit)}
	 * functions properly. The path from origin to destination is completely open.
	 */
	@Test
	public void test_getPathBetween_normal() {
		final List<ModelCountry> path = testFortify.getPathBetween(testSource, testDestination,
				UnitHelper.getInstance().getWeakest());

		assertTrue(!path.isEmpty());
		assertTrue(path.size() == 7);
		assertTrue(path.get(0) == testSource);
		assertTrue(path.get(6) == testDestination);

	}

	/**
	 * Tests that
	 * {@link Fortify#getPathBetween(ModelCountry, ModelCountry, ModelUnit)}
	 * functions properly. The path from origin to destination has one blockaded
	 * link.
	 */
	@Test
	public void test_getPathBetween_reverse() {

		// The path should be empty as there is a blockade.
		final List<ModelCountry> path = testFortify.getPathBetween(testDestination, testSource,
				UnitHelper.getInstance().getWeakest());

		assertTrue(path.isEmpty());

	}

	/**
	 * Tests the {@link Fortify#select(ModelCountry)} will not select the source
	 * {@link ModelCountry} if there is only one {@link ModelUnit} in the
	 * {@link ModelArmy}.
	 */
	@Test
	public void test_select_notEnoughUnits() {

		// Clear the source army and add one unit
		testSource.getArmy().clearUnits();
		testSource.getArmy().add(UnitHelper.getInstance().getWeakest());

		// Attempt to select the source.
		testFortify.deselectAll();
		assertTrue(!testFortify.select(testSource));
		assertTrue(testFortify.numberOfSelected() == 0);

	}

	/**
	 * Tests the {@link Fortify#select(ModelCountry)} will select the source
	 * {@link ModelCountry} if there is more than one {@link ModelUnit} in the
	 * {@link ModelArmy}.
	 */
	@Test
	public void test_select_enoughUnits() {

		// Clear the source army and add two units
		testSource.getArmy().clearUnits();
		testSource.getArmy().add(UnitHelper.getInstance().getWeakest());
		testSource.getArmy().add(UnitHelper.getInstance().getWeakest());

		// Attempt to select the source.
		testFortify.deselectAll();
		assertTrue(testFortify.select(testSource));
		assertTrue(testFortify.numberOfSelected() == 1);

	}

	/**
	 * Tests the {@link Fortify#select(ModelCountry)} will select the source and
	 * destination {@link ModelCountry} if there an open path between them.
	 */
	@Test
	public void test_select_openPath() {

		// Clear the source army and add two units
		testSource.getArmy().clearUnits();
		testSource.getArmy().add(UnitHelper.getInstance().getWeakest());
		testSource.getArmy().add(UnitHelper.getInstance().getWeakest());

		// Clear the destination army and add two units
		testDestination.getArmy().clearUnits();
		testDestination.getArmy().add(UnitHelper.getInstance().getWeakest());
		testDestination.getArmy().add(UnitHelper.getInstance().getWeakest());

		// Attempt to select the source and destination.
		testFortify.deselectAll();
		assertTrue(testFortify.select(testSource));
		assertTrue(testFortify.select(testDestination));
		assertTrue(testFortify.numberOfSelected() == 2);

	}

	/**
	 * Tests the {@link Fortify#select(ModelCountry)} will not select the source and
	 * destination {@link ModelCountry} if there an blocked path between them.
	 */
	@Test
	public void test_select_blockedPath() {

		// Clear the source army and add two units
		testSource.getArmy().clearUnits();
		testSource.getArmy().add(UnitHelper.getInstance().getWeakest());
		testSource.getArmy().add(UnitHelper.getInstance().getWeakest());

		// Clear the destination army and add two units
		testDestination.getArmy().clearUnits();
		testDestination.getArmy().add(UnitHelper.getInstance().getWeakest());
		testDestination.getArmy().add(UnitHelper.getInstance().getWeakest());

		testFortify.deselectAll();

		// Assert the destination was selected as the primary.
		assertTrue(testFortify.select(testDestination));
		assertTrue(testFortify.numberOfSelected() == 1);

		// Assert that the source was not connected to the destination by a valid link
		// so it is not selected.
		assertTrue(!testFortify.select(testSource));
		assertTrue(testFortify.numberOfSelected() == 0);

	}

	/**
	 * Tests that {@link Fortify#select(ModelCountry)} will not select null.
	 */
	@Test
	public void test_select_null() {

		testFortify.deselectAll();

		// Assert that null cannot be selected.
		assertTrue(!testFortify.select(null));
		assertTrue(testFortify.numberOfSelected() == 0);
	}

	/**
	 * Tests that {@link Fortify#select(ModelCountry)} will not select null but will
	 * also de-select any {@link ModelCountry}s that are selected.
	 */
	@Test
	public void test_select_nullDeselect() {

		// Clear the source army and add two units
		testSource.getArmy().clearUnits();
		testSource.getArmy().add(UnitHelper.getInstance().getWeakest());
		testSource.getArmy().add(UnitHelper.getInstance().getWeakest());

		testFortify.deselectAll();

		// Assert the destination was selected as the primary.
		assertTrue(testFortify.select(testSource));
		assertTrue(testFortify.numberOfSelected() == 1);

		// Assert that after the null select there are no selected countries.
		assertTrue(!testFortify.select(null));
		assertTrue(testFortify.numberOfSelected() == 0);

	}

	/**
	 * Tests {@link Fortify#fortify()} moves one unit if two valid
	 * {@link ModelCountry}s are selected.
	 */
	@Test
	public void test_fortify_moveUnit() {

		// Clear the source army and add two units
		testSource.getArmy().clearUnits();
		testSource.getArmy().add(UnitHelper.getInstance().getWeakest());
		testSource.getArmy().add(UnitHelper.getInstance().getWeakest());

		// Assert the army size
		assertTrue(testSource.getArmy().getNumberOfUnits() == 2);

		// Clear the destination army and add two units
		testDestination.getArmy().clearUnits();
		testDestination.getArmy().add(UnitHelper.getInstance().getWeakest());
		testDestination.getArmy().add(UnitHelper.getInstance().getWeakest());

		// Assert the army size
		assertTrue(testDestination.getArmy().getNumberOfUnits() == 2);

		testFortify.deselectAll();

		// Attempt to select the source and destination.
		assertTrue(testFortify.select(testSource));
		assertTrue(testFortify.select(testDestination));
		assertTrue(testFortify.numberOfSelected() == 2);

		testFortify.fortify();

		// Assert a unit has moved.
		assertTrue(testSource.getArmy().getNumberOfUnits() == 1);
		assertTrue(testDestination.getArmy().getNumberOfUnits() == 3);

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
