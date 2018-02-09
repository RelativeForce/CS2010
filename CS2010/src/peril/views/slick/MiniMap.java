package peril.views.slick;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import peril.model.board.ModelBoard;
import peril.views.slick.components.Component;

/**
 * This class models a MiniMap that will display the entirety of the
 * {@link ModelBoard} and will allow the user to move around their view of the
 * {@link ModelBoard}.
 * 
 * @author Ezekiel_Trinidad
 *
 */
public class MiniMap extends Clickable implements Component {

	private final Window window;

	private final Image map;

	private final int screenWidth;
	private final int screenHeight;

	private static final int WIDTH = 400;
	private static final int HEIGHT = 300;

	/**
	 * Constructs a MiniMap
	 * 
	 * @param region
	 *            {@link Region}
	 */
	public MiniMap(Image map, int screenWidth, int screenHeight) {
		super(new Region(map.getScaledCopy(WIDTH, HEIGHT)));

		this.map = map.getScaledCopy(WIDTH, HEIGHT);

		final float scaleWidth = (float) map.getWidth() / screenWidth;
		final float scaleHeight = (float) map.getHeight() / screenHeight;

		// The relative dimensions of the window based on the size of the map compared
		// to the size of the screen.
		final int windowWidth = (int) (scaleWidth * WIDTH);
		final int windowHeight = (int) (scaleHeight * HEIGHT);

		this.window = new Window(windowWidth, windowHeight);
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void init() {

	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(map, getPosition().x, getPosition().y);
		g.setLineWidth(5f); //thicc
		g.setColor(Color.red);
		g.drawRect(getPosition().x, getPosition().y, getWidth(), getHeight());
	}

	/**
	 * 
	 * @param click
	 */
	public void parseClick(Point click) {

	}

	/**
	 * Models a window that pans around the {@link MiniMap}.
	 * 
	 * @author Ezekiel_Trinidad
	 *
	 */
	private class Window extends Clickable implements Component {

		private Window(int width, int height) {
			super(new Region(width, height, new Point(0, 0)));

		}

		@Override
		public void init() {
			// TODO Auto-generated method stub

		}

		@Override
		public void draw(Graphics g) {
			// TODO Auto-generated method stub

		}

	}

}
