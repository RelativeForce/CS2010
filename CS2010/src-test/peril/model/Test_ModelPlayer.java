package peril.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import peril.ai.AI;
import peril.helpers.PlayerHelper;
import peril.helpers.UnitHelper;
import peril.model.board.ModelArmy;

/**
 * 
 * Tests {@link ModelPlayer}.
 * 
 * @author Joseph_Rolli
 * 
 * @version 1.01.05
 * @since 2018-03-12
 * 
 *
 */
public class Test_ModelPlayer {
	
	/**
	 * The {@link UnitHelper} that holds the instance of
	 * {@link UnitHelper#getInstance()}.
	 */
	private PlayerHelper playerHelper;

	/**
	 * The pre-test setup operations. 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	/**
	 * This test confirms if the {@link ModelPlayer#setCountriesRuled()} method sets the
	 * correct strength of a {@link ModelPlayer}
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_setCountriesRuled() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);
		
		modelPlayer.setCountriesRuled(5);
		assertEquals(5,modelPlayer.getCountriesRuled());
		
		// Should throw an illegal argument exception
		modelPlayer.setCountriesRuled(-1);
	}
	
	/**
	 * This test confirms if the {@link ModelPlayer#getCountriesRuled()} method retrieves
	 * the correct strength of a {@link ModelPlayer}.
	 */
	@Test
	public void test_getCountriesRuled() {
		fail("Not yet implemented");
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_setContinentsRuled() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);

		modelPlayer.setContinentsRuled(5);
		assertEquals(5,modelPlayer.getContinentsRuled());
		
		// Should throw an illegal argument exception
		modelPlayer.setContinentsRuled(-1);
	}
	
	@Test
	public void test_getPoints() {
		fail("Not yet implemented");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_setPoints() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);

		// Should throw an illegal argument exception
		modelPlayer.setPoints(-1);
	}
	
	@Test
	public void test_addPoints() {
		fail("Not yet implemented");
	}
	
	@Test
	public void test_getDistributableArmy() {
		fail("Not yet implemented");
	}
	
	@Test
	public void test_getTotalArmy() {
		fail("Not yet implemented");
	}
	
	@Test
	public void test_getUnitsKilled() {
		fail("Not yet implemented");
	}
	
	@Test
	public void test_getCountriesTaken() {
		fail("Not yet implemented");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_setCountriesTaken() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);


		modelPlayer.setCountriesTaken(5);
		assertEquals(5,modelPlayer.getCountriesTaken());
		
		// Should throw an illegal argument exception
		modelPlayer.setCountriesTaken(-1);
	}
	
	@Test
	public void test_getPointsSpent() {
		fail("Not yet implemented");
	}
	
	@Test
	public void test_spendPoints() {
		fail("Not yet implemented");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_setPointsSpent() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);

		modelPlayer.setPointsSpent(5);
		assertEquals(5,modelPlayer.getPointsSpent());
		
		// Should throw an illegal argument exception
		modelPlayer.setPointsSpent(-1);
	}
	
	@Test
	public void test_addUnitsKilled() {
		fail("Not yet implemented");
	}
}
