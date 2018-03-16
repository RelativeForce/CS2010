package peril.model.states;

import static org.junit.Assert.*;

import java.util.LinkedList;
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
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.views.View;

/**
 * This tests the {@link Reinforce} state functions properly.
 * 
 * @author Ezekiel_Trinidad
 *
 * @since 16-03-2018
 * @version 1.01.01 
 *
 */
public class Test_Reinforce {

	/**
	 * {@link UnitHelper} for the purpose of assisting on the distribution of units.
	 */
	private UnitHelper unitHelper;

	/**
	 * TestGameController for test purposes only
	 */
	private TestGC testGC;

	// private PlayerHelper playerHelper = new PlayerHelper();
	private Reinforce reinforce;

	private ModelPlayer testPlayer;

	private List<ModelUnit> testArmyUnits;

	// *Make assumptions that all other classes are working perfectly
	// should be able to select 2 valid countries
	// Country must be owned by current player as specified by GameController

	@Before
	public void setUp() {
		testGC = new TestGC();
		reinforce = new Reinforce(testGC);
		testArmyUnits = new LinkedList<>();
		unitHelper = UnitHelper.getInstance();
	}

	/**
	 * This method asserts that only a {@link ModelCountry} that is owned by the
	 * current {@link ModelPlayer} can be reinforced by them. Furthermore it checks
	 * if the country is reinforced by the correct amount from the
	 * {@link ModelPlayer}'s distributable Army and also checks if that has been
	 * decreased by the correct amount.
	 */
	@Test
	public void test_normal() {

		final ModelCountry testCountry = new ModelCountry("country", new ModelColor(0, 0, 0));
		final ModelUnit testUnit = new ModelUnit("testUnit", 1, "N/A");

		testPlayer = new ModelPlayer(1, AI.USER);

		unitHelper.clear();
		unitHelper.addUnit(testUnit);

		testArmyUnits.add(testUnit);
		testArmyUnits.add(testUnit);
		testArmyUnits.add(testUnit);
		testArmyUnits.add(testUnit);
		testArmyUnits.add(testUnit);

		reinforce.game.addPlayer(testPlayer);

		testCountry.getArmy().add(testArmyUnits);
		testCountry.setRuler(testPlayer);
		testPlayer.distributableArmy.add(testArmyUnits);

		reinforce.select(testCountry);
		reinforce.reinforce();

		// makes sure that the country selected is owned by the current player;
		assertEquals(testPlayer, reinforce.game.getCurrentModelPlayer());

		// checks that the selected country's army is reinforced by the correct amount
		assertEquals(6, testCountry.getArmy().getStrength());

		// checks that the distributable army is 1 less
		assertEquals(4, testPlayer.getDistributableArmyStrength());

	}

	private final class TestGC implements GameController {

		public TestGC() {

		}

		@Override
		public ModelPlayer getCurrentModelPlayer() {
			return testPlayer;
		}

		@Override
		public ModelBoard getModelBoard() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ModelPlayer getModelPlayer(int playerNumber) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Attack getAttack() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Reinforce getReinforce() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Fortify getFortify() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Setup getSetup() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Challenge> getChallenges() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public View getView() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AIController getAIController() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Directory getDirectory() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setHelpMenuPage(int pageId) {
			// TODO Auto-generated method stub

		}

		@Override
		public void resetGame() {
			// TODO Auto-generated method stub

		}

		@Override
		public void setBoardName(String name) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addPlayer(ModelPlayer player) {
			// TODO Auto-generated method stub

		}

		@Override
		public void forEachModelCountry(Consumer<ModelCountry> task) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isPlaying(int playerNumber) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void checkChallenges() {
			// TODO Auto-generated method stub

		}

		@Override
		public void nextPlayer() {
			// TODO Auto-generated method stub

		}

		@Override
		public void checkWinner() {
			// TODO Auto-generated method stub

		}

		@Override
		public void checkContinentRulership() {
			// TODO Auto-generated method stub

		}

		@Override
		public void setCurrentPlayer(ModelPlayer model) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setRoundNumber(int parseInt) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addChallenge(Challenge challenge) {
			// TODO Auto-generated method stub

		}

		@Override
		public void forEachModelPlayer(Consumer<ModelPlayer> task) {
			// TODO Auto-generated method stub

		}

		@Override
		public void forEachLoser(Consumer<ModelPlayer> task) {
			// TODO Auto-generated method stub

		}

		@Override
		public int getRoundNumber() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void confirmReinforce() {
			// TODO Auto-generated method stub

		}

		@Override
		public void confirmCombat() {
			// TODO Auto-generated method stub

		}

		@Override
		public void confirmSetup() {
			// TODO Auto-generated method stub

		}

		@Override
		public void confirmMovement() {
			// TODO Auto-generated method stub

		}

		@Override
		public void autoDistributeCountries() {
			// TODO Auto-generated method stub

		}

		@Override
		public void setLoser(ModelPlayer player) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addPoints(int points) {
			// TODO Auto-generated method stub

		}

		@Override
		public void processAI(int delta) {
			// TODO Auto-generated method stub

		}

		@Override
		public ModelState getCurrentState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AIHelper getAIs() {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
