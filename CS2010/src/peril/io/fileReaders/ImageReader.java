package peril.io.fileReaders;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import peril.board.Board;
import peril.ui.components.Region;

/**
 * Reads an image using a specified file path.
 * 
 * @author Joshua_Eddy
 *
 */
public class ImageReader {

	/**
	 * The {@link File} that denotes the image this {@link ImageReader} will read.
	 */
	private final String path;

	/**
	 * Constructs a new {@link ImageReader}.
	 * 
	 * @param path
	 *            The path of the image this {@link ImageReader} will read.
	 */
	private ImageReader(final String path) {

		this.path = path;
	}

	/**
	 * Reads the image from the {@link ImageReader#file}.
	 * 
	 * @return {@link BufferedImage}
	 */
	private Image readImage() {

		// Attempt to read the specified file and if there is an exception thrown return
		// null.
		try {

			return new Image(path);

		} catch (Exception e) {
			System.out.println("Error: Iamge NOT Found.");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves a {@link Region} of the {@link Board} denoted by a {@link Color}.
	 * @param path <code>String</code> path of the {@link Image} file.
	 * @param colour {@link Color} that denotes this region on the board.
	 * @return {@link Region}.
	 */
	public static Region getColourRegion(String path, Color colour) {
		return new Region(new ImageReader(path).readImage(), colour);
	}

	/**
	 * Retrieves an array of RGB values that represents the image specified by the
	 * path.
	 * 
	 * @param path
	 *            The file path of an image.
	 * @return <code>Integer[][]</code>
	 */
	public static Image getImage(String path) {
		
		// Holds the raw image retrieved from the image reader.
		return new ImageReader(path).readImage();

		
	}

}