package peril;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import peril.views.slick.util.Point;

public class Test_Point {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test whether the {@link Point} can be assigned an x and a y value.
	 */
	@Test
	public void testAssignment() {

		// Holds the point being tested.
		Point testPoint;

		// Test variables.
		int x = 0;
		int y = 0;

		// Constructs the test point
		testPoint = new Point(x, y);

		//Assert that the coordinate has been stored in the Point.
		assertTrue(testPoint.x == x);
		assertTrue(testPoint.y == y);

	}

}
