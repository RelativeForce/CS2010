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

	private Window window;

	private final SlickBoard board;

	private final int screenWidth;
	private final int screenHeight;

	private static final int WIDTH = 400;
	private static final int HEIGHT = 250;

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

		reScale();

	}

	public void reScale() {

		final float scaleWidth = (float) screenWidth / board.getWidth();
		final float scaleHeight = (float) screenHeight / board.getHeight();

		// The relative dimensions of the window based on the size of the map compared
		// to the size of the screen.
		final int windowWidth = (int) (scaleWidth * WIDTH);
		final int windowHeight = (int) (scaleHeight * HEIGHT);

		this.window = new Window(windowWidth, windowHeight);

		repositionWindow();

	}

	@Override
	public void init() {

	}

	@Override
	public void draw(Frame frame) {
		frame.draw(board.getImage().getScaledCopy(WIDTH, HEIGHT), getPosition().x, getPosition().y);
		frame.setLineWidth(5f); //
		frame.setColor(Color.red);
		frame.drawRect(getPosition().x, getPosition().y, getWidth(), getHeight());

		frame.draw(window, new EventListener() {

			@Override
			public void mouseHover(Point mouse, int delta) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {

				repositionWindow(mouse);

			}

			@Override
			public void draw(Frame frame) {
				window.draw(frame);

			}

			@Override
			public void buttonPress(int key, Point mouse) {
				// Do nothing

			}
		});
	}

	private void repositionWindow(Point click) {

		int windowX = (click.x - (window.getWidth() / 2));
		int windowY = (click.y - (window.getHeight() / 2));

		final int minX = getPosition().x;
		final int maxX = minX + WIDTH - window.getWidth();

		windowX = windowX < minX ? minX : windowX;
		windowX = windowX > maxX ? maxX : windowX;

		final int minY = getPosition().y;
		final int maxY = minY + HEIGHT - window.getHeight();

		windowY = windowY < minY ? minY : windowY;
		windowY = windowY > maxY ? maxY : windowY;

		final float scaleWidth = (float) board.getWidth() / WIDTH;
		final float scaleHeight = (float) board.getHeight() / HEIGHT;

		final int boardVectorX = (int) (((float) (window.getPosition().x - windowX)) * scaleWidth);
		final int boardVectorY = (int) (((float) (window.getPosition().y - windowY)) * scaleHeight);

		board.move(new Point(boardVectorX, boardVectorY), screenWidth, screenHeight);

		window.setPosition(new Point(windowX, windowY));

	}

	public void repositionWindow() {

		final float scaleWidth = (float) WIDTH / board.getWidth();
		final float scaleHeight = (float) HEIGHT / board.getHeight();

		final int x = getPosition().x + (int) ((float) (0 - board.getPosition().x) * scaleWidth);
		final int y = getPosition().y + (int) ((float) (0 - board.getPosition().y) * scaleHeight);

		window.setPosition(new Point(x, y));

	}

	/**
	 * 
	 * @param click
	 */
	public void parseClick(Point click) {
		repositionWindow(click);
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
			frame.drawRect(Window.this.getPosition().x, Window.this.getPosition().y, Window.this.getWidth(),
					Window.this.getHeight());
		}

	}

}
