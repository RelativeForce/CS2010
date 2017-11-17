package peril.io.fileParsers;

import peril.io.fileReaders.TextFileReader;

/**
 * 
 * Defines the behaviour of an object that can parse a text file. The object
 * will retrieve the file commonly using {@link TextFileReader}. The lines of
 * the reader will be read sequentially using {@link FileParser#parseLine()}.
 * 
 * @author Joshua_Eddy
 *
 */
public interface FileParser {

	/**
	 * Parses a line of the {@link FileParser}.
	 */
	public void parseLine();

	/**
	 * Retrieves the index that this {@link FileParser} in the processing of their file.
	 * 
	 * @return <code>int</code>
	 */
	public int getIndex();

	/**
	 * Retrieves the length of the file that this {@link FileParser} must parse.
	 * 
	 * @return <code>int</code>
	 */
	public int getLength();

	/**
	 * Retrieves whether this {@link FileParser} has completely parse its associated
	 * file.
	 * 
	 * @return <code>boolean</code>
	 */
	default boolean isFinished() {
		return getIndex() == getLength();
	}

}
