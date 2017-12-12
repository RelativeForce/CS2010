package peril.io;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * This tests the {@link TextFileWriter} that will be used to save the game.
 * 
 * @author Joshua_Eddy
 *
 */
public class Test_TextFileWriter {

	/**
	 * Holds the {@link TextFileWriter} that will be tested.
	 */
	private TextFileWriter writer;

	/**
	 * Pre test setup
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {

		writer = new TextFileWriter("monkey-butt.txt", true);

	}

	/**
	 * Test writing to a file.
	 */
	@Test
	public void test_write() {

		writer.open();
		writer.writeLine("Pickle Rick 1");
		writer.writeLine("Pickle Rick 2");
		writer.writeLine("Pickle Rick 3");
		writer.save();

		assertTrue(true);
	}

}
