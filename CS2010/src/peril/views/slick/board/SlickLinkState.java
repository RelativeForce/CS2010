package peril.views.slick.board;

import org.newdawn.slick.Color;
import peril.model.board.links.ModelLinkState;
import peril.views.slick.Frame;
import peril.views.slick.util.Point;

public enum SlickLinkState {

	OPEN(ModelLinkState.OPEN) {

		@Override
		public void draw(Frame frame, Point a, Point b) {

			final Color temp = frame.getColor();
			final float line = frame.getLineWidth();

			frame.setColor(Color.green);
			frame.setLineWidth(3);

			final Point middle = Point.getMiddle(a, b);
			
			frame.drawLine(a, middle);
			

			frame.setLineWidth(line);
			frame.setColor(temp);
			
		}
	},BLOCKADE(ModelLinkState.BLOCKADE) {
		
		@Override
		public void draw(Frame frame, Point a, Point b) {
			
			final Color temp = frame.getColor();
			final float line = frame.getLineWidth();;

			frame.setColor(Color.red);
			frame.setLineWidth(3);
			
			final Point middle = Point.getMiddle(a, b);
			
			frame.drawLine(a, middle);
			

			frame.setLineWidth(line);
			frame.setColor(temp);
			
		}
	};

	public final ModelLinkState model;

	private SlickLinkState(ModelLinkState model) {
		this.model = model;
	}

	public abstract void draw(Frame frame, Point a, Point b);

}
