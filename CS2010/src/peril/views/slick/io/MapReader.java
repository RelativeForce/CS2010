package peril.views.slick.io;

import java.io.File;
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
import peril.views.slick.Point;
import peril.views.slick.Region;
import peril.views.slick.SlickGame;
import peril.views.slick.SlickModelView;
import peril.views.slick.board.*;
import peril.views.slick.states.InteractiveState;

/**
 * Reader the map from a specified file and uses that to construct the
 * {@link SlickBoard}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class MapReader extends FileParser {

	/**
	 * The {@link List} of all the {@link SlickContinent}s on the
	 * {@link SlickBoard}.
	 */
	private final Set<ModelContinent> continents;

	/**
	 * The image of the {@link SlickBoard}.
	 */
	private final Image normalMap;

	/**
	 * Holds the {@link Image} which denotes the shapes of the
	 * {@link SlickCountry}s.
	 */
	private final Image countryMap;

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

	private final SlickModelView view;

	private final SlickGame slickGame;

	/**
	 * Constructs a new {@link MapReader}.
	 * 
	 * @param directoryPath
	 *            The path of the parent directory which contains the map files.
	 * @param model
	 *            Holds the board this {@link MapReader} will populate when
	 *            {@link MapReader#parseBoard(SlickBoard)} is performed using the
	 *            details file from the directory path}.
	 * @param file
	 *            The file that will contain this map.
	 */
	public MapReader(String directoryPath, GameController game, SaveFile file) {
		super(directoryPath, file.filename);

		if (game == null) {
			throw new NullPointerException("Game cannot be null.");
		}

		this.continents = new HashSet<>();
		this.countries = new HashMap<>();
		this.game = game;

		this.slickGame = (SlickGame) game.getView();
		this.view = (SlickModelView) game.getView().getModelView();

		this.normalMap = ImageReader.getImage(directoryPath + File.separatorChar + "normal.png");
		this.countryMap = ImageReader.getImage(directoryPath + File.separatorChar + "countries.png");

		final SlickBoard board = this.view.getVisual(game.getModelBoard());

		if (board != null) {
			board.setPosition(new Point(0, 0));
			// Set the normal map as the visual image of the visual representation.
			board.swapImage(normalMap);
		}

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
			case "Unit":
				parseUnit(details);
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
			case "ArmySize":
				parseArmySize(details);
				break;
			case "CountriesOwned":
				parseCountriesOwned(details);
				break;
			case "ContinentsOwned":
				parseContinentsOwned(details);
				break;
			}

			index++;

			if (isFinished()) {
				// Set the boards continents
				game.getModelBoard().setContinents(continents);
			}
		}

	}

	private void parseUnit(String[] details) {

		int UNIT_LENGTH = 4;

		if (details.length != UNIT_LENGTH) {
			throw new IllegalArgumentException(
					"The line does not contain the correct number of elements, there should be " + UNIT_LENGTH + "");
		}

		String name = details[1];

		int strength;

		// Parse the strength value of the unit.
		try {
			strength = Integer.parseInt(details[2]);
		} catch (Exception ex) {
			throw new IllegalArgumentException(details[2] + " is not a valid rgb value.");
		}

		String fileName = details[3];

		ModelUnit model = new ModelUnit(name, strength, fileName);

		Image asset = ImageReader.getImage(game.getUIPath() + fileName).getScaledCopy(SlickUnit.WIDTH, SlickUnit.HEIGHT);

		SlickUnit slickUnit = new SlickUnit(model, asset);

		view.addUnit(slickUnit);

		UnitHelper.getInstance().addUnit(model);

	}

	/**
	 * Parses a <code>String</code> array of details into a new
	 * {@link SlickCountry}.
	 * 
	 * @param details
	 *            The details about the {@link SlickCountry}.
	 */
	private void parseCountry(String[] details) {

		int COUNTRY_LENGTH = 7;

		if (details.length != COUNTRY_LENGTH) {
			throw new IllegalArgumentException(
					"The line does not contain the correct number of elements, there should be " + COUNTRY_LENGTH + "");
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

		SlickPlayer slick = parsePlayer(details[6]);

		ModelCountry model = new ModelCountry(name, new ModelColor(r, g, b));

		// If there is an owner add it to the players list
		if (slick != null) {
			ModelPlayer ruler = slick.model;

			// Set the ruler
			model.setRuler(ruler);
			ruler.setCountriesRuled(ruler.getCountriesRuled() + 1);

			ModelArmy.generateUnits(armySize).forEach(unit -> ruler.totalArmy.add(unit));
		}

		// Gets the region by colour.
		Region region = new Region(countryMap, new Color(r, g, b));

		// Initialise the new country.
		SlickCountry country = new SlickCountry(region, new Point(xOffset, yOffset), model, view);

		SlickArmy army = new SlickArmy(country.model.getArmy());

		// Set the army strength
		country.model.getArmy().setStrength(armySize);

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
	 *            The details about the {@link SlickContinent}.
	 */
	private void parseContinent(String[] details) {

		try {

			// Holds the name of the continent.
			String name = details[1];

			// Holds the hazard the will be assigned to this continent.
			ModelHazard hazard = ModelHazard.getByName(details[2]);

			// Holds the regions of each country that will be used to make the continent.
			List<Region> toCombine = new LinkedList<>();

			// Holds countries the that will be added to the continent.
			List<ModelCountry> toAdd = new LinkedList<>();
			/**
			 * Iterate through all the countries in the countries map and if a country is
			 * denoted by a string in the map detail add it to the new continent.
			 */
			for (String countryName : details[3].split("-")) {

				SlickCountry country = countries.get(countryName);

				toAdd.add(country.model);
				toCombine.add(country.getRegion());

			}

			ModelContinent model = new ModelContinent(hazard, name);

			Region region = Region.combine(toCombine, normalMap.getWidth(), normalMap.getHeight());

			// Create the new continent.
			SlickContinent newContinent = new SlickContinent(region, model, view);

			toAdd.forEach(country -> model.addCountry(country));

			view.addContinent(newContinent);

			// Add the continent to the list of continents.
			continents.add(model);

		} catch (Exception ex) {
			throw new IllegalArgumentException("details not valid.");
		}

	}

	/**
	 * Parses a <code>String</code> array of details into link between two
	 * {@link SlickCountry}s. This method will check {@link MapReader#countries} for
	 * {@link SlickCountry}s that are denoted in the map file.
	 * 
	 * @param details
	 *            The details about the link between two {@link SlickCountry}s.
	 */
	private void parseLink(String[] details) {

		// Holds both the countries if they are in the countries map.
		ModelCountry country1 = countries.get(details[1]).model;
		ModelCountry country2 = countries.get(details[2]).model;

		country1.addNeighbour(country2, new ModelLink(ModelLinkState.OPEN));
		country2.addNeighbour(country1, new ModelLink(ModelLinkState.OPEN));

	}

	/**
	 * Parses a {@link SlickPlayer} from a <code>String</code> array of details.
	 * 
	 * @param details
	 *            <code>String</code> array of details about the
	 *            {@link SlickPlayer}.
	 */
	private void parsePlayer(String[] details) {

		int STATE_LENGTH = 4;

		// Check there is the correct number of details
		if (details.length != STATE_LENGTH) {
			throw new IllegalArgumentException("Line " + index
					+ " does not contain the correct number of elements, there should be " + STATE_LENGTH + "");
		}

		int armyStrength;

		try {
			armyStrength = Integer.parseInt(details[2]);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + " " + details[2] + " is not a valid army size");
		}

		int playerNumber;

		try {
			playerNumber = Integer.parseInt(details[1]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[1] + " is not a valid player number.");
		}

		SlickPlayer player = new SlickPlayer(playerNumber, slickGame.getColor(playerNumber), AI.USER);

		player.model.distributableArmy.setStrength(armyStrength);

		player.replaceImage(slickGame.getPlayerIcon(playerNumber));

		boolean isActive;
		try {
			isActive = Boolean.parseBoolean(details[3]);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + " " + details[2] + " is not a valid army size");
		}

		view.addPlayer(player);

		if (isActive) {
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
	 *            <code>String</code> array of details about the
	 *            {@link InteractiveState}.
	 */
	private void parseState(String[] details) {

		int STATE_LENGTH = 4;

		// Check there is the correct number of details
		if (details.length != STATE_LENGTH) {
			throw new IllegalArgumentException("Line " + index
					+ " does not contain the correct number of elements, there should be " + STATE_LENGTH + "");
		}

		// Set the first state as the state read from the game.
		slickGame.states.loadingScreen.setFirstState(slickGame.states.getSaveState(details[1]));

		// Set the current player of the as the player specified by the name.

		game.setCurrentPlayer(parsePlayer(details[2]).model);

		try {
			game.setRoundNumber(Integer.parseInt(details[3]));

		} catch (Exception e) {
			throw new IllegalArgumentException(details[3] + " is not a valid round number");
		}

	}

	/**
	 * Parses a <code>String</code> array of details in to an new {@link Challenge}
	 * where the {@link SlickPlayer} requires a number of {@link SlickContinent}s to
	 * get the bonus.
	 * 
	 * @param details
	 *            The details about the {@link Challenge}.
	 */
	private void parseContinentsOwned(String[] details) {

		// Enter try-catch as many parts of this section can throw erroneous exceptions.
		try {

			// Holds the number of continents that the player must own in order to complete
			// this challenge.
			int numberOfContinets = Integer.parseInt(details[1]);

			int reward = Integer.parseInt(details[2]);

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
			throw new IllegalArgumentException("details not valid.");
		}

	}

	/**
	 * Parses a <code>String</code> array of details in to an new {@link Challenge}
	 * where the {@link SlickPlayer} requires a number of {@link SlickCountry}s to
	 * get the bonus.
	 * 
	 * @param details
	 *            The details about the {@link Challenge}.
	 */
	private void parseCountriesOwned(String[] details) {

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
			throw new IllegalArgumentException("details not valid.");
		}

	}

	/**
	 * Parses a <code>String</code> array of details in to an new {@link Challenge}
	 * where the {@link SlickPlayer} requires a combined {@link ModelArmy} of a
	 * certain size to get the bonus.
	 * 
	 * @param details
	 *            The details about the {@link Challenge}.
	 */
	private void parseArmySize(String[] details) {

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
			throw new IllegalArgumentException("details not valid.");
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
			throw new IllegalArgumentException(player + " is not a valid player number.");
		}

		return view.getVisual(game.getModelPlayer(playerNumber));
	}
}
