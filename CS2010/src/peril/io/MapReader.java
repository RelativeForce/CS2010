package peril.io;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import peril.board.Board;
import peril.board.Continent;
import peril.board.Country;
import peril.board.EnvironmentalHazard;
import peril.board.Region;

/**
 * Reader the map from a specified file and uses that to construct the
 * {@link Board}.
 * 
 * @author Joshua_Eddy
 *
 */
public class MapReader {

	/**
	 * The lines of the details file which specifies all the details about the map.
	 */
	private String[] detailsFile;

	/**
	 * The name of the current map, this will be used to locate all the map resources.
	 */
	private String mapName;
	
	/**
	 * The path to the directory with all the map assets in it.
	 */
	private String directoryPath;

	/**
	 * The {@link List} of all the {@link Country}s on the {@link Board}. 
	 */
	private List<Country> countries;

	/**
	 *  The {@link List} of all the {@link Continent}s on the {@link Board}.
	 */
	private List<Continent> continents;

	/**
	 * The image of the {@link Board}.
	 */
	private Integer[][] normalMap;

	/**
	 * Constructs a new {@link MapReader} object.
	 * 
	 * @param directoryPath
	 *            The path of the parent directory which contains the levels
	 */
	private MapReader(String directoryPath, String mapName) {
		
		this.mapName = mapName;
		this.directoryPath = directoryPath;
		this.detailsFile = TextFileReader.scanFile(directoryPath, mapName + "Details");
		this.normalMap = ImageReader.getImage(directoryPath + File.separatorChar + mapName +".png");
		this.countries = new LinkedList<>();
		this.continents = new LinkedList<>();

	}

	/**
	 * Retrieves the {@link Board} specified by a given map name.
	 * @param directoryPath
	 * @param mapName
	 */
	public static void getBoard(String directoryPath, String mapName) {

		MapReader reader = new MapReader(directoryPath, mapName);
		reader.parseDetailsFile();
		
		Board newBoard = new Board();
		
		// TODO: Add the readers fields to the new Board.

	}

	/**
	 * Reads the details of the current level from the level file.
	 * 
	 * @param levelNum
	 *            The number of the level to be loaded.
	 */
	private void parseDetailsFile() {

		// Iterate through all the lines in the details file.
		for (String line : detailsFile) {
			parseLine(line);
		}
	}

	/**
	 * Parses a line from a map details file.
	 * 
	 * @param line
	 *            <code>String</code> line of details.
	 */
	private void parseLine(String line) {

		// Split the line by ','
		String[] details = line.split(",");

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

	}

	/**
	 * Parses a <code>String</code> array of details into a new {@link Country}.
	 * 
	 * @param details
	 *            The details about the {@link Country}.
	 */
	private void parseCountry(String[] details) {

		// Enter try-catch as many parts of this section can throw erroneous exceptions.
		try {

			// Holds the name of the country
			String name = details[1];

			// Holds the rgb value of the counrty's region.
			String rgb = details[2];

			/*
			 * Convert the rgb values stored in the rgb string and store them in their own
			 * variable.
			 */
			int r = Integer.parseInt(rgb.substring(0, 3));
			int g = Integer.parseInt(rgb.substring(3, 6));
			int b = Integer.parseInt(rgb.substring(6, 9));

			// Initialise a new color using the rgb values.
			Color color = new Color(r, g, b);

			// Initialise the new country.
			Country country = new Country(name);
			
			// Set the clickable region of the country
			country.setRegion(ImageReader.getColourRegion(directoryPath + File.separatorChar + mapName +"Countries.png", color));
			
			// Construct a new counrty and add the country to the list of countries.
			countries.add(country);

		} catch (Exception ex) {
			throw new IllegalArgumentException("details not valid.");
		}

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
			EnvironmentalHazard hazard = null;

			/*
			 * Iterate through all the hazards in the game and if the hazard specified in
			 * the map file details is the same as one from the game set the hazard of the
			 * new continent as that. Otherwise there will be NO hazard in the new
			 * continent.
			 */
			for (EnvironmentalHazard indexHazard : EnvironmentalHazard.values()) {
				if (indexHazard.toString().equals(details[2])) {
					hazard = indexHazard;
					break;
				}
			}

			// Create the new continent.
			Continent newContinent = new Continent(hazard, name);

			/**
			 * Iterate through all the countries in the countries map and if a country is
			 * denoted by a string in the map detail add it to the new continent.
			 */
			for (String countryName : details[3].split("-")) {
				for (Country country : countries) {
					if (country.toString().equals(countryName)) {
						newContinent.addCountry(country);
						break;
					}
				}
			}

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

			// If both countries have been found set them as each other's neighbour and
			// leave the loop.
			if (country1 != null && country2 != null) {
				country1.addNeighbour(country2);
				country2.addNeighbour(country1);
				break;
			}

			// If the current country has the same name as counrtyName1 assign counrty to
			// country1
			if (country.toString().equals(countryName1)) {
				country1 = country;
			}
			// If the current country has the same name as counrtyName2 assign counrty to
			// country2
			else if (country.toString().equals(countryName2)) {
				country2 = country;
			}

		}

	}

}
