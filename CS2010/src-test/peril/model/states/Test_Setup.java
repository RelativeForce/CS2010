package peril.model.states;

import static org.junit.Assert.*;

import java.util.List;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

import peril.Challenge;
import peril.controllers.AIController;
import peril.controllers.Directory;
import peril.controllers.GameController;
import peril.helpers.AIHelper;
import peril.model.ModelColor;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.model.states.Setup;
import peril.views.View;

/**
 * 
 * Tests {@link Setup}.
 * 
 * @author Joseph_Rolli
 * 
 * @version 1.01.02
 * @since 2018-03-15
 * 
 *
 */
public class Test_Setup {

	/**
	 * The {@link Setup} that is the subject of these tests.
	 */
	private Setup testSetup;

	@Before
	public void setUp() throws Exception {
		this.testSetup = new Setup(new TestGC());

	}

	@Test
	public void test_select() {
		// The countries taking part in the war.
		final ModelCountry country1 = new ModelCountry("country1", new ModelColor(0, 0, 0));
		final ModelCountry country2 = new ModelCountry("country2", new ModelColor(0, 0, 1));
		
		testSetup.select(country1);
		
		
		fail("Not yet implemented");
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
			throw new UnsupportedOperationException("Setup should not require this method.");

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
