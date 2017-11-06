package peril.io;

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
	 * Constructs a new {@link AssetReader}.
	 * 
	 * @param coreGameStates
	 *            The {@link CoreGameState}s that will be populated when
	 *            {@link AssetReader#read()} is performed.
	 * @param directoryPath
	 *            File path of the asset details file.
	 */
	public AssetReader(CoreGameState[] coreGameStates, String directoryPath) {

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
