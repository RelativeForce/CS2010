package peril.model.board;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import peril.helpers.UnitHelper;

/**
 * 
 * Tests {@link ModelArmy}.
 * 
 * @author Ezekiel_Trinidad, Joshua_Eddy
 * 
 * @version 1.01.02
 * @since 2018-03-10
 * 
 * @see UnitHelper
 * @see ModelUnit
 *
 */
public final class Test_ModelArmy {

	/**
	 * The {@link UnitHelper} that holds the instance of
	 * {@link UnitHelper#getInstance()}.
	 */
	private UnitHelper unitHelper;

	/**
	 * The pre-test setup operations. 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		unitHelper = UnitHelper.getInstance();
	}

	/**
	 * This test confirms if the {@link ModelArmy#getStrength()} method retrieves
	 * the correct strength of a {@link ModelArmy}.
	 */
	@Test
	public void test_getStrength() {

		final ModelUnit testUnit1 = new ModelUnit("testOne", 1, "fileNameOne");
		final ModelUnit testUnit2 = new ModelUnit("testTwo", 2, "fileNameTwo");
		final ModelUnit testUnit3 = new ModelUnit("testThree", 3, "fileNameThree");

		// Clear instance of UnitHelper
		unitHelper.clear();
		// Add the units to UnitHelper
		unitHelper.addUnit(testUnit1);
		unitHelper.addUnit(testUnit2);
		unitHelper.addUnit(testUnit3);

		final ModelArmy modelArmy = new ModelArmy();

		// Add units to modelArmy
		// and asserts the correct values
		modelArmy.add(testUnit1);
		assertEquals(1, modelArmy.getStrength());
		modelArmy.add(testUnit2);
		assertEquals(3, modelArmy.getStrength());
		modelArmy.add(testUnit3);
		assertEquals(6, modelArmy.getStrength());
		// System.out.println("This is the ModelArmy strength " +
		// modelArmyOne.getStrength());
	}

	/**
	 * This test confirms if the {@link ModelArmy#setStrength()} method sets the
	 * correct strength of a {@link ModelArmy}
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_setStrength() {

		final ModelUnit testUnit1 = new ModelUnit("testUnit", 1, "na");

		unitHelper.clear();
		unitHelper.addUnit(testUnit1);

		// Create the test army
		final ModelArmy modelArmy1 = new ModelArmy();

		final int testStrength = 5;

		// Set the army to the test strength
		modelArmy1.setStrength(testStrength);
		assertEquals(testStrength, modelArmy1.getStrength());

		// Should throw an illegal argument exception
		modelArmy1.setStrength(-1);

	}

	/**
	 * This test confirms if {@link ModelArmy#remove(int)} and
	 * {@link ModelArmy#remove(ModelUnit)} works.
	 * 
	 */
	@Test
	public void test_remove_strength() {

		final ModelUnit testUnit1 = new ModelUnit("testOne", 1, "fileNameOne");
		final ModelUnit testUnit2 = new ModelUnit("testTwo", 2, "fileNameTwo");
		final ModelUnit testUnit3 = new ModelUnit("testThree", 3, "fileNameThree");

		unitHelper.clear();
		unitHelper.addUnit(testUnit1);
		unitHelper.addUnit(testUnit2);
		unitHelper.addUnit(testUnit3);

		ModelArmy modelArmyOne = new ModelArmy();
		modelArmyOne.add(testUnit1);
		modelArmyOne.add(testUnit2);
		modelArmyOne.add(testUnit3);

		modelArmyOne.remove(testUnit1);
		assertEquals(5, modelArmyOne.getStrength());
		modelArmyOne.remove(3);
		assertEquals(2, modelArmyOne.getStrength());

	}

	/**
	 * This test will confirm that if {@link ModelArmy#remove(ModelUnit)} is called
	 * on an army consisting of units large that the unit to remove, this army will
	 * break the units down until the unit can be remove.
	 */
	@Test
	public void test_remove_unit() {

		final ModelUnit testUnit1 = new ModelUnit("test1", 1, "na");
		final ModelUnit testUnit2 = new ModelUnit("test2", 5, "na");
		final ModelUnit testUnit3 = new ModelUnit("test3", 15, "na");

		// Add the units to the unit helper
		unitHelper.clear();
		unitHelper.addUnit(testUnit1);
		unitHelper.addUnit(testUnit2);
		unitHelper.addUnit(testUnit3);

		// The army being tested
		final ModelArmy testArmy = new ModelArmy();

		// Add the largest test unit
		testArmy.add(testUnit3);

		// Check the test unit was added correctly
		assertTrue(testArmy.getNumberOf(testUnit3) == 1);
		assertTrue(testArmy.getStrength() == testUnit3.strength);

		/*
		 * Remove 1 from a army with 15 strength. As there is only one unit of strength
		 * 15, when we remove one unit of strength 1 the unit with 15 strength should be
		 * broken into three units of strength 5. then one of those units should be
		 * broke into five units of strength 1. Then the unit that we want to remove can
		 * be removed.
		 */
		testArmy.remove(testUnit1);

		// Check the strength was removed
		assertTrue(testArmy.getStrength() == (testUnit3.strength - testUnit1.strength));

		// Check that the units were broken down correctly
		assertTrue(testArmy.getNumberOf(testUnit3) == 0);
		assertTrue(testArmy.getNumberOf(testUnit2) == 2);
		assertTrue(testArmy.getNumberOf(testUnit1) == 4);

	}

}
