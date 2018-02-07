package peril.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Encapsulates the behaviour of writing text to a file.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-02-06
 *
 */
public class TextFileWriter {

	/**
	 * The {@link FileWriter} that opens the file.
	 */
	private FileWriter writer;

	/**
	 * The {@link PrintWriter} that prints lines to the file.
	 */
	private PrintWriter printer;

	/**
	 * Holds the path to the file to be written in.
	 */
	private String path;

	/**
	 * Holds whether this {@link TextFileWriter} will append to the target file or
	 * not.
	 */
	private boolean append;

	/**
	 * Constructs a new {@link TextFileWriter}
	 * 
	 * @param path
	 *            Holds the path to the file to be written in.
	 * @param append
	 *            Holds whether this {@link TextFileWriter} will append to the
	 *            target file or not.
	 */
	public TextFileWriter(String path, boolean append) {
		this.path = path;
		this.append = append;
	}

	/**
	 * Opens the {@link TextFileWriter} allowing the target filr to be written into
	 * using {@link TextFileWriter#writeLine(String)}.
	 */
	public void open() {
		try {
			writer = new FileWriter(path, append);
			printer = new PrintWriter(writer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes a line to the file. To change the file and add the specified lines use
	 * {@link TextFileWriter#save()}
	 * 
	 * @param line
	 *            line to write to the file.
	 */
	public void writeLine(String line) {
		printer.println(line);
	}

	/**
	 * Saves the lines added to the target file.
	 */
	public void save() {
		printer.close();
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
