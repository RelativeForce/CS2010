package peril.ui;

/**
 * Allow an object to be displayed by the {@link UserInterface}.
 * @author Joshua_Eddy
 *
 */
public interface Viewable {

	/**
	 * Retrieves the visual representation of any object that realises {@link Viewable}.
	 * @return {@link VisualRepresentation}
	 */
	VisualRepresentation getVisual();
	
}
