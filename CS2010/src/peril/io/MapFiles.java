package peril.io;

import java.io.File;

public enum MapFiles {

	DEFAULT("default.txt"), SAVE1("save1.txt"), SAVE2("save2.txt"), SAVE3("save3.txt");

	public final String filename;

	private MapFiles(String filename) {
		this.filename = filename;
	}

	public boolean isPresent(String mapDirectory) {

		try {
			return new File(mapDirectory + File.separatorChar + this.filename).exists();
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to load directory - " + mapDirectory);
		}
	}

}
