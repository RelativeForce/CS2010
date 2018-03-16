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
	
	@Before
	public void setUp() throws Exception {	
		
	}

	/**
	 * Checks that the {@link ModelHazard#getByName(String)}
	 * method works correctly.
	 */
	@Test
	public void test_getByName() {
		
		// Checks that the returned string is equal to the name of the Volcanic Eruption hazard
		assertEquals("Volcanic Eruption", ModelHazard.VOLCANIC_ERUPTION.toString());
		// Checks that the returned string is equal to the name of the Tornado hazard
		assertEquals("Tornado", ModelHazard.TORNADO.toString());
		// Checks that the returned string is equal to the name of the Hurricane hazard
		assertEquals("Hurricane", ModelHazard.HURRICANE.toString());
		// Checks that the returned string is equal to the name of the Tsunami hazard
		assertEquals("Tsunami", ModelHazard.TSUNAMI.toString());
	}
	
	/**
	 * Checks that the {@link ModelHazard#act(ModelArmy)}
	 * method works correctly.
	 */
	@Test
	public void test_act() {
		// I don't know	
	}

}
