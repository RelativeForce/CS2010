package peril.board;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import peril.Point;
import peril.ui.components.Region;

/**
 * This class tests {@link Country}.
 * 
 * @author James_Rowntree
 *
 */
public class Test_Country {
	
	/**
	 * Hold the current {@link Country} that is being used for testing. 
	 */
	private Country testCountry;
	/**
	 * Holds the name of the {@link Test_Country#testCountry}, used for testing.
	 */
	private String testName;

	/**
	 * Constructs a new country with the given name.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		testName = "France";
		testCountry = new Country(testName, new Region(50,50, new Point(100,100)));

	}

	/**
	 * Checks that the neighbour cannot be null.
	 */
	@Test(expected = NullPointerException.class)
	public void test_addNullNeighbour() {
		testCountry.addNeighbour(null);
	}

	/**
	 * Checks that the {@link addNeighbour} method works correctly.
	 */
	@Test
	public void test_addNeighbour() {
		// Creates a new country with the name of testName
		Country newNeighbour = new Country(testName, new Region(50,50, new Point(100,100)));

		// Adds a country to the list of neighbours in the testCountry.
		testCountry.addNeighbour(newNeighbour);

		// Stores the current neighbours of the testCountry in resultList.
		List<Country> resultList = testCountry.getNeighbours();

		// Asserting that there is a neighbour in the list.
		assertTrue(!resultList.isEmpty());

		// Assert that there is only one neighbour in the list.
		assertTrue(resultList.size() == 1);

		// Assert that the only neighbour in the list is the new neighbour country.
		assertTrue(resultList.contains(newNeighbour));
	}

	/**
	 * Checks that the army cannot be set to null.
	 */
	@Test(expected = NullPointerException.class)
	public void test_setNullArmy() {
		testCountry.setArmy(null);

	}

	/**
	 * Checks that the army passed into {@link Country#setArmy} is the actual army
	 * and not null.
	 */
	@Test
	public void test_setArmy() {
		// Creates a new army object stores it in 'newArmy'
		Army newArmy = new Army();
		// Calls the mutator 'setArmy' and assigns it the new army object.
		testCountry.setArmy(newArmy);
		// Checks that the army created and stored is the 'newArmy'.
		assertTrue(testCountry.getArmy().equals(newArmy));
	}

}
