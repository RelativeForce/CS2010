package peril.views.slick.board;

import org.newdawn.slick.Color;

import peril.ai.AI;
import peril.model.ModelPlayer;
import peril.views.slick.util.Point;
import peril.views.slick.util.Viewable;

/**
 * The visual representation of a {@link ModelPlayer}.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad
 * 
 * @since 2018-02-18
 * @version 1.01.01
 * 
 * @see Viewable
 *
 */
public final class SlickPlayer extends Viewable {

	/**
	 * The {@link Color} of this {@link SlickPlayer}'s {@link SlickCountry}s on the
	 * screen.
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
	 *            The number of this player.
	 * @param color
	 *            The {@link Color} of this {@link SlickPlayer}'s
	 *            {@link SlickCountry}s.
	 * @param ai
	 *            The {@link AI} that controls this {@link SlickPlayer}.
	 */
	public SlickPlayer(int number, Color color, AI ai) {
		super(new Point(15, 45));
		this.model = new ModelPlayer(number, ai);
		this.color = color;
	}

}
