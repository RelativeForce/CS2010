package peril.io;

import java.io.File;

/**
 * Holds the file names and text representation of the files that are used to
 * save and load the game.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-02-06
 *
 */
public enum SaveFile {

	/**
	 * The default save file.
	 */
	DEFAULT("default.txt", "Default"),
	/**
	 * The first save file.
	 */
	ONE("save1.txt", "Save 1"),
	/**
	 * The second save file.
	 */
	TWO("save2.txt", "Save 2"),
	/**
	 * The third save file.
	 */
	THREE("save3.txt", "Save 3");

	/**
	 * Holds the file name of the {@link SaveFile}.
	 */
	public final String filename;

	/**
	 * The text representation of the {@link SaveFile}.
	 */
	public final String name;

	/**
	 * Constructs a new {@link SaveFile}.
	 * 
	 * @param filename
	 *            Holds the file name of the {@link SaveFile}.
	 * @param name
	 *            The text representation of the {@link SaveFile}.
	 */
	private SaveFile(String filename, String name) {
		this.filename = filename;
		this.name = name;
	}

	/**
	 * Checks if this {@link SaveFile} exists in the specified map directory.
	 * 
	 * @param mapDirectory
	 * @return <code>boolean</code>
	 */
	public boolean existsIn(String mapDirectory) {

		try {
			return new File(mapDirectory + this.filename).exists();
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to load directory - " + mapDirectory);
		}
	}

}
