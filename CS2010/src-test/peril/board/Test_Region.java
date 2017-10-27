package peril.board;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import peril.Point;

public class Test_Region {

	/**
	 * 
	 */
	private boolean[][] testRegionMap;

	private Region testRegion;

	@Before
	public void setUp() throws Exception {

		// Construct a 4x4 image boolean array with a 2x2 true region in the middle.
		testRegionMap = new boolean[][] { { false, false, false, false }, { false, true, true, false },
				{ false, true, true, false }, { false, false, false, false } };

		testRegion = new Region(testRegionMap, 4, 4);
	}

	@Test
	public void test_ConstructionByArray() {

		// Assert that the 2x2 true region is stored and can be referenced in the way
		// that
		// region will use it.
		assertTrue(testRegionMap[1][1] && testRegionMap[1][2] && testRegionMap[2][1] && testRegionMap[2][2]);

		assertTrue(testRegion.x == 1);

		assertTrue(testRegion.y == 1);

		assertTrue(testRegion.width == 2);

		assertTrue(testRegion.height == 2);

		assertTrue(testRegion.object[0][0] && testRegion.object[0][1] && testRegion.object[1][0]
				&& testRegion.object[1][1]);

	}

	@Test(expected = IllegalArgumentException.class)
	public void test_IsInside() {

		assertTrue(!testRegion.isInside(new Point(0, 0)));

		assertTrue(testRegion.isInside(new Point(1, 1)));

		assertTrue(testRegion.isInside(null));

	}

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

		assertTrue(combined.x == 1);

		assertTrue(combined.y == 1);

		assertTrue(combined.width == 2);

		assertTrue(combined.height == 3);

		assertTrue(combined.object[0][0]);

		assertTrue(combined.object[0][1]);

		assertTrue(combined.object[1][0]);

		assertTrue(combined.object[1][1]);

		assertTrue(combined.object[0][2]);

		assertTrue(!combined.object[1][2]);

	}

}
