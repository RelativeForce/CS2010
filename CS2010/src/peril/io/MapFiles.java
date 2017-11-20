package peril.io;

import java.io.File;

public enum MapFiles {

	DEFAULT("default.txt", "Default"), SAVE1("save1.txt", "Save 1"), SAVE2("save2.txt", "Save 2"), SAVE3("save3.txt", "Save 2");

	public final String filename;
	
	public final String name;

	private MapFiles(String filename, String name) {
		this.filename = filename;
		this.name= name;
	}

	public boolean existsIn(String mapDirectory) {

		try {
			return new File(mapDirectory + File.separatorChar + this.filename).exists();
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to load directory - " + mapDirectory);
		}
	}

}
