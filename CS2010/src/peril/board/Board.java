package peril.board;

import java.nio.channels.NetworkChannel;

import peril.ui.Viewable;
import peril.ui.VisualRepresentation;

/**
 * Encapsulates the behaviour of the the game board in the {@link Game}. This
 * realises {@link Viewable} allowing it to be displayed by the
 * {@link UserInterface}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class Board implements Viewable {

	/**
	 * Holds the {@link VisualRepresentation} of the {@link Board}.
	 */
	private Visual visual;
	
	/**
	 * Constructs a {@link Board}. 
	 */
	private Board(){
		visual = new Visual();
	}
	
	/**
	 * The visual representation of the {@link Board}.
	 * @author Joshua_Eddy
	 *
	 */
	private class Visual extends VisualRepresentation{
		
	}

	@Override
	public VisualRepresentation getVisual() {
		return visual;
	}
	
}
