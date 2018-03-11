package peril.model.states.combat;

import static org.junit.Assert.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import peril.Challenge;
import peril.controllers.AIController;
import peril.controllers.Directory;
import peril.controllers.GameController;
import peril.helpers.AIHelper;
import peril.helpers.UnitHelper;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.states.*;
import peril.views.View;

/**
 * 
 * Tests that {@link CombatHelper} functions properly.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-03-11
 * 
 * @see CombatHelper
 * @see CombatRound
 * @see CombatView
 * @see GameController
 *
 */
public final class Test_CombatHelper {

	/**
	 * The instance of {@link CombatHelper} that will be tested.
	 */
	private CombatHelper combat;

	/**
	 * The {@link List} of {@link ModelUnit}s that will make up a standard
	 * {@link ModelArmy}.
	 */
	private List<ModelUnit> testArmyUnits;

	/**
	 * Sets up the {@link CombatHelper} that will be used by every test.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {

		final ModelUnit testUnit1 = new ModelUnit("testUnit1", 1, "na");

		this.combat = new CombatHelper(new TestGC());

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
	
	@Test
	public void test() {
		
		combat.clear();
		
		assertTrue(true);
		
		
	}

	/**
	 * A version of the {@link GameController} that is tailored to only provide the
	 * functionality used for {@link Test_CombatHelper}. All operations that are
	 * should not be required by {@link CombatHelper} will throw an
	 * {@link UnsupportedOperationException}.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @version 1.01.01
	 * @since 2018-03-11
	 *
	 */
	private final class TestGC implements GameController {

		@Override
		public ModelPlayer getCurrentModelPlayer() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public ModelBoard getModelBoard() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public ModelPlayer getModelPlayer(int playerNumber) {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public Attack getAttack() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public Reinforce getReinforce() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public Fortify getFortify() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public Setup getSetup() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public List<Challenge> getChallenges() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public View getView() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public AIController getAIController() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public Directory getDirectory() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void setHelpMenuPage(int pageId) {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void resetGame() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void setBoardName(String name) {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void addPlayer(ModelPlayer player) {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void forEachModelCountry(Consumer<ModelCountry> task) {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public boolean isPlaying(int playerNumber) {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void checkChallenges() {
			// Do nothing as the result of this methods execution is not required for
			// testing combat helper.
		}

		@Override
		public void nextPlayer() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void checkWinner() {
			// Do nothing as the result of this methods execution is not required for
			// testing combat helper.
		}

		@Override
		public void checkContinentRulership() {
			// Do nothing as the result of this methods execution is not required for
			// testing combat helper.
		}

		@Override
		public void setCurrentPlayer(ModelPlayer model) {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void setRoundNumber(int parseInt) {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void addChallenge(Challenge challenge) {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void forEachModelPlayer(Consumer<ModelPlayer> task) {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void forEachLoser(Consumer<ModelPlayer> task) {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public int getRoundNumber() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void confirmReinforce() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void confirmCombat() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void confirmSetup() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void confirmMovement() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void autoDistributeCountries() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void setLoser(ModelPlayer player) {
			// Do nothing as the result of this methods execution is not required for
			// testing combat helper.
		}

		@Override
		public void addPoints(int points) {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public void processAI(int delta) {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");

		}

		@Override
		public ModelState getCurrentState() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}

		@Override
		public AIHelper getAIs() {
			throw new UnsupportedOperationException("CombatHelper should not require this method.");
		}
	}

}
