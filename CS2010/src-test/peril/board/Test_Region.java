package peril.board;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import peril.Point;

/**
 * Tests {@link Region}. In specific the ability for the {@link Region} to store
 * and perform actions on the hit detection box of an object.
 * 
 * @author Joshua_Eddy
 * 
 * @see Region
 * @see LinkedList
 * @see List
 *
 */
public class Test_Region {

	/**
	 * The array that will be used to create the {@link Region} to be tested.
	 */
	private boolean[][] testArray;

	/**
	 * The {@link Region}
	 */
	private Region testRegion;

	/**
	 * Sets up the inital objects that will be used in the tests.
	 * 
	 * @throws Exception
	 *             Triggered by set up fail.
	 */
	@Before
	public void setUp() throws Exception {

		// Construct a 4x4 image boolean array with a 2x2 true region in the middle.
		testArray = new boolean[][] { { false, false, false, false }, { false, true, true, false },
				{ false, true, true, false }, { false, false, false, false } };

		// Construct a region using the testArray.
		testRegion = new Region(testArray, 4, 4);
	}

	/**
	 * Test that {@link Test_Region#testRegion} was correctly constructed using
	 * {@link Test_Region#testArray}.
	 */
	@Test
	public void test_ConstructionByArray() {

		// Assert that the 2x2 true region is stored and can be referenced in the way
		// that region will use it.
		assertTrue(testArray[1][1] && testArray[1][2] && testArray[2][1] && testArray[2][2]);

		// Assert that the x position is 1 as the lowest true value is at x = 1.
		assertTrue(testRegion.x == 1);

		// Assert that the y position is 1 as the lowest true value is at y = 1.
		assertTrue(testRegion.y == 1);

		// Assert that the width is 2 as the width of the region should be 2.
		assertTrue(testRegion.width == 2);

		// Assert that the height is 2 as the height of the region should be 2.
		assertTrue(testRegion.height == 2);

		// Assert that the Region reducer has shrunk the array to the 2x2.
		assertTrue(testRegion.object[0][0] && testRegion.object[0][1] && testRegion.object[1][0]
				&& testRegion.object[1][1]);

	}

	/**
	 * Test that {@link Region#isInside(Point)} works for
	 * {@link Test_Region#testRegion}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_IsInside() {

		// Assert that the (0,0) is not inside the region
		assertTrue(!testRegion.isInside(new Point(0, 0)));

		// Assert that (1,1) is inside the region
		assertTrue(testRegion.isInside(new Point(1, 1)));

		// Assert that you cannot give a null Point.
		assertTrue(testRegion.isInside(null));

	}

	/**
	 * Test the {@link Region#combine(List, int, int)} on
	 * {@link Test_Region#testRegion}.
	 */
	@Test
	public void test_Combine() {

		// Construct a 4x4 image boolean array with a 1x1 true region in the at (1,4).
		boolean[][] testArrayToCombine = new boolean[][] { { false, false, false, false },
				{ false, false, false, true }, { false, false, false, false }, { false, false, false, false } };

		// Creates a Region that is to be combined with the test region.
		Region testRegionToCombine = new Region(testArrayToCombine, 4, 4);

		// Add both regions to the list of regions to combine.
		List<Region> toCombineList = new LinkedList<>();
		toCombineList.add(testRegionToCombine);
		toCombineList.add(testRegion);

		// Combine the regions and then store it.
		Region combined = Region.combine(toCombineList, 4, 4);

		// Assert that the position of the region is (1,1)
		assertTrue(combined.x == 1);
		assertTrue(combined.y == 1);

		// Assert that the region has dimensions of 2x3
		assertTrue(combined.width == 2);
		assertTrue(combined.height == 3);

		// Assert that the points in the region have the correct boolean state.
		assertTrue(combined.object[0][0]);
		assertTrue(combined.object[0][1]);
		assertTrue(combined.object[1][0]);
		assertTrue(combined.object[1][1]);
		assertTrue(combined.object[0][2]);
		assertTrue(!combined.object[1][2]);

	}

}
