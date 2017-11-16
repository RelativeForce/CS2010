package peril.io.fileParsers;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import peril.Point;
import peril.board.Board;
import peril.board.Continent;
import peril.board.Country;
import peril.board.EnvironmentalHazard;
import peril.io.FileParser;
import peril.io.fileReaders.ImageReader;
import peril.io.fileReaders.TextFileReader;
import peril.ui.components.Region;

/**
 * Reader the map from a specified file and uses that to construct the
 * {@link Board}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class MapReader implements FileParser {

	/**
	 * The path to the directory with all the map assets in it.
	 */
	private final String directoryPath;

	/**
	 * The {@link List} of all the {@link Continent}s on the {@link Board}.
	 */
	private final List<Continent> continents;

	/**
	 * The lines of the details file which specifies all the details about the map.
	 */
	private final String[] detailsFile;

	/**
	 * The image of the {@link Board}.
	 */
	private final Image normalMap;

	/**
	 * The {@link List} of all the {@link Country}s on the {@link Board}.
	 */
	private final List<Country> countries;

	/**
	 * Holds the board this {@link MapReader} will populate when
	 * {@link MapReader#parseBoard(Board)} is performed.
	 */
	private final Board board;

	/**
	 * The index of the next line that will be parsed by
	 * {@link FileParser#parseLine()}.
	 */
	private int index;

	/**
	 * Constructs a new {@link MapReader}.
	 * 
	 * @param directoryPath
	 *            The path of the parent directory which contains the map files.
	 * @param board
	 *            Holds the board this {@link MapReader} will populate when
	 *            {@link MapReader#parseBoard(Board)} is performed using the details
	 *            file from the directory path}.
	 */
	public MapReader(String directoryPath, Board board) {

		if (directoryPath.isEmpty()) {
			throw new NullPointerException("Directory path cannot be null.");
		} else if (board == null) {
			throw new NullPointerException("Directory path cannot be null.");
		}

		this.directoryPath = directoryPath;
		this.detailsFile = TextFileReader.scanFile(directoryPath, "details.txt");
		this.continents = new LinkedList<>();
		this.countries = new LinkedList<>();
		this.board = board;
		this.index = 0;

		normalMap = ImageReader.getImage(directoryPath + File.separatorChar + "normal.png");

	}

	/**
	 * Parses a line from a map details file.
	 */
	public void parseLine() {

		if (!isFinished()) {
			// Split the line by ','
			String[] details = detailsFile[index].split(",");

			// The first section of the line denotes the type of instruction.
			String type = details[0];

			// Parse the line differently based on the type of instruction.
			switch (type) {
			case "Country":
				parseCountry(details);
				break;
			case "Link":
				parseLink(details);
				break;
			case "Continent":
				parseContinent(details);
				break;
			}

			index++;

			if (isFinished()) {
				// Set the boards continents
				board.setContinents(continents);

				// Set the normal map as the visual image of the visual representation.
				board.setImage(new Point(0, 0), normalMap);
			}
		}

	}

	/**
	 * Parses a <code>String</code> array of details into a new {@link Country}.
	 * 
	 * @param details
	 *            The details about the {@link Country}.
	 */
	private void parseCountry(String[] details) {

		int BUTTON_LENGTH = 5;

		if (details.length != BUTTON_LENGTH) {
			throw new IllegalArgumentException(
					"The line does not contain the correct number of elements, there should be " + BUTTON_LENGTH + "");
		}

		String name = details[1];

		/*
		 * Convert the rgb values stored in the rgb string and store them in their own
		 * variable.
		 */
		int r;
		int g;
		int b;

		// Parse the red rgb value of the counrty's region.
		try {
			r = Integer.parseInt(details[2].substring(0, 3));
		} catch (Exception ex) {
			throw new IllegalArgumentException(details[2] + " is not a valid rgb value.");
		}

		// Parse the green rgb value of the counrty's region.
		try {
			g = Integer.parseInt(details[2].substring(3, 6));
		} catch (Exception ex) {
			throw new IllegalArgumentException(details[2] + " is not a valid rgb value.");
		}

		// Parse the blue rgb value of the counrty's region.
		try {
			b = Integer.parseInt(details[2].substring(6, 9));
		} catch (Exception ex) {
			throw new IllegalArgumentException(details[2] + " is not a valid rgb value.");
		}

		// Check if the rgb values are valid
		if ((r > 255 || r < 0) || (g > 255 || g < 0) || (b > 255 || b < 0)) {
			throw new IllegalArgumentException(details[2] + " is not a valid rgb value.");
		}

		int xOffset;
		int yOffset;

		try {
			xOffset = Integer.parseInt(details[3]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[3] + " is not a valid x coordinate.");
		}

		try {
			yOffset = Integer.parseInt(details[4]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[4] + " is not a valid x coordinate.");
		}

		// Initialise a new color using the rgb values.
		Color color = new Color(r, g, b);

		// Initialise the new country.
		Country country = new Country(name,
				ImageReader.getColourRegion(directoryPath + File.separatorChar + "countries.png", color));

		// Set the army offset.
		country.getArmy().setOffset(new Point(xOffset, yOffset));

		// Construct a new counrty and add the country to the list of countries.
		countries.add(country);

	}

	/**
	 * Parses a <code>String</code> array of details into a new {@link Continent}.
	 * This method will check {@link MapReader#countries} for {@link Country}s that
	 * are denoted in the map file.
	 * 
	 * @param details
	 *            The details about the {@link Continent}.
	 */
	private void parseContinent(String[] details) {

		try {

			// Holds the name of the continent.
			String name = details[1];

			// Holds the hazard the will be assigned to this continent.
			EnvironmentalHazard hazard = EnvironmentalHazard.getByName(details[2]);

			// Holds the regions of each country that will be used to make the continent.
			List<Region> toCombine = new LinkedList<>();

			// Holds countries the that will be added to the continent.
			List<Country> toAdd = new LinkedList<>();
			/**
			 * Iterate through all the countries in the countries map and if a country is
			 * denoted by a string in the map detail add it to the new continent.
			 */
			for (String countryName : details[3].split("-")) {

				// Iterate through all the countries that have been read from the file.
				for (Country country : countries) {

					// If the country's name is specified by the details file to be in this
					// continent.
					if (country.getName().equals(countryName)) {
						toAdd.add(country);
						toCombine.add(country.getRegion());
						break;
					}
				}
			}

			// Create the new continent.
			Continent newContinent = new Continent(hazard, name,
					Region.combine(toCombine, normalMap.getWidth(), normalMap.getHeight()));

			toAdd.forEach(country -> newContinent.addCountry(country));

			// Add the continent to the list of continents.
			continents.add(newContinent);

		} catch (Exception ex) {
			throw new IllegalArgumentException("details not valid.");
		}

	}

	/**
	 * Parses a <code>String</code> array of details into link between two
	 * {@link Country}s. This method will check {@link MapReader#countries} for
	 * {@link Country}s that are denoted in the map file.
	 * 
	 * @param details
	 *            The details about the link between two {@link Country}s.
	 * @param details
	 */
	private void parseLink(String[] details) {

		// Holds the names of both the countries specified by the map file.
		String countryName1 = details[1];
		String countryName2 = details[2];

		// Holds both the countries if they are in the countries map.
		Country country1 = null;
		Country country2 = null;

		/**
		 * Iterate through all the countries in the countries map.
		 */
		for (Country country : countries) {

			// If the current country has the same name as counrtyName1 assign country to
			// country1
			if (country.getName().equals(countryName1)) {
				country1 = country;
			}
			// If the current country has the same name as counrtyName2 assign country to
			// country2
			else if (country.getName().equals(countryName2)) {
				country2 = country;
			}

			// If both countries have been found set them as each other's neighbour and
			// leave the loop.
			if (country1 != null && country2 != null) {
				country1.addNeighbour(country2);
				country2.addNeighbour(country1);
				break;
			}

		}

	}

	/**
	 * Retrieves the index that this {@link MapReader} in the processing of the map
	 * file..
	 */
	@Override
	public int getIndex() {
		return index;
	}

	/**
	 * Retrieves the length of the mpas file.
	 */
	@Override
	public int getLength() {
		return detailsFile.length;
	}

}