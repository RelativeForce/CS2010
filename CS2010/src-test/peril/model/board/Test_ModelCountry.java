package peril.model.board;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import peril.ai.AI;
import peril.model.ModelColor;
import peril.model.ModelPlayer;
import peril.model.board.links.ModelLink;
import peril.model.board.links.ModelLinkState;

/**
 * This class tests {@link ModelCountry}.
 * 
 * @author James_Rowntree, Joshua_Eddy
 *
 */
public class Test_ModelCountry {

	/**
	 * Hold the current {@link SlickCountry} that is being used for testing.
	 */
	private ModelCountry testCountry;

	/**
	 * Holds the name of the {@link Test_ModelCountry#testCountry}, used for
	 * testing.
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

	/**
	 * Checks that the {@link ModelCountry#addNeighbour(ModelCountry, ModelLink)}
	 * method prevents null neighbours.
	 */
	@Test(expected = NullPointerException.class)
	public void test_addNullNeighbour() {
		testCountry.addNeighbour(null, null);
	}

	/**
	 * Checks that the {@link ModelCountry#addNeighbour(ModelCountry, ModelLink)}
	 * method works correctly.
	 */
	@Test
	public void test_addNeighbour() {

		// Creates a new country with the name of testName
		final ModelCountry testNeighbour = new ModelCountry("Germany", new ModelColor(0, 0, 0));

		// Adds a country to the list of neighbours in the testCountry.
		testCountry.addNeighbour(testNeighbour, new ModelLink(ModelLinkState.OPEN));
		
		// Assert the country is a neighbour.
		assertTrue(testCountry.isNeighbour(testNeighbour));

		// Stores the current neighbours of the testCountry in resultList.
		final Set<ModelCountry> neighbours = testCountry.getNeighbours();

		// Asserting that there is a neighbour in the list.
		assertTrue(!neighbours.isEmpty());

		// Assert that there is only one neighbour in the list.
		assertTrue(neighbours.size() == 1);

		// Assert that the only neighbour in the list is the new neighbour country.
		assertTrue(neighbours.contains(testNeighbour));

	}

	/**
	 * Check that {@link ModelCountry#setRuler(ModelPlayer)} functions properly.
	 */
	@Test
	public void test_setRuler() {

		// Create the test rulers of the test country
		final ModelPlayer testRuler1 = new ModelPlayer(1, AI.USER);
		final ModelPlayer testRuler2 = new ModelPlayer(2, AI.USER);

		// Asset there is no ruler by default
		assertTrue(testCountry.getRuler() == null);

		// Set testRuler1 as the ruler
		testCountry.setRuler(testRuler1);
		assertTrue(testCountry.getRuler() == testRuler1);

		// Set testRuler2 as the ruler
		testCountry.setRuler(testRuler2);
		assertTrue(testCountry.getRuler() == testRuler2);

		// Set the ruler as neutral
		testCountry.setRuler(null);
		assertTrue(testCountry.getRuler() == null);

	}

	/**
	 * Checks that {@link ModelCountry#getLinkTo(ModelCountry)} and
	 * {@link ModelCountry#changeLinkTo(ModelCountry, ModelLinkState, int)} function
	 * properly.
	 */
	@Test
	public void test_LinkTo() {

		// Creates a new country with the name of testName
		final ModelCountry testNeighbour = new ModelCountry("Great Britian", new ModelColor(0, 0, 0));
		final ModelLink testLink = new ModelLink(ModelLinkState.OPEN);

		// Adds a country to the list of neighbours in the testCountry.
		testCountry.addNeighbour(testNeighbour, testLink);

		// Assert that the link is the specified initial link.
		assertTrue(testCountry.getLinkTo(testNeighbour) == testLink);

		// Change the state of the link
		final int testDuration = 3;
		testCountry.changeLinkTo(testNeighbour, ModelLinkState.BLOCKADE, testDuration);

		// Assert the details of the new link.
		assertTrue(testCountry.getLinkTo(testNeighbour).getState() == ModelLinkState.BLOCKADE);
		assertTrue(testCountry.getLinkTo(testNeighbour).getDuration() == testDuration);
	}

}
