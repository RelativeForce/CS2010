package peril.views.slick.io;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import peril.views.slick.util.Region;

/**
 * A function object that can reads an {@link Image} using a specified file
 * path. This object is used as a factory for {@link Image}s.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-17
 * @version 1.01.01
 * 
 * @see Image
 * @see Region
 * @see Color
 *
 */
public class ImageReader {

	/**
	 * The path that denotes the image this {@link ImageReader} will read.
	 */
	private final String path;

	/**
	 * Constructs a new {@link ImageReader}.
	 * 
	 * @param path
	 *            The path of the {@link Image} this {@link ImageReader} will read.
	 */
	private ImageReader(String path) {

		this.path = path;
	}

	/**
	 * Reads the {@link Image} from the image file specified by the path.
	 * 
	 * @return The {@link Image}
	 */
	private Image readImage() {

		// Attempt to read the specified file and if there is an exception thrown return
		// null.
		try {
			return new Image(path);

		} catch (Exception e) {
			System.out.println("Error: Image NOT Found.");
			System.out.println(path);
			return null;
		}
	}

	/**
	 * Retrieves a {@link Region} denoted by a {@link Color}.
	 * 
	 * @param path
	 *            <code>String</code> path of the {@link Image} file.
	 * @param colour
	 *            {@link Color} that denotes this region on the board.
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
	 * @return {@link Image}
	 */
	public static Image getImage(String path) {
		return new ImageReader(path).readImage();

	}
}