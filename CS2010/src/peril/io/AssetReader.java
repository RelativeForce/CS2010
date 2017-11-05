package peril.io;

import java.io.File;

import org.newdawn.slick.Image;

import peril.Game;
import peril.Player;
import peril.board.Army;
import peril.board.Country;
import peril.multiThread.Action;
import peril.ui.states.CoreGameState;
import peril.ui.visual.Clickable;
import peril.ui.visual.Viewable;

/**
 * Reads all the {@link Viewable} and {@link Clickable} specifed by the ui
 * assets details file and puts them in their specified {@link CoreGameState}s.
 * 
 * @author Joshua_Eddy
 *
 */
public class AssetReader {

	/**
	 * The {@link CoreGameState}s that will be populated when
	 * {@link AssetReader#read()} is performed.
	 */
	private CoreGameState[] coreGameStates;

	/**
	 * File path of the asset details file.
	 */
	private String directoryPath;

	/**
	 * Holds the instance of {@link Game} that was used to construct this
	 * {@link AssetReader}.
	 */
	private Game game;

	/**
	 * Constructs a new {@link AssetReader}.
	 * 
	 * @param coreGameStates
	 *            The {@link CoreGameState}s that will be populated when
	 *            {@link AssetReader#read()} is performed.
	 * @param directoryPath
	 *            File path of the asset details file.
	 */
	public AssetReader(CoreGameState[] coreGameStates, Game game, String directoryPath) {

		// Check params
		if (directoryPath.isEmpty()) {
			throw new NullPointerException("File path cannot be empty.");
		} else if (coreGameStates.length == 0) {
			throw new NullPointerException("CoreGameState array cannot be empty.");
		}

		this.directoryPath = directoryPath;
		this.coreGameStates = coreGameStates;
	}

	/**
	 * Populates the {@link CoreGameState}s stored in
	 * {@link AssetReader#coreGameStates} will {@link Viewable} and
	 * {@link Clickable} from the details file.
	 * 
	 * @see TextFileReader
	 */
	public void read() {

		// Iterate through all the lines in the assets details file.
		for (String line : TextFileReader.scanFile(directoryPath, "assets.txt")) {
			parseLine(line);
		}
	}

	/**
	 * Parses a line of the assets file and selects what action should be taken.
	 * 
	 * @param line
	 *            Line of the assets file to be parsed.
	 */
	private void parseLine(String line) {

		String[] details = line.split(",");

		switch (details[0]) {
		case "button":
			parseButton(details);
			break;
		default:
			// Invalid line - do nothing
			break;
		}

	}

	private void parseButton(String[] details) {

		if (details.length != 5) {
			throw new IllegalArgumentException(
					"The line does not contain the correct number of elements, there should be " + 5 + "");
		}

		int functionCode;
		int stateID;
		Image asset;
		int x;
		int y;

		try {
			stateID = Integer.parseInt(details[1]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[1] + " is not a valid state id");
		}

		CoreGameState state = getGameStateByID(stateID);

		try {
			functionCode = Integer.parseInt(details[2]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[2] + " is not a valid function code");
		}

		Action<?> action = getActionByCode(functionCode, state);

		try {
			asset = ImageReader.getImage(directoryPath + File.separatorChar + details[3]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[3] + " is not a valid path");
		}

	}

	private Action<?> getActionByCode(int code, CoreGameState state) {
		switch (code) {

//		case 0:
//			return new Action<CoreGameState>(state, actionState -> {
//				
//				// Holds the currently highlighted country
//				Country highlightedCountry = actionState.getHighlightedCountry();
//				
//				// Holds the current player.
//				Player player = game.getCurrentPlayer();
//				
//				// Holds the size of the army that the player has to distribute.
//				int armySize = player.getDistributableArmySize();
//
//				// If there is a country highlighted increase its army's size by 1 and
//				// subtract one from the current player's army to distribute.
//				if (highlightedCountry != null && armySize > 0) {
//					
//					Army army = highlightedCountry.getArmy();
//					
//					army.setSize(army.getSize() + 1);
//					
//					player.setDistributableArmySize(armySize - 1);
//				}else {
//					System.out.println("No country selected.");
//				}
//			});

		}
		return null;
	}

	/**
	 * Retrieves a game state with a specified id.
	 * 
	 * @param id
	 *            <code>int</code> id of the {@link CoreGameState}
	 * @return {@link CoreGameState} with the specified id checked using
	 *         {@link CoreGameState#getID()}.
	 */
	private CoreGameState getGameStateByID(int id) {

		// Iterate through all game states in the reader.
		for (CoreGameState state : coreGameStates) {

			// Return the state that has the specified id.
			if (state.getID() == id) {
				return state;
			}
		}

		throw new NullPointerException("ID: " + id + " is not assigned to a game state.");
	}

}
