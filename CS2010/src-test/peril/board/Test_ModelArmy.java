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
		
		ModelUnit testUnit = new ModelUnit("test", 1, "fileName");
		UnitHelper.getInstance().addUnit(testUnit);
		
		ModelArmy modelArmyOne = new ModelArmy();
		modelArmyOne.add(testUnit);
		modelArmyOne.setStrength(1);
		
		System.out.println("This is the ModelArmy strength " + modelArmyOne.getStrength());
		assertEquals(1, modelArmyOne.getStrength());
		
	}


	
}
