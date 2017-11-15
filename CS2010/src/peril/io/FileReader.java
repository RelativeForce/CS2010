package peril.io;

public interface FileReader {

	/**
	 * Parses a line of the file and selects what action should be taken.
	 */
	public void parseLine();

	public int getIndex();

	public int getLength();
	
	public boolean isFinished();

}
