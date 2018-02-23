package peril.views.slick.components;

import org.newdawn.slick.Color;
import peril.model.board.ModelBoard;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickBoard;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * This class models a MiniMap that will display the entirety of the
 * {@link ModelBoard} and will allow the user to move around their view of the
 * {@link ModelBoard}.
 * 
 * @author Ezekiel_Trinidad, Joshua_Eddy
 * 
 * @since 2018-02-23
 * @version 1.01.01
 * 
 * @see Clickable
 * @see Component
 * @see Window
 *
 */
public final class MiniMap extends Clickable implements Component {

	/**
	 * The width of the {@link MiniMap}.
	 */
	private static final int WIDTH = 400;

	/**
	 * The height of the {@link MiniMap}.
	 */
	private static final int HEIGHT = 250;

	/**
	 * The {@link SlickBoard} that the {@link MiniMap} navigates.
	 */
	private final SlickBoard board;

	/**
	 * The width of the screen.
	 */
	private final int screenWidth;

	/**
	 * The height of the screen.
	 */
	private final int screenHeight;

	/**
	 * The {@link Window} that appears over the {@link MiniMap}.
	 */
	private Window window;

	/**
	 * Constructs a MiniMap
	 * 
	 * @param board
	 *            The {@link SlickBoard} that the {@link MiniMap} navigates.
	 * @param screenWidth
	 *            The width of the screen.
	 * @param screenHeight
	 *            The height of the screen.
	 */
	public MiniMap(SlickBoard board, int screenWidth, int screenHeight) {
		super(new Region(WIDTH, HEIGHT, new Point(screenWidth - WIDTH, 0)));

		this.board = board;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		reScale();

	}

	/**
	 * Resizes the {@link Window} based on the size of the board.
	 */
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

	/**
	 * Initialises this {@link MiniMap}.
	 */
	@Override
	public void init() {

	}

	/**
	 * Draws this {@link MiniMap} on screen.
	 */
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
				parseClick(mouse);
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

	/**
	 * Repositions the {@link Window} and {@link SlickBoard} on this {@link MiniMap}
	 * based on the specified mouse click.
	 * 
	 * @param click
	 *            The position of the mouse click.
	 */
	public void parseClick(Point click) {

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

	/**
	 * Repositions the {@link MiniMap} based on the position of the
	 * {@link SlickBoard}.
	 */
	public void repositionWindow() {

		final float scaleWidth = (float) WIDTH / board.getWidth();
		final float scaleHeight = (float) HEIGHT / board.getHeight();

		final int x = getPosition().x + (int) ((float) (0 - board.getPosition().x) * scaleWidth);
		final int y = getPosition().y + (int) ((float) (0 - board.getPosition().y) * scaleHeight);

		window.setPosition(new Point(x, y));

	}

	/**
	 * Models a window that pans around the {@link MiniMap}.
	 * 
	 * @author Ezekiel_Trinidad, Joshua_Eddy
	 * 
	 * @since 2018-02-23
	 * @version 1.01.01
	 * 
	 * @see Clickable
	 * @see Component
	 *
	 */
	private final class Window extends Clickable {

		/**
		 * Constructs a new {@link Window}.
		 * 
		 * @param width
		 *            The width of the {@link Window}.
		 * @param height
		 *            The height of the {@link Window}.
		 */
		public Window(int width, int height) {
			super(new Region(width, height, new Point(0, 0)));
		}

		/**
		 * Draws this {@link Window} on screen.
		 * 
		 * @param frame
		 *            The {@link Frame} that draws the {@link Window}.
		 */
		public void draw(Frame frame) {
			frame.setLineWidth(3);
			frame.drawRect(Window.this.getPosition().x, Window.this.getPosition().y, Window.this.getWidth(),
					Window.this.getHeight());
		}

	}

}
