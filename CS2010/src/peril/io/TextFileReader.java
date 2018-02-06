package peril.io;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Reads a external text file. Use
 * {@link TextFileReader#scanFile(String, String)} to get the lines of the
 * desired text file.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-02-06
 *
 * @see java.io.File
 */
public class TextFileReader {

	/**
	 * The directory that holds all the map {@link File}s.
	 */
	private File[] directory;

	/**
	 * Constructs a new {@link TextFileReader}.
	 * 
	 * @param directoryPath
	 */
	private TextFileReader(String directoryPath) {
		directory = getDirectory(directoryPath);
	}

	/**
	 * Retrieves the array of lines that denote a specified text file in a specified
	 * directory.
	 * 
	 * @param fileName
	 *            The filename of the file.
	 * @param directory
	 *            The path of the parent directory to the file.
	 * @return The lines of the file.
	 */
	public static String[] scanFile(String directoryPath, String filename) {

		// The scanner that has read the text file.
		Scanner scanner = (new TextFileReader(directoryPath)).getScanner(filename);

		// Holds the lines of the text file.
		List<String> lines = new LinkedList<>();

		// If the file was found, otherwise return an empty array.
		if (scanner != null) {

			// Iterate through the scanner and add each line to the list.
			while (scanner.hasNext()) {
				lines.add(scanner.nextLine());
			}

			// Return the list of lines as an array.
			return lines.toArray(new String[lines.size()]);
		} else {
			return new String[0];
		}

	}

	/**
	 * Retrieves the {@link Scanner} that contains the lines of the specified file.
	 * 
	 * @param fileName
	 *            Specified file inside {@link TextFileReader#directory}.
	 * @return {@link Scanner}
	 */
	private Scanner getScanner(String fileName) {

		Scanner scnr = null;

		// Attempt to read the file.
		try {
			scnr = new Scanner(getFile(fileName));
		} catch (Exception e) {
			System.out.println("Failed to find " + fileName);
		}

		return scnr;

	}

	/**
	 * Retrieves a file from a directory.
	 * 
	 * @param fileName
	 *            The filename of the file.
	 * @param directory
	 *            The path of the parent directory to the file.
	 * @return A raw File.
	 */
	private File getFile(String fileName) {

		for (File level : directory) {
			if (level.getPath().contains(fileName)) {

				try {
					return level;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;

	}

	/**
	 * Gets the files in a specified directory.
	 * 
	 * @param directoryPath
	 *            The specified directory.
	 * @return <code>File[]</code> that contains all the files in the specified
	 *         directory directory.
	 */
	private File[] getDirectory(String directoryPath) {
		File[] directory = null;
		try {
			File folder = new File(directoryPath);
			directory = folder.listFiles();
		} catch (Exception e) {
			System.out.println("Failed to load directory");
		}
		return directory;
	}

}
