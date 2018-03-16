package peril.io;

import peril.controllers.Directory;

/**
 * 
 * Defines the behaviour of an object that can parse a text file. The object
 * will retrieve the file commonly using {@link TextFileReader}. The lines of
 * the reader will be read sequentially using {@link FileParser#parseLine()}.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-02-06
 *
 */
public abstract class FileParser {

	/**
	 * The path to the directory that contains the file.
	 */
	protected final Directory directory;

	/**
	 * Holds the lines of the file to be parsed by this {@link FileParser}.
	 */
	protected final String[] lines;

	/**
	 * The index of the next line that will be parsed by
	 * {@link FileParser#parseLine()}.
	 */
	protected int index;

	/**
	 * Constructs a new {@link FileParser}.
	 * 
	 * @param directory
	 *            The {@link Directory} of the file.
	 * @param filename
	 *            The file name.
	 */
	public FileParser(String sourceFilePath, Directory directory, String filename) {

		if (directory == null) {
			throw new NullPointerException("File path cannot be null.");
		} else if (filename.isEmpty()) {
			throw new IllegalArgumentException("File name cannot be empty.");
		}
		this.directory = directory;
		this.lines = TextFileReader.scanFile(sourceFilePath, filename);
		this.index = 0;
	}

	/**
	 * Parses a line of the {@link FileParser}.
	 */
	public abstract void parseLine();

	/**
	 * Retrieves the index that this {@link FileParser} in the processing of their
	 * file.
	 * 
	 * @return <code>int</code>
	 */
	public final int getIndex() {
		return index;
	}

	/**
	 * Retrieves the length of the file that this {@link FileParser} must parse.
	 * 
	 * @return <code>int</code>
	 */
	public final int getLength() {
		return lines.length;
	}

	/**
	 * Retrieves whether this {@link FileParser} has completely parse its associated
	 * file.
	 * 
	 * @return <code>boolean</code>
	 */
	public final boolean isFinished() {
		return getIndex() == getLength();
	}

}
