package peril.ui;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;

import peril.Game;
import peril.Point;
import peril.ui.states.InteractiveState;
import peril.ui.states.gameStates.CoreGameState;

/**
 * Handles all the interactions that the user will cause using the
 * {@link UserInterface}.
 * 
 * @author Joshua_Eddy, Adrian_Wong
 *
 * @see MouseListener
 */
public class UIEventHandler implements MouseListener, KeyListener {

	/**
	 * The instance of the {@link Game} that this {@link UIEventHandler} assists.
	 */
	private Game game;

	/**
	 * Constructs an new {@link UIEventHandler};
	 * 
	 * @param game
	 *            {@link UIEventHandler#game}
	 */
	public UIEventHandler(Game game) {
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
			((CoreGameState) state).pan(new Point(newx, newy));
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		game.getCurrentState().parseClick(button, new Point(x, y));
	}

	@Override
	public void mouseReleased(int button, int x, int y) {

	}

	@Override
	public void mouseWheelMoved(int change) {
	}

	@Override
	public void keyPressed(int key, char c) {
		Input input = game.getContainer().getInput();
		game.getCurrentState().parseButton(key, c, new Point(input.getAbsoluteMouseX(), input.getAbsoluteMouseY()));
	}

	@Override
	public void keyReleased(int key, char c) {
	}

}
