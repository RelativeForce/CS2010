package peril.views.slick;

/**
 * Defines the behaviours of a object that can contain {@link Button}s and
 * {@link Viewable}s. In the case of {@link Buttons} this also defines the
 * behaviour of retrieving unique object defining {@link String} and processes a
 * click on the container.
 * 
 * @author Joshua_Eddy
 *
 */
public interface Container {

	/**
	 * Adds a {@link Button} to this {@link Container}.
	 * 
	 * @param button
	 *            {@link Button}
	 */
	void addButton(Button button);

	/**
	 * Adds a {@link Viewable} to this {@link Container}.
	 * 
	 * @param image
	 *            {@link Viewable}
	 */
	void addImage(Viewable image);

	/**
	 * Retrieves a uniquely identifying string that denotes this {@link Container}.
	 * 
	 * @return name of this {@link Container}.
	 */
	String getName();

	/**
	 * Retrieves a {@link Button} by its id.
	 * 
	 * @return {@link Button}
	 */
	Button getButton(String id);
}
