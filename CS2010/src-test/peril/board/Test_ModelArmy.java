package peril.board;

import static org.junit.Assert.*;

import javax.jws.WebParam.Mode;

import org.junit.Before;
import org.junit.Test;

import peril.helpers.UnitHelper;
import peril.model.board.ModelArmy;
import peril.model.board.ModelUnit;

public class Test_ModelArmy {

	@Before
	public void setUp() throws Exception {

	}

	/**
	 * This test confirms if the {@link ModelArmy#getStrength()} method retrieves
	 * the correct strength of a {@link ModelArmy}.
	 */
	@Test
	public void test_GetStrength() {

		ModelUnit testUnitOne = new ModelUnit("testOne", 1, "fileNameOne");
		ModelUnit testUnitTwo = new ModelUnit("testTwo", 2, "fileNameTwo");
		ModelUnit testUnitThree = new ModelUnit("testThree", 3, "fileNameThree");

		// Clear instance of UnitHelper
		UnitHelper.getInstance().clear();
		// Add the units to UnitHelper
		UnitHelper.getInstance().addUnit(testUnitOne);
		UnitHelper.getInstance().addUnit(testUnitTwo);
		UnitHelper.getInstance().addUnit(testUnitThree);

		ModelArmy modelArmy = new ModelArmy();

		// Add units to modelArmy
		// and asserts the correct values
		modelArmy.add(testUnitOne);
		assertEquals(1, modelArmy.getStrength());
		modelArmy.add(testUnitTwo);
		assertEquals(3, modelArmy.getStrength());
		modelArmy.add(testUnitThree);
		assertEquals(6, modelArmy.getStrength());
		// System.out.println("This is the ModelArmy strength " +
		// modelArmyOne.getStrength());
	}

	/**
	 * This test confirms if the {@link ModelArmy#setStrength()} method sets the
	 * correct strength of a {@link ModelArmy}
	 */
	@Test
	public void test_SetStrength() {

		UnitHelper.getInstance().clear();

		ModelUnit testUnitOne = new ModelUnit("testUnitOne", 1, "fileName");
		ModelUnit testUnitTwo = new ModelUnit("testUnitTwo", 10, "fileName");

		ModelArmy modelArmyOne = new ModelArmy();
		ModelArmy modelArmyTwo = new ModelArmy();
		// Combination of Three units
		ModelArmy modelArmyThree = new ModelArmy();

		UnitHelper.getInstance().addUnit(testUnitOne);
		UnitHelper.getInstance().addUnit(testUnitTwo);

		modelArmyOne.add(testUnitOne);
		modelArmyOne.setStrength(2);
		assertEquals(2, modelArmyOne.getStrength());

		modelArmyTwo.add(testUnitTwo);
		modelArmyTwo.setStrength(1);
		assertEquals(1, modelArmyTwo.getStrength());

		modelArmyThree.add(testUnitOne);
		modelArmyThree.add(testUnitTwo);
		assertEquals(11, modelArmyThree.getStrength());

	}

	/**
	 * This test confirms if {@link ModelArmy#remove(int)} and
	 * {@link ModelArmy#remove(ModelUnit)} works.
	 * 
	 */
	@Test
	public void test_Remove() {

		ModelUnit testUnitOne = new ModelUnit("testOne", 1, "fileNameOne");
		ModelUnit testUnitTwo = new ModelUnit("testTwo", 2, "fileNameTwo");
		ModelUnit testUnitThree = new ModelUnit("testThree", 3, "fileNameThree");

		UnitHelper.getInstance().clear();
		UnitHelper.getInstance().addUnit(testUnitOne);
		UnitHelper.getInstance().addUnit(testUnitTwo);
		UnitHelper.getInstance().addUnit(testUnitThree);

		ModelArmy modelArmyOne = new ModelArmy();
		modelArmyOne.add(testUnitOne);
		modelArmyOne.add(testUnitTwo);
		modelArmyOne.add(testUnitThree);

		modelArmyOne.remove(testUnitOne);
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
	public void test_Remove_BreakUnit() {

		final ModelUnit testUnit1 = new ModelUnit("test1", 1, "na");
		final ModelUnit testUnit2 = new ModelUnit("test2", 5, "na");
		final ModelUnit testUnit3 = new ModelUnit("test3", 15, "na");

		// Add the units to the unit helper
		UnitHelper.getInstance().clear();
		UnitHelper.getInstance().addUnit(testUnit1);
		UnitHelper.getInstance().addUnit(testUnit2);
		UnitHelper.getInstance().addUnit(testUnit3);

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

		System.out.println("Test: remove() - Breaking down units");

		// Print the number of units for feed back sake.
		for (ModelUnit unit : testArmy) {
			System.out.println(unit.name + " " + unit.strength + " " + testArmy.getNumberOf(unit));
		}

	}

}
