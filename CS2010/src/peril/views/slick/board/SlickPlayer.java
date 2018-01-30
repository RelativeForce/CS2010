package peril.views.slick.board;

import org.newdawn.slick.Color;

import peril.ai.AI;
import peril.model.ModelPlayer;
import peril.views.slick.Point;
import peril.views.slick.Viewable;

/**
 * The internal representation of a user of the system. This object will hold
 * all of the details about a users game such as the number of
 * {@link ModelCountry}s that user has.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad
 *
 */
public final class SlickPlayer extends Viewable {

	/**
	 * The {@link Color} of this {@link SlickPlayer}s overlay on the
	 * {@link UserInterface}.
	 */
	public final Color color;

	/**
	 * The {@link ModelPlayer} that this {@link SlickPlayer} represents.
	 */
	public final ModelPlayer model;

	/**
	 * Constructs a new {@link SlickPlayer}.
	 * 
	 * @param number
	 *            The number of this player
	 * @param color
	 *            The {@link Color} of this {@link SlickPlayer}'s {@link SlickCountry}s.
	 * @param ai
	 *            The {@link AI} that controls this {@link SlickPlayer}.
	 */
	public SlickPlayer(int number, Color color, AI ai) {
		super(new Point(15, 45));
		this.model = new ModelPlayer(number, ai);
		this.color = color;
	}

}
