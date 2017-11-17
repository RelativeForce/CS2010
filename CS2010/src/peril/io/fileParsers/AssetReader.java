package peril.io.fileParsers;

import java.io.File;

import org.newdawn.slick.Image;

import peril.Game;
import peril.Point;
import peril.io.FunctionHandler;
import peril.io.fileReaders.ImageReader;
import peril.multiThread.Action;
import peril.ui.Button;
import peril.ui.Container;
import peril.ui.components.Clickable;
import peril.ui.components.Viewable;
import peril.ui.states.InteractiveState;

/**
 * Reads all the {@link Viewable} and {@link Clickable} specified by the user
 * interface assets details file and puts them in their specified
 * {@link InteractiveState}s.
 * 
 * @author Joshua_Eddy
 *
 */
public final class AssetReader extends FileParser {

	/**
	 * The {@link CoreGameState}s that will be populated when
	 * {@link AssetReader#read()} is performed.
	 */
	private final Container[] containers;

	/**
	 * Holds the {@link FunctionHandler} that contains the functions that buttons
	 * will execute.
	 */
	private final FunctionHandler functionHandler;

	/**
	 * Constructs a new {@link AssetReader}.
	 * 
	 * @param containers
	 *            The {@link Container}s that will be populated when
	 *            {@link AssetReader#read()} is performed.
	 * @param directoryPath
	 *            File path of the asset details file.
	 * @param game
	 *            The {@link Game} this {@link AssetReader} will be used by.
	 */
	public AssetReader(Container[] containers, String directoryPath, Game game) {
		super(directoryPath, "assets.txt");
		
		// Check params
		if (containers.length == 0) {
			throw new NullPointerException("CoreGameState array cannot be empty.");
		} else if (game == null) {
			throw new NullPointerException("Game cannot be null.");
		}
		this.functionHandler = new FunctionHandler(game);
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

	/**
	 * Parses an {@link Viewable} and adds it to the necessary {@link Container}.
	 * 
	 * @param details
	 *            <code>String[]</code> containing the details that specify the
	 *            {@link Image}.
	 */
	private void parseImage(String[] details) {

		int IMAGE_LENGTH = 7;

		// Check there is the correct number of details
		if (details.length != IMAGE_LENGTH) {
			throw new IllegalArgumentException("Line " + index
					+ " does not contain the correct number of elements, there should be " + IMAGE_LENGTH + "");
		}

		// Holds the scaled image
		Image asset = parseAsset(details[2], details[3], details[4]);

		// Holds the position of the image on the screen.
		Point position = parsePosition(details[5], details[6]);

		// Get the state by name
		Container container = getContainerByName(details[1]);

		// Add the image to the container
		container.addImage(new Viewable(asset, position));

	}

	/**
	 * Parses an <code>String[]</code> into a new {@link Button} which
	 * 
	 * @param details
	 */
	private void parseButton(String[] details) {

		int BUTTON_LENGTH = 9;

		// Check there is the correct number of details
		if (details.length != BUTTON_LENGTH) {
			throw new IllegalArgumentException("Line " + index
					+ " does not contain the correct number of elements, there should be " + BUTTON_LENGTH + "");
		}

		// Holds the container that the button will be added to.
		Container container = getContainerByName(details[1]);

		// Holds the function of the button.
		Action<?> function = parseFunction(details[2]);

		// Holds the image of the button
		Image asset = parseAsset(details[3], details[4], details[5]);

		// Holds the image of the button.
		Point position = parsePosition(details[6], details[7]);
		
		String id = details[8];

		// Add the button to the container
		container.addButton(new Button(position, asset, function, id));

	}

	/**
	 * Parses a {@link Image} from a specified file and dimensions.
	 * 
	 * @param fileName
	 *            The file name of the {@link Image} in the assets folder
	 * @param widthStr
	 *            The desired scaled width of the {@link Image}
	 * @param heightStr
	 *            The desired scaled height of the {@link Image}
	 * @return {@link Image}
	 */
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

		// If the width is a dash then don't scale the width
		if (!widthStr.equals("-")) {
			width = parseDimension(widthStr);
		} else {
			width = 0;
		}

		// If the width is a dash then don't scale the height
		if (!heightStr.equals("-")) {
			height = parseDimension(heightStr);
		} else {
			height = 0;
		}

		// If width and height are zero don't scale the image.
		if (width != 0 && height != 0) {

			// Check both the width an height for the scaled assets
			width = width == 0 ? asset.getWidth() : width;
			height = height == 0 ? asset.getHeight() : height;

			// Scale the assets to the desired dimensions.
			asset = asset.getScaledCopy(width, height);
		}

		return asset;

	}

	/**
	 * Parses a {@link Point} from an x and y.
	 * 
	 * @param xStr
	 *            x position
	 * @param yStr
	 *            y position
	 * @return {@link Point} position on screen.
	 */
	private Point parsePosition(String xStr, String yStr) {

		int x;
		int y;

		// Parse x coordinate.
		try {
			x = Integer.parseInt(xStr);
		} catch (Exception e) {
			throw new IllegalArgumentException(xStr + " is not a valid x coordinate");
		}

		// Parse y coordinate
		try {
			y = Integer.parseInt(yStr);
		} catch (Exception e) {
			throw new IllegalArgumentException(yStr + " is not a valid y coordinate");
		}

		return new Point(x, y);

	}

	/**
	 * Parses a {@link Action} from a specified function code using
	 * {@link FunctionHandler}.
	 * 
	 * @param functionCodeStr
	 * @return {@link Action} associated with a code.
	 */
	private Action<?> parseFunction(String functionCodeStr) {

		int functionCode;

		// Parse the function code
		try {
			functionCode = Integer.parseInt(functionCodeStr);
		} catch (Exception e) {
			throw new IllegalArgumentException(functionCodeStr + " is not a valid function code");
		}

		return functionHandler.get(functionCode);

	}

	/**
	 * Parses a dimension of a scaled {@link Image}.
	 * 
	 * @param dimension
	 * @return
	 */
	private int parseDimension(String dimensionStr) {

		int dimension;

		// Parse the desired dimension
		try {
			dimension = Integer.parseInt(dimensionStr);
		} catch (Exception e) {
			throw new IllegalArgumentException(dimensionStr + " is not a valid scaled dimension.");
		}

		// Check the dimension is valid.
		if (dimension <= 0) {
			throw new IllegalArgumentException("Dimension cannot be <= zero");
		}

		return dimension;
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
