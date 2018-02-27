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
		
		ModelArmy modelArmyOne = new ModelArmy();
		
		modelArmyOne.add(testUnitOne);
		assertEquals(1, modelArmyOne.getStrength());
		modelArmyOne.add(testUnitTwo);
		assertEquals(3, modelArmyOne.getStrength());
		modelArmyOne.add(testUnitThree);
		assertEquals(6, modelArmyOne.getStrength());
		//System.out.println("This is the ModelArmy strength " + modelArmyOne.getStrength());

		
	}


	
}
