package peril.views.slick.io;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import peril.Challenge;
import peril.Game;
import peril.ai.AI;
import peril.controllers.GameController;
import peril.helpers.UnitHelper;
import peril.io.FileParser;
import peril.io.SaveFile;
import peril.model.ModelColor;
import peril.model.ModelPlayer;
import peril.model.board.*;
import peril.model.board.links.*;
import peril.views.ModelView;
import peril.views.View;
import peril.views.slick.SlickGame;
import peril.views.slick.SlickModelView;
import peril.views.slick.board.*;
import peril.views.slick.states.InteractiveState;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * Reader the map from a specified {@link SaveFile} and uses that to construct
 * the {@link SlickBoard} and all its {@link SlickCountry}s,
 * {@link SlickContinent}s, {@link SlickUnit}s. In addition of this it restores
 * the state of the game.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-16
 * @version 1.01.01
 * 
 * @see FileParser
 * @see SaveFile
 *
 */
public final class MapReader extends FileParser {

	/**
	 * A constant string used in the map file.
	 */
	private static final String COUNTRY = "Country", UNIT = "Unit", LINK = "Link", CONTINENT = "Continent",
			STATE = "State", PLAYER = "Player", ARMY_STRENGTH = "ArmySize", COUNTRIES_OWNED = "CountriesOwned",
			CONTINENTS_OWNED = "ContinentsOwned";

	/**
	 * The {@link List} of all the {@link SlickContinent}s on the
	 * {@link SlickBoard}.
	 */
	private final Set<ModelContinent> continents;

	/**
	 * The {@link List} of all the {@link SlickCountry}s on the {@link SlickBoard}.
	 */
	private final Map<String, SlickCountry> countries;

	/**
	 * Holds the {@link Game} which contains the {@link SlickBoard} this
	 * {@link MapReader} will populate when {@link MapReader#parseBoard(SlickBoard)}
	 * is performed.
	 */
	private final GameController game;

	/**
	 * The name of the map being loaded.
	 */
	private final String mapName;

	/**
	 * The {@link SlickModelView} that allows the {@link MapReader} to add visual
	 * elements of the game.
	 * 
	 * @see ModelView
	 */
	private final SlickModelView view;

	/**
	 * The slick 2d {@link View} of the game.
	 * 
	 * @see SlickGame
	 */
	private final SlickGame slickGame;

	/**
	 * Holds whether the game has been fully loaded or not.
	 */
	private boolean loadedMap;

	/**
	 * The {@link Image} of the {@link SlickBoard}.
	 */
	private Image normalMap;

	/**
	 * Holds the {@link Image} which denotes the shapes of the
	 * {@link SlickCountry}s.
	 */
	private Image countryMap;

	/**
	 * Constructs a new {@link MapReader}.
	 * 
	 * @param mapName
	 *            The name of the map.
	 * @param game
	 *            The {@link GameController} that allows the {@link MapReader} to
	 *            interact with the game.
	 * @param file
	 *            The {@link SaveFile} that contains the map to be loaded.
	 */
	public MapReader(String mapName, GameController game, SaveFile file) {
		super(game.getDirectory().asMapPath(mapName), game.getDirectory(), file.filename);

		this.continents = new HashSet<>();
		this.countries = new HashMap<>();
		this.game = game;
		this.mapName = mapName;
		this.slickGame = (SlickGame) game.getView();
		this.view = (SlickModelView) game.getView().getModelView();
		this.loadedMap = false;

	}

	/**
	 * Parses a line from a map details file.
	 */
	public void parseLine() {

		if (!isFinished()) {

			if (!loadedMap) {
				loadMapImage();
			} else {
				parseLineType();
			}

			if (isFinished()) {
				// Set the boards continents
				game.getModelBoard().setContinents(continents);
			}
		}

	}

	/**
	 * Discerns the type of element that the current line specifies.
	 */
	private void parseLineType() {

		// Split the line by ','
		String[] details = lines[index].split(",");

		// The first section of the line denotes the type of instruction.
		String type = details[0];

		// Parse the line differently based on the type of instruction.
		switch (type) {
		case COUNTRY:
			parseCountry(details);
			break;
		case UNIT:
			parseUnit(details);
			break;
		case LINK:
			parseLink(details);
			break;
		case CONTINENT:
			parseContinent(details);
			break;
		case STATE:
			parseState(details);
			break;
		case PLAYER:
			parsePlayer(details);
			break;
		case ARMY_STRENGTH:
			parseArmyStrength(details);
			break;
		case COUNTRIES_OWNED:
			parseCountriesOwned(details);
			break;
		case CONTINENTS_OWNED:
			parseContinentsOwned(details);
			break;
		}

		index++;

	}

	/**
	 * Loads the map {@link Image} from memory.
	 */
	private void loadMapImage() {

		final Image tempNormalMap = ImageReader.getImage(directory.asMapPath(mapName) + "normal.png");
		final Image tempCountryMap = ImageReader.getImage(directory.asMapPath(mapName) + "countries.png");

		// float scaleFactor = 1;
		//
		// if (screenWidth < tempNormalMap.getWidth()) {
		// scaleFactor = (float) screenWidth / (float) tempNormalMap.getWidth();
		// } else if (screenHeight < tempNormalMap.getHeight()) {
		// scaleFactor = (float) screenHeight / (float) tempNormalMap.getHeight();
		// }

		// final int newWidth = (int) ((float) tempNormalMap.getWidth() * scaleFactor);
		// final int newHeight = (int) ((float) tempNormalMap.getHeight() *
		// scaleFactor);

		// this.normalMap = ImageReader.resizeImage(tempNormalMap, newWidth, newHeight);
		// this.countryMap = ImageReader.resizeImage(tempCountryMap, newWidth,
		// newHeight);

		this.normalMap = tempNormalMap;
		this.countryMap = tempCountryMap;

		final SlickBoard board = this.view.getVisual(game.getModelBoard());

		if (board != null) {

			// Set the normal map as the visual image of the visual representation.
			board.setPosition(new Point(0, 0));
			board.swapImage(normalMap);
			slickGame.initMiniMap();
		}

		loadedMap = true;

	}

	/**
	 * Parsed a <code>String</code> array into the details into a new
	 * {@link SlickUnit}.
	 * 
	 * @param details
	 *            A <code>String</code> array where:
	 *            <ol>
	 *            <li>The name of the {@link ModelUnit}</li>
	 *            <li>The strength of the {@link ModelUnit}</li>
	 *            <li>The file name of the image that denotes the {@link SlickUnit}s
	 *            image.</li>
	 *            </ol>
	 */
	private void parseUnit(String[] details) {

		// The correct number of elements on a unit line.
		final int UNIT_LENGTH = 4;

		if (details.length != UNIT_LENGTH) {
			throw new IllegalArgumentException(
					"Line " + index + ": Incorrect number of elements, there should be " + UNIT_LENGTH + ".");
		}

		// The name of the unit
		final String name = details[1];

		int strength;

		// Parse the strength value of the unit.
		try {
			strength = Integer.parseInt(details[2]);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Line " + index + ": '" + details[2] + "' is not a valid rgb value.");
		}

		// The file name of the unit image
		final String fileName = details[3];

		// Holds the model unit
		final ModelUnit model = new ModelUnit(name, strength, fileName);

		// The visual image of the unit.
		final Image asset = ImageReader.getImage(directory.getUnitsPath() + fileName).getScaledCopy(SlickUnit.WIDTH,
				SlickUnit.HEIGHT);

		// The slick unit
		final SlickUnit slickUnit = new SlickUnit(model, asset);

		// Add the unit to the model view and unit helper.
		view.addUnit(slickUnit);
		UnitHelper.getInstance().addUnit(model);

	}

	/**
	 * Parses a <code>String</code> array of details into a new
	 * {@link SlickCountry}.
	 * 
	 * @param details
	 *            A <code>String</code> array where:
	 *            <ol>
	 *            <li>The name of the {@link ModelCountry}</li>
	 *            <li>The RGB value of the country {@link Color} on the countries
	 *            {@link Image}</li>
	 *            <li>The army strength</li>
	 *            <li>The x army offset</li>
	 *            <li>The y army offset</li>
	 *            <li>The number of the player that rules the country</li>
	 *            </ol>
	 * 
	 */
	private void parseCountry(String[] details) {

		final int COUNTRY_LENGTH = 7;

		if (details.length != COUNTRY_LENGTH) {
			throw new IllegalArgumentException(
					"Line " + index + ": Incorrect number of elements, there should be " + COUNTRY_LENGTH + ".");
		}

		// The name of the country
		final String name = details[1];

		// Convert the RGB values stored in the RGB string and store them in their own
		// variable.
		int r;
		int g;
		int b;

		// Parse the red RGB value of the counrty's region.
		try {
			r = Integer.parseInt(details[2].substring(0, 3));
		} catch (Exception ex) {
			throw new IllegalArgumentException("Line " + index + ": " + details[2] + " is not a valid rgb value.");
		}

		// Parse the green RGB value of the counrty's region.
		try {
			g = Integer.parseInt(details[2].substring(3, 6));
		} catch (Exception ex) {
			throw new IllegalArgumentException("Line " + index + ": " + details[2] + " is not a valid rgb value.");
		}

		// Parse the blue RGB value of the counrty's region.
		try {
			b = Integer.parseInt(details[2].substring(6, 9));
		} catch (Exception ex) {
			throw new IllegalArgumentException("Line " + index + ": " + details[2] + " is not a valid rgb value.");
		}

		// Check if the rgb values are valid
		if ((r > 255 || r < 0) || (g > 255 || g < 0) || (b > 255 || b < 0)) {
			throw new IllegalArgumentException("Line " + index + ": " + details[2] + " is not a valid rgb value.");
		}

		// Holds the strength of the army
		int armyStrength;

		try {
			armyStrength = Integer.parseInt(details[3]);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + ": " + details[3] + " is not a valid army size.");
		}

		// Holds the army x and y offset.
		int xOffset;
		int yOffset;

		try {
			xOffset = Integer.parseInt(details[4]);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + ": " + details[4] + " is not a valid x coordinate.");
		}

		try {
			yOffset = Integer.parseInt(details[5]);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + ": " + details[5] + " is not a valid y coordinate.");
		}

		// The player that rules the country
		final SlickPlayer slick = parsePlayer(details[6]);

		// The model country
		final ModelCountry model = new ModelCountry(name, new ModelColor(r, g, b));

		// If there is an owner add it to the players list
		if (slick != null) {
			final ModelPlayer ruler = slick.model;

			// Set the ruler
			model.setRuler(ruler);
			ruler.setCountriesRuled(ruler.getCountriesRuled() + 1);

			ModelArmy.generateUnits(armyStrength).forEach(unit -> ruler.totalArmy.add(unit));
		}

		// Gets the region by colour.
		final Region region = new Region(countryMap, new Color(r, g, b));

		// Initialise the new country.
		final SlickCountry country = new SlickCountry(region, new Point(xOffset, yOffset), model, view);

		// Construct the slick army.
		final SlickArmy army = new SlickArmy(country.model.getArmy());

		// Set the army strength
		country.model.getArmy().setStrength(armyStrength);

		// Add the country to the view.
		view.addCountry(country);

		// Add the army to the view.
		view.addArmy(army);

		// Construct a new country and add the country to the list of countries.
		countries.put(name, country);

	}

	/**
	 * Parses a <code>String</code> array of details into a new
	 * {@link SlickContinent}. This method will check {@link MapReader#countries}
	 * for {@link SlickCountry}s that are denoted in the map file.
	 * 
	 * @param details
	 *            A <code>String</code> array where:
	 *            <ol>
	 *            <li>The name of the {@link ModelContinent}</li>
	 *            <li>The hazard that plagues this {@link ModelContinent}</li>
	 *            <li>The names of all the countries in the continent separated by a
	 *            '-'</li>
	 *            </ol>
	 */
	private void parseContinent(String[] details) {

		// The correct number of elements in the details array.
		final int CONTINENT_LENGTH = 4;

		if (details.length != CONTINENT_LENGTH) {
			throw new IllegalArgumentException(
					"Line " + index + ": Incorrect number of elements, there should be " + CONTINENT_LENGTH + ".");
		}

		// Holds the name of the continent.
		final String name = details[1];

		// Holds the hazard the will be assigned to this continent.
		final ModelHazard hazard = ModelHazard.getByName(details[2]);

		// Holds the regions of each country that will be used to make the continent.
		final List<Region> toCombine = new LinkedList<>();

		// Holds countries the that will be added to the continent.
		final List<ModelCountry> toAdd = new LinkedList<>();

		/**
		 * Iterate through all the countries in the countries map and if a country is
		 * denoted by a string in the map detail add it to the new continent.
		 */
		for (String countryName : details[3].split("-")) {

			// Retrieve the country from the map of defined countries.
			final SlickCountry country = countries.get(countryName);

			// Check the country is pre-defined in the file.
			if (country == null) {
				throw new IllegalArgumentException("Line " + index + ": " + countryName + " is not a defined country.");
			}

			toAdd.add(country.model);
			toCombine.add(country.getRegion());

		}

		// The model continent.
		final ModelContinent model = new ModelContinent(hazard, name);

		// The super region consisting of all the country regions combined.
		final Region region = Region.combine(toCombine, normalMap.getWidth(), normalMap.getHeight());

		// Create the new continent.
		final SlickContinent newContinent = new SlickContinent(region, model, view);

		// Add each country to the model continent
		toAdd.forEach(country -> model.addCountry(country));

		// Add the continent to the model view.
		view.addContinent(newContinent);

		// Add the continent to the list of continents.
		continents.add(model);

	}

	/**
	 * Parses a <code>String</code> array of details into link between two
	 * {@link SlickCountry}s. This method will check {@link MapReader#countries} for
	 * {@link SlickCountry}s that are denoted in the map file.
	 * 
	 * @param details
	 *            A <code>String</code> array where:
	 *            <ol>
	 *            <li>The name of first country in the link.</li>
	 *            <li>The name of second country in the link.</li>
	 *            </ol>
	 */
	private void parseLink(String[] details) {

		// The correct number of elements in the details array.
		final int LINK_LENGTH = 6;

		if (details.length != LINK_LENGTH) {
			throw new IllegalArgumentException(
					"Line " + index + ": Incorrect number of elements, there should be " + LINK_LENGTH + ".");
		}

		// Check the country is defined.
		if (countries.get(details[1]) == null) {
			throw new IllegalArgumentException("Line " + index + ": " + details[1] + " is not a defined country.");
		}

		// Holds the first country in the link.
		final ModelCountry country1 = countries.get(details[1]).model;

		// Check the country is defined.
		if (countries.get(details[2]) == null) {
			throw new IllegalArgumentException("Line " + index + ": " + details[2] + " is not a defined country.");
		}

		// Holds the second country in the link.
		final ModelCountry country2 = countries.get(details[2]).model;

		// Holds the default model link state of the model link
		final ModelLinkState defaultLinkState = ModelLinkState.get(details[3]);

		if (defaultLinkState == null) {
			throw new IllegalArgumentException("Line " + index + ": " + details[3] + " is not a model link state.");
		}

		// The link between the two countries.
		final ModelLink link = new ModelLink(defaultLinkState);

		// Holds the default model link state of the model link
		final ModelLinkState currentLinkState = ModelLinkState.get(details[4]);

		if (currentLinkState == null) {
			throw new IllegalArgumentException("Line " + index + ": " + details[4] + " is not a model link state.");
		}

		int duration;

		// Parse the duration of the link's current state.
		try {
			duration = Integer.parseInt(details[5]);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Line " + index + ": " + details[5] + " is not a duration.");
		}
		
		// If the duration is longer than zero rounds.
		if(duration > 0) {
			link.setState(currentLinkState, duration);
		}
		
		country1.addNeighbour(country2, link);

	}

	/**
	 * Parses a {@link SlickPlayer} from a <code>String</code> array of details.
	 * 
	 * @param details
	 *            A <code>String</code> array where:
	 *            <ol>
	 *            <li>The player number</li>
	 *            <li>The players distributable army strength.</li>
	 *            <li>The boolean of whether the player has NOT lost.</li>
	 *            </ol>
	 */
	private void parsePlayer(String[] details) {

		final int STATE_LENGTH = 4;

		// Check there is the correct number of details
		if (details.length != STATE_LENGTH) {
			throw new IllegalArgumentException(
					"Line " + index + ": Incorrect number of elements, there should be " + STATE_LENGTH + ".");
		}

		// Holds the player number
		int playerNumber;

		try {
			playerNumber = Integer.parseInt(details[1]);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + ": " + details[1] + " is not a valid player number.");
		}

		// Holds the strength of the players distrubutable army.
		int armyStrength;

		try {
			armyStrength = Integer.parseInt(details[2]);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + ": " + details[2] + " is not a valid army strength.");
		}

		// Holds whether the player is not a loser.
		boolean notLoser;
		try {
			notLoser = Boolean.parseBoolean(details[3]);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + ": " + details[3] + " is not a valid player state.");
		}

		// Holds the slick player.
		final SlickPlayer player = new SlickPlayer(playerNumber, slickGame.getColor(playerNumber), AI.USER);

		// Set the state of the player
		player.model.distributableArmy.setStrength(armyStrength);
		player.replaceImage(slickGame.getPlayerIcon(playerNumber));

		// Add to the view
		view.addPlayer(player);

		// Add to the game or podium based on loser status.
		if (notLoser) {
			game.addPlayer(player.model);
		} else {
			slickGame.addLoser(player.model);
		}

	}

	/**
	 * Parses a {@link InteractiveState} from a <code>String</code> array of
	 * details.
	 * 
	 * @param details
	 *            A <code>String</code> array where:
	 *            <ol>
	 *            <li>The name of the games state.</li>
	 *            <li>The number of the current player.</li>
	 *            <li>The round number.</li>
	 *            </ol>
	 * 
	 */
	private void parseState(String[] details) {

		int STATE_LENGTH = 4;

		// Check there is the correct number of details
		if (details.length != STATE_LENGTH) {
			throw new IllegalArgumentException(
					"Line " + index + ": Incorrect number of elements, there should be " + STATE_LENGTH + ".");
		}

		// Set the first state as the state read from the game.
		slickGame.states.loadingScreen.setFirstState(slickGame.states.getSaveState(details[1]));

		// Set the current player of the as the player specified by the name.
		game.setCurrentPlayer(parsePlayer(details[2]).model);

		try {
			game.setRoundNumber(Integer.parseInt(details[3]));
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + ": " + details[3] + " is not a valid round number.");
		}

	}

	/**
	 * Parses a <code>String</code> array of details in to an new {@link Challenge}
	 * where the {@link SlickPlayer} requires a number of {@link SlickContinent}s to
	 * get the bonus.
	 * 
	 * @param details
	 *            A <code>String</code> array where:
	 *            <ol>
	 *            <li>The goal of the challenge</li>
	 *            <li>The reward of the challenge</li>
	 *            </ol>
	 */
	private void parseContinentsOwned(String[] details) {

		int CONTINENTS_LENGTH = 3;

		// Check there is the correct number of details
		if (details.length != CONTINENTS_LENGTH) {
			throw new IllegalArgumentException(
					"Line " + index + ": Incorrect number of elements, there should be " + CONTINENTS_LENGTH + ".");
		}

		// Enter try-catch as many parts of this section can throw erroneous exceptions.
		try {

			// Holds the number of continents that the player must own in order to complete
			// this challenge.
			final int numberOfContinets = Integer.parseInt(details[1]);

			final int reward = Integer.parseInt(details[2]);

			game.addChallenge(new Challenge(details[0], numberOfContinets, reward) {

				@Override
				public boolean hasCompleted(ModelPlayer player, ModelBoard board) {

					if (player.getContinentsRuled() >= goal) {
						// Give the player a army to distribute.
						player.distributableArmy.add(ModelArmy.generateUnits(reward));
						return true;
					}

					return false;

				}

				@Override
				public String toString() {
					return "Own " + numberOfContinets + " Continents";
				}

			});

		} catch (Exception ex) {
			throw new IllegalArgumentException("Line " + index + ": Details not valid.");
		}

	}

	/**
	 * Parses a <code>String</code> array of details in to an new {@link Challenge}
	 * where the {@link SlickPlayer} requires a number of {@link SlickCountry}s to
	 * get the bonus.
	 * 
	 * @param details
	 *            A <code>String</code> array where:
	 *            <ol>
	 *            <li>The goal of the challenge</li>
	 *            <li>The reward of the challenge</li>
	 *            </ol>
	 */
	private void parseCountriesOwned(String[] details) {

		int COUNTRIES_LENGTH = 3;

		// Check there is the correct number of details
		if (details.length != COUNTRIES_LENGTH) {
			throw new IllegalArgumentException(
					"Line " + index + ": Incorrect number of elements, there should be " + COUNTRIES_LENGTH + ".");
		}

		// Enter try-catch as many parts of this section can throw erroneous exceptions.
		try {

			// Holds the number of countries that the player must own in order to complete
			// this challenge.
			int numberOfCountries = Integer.parseInt(details[1]);

			int reward = Integer.parseInt(details[2]);

			game.addChallenge(new Challenge(details[0], numberOfCountries, reward) {

				@Override
				public boolean hasCompleted(ModelPlayer player, ModelBoard board) {

					if (player.getCountriesRuled() >= goal) {
						// Give the player a army to distribute.
						player.distributableArmy.add(ModelArmy.generateUnits(reward));

						return true;
					}

					return false;
				}

				@Override
				public String toString() {
					return "Own " + numberOfCountries + " Countries";
				}

			});

		} catch (Exception ex) {
			throw new IllegalArgumentException("Line " + index + ": Details not valid.");
		}

	}

	/**
	 * Parses a <code>String</code> array of details in to an new {@link Challenge}
	 * where the {@link SlickPlayer} requires a combined {@link ModelArmy} of a
	 * certain strength to get the bonus.
	 * 
	 * @param details
	 *            A <code>String</code> array where:
	 *            <ol>
	 *            <li>The goal of the challenge</li>
	 *            <li>The reward of the challenge</li>
	 *            </ol>
	 */
	private void parseArmyStrength(String[] details) {

		int ARMY_STRENGTH_LENGTH = 3;

		// Check there is the correct number of details
		if (details.length != ARMY_STRENGTH_LENGTH) {
			throw new IllegalArgumentException(
					"Line " + index + ": Incorrect number of elements, there should be " + ARMY_STRENGTH_LENGTH + ".");
		}

		// Enter try-catch as many parts of this section can throw erroneous exceptions.
		try {

			// Holds the number of continents that the player must own in order to complete
			// this challenge.
			int sizeOfArmy = Integer.parseInt(details[1]);

			int reward = Integer.parseInt(details[2]);

			game.addChallenge(new Challenge(details[0], sizeOfArmy, reward) {

				@Override
				public boolean hasCompleted(ModelPlayer player, ModelBoard board) {

					if (player.totalArmy.getStrength() >= sizeOfArmy) {

						// Give the player a army to distribute.
						player.distributableArmy.add(ModelArmy.generateUnits(reward));
						return true;
					}

					return false;
				}

				@Override
				public String toString() {

					return "Lead " + sizeOfArmy + " Units";
				}
			});

		} catch (Exception ex) {
			throw new IllegalArgumentException("Line " + index + ": Details not valid.");
		}

	}

	/**
	 * Processes a string player number into a {@link SlickPlayer}.
	 * 
	 * @param player
	 *            String player number
	 * @return {@link SlickPlayer}
	 */
	private SlickPlayer parsePlayer(String player) {

		if (player.equals("-")) {
			return null;
		}

		int playerNumber;

		try {
			playerNumber = Integer.parseInt(player);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + ": " + player + " is not a valid player number.");
		}

		return view.getVisual(game.getModelPlayer(playerNumber));
	}

}
