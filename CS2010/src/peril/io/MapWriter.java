package peril.io;

import peril.Challenge;
import peril.GameController;
import peril.helpers.UnitHelper;
import peril.model.ModelColor;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelContinent;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.board.links.ModelLink;
import peril.model.states.ModelState;

/**
 * Used for writing the {@link ModelBoard} from the game into memory.
 * 
 * @author Joshua_Eddy
 *
 * @version 1.01.07
 * @since 2018-03-15
 */
public final class MapWriter {

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

		final StringBuilder line = new StringBuilder();
		
		line.append(LineType.UNIT.text);
		line.append(',');

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

		final StringBuilder line = new StringBuilder();
		
		line.append(LineType.STATE.text);
		line.append(',');

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

		final StringBuilder line = new StringBuilder();

		line.append(LineType.PLAYER.text);
		line.append(',');

		line.append(player.number);
		line.append(',');
		
		line.append(player.ai.name);
		line.append(',');

		line.append(player.distributableArmy.getStrength());
		line.append(',');
		
		line.append(player.getCountriesTaken());
		line.append(',');
		
		line.append(player.getUnitsKilled());
		line.append(',');
		
		line.append(player.getPointsSpent());
		line.append(',');

		line.append(isActive);
		line.append(',');

		line.append(player.getPoints());

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

		final StringBuilder line = new StringBuilder();

		line.append(LineType.LINK.text);
		line.append(',');
		line.append(country.getName());
		line.append(',');
		line.append(neighbour.getName());
		line.append(',');

		final ModelLink link = country.getLinkTo(neighbour);

		line.append(link.getDefaultState().name);
		line.append(',');
		line.append(link.getState().name);
		line.append(',');
		line.append(Integer.toString(link.getDuration()));

		writer.writeLine(line.toString());

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

		final StringBuilder line = new StringBuilder();

		line.append(LineType.CONTINENT.text);
		line.append(',');

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

		final StringBuilder line = new StringBuilder();

		line.append(LineType.COUNTRY.text);
		line.append(',');

		// Country name
		line.append(country.getName());
		line.append(',');

		final ModelColor color = country.getColor();

		// Country RGB
		line.append(formatRGB(color.red));
		line.append(formatRGB(color.green));
		line.append(formatRGB(color.blue));
		line.append(',');

		// Army Size
		line.append(parseArmy(country.getArmy()));
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
	 * Processes a {@link ModelArmy} into a string that can be written to the map
	 * file.
	 * 
	 * @param army
	 *            The {@link ModelArmy} to be parsed.
	 * @return String representation of the specified {@link ModelArmy}.
	 */
	private String parseArmy(ModelArmy army) {

		final StringBuilder line = new StringBuilder();
		
		//Whether the current unit is the first.
		boolean firstUnit = true;

		// Iterate over every unit in the army.
		for (ModelUnit unit : army) {

			if (!firstUnit) {
				line.append('-');
			}

			line.append(unit.name);
			line.append(':');
			line.append(army.getNumberOf(unit));

			firstUnit = false;
		}

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
		
		final StringBuilder line = new StringBuilder();

		line.append(challenge.type);
		line.append(',');

		line.append(challenge.goal);
		line.append(',');

		line.append(challenge.reward);

		return line.toString();

	}
}
