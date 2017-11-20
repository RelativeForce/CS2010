package peril.io.fileParsers;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.board.Board;
import peril.board.Continent;
import peril.board.Country;
import peril.board.EnvironmentalHazard;
import peril.io.SaveFile;
import peril.io.fileReaders.ImageReader;
import peril.ui.components.Region;

/**
 * Reader the map from a specified file and uses that to construct the
 * {@link Board}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class MapReader extends FileParser {

	/**
	 * The {@link List} of all the {@link Continent}s on the {@link Board}.
	 */
	private final List<Continent> continents;

	/**
	 * The image of the {@link Board}.
	 */
	private final Image normalMap;

	/**
	 * The {@link List} of all the {@link Country}s on the {@link Board}.
	 */
	private final List<Country> countries;

	/**
	 * Holds the {@link Game} which contains the {@link Board} this
	 * {@link MapReader} will populate when {@link MapReader#parseBoard(Board)} is
	 * performed.
	 */
	private final Game game;

	/**
	 * Constructs a new {@link MapReader}.
	 * 
	 * @param directoryPath
	 *            The path of the parent directory which contains the map files.
	 * @param board
	 *            Holds the board this {@link MapReader} will populate when
	 *            {@link MapReader#parseBoard(Board)} is performed using the details
	 *            file from the directory path}.
	 * @param file
	 *            The file that will contain this map.
	 */
	public MapReader(String directoryPath, Game game, SaveFile file) {
		super(directoryPath, file.filename);

		if (game == null) {
			throw new NullPointerException("Game cannot be null.");
		}

		this.continents = new LinkedList<>();
		this.countries = new LinkedList<>();
		this.game = game;

		normalMap = ImageReader.getImage(directoryPath + File.separatorChar + "normal.png");

	}

	/**
	 * Parses a line from a map details file.
	 */
	public void parseLine() {

		if (!isFinished()) {
			// Split the line by ','
			String[] details = lines[index].split(",");

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
			case "State":
				parseState(details);
				break;
			case "Player":
				parsePlayer(details);
				break;
			}

			index++;

			if (isFinished()) {
				// Set the boards continents
				game.board.setContinents(continents);

				// Set the normal map as the visual image of the visual representation.
				game.board.setImage(new Point(0, 0), normalMap);
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

		int BUTTON_LENGTH = 7;

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
		int armySize;

		try {
			armySize = Integer.parseInt(details[3]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[3] + " is not a valid army size.");
		}

		try {
			xOffset = Integer.parseInt(details[4]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[4] + " is not a valid x coordinate.");
		}

		try {
			yOffset = Integer.parseInt(details[5]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[5] + " is not a valid y coordinate.");
		}

		Player ruler = Player.getByName(details[6]);

		// If there is an owner add it to the players list
		if (ruler != null) {
			ruler.setTotalArmySize(ruler.getTotalArmySize() + armySize);
			ruler.setCountriesRuled(ruler.getCountriesRuled() + 1);
		}

		// Initialise a new colour using the RGB values.
		Color color = new Color(r, g, b);

		// Gets the region by colour.
		Region region = ImageReader.getColourRegion(directoryPath + File.separatorChar + "countries.png", color);

		// Initialise the new country.
		Country country = new Country(name, region, color);

		// Set the army size
		country.getArmy().setSize(armySize);

		// Set the army offset.
		country.getArmy().setOffset(new Point(xOffset, yOffset));

		// Set the ruler
		country.setRuler(ruler);

		// Construct a new country and add the country to the list of countries.
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

	private void parsePlayer(String[] details) {

		int STATE_LENGTH = 3;

		// Check there is the correct number of details
		if (details.length != STATE_LENGTH) {
			throw new IllegalArgumentException("Line " + index
					+ " does not contain the correct number of elements, there should be " + STATE_LENGTH + "");
		}

		Player player = Player.getByName(details[1]);

		int armySize;

		try {
			armySize = Integer.parseInt(details[2]);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + " " + details[2] + " is not a valid army size");
		}

		player.setDistributableArmySize(armySize);

		game.players.add(player);

	}

	private void parseState(String[] details) {

		int STATE_LENGTH = 3;

		// Check there is the correct number of details
		if (details.length != STATE_LENGTH) {
			throw new IllegalArgumentException("Line " + index
					+ " does not contain the correct number of elements, there should be " + STATE_LENGTH + "");
		}

		game.states.loadingScreen.setFirstState(game.states.getSaveState(details[1]));
		
		game.players.setCurrent(Player.getByName(details[2]));

	}
}
