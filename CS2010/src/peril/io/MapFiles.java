package peril.io;

public enum MapFiles {

	DEFAULT("default.txt"), SAVE1("save1.txt"), SAVE2("save2.txt"), SAVE3("save3.txt");

	public final String filename;

	private MapFiles(String filename) {
		this.filename = filename;
	}

}
