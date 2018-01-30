package peril.board;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import peril.views.slick.board.SlickContinent;

/**
 * This test case tests {@link SlickContinent} from the perspectve of a client class.
 * 
 * @author Joshua_Eddy
 *
 */
public class Test_Continent {

<<<<<<< HEAD
	/**
	 * Performed before each test.
	 * @throws Exception
	 */
=======
	private Country testCountry;

	private Continent testContinent;

	private String testContinentName;

	private String testCountryName;

	private EnvironmentalHazard testHazard;

>>>>>>> branch 'James' of https://github.com/RelativeForce/CS2010.git
	@Before
	public void setUp() throws Exception {

		testContinentName = "Europe";
		testCountryName = "France";
		testContinent = new Continent(testHazard, testContinentName, null);

	}

	@Test
	public void test_addCountry() {

		testCountry = new Country(testCountryName, null, null);

		testContinent.addCountry(testCountry);

		List<Country> resultList = testContinent.getCountries();

		assertTrue(!resultList.isEmpty());

		assertTrue(resultList.size() == 1);

		assertTrue(resultList.contains(testCountry));

	}

}
