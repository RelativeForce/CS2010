package peril.io;

public interface FileParser {

	/**
	 * Parses a line of the file and selects what action should be taken.
	 */
	public void parseLine();

	public int getIndex();

	public int getLength();
	
	public boolean isFinished();

}
