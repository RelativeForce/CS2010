package peril.io;

import java.io.File;

/**
 * Holds the file names and text representation of the files that are used to
 * save and load the {@link Game}.
 * 
 * @author Joshua_Eddy
 *
 */
public enum SaveFile {

	DEFAULT("default.txt", "Default"), ONE("save1.txt", "Save 1"), TWO("save2.txt", "Save 2"), THREE("save3.txt",
			"Save 3");

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
			return new File(mapDirectory + File.separatorChar + this.filename).exists();
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to load directory - " + mapDirectory);
		}
	}

}
