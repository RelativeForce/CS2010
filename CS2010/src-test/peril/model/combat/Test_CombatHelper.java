package peril.model.combat;

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
import peril.model.board.ModelArmy;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.board.links.ModelLink;
import peril.model.board.links.ModelLinkState;
import peril.model.combat.CombatHelper;
import peril.model.combat.CombatRound;
import peril.model.combat.CombatView;
import peril.model.combat.ModelSquad;
import peril.model.states.*;
import peril.views.View;

/**
 * 
 * Tests that {@link CombatHelper} functions properly.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.04
 * @since 2018-03-14
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
	 * 			If setup fails throw Exception.
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
	 * Test that the dice that are in {@link CombatView#attackerDiceRolls} and
	 * {@link CombatView#defenderDiceRolls} are ordered from highest (index: 0) to
	 * lowest.
	 */
	@Test
	public void test_fight_diceOrder() {

		combat.clear();

		// Constructs the combat round with no exceptions.
		combat.fight(getStandardRound());

		// Holds the post view combat view.
		final CombatView view = combat.view;

		// Check the dice rolls.
		checkDiceRolls(view.attackerDiceRolls);
		checkDiceRolls(view.defenderDiceRolls);

	}

	/**
	 * This test confirms that when if a dice if the attackers dice was higher than
	 * the defenders dice that the unit is removes from the appropriate squad. This
	 * assumes that there is only a single atomic {@link ModelUnit} in the
	 * {@link UnitHelper}.
	 */
	@Test
	public void test_fight_unitsKilled() {

		// The number of times this test will be repeated to minimise the effects of the
		// random nature of combat.
		final int numberOfTests = 1000;

		// Repeat the test the specifed number of times.
		for (int testIndex = 0; testIndex < numberOfTests; testIndex++) {

			combat.clear();

			// Create a basic combat round.
			final CombatRound round = getStandardRound();

			// Retrieve the number of units in the squads before the combat.
			final int preComabt_AliveAttackUnits = round.attackerSquad.getAliveUnits();
			final int preComabt_AliveDefendUnits = round.defenderSquad.getAliveUnits();

			combat.fight(round);

			final CombatView view = combat.view;

			// Retrieve the number of alive units in the squads post combat.
			final int postComabt_AliveAttackUnits = view.round.attackerSquad.getAliveUnits();
			final int postComabt_AliveDefendUnits = view.round.defenderSquad.getAliveUnits();

			// Receive the details about the dice.
			final int attackDice = view.attackerDiceRolls.length;
			final int defendDice = view.defenderDiceRolls.length;
			final int diceToCheck = attackDice > defendDice ? defendDice : attackDice;

			// Holds the number of units for each quad that were killed during the comabt.
			int attackKilled = 0;
			int defendKilled = 0;

			// Iterate over all the dice and compare them. If the attacker dice won then one
			// defending unit was killed and vice versa.
			for (int diceIndex = 0; diceIndex < diceToCheck; diceIndex++) {

				if (view.attackerDiceRolls[diceIndex] > view.defenderDiceRolls[diceIndex]) {
					defendKilled++;
				} else {
					attackKilled++;
				}

			}

			// Confirm that the number of units that were killed plus the number of units
			// that are still alive is equal to the original number of units for each
			// squad.
			assertTrue(preComabt_AliveAttackUnits == postComabt_AliveAttackUnits + attackKilled);
			assertTrue(preComabt_AliveDefendUnits == postComabt_AliveDefendUnits + defendKilled);

		}

	}

	/**
	 * Checks that the numbers that are in the parameter dice rolls are in
	 * descending order.
	 * 
	 * @param diceRolls
	 *            The dice rolls.
	 */
	private void checkDiceRolls(Integer[] diceRolls) {

		// The current lowest dice that has been seen
		int currentLowest = 0;

		// Iterate over the dice rolls.
		for (int attackIndex = 0; attackIndex < diceRolls.length; attackIndex++) {

			// If this is the first dice then set it as the current lowest.
			if (attackIndex == 0) {
				currentLowest = diceRolls[attackIndex];
			} else {

				assertTrue(currentLowest >= diceRolls[attackIndex]);
				currentLowest = diceRolls[attackIndex];
			}

		}

	}

	/**
	 * Constructs a standard {@link CombatRound} for testing purposes.
	 * 
	 * @return A standard {@link CombatRound}
	 */
	private CombatRound getStandardRound() {

		// Define the players that will rule both countries.
		final ModelPlayer player1 = new ModelPlayer(1, AI.USER);
		final ModelPlayer player2 = new ModelPlayer(2, AI.USER);

		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Set the countries as linked.
		country1.addNeighbour(country2, new ModelLink(ModelLinkState.OPEN));
		country2.addNeighbour(country1, new ModelLink(ModelLinkState.OPEN));

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

		// Constructs the combat round with no exceptions.
		return new CombatRound(country1, country2, attackSquad, defendSquad);

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
