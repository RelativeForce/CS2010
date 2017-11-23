package peril.board;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class Test_Continent {

	private Country testCountry;

	private Continent testContinent;

	private String testContinentName;

	private String testCountryName;

	private EnvironmentalHazard testHazard;

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
