package peril.views.slick;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import peril.model.board.ModelBoard;
import peril.views.slick.board.SlickBoard;
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

	private final SlickBoard board;

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
	public MiniMap(SlickBoard board, int screenWidth, int screenHeight) {
		super(new Region(WIDTH, HEIGHT, new Point(screenWidth - WIDTH, 0)));

		this.board = board;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		final float scaleWidth = (float) board.getWidth() / screenWidth;
		final float scaleHeight = (float) board.getHeight() / screenHeight;

		// The relative dimensions of the window based on the size of the map compared
		// to the size of the screen.
		final int windowWidth = (int) (scaleWidth * WIDTH);
		final int windowHeight = (int) (scaleHeight * HEIGHT);

		this.window = new Window(windowWidth, windowHeight);
		
	}

	@Override
	public void init() {

	}

	@Override
	public void draw(Frame frame) {
		frame.draw(board.getImage(), getPosition().x, getPosition().y);
		frame.setLineWidth(5f); // thicc
		frame.setColor(Color.red);
		frame.drawRect(getPosition().x, getPosition().y, getWidth(), getHeight());
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
		public void draw(Frame frame) {
			frame.setLineWidth(3);
			frame.drawRect(getPosition().x, getPosition().y, getWidth(), getHeight());
		}

	}

}
