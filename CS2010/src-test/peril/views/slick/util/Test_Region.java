package peril.views.slick.util;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * Tests {@link Region}.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-03-10
 * 
 * @see Region
 * @see LinkedList
 * @see List
 * @see Point
 *
 */
public final class Test_Region {

	/**
	 * The array that will be used to create the {@link Region} to be tested.
	 */
	private boolean[] testArray;

	/**
	 * The {@link Region} that will be the primary source of the test.
	 */
	private Region testRegion;

	/**
	 * Sets up the initial objects that will be used in the tests.
	 * 
	 * @throws Exception
	 *             Triggered by set up fail.
	 */
	@Before
	public void setUp() throws Exception {

		// Construct a 4x4 image boolean array with a 2x2 true region in the middle.
		testArray = new boolean[] { false, false, false, false, false, true, true, false, false, true, true, false,
				false, false, false, false };

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
		assertTrue(testArray[5] && testArray[6] && testArray[9] && testArray[10]);

		// Assert that the x position is 1 as the lowest true value is at x = 1.
		assertTrue(testRegion.getPosition().x == 1);

		// Assert that the y position is 1 as the lowest true value is at y = 1.
		assertTrue(testRegion.getPosition().y == 1);

		// Assert that the width is 2 as the width of the region should be 2.
		assertTrue(testRegion.getWidth() == 2);

		// Assert that the height is 2 as the height of the region should be 2.
		assertTrue(testRegion.getHeight() == 2);

		final boolean[] object = testRegion.getObject();

		// Assert that the Region reducer has shrunk the array to the 2x2.
		assertTrue(object[0] && object[1] && object[2] && object[3]);

	}

	/**
	 * Test that {@link Region#isValid(Point)} works for
	 * {@link Test_Region#testRegion}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_IsValid() {

		// Assert that the (0,0) is not inside the region
		assertTrue(!testRegion.isValid(new Point(0, 0)));

		// Assert that (1,1) is inside the region
		assertTrue(testRegion.isValid(new Point(1, 1)));

		// Assert that you cannot give a null Point.
		assertTrue(testRegion.isValid(null));

	}

	/**
	 * Test the {@link Region#combine(List, int, int)} on
	 * {@link Test_Region#testRegion}.
	 */
	@Test
	public void test_Combine() {

		// Construct a 4x4 image boolean array with a 1x1 true region in the at (1,4).
		final boolean[] testArrayToCombine = new boolean[] { false, false, false, false, false, false, false, true,
				false, false, false, false, false, false, false, false };

		// Creates a Region that is to be combined with the test region.
		final Region testRegionToCombine = new Region(testArrayToCombine, 4, 4);

		// Add both regions to the list of regions to combine.
		final List<Region> toCombineList = new LinkedList<>();
		toCombineList.add(testRegionToCombine);
		toCombineList.add(testRegion);

		// Combine the regions and then store it.
		final Region combined = Region.combine(toCombineList, 4, 4);

		// Assert that the position of the region is (1,1)
		assertTrue(combined.getPosition().x == 1);
		assertTrue(combined.getPosition().y == 1);

		// Assert that the region has dimensions of 2x3
		assertTrue(combined.getWidth() == 2);
		assertTrue(combined.getHeight() == 3);

		final boolean[] object = combined.getObject();

		// Assert that the points in the region have the correct boolean state.
		assertTrue(object[0]);
		assertTrue(object[1]);
		assertTrue(object[2]);
		assertTrue(object[3]);
		assertTrue(object[4]);
		assertTrue(!object[5]);

	}

	/**
	 * Test the {@link Region#overlap(Region, Region)}
	 */
	@Test
	public void test_overlap() {

		// A test 1x1 region to test over lap with test 2x2 region.
		final Region testRegionToOverlap = new Region(new boolean[] { true }, 1, 1);

		// Assert the default position of the region.
		assertTrue(testRegionToOverlap.getPosition().x == 0);
		assertTrue(testRegionToOverlap.getPosition().y == 0);

		// Assert that the 1x1 region at (0,0) does not over lap with the 2x2 test
		// region at (1,1)
		assertTrue(!Region.overlap(testRegion, testRegionToOverlap));

		// Move the region to (1,1)
		testRegionToOverlap.setPosition(new Point(1, 1));

		// Assert that the 1x1 region at (1,1) does not over lap with the 2x2 test
		// region at (1,1)
		assertTrue(Region.overlap(testRegion, testRegionToOverlap));

	}

}
