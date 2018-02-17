package peril.views.slick.board;

import org.newdawn.slick.Image;

import peril.model.board.ModelUnit;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Region;

/**
 * The visual representation for a {@link ModelUnit}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-17
 * @version 1.01.01
 * 
 * @see ModelUnit
 * @see Clickable
 */
public final class SlickUnit extends Clickable {

	/**
	 * The width of a {@link SlickUnit}s {@link Image}.
	 * 
	 * @see Clickable
	 */
	public static final int WIDTH = 50;

	/**
	 * The width of a {@link SlickUnit}s {@link Image}.
	 * 
	 * @see Clickable
	 */
	public static final int HEIGHT = 50;

	/**
	 * The {@link ModelUnit} this {@link SlickUnit} displays.
	 */
	public final ModelUnit model;

	/**
	 * The filename of the {@link Image} that is displayed to the user.
	 */
	public final String fileName;

	/**
	 * Constructs a new {@link SlickUnit};
	 * 
	 * @param model
	 *            The {@link ModelUnit} this {@link SlickUnit} displays.
	 * @param image
	 *            The {@link Image} that is the visual version of the
	 *            {@link ModelUnit}.
	 */
	public SlickUnit(ModelUnit model, Image image) {
		super(new Region(image), image);
		this.model = model;
		this.fileName = getImage().getResourceReference();
	}

}
