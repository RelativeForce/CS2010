package peril.views.slick.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests that {@link Point} performs correctly.
 * 
 * @author Joshua_Eddy
 *
 * @version 1.01.01
 * @since 2018-03-16
 *
 * @see Point
 */
public final class Test_Point {

	/**
	 * Test whether the {@link Point} can be assigned an x and a y value.
	 */
	@Test
	public void test_construct() {

		// Test variables.
		final int x = 0;
		final int y = 0;

		// Constructs the test point
		final Point testPoint = new Point(x, y);

		// Assert that the coordinate has been stored in the Point.
		assertTrue(testPoint.x == x);
		assertTrue(testPoint.y == y);

	}

	/**
	 * Tests that {@link Point#getMiddle(Point, Point)} performs correctly.
	 */
	@Test
	public void test_getMiddle_normal() {

		// Define point 1
		final int x1 = 0;
		final int y1 = 0;
		final Point testPoint1 = new Point(x1, y1);

		// Define point 2
		final int x2 = 10;
		final int y2 = 10;
		final Point testPoint2 = new Point(x2, y2);

		// The middle point
		final Point testMiddle = Point.getMiddle(testPoint1, testPoint2);

		// Assert that the point is half way between point 1 and 2.
		assertTrue(testMiddle.x == (x1 + x2) / 2);
		assertTrue(testMiddle.y == (y1 + y2) / 2);

	}

	/**
	 * Asserts that {@link Point#getMiddle(Point, Point)} throws a
	 * {@link NullPointerException} if null points are given as parameters.
	 */
	@Test(expected = NullPointerException.class)
	public void test_getMiddle_null() {

		// Perform the method with null points.
		Point.getMiddle(null, null);

	}

	/**
	 * Asserts that {@link Point#equals(Object)} returns true if two {@link Point}s
	 * have the same x and y.
	 */
	@Test
	public void test_equals() {

		// Define x and y coordinates.
		final int x1 = 5;
		final int y1 = 4;

		// Creates some points.
		final Point testPoint1 = new Point(x1, y1);
		final Point testPoint2 = new Point(x1, y1);
		final Point testPoint3 = new Point(x1, y1 + 1);

		assertTrue(testPoint1.equals(testPoint2));
		assertTrue(!testPoint1.equals(testPoint3));
	}

}
