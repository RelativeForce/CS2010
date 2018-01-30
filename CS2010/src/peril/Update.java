package peril;

import java.util.Observable;

/**
 * Used by the {@link Observable} model to notify observers. This allows
 * different updates that have the same type to be distinguished.
 * 
 * @author Joshua_Eddy
 *
 */
public class Update {

	/**
	 * The string denoting the property that has been updated.
	 */
	public final String property;

	/**
	 * The new value of the updated property.
	 */
	public final Object newValue;

	/**
	 * Constructs a new {@link Update}.
	 * 
	 * @param property
	 *            The string denoting the property that has been updated.
	 * @param newValue
	 *            The new value of the updated property.
	 */
	public Update(String property, Object newValue) {
		this.property = property;
		this.newValue = newValue;
	}

}
