package peril.views.slick.board;

import org.newdawn.slick.Color;

import peril.ai.AI;
import peril.model.ModelPlayer;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Point;

/**
 * The visual representation of a {@link ModelPlayer}.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad, Joseph_Rolli
 * 
 * @since 2018-02-19
 * @version 1.01.02
 * 
 * @see Clickable
 *
 */
public final class SlickPlayer extends Clickable {

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
		super();
		setPosition(new Point(20, 80));
		this.model = new ModelPlayer(number, ai);
		this.color = color;
	}

}
