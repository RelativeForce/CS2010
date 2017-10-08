package peril.board;

import peril.ui.Veiwable;
import peril.ui.VisualRepresenation;

/**
 * Encapsulates the behaviour of the the game board in the {@link Game}. This
 * realises {@link Viewable} allowing it to be displayed by the
 * {@link UserInterface}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class Board implements Veiwable {

	
	/**
	 * The visual representation of the {@link Board}.
	 * @author Joshua_Eddy
	 *
	 */
	private class Visual extends VisualRepresenation{
		
	}

	@Override
	public VisualRepresenation getVisual() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
