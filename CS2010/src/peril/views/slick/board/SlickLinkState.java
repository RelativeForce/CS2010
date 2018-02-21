package peril.views.slick.board;

import org.newdawn.slick.Color;
import peril.model.board.links.ModelLinkState;
import peril.views.slick.Frame;
import peril.views.slick.util.Point;

/**
 * The visual representation for a {@link ModelLinkState}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-17
 * @version 1.01.01
 * 
 * @see ModelLinkState
 *
 */
public enum SlickLinkState {

	/**
	 * The visual representation of {@link ModelLinkState#OPEN}.
	 */
	OPEN(ModelLinkState.OPEN) {

		/**
		 * Draw the link as a green line.
		 */
		@Override
		public void draw(Frame frame, Point origin, Point destination) {

			final Color color = frame.getColor();
			final float width = frame.getLineWidth();
			final Point middle = Point.getMiddle(origin, destination);

			frame.setColor(Color.green);
			frame.setLineWidth(3);

			frame.drawLine(origin, middle);

			frame.setLineWidth(width);
			frame.setColor(color);

		}
	},
	/**
	 * The visual representation of {@link ModelLinkState#BLOCKADE}.
	 */
	BLOCKADE(ModelLinkState.BLOCKADE) {

		/**
		 * Draw the link as a red line.
		 */
		@Override
		public void draw(Frame frame, Point origin, Point destination) {

			final Color color = frame.getColor();
			final float width = frame.getLineWidth();
			final Point middle = Point.getMiddle(origin, destination);

			frame.setColor(Color.red);
			frame.setLineWidth(3);

			frame.drawLine(origin, middle);

			frame.setLineWidth(width);
			frame.setColor(color);

		}
	};

	/**
	 * The {@link ModelLinkState} that this {@link SlickLinkState} displays to the
	 * user.
	 */
	public final ModelLinkState model;

	/**
	 * Constructs an new {@link SlickLinkState}.
	 * 
	 * @param model
	 *            The {@link ModelLinkState} that this {@link SlickLinkState}
	 *            displays to the user.
	 */
	private SlickLinkState(ModelLinkState model) {
		this.model = model;
	}

	/**
	 * Draws the {@link SlickLinkState} on screen between the two {@link Point}s.
	 * 
	 * @param frame
	 *            The {@link Frame} the displays the link to the user.
	 * @param origin
	 *            The origin of the link.
	 * @param destination
	 *            The destination of the link.
	 */
	public abstract void draw(Frame frame, Point origin, Point destination);

}
