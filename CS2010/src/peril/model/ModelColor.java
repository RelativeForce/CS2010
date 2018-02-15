package peril.model;

/**
 * An 8 bit RGB colour.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-02-11
 *
 */
public final class ModelColor {

	/**
	 * The 3 bit red level of the {@link ModelColor}.
	 */
	public final int red;

	/**
	 * The 3 bit green level of the {@link ModelColor}.
	 */
	public final int green;

	/**
	 * The 3 bit blue level of the {@link ModelColor}.
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
