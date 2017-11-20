package peril.io;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import peril.Game;
import peril.board.Continent;
import peril.board.Country;

/**
 * Used for writing the {@link Board} from the {@link Game} into memory.
 * 
 * @author Joshua_Eddy
 *
 */
public class MapWriter {

	/**
	 * Holds all the links that have been written into the file.
	 */
	private Set<String> savedLinks;

	/**
	 * The {@link TextFileWriter} that creates the save file.
	 */
	private TextFileWriter writer;

	/**
	 * The {@link Game} that this {@link MapWriter} is a part of.
	 */
	private Game game;

	/**
	 * Constructs a new {@link MapWriter}.
	 * 
	 * @param game
	 *            The {@link Game} that this {@link MapWriter} is a part of.
	 * @param mapDiretory
	 *            directory of the current map.
	 * @param file
	 *            The {@link MapFiles} that will be written to.
	 */
	public MapWriter(Game game, String mapDiretory, MapFiles file) {
		this.savedLinks = new HashSet<>();
		this.game = game;
		this.writer = new TextFileWriter(mapDiretory + File.separatorChar + file.filename, false);
	}

	/**
	 * Writes the map save file
	 */
	public void write() {

		// Open the file
		writer.open();

		// Write all the countries to the file
		game.board.forEachCountry(country -> writer.writeLine(parseCountry(country)));

		// Write all the continents to the file
		game.board.getContinents().forEach(continent -> writer.writeLine(parseContinent(continent)));

		// Write all the links to the file
		game.board.forEachCountry(country -> parseLinks(country));

		// Save the file
		writer.save();
	}

	/**
	 * Processes all the links between the specified {@link Country} and all its
	 * neighbours.
	 * 
	 * @param country
	 *            {@link Country}
	 */
	private void parseLinks(Country country) {
		country.getNeighbours().forEach(neighbour -> parseLink(country, neighbour));
	}

	/**
	 * Writes a link to the file if it does not already exist in the file.
	 * 
	 * @param country
	 *            {@link Country}
	 * @param neighbour
	 *            {@link Country}
	 */
	private void parseLink(Country country, Country neighbour) {

		StringBuilder line = new StringBuilder();
		StringBuilder potentialDuplicate = new StringBuilder();

		line.append("Link,");
		line.append(country.getName());
		line.append(',');
		line.append(neighbour.getName());

		potentialDuplicate.append("Link,");
		potentialDuplicate.append(neighbour.getName());
		potentialDuplicate.append(',');
		potentialDuplicate.append(country.getName());

		// Check if the link has already been written into the file.
		if (!savedLinks.contains(potentialDuplicate.toString())) {
			String lineStr = line.toString();
			writer.writeLine(lineStr);
			savedLinks.add(lineStr);
		}
	}

	/**
	 * Processes a {@link Continent} into its <code>String</code> representation
	 * that will be used to store the {@link Continent} in the file.
	 * 
	 * @param country
	 *            {@link Continent}
	 * @return <code>String</code>
	 */
	private String parseContinent(Continent continent) {

		StringBuilder line = new StringBuilder();

		line.append("Continent,");

		// Country name
		line.append(continent.getName());
		line.append(',');

		// Hazard name
		line.append(continent.hazard.toString());
		line.append(',');

		// Append countries
		continent.getCountries().forEach(country -> {
			line.append(country.getName());
			line.append('-');
		});

		// Remove the extra dash
		line.deleteCharAt(line.length() - 1);

		return line.toString();
	}

	/**
	 * Processes a {@link Country} into its <code>String</code> representation that
	 * will be used to store the {@link Country} in the file.
	 * 
	 * @param country
	 *            {@link Country}
	 * @return <code>String</code>
	 */
	private String parseCountry(Country country) {

		StringBuilder line = new StringBuilder();

		line.append("Country");
		line.append(',');

		// Country name
		line.append(country.getName());
		line.append(',');

		// Country RGB
		line.append(formatRGB(country.getColor().getRed()));
		line.append(formatRGB(country.getColor().getGreen()));
		line.append(formatRGB(country.getColor().getBlue()));
		line.append(',');

		// Army Size
		line.append(country.getArmy().getSize());
		line.append(',');

		// Army offset
		line.append(Integer.toString(country.getArmy().getOffset().x));
		line.append(',');
		line.append(Integer.toString(country.getArmy().getOffset().y));
		line.append(',');

		// Player ruler
		line.append(country.getRuler() != null ? country.getRuler().toString() : '-');

		return line.toString();
	}

	/**
	 * Converts a int value into a three
	 * 
	 * @param value
	 * @return
	 */
	private String formatRGB(int value) {

		String str = "" + value;

		if (value == 0) {
			return "000";
		}

		int preceedingZeros = 2 - (int) Math.log10((double) value);

		for (int index = 0; index < preceedingZeros; index++) {
			str = "0" + str;
		}

		return str;

	}
}
