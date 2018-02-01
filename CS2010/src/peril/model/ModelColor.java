package peril.model;

/**
 * An 8 bit RGB colour.
 * 
 * @author Joshua_Eddy
 *
 */
public class ModelColor {

	/**
	 * The 3 bit red level.
	 */
	public final int red;

	/**
	 * The 3 bit green level.
	 */
	public final int green;

	/**
	 * The 3 bit blue level.
	 */
	public final int blue;

	/**
	 * Constructs a new {@link ModelColor}.
	 * 
	 * @param red
	 *            The 3 bit red level.
	 * @param green
	 *            The 3 bit green level.
	 * @param blue
	 *            The 3 bit blue level.
	 */
	public ModelColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

}
