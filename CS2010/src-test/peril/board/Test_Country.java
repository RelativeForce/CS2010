package peril.board;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import peril.model.ModelColor;
import peril.model.board.ModelCountry;

import peril.Player;

/**
 * This class tests {@link SlickCountry}.
 * 
 * @author James_Rowntree
 *
 */
public class Test_Country {

	/**
	 * Hold the current {@link SlickCountry} that is being used for testing.
	 */
	private ModelCountry testCountry;

	/**
	 * Holds the name of the {@link Test_Country#testCountry}, used for testing.
	 */
	private String testName;

	/**
	 * Constructs a new country with the given name.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		testName = "France";
		testCountry = new ModelCountry(testName, new ModelColor(0, 0, 0));

	}

	@Test
	public void test_ruler() {

		Player newRuler = Player.FOUR;

		testCountry.setRuler(newRuler);

		assertTrue(testCountry.getRuler().equals(newRuler));

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
		ModelCountry newNeighbour = new ModelCountry(testName, new ModelColor(0, 0, 0));

		// Adds a country to the list of neighbours in the testCountry.
		testCountry.addNeighbour(newNeighbour);

		// Stores the current neighbours of the testCountry in resultList.
		List<ModelCountry> resultList = testCountry.getNeighbours();

		// Asserting that there is a neighbour in the list.
		assertTrue(!resultList.isEmpty());

		// Assert that there is only one neighbour in the list.
		assertTrue(resultList.size() == 1);

		// Assert that the only neighbour in the list is the new neighbour country.
		assertTrue(resultList.contains(newNeighbour));
	}

}
