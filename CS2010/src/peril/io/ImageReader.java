package peril.io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.newdawn.slick.Image;

import org.newdawn.slick.ImageBuffer;

import peril.board.Region;

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
	private final File file;

	/**
	 * Constructs a new {@link ImageReader}.
	 * 
	 * @param path
	 *            The path of the image this {@link ImageReader} will read.
	 */
	private ImageReader(final String path) {

		file = new File(path);
	}

	/**
	 * Reads the image from the {@link ImageReader#file}.
	 * 
	 * @return {@link BufferedImage}
	 */
	private BufferedImage readImage() {

		// Attempt to read the specified file and if there is an exception thrown return
		// null.
		try {

			return ImageIO.read(file);

		} catch (IOException e) {
			System.out.println("Error: Iamge NOT Found.");
			e.printStackTrace();
			return null;
		}
	}

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
		BufferedImage rawImage = new ImageReader(path).readImage();

		// Holds the width and height of the image.
		int width = rawImage.getWidth();
		int height = rawImage.getHeight();

		// Get the image buffer.
		ImageBuffer imgbuff = new ImageBuffer(width, height);

		// Iterate through the entire image and copy all the pixels into the image
		// buffer.
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				// Get the RGB values from the BufferedImage and put them in the ImageBuffer.
				Color col = new Color(rawImage.getRGB(x, y));
				imgbuff.setRGBA(x, y, col.getRed(), col.getGreen(), col.getBlue(), Color.OPAQUE);
			}
		}

		return imgbuff.getImage();
	}

}