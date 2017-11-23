package peril.io;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Test_TextFileWriter {

	private TextFileWriter writer;
	
	@Before
	public void setUp() throws Exception {
		
		writer = new TextFileWriter("monkey-butt.txt", true);
		
	}

	@Test
	public void test() {
		
		writer.open();
		writer.writeLine("Pickle Rick 1");
		writer.writeLine("Pickle Rick 2");
		writer.writeLine("Pickle Rick 3");
		writer.save();
		
		assertTrue(true);
	}
	
	

}
