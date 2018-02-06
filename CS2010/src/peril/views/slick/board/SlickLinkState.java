package peril.views.slick.board;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.model.board.links.ModelLinkState;
import peril.views.slick.Point;

public enum SlickLinkState {

	OPEN(ModelLinkState.OPEN) {

		@Override
		public void draw(Graphics g, Point a, Point b) {

			final Color temp = g.getColor();
			final float line = g.getLineWidth();

			g.setColor(Color.green);
			g.setLineWidth(3);

			final Point middle = Point.getMiddle(a, b);
			
			g.drawLine(a.x, a.y, middle.x, middle.y);
			

			g.setLineWidth(line);
			g.setColor(temp);
			
		}
	},BLOCKADE(ModelLinkState.BLOCKADE) {
		
		@Override
		public void draw(Graphics g, Point a, Point b) {
			
			final Color temp = g.getColor();
			final float line = g.getLineWidth();

			g.setColor(Color.red);
			g.setLineWidth(3);

			final Point middle = Point.getMiddle(a, b);
			
			g.drawLine(a.x, a.y, middle.x, middle.y);
			

			g.setLineWidth(line);
			g.setColor(temp);
			
		}
	};

	public final ModelLinkState model;

	private SlickLinkState(ModelLinkState model) {
		this.model = model;
	}

	public abstract void draw(Graphics g, Point a, Point b);

}
