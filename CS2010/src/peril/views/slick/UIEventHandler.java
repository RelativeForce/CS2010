package peril.views.slick;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;

/**
 * Handles all the input interactions that the user will cause using the
 * {@link SlickGame}.
 * 
 * @author Joshua_Eddy, Adrian_Wong
 * 
 * @since 2018-02-16
 * @version 1.01.01
 *
 * @see MouseListener
 * @see KeyListener
 */
public final class UIEventHandler implements MouseListener, KeyListener {

	/**
	 * The instance of the {@link SlickGame} that this {@link UIEventHandler}
	 * assists.
	 */
	private SlickGame game;

	/**
	 * Constructs an new {@link UIEventHandler};
	 * 
	 * @param game
	 *            The {@link SlickGame} this {@link UIEventHandler} will handle
	 *            events for.
	 */
	public UIEventHandler(SlickGame game) {
		this.game = game;
	}

	@Override
	public void inputEnded() {
		// Do nothing
	}

	@Override
	public void inputStarted() {
		// Do Nothing
	}

	@Override
	public boolean isAcceptingInput() {

		// The game should always accept input.
		return true;
	}

	@Override
	public void setInput(Input input) {
		// Do nothing
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		// Do nothing on initial mouse click.
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		// Do nothing
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {

		// The current state will process the mouse movement.
		game.getCurrentState().parseMouse(new Point(newx, newy));

	}

	@Override
	public void mousePressed(int button, int x, int y) {

		// The current state will process the mouse press and release.
		game.getCurrentState().click(new Point(x, y), button);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		// Do nothing
	}

	@Override
	public void mouseWheelMoved(int change) {

		// If the mouse wheel was moved forward then that is UP otherwise it is down.
		final int key = change > 0 ? Input.KEY_UP : Input.KEY_DOWN;

		// Process the mouse move as a button press.
		buttonPress(key);

	}

	@Override
	public void keyPressed(int key, char c) {

		// Process the key press.
		buttonPress(key);
	}

	@Override
	public void keyReleased(int key, char c) {
		// Do nothing
	}

	/**
	 * The current game state will process the specified {@link Input} key.
	 * 
	 * @param key
	 *            The {@link Input} key to be processed.
	 */
	private void buttonPress(int key) {

		Input input = game.getContainer().getInput();
		game.getCurrentState().parseButton(key, new Point(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()));

	}

}
