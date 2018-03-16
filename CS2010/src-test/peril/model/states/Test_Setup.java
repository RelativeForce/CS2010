package peril.model.states;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

import peril.Challenge;
import peril.Directory;
import peril.Game;
import peril.GameController;
import peril.ai.AI;
import peril.ai.AIController;
import peril.ai.api.Board;
import peril.ai.api.Continent;
import peril.ai.api.Country;
import peril.helpers.AIHelper;
import peril.helpers.PlayerHelper;
import peril.helpers.UnitHelper;
import peril.model.ModelColor;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelContinent;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.board.links.ModelLinkState;
import peril.model.states.Setup;
import peril.views.View;

/**
 * 
 * Tests {@link Setup}.
 * 
 * @author Joseph_Rolli
 * 
 * @version 1.01.07
 * @since 2018-03-16
 * 
 *
 */
public class Test_Setup {

	/**
	 * The {@link Setup} that is the subject of these tests.
	 */
	private Setup testSetup;

	/**
	 * A{@link ModelPlayer} that serves as the player 1
	 */
	private ModelPlayer player1;

	/**
	 * Another {@link ModelPlayer} that serves as player 2
	 */
	private ModelPlayer player2;

	/**
	 * This method sets up the {@link Setup} state, with 2 players, a {@link Game} instance, and 
	 * a unit for the {@link UnitHelper}
	 */
	@Before
	public void setUp() throws Exception {
		this.player1 = new ModelPlayer(1, AI.USER);
		this.player2 = new ModelPlayer(2, AI.USER);
		this.testSetup = new Setup(new TestGC());
		UnitHelper.getInstance().clear();
		UnitHelper.getInstance().addUnit(new ModelUnit("Unit", 1, null));
		testSetup.game.addPlayer(player1);
		testSetup.game.addPlayer(player2);

	}

	/**
	 * This method asserts that on valid {@link ModelCountry} can be selected by
	 * the {@link Setup}. When a second country is selected the first should be cleared,
	 * and the second one indexed instead.
	 */
	@Test
	public void test_select() {
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		// Select country 1 and test how many are selected, and whether index 0 is the right country.
		testSetup.select(country1);
		assertTrue(testSetup.numberOfSelected() == 1);
		assertTrue(testSetup.getSelected(0) == country1);
		assertTrue(testSetup.getSelected(1) == null);

		// Select country 2 and test again.
		testSetup.select(country2);
		assertTrue(testSetup.numberOfSelected() == 1);
		assertTrue(testSetup.getSelected(0) == country2);
		assertTrue(testSetup.getSelected(1) == null);

	}

	/**
	 * This method asserts that the {@link Setup} distributes countries between players correctly,
	 * and fairly. Where two countries are distributed among two players each player should always recieve one.
	 */
	@Test
	public void test_autoDistributeCountries() {
		
		//Setup playerhelper with players
		PlayerHelper players = new PlayerHelper(null);
		players.addPlayer(player1);
		players.addPlayer(player2);
		
		//Setup countries
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));

		//Setup continent and add countries to it.
		ModelContinent continent = new ModelContinent(null, null);
		continent.addCountry(country1);
		continent.addCountry(country2);
		
		Set<ModelContinent> newContinents = new HashSet<ModelContinent>();
		newContinents.add(continent);
		
		//Setup board, add continents to it.
		ModelBoard board = new ModelBoard("Board");
		board.setContinents(newContinents);
		
		//Distribute countries between players.
		testSetup.autoDistributeCountries(board, players);
		
		//Count the number of countries owned by each player, if equal, pass
		int player1Owned = 0;
		int player2Owned = 0;
		for (Country country : continent.getCountries()) {
			if (country.getOwner() == player1) {
				player1Owned++;
			}
			if (country.getOwner() == player2) {
				player2Owned++;
			}

		}
		System.out.println("P1"+player1Owned+"P2"+player2Owned);
		assertTrue(player1Owned == 1 && player2Owned == 1);
	}

	/**
	 * A version of the {@link GameController} that is tailored to only provide the
	 * functionality used for {@link Test_Setup}. All operations that are should not
	 * be required by {@link Setup} will throw an
	 * {@link UnsupportedOperationException}.
	 * 
	 * @author Joseph_Rolli
	 * 
	 * @version 1.01.02
	 * @since 2018-03-13
	 *
	 */
	private final class TestGC implements GameController {

		@Override
		public ModelPlayer getCurrentModelPlayer() {
			throw new UnsupportedOperationException("Setup should not require this method.");
		}

		@Override
		public ModelBoard getModelBoard() {
			throw new UnsupportedOperationException("Setup should not require this method.");
		}

		@Override
		public ModelPlayer getModelPlayer(int playerNumber) {
			throw new UnsupportedOperationException("Setup should not require this method.");
		}

		@Override
		public Attack getAttack() {
			throw new UnsupportedOperationException("Setup should not require this method.");
		}

		@Override
		public Reinforce getReinforce() {
			throw new UnsupportedOperationException("Setup should not require this method.");
		}

		@Override
		public Fortify getFortify() {
			throw new UnsupportedOperationException("Setup should not require this method.");
		}

		@Override
		public Setup getSetup() {
			throw new UnsupportedOperationException("Setup should not require this method.");
		}

		@Override
		public List<Challenge> getChallenges() {
			throw new UnsupportedOperationException("Setup should not require this method.");
		}

		@Override
		public View getView() {
			throw new UnsupportedOperationException("Setup should not require this method.");
		}

		@Override
		public AIController getAIController() {
			throw new UnsupportedOperationException("Setup should not require this method.");
		}

		@Override
		public Directory getDirectory() {
			throw new UnsupportedOperationException("Setup should not require this method.");
		}

		@Override
		public void setHelpMenuPage(int pageId) {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void resetGame() {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void setBoardName(String name) {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void addPlayer(ModelPlayer player) {

		}

		@Override
		public void forEachModelCountry(Consumer<ModelCountry> task) {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public boolean isPlaying(int playerNumber) {
			throw new UnsupportedOperationException("Setup should not require this method.");
		}

		@Override
		public void checkChallenges() {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void nextPlayer() {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void checkWinner() {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void checkContinentRulership() {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void setCurrentPlayer(ModelPlayer model) {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void setRoundNumber(int parseInt) {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void addChallenge(Challenge challenge) {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void forEachModelPlayer(Consumer<ModelPlayer> task) {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void forEachLoser(Consumer<ModelPlayer> task) {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public int getRoundNumber() {
			throw new UnsupportedOperationException("Setup should not require this method.");
		}

		@Override
		public void confirmReinforce() {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void confirmCombat() {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void confirmSetup() {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void confirmMovement() {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void autoDistributeCountries() {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void setLoser(ModelPlayer player) {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void addPoints(int points) {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public void processAI(int delta) {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public ModelState getCurrentState() {
			throw new UnsupportedOperationException("Setup should not require this method.");

		}

		@Override
		public AIHelper getAIs() {
			throw new UnsupportedOperationException("Attack should not require this method.");

		}

	}
}
