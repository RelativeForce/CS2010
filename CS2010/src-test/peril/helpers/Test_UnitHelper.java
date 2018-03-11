package peril.helpers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import peril.helpers.UnitHelper;
import peril.model.board.ModelUnit;

/**
 * Tests {@link UnitHelper}
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-03-11
 * 
 * @see UnitHelper
 *
 */
public final class Test_UnitHelper {

	/**
	 * The {@link UnitHelper} that will be used for testing.
	 */
	private final UnitHelper helper = UnitHelper.getInstance();

	/**
	 * Clear the {@link UnitHelper} before any testing.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		helper.clear();
	}

	/**
	 * Test {@link UnitHelper#getStrongest()} functions property.
	 */
	@Test
	public void test_getStrongest() {

		// Create the units that will be used.
		final ModelUnit soldier = new ModelUnit("soldier", 1, "");
		final ModelUnit car = new ModelUnit("car", 3, "");
		final ModelUnit tank = new ModelUnit("tank", 5, "");

		// Add one unit to the unit helper
		helper.clear();

		// Add soldier so it is now the strongest.
		helper.addUnit(soldier);

		// Assert that the soldier is the strongest unit.
		assertTrue(helper.getStrongest().name.equals(soldier.name));

		// Add car so it is now the strongest.
		helper.addUnit(car);

		// Assert that the car is the strongest unit.
		assertTrue(helper.getStrongest().name.equals(car.name));

		// Add tank so it is now the strongest.
		helper.addUnit(tank);

		// Assert that the tank is the strongest unit.
		assertTrue(helper.getStrongest().name.equals(tank.name));

	}

	/**
	 * Test {@link UnitHelper#getWeakest()} functions properly.
	 */
	@Test
	public void test_getWeakest() {

		// Create the units that will be used.
		final ModelUnit soldier = new ModelUnit("soldier", 1, "");
		final ModelUnit car = new ModelUnit("car", 3, "");
		final ModelUnit tank = new ModelUnit("tank", 5, "");

		// Add one unit to the unit helper
		helper.clear();

		// Add tank so it is now the strongest.
		helper.addUnit(tank);

		// Assert that the tank is the strongest unit.
		assertTrue(helper.getWeakest().name.equals(tank.name));

		// Add car so it is now the strongest.
		helper.addUnit(car);

		// Assert that the car is the strongest unit.
		assertTrue(helper.getWeakest().name.equals(car.name));

		// Add soldier so it is now the strongest.
		helper.addUnit(soldier);

		// Assert that the soldier is the strongest unit.
		assertTrue(helper.getWeakest().name.equals(soldier.name));

	}

	/**
	 * Test {@link UnitHelper#getUnitAbove(ModelUnit)} and
	 * {@link UnitHelper#getUnitBelow(ModelUnit)}.
	 */
	@Test
	public void test_getRelativeTo() {

		// Create the units that will be used.
		final ModelUnit soldier = new ModelUnit("soldier", 1, "");
		final ModelUnit car = new ModelUnit("car", 3, "");
		final ModelUnit tank = new ModelUnit("tank", 5, "");

		// Add one unit to the unit helper
		helper.clear();
		helper.addUnit(car);

		// Assert that because there is only one unit. That unit is the strongest and
		// weakest unit in the unit helper.
		assertTrue(helper.getStrongest().name.equals(car.name));
		assertTrue(helper.getWeakest().name.equals(car.name));

		// Add two units, one above and one below the car.
		helper.addUnit(soldier);
		helper.addUnit(tank);

		// Assert that the soldier is the unit below the car.
		assertTrue(helper.getUnitBelow(car).name.equals(soldier.name));

		// Assert that the tank is the unit above the car.
		assertTrue(helper.getUnitAbove(car).name.equals(tank.name));

		// Assert that there is no above the tank and no unit below the soldier.
		assertTrue(helper.getUnitAbove(tank) == null);
		assertTrue(helper.getUnitBelow(soldier) == null);
	}

}
