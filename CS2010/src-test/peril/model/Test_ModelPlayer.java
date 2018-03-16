package peril.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import peril.ai.AI;
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
	 * The pre-test setup operations.
	 * 
	 * @throws Exception
	 *             If setup fails throw Exception.
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * This test confirms if the {@link ModelPlayer#setCountriesRuled(int)} method sets
	 * the correct strength of a {@link ModelPlayer}
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_setCountriesRuled() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);

		modelPlayer.setCountriesRuled(5);
		assertEquals(5, modelPlayer.getCountriesRuled());

		// Should throw an illegal argument exception
		modelPlayer.setCountriesRuled(-1);
	}

	/**
	 * This test confirms if the {@link ModelPlayer#getCountriesRuled()} method
	 * retrieves the correct number of countries ruled by a {@link ModelPlayer}.
	 */
	@Test
	public void test_getCountriesRuled() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);
		assertEquals(0, modelPlayer.getCountriesRuled());
		modelPlayer.setCountriesRuled(23);
		assertEquals(23, modelPlayer.getCountriesRuled());
	}

	/**
	 * This test confirms if the {@link ModelPlayer#setContinentsRuled(int)} method
	 * sets the correct number of continents of a {@link ModelPlayer}
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_setContinentsRuled() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);

		modelPlayer.setContinentsRuled(5);
		assertEquals(5, modelPlayer.getContinentsRuled());

		// Should throw an illegal argument exception
		modelPlayer.setContinentsRuled(-1);
	}

	/**
	 * This test confirms if the {@link ModelPlayer#getPoints()} method retrieves
	 * the correct number of points held by a {@link ModelPlayer}.
	 */
	@Test
	public void test_getPoints() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);
		assertEquals(0, modelPlayer.getPoints());
		modelPlayer.setPoints(7);
		assertEquals(7, modelPlayer.getPoints());
	}

	/**
	 * This test confirms if the {@link ModelPlayer#setPoints(int)} method sets the
	 * correct number of points for a {@link ModelPlayer}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_setPoints() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);

		// Should throw an illegal argument exception
		modelPlayer.setPoints(-1);
	}

	/**
	 * This test confirms if the {@link ModelPlayer#setCountriesRuled(int)} method adds
	 * the correct number of points to{@link ModelPlayer}
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_addPoints() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);
		modelPlayer.setPoints(0);

		assertEquals(0, modelPlayer.getPoints());
		modelPlayer.addPoints(5);
		assertEquals(5, modelPlayer.getPoints());
		modelPlayer.addPoints(10);
		assertEquals(15, modelPlayer.getPoints());

		// Should throw an illegal argument exception
		modelPlayer.addPoints(-1);

	}

	/**
	 * This test confirms if the {@link ModelPlayer#getDistributableArmy()} method
	 * retrieves the {@link ModelArmy} owned by a {@link ModelPlayer} for
	 * distribution.
	 */
	@Test
	public void test_getDistributableArmy() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);
		assertFalse(modelPlayer.getDistributableArmy().equals(null));
		assertTrue(modelPlayer.getDistributableArmy() instanceof ModelArmy);

	}

	/**
	 * This test confirms if the {@link ModelPlayer#getTotalArmy()} method retrieves
	 * the {@link ModelPlayer}s instance of {@link ModelArmy}.
	 */
	@Test
	public void test_getTotalArmy() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);
		assertTrue(modelPlayer.getTotalArmy() instanceof ModelArmy);
	}

	/**
	 * This test confirms if the {@link ModelPlayer#getUnitsKilled()} method
	 * retrieves the correct number of units killed by a {@link ModelPlayer}.
	 */
	@Test
	public void test_getUnitsKilled() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);
		assertEquals(0, modelPlayer.getUnitsKilled());
		modelPlayer.setUnitsKilled(23);
		assertEquals(23, modelPlayer.getUnitsKilled());
	}

	/**
	 * This test confirms if the {@link ModelPlayer#getCountriesTaken()} method
	 * retrieves the correct number of countries taken by a {@link ModelPlayer}.
	 */
	@Test
	public void test_getCountriesTaken() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);
		assertEquals(0, modelPlayer.getCountriesTaken());
		modelPlayer.setCountriesTaken(20);
		assertEquals(20, modelPlayer.getCountriesTaken());
	}

	/**
	 * This test confirms if the {@link ModelPlayer#setCountriesTaken(int)} method
	 * sets the correct number of countries taken for a {@link ModelPlayer}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_setCountriesTaken() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);

		modelPlayer.setCountriesTaken(5);
		assertEquals(5, modelPlayer.getCountriesTaken());

		// Should throw an illegal argument exception
		modelPlayer.setCountriesTaken(-1);
	}

	/**
	 * This test confirms if the {@link ModelPlayer#getPointsSpent()} method
	 * retrieves the correct number of points spent by a {@link ModelPlayer}.
	 */
	@Test
	public void test_getPointsSpent() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);
		assertEquals(0, modelPlayer.getPointsSpent());
		modelPlayer.setPoints(8);
		modelPlayer.spendPoints(2);
		assertEquals(2, modelPlayer.getPointsSpent());
		modelPlayer.spendPoints(6);
		assertEquals(8, modelPlayer.getPointsSpent());
	}

	/**
	 * This test confirms if the {@link ModelPlayer#spendPoints(int)} method spends
	 * the correct number of points from.{@link ModelPlayer}
	 */
	@Test(expected = IllegalArgumentException.class)

	public void test_spendPoints() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);
		modelPlayer.setPoints(50);

		assertEquals(50, modelPlayer.getPoints());
		modelPlayer.spendPoints(5);
		assertEquals(45, modelPlayer.getPoints());
		modelPlayer.spendPoints(10);
		assertEquals(35, modelPlayer.getPoints());
		modelPlayer.spendPoints(35);
		assertEquals(0, modelPlayer.getPoints());

		// Should throw an illegal argument exception
		modelPlayer.spendPoints(55);
		modelPlayer.spendPoints(-5);

	}

	@Test(expected = IllegalArgumentException.class)
	public void test_setPointsSpent() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);

		modelPlayer.setPointsSpent(5);
		assertEquals(5, modelPlayer.getPointsSpent());

		// Should throw an illegal argument exception
		modelPlayer.setPointsSpent(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_addUnitsKilled() {
		ModelPlayer modelPlayer = new ModelPlayer(0, AI.USER);

		// Should throw an illegal argument exception
		modelPlayer.addUnitsKilled(-1);
	}
}
