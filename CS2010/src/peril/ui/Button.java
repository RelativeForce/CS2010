package peril.ui;

import org.newdawn.slick.Image;

import peril.Point;
import peril.multiThread.Action;
import peril.ui.components.Clickable;
import peril.ui.components.Region;

/**
 * Encapsulates the behaviour of a button on screen. This button can be clicked
 * using {@link Button#click()}.
 * 
 * @author Joseph_Rolli, Joshua_Eddy, Mohammed_Sayed_Ackbar
 *
 */
public class Button extends Clickable {

	/**
	 * The {@link Action} this {@link Button} will perform when
	 * {@link Button#click()} is performed.
	 * 
	 * @see Action {@link Action}
	 */
	private Action<?> action;

	/**
	 * Constructs a new {@link Button}.
	 * 
	 * @param position
	 *            The {@link Point} position of the {@link Button}.
	 * @param image
	 *            The {@link Image} this {@link Button} will display on screen.
	 * @param action
	 *            The {@link Action} this button will perform when
	 *            {@link Button#click()} is performed.
	 */
	public Button(Point position, Image image, Action<?> action) {
		super();

		// Check the params
		if (position == null) {
			throw new NullPointerException("The position of the button cannot be null.");
		} else if (image == null) {
			throw new NullPointerException("The image of the button cannot be null.");
		} else if (action == null) {
			throw new NullPointerException("The action of this button cannot be null.");
		}

		// Initialise the image and action.
		this.setImage(position, image);
		this.setRegion(new Region(image));
		this.setPosition(position);
		
		this.action = action;
	}

	/**
	 * Performs the {@link Button#action} click in this {@link Button}.
	 */
	public void click() {
		action.execute();
	}
}
