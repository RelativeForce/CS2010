package peril.views.slick.io;

import org.newdawn.slick.Image;

import peril.concurrent.Action;
import peril.controllers.GameController;
import peril.helpers.FunctionHelper;
import peril.io.FileParser;
import peril.views.slick.Container;
import peril.views.slick.components.Button;
import peril.views.slick.util.Point;
import peril.views.slick.util.Viewable;

/**
 * Reads all the visual elements in a specified assets details file and adds
 * them in their specified {@link Container}s. Use
 * {@link AssetReader#parseLine()} process one line of the file.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-13
 * @version 1.01.01
 * 
 * @see Container
 * @see Image
 * @see Button
 * @see FunctionHelper
 *
 */
public final class AssetReader extends FileParser {

	/**
	 * The string that denotes whether a asset should be scaled to the size of the
	 * containing window.
	 */
	private static final String SCALE_TO_WINDOW = "w";

	/**
	 * The string that denotes whether a asset should not be scaled.
	 */
	private static final String DONT_SCALE = "-";

	/**
	 * The string that denotes whether a line should be parsed as a {@link Button}.
	 */
	private static final String BUTTON = "button";

	/**
	 * The string that denotes whether a line should be parsed as a
	 * {@link Viewable}.
	 */
	private static final String IMAGE = "image";

	/**
	 * The {@link Container}s that the parsed assets will be placed in.
	 */
	private final Container[] containers;

	/**
	 * Holds the {@link FunctionHelper} that contains the functions that
	 * {@link Button}s will execute.
	 */
	private final FunctionHelper functionHandler;

	/**
	 * The {@link GameController} that allows access to the game.
	 */
	private final GameController game;

	/**
	 * Constructs a new {@link AssetReader}.
	 * 
	 * @param containers
	 *            The {@link Container}s that the parsed assets will be placed in.
	 * @param game
	 *            The {@link GameController} that allows access to the game.
	 * @param filename
	 *            The file name of the assets file.
	 */
	public AssetReader(Container[] containers, String filename, GameController game) {
		super(game.getDirectory().getUIPath(), game.getDirectory(), filename);

		// Check params
		if (containers.length == 0) {
			throw new NullPointerException("Containers array cannot be empty.");
		}

		this.functionHandler = new FunctionHelper(game);
		this.containers = containers;
		this.game = game;
	}

	/**
	 * Parses a line of the assets file and selects what action should be taken.
	 */
	@Override
	public void parseLine() {

		// If there is another line to parse
		if (!isFinished()) {

			// The current line details.
			final String[] details = lines[index].split(",");

			// Distinguish which type of line the current line is.
			switch (details[0]) {
			case BUTTON:
				parseButton(details);
				break;
			case IMAGE:
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
	 *            {@link Viewable}.
	 */
	private void parseImage(String[] details) {

		final int IMAGE_LENGTH = 7;

		// Check there is the correct number of details
		if (details.length != IMAGE_LENGTH) {
			throw new IllegalArgumentException("Line " + index
					+ " does not contain the correct number of elements, there should be " + IMAGE_LENGTH + "");
		}

		// The file path of the image
		final String filePath = directory.getUIPath() + details[2];

		// Holds the scaled image
		final Image asset = parseAsset(filePath, details[3], details[4]);

		// Holds the position of the image on the screen.
		final Point position = parsePosition(details[5], details[6]);

		// Get the state by name
		final Container container = getContainerByName(details[1]);

		// Add the image to the container
		container.addImage(new Viewable(asset, position));

	}

	/**
	 * Parses an <code>String[]</code> into a new {@link Button}.
	 * 
	 * @param details
	 *            <code>String[]</code> containing the details that specify the
	 *            {@link Button}.
	 */
	private void parseButton(String[] details) {

		final int BUTTON_LENGTH = 9;

		// Check there is the correct number of details
		if (details.length != BUTTON_LENGTH) {
			throw new IllegalArgumentException("Line " + index
					+ " does not contain the correct number of elements, there should be " + BUTTON_LENGTH + "");
		}

		// Holds the container that the button will be added to.
		final Container container = getContainerByName(details[1]);

		// Holds the function of the button.
		final Action<?> function = parseFunction(details[2]);

		// The file path of the image
		final String filePath = directory.getButtonsPath() + details[3];

		// Holds the image of the button
		final Image asset = parseAsset(filePath, details[4], details[5]);

		// Holds the image of the button.
		final Point position = parsePosition(details[6], details[7]);

		// The id of the button
		final String id = details[8];

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
	private Image parseAsset(String filePath, String widthStr, String heightStr) {

		Image asset;
		int width;
		int height;

		// Get the asset image
		try {
			asset = ImageReader.getImage(filePath);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + ": " + filePath + " is not a valid path");
		}

		// Scale the width accordingly
		if (widthStr.equals(DONT_SCALE)) {
			width = asset.getWidth();
		} else if (widthStr.equals(SCALE_TO_WINDOW)) {
			width = game.getView().getWindowWidth();
		} else {
			width = parseDimension(widthStr);
		}

		// Scale the height accordingly
		if (heightStr.equals(DONT_SCALE)) {
			height = asset.getHeight();
		} else if (heightStr.equals(SCALE_TO_WINDOW)) {
			height = game.getView().getWindowHeight();
		} else {
			height = parseDimension(heightStr);
		}

		// Return the scaled copy
		return asset.getScaledCopy(width, height);

	}

	/**
	 * Parses a {@link Point} from an x and y.
	 * 
	 * @param xStr
	 *            x position as string
	 * @param yStr
	 *            y position as string
	 * @return {@link Point} position on screen.
	 */
	private Point parsePosition(String xStr, String yStr) {

		int x;
		int y;

		// Parse x coordinate.
		try {
			x = Integer.parseInt(xStr);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + ": " + xStr + " is not a valid x coordinate");
		}

		// Parse y coordinate
		try {
			y = Integer.parseInt(yStr);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + ": " + yStr + " is not a valid y coordinate");
		}

		return new Point(x, y);

	}

	/**
	 * Parses a {@link Action} from a specified function code using
	 * {@link FunctionHelper}.
	 * 
	 * @param functionCodeStr
	 *            function code
	 * @return {@link Action} associated with the function code.
	 */
	private Action<?> parseFunction(String functionCodeStr) {

		int functionCode;

		// Parse the function code
		try {
			functionCode = Integer.parseInt(functionCodeStr);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + ": " + functionCodeStr + " is not a valid function code");
		}

		return functionHandler.get(functionCode);

	}

	/**
	 * Parses a dimension of a {@link Image} from a specified string.
	 * 
	 * @param dimension
	 *            The dimension of the image.
	 * @return
	 */
	private int parseDimension(String dimensionStr) {

		int dimension;

		// Parse the desired dimension
		try {
			dimension = Integer.parseInt(dimensionStr);
		} catch (Exception e) {
			throw new IllegalArgumentException("Line " + index + ": " + dimensionStr + " is not a valid scaled dimension.");
		}

		// Check the dimension is valid.
		if (dimension <= 0) {
			throw new IllegalArgumentException("Line " + index + ": " + "Dimension cannot be <= zero");
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

		throw new NullPointerException("Line " + index + ": " + "State: " + name + " is not assigned to a game state.");
	}

}
