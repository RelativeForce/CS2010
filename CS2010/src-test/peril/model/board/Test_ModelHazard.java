package peril.model.board;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * This class tests {@link ModelHazard}.
 * 
 * @author Adrian_Wong
 *
 */

public class Test_ModelHazard {

	private String VOLCANIC_ERUPTION;
	
	private String TORNADO;
	
	private String HURRICANE;
	
	private String TSUNAMI;
	

	@Before
	public void setUp() throws Exception {

		VOLCANIC_ERUPTION = "Volcanic Eruption";
		TORNADO = "Tornado";
		HURRICANE = "Hurricane";
		TSUNAMI = "Tsunami";;
					
	}

	@Test
	public void test_getByName() {
		assertEquals(VOLCANIC_ERUPTION, ModelHazard.VOLCANIC_ERUPTION.toString());
		assertEquals(TORNADO, ModelHazard.TORNADO.toString());
		assertEquals(HURRICANE, ModelHazard.HURRICANE.toString());
		assertEquals(TSUNAMI, ModelHazard.TSUNAMI.toString());
	}

}
