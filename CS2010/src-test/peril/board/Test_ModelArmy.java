package peril.board;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import peril.helpers.UnitHelper;
import peril.model.board.ModelArmy;
import peril.model.board.ModelUnit;

public class Test_ModelArmy {
	
	@Before
	public void setUp() throws Exception {

	}
	
	@Test
	public void test_GetStrength() {
		
		ModelUnit testUnitOne = new ModelUnit("testOne", 1, "fileNameOne");
		ModelUnit testUnitTwo = new ModelUnit("testTwo", 2, "fileNameTwo");
		ModelUnit testUnitThree = new ModelUnit("testThree", 3 , "fileNameThree");
		
		UnitHelper.getInstance().clear();
		UnitHelper.getInstance().addUnit(testUnitOne);
		UnitHelper.getInstance().addUnit(testUnitTwo);
		UnitHelper.getInstance().addUnit(testUnitThree);
		
		ModelArmy modelArmy = new ModelArmy();
		
		modelArmy.add(testUnitOne);
		assertEquals(1, modelArmy.getStrength());
		modelArmy.add(testUnitTwo);
		assertEquals(3, modelArmy.getStrength());
		modelArmy.add(testUnitThree);
		assertEquals(6, modelArmy.getStrength());
		//System.out.println("This is the ModelArmy strength " + modelArmyOne.getStrength());
	}

	@Test
	public void test_SetStrength() {
		
		UnitHelper.getInstance().clear();
		
		ModelUnit testUnit = new ModelUnit("testUnit", 1, "fileName");
		ModelArmy modelArmy = new ModelArmy();
		
		UnitHelper.getInstance().addUnit(testUnit);
		
		modelArmy.add(testUnit);
		modelArmy.setStrength(2);
		assertEquals(2, modelArmy.getStrength());
		
	}
	
}
