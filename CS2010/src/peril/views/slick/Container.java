package peril.views.slick;

import peril.views.slick.util.Button;
import peril.views.slick.util.Viewable;

/**
 * Defines the behaviours of a object that can contain {@link Button}s and
 * {@link Viewable}s. {@link Button}s can be retrieved using
 * {@link Container#getButton(String)}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-16
 * @version 1.01.01
 * 
 * @see Viewable
 * @see Button
 *
 */
public interface Container {

	/**
	 * Adds a {@link Button} to this {@link Container}.
	 * 
	 * @param button
	 *            The {@link Button} to be added.
	 */
	void addButton(Button button);

	/**
	 * Adds a {@link Viewable} to this {@link Container}.
	 * 
	 * @param image
	 *            The {@link Viewable} to be added.
	 */
	void addImage(Viewable image);

	/**
	 * Retrieves a uniquely identifying string that denotes this {@link Container}.
	 * 
	 * @return The name of this {@link Container}.
	 */
	String getName();

	/**
	 * Retrieves a {@link Button} by its id.
	 * 
	 * @param id
	 *            The id of a {@link Button} in this {@link Container}.
	 * 
	 * @return The {@link Button} assigned to the specified id.
	 */
	Button getButton(String id);
}
