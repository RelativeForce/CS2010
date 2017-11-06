package peril.io;

import java.io.File;

import org.newdawn.slick.Image;

import peril.Player;
import peril.Point;
import peril.board.Army;
import peril.board.Country;
import peril.multiThread.Action;
import peril.ui.Button;
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

	/**
	 * Parses an <code>String[]</code> into a new {@link Button} which
	 * 
	 * @param details
	 */
	private void parseButton(String[] details) {

		int BUTTON_LENGTH = 8;

		// Check there is the corrent number of details
		if (details.length != BUTTON_LENGTH) {
			throw new IllegalArgumentException(
					"The line does not contain the correct number of elements, there should be " + BUTTON_LENGTH + "");
		}

		int functionCode;
		Image asset;
		int width;
		int height;
		int x;
		int y;

		// Get the state by name
		CoreGameState state = getGameStateByName(details[1]);

		// Parse the function code
		try {
			functionCode = Integer.parseInt(details[2]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[2] + " is not a valid function code");
		}

		Action<?> action = getActionByCode(functionCode, state);

		// Get the asset image
		try {
			asset = ImageReader.getImage(directoryPath + File.separatorChar + details[3]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[3] + " is not a valid name");
		}

		// Parse the desired width of the asset
		try {
			width = Integer.parseInt(details[4]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[4] + " is not a valid width");
		}

		// Check width
		if (width <= 0) {
			throw new IllegalArgumentException("Width cannot be <= zero");
		}

		// Parse the desired height of the asset
		try {
			height = Integer.parseInt(details[5]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[5] + " is not a valid height");
		}

		// Check height
		if (height <= 0) {
			throw new IllegalArgumentException("Height cannot be <= zero");
		}

		// Scale the assets to its desired dimensions.
		asset = asset.getScaledCopy(width, height);

		// Parse x coordinate.
		try {
			x = Integer.parseInt(details[6]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[6] + " is not a valid x coordinate");
		}

		// Parse y coordinate
		try {
			y = Integer.parseInt(details[7]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[7] + " is not a valid y coordinate");
		}

		Point position = new Point(x, y);

		// Construct button
		Button newButton = new Button(position, asset, action);

		// Add the button to the state, cast to clickable so the correct method is used
		state.addElement((Clickable) newButton);

	}

	/**
	 * Retrieves a predefined {@link Action} using a <code>int</code> code.
	 * 
	 * @param code
	 *            <code>int</code> denoting the {@link Action}
	 * @param state
	 *            {@link CoreGameState} that the {@link Action} will be performed
	 *            by.
	 * @return {@link Action}
	 */
	private Action<?> getActionByCode(int code, CoreGameState state) {
		switch (code) {

		case 0:
			return new Action<CoreGameState>(state, actionState -> {

				// Holds the currently highlighted country
				Country highlightedCountry = actionState.getHighlightedCountry();

				// Holds the current player.
				Player player = actionState.getGame().getCurrentPlayer();

				// Holds the size of the army that the player has to distribute.
				int armySize = player.getDistributableArmySize();

				// If there is a country highlighted.
				if (highlightedCountry != null) {

					// If the player has any units to place
					if (armySize > 0) {

						Player ruler = highlightedCountry.getRuler();

						// If the highlighted country has a ruler and it is that player
						if (ruler != null && ruler.equals(player)) {

							// Get that country's army and increase its size by one.
							Army army = highlightedCountry.getArmy();
							army.setSize(army.getSize() + 1);

							// Remove the unit from the list of units to place.
							player.setDistributableArmySize(armySize - 1);

						} else {
							System.out.println(player.toString() + " does not rule this country");
						}

					} else {
						System.out.println("No units to distribute.");
					}

				} else {
					System.out.println("No country selected.");
				}
			});

		// Enter combat state.
		case 1:
			return new Action<CoreGameState>(state, actionState -> {
				actionState.unhighlightCountry(actionState.getHighlightedCountry());
				actionState.getGame().enterState(actionState.getGame().combatState.getID());
			});

		// Enter movement state.
		case 2:
			return new Action<CoreGameState>(state, actionState -> {
				actionState.unhighlightCountry(actionState.getHighlightedCountry());
				actionState.getGame().enterState(actionState.getGame().movementState.getID());
			});

		// Enter reinforcement state.
		case 3:
			return new Action<CoreGameState>(state, actionState -> {
				actionState.unhighlightCountry(actionState.getHighlightedCountry());
				actionState.getGame().enterState(actionState.getGame().reinforcementState.getID());
				actionState.getGame().nextPlayer();
			});
		// Leave set up state
		case 4:
			return new Action<CoreGameState>(state, actionState -> {
				actionState.unhighlightCountry(actionState.getHighlightedCountry());
				actionState.getGame().enterState(actionState.getGame().reinforcementState.getID());
			});

		}
		return null;
	}

	/**
	 * Retrieves a game state with a specified id.
	 * 
	 * @param name
	 *            {@link CoreGameState#getStateName()}
	 * @return {@link CoreGameState} with the specified name.
	 */
	private CoreGameState getGameStateByName(String name) {

		// Iterate through all game states in the reader.
		for (CoreGameState state : coreGameStates) {

			// Return the state that has the specified name.
			if (state.getStateName().equals(name)) {
				return state;
			}
		}

		throw new NullPointerException("State: " + name + " is not assigned to a game state.");
	}

}
