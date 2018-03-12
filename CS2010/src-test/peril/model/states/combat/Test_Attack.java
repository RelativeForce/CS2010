package peril.model.states.combat;

import static org.junit.Assert.*;

import java.util.LinkedList;
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
import peril.model.board.ModelArmy;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.board.links.ModelLink;
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
	 * The {@link List} of {@link ModelUnit}s that will make up a standard
	 * {@link ModelArmy}.
	 */
	private List<ModelUnit> testArmyUnits;

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

		// Set the players as owning one country
		this.testCurrentPlayer.setCountriesRuled(1);
		this.testEnemyPlayer.setCountriesRuled(1);

		final ModelUnit testUnit1 = new ModelUnit("testUnit1", 1, "na");

		UnitHelper.getInstance().clear();
		UnitHelper.getInstance().addUnit(testUnit1);

		// The basic units for a standard army.
		this.testArmyUnits = new LinkedList<>();
		this.testArmyUnits.add(testUnit1);
		this.testArmyUnits.add(testUnit1);
		this.testArmyUnits.add(testUnit1);
		this.testArmyUnits.add(testUnit1);
		this.testArmyUnits.add(testUnit1);

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

		// Reset test fields to initial state
		testCurrentPlayer.totalArmy.clearUnits();
		testEnemyPlayer.totalArmy.clearUnits();
		testAttack.deselectAll();

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Set the countries as linked.
		country1.addNeighbour(country2, new ModelLink(ModelLinkState.OPEN));
		country2.addNeighbour(country1, new ModelLink(ModelLinkState.OPEN));

		// Assign the rulers.
		country1.setRuler(testCurrentPlayer);
		country2.setRuler(testEnemyPlayer);

		// Build the armies
		country1.getArmy().add(testArmyUnits);
		country2.getArmy().add(testArmyUnits);

		// Add the units to the players total armies.
		testCurrentPlayer.totalArmy.add(testArmyUnits);
		testEnemyPlayer.totalArmy.add(testArmyUnits);

		// Assert that the friendly country with a army with more than one unit.
		assertTrue(testAttack.select(country1));

		// Assert the the enemy country that is a neighbour connected by a valid link
		assertTrue(testAttack.select(country2));

		// Assert that there is two countries selected and they are in the correct
		// order.
		assertTrue(testAttack.numberOfSelected() == 2);
		assertTrue(testAttack.getPrimary().equals(country1));
		assertTrue(testAttack.getSecondary().equals(country2));

	}

	/**
	 * This method asserts that if two {@link ModelCountry}s ruled by the same
	 * {@link ModelPlayer} can be selected but when the second is selected it will
	 * replace the primary.
	 */
	@Test
	public void test_select_swapPrimary() {

		// Reset test fields to initial state
		testCurrentPlayer.totalArmy.clearUnits();
		testEnemyPlayer.totalArmy.clearUnits();
		testAttack.deselectAll();

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Set the countries as linked.
		country1.addNeighbour(country2, new ModelLink(ModelLinkState.OPEN));
		country2.addNeighbour(country1, new ModelLink(ModelLinkState.OPEN));

		// Assign both the countries with the same ruler.
		country1.setRuler(testCurrentPlayer);
		country2.setRuler(testCurrentPlayer);

		// Build the armies
		country1.getArmy().add(testArmyUnits);
		country2.getArmy().add(testArmyUnits);

		// Add the units to the players total armies.
		testCurrentPlayer.totalArmy.add(testArmyUnits);
		testEnemyPlayer.totalArmy.add(testArmyUnits);

		// Assert that the friendly country with a army with more than one unit.
		assertTrue(testAttack.select(country1));

		// Assert the the second friendly country can be selected but it will be the new
		// primary.
		assertTrue(testAttack.select(country2));

		// Assert that there is one country selected and it is the second.
		assertTrue(testAttack.numberOfSelected() == 1);
		assertTrue(testAttack.getPrimary().equals(country2));
		assertTrue(testAttack.getSecondary() == null);

	}

	/**
	 * This method asserts that a valid enemy {@link ModelCountry} cannot be
	 * selected before the valid friendly {@link ModelCountry}.
	 */
	@Test
	public void test_select_enemy() {

		// Reset test fields to initial state
		testCurrentPlayer.totalArmy.clearUnits();
		testEnemyPlayer.totalArmy.clearUnits();
		testAttack.deselectAll();

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));

		// Assign both the countries with the same ruler.
		country1.setRuler(testEnemyPlayer);

		// Build the armies
		country1.getArmy().add(testArmyUnits);

		// Add the units to the players total armies.
		testEnemyPlayer.totalArmy.add(testArmyUnits);

		// Assert that the friendly country with a army with more than one unit.
		assertTrue(!testAttack.select(country1));

		// Assert that there is one country selected and it is the second.
		assertTrue(testAttack.numberOfSelected() == 0);
	}

	/**
	 * This method asserts that two otherwise valid {@link ModelCountry}s cannot be
	 * selected if they are not neighbours of each other.
	 */
	@Test
	public void test_select_notNeighbours() {

		// Reset test fields to initial state
		testCurrentPlayer.totalArmy.clearUnits();
		testEnemyPlayer.totalArmy.clearUnits();
		testAttack.deselectAll();

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Assign the rulers.
		country1.setRuler(testCurrentPlayer);
		country2.setRuler(testEnemyPlayer);

		// Build the armies
		country1.getArmy().add(testArmyUnits);
		country2.getArmy().add(testArmyUnits);

		// Add the units to the players total armies.
		testCurrentPlayer.totalArmy.add(testArmyUnits);
		testEnemyPlayer.totalArmy.add(testArmyUnits);

		// Assert that the friendly country with a army with more than one unit.
		assertTrue(testAttack.select(country1));

		// Assert the the enemy country that is a neighbour connected by a valid link
		assertTrue(!testAttack.select(country2));

		// Assert that the primary was de-selected.
		assertTrue(testAttack.numberOfSelected() == 0);

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
