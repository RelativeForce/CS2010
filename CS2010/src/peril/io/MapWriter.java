package peril.io;

import java.util.HashSet;
import java.util.Set;

import peril.Challenge;
import peril.controllers.GameController;
import peril.helpers.UnitHelper;
import peril.model.ModelColor;
import peril.model.ModelPlayer;
import peril.model.board.ModelContinent;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.states.ModelState;

/**
 * Used for writing the {@link ModelBoard} from the game into memory.
 * 
 * @author Joshua_Eddy
 *
 * @version 1.01.01
 * @since 2018-02-06
 */
public class MapWriter {

	/**
	 * Holds all the links that have been written into the file.
	 */
	private final Set<String> savedLinks;

	/**
	 * The {@link TextFileWriter} that creates the save file.
	 */
	private final TextFileWriter writer;

	/**
	 * The {@link GameController} that this {@link MapWriter} uses to interact with
	 * the game.
	 */
	private final GameController game;

	/**
	 * Constructs a new {@link MapWriter}.
	 * 
	 * @param game
	 *            The {@link GameController} that this {@link MapWriter} uses to
	 *            interact with the game.
	 * @param file
	 *            The {@link SaveFile} that will be written to.
	 */
	public MapWriter(GameController game, SaveFile file) {
		this.savedLinks = new HashSet<>();
		this.game = game;

		// The file path for the map
		final String mapDirectoryPath = game.getDirectory().asMapPath(game.getModelBoard().getName());

		this.writer = new TextFileWriter(mapDirectoryPath + file.filename, false);
	}

	/**
	 * Writes the map save file
	 */
	public void write() {

		// Open the file
		writer.open();

		// Write units into map file.
		UnitHelper.getInstance().forEach(unit -> writer.writeLine(parseUnit(unit)));

		// Write player details for the active players
		game.forEachModelPlayer(player -> writer.writeLine(parsePlayer(player, true)));

		// Write the player details from the losers
		game.forEachLoser(player -> writer.writeLine(parsePlayer(player, false)));

		// Write the state the game will start in
		writer.writeLine(parseGameState());

		// Write all the countries to the file
		game.getModelBoard().forEachCountry(country -> writer.writeLine(parseCountry(country)));

		// Write all the continents to the file
		game.getModelBoard().getContinents().values().forEach(continent -> writer.writeLine(parseContinent(continent)));

		// Write all the links to the file
		game.getModelBoard().forEachCountry(country -> parseLinks(country));

		game.getChallenges().forEach(challenge -> writer.writeLine(parseChallenge(challenge)));

		// Save the file
		writer.save();
	}

	/**
	 * Parses the details of a {@link ModelUnit} into a string that will be strored
	 * in the level file.
	 * 
	 * @param unit
	 *            {@link ModelUnit}
	 * @return String
	 */
	private String parseUnit(ModelUnit unit) {

		StringBuilder line = new StringBuilder();
		line.append("Unit,");

		line.append(unit.name);
		line.append(',');

		line.append(unit.strength);
		line.append(',');

		line.append(unit.fileName);

		return line.toString();
	}

	/**
	 * Parses the details of the game's current state into a <code>String</code>
	 * representation that will be used to stored in the file.
	 * 
	 * @return <code>String</code>
	 */
	private String parseGameState() {

		final ModelState state = game.getCurrentState();

		// If the state is null then the current state is not a model state.
		if (state == null) {
			throw new IllegalStateException("The current state cannot be saved.");
		}

		StringBuilder line = new StringBuilder();
		line.append("State,");

		line.append(state.getName());
		line.append(',');

		line.append(game.getCurrentModelPlayer().number);
		line.append(',');

		line.append(game.getRoundNumber());

		return line.toString();

	}

	/**
	 * Parses the details of a {@link ModelPlayer} its <code>String</code>
	 * representation that will be used to store the {@link ModelPlayer} in the
	 * file.
	 * 
	 * @param player
	 *            {@link ModelPlayer}
	 * @param isActive
	 *            Whether this player is currently playing or has lost.
	 * @return <code>String</code>
	 */
	private String parsePlayer(ModelPlayer player, Boolean isActive) {

		StringBuilder line = new StringBuilder();

		line.append("Player,");

		line.append(player.number);
		line.append(',');

		line.append(player.distributableArmy.getStrength());
		line.append(',');

		line.append(isActive);

		return line.toString();

	}

	/**
	 * Processes all the links between the specified {@link ModelCountry} and all
	 * its neighbours.
	 * 
	 * @param country
	 *            {@link ModelCountry}
	 */
	private void parseLinks(ModelCountry country) {
		country.getNeighbours().forEach(neighbour -> parseLink(country, neighbour));
	}

	/**
	 * Writes a link to the file if it does not already exist in the file.
	 * 
	 * @param country
	 *            {@link ModelCountry}
	 * @param neighbour
	 *            {@link ModelCountry}
	 */
	private void parseLink(ModelCountry country, ModelCountry neighbour) {

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
	 * Processes a {@link ModelContinent} into its <code>String</code>
	 * representation that will be used to store the {@link ModelContinent} in the
	 * file.
	 * 
	 * @param country
	 *            {@link ModelContinent}
	 * @return <code>String</code>
	 */
	private String parseContinent(ModelContinent continent) {

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
	 * Processes a {@link ModelCountry} into its <code>String</code> representation
	 * that will be used to store the {@link ModelCountry} in the file.
	 * 
	 * @param country
	 *            {@link ModelCountry}
	 * @return <code>String</code>
	 */
	private String parseCountry(ModelCountry country) {

		StringBuilder line = new StringBuilder();

		line.append("Country");
		line.append(',');

		// Country name
		line.append(country.getName());
		line.append(',');

		ModelColor color = country.getColor();

		// Country RGB
		line.append(formatRGB(color.red));
		line.append(formatRGB(color.green));
		line.append(formatRGB(color.blue));
		line.append(',');

		// Army Size
		line.append(country.getArmy().getStrength());
		line.append(',');

		// Army offset
		line.append(Integer.toString(game.getView().getArmyOffsetX(country)));
		line.append(',');
		line.append(Integer.toString(game.getView().getArmyOffsetY(country)));
		line.append(',');

		// Player ruler
		line.append(country.getRuler() != null ? country.getRuler().number : '-');

		return line.toString();
	}

	/**
	 * Converts a int value into a three binary bits.
	 * 
	 * @param value
	 *            0 - 255
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

	/**
	 * Processes a {@link Challenge} into its <code>String</code> representation
	 * that will be used to store the {@link Challenge} in the file.
	 * 
	 * @param challlenge
	 *            {@link Challenge}
	 * @return <code>String</code>
	 */
	private String parseChallenge(Challenge challenge) {
		StringBuilder line = new StringBuilder();

		line.append(challenge.type);
		line.append(',');

		line.append(challenge.goal);
		line.append(',');

		line.append(challenge.reward);

		return line.toString();

	}
}
