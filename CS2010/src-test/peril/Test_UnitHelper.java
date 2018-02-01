package peril;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import peril.helpers.UnitHelper;
import peril.model.board.ModelUnit;

public class Test_UnitHelper {

	private final UnitHelper helper = UnitHelper.getInstance();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {

		ModelUnit soldier = new ModelUnit("soldier", 1, "");
		ModelUnit car = new ModelUnit("car", 3, "");
		ModelUnit tank = new ModelUnit("tank", 5, "");

		helper.clear();
		helper.addUnit(soldier);

		assertTrue(helper.getStrongest().name.equals(soldier.name));
		assertTrue(helper.getWeakest().name.equals(soldier.name));

		helper.addUnit(car);
		helper.addUnit(tank);

		assertTrue(helper.getWeakest().name.equals(soldier.name));
		assertTrue(helper.getStrongest().name.equals(tank.name));

		assertTrue(helper.getUnitAbove(car).name.equals(tank.name));
		assertTrue(helper.getUnitBelow(car).name.equals(soldier.name));

		assertTrue(helper.getUnitAbove(tank) == null);
		assertTrue(helper.getUnitBelow(soldier) == null);

	}

}
