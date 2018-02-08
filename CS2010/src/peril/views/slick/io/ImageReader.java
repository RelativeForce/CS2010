package peril.views.slick.io;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;

import peril.views.slick.Region;
import peril.views.slick.board.SlickBoard;

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
			System.out.println("Error: Image NOT Found.");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves a {@link Region} of the {@link SlickBoard} denoted by a
	 * {@link Color}.
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
	 * @return <code>Integer[][]</code>
	 */
	public static Image getImage(String path) {

		// Holds the raw image retrieved from the image reader.
		return new ImageReader(path).readImage();

	}

	/**
	 * Resizes an image
	 * 
	 * WE DID NOT WRITE THIS
	 * 
	 * Source: <a href="http://slick.ninjacave.com/forum/viewtopic.php?t=6673">Slick
	 * Forums</a>
	 */
	public static Image resizeImage(Image originalImage, int newWidth, int newHeight) {
		
		int type = BufferedImage.TYPE_INT_ARGB;
		BufferedImage originalBufferedImage = toBufferedImage(originalImage, true);
		java.awt.Image image = originalBufferedImage.getScaledInstance(newWidth, newHeight,
				java.awt.Image.SCALE_SMOOTH);
		BufferedImage buffered = new BufferedImage(newWidth, newHeight, type);
		buffered.getGraphics().drawImage(image, 0, 0, null);

		BufferedImage b = new BufferedImage(buffered.getWidth(null), buffered.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		b.createGraphics().drawImage(buffered, 0, 0, null);
		ImageBuffer buf = new ImageBuffer(b.getWidth(), b.getHeight());
		int x, y, rgb;
		for (y = 0; y < b.getHeight(); y++) {
			for (x = 0; x < b.getWidth(); x++) {
				rgb = b.getRGB(x, y);
				buf.setRGBA(x, y, (rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff, (rgb >> 24) & 0xff);
			}
		}
		return buf.getImage();

	}

	private static java.awt.image.BufferedImage toBufferedImage(org.newdawn.slick.Image image, boolean hasAlpha) {
		// conver the image into a byte buffer by reading each pixel in turn
		int len = 4 * image.getWidth() * image.getHeight();
		if (!hasAlpha) {
			len = 3 * image.getWidth() * image.getHeight();
		}

		ByteBuffer out = ByteBuffer.allocate(len);
		org.newdawn.slick.Color c;

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				c = image.getColor(x, y);

				out.put((byte) (c.r * 255.0f));
				out.put((byte) (c.g * 255.0f));
				out.put((byte) (c.b * 255.0f));
				if (hasAlpha) {
					out.put((byte) (c.a * 255.0f));
				}
			}
		}

		// create a raster of the correct format and fill it with our buffer
		DataBuffer dataBuffer = new DataBufferByte(out.array(), len);

		SampleModel sampleModel;

		ColorModel cm;

		if (hasAlpha) {
			int[] offsets = { 0, 1, 2, 3 };
			sampleModel = new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, image.getWidth(), image.getHeight(), 4,
					4 * image.getWidth(), offsets);

			cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 }, true,
					false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
		} else {
			int[] offsets = { 0, 1, 2 };
			sampleModel = new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, image.getWidth(), image.getHeight(), 3,
					3 * image.getWidth(), offsets);

			cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 0 }, false,
					false, ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);
		}
		WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, new java.awt.Point(0, 0));

		// finally create the buffered image based on the data from the texture
		// and spit it through to ImageIO
		return new BufferedImage(cm, raster, false, null);
	}

}