package peril.model.board;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import peril.ai.AI;
import peril.model.ModelColor;
import peril.model.ModelPlayer;

/**
 * This test class ensures that {@link ModelContinent} functions correctly
 * properly.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-03-11
 * 
 * @see ModelContinent
 *
 */
public final class Test_ModelContinent {

	/**
	 * The {@link Set} of {@link ModelCountry}s to use in the tests.
	 */
	private Set<ModelCountry> testCountries;

	/**
	 * Holds the {@link ModelContinent} that will be used to test
	 * {@link ModelContinent}.
	 */
	private ModelContinent testContinent;

	/**
	 * Performed before the tests. This adds {@link #testCountries} to the
	 * {@link #testContinent}.
	 * 
	 * @throws Exception
	 *             If setup fails throw Exception.
	 */
	@Before
	public void setUp() throws Exception {

		this.testCountries = new HashSet<>();
		this.testCountries.add(new ModelCountry("testCountry1", new ModelColor(0, 0, 0)));
		this.testCountries.add(new ModelCountry("testCountry2", new ModelColor(0, 0, 1)));
		this.testCountries.add(new ModelCountry("testCountry3", new ModelColor(0, 1, 0)));
		this.testCountries.add(new ModelCountry("testCountry4", new ModelColor(1, 0, 0)));

		this.testContinent = new ModelContinent(ModelHazard.TORNADO, "testContinent");

		this.testCountries.forEach(country -> testContinent.addCountry(country));

	}

	/**
	 * Tests {@link ModelContinent#isRuled()} functions properly.
	 */
	@Test
	public void test_isRuled() {

		final ModelPlayer testPlayer1 = new ModelPlayer(1, AI.USER);
		final ModelPlayer testPlayer2 = new ModelPlayer(2, AI.USER);
		final ModelCountry testCountry = testCountries.iterator().next();

		// Assert that the test continent is not ruled by a single player.
		assertTrue(!testContinent.isRuled());

		// Set all the countries with the same ruler.
		testCountries.forEach(country -> country.setRuler(testPlayer1));

		assertTrue(testContinent.isRuled());

		// Set one countries back to null
		testCountry.setRuler(null);

		// Assert that the test continent is not ruled by a single player.
		assertTrue(!testContinent.isRuled());

		// Set one of the countries to a different ruler.
		testCountry.setRuler(testPlayer2);

		// Assert that the test continent is not ruled by a single player.
		assertTrue(!testContinent.isRuled());

		// Reset all of the countries to no ruler.
		testCountries.forEach(country -> country.setRuler(null));
	}

	/**
	 * Tests that {@link ModelContinent#getRuler()} functions properly.
	 */
	@Test
	public void test_getRuler() {

		final ModelPlayer testPlayer1 = new ModelPlayer(1, AI.USER);
		final ModelPlayer testPlayer2 = new ModelPlayer(2, AI.USER);
		final ModelCountry testCountry = testCountries.iterator().next();

		// Assert that the test continent is not ruled by a single player.
		assertTrue(testContinent.getRuler() == null);

		// Set all the countries with the same ruler.
		testCountries.forEach(country -> country.setRuler(testPlayer1));

		assertTrue(testContinent.getRuler() == testPlayer1);

		// Set one countries back to null
		testCountry.setRuler(null);

		// Assert that the test continent is not ruled by a single player.
		assertTrue(testContinent.getRuler() == null);

		// Set one of the countries to a different ruler.
		testCountry.setRuler(testPlayer2);

		// Assert that the test continent is not ruled by a single player.
		assertTrue(testContinent.getRuler() == null);

		// Reset all of the countries to no ruler.
		testCountries.forEach(country -> country.setRuler(null));

	}

}
