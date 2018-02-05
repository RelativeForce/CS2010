package peril.io;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import peril.Challenge;
import peril.Game;
import peril.controllers.GameController;
import peril.helpers.UnitHelper;
import peril.model.ModelColor;
import peril.model.ModelPlayer;
import peril.model.board.ModelUnit;
import peril.views.slick.SlickGame;
import peril.views.slick.board.SlickContinent;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.states.InteractiveState;

/**
 * Used for writing the {@link ModelBoard} from the {@link Game} into memory.
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
	private GameController game;

	/**
	 * Constructs a new {@link MapWriter}.
	 * 
	 * @param game
	 *            The {@link Game} that this {@link MapWriter} is a part of.
	 * @param mapDiretory
	 *            directory of the current map.
	 * @param file
	 *            The {@link SaveFile} that will be written to.
	 */
	public MapWriter(GameController game, SaveFile file) {
		this.savedLinks = new HashSet<>();
		this.game = game;

		// The file path for the map
		final String mapPath = game.getDirectory().getMapsPath() + game.getModelBoard().getName();

		this.writer = new TextFileWriter(mapPath + File.separatorChar + file.filename, false);
	}

	/**
	 * Writes the map save file
	 */
	public void write() {

		// Open the file
		writer.open();

		UnitHelper.getInstance().forEach(unit -> writer.writeLine(parseUnit(unit)));

		// Write player details for the active players
		game.forEachModelPlayer(player -> writer.writeLine(parsePlayer(player, true)));

		// Write the player details from the losers
		game.forEachLoser(player -> writer.writeLine(parsePlayer(player, false)));

		// Write the state the game will start in
		writer.writeLine(parseState(((SlickGame) game.getView()).getCurrentState(), game.getRoundNumber()));

		// Write all the countries to the file
		game.getModelBoard().forEachCountry(country -> writer
				.writeLine(parseCountry((SlickCountry) game.getView().getModelView().getVisual(country))));

		// Write all the continents to the file
		game.getModelBoard().getContinents().values().forEach(continent -> writer
				.writeLine(parseContinent((SlickContinent) game.getView().getModelView().getVisual(continent))));

		// Write all the links to the file
		game.getModelBoard()
				.forEachCountry(country -> parseLinks((SlickCountry) game.getView().getModelView().getVisual(country)));

		game.getChallenges().forEach(challenge -> writer.writeLine(parseChallenge(challenge)));

		// Save the file
		writer.save();
	}

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
	 * Parses the details of a {@link InteractiveState} its <code>String</code>
	 * representation that will be used to store the {@link InteractiveState} in the
	 * file.
	 * 
	 * @param state
	 *            {@link InteractiveState}
	 * @return <code>String</code>
	 */
	private String parseState(InteractiveState state, int roundNumber) {

		StringBuilder line = new StringBuilder();
		line.append("State,");

		line.append(state.getName());
		line.append(',');

		line.append(game.getCurrentModelPlayer().number);
		line.append(',');

		line.append(roundNumber);

		return line.toString();

	}

	/**
	 * Parses the details of a {@link SlickPlayer} its <code>String</code>
	 * representation that will be used to store the {@link SlickPlayer} in the
	 * file.
	 * 
	 * @param player
	 *            {@link SlickPlayer}
	 * @param isActive
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
	 * Processes all the links between the specified {@link SlickCountry} and all
	 * its neighbours.
	 * 
	 * @param country
	 *            {@link SlickCountry}
	 */
	private void parseLinks(SlickCountry country) {
		country.model.getNeighbours().forEach(
				neighbour -> parseLink(country, (SlickCountry) game.getView().getModelView().getVisual(neighbour)));
	}

	/**
	 * Writes a link to the file if it does not already exist in the file.
	 * 
	 * @param country
	 *            {@link SlickCountry}
	 * @param neighbour
	 *            {@link SlickCountry}
	 */
	private void parseLink(SlickCountry country, SlickCountry neighbour) {

		StringBuilder line = new StringBuilder();
		StringBuilder potentialDuplicate = new StringBuilder();

		line.append("Link,");
		line.append(country.model.getName());
		line.append(',');
		line.append(neighbour.model.getName());

		potentialDuplicate.append("Link,");
		potentialDuplicate.append(neighbour.model.getName());
		potentialDuplicate.append(',');
		potentialDuplicate.append(country.model.getName());

		// Check if the link has already been written into the file.
		if (!savedLinks.contains(potentialDuplicate.toString())) {
			String lineStr = line.toString();
			writer.writeLine(lineStr);
			savedLinks.add(lineStr);
		}
	}

	/**
	 * Processes a {@link SlickContinent} into its <code>String</code>
	 * representation that will be used to store the {@link SlickContinent} in the
	 * file.
	 * 
	 * @param country
	 *            {@link SlickContinent}
	 * @return <code>String</code>
	 */
	private String parseContinent(SlickContinent continent) {

		StringBuilder line = new StringBuilder();

		line.append("Continent,");

		// Country name
		line.append(continent.model.getName());
		line.append(',');

		// Hazard name
		line.append(continent.model.hazard.toString());
		line.append(',');

		// Append countries
		continent.model.getCountries().forEach(country -> {
			line.append(country.getName());
			line.append('-');
		});

		// Remove the extra dash
		line.deleteCharAt(line.length() - 1);

		return line.toString();
	}

	/**
	 * Processes a {@link SlickCountry} into its <code>String</code> representation
	 * that will be used to store the {@link SlickCountry} in the file.
	 * 
	 * @param country
	 *            {@link SlickCountry}
	 * @return <code>String</code>
	 */
	private String parseCountry(SlickCountry country) {

		StringBuilder line = new StringBuilder();

		line.append("Country");
		line.append(',');

		// Country name
		line.append(country.model.getName());
		line.append(',');

		ModelColor color = country.model.getColor();

		// Country RGB
		line.append(formatRGB(color.red));
		line.append(formatRGB(color.green));
		line.append(formatRGB(color.blue));
		line.append(',');

		// Army Size
		line.append(country.model.getArmy().getStrength());
		line.append(',');

		// Army offset
		line.append(Integer.toString(country.getArmyOffset().x));
		line.append(',');
		line.append(Integer.toString(country.getArmyOffset().y));
		line.append(',');

		// Player ruler
		line.append(country.model.getRuler() != null ? country.model.getRuler().number : '-');

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
