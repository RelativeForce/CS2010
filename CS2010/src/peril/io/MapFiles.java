package peril.io;

import java.io.File;

/**
 * Holds the file names and text representation of the files that are used to
 * save and load the {@link Game}.
 * 
 * @author Joshua_Eddy
 *
 */
public enum MapFiles {

	DEFAULT("default.txt", "Default"), SAVE1("save1.txt", "Save 1"), SAVE2("save2.txt", "Save 2"), SAVE3("save3.txt",
			"Save 2");

	/**
	 * Holds the file name of the {@link MapFiles}.
	 */
	public final String filename;

	/**
	 * The text representation of the {@link MapFiles}.
	 */
	public final String name;

	/**
	 * Constructs a new {@link MapFiles}.
	 * 
	 * @param filename
	 *            Holds the file name of the {@link MapFiles}.
	 * @param name
	 *            The text representation of the {@link MapFiles}.
	 */
	private MapFiles(String filename, String name) {
		this.filename = filename;
		this.name = name;
	}

	/**
	 * Checks if this {@link MapFiles} exists in the specified map directory.
	 * 
	 * @param mapDirectory
	 * @return <code>boolean</code>
	 */
	public boolean existsIn(String mapDirectory) {

		try {
			return new File(mapDirectory + File.separatorChar + this.filename).exists();
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to load directory - " + mapDirectory);
		}
	}

}
