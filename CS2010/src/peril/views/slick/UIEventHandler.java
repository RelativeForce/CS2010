package peril.views.slick;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;

import peril.Game;
import peril.views.slick.states.InteractiveState;
import peril.views.slick.states.gameStates.CoreGameState;

/**
 * Handles all the interactions that the user will cause using the
 * {@link UserInterface}.
 * 
 * @author Joshua_Eddy, Adrian_Wong
 *
 * @see MouseListener
 */
public final class UIEventHandler implements MouseListener, KeyListener {

	/**
	 * The instance of the {@link Game} that this {@link UIEventHandler} assists.
	 */
	private SlickGame game;

	/**
	 * Constructs an new {@link UIEventHandler};
	 * 
	 * @param game
	 *            {@link UIEventHandler#game}
	 */
	public UIEventHandler(SlickGame game) {
		this.game = game;
	}

	@Override
	public void inputEnded() {

	}

	@Override
	public void inputStarted() {

	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void setInput(Input input) {

	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {

	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {

	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		
		InteractiveState state = game.getCurrentState();
		
		if (state instanceof CoreGameState) {
			((CoreGameState) state).parseMouse(new Point(newx, newy));
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		game.getCurrentState().click(new Point(x, y), button);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {

	}

	@Override
	public void mouseWheelMoved(int change) {
		
		int key = change > 0 ? Input.KEY_UP : Input.KEY_DOWN;
		
		buttonPress(key);
		
	}

	@Override
	public void keyPressed(int key, char c) {
		buttonPress(key);
	}

	@Override
	public void keyReleased(int key, char c) {
	}
	
	private void buttonPress(int key) {
		
		Input input = game.getContainer().getInput();
		game.getCurrentState().parseButton(key, new Point(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()));
		
	}

}
