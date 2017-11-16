package peril.io.fileParsers;

import java.io.File;

import org.newdawn.slick.Image;

import peril.Game;
import peril.Point;
import peril.io.FileParser;
import peril.io.FunctionHandler;
import peril.io.fileReaders.ImageReader;
import peril.io.fileReaders.TextFileReader;
import peril.multiThread.Action;
import peril.ui.Button;
import peril.ui.Container;
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
public class AssetReader implements FileParser {

	/**
	 * The {@link CoreGameState}s that will be populated when
	 * {@link AssetReader#read()} is performed.
	 */
	private Container[] containers;

	/**
	 * File path of the asset details file.
	 */
	private String directoryPath;

	private String[] lines;

	public int index;

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
	public AssetReader(Container[] containers, String directoryPath, Game game) {

		// Check params
		if (directoryPath.isEmpty()) {
			throw new NullPointerException("File path cannot be empty.");
		} else if (containers.length == 0) {
			throw new NullPointerException("CoreGameState array cannot be empty.");
		} else if (game == null) {
			throw new NullPointerException("Game cannot be null.");
		}
		this.lines = TextFileReader.scanFile(directoryPath, "assets.txt");
		this.index = 0;
		this.functionHandler = new FunctionHandler(game);
		this.directoryPath = directoryPath;
		this.containers = containers;
	}

	/**
	 * Parses a line of the assets file and selects what action should be taken.
	 * 
	 * @param line
	 *            Line of the assets file to be parsed.
	 */
	@Override
	public void parseLine() {

		if (index != lines.length) {

			String[] details = lines[index].split(",");

			switch (details[0]) {
			case "button":
				parseButton(details);
				break;
			case "image":
				parseImage(details);
			default:
				// Invalid line - do nothing
				break;
			}
		}

		index++;

	}

	@Override
	public int getIndex() {
		return index;
	}

	public boolean isFinished() {
		return index == lines.length;
	}

	@Override
	public int getLength() {
		return lines.length;
	}

	private void parseImage(String[] details) {

		int IMAGE_LENGTH = 7;

		// Check there is the correct number of details
		if (details.length != IMAGE_LENGTH) {
			throw new IllegalArgumentException("Line " + index
					+ " does not contain the correct number of elements, there should be " + IMAGE_LENGTH + "");
		}

		int x;
		int y;

		Image asset = parseAsset(details[2], details[3], details[4]);

		// Get the state by name
		Container container = getContainerByName(details[1]);

		// Parse x coordinate.
		try {
			x = Integer.parseInt(details[5]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[5] + " is not a valid x coordinate");
		}

		// Parse y coordinate
		try {
			y = Integer.parseInt(details[6]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[6] + " is not a valid y coordinate");
		}

		Point position = new Point(x, y);

		container.addImage(new Viewable(asset, position));

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
			throw new IllegalArgumentException("Line " + index
					+ " does not contain the correct number of elements, there should be " + BUTTON_LENGTH + "");
		}

		int functionCode;
		int x;
		int y;

		// Get the state by name
		Container container = getContainerByName(details[1]);

		// Parse the function code
		try {
			functionCode = Integer.parseInt(details[2]);
		} catch (Exception e) {
			throw new IllegalArgumentException(details[2] + " is not a valid function code");
		}

		Action<?> action = functionHandler.get(functionCode);

		Image asset = parseAsset(details[3], details[4], details[5]);

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

	private Image parseAsset(String fileName, String widthStr, String heightStr) {

		Image asset;
		int width;
		int height;

		// Get the asset image
		try {
			asset = ImageReader.getImage(directoryPath + File.separatorChar + fileName);
		} catch (Exception e) {
			throw new IllegalArgumentException(fileName + " is not a valid name");
		}

		if (!widthStr.equals("-")) {
			width = parseWidth(widthStr);
		} else {
			width = 0;
		}

		if (!heightStr.equals("-")) {
			height = parseHeight(heightStr);
		} else {
			height = 0;
		}

		if (width != 0 && height != 0) {
			// Scale the assets to its desired dimensions.
			asset = asset.getScaledCopy(width, height);
		}

		return asset;

	}

	private int parseWidth(String width) {

		int validWidth;

		// Parse the desired width of the asset
		try {
			validWidth = Integer.parseInt(width);
		} catch (Exception e) {
			throw new IllegalArgumentException(width + " is not a valid width");
		}

		// Check width
		if (validWidth <= 0) {
			throw new IllegalArgumentException("Width cannot be <= zero");
		}

		return validWidth;
	}

	private int parseHeight(String height) {
		int validHeight;

		// Parse the desired height of the asset
		try {
			validHeight = Integer.parseInt(height);
		} catch (Exception e) {
			throw new IllegalArgumentException(height + " is not a valid height");
		}

		// Check height
		if (validHeight <= 0) {
			throw new IllegalArgumentException("Height cannot be <= zero");
		}

		return validHeight;

	}

	/**
	 * Retrieves a game state with a specified id.
	 * 
	 * @param name
	 *            {@link CoreGameState#getStateName()}
	 * @return {@link CoreGameState} with the specified name.
	 */
	private Container getContainerByName(String name) {

		// Iterate through all game states in the reader.
		for (Container container : containers) {

			// Return the state that has the specified name.
			if (container.getName().equals(name)) {
				return container;
			}
		}

		throw new NullPointerException("State: " + name + " is not assigned to a game state.");
	}

}
