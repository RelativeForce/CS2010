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

	/**
	 * Tests that the {@link CombatHelper#fight(CombatRound)} performs properly when
	 * given a valid {@link CombatRound}.
	 */
	@Test
	public void test_fight_round_normal() {

		combat.clear();

		// Define the players that will rule both countries.
		final ModelPlayer player1 = new ModelPlayer(1, AI.USER);
		final ModelPlayer player2 = new ModelPlayer(2, AI.USER);

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Assign the rulers.
		country1.setRuler(player1);
		country2.setRuler(player2);

		// Build the armies
		country1.getArmy().add(testArmyUnits);
		country2.getArmy().add(testArmyUnits);

		// Define the squads
		final ModelSquad attackSquad = new ModelSquad(CombatHelper.MAX_ATTACK_SQUAD_SIZE);
		final ModelSquad defendSquad = new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE);

		// Auto populate the squads
		attackSquad.autoPopulate(country1.getArmy(), 1);
		defendSquad.autoPopulate(country2.getArmy(), 0);

		// Define the round that will be tested.
		final CombatRound testRound = new CombatRound(country1, country2, attackSquad, defendSquad);

		// Perform the fight
		combat.fight(testRound);

		// Check there is a combat view to confirm that the war happened with no errors.
		assertTrue(combat.view != null);

	}

	/**
	 * Tests that {@link CombatHelper#fight(CombatRound)} throws an
	 * {@link IllegalArgumentException} if empty {@link ModelSquad}s are used for
	 * the combat.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_fight_round_emptySquads() {

		combat.clear();

		// Define the players that will rule both countries.
		final ModelPlayer player1 = new ModelPlayer(1, AI.USER);
		final ModelPlayer player2 = new ModelPlayer(2, AI.USER);

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Assign the rulers.
		country1.setRuler(player1);
		country2.setRuler(player2);

		// Build the armies
		country1.getArmy().add(testArmyUnits);
		country2.getArmy().add(testArmyUnits);

		// Define the squads
		final ModelSquad attackSquad = new ModelSquad(CombatHelper.MAX_ATTACK_SQUAD_SIZE);
		final ModelSquad defendSquad = new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE);

		// Define the round that will be tested.
		final CombatRound testRound = new CombatRound(country1, country2, attackSquad, defendSquad);

		// This should throw an illegal argument exception as the countries are ruled by
		// the same player.
		combat.fight(testRound);

	}

	/**
	 * Tests that {@link CombatHelper#fight(CombatRound)} throws an
	 * {@link IllegalArgumentException} when the proposed {@link CombatRound} has
	 * the attacking squad and defending squad are the same squad.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_fight_round_sameSquad() {

		combat.clear();

		// Define the players that will rule both countries.
		final ModelPlayer player1 = new ModelPlayer(1, AI.USER);
		final ModelPlayer player2 = new ModelPlayer(2, AI.USER);

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Assign the rulers.
		country1.setRuler(player1);
		country2.setRuler(player2);

		// Build the armies
		country1.getArmy().add(testArmyUnits);
		country2.getArmy().add(testArmyUnits);

		// Define the squads
		final ModelSquad defendSquad = new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE);

		// Auto populate the squads
		defendSquad.autoPopulate(country2.getArmy(), 1);

		// Define the round that will be tested.
		final CombatRound testRound = new CombatRound(country1, country2, defendSquad, defendSquad);

		// This should throw an illegal argument exception as the countries are ruled by
		// the same player.
		combat.fight(testRound);

	}

	/**
	 * Tests that {@link CombatHelper#fight(CombatRound)} throws an
	 * {@link IllegalArgumentException} if the countries fighting are ruled by the
	 * same player.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_fight_round_friendlyCountrys() {

		combat.clear();

		// The player that rules both countries.
		final ModelPlayer player1 = new ModelPlayer(1, AI.USER);

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Assign the rulers
		country1.setRuler(player1);
		country2.setRuler(player1);

		// Build the armies
		country1.getArmy().add(testArmyUnits);
		country2.getArmy().add(testArmyUnits);

		// Define the squads
		final ModelSquad attackSquad = new ModelSquad(CombatHelper.MAX_ATTACK_SQUAD_SIZE);
		final ModelSquad defendSquad = new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE);

		// Auto populate the squads
		attackSquad.autoPopulate(country1.getArmy(), 1);
		defendSquad.autoPopulate(country2.getArmy(), 0);

		// Define the round that will be tested.
		final CombatRound testRound = new CombatRound(country1, country2, attackSquad, defendSquad);

		// This should throw an illegal argument exception as the squads are empty
		combat.fight(testRound);

	}

	/**
	 * Tests that {@link CombatHelper#fight(CombatRound)} throws an
	 * {@link IllegalArgumentException} if the countries fighting are the same
	 * country.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_fight_round_sameCountry() {

		combat.clear();

		// The player that rules both countries.
		final ModelPlayer player1 = new ModelPlayer(1, AI.USER);

		// Define the single country.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));

		// Assign the rulers
		country1.setRuler(player1);

		// Build the armies
		country1.getArmy().add(testArmyUnits);

		// Define the squads
		final ModelSquad attackSquad = new ModelSquad(CombatHelper.MAX_ATTACK_SQUAD_SIZE);
		final ModelSquad defendSquad = new ModelSquad(CombatHelper.MAX_DEFEND_SQUAD_SIZE);

		// Auto populate the squads
		attackSquad.autoPopulate(country1.getArmy(), 1);
		defendSquad.autoPopulate(country1.getArmy(), 1);

		// Define the round that will be tested.
		final CombatRound testRound = new CombatRound(country1, country1, attackSquad, defendSquad);

		// This should throw an illegal argument exception as the countries are the
		// same.
		combat.fight(testRound);

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
