package peril.io;

import java.awt.Color;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import peril.Challenge;
import peril.Player;
import peril.board.Army;
import peril.board.Board;
import peril.board.Continent;
import peril.board.Country;

/**
 * Reads the challenges from an external file and uses then constructs the
 * challenges for the {@link Game}.
 */
public class ChallengeReader {

	/**
	 * The lines of the challenges file which specifies all the challenges of the
	 * {@link Player}s on the specified map.
	 */
	private String[] challengesFile;

	/**
	 * The {@link Challenge}s read from the challenges file.
	 */
	private List<Challenge> challenges;

	/**
	 * Constructs a new {@link ChallengeReader}.
	 * 
	 * @param directoryPath
	 *            The path of the parent directory which contains the map files.
	 */
	private ChallengeReader(String directoryPath, String mapName) {

		this.challengesFile = TextFileReader.scanFile(directoryPath, mapName + "Challenges");
		challenges = new LinkedList<>();

	}

	/**
	 * Reads the challenges of the current map from the challenges file.
	 * 
	 * @param levelNum
	 *            The number of the level to be loaded.
	 */
	private void parsechallengesFile() {

		// Iterate through all the lines of the challenges file.
		for (String line : challengesFile) {
			parseLine(line);
		}

	}

	/**
	 * Parses a line from a challenges file.
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

	}

	/**
	 * Parses a <code>String</code> array of details in to an new {@link Challenge}
	 * where the {@link Player} requires a number of {@link Continent}s to get the
	 * bonus.
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

			challenges.add(new Challenge() {

				@Override
				public boolean hasCompleted(Player player, Board board) {

					// Hold the number of continents that the player owns.
					int ownedContinents = 0;

					// Iterate through each continent
					for (Continent continent : board.getContinents()) {

						// If the player rules the current continent increment the number of continents
						// the player owns.
						if (continent.getRuler() == player) {
							ownedContinents++;

							// If the player has or exceeds the number required to complete the challenge
							// return true.
							if (ownedContinents >= numberOfContinets) {

								// Give the player a army of size 5 to distribute.
								player.award(new Army(5));

								return true;
							}
						}

					}
					return false;

				}

			});

		} catch (Exception ex) {
			throw new IllegalArgumentException("details not valid.");
		}

	}

	/**
	 * Parses a <code>String</code> array of details in to an new {@link Challenge}
	 * where the {@link Player} requires a number of {@link Country}s to get the
	 * bonus.
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

			challenges.add(new Challenge() {

				@Override
				public boolean hasCompleted(Player player, Board board) {

					// Hold the number of continents that the player owns.
					int ownedCountries = 0;

					// Iterate through each continent
					for (Continent continent : board.getContinents()) {

						// Iterate through all the countries in the continent.
						for (Country country : continent.getCountries()) {

							// If the player rules the current country increment the number of continents
							// the player owns.
							if (country.getRuler() == player) {
								ownedCountries++;

								// If the player has or exceeds the number required to complete the challenge
								// return true.
								if (ownedCountries >= numberOfCountries) {

									// Give the player a army of size 5 to distribute.
									player.award(new Army(20));

									return true;
								}
							}
						}
					}
					return false;
				}
			});

		} catch (Exception ex) {
			throw new IllegalArgumentException("details not valid.");
		}

	}

	/**
	 * Parses a <code>String</code> array of details in to an new {@link Challenge}
	 * where the {@link Player} requires a combined {@link Army} of a certain size
	 * to get the bonus.
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

			challenges.add(new Challenge() {

				@Override
				public boolean hasCompleted(Player player, Board board) {

					// Hold the number of continents that the player owns.
					int currentArmySize = 0;

					// Iterate through each continent
					for (Continent continent : board.getContinents()) {

						// Iterate through all the countries in the continent.
						for (Country country : continent.getCountries()) {

							// If the player rules the current country.
							if (country.getRuler() == player) {

								// Add the size of this country's army to the running total.
								currentArmySize += country.getArmy().getSize();

								// If the player has or exceeds the number required to complete the challenge
								// return true.
								if (currentArmySize >= sizeOfArmy) {

									// Give the player a army of size 5 to distribute.
									player.award(new Army(10));

									return true;
								}
							}
						}
					}
					return false;
				}
			});

		} catch (Exception ex) {
			throw new IllegalArgumentException("details not valid.");
		}

	}

	/**
	 * Retrieves the {@link Challenge}s from a specified file.
	 * 
	 * @param directoryPath
	 *            The <code>string</code> path to the directory containing the file.
	 * @param mapName
	 *            The name of the map.
	 * @return {@link List} of {@link Challenge}sS
	 */
	public static List<Challenge> getChallenges(String directoryPath, String mapName) {

		// Create a reader to read the challenges file.
		ChallengeReader reader = new ChallengeReader(directoryPath, mapName);

		// Parse the contents of the file.
		reader.parsechallengesFile();

		// Return the challenges.
		return reader.challenges;
	}

}
