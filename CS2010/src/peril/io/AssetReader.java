package peril.io;

import java.io.File;

import org.newdawn.slick.Image;

import peril.Game;
import peril.Point;
import peril.multiThread.Action;
import peril.ui.Button;
import peril.ui.ButtonContainer;
import peril.ui.components.Clickable;
import peril.ui.components.Viewable;
import peril.ui.states.InteractiveState;

/**
 * Reads all the {@link Viewable} and {@link Clickable} specified by the ui
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
	private ButtonContainer[] containers;

	/**
	 * File path of the asset details file.
	 */
	private String directoryPath;

	private FunctionHandler functionHandler;

	/**
	 * Constructs a new {@link AssetReader}.
	 * 
	 * @param containers
	 *            The {@link InteractiveState}s that will be populated when
	 *            {@link AssetReader#read()} is performed.
	 * @param directoryPath
	 *            File path of the asset details file.
	 */
	public AssetReader(ButtonContainer[] containers, String directoryPath, Game game) {

		// Check params
		if (directoryPath.isEmpty()) {
			throw new NullPointerException("File path cannot be empty.");
		} else if (containers.length == 0) {
			throw new NullPointerException("CoreGameState array cannot be empty.");
		} else if (game == null) {
			throw new NullPointerException("Game cannot be null.");
		}

		this.functionHandler = new FunctionHandler(game);
		this.directoryPath = directoryPath;
		this.containers = containers;
	}

	/**
	 * Populates the {@link CoreGameState}s stored in {@link AssetReader#containers}
	 * will {@link Viewable} and {@link Clickable} from the details file.
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
		ButtonContainer container = getContainerByName(details[1]);

		// Parse the function code
		try {
			functionCode = Integer.parseInt(details[2]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[2] + " is not a valid function code");
		}

		Action<?> action = functionHandler.get(functionCode);

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
		container.addButton(newButton);

	}

	/**
	 * Retrieves a game state with a specified id.
	 * 
	 * @param name
	 *            {@link CoreGameState#getStateName()}
	 * @return {@link CoreGameState} with the specified name.
	 */
	private ButtonContainer getContainerByName(String name) {

		// Iterate through all game states in the reader.
		for (ButtonContainer container : containers) {

			// Return the state that has the specified name.
			if (container.getName().equals(name)) {
				return container;
			}
		}

		throw new NullPointerException("State: " + name + " is not assigned to a game state.");
	}

}
