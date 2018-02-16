package peril.views.slick;

import org.newdawn.slick.Image;

import peril.concurrent.Action;

/**
 * Encapsulates the behaviour of a button on screen. This button can be clicked
 * using {@link Button#click()}.
 * 
 * @author Joseph_Rolli, Joshua_Eddy, Mohammed_Sayed_Ackbar
 * 
 * @since 2018-02-16
 * @version 1.01.01
 * 
 * @see Action
 * @see Clickable
 *
 */
public final class Button extends Clickable {

	/**
	 * Holds the id of this {@link Button} allowing it to be identified.
	 */
	public final String id;

	/**
	 * The {@link Action} this {@link Button} will perform when
	 * {@link Button#click()} is performed.
	 * 
	 * @see Action
	 */
	private final Action<?> action;

	/**
	 * Holds whether this {@link Button} should be displayed on screen or not.
	 */
	private boolean isVisible;

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
	 * @param id
	 *            The id string that denotes this button.
	 */
	public Button(Point position, Image image, Action<?> action, String id) {
		super(new Region(image), image);

		// Check the params
		if (position == null) {
			throw new NullPointerException("The position of the button cannot be null.");
		} else if (image == null) {
			throw new NullPointerException("The image of the button cannot be null.");
		} else if (action == null) {
			throw new NullPointerException("The action of this button cannot be null.");
		}

		this.id = id;
		this.action = action;
		this.isVisible = true;

		// Assign the position to the button.
		setPosition(position);

	}

	/**
	 * Performs the {@link Action} assigned to this {@link Button}. The
	 * {@link Button} can only been clicked when it {@link Button#isVisible()}.
	 */
	public void click() {
		if (isVisible) {
			action.execute();
		}
	}

	/**
	 * Retrieves the {@link Action} assigned to this {@link Button}.
	 * 
	 * @return {@link Action}.
	 */
	public Action<?> getAction() {
		return action;
	}

	/**
	 * Sets this {@link Button} to hidden.
	 */
	public void hide() {
		isVisible = false;
	}

	/**
	 * Sets this {@link Button} to visible.
	 */
	public void show() {
		isVisible = true;
	}

	/**
	 * Retrieves whether this {@link Button} should be displayed on screen or not.
	 * If this is not visible then {@link Button#click()} will do nothing.
	 * 
	 * @return <code>boolean</code>
	 */
	public boolean isVisible() {
		return isVisible;
	}
}
