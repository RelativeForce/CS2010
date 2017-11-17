package peril.io.fileParsers;

import java.util.LinkedList;
import java.util.List;

import peril.Challenge;
import peril.Game;
import peril.Player;
import peril.board.Army;
import peril.board.Board;
import peril.board.Continent;
import peril.board.Country;
import peril.io.fileReaders.TextFileReader;

/**
 * Reads the challenges from an external file and uses then constructs the
 * challenges for the {@link Game}. Must be executed after the {@link Board} has
 * be parsed by the {@link MapReader}.
 */
public final class ChallengeReader implements FileParser {

	/**
	 * The lines of the challenges file which specifies all the challenges of the
	 * {@link Player}s on the specified map.
	 */
	private final String[] challengesFile;

	/**
	 * The {@link Challenge}s read from the challenges file.
	 */
	private final List<Challenge> challenges;

	/**
	 * The {@link Game} is using this {@link ChallengeReader}.
	 */
	private final Game game;

	/**
	 * The index of the next line that will be parsed by
	 * {@link FileParser#parseLine()}.
	 */
	private int index;

	/**
	 * Constructs a new {@link ChallengeReader}.
	 * 
	 * @param game
	 *            The {@link Game} is using this {@link ChallengeReader}.
	 * @param directoryPath
	 *            The path of the parent directory which contains the map files.
	 * 
	 */
	public ChallengeReader(Game game, String directoryPath) {

		this.challengesFile = TextFileReader.scanFile(directoryPath, "challenges.txt");
		this.challenges = new LinkedList<>();
		this.game = game;
		this.index = 0;

	}

	/**
	 * Parses a line from a challenges file.
	 * 
	 * @param line
	 *            <code>String</code> line of details.
	 */
	public void parseLine() {

		if (!isFinished()) {

			// Split the line by ','
			String[] details = challengesFile[index].split(",");

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

			index++;

			if (isFinished()) {
				game.setChallenges(challenges);
			}
		}

	}

	/**
	 * Retrieves the index that this {@link ChallengeReader} is in the challenges
	 * file.
	 */
	@Override
	public int getIndex() {
		return index;
	}

	/**
	 * Retrieves the length of the challenges file.
	 */
	@Override
	public int getLength() {
		return challengesFile.length;
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
			int numberOfContinets = (Integer.parseInt(details[1]) * game.board.getNumberOfCountries()) / 10;

			int reward = Integer.parseInt(details[2]);

			challenges.add(new Challenge() {

				@Override
				public boolean hasCompleted(Player player, Board board) {

					if (player.getContinentsRuled() >= numberOfContinets) {
						// Give the player a army of size 5 to distribute.
						player.award(new Army(reward));

						return true;
					}

					return false;

				}

				@Override
				public String toString() {
					return super.toString() + "Own " + numberOfContinets + " Continents.";
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
			int numberOfCountries = (Integer.parseInt(details[1]) * game.board.getNumberOfCountries()) / 10;

			int reward = Integer.parseInt(details[2]);

			challenges.add(new Challenge() {

				@Override
				public boolean hasCompleted(Player player, Board board) {

					if (player.getCountriesRuled() >= numberOfCountries) {
						// Give the player a reward army to distribute.
						player.award(new Army(reward));

						return true;
					}

					return false;
				}

				@Override
				public String toString() {
					return super.toString() + "Own " + numberOfCountries + " Countries.";
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
			int sizeOfArmy = (Integer.parseInt(details[1]) * game.board.getNumberOfCountries()) / 10;

			int reward = Integer.parseInt(details[2]);

			challenges.add(new Challenge() {

				@Override
				public boolean hasCompleted(Player player, Board board) {

					if (player.getTotalArmySize() >= sizeOfArmy) {
						// Give the player a army of size 5 to distribute.
						player.award(new Army(reward));
						return true;
					}

					return false;
				}

				@Override
				public String toString() {

					return super.toString() + "Lead " + sizeOfArmy + " Units.";
				}
			});

		} catch (Exception ex) {
			throw new IllegalArgumentException("details not valid.");
		}

	}

}
